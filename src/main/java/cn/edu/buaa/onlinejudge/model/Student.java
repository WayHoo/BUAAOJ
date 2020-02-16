package cn.edu.buaa.onlinejudge.model;

public class Student extends User{

    /**
     * 学生偏好语言ID，默认为1(Python 3)
     */
    private int preferLanguageId = 1;

    /**
     * 学生的无参构造方法
     */
    public Student() {
        super();
    }

    /**
     * 通过父类对象构造子类
     * @param user - 父类对象
     */
    public Student(User user){
        super(user.getUserId(),user.getEmail(),user.getUserName(),user.getUserNumber(),user.getPassword(),user.getDepartmentId(),user.getIntroduction(),user.getVerifyCode());
    }

    public Student(int preferLanguageId) {
        this.preferLanguageId = preferLanguageId;
    }

    public Student(long userId, String email, String userName, String userNumber, String password, int departmentId, String introduction, String verifyCode, int preferLanguageId) {
        super(userId, email, userName, userNumber, password, departmentId, introduction, verifyCode);
        this.preferLanguageId = preferLanguageId;
    }

    public int getPreferLanguageId() {
        return preferLanguageId;
    }

    public void setPreferLanguageId(int preferLanguageId) {
        this.preferLanguageId = preferLanguageId;
    }
}
