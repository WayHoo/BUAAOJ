package cn.edu.buaa.onlinejudge.model;

public class Course {
    /**
     * 课程唯一标识符
     */
    private int courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程的开课教师
     */
    private Teacher teacher;

    /**
     * 课程简介
     */
    private String introduction;

    /**
     * 课程资源路径
     */
    private String coursewareUrl;

    /**
     * 课程的无参构造方法
     */
    public Course() { }

    /**
     * 课程的有参构造方法
     * @param courseId - 课程的唯一标识符
     * @param courseName - 课程名称
     * @param teacher - 课程的开课教师
     * @param introduction - 课程简介
     * @param coursewareUrl - 课程资源路径
     */
    public Course(int courseId, String courseName, Teacher teacher, String introduction, String coursewareUrl) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacher = teacher;
        this.introduction = introduction;
        this.coursewareUrl = coursewareUrl;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCoursewareUrl() {
        return coursewareUrl;
    }

    public void setCoursewareUrl(String coursewareUrl) {
        this.coursewareUrl = coursewareUrl;
    }
}
