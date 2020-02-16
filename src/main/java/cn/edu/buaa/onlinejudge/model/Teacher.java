package cn.edu.buaa.onlinejudge.model;

public class Teacher extends User{

    /**
     * 教师是否通过系统管理员审核（true - 允许开课，false - 不允许开课）
     */
    private boolean isAccept;

    /**
     * 教师的无参构造方法
     */
    public Teacher() {
        super();
    }

    /**
     * 通过父类对象构造子类
     * @param user - 父类对象
     */
    public Teacher(User user){
        super(user.getUserId(),user.getEmail(),user.getUserName(),user.getUserNumber(),user.getPassword(),user.getDepartmentId(),user.getIntroduction(),user.getVerifyCode());
    }

    public Teacher(boolean isAccept) {
        this.isAccept = isAccept;
    }

    public Teacher(long userId, String email, String userName, String userNumber, String password, int departmentId, String introduction, String verifyCode, boolean isAccept) {
        super(userId, email, userName, userNumber, password, departmentId, introduction, verifyCode);
        this.isAccept = isAccept;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }
}
