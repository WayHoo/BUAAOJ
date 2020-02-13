package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.model.Student;
import cn.edu.buaa.onlinejudge.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    public Student getStudentById(long studentId){
        return studentMapper.getStudentById(studentId);
    }

    public Student getStudentByEmail(String email) {
        return studentMapper.getStudentByEmail(email);
    }

    public void insertStudent(Student student){
        studentMapper.insertStudent(student);
    }

    public void updateStudent(Student student){
        studentMapper.updateStudent(student);
    }

    public void resetPassword(Student student) {
        studentMapper.resetPassword(student);
    }
}
