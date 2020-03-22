package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.TeacherMapper;
import cn.edu.buaa.onlinejudge.model.Teacher;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    public Teacher getTeacherById(long teacherId){
        return teacherMapper.getTeacherById(teacherId);
    }

    public Teacher getTeacherByEmail(String email){
        return teacherMapper.getTeacherByEmail(email);
    }

    public void insertTeacher(Teacher teacher){
        teacherMapper.insertTeacher(teacher);
    }

    public void updateTeacher(Teacher teacher){
        teacherMapper.updateTeacher(teacher);
    }

    public void resetPassword(Teacher teacher){
        teacherMapper.resetPassword(teacher);
    }

    /**
     * 教师是否具有开课权限（包括开展竞赛，创建题目权限）
     * @param teacherId - 教师ID
     * @return
     */
    public boolean isTeacherAccept(long teacherId){
        Teacher teacher = teacherMapper.getTeacherById(teacherId);
        return teacher.isAccept();
    }
}
