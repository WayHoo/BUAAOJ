package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.*;
import cn.edu.buaa.onlinejudge.model.Course;
import cn.edu.buaa.onlinejudge.model.CourseStudentRelationship;
import cn.edu.buaa.onlinejudge.model.Student;
import cn.edu.buaa.onlinejudge.utils.FileUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    private final CourseMapper courseMapper;

    private final ContestService contestService;

    private final ProblemMapper problemMapper;

    private final CourseStudentRelationshipMapper courseStudentRelationshipMapper;

    private final StudentMapper studentMapper;

    public CourseService(CourseMapper courseMapper,
                         ContestService contestService, ProblemMapper problemMapper,
                         CourseStudentRelationshipMapper courseStudentRelationshipMapper,
                         StudentMapper studentMapper) {
        this.courseMapper = courseMapper;
        this.contestService = contestService;
        this.problemMapper = problemMapper;
        this.courseStudentRelationshipMapper = courseStudentRelationshipMapper;
        this.studentMapper = studentMapper;
    }

    /**
     * 获取所有课程
     * @return 课程对象列表
     */
    public List<Course> getAllCourses() {
        return courseMapper.getAllCourses();
    }

    /**
     * 根据课程名字进行模糊查询，前缀精确匹配模式
     * 例如：“2019级”可匹配“2019级”、“2019级-数据结构”、“2019级-Python编程”等
     * @param courseName - 课程名称
     * @return Course对象列表
     */
    public List<Course> fuzzyQueryCoursesByCourseName(String courseName){
        return courseMapper.fuzzyQueryCoursesByCourseName(courseName);
    }

    public List<Course> getCoursesOfTeacher(long teacherId){
        return courseMapper.getAllCoursesByTeacherId(teacherId);
    }

    public List<Course> fuzzyQueryCoursesOfTeacherByCourseName(long teacherId, String courseName){
        return courseMapper.fuzzyQueryCoursesOfTeacherByCourseName(teacherId, courseName);
    }

    public Course getCourseById(int courseId) {
        return courseMapper.getCourseById(courseId);
    }

    /**
     * 学生加入课程的状态
     * @param studentId - 学生ID
     * @param courseId - 课程ID
     * @return 0 - 申请尚未通过，1 - 已通过，-1 - 未提交申请
     */
    public int studentJoinCourseStatus(long studentId, int courseId) {
        CourseStudentRelationship relationship =
                courseStudentRelationshipMapper.getCourseStudentRelationship(studentId,courseId);
        if ( relationship == null ){
            return -1;
        } else {
            return relationship.isStudentAccept() ? 1 : 0;
        }
    }

    public void joinCourse(long studentId, int courseId) {
        CourseStudentRelationship relationship =
                courseStudentRelationshipMapper.getCourseStudentRelationship(studentId, courseId);
        if (relationship != null ) {
            return ;
        }
        CourseStudentRelationship newRelationship = new CourseStudentRelationship(courseId, studentId);
        courseStudentRelationshipMapper.insertCourseStudentRelationship(newRelationship);
    }

    /**
     * 获取课程审核通过或未通过的成员
     * @param courseId - 课程ID
     * @param tag - 选择标签，tag = 0表示获取审核未通过的成员，tag = 1表示获取审核通过的成员
     * @return
     */
    public List<Map<String,Object>> getCourseMembers(int courseId, int tag) {
        if( tag != 0 && tag != 1 ){
            return null;
        }
        List<Map<String,Object>> memberList = new ArrayList<>();
        List<CourseStudentRelationship> relationshipList =
                courseStudentRelationshipMapper.getCourseMembers(courseId,tag);
        for (CourseStudentRelationship relationship : relationshipList) {
            Map<String,Object> metadata = new HashMap<>();
            metadata.put("studentId",relationship.getStudentId());
            metadata.put("studentRole",relationship.getStudentRole());
            Student student = studentMapper.getStudentById(relationship.getStudentId());
            metadata.put("studentName",student.getUserName());
            metadata.put("studentNumber",student.getUserNumber());
            memberList.add(metadata);
        }
        return memberList;
    }

    public List<Integer> getStudentJoinedCourseIdList(long studentId) {
        return courseStudentRelationshipMapper.getStudentJoinedCourseIdList(studentId);
    }

    public void insertCourse(Course course){
        courseMapper.insertCourse(course);
    }

    public void updateCourse(Course course){
        courseMapper.updateCourse(course);
    }

    /**
     * 判断课程是否有课件供下载
     * @param courseId - 课程ID
     * @return
     */
    public boolean isCoursewareExists(int courseId){
        String filePath = FileUtil.getCoursewareUploadPath(Integer.toString(courseId));
        //每个课程的课件被上传之后会自动将该课程的所有课件打包为courseware.zip存放在该课程根目录下
        File file = new File(filePath + "courseware.zip");
        return file.exists();
    }

    public void updateCourseMemberRole(int courseId, long studentId, int role){
        courseStudentRelationshipMapper.updateCourseMemberRole(courseId, studentId, role);
    }

    /**
     * 反转学生加入课程的申请状态
     * @param courseId - 课程ID
     * @param studentId - 学生ID
     */
    public void revertStudentAcceptance(int courseId, long studentId){
        CourseStudentRelationship relationship =
                courseStudentRelationshipMapper.getCourseStudentRelationship(studentId, courseId);
        //新的状态与当前状态相反
        int newAcceptance = relationship.isStudentAccept() ? 0 : 1;
        courseStudentRelationshipMapper.setStudentAcceptance(courseId, studentId, newAcceptance);
    }

    /**
     * 删除课程，并删除课程的所有课件
     * @param courseId - 课程ID
     */
    public void deleteCourse(int courseId){
        //在删除课程之前获取课程中所有题目ID
        List<Integer> contestIdList = contestService.getAllContestIdOfCourse(courseId);
        List<Long> problemIdList = new ArrayList<>();
        for (Integer contestId : contestIdList) {
            problemIdList.addAll(problemMapper.getAllProblemIdOfContest(contestId));
        }
        //删除课程，以及课程下所有竞赛和题目
        courseMapper.deleteCourse(courseId);
        //删除课程的所有课件
        String filePath = FileUtil.getCoursewareUploadPath(Integer.toString(courseId));
        FileUtil.delete(filePath);
        //删除课程下所有题目的测试点文件
        for (Long problemId : problemIdList) {
            String checkpointFileAbsolutePath =
                    FileUtil.getCheckpointUploadPath(Long.toString(problemId));
            //删除题目测试点文件夹及文件夹下所有文件
            FileUtil.delete(checkpointFileAbsolutePath);
        }
    }
}