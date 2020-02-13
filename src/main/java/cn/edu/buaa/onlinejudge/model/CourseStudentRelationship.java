package cn.edu.buaa.onlinejudge.model;

public class CourseStudentRelationship {
    /**
     * 学生与课程关系（是否参加课程以及在课程中的角色）ID
     */
    private long relationshipId;
    /**
     * 课程ID
     */
    private int courseId;
    /**
     * 学生ID
     */
    private long studentId;
    /**
     * 学生提交的加入课程申请是否通过
     */
    private boolean isStudentAccept;
    /**
     * 学生在课程中的角色（如果学生通过申请），学生 -- 0，助教 -- 1
     * 默认为学生
     */
    private int studentRole;

    public CourseStudentRelationship( ) { }

    public CourseStudentRelationship(int courseId, long studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public boolean isStudentAccept() {
        return isStudentAccept;
    }

    public void setStudentAccept(boolean studentAccept) {
        isStudentAccept = studentAccept;
    }

    public int getStudentRole() {
        return studentRole;
    }

    public void setStudentRole(int studentRole) {
        this.studentRole = studentRole;
    }
}
