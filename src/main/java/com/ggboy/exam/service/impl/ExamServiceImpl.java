package com.ggboy.exam.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ggboy.exam.beans.ExamSearchCondition;
import com.ggboy.exam.beans.PaperInfoResponse;
import com.ggboy.exam.beans.PaperListResponse;
import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.beans.exam.StuExamAnsw;
import com.ggboy.exam.beans.exam.StuTeaCourseLink;
import com.ggboy.exam.beans.itemBank.*;
import com.ggboy.exam.common.ExamEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.ExamDao;
import com.ggboy.exam.dao.exam.StuExamAnswDao;
import com.ggboy.exam.dao.exam.StuTeaCourseLinkDao;
import com.ggboy.exam.dao.itemBank.*;
import com.ggboy.exam.service.ExamService;
import com.ggboy.exam.utils.RedisUtils;
import com.ggboy.exam.utils.UnixUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    @Resource
    private ExamDao examDao;

    @Resource
    private PaperDao paperDao;

    @Resource
    private ChoiceDao choiceDao;

    @Resource
    private TOFDao tofDao;

    @Resource
    private DesignDao designDao;

    @Resource
    private BigDao bigDao;

    @Resource
    private CourseDao courseDao;

    @Resource
    private UserDao userDao;

    @Resource
    private StuTeaCourseLinkDao stuTeaCourseLinkDao;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private StuExamAnswDao stuExamAnswDao;

    @Resource
    private QstTypeDao qstTypeDao;

    @Override
    public ResultResponse addExam(JSONObject examInfo,Integer userId) {
        ExamInfo examInfo1 = examInfo.toJavaObject(ExamInfo.class);
        examInfo1.setTeacherId(userId);
        examDao.insert(examInfo1);
        return ResultResponse.success();
    }

    @Override
    public ResultResponse searchPaper(Integer paperId) {
        Paper paper = new Paper();
        paper.setPaperId(paperId);
        Paper paper1 = paperDao.selectOne(paper);
        if (paper1 != null){
            PaperInfoResponse paperInfoResponse = searchPaperDetails(paper1);
            return ResultResponse.success(paperInfoResponse);
        }
        return ResultResponse.fail("未查询到试卷信息");
    }

    @Transactional
    @Override
    public ResultResponse listPaper(String courseId,Integer userId,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Paper> papers = paperDao.selectPaperByCourseAndUserId(courseId, userId);
        Example example = Example.builder(Paper.class).andWhere(Sqls.custom()
                .andEqualTo("paperCourse", courseId)
                .andEqualTo("paperTeacher", userId)).build();
        int count = paperDao.selectCountByExample(example);
        if (papers.size() == 0){
            return ResultResponse.fail("未找到试卷信息");
        }
        List<PaperListResponse> paperListResponses = new ArrayList<>();

        papers.forEach(paper -> {
            PaperListResponse paperListResponse = new PaperListResponse();
            paperListResponse.setPaperId(paper.getPaperId());
            paperListResponse.setPaperMadeDate(paper.getPaperMadeDate());
            paperListResponse.setPaperLevel(paper.getPaperLevel());

            String courseName = courseDao.selectNameById(paper.getPaperCourse());
            String userName = userDao.selectUserNameById(paper.getPaperTeacher());

            paperListResponse.setPaperCourse(courseName);
            paperListResponse.setPaperTeacher(userName);

            paperListResponses.add(paperListResponse);
        });
        PageInfo<PaperListResponse> pageInfo = new PageInfo<>(paperListResponses);
        pageInfo.setTotal(count);
        return ResultResponse.success(pageInfo);
    }

    @Override
    public ResultResponse listExam(ExamSearchCondition examSearchCondition,Integer userId) {
        Sqls custom = Sqls.custom();

        if (examSearchCondition.getStartTime() != null){
            custom.andGreaterThanOrEqualTo("startTime",examSearchCondition.getStartTime());
        }
        if (examSearchCondition.getEndTime() != null){
            custom.andLessThanOrEqualTo("endTime",examSearchCondition.getEndTime());
        }
        if (examSearchCondition.getCourseName() != null && !"".equals(examSearchCondition.getCourseName())){
            Example example = Example.builder(Subject.class).andWhere(Sqls.custom()
                    .andLike("courseName", examSearchCondition.getCourseName())).build();
            Subject subject = courseDao.selectOneByExample(example);
            custom.andEqualTo("courseId",subject.getCourseId());
        }
        if (examSearchCondition.getPaperId() != null && !"".equals(examSearchCondition.getPaperId())){
            custom.andEqualTo("paperId",examSearchCondition.getPaperId());
        }
        if (examSearchCondition.getStatus() != null && !"".equals(examSearchCondition.getStatus())){
            custom.andEqualTo("status",examSearchCondition.getStatus());
        }

        Example example = Example.builder(ExamInfo.class).andWhere(custom).build();
        PageHelper.startPage(examSearchCondition.getPageNum(),examSearchCondition.getPageSize());
        List<ExamInfo> examInfos = examDao.selectByExample(example);
        examInfos.forEach(examInfo -> {
            String courseId = examInfo.getCourseId();
            Subject subject = courseDao.selectByPrimaryKey(courseId);
            examInfo.setCourseId(subject.getCourseName());
            if (examInfo.getStatus().equals(ExamEnum.PENDING.getEncode())){
                examInfo.setStatus(ExamEnum.PENDING.getMessage());
            }else if (examInfo.getStatus().equals(ExamEnum.END.getEncode())){
                examInfo.setStatus(ExamEnum.END.getMessage());
            }else {
                examInfo.setStatus(ExamEnum.PROCESSING.getMessage());
            }
        });
        if (examInfos.size() == 0){
            return ResultResponse.fail("当前用户无考试信息");
        }
        PageInfo<ExamInfo> examInfoPageInfo = new PageInfo<>(examInfos);
        return ResultResponse.success(examInfoPageInfo);
    }

    /**
     * @Author qiang
     * @Description //TODO 结束考试
     * @Date 15:32 2020/11/18
     * @Param []
     * @return com.ggboy.exam.common.ResultResponse
     */
    @Transactional
    @Override
    public ResultResponse startExam(Date startTime) {
        Example example = Example.builder(ExamInfo.class).select()
                .andWhere(Sqls.custom()
                        .andEqualTo("status", ExamEnum.PENDING.getEncode())
                        .andLessThanOrEqualTo("startTime", startTime))
                .build();
        List<ExamInfo> examInfos = examDao.selectByExample(example);
        if (examInfos.size() == 0){
            return ResultResponse.success();
        }
        try {
            examInfos.forEach(examInfo -> {
                examInfo.setStatus(ExamEnum.PROCESSING.getEncode());
                examDao.updateByPrimaryKey(examInfo);
            });
        }catch (Exception e){
            e.printStackTrace();
            return ResultResponse.fail("失败，请联系管理员处理");
        }
        return ResultResponse.success();
    }

    /**
     * @Author qiang
     * @Description //TODO 结束考试
     * @Date 16:24 2020/11/18
     * @Param [time]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @Transactional
    @Override
    public ResultResponse endExam(Date endTime) {
        Example example = Example.builder(ExamInfo.class).select()
                .andWhere(Sqls.custom()
                        .andEqualTo("status", ExamEnum.PROCESSING.getEncode())
                        .andLessThanOrEqualTo("endTime", endTime))
                .build();
        List<ExamInfo> examInfos = examDao.selectByExample(example);
        if (examInfos.size() == 0){
            return ResultResponse.success();
        }
        try {
            examInfos.forEach(examInfo -> {
                examInfo.setStatus(ExamEnum.END.getEncode());
                checkAnsw(examInfo);
                examDao.updateByPrimaryKey(examInfo);
            });
        }catch (Exception e){
            e.printStackTrace();
            return ResultResponse.fail("失败，请联系管理员处理");
        }

        return ResultResponse.success();
    }

    @Override
    public ResultResponse alarmExam(String examId) {

        ExamInfo examInfo = examDao.selectByPrimaryKey(examId);
        if (!examInfo.getStatus().equals(ExamEnum.PENDING.getEncode())){
            return ResultResponse.fail("该考试已结束或正在进行中");
        }
        Example example = Example.builder(StuTeaCourseLink.class).andWhere(Sqls.custom()
                .andEqualTo("courseId", examInfo.getCourseId())
                .andEqualTo("teaId", examInfo.getTeacherId())).build();
        Long time = examInfo.getStartTime().getTime()/1000L;
        Long unix = UnixUtils.getUnix();
        List<StuTeaCourseLink> stuTeaCourseLinks = stuTeaCourseLinkDao.selectByExample(example);
        Subject subject = courseDao.selectByPrimaryKey(examInfo.getCourseId());
        stuTeaCourseLinks.forEach(stuTeaCourseLink -> {
            String alarm = (String) redisUtils.get(stuTeaCourseLink.getStuId() + "_alarm");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(examInfo.getStartTime());
            if (alarm == null){
                redisUtils.setex(stuTeaCourseLink.getStuId() + "_alarm",
                        "请于" + format + "准时参加" + subject.getCourseName() + "考试",time - unix);
            }else {
                alarm += ";请于" + format + "准时参加" + subject.getCourseName() + "考试";
                redisUtils.setex(stuTeaCourseLink.getStuId() + "_alarm",alarm,time - unix);
            }
        });

        return ResultResponse.success();
    }

    @Override
    public ResultResponse deleteExam(String examId) {
        examDao.deleteByPrimaryKey(examId);
        return ResultResponse.success();
    }

    @Override
    public ResultResponse hasExam(String user) {
        String exam = (String) redisUtils.get(user+"_exam");
        if (exam == null){
            return ResultResponse.success(false);
        }else {
            return ResultResponse.success(exam);
        }
    }

    @Override
    public ResultResponse examTime(String user) {
        String exam = (String) redisUtils.get(user+"_exam");
        ExamInfo examInfo = examDao.selectByPrimaryKey(exam);
        Date endTime = examInfo.getEndTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(endTime);
        return ResultResponse.success(format);
    }

    @Override
    public ResultResponse submitExamAnsw(Map<String, Object> map,String user) {
        String exam = (String) redisUtils.get(user+"_exam");
        ExamInfo examInfo = examDao.selectByPrimaryKey(exam);
        map.forEach((k,v)->{
            StuExamAnsw stuExamAnsw = new StuExamAnsw();
            stuExamAnsw.setStuId(user);
            stuExamAnsw.setExamId(exam);
            stuExamAnsw.setPaperId(examInfo.getPaperId());
            stuExamAnsw.setQuestionId(k);
            stuExamAnsw.setAnsw(String.valueOf(v));
            stuExamAnswDao.insert(stuExamAnsw);
        });
        return ResultResponse.success();
    }

    @Override
    public ResultResponse isSubmit(String user) {
        String exam = (String) redisUtils.get(user+"_exam");
        Example example = Example.builder(StuExamAnsw.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("stuId", user)
                        .andEqualTo("examId", exam)).build();
        List<StuExamAnsw> stuExamAnsws = stuExamAnswDao.selectByExample(example);
        if (!stuExamAnsws.isEmpty()){
            return ResultResponse.success(true);
        }
        return ResultResponse.success(false);
    }


    /**
     * @Author qiang
     * @Description //TODO 查询试卷详情信息
     * @Date 10:49 2020/11/18
     * @Param [papers]
     * @return com.ggboy.exam.beans.PaperInfoResponse
     */
    private PaperInfoResponse searchPaperDetails(Paper paper){
        List<ChoiceQst> choiceQsts = new ArrayList<>();
        List<TOFQst> tofQsts = new ArrayList<>();
        List<DesignQst> designQsts = new ArrayList<>();
        List<BigQst> bigQsts = new ArrayList<>();

        String[] choiceQuestions = paper.getPaperChoiceInfo().split(",");
        for (String choiceQuestion :
                choiceQuestions) {
            ChoiceQst choiceQst = choiceDao.selectChoiceQstByQuestionId(choiceQuestion);
            choiceQsts.add(choiceQst);
        }

        String[] tofQuestions = paper.getPaperTOFInfo().split(",");
        for (String tofQuestion :
                tofQuestions) {
            TOFQst tofQst = tofDao.selectTOFQstByQuestionId(tofQuestion);
            tofQsts.add(tofQst);
        }

        String[] designQuestions = paper.getPaperDesignInfo().split(",");
        for (String designQuestion:
             designQuestions) {
            DesignQst designQst = designDao.selectDesignQstByQuestionId(designQuestion);
            designQsts.add(designQst);
        }

        String[] bigQuestions = paper.getPaperBigInfo().split(",");
        for (String bigQuestion :
                bigQuestions) {
            BigQst bigQst = bigDao.selectBigQstByQuestionId(bigQuestion);
            bigQsts.add(bigQst);
        }

        PaperInfoResponse paperInfoResponse = new PaperInfoResponse();

        String courseName = courseDao.selectNameById(paper.getPaperCourse());
        String userName = userDao.selectUserNameById(paper.getPaperTeacher());
        paperInfoResponse.setPaperId(paper.getPaperId());
        paperInfoResponse.setPaperCourse(courseName);
        paperInfoResponse.setPaperMadeDate(paper.getPaperMadeDate());
        paperInfoResponse.setPaperTeacher(userName);
        paperInfoResponse.setPaperChoiceInfo(choiceQsts);
        paperInfoResponse.setPaperTOFInfo(tofQsts);
        paperInfoResponse.setPaperDesignInfo(designQsts);
        paperInfoResponse.setPaperBigInfo(bigQsts);
        paperInfoResponse.setPaperLevel(paper.getPaperLevel());

        return paperInfoResponse;
    }

    /**
     * @Author ggboy88
     * @Description //TODO 校对答案
     * @Date 2021/1/21 17:25
     * @Param [examInfo]
     * @return void
     */
    @Async("correctExecutor")
    public void checkAnsw(ExamInfo examInfo){
        Example example = Example.builder(StuExamAnsw.class)
                .andWhere(Sqls.custom().andEqualTo("examId", examInfo.getId())).build();
        List<StuExamAnsw> stuExamAnsws = stuExamAnswDao.selectByExample(example);
        stuExamAnsws.stream().collect(Collectors.groupingBy(StuExamAnsw::getStuId)).forEach((k, v) -> {
            v.forEach(stuExamAnsw -> {
                String questionId = stuExamAnsw.getQuestionId();
                QstType qstType = qstTypeDao.selectByPrimaryKey(questionId);
                if ("选择题".equals(qstType.getQuestionName())){
                    ChoiceQst choiceQst = choiceDao.selectChoiceQstByQuestionId(questionId);
                    if (choiceQst.getChoiceQstAnsw().equals(stuExamAnsw.getAnsw())){
                        stuExamAnsw.setIsCorrect("1");
                    }else {
                        stuExamAnsw.setIsCorrect("0");
                    }
                    stuExamAnswDao.updateByPrimaryKeySelective(stuExamAnsw);
                }
                if ("判断题".equals(qstType.getQuestionName())){
                    TOFQst tofQst = tofDao.selectTOFQstByQuestionId(questionId);
                    if (tofQst.getTOFAnsw().equals(stuExamAnsw.getAnsw())){
                        stuExamAnsw.setIsCorrect("1");
                    }else {
                        stuExamAnsw.setIsCorrect("0");
                    }
                    stuExamAnswDao.updateByPrimaryKeySelective(stuExamAnsw);
                }
            });
        });
    }

}
