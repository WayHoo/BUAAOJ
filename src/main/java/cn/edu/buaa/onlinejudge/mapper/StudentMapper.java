package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper {
    /**
     * 根据学生ID获取学生对象
     * @param studentId - 学生ID
     * @return Student对象
     */
    Student getStudentById(long studentId);

    /**
     * 根据邮箱（账号）获取学生对象
     * @param email - 邮箱（账号）
     * @return Student对象
     */
    Student getStudentByEmail(String email);

    /**
     * 插入学生对象
     * @param student - 学生对象
     */
    void insertStudent(Student student);

    /**
     * 更新学生信息
     * @param student - 学生对象
     */
    void updateStudent(Student student);

    /**
     * 更改学生密码
     * @param student - 学生对象
     */
    void resetPassword(Student student);
}
