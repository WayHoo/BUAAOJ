package cn.edu.buaa.onlinejudge.model;

public class User {
    /**
     * 用户ID
     */
    private long userId;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户编号（学号）
     */
    private String userNumber;

    /**
     * 教师账户密码
     */
    private String password;

    /**
     * 教师所属院系ID
     */
    private int departmentId;

    /**
     * 教师的个人简介
     */
    private String introduction;

    /**
     * 教师登录验证码
     */
    private String verifyCode;

    public User() { }

    public User(long userId, String email, String userName, String userNumber, String password, int departmentId, String introduction, String verifyCode) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.userNumber = userNumber;
        this.password = password;
        this.departmentId = departmentId;
        this.introduction = introduction;
        this.verifyCode = verifyCode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
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
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", password='" + password + '\'' +
                ", departmentId=" + departmentId +
                ", introduction='" + introduction + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
