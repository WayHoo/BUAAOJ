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
     * 教职工编号
     */
    private String teacherNumber;

    /**
     * 教师账户密码
     */
    private String password;

    /**
     * 教师所属院系ID
     */
    private int departmentId;

    /**
     * 教师是否通过系统管理员审核（true - 允许开课，false - 不允许开课）
     */
    private boolean isAccept;

    /**
     * 教师的个人简介
     */
    private String introduction;

    /**
     * 教师登录验证码
     */
    private String verifyCode;

    /**
     * 教师的无参构造方法
     */
    public Teacher() { }

    public Teacher(int teacherId, String email, String teacherName, String teacherNumber, String password, int departmentId, boolean isAccept, String introduction, String verifyCode) {
        this.teacherId = teacherId;
        this.email = email;
        this.teacherName = teacherName;
        this.teacherNumber = teacherNumber;
        this.password = password;
        this.departmentId = departmentId;
        this.isAccept = isAccept;
        this.introduction = introduction;
        this.verifyCode = verifyCode;
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

    public String getTeacherNumber() {
        return teacherNumber;
    }

    public void setTeacherNumber(String teacherNumber) {
        this.teacherNumber = teacherNumber;
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

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
