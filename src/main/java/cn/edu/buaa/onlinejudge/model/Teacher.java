package cn.edu.buaa.onlinejudge.model;

public class Teacher {
    /**
     * 教师唯一标识符
     */
    private int teacherId;

    /**
     * 教师邮箱
     */
    private String email;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 教师账户密码
     */
    private String password;

    /**
     * 教师所属院系ID
     */
    private int departmentId;

    /**
     * 教师的开课权限（true - 允许开课，false - 不允许开课）
     */
    private boolean privilege;

    /**
     * 教师的个人简介
     */
    private String introduction;

    /**
     * 教师的无参构造方法
     */
    public Teacher() { }

    public Teacher(int teacherId, String email, String teacherName, String password, int departmentId, boolean privilege, String introduction) {
        this.teacherId = teacherId;
        this.email = email;
        this.teacherName = teacherName;
        this.password = password;
        this.departmentId = departmentId;
        this.privilege = privilege;
        this.introduction = introduction;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public boolean isPrivilege() {
        return privilege;
    }

    public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", email='" + email + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", password='" + password + '\'' +
                ", departmentId=" + departmentId +
                ", privilege=" + privilege +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
