package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.PaperInfoResponse;
import com.ggboy.exam.beans.StuCourseInfoResponse;
import com.ggboy.exam.beans.TeaCourseLinkResponse;
import com.ggboy.exam.beans.exam.ExamInfo;
import com.ggboy.exam.beans.exam.StuTeaCourseLink;
import com.ggboy.exam.beans.exam.TeaAccess;
import com.ggboy.exam.beans.exam.TeaCourseLink;
import com.ggboy.exam.beans.itemBank.*;
import com.ggboy.exam.common.ExamEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.*;
import com.ggboy.exam.dao.itemBank.*;
import com.ggboy.exam.service.StudentService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StuSpecialtyLinkDao stuSpecialtyLinkDao;

    @Resource
    private TeaCourseLinkDao teaCourseLinkDao;

    @Resource
    private UserDao userDao;

    @Resource
    private TeaAccessDao teaAccessDao;

    @Resource
    private StuTeaCourseLinkDao stuTeaCourseLinkDao;

    @Resource
    private CourseDao courseDao;

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

    @Override
    public ResultResponse searchCourseAndTeacher(String stuId,Integer pageNum,Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Subject> subjects = stuSpecialtyLinkDao.selectSubjectsByStuId(stuId);
        if (subjects.size() == 0){
            return ResultResponse.fail("当前学院课程为0，请联系老师处理");
        }
        List<TeaCourseLinkResponse> teaCourseLinkResponses = new ArrayList<>();
        subjects.forEach(subject -> {
            List<User> users = selectTeacher(subject.getCourseId());
            TeaCourseLinkResponse teaCourseLinkResponse = new TeaCourseLinkResponse();
            teaCourseLinkResponse.setUsers(users);
            teaCourseLinkResponse.setSubject(subject);
            teaCourseLinkResponses.add(teaCourseLinkResponse);
        });
        return ResultResponse.success(teaCourseLinkResponses);
    }

    @Override
    public ResultResponse sendApply(String userId, String courseId, Integer teaId) {
        TeaAccess teaAccess = new TeaAccess(courseId,userId,teaId);
        teaAccessDao.insert(teaAccess);
        return ResultResponse.success("请求发送成功，等待教师审核");
    }

    /**
     * @Author qiang
     * @Description //TODO 查询当前学生所选课程信息
     * @Date 10:15 2020/11/24
     * @Param [userId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @Override
    public ResultResponse selectCourse(String userId) {
        Example example = Example.builder(StuTeaCourseLink.class).andWhere(Sqls.custom()
                .andEqualTo("stuId", userId)).build();
        List<StuTeaCourseLink> stuTeaCourseLinks = stuTeaCourseLinkDao.selectByExample(example);
        if (stuTeaCourseLinks.size() == 0){
            return ResultResponse.fail("当前学生无课程");
        }
        List<StuCourseInfoResponse> stuCourseInfoResponses = new ArrayList<>();
        stuTeaCourseLinks.forEach(stuTeaCourseLink -> {
            StuCourseInfoResponse stuCourseInfoResponse = new StuCourseInfoResponse();
            Subject subject = courseDao.selectByPrimaryKey(stuTeaCourseLink.getCourseId());
            Example example1 = Example.builder(ExamInfo.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo("courseId", subject.getCourseId())
                            .andEqualTo("teacherId", stuTeaCourseLink.getTeaId())
                            .andNotEqualTo("status",ExamEnum.END.getEncode()))
                    .build();
            List<ExamInfo> examInfos = examDao.selectByExample(example1);

            if (examInfos.size() != 0){
                stuCourseInfoResponse.setHasExam(true);
                stuCourseInfoResponse.setTeaName(userDao.selectUserNameById(stuTeaCourseLink.getTeaId()));
                stuCourseInfoResponse.setSubject(subject);
                stuCourseInfoResponse.setExamInfos(examInfos);
            }

            stuCourseInfoResponses.add(stuCourseInfoResponse);

        });
        return ResultResponse.success(stuCourseInfoResponses);
    }

    /**
     * @Author qiang
     * @Description //TODO 开始考试
     * @Date 10:44 2020/11/24
     * @Param [examId]
     * @return com.ggboy.exam.common.ResultResponse
     */
    @Override
    public ResultResponse startExam(String examId) {
        ExamInfo examInfo = examDao.selectByPrimaryKey(examId);
        if (ExamEnum.END.getEncode().equals(examInfo.getStatus())){
            return ResultResponse.fail("该考试已结束");
        }

        if (ExamEnum.PENDING.getEncode().equals(examInfo.getStatus())){
            return ResultResponse.fail("考试未开始");
        }

        Paper paper = paperDao.selectByPrimaryKey(examInfo.getPaperId());
        PaperInfoResponse paperInfoResponse = searchPaperDetails(paper);
        return ResultResponse.success(paperInfoResponse);
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
            choiceQst.setChoiceQstAnsw(null);
            choiceQsts.add(choiceQst);
        }

        String[] tofQuestions = paper.getPaperTOFInfo().split(",");
        for (String tofQuestion :
                tofQuestions) {
            TOFQst tofQst = tofDao.selectTOFQstByQuestionId(tofQuestion);
            tofQst.setTOFAnsw(null);
            tofQsts.add(tofQst);
        }

        String[] designQuestions = paper.getPaperDesignInfo().split(",");
        for (String designQuestion:
                designQuestions) {
            DesignQst designQst = designDao.selectDesignQstByQuestionId(designQuestion);
            designQst.setDesignAnsw(null);
            designQsts.add(designQst);
        }

        String[] bigQuestions = paper.getPaperBigInfo().split(",");
        for (String bigQuestion :
                bigQuestions) {
            BigQst bigQst = bigDao.selectBigQstByQuestionId(bigQuestion);
            bigQst.setBigAnsw(null);
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

    private List<User> selectTeacher(String courseId) {
        Example example = Example.builder(TeaCourseLink.class)
                .andWhere(Sqls.custom().andEqualTo("courseId", courseId)).build();
        List<TeaCourseLink> teaCourseLinks = teaCourseLinkDao.selectByExample(example);
        if (teaCourseLinks.size() == 0){
            return null;
        }
        List<User> users = new ArrayList<>();
        teaCourseLinks.forEach(teaCourseLink -> {
            Example example1 = Example.builder(User.class)
                    .andWhere(Sqls.custom().andEqualTo("id", teaCourseLink.getTeaId())).build();
            User user = userDao.selectOneByExample(example1);
            users.add(user);
        });
        return users;
    }
}
