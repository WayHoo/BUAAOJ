package cn.edu.buaa.onlinejudge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Language {
    /**
     * 编程语言ID
     */
    private int languageId;

    /**
     * 编程语言名称
     */
    private String languageName;

    /**
     * 编程语言的编译命令
     */
    @JsonIgnore
    private String compileCommand;

    /**
     * 编程语言的运行命令
     */
    @JsonIgnore
    private String runCommand;

    /**
     * 编程语言的默认构造方法
     */
    public Language() { }

    /**
     * 编程语言的有参构造方法
     * @param languageId - 编程语言的唯一标识符
     * @param languageName - 编程语言名称
     * @param compileCommand - 编程语言的编译命令
     * @param runCommand - 编程语言的运行命令
     */
    public Language(int languageId, String languageName, String compileCommand, String runCommand) {
        this.languageId = languageId;
        this.languageName = languageName;
        this.compileCommand = compileCommand;
        this.runCommand = runCommand;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getCompileCommand() {
        return compileCommand;
    }

    public void setCompileCommand(String compileCommand) {
        this.compileCommand = compileCommand;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(String runCommand) {
        this.runCommand = runCommand;
    }

    @Override
    public String toString() {
        return "Language{" +
                "languageId=" + languageId +
                ", languageName='" + languageName + '\'' +
                ", compileCommand='" + compileCommand + '\'' +
                ", runCommand='" + runCommand + '\'' +
                '}';
    }
}
