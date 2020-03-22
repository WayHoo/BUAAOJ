package cn.edu.buaa.onlinejudge.controller;

import cn.edu.buaa.onlinejudge.model.Student;
import cn.edu.buaa.onlinejudge.model.Teacher;
import cn.edu.buaa.onlinejudge.model.User;
import cn.edu.buaa.onlinejudge.service.StudentService;
import cn.edu.buaa.onlinejudge.service.TeacherService;
import cn.edu.buaa.onlinejudge.utils.HttpResponseWrapperUtil;
import cn.edu.buaa.onlinejudge.utils.ImageVerifyCodeUtil;
import cn.edu.buaa.onlinejudge.utils.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping(value = "BUAAOJ/accounts")
public class AccountController {

    private final StudentService studentService;

    private final TeacherService teacherService;

    public AccountController(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    /**
     * 生成指定大小的验证码图片，验证码为四位数
     * 包含大小写字母和数字，字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
     * @param session - 会话对象
     * @param response - Servlet响应对象
     */
    @ApiOperation(value = "生成图片验证码接口")
    @GetMapping(value = "/getImgVerifyCode")
    public void getImgVerifyCode(HttpSession session, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        //生成随机字串
        String verifyCode = ImageVerifyCodeUtil.generateVerifyCode(4);
        session.setAttribute("verifyCode", verifyCode);
        //生成指定长宽分辨率的验证码图片
        int width = 160, height = 50;
        try {
            ImageVerifyCodeUtil.outputImage(width, height, response.getOutputStream(), verifyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户（学生和教师）登录
     * @param user - 用户对象
     * @param tag - 用户标签，0表示学生，1表示教师
     * @param session - 会话
     * @return JSON数据
     */
    @ApiOperation(value = "用户（学生和教师）登录接口")
    @PostMapping(value = "/login/{tag}")
    public HttpResponseWrapperUtil login(@RequestBody User user, @PathVariable("tag") int tag, HttpSession session) {
        //获取session中的验证码并检验用户输入是否正确
        String codeInSession = (String) session.getAttribute("verifyCode");
        if( !checkVerifyCode(codeInSession, user.getVerifyCode()) ) {
            return new HttpResponseWrapperUtil(null, -1, "验证码错误");
        }
        //根据用户输入邮箱号检查数据库中是否存在对应账号
        User realUser;
        if( tag == 0 ){
            realUser = studentService.getStudentByEmail(user.getEmail());
        } else if( tag == 1 ){
            realUser = teacherService.getTeacherByEmail(user.getEmail());
        } else{
            return new HttpResponseWrapperUtil(null, -1, "用户标签参数非法");
        }
        if( realUser == null ){
            return new HttpResponseWrapperUtil(null, -1, "该账号不存在");
        }
        //检查用户输入密码是否正确
        if( !MD5Util.verifyMD5(user.getPassword(), realUser.getPassword()) ){
            return new HttpResponseWrapperUtil(null, -1, "账号或密码错误");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("userId",realUser.getUserId());
        data.put("userName",realUser.getUserName());
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 用户（学生和教师）注册
     * @param user - 用户对象
     * @param tag - 用户标签，0表示学生，1表示教师
     * @param session - 会话对象
     * @return JSON数据
     */
    @ApiOperation("用户（学生和教师）注册接口")
    @PostMapping(value = "/register/{tag}")
    public HttpResponseWrapperUtil register(@RequestBody User user, @PathVariable("tag") int tag, HttpSession session){
        //根据用户输入邮箱号检查数据库中是否存在对应账号，防止用户重复注册
        User realUser;
        if( tag == 0 ){
            realUser = studentService.getStudentByEmail(user.getEmail());
        } else if( tag == 1 ){
            realUser = teacherService.getTeacherByEmail(user.getEmail());
        } else{
            return new HttpResponseWrapperUtil(null, -1, "用户标签参数非法");
        }
        if( realUser != null ){
            return new HttpResponseWrapperUtil(null, -1, "该邮箱已注册");
        }
        //获取session中的验证码并检验用户输入是否正确
        String codeInSession = (String) session.getAttribute("verifyCode");
        if( !checkVerifyCode(codeInSession, user.getVerifyCode()) ) {
            return new HttpResponseWrapperUtil(null, -1, "验证码错误");
        }
        //将用户密码通过MD5算法加密后存入数据库
        user.setPassword(MD5Util.encryptedByMD5(user.getPassword()));
        User newUser;
        if( tag == 0 ){
            newUser = new Student(user);
            studentService.insertStudent((Student)newUser);
        } else{
            newUser = new Teacher(user);
            teacherService.insertTeacher((Teacher)newUser);
        }
        Map<String,Object> data = new HashMap<>();
        data.put("userId",newUser.getUserId());
        data.put("userName",newUser.getUserName());
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 用户（学生和教师）查看个人信息
     * @param tag - 用户标签，0表示学生，1表示教师
     * @param userId - 用户ID
     * @return JSON数据
     */
    @ApiOperation("查看用户信息接口")
    @GetMapping(value = "/getUserInfo/{tag}/{userId}")
    public HttpResponseWrapperUtil getUserInfo(@PathVariable("tag") int tag, @PathVariable("userId") long userId){
        User user;
        if( tag == 0 ){
            user = studentService.getStudentById(userId);
        }else if( tag == 1 ){
            user = teacherService.getTeacherById(userId);
        }else{
            return new HttpResponseWrapperUtil(null, -1, "用户标签参数非法");
        }
        if( user == null ) {
            return new HttpResponseWrapperUtil(null, -1, "该用户不存在");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("email",user.getEmail());
        data.put("userName",user.getUserName());
        data.put("userNumber",user.getUserNumber());
        data.put("departmentId",user.getDepartmentId());
        data.put("introduction",user.getIntroduction());
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 用户（学生和教师）修改个人信息
     * @param user - 用户对象
     * @param tag - 用户标签，0表示学生，1表示教师
     * @return JSON数据
     */
    @ApiOperation("用户修改个人信息接口")
    @PostMapping(value = "/updateUserInfo/{tag}")
    public HttpResponseWrapperUtil updateUserInfo(@RequestBody User user, @PathVariable("tag") int tag){
        User newUser;
        if( tag == 0 ){
            studentService.updateStudent(new Student(user));
            newUser = studentService.getStudentById(user.getUserId());
        }else if( tag == 1 ){
            teacherService.updateTeacher(new Teacher(user));
            newUser = teacherService.getTeacherById(user.getUserId());
        }else{
            return new HttpResponseWrapperUtil(null, -1, "用户标签参数非法");
        }
        if( newUser == null ) {
            return new HttpResponseWrapperUtil(null, -1, "该用户不存在");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("userId",newUser.getUserId());
        data.put("userName",newUser.getUserName());
        data.put("userNumber",newUser.getUserNumber());
        data.put("departmentId",newUser.getDepartmentId());
        data.put("introduction",newUser.getIntroduction());
        return new HttpResponseWrapperUtil(data);
    }

    /**
     * 用户（学生和教师）修改密码
     * @param tag - 用户标签，0表示学生，1表示教师
     * @param userId - 用户ID
     * @param oldPassword - 旧密码
     * @param newPassword - 新密码
     * @return JSON数据
     */
    @ApiOperation("用户修改密码接口")
    @PostMapping(value = "/resetUserPassword/{tag}")
    public HttpResponseWrapperUtil resetUserPassword(@PathVariable("tag") int tag,
                                                     @RequestParam("userId") long userId,
                                                     @RequestParam("oldPassword") String oldPassword,
                                                     @RequestParam("newPassword") String newPassword){
        User user;
        if( tag == 0 ){
            user = studentService.getStudentById(userId);
        }else if( tag == 1 ){
            user = teacherService.getTeacherById(userId);
        }else{
            return new HttpResponseWrapperUtil(null, -1, "用户标签参数非法");
        }
        if( user == null ){
            return new HttpResponseWrapperUtil(null, -1, "该用户不存在");
        }
        if( !MD5Util.verifyMD5(oldPassword, user.getPassword()) ){
            return new HttpResponseWrapperUtil(null, -1, "旧密码输入错误");
        }
        if( oldPassword.equals(newPassword) ){
            return new HttpResponseWrapperUtil(null, -1, "新密码与旧密码不能相同");
        }
        user.setPassword(MD5Util.encryptedByMD5(newPassword));
        if( tag == 0 ){
            studentService.resetPassword((Student)user);
        }else{
            teacherService.resetPassword((Teacher)user);
        }
        return new HttpResponseWrapperUtil(null);
    }

    /**
     * 判断验证码是否正确
     * @param codeInSession - Session中的验证码
     * @param verfiyCode - 用户输入的验证码
     * @return - 验证码是否正确的布尔值
     */
    public boolean checkVerifyCode(String codeInSession, String verfiyCode) {
        return !StringUtils.isEmpty(codeInSession) &&
                codeInSession.equals(verfiyCode.toUpperCase());
    }
}
