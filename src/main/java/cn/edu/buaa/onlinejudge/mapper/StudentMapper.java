package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMapper {
    Student getStudentById(long studentId);
    Student getStudentByEmail(String email);
    void insertStudent(Student student);
    void updateStudent(Student student);
    void resetPassword(Student student);
}
