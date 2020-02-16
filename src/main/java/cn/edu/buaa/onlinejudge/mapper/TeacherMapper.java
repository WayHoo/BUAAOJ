package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Teacher;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherMapper {

    /**
     * 根据教师ID获取教师对象
     * @param teacherId - 教师ID
     * @return Teacher对象
     */
    Teacher getTeacherById(int teacherId);

    /**
     * 根据邮箱（账号）获取教师对象
     * @param email - 邮箱（账号）
     * @return Teacher对象
     */
    Teacher getTeacherByEmail(String email);

    /**
     * 插入教师对象
     * @param teacher - 教师对象
     */
    void insertTeacher(Teacher teacher);

    /**
     * 更新教师信息
     * @param teacher - 教师对象
     */
    void updateTeacher(Teacher teacher);

    /**
     * 教师更改密码
     * @param teacher - 教师对象
     */
    void resetPassword(Teacher teacher);
}
