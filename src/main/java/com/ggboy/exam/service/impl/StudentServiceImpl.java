package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.PaperInfoResponse;
import com.ggboy.exam.beans.StuCourseInfoResponse;
import com.ggboy.exam.beans.TeaCourseLinkResponse;
import com.ggboy.exam.beans.exam.*;
import com.ggboy.exam.beans.itemBank.*;
import com.ggboy.exam.beans.vo.UpdateUserVo;
import com.ggboy.exam.common.ExamEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.*;
import com.ggboy.exam.dao.itemBank.*;
import com.ggboy.exam.service.StudentService;
import com.ggboy.exam.utils.MD5Util;
import com.ggboy.exam.utils.RedisUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StuDao stuDao;

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

    @Resource
    private RedisUtils redisUtils;

    @Override
    public ResultResponse searchCourseAndTeacher(String stuId,Integer pageNum,Integer pageSize) {
        List<Subject> count = stuSpecialtyLinkDao.selectSubjectsByStuId(stuId);
        PageHelper.startPage(pageNum,pageSize);
        List<Subject> subjects = stuSpecialtyLinkDao.selectSubjectsByStuId(stuId);
        if (subjects.size() == 0){
            return ResultResponse.fail("当前学院课程为0，请联系管理员处理");
        }
        List<TeaCourseLinkResponse> teaCourseLinkResponses = new ArrayList<>();
        subjects.forEach(subject -> {
            List<User> users = selectTeacher(subject.getCourseId());
            TeaCourseLinkResponse teaCourseLinkResponse = new TeaCourseLinkResponse();
            teaCourseLinkResponse.setUsers(users);
            teaCourseLinkResponse.setSubject(subject);
            teaCourseLinkResponses.add(teaCourseLinkResponse);
        });
        PageInfo<TeaCourseLinkResponse> pageInfo = new PageInfo<>(teaCourseLinkResponses);
        pageInfo.setTotal(count.size());
        return ResultResponse.success(pageInfo);
    }

    @Override
    public ResultResponse sendApply(String userId, String courseId, Integer teaId) {
        TeaAccess teaAccess = new TeaAccess(courseId,userId,teaId);
        teaAccessDao.insert(teaAccess);
        return ResultResponse.success("申请成功，等待教师审核");
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
                .andEqualTo("stuId", userId).andEqualTo("deleteFlag",1)).build();
        List<StuTeaCourseLink> stuTeaCourseLinks = stuTeaCourseLinkDao.selectByExample(example);
        if (stuTeaCourseLinks.size() == 0){
            return ResultResponse.fail("当前用户无课程");
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
                stuCourseInfoResponse.setExamInfos(examInfos);
            }
            stuCourseInfoResponse.setTeaName(userDao.selectUserNameById(stuTeaCourseLink.getTeaId()));
            stuCourseInfoResponse.setSubject(subject);

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

    @Override
    public ResultResponse queryStu(String userId) {
        Example example = Example.builder(StuInfo.class).andWhere(Sqls.custom()
                .andEqualTo("status", 1)
                .andEqualTo("id", userId)).build();
        StuInfo stuInfo = stuDao.selectOneByExample(example);
        return ResultResponse.success(stuInfo);
    }

    @Override
    public ResultResponse checkPass(String password,String userId) {
        StuInfo stuInfo = stuDao.selectByPrimaryKey(userId);
        String s = MD5Util.MD5(password.toUpperCase(), stuInfo.getSalt());
        if (s.toUpperCase().equals(stuInfo.getPassword())){
            return ResultResponse.success();
        }
        return ResultResponse.fail();
    }

    @Override
    public ResultResponse updateUser(UpdateUserVo updateUserVo, String userId) {
        StuInfo stuInfo = stuDao.selectByPrimaryKey(userId);
        stuInfo.setUsername(updateUserVo.getName());
        stuInfo.setPhone(updateUserVo.getUsername());
        String salt = MD5Util.randomNum();
        String newPass = MD5Util.MD5(updateUserVo.getPassword(), salt);
        stuInfo.setSalt(salt);
        stuInfo.setPassword(newPass);
        stuDao.updateByPrimaryKey(stuInfo);
        return ResultResponse.success();
    }

    @Override
    public ResultResponse uploadHead(MultipartFile file, String userId) {
        String path = "D:/我的文档/Documents/HBuilderProjects/exam/static/img/head/"+userId;
        String originalFilename = file.getOriginalFilename();

        assert originalFilename != null;
        String[] split = originalFilename.split("\\.");
        String fileName = System.currentTimeMillis() + userId +"."+split[1];
        byte[] bytes;
        try {
            bytes = file.getBytes();
            uploadPic(bytes,path,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultResponse.fail(e.getMessage());
        }
        StuInfo stuInfo = new StuInfo();
        stuInfo.setId(userId);
        stuInfo.setImgUrl(path+"/"+fileName);
        stuDao.updateByPrimaryKeySelective(stuInfo);
        return ResultResponse.success();
    }

    @Override
    public ResultResponse getAlarm(String userId) {
        String msg = (String) redisUtils.get(userId+"_alarm");
        if (msg == null){
            return ResultResponse.success();
        }
        return ResultResponse.success(msg);
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

    /**
     * @Author qiang
     * @Description //TODO 图片上传
     * @Date 14:47 2020/12/30
     * @Param [file, filePath, fileName]
     * @return void
     */
    private void uploadPic(byte[] file,String filePath,String fileName) throws IOException {
        File targetFile = new File(filePath);
        if (!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(filePath+"/"+fileName);
        fileOutputStream.write(file);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
