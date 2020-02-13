package cn.edu.buaa.onlinejudge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Student {
    /**
     * 学生的唯一标识符
     */
    private long studentId;

    /**
     * 学生邮箱
     */
    private String email;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 学生账号密码
     */
    private String password;

    /**
     * 学生所属院系ID
     */
    private int departmentId;

    /**
     * 学生偏好语言ID，默认为1(Python 3)
     */
    private int preferLanguageId = 1;

    /**
     * 学生个人简介
     */
    private String introduction;

    /**
     * 学生登录验证码
     */
    private String verifyCode;

    /**
     * 学生的无参构造方法
     */
    public Student() { }

    public Student(long studentId, String email, String studentName, String studentNumber, String password, int departmentId, int preferLanguageId, String introduction, String verifyCode) {
        this.studentId = studentId;
        this.email = email;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.password = password;
        this.departmentId = departmentId;
        this.preferLanguageId = preferLanguageId;
        this.introduction = introduction;
        this.verifyCode = verifyCode;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
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

    public int getPreferLanguageId() {
        return preferLanguageId;
    }

    public void setPreferLanguageId(int preferLanguageId) {
        this.preferLanguageId = preferLanguageId;
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

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", email='" + email + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", password='" + password + '\'' +
                ", departmentId=" + departmentId +
                ", preferLanguageId=" + preferLanguageId +
                ", introduction='" + introduction + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
