package cn.edu.buaa.onlinejudge.model;

public class Administrator {
    /**
     * 系统管理员ID
     */
    private int administratorId;

    /**
     * 系统管理员邮箱
     */
    private String email;

    /**
     * 系统管理员姓名
     */
    private String administratorName;

    /**
     * 系统管理员账户密码
     */
    private String password;

    /**
     * 系统管理员无参构造方法
     */
    public Administrator() { }

    /**
     * 系统管理员有参构造方法
     * @param administratorId - 系统管理员唯一标识符
     * @param email - 系统管理员邮箱
     * @param administratorName - 系统管理员姓名
     * @param password - 系统管理员账户密码
     */
    public Administrator(int administratorId, String email, String administratorName, String password) {
        this.administratorId = administratorId;
        this.email = email;
        this.administratorName = administratorName;
        this.password = password;
    }

    public int getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(int administratorId) {
        this.administratorId = administratorId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdministratorName() {
        return administratorName;
    }

    public void setAdministratorName(String administratorName) {
        this.administratorName = administratorName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
