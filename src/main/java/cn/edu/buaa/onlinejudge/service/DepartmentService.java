package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.DepartmentMapper;
import cn.edu.buaa.onlinejudge.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    /**
     * 自动注入的DepartmentMapper对象
     */
    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 根据院系ID获取院系对象
     * @param departmentId - 院系的ID
     * @return 院系对象
     */
    public Department getDepartmentById(int departmentId) {
        return departmentMapper.getDepartmentById(departmentId);
    }

    /**
     * 获取支持的院系
     * @return 院系列表(List<Department>对象)
     */
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments();
    }
}
