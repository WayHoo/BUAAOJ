package cn.edu.buaa.onlinejudge.model;

public class Department {
    /**
     * 院系的唯一标识符
     */
    private int departmentId;

    /**
     * 院系名称
     */
    private String departmentName;

    /**
     * 院系的默认构造方法
     */
    public Department() { }

    /**
     * 院系的有参构造方法
     * @param departmentId - 院系的唯一标识符
     * @param departmentName - 院系名称
     */
    public Department(int departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
