package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.TeacherMapper;
import cn.edu.buaa.onlinejudge.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

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
}
