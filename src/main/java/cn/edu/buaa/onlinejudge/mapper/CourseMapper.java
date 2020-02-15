package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMapper {
    /**
     * 获取所有的课程
     * @return Course对象列表
     */
    List<Course> getAllCourses();

    /**
     * 根据课程ID获取课程对象
     * @param courseId - 课程ID
     * @return Course对象
     */
    Course getCourseById(int courseId);
}
