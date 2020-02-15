package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentMapper {
    /**
     * 根据院系ID获取院系对象
     * @param departmentId - 院系ID
     * @return Department对象
     */
    Department getDepartmentById(int departmentId);

    /**
     * 获取所有的院系对象
     * @return - Department对象列表
     */
    List<Department> getAllDepartments();
}
