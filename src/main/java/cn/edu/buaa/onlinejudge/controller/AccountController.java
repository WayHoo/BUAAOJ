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
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "生成图片验证码接口")
    @RequestMapping(value = "/getImgVerifyCode", method = RequestMethod.GET)
    public void getImgVerifyCode(HttpSession session, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        //生成随机字串
        String verifyCode = ImageVerifyCodeUtil.generateVerifyCode(4);
        session.setAttribute("verifyCode", verifyCode);
        //生成验证码图片
        int width = 100, height = 30;
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
    @RequestMapping(value = "/login/{tag}", method = RequestMethod.POST)
    public HttpResponseWrapperUtil login(@RequestBody User user, @PathVariable("tag") int tag, HttpSession session) {
        String codeInSession = (String) session.getAttribute("verifyCode");
        if( !checkVerifyCode(codeInSession, user.getVerifyCode()) ) {
            return new HttpResponseWrapperUtil(null, -1, "验证码错误");
        }
        User realUser = null;
        if( tag == 0 ){
            realUser = studentService.getStudentByEmail(user.getEmail());
        } else if( tag == 1 ){
            realUser = teacherService.getTeacherByEmail(user.getEmail());
        } else{
            return new HttpResponseWrapperUtil(null, -1, "tag参数错误");
        }
        if( realUser == null ){
            return new HttpResponseWrapperUtil(null, -1, "账号不存在");
        }
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
     * @param session - 会话
     * @return JSON数据
     */
    @ApiOperation("用户（学生和教师）注册接口")
    @RequestMapping(value = "/register/{tag}", method = RequestMethod.POST)
    public HttpResponseWrapperUtil register(@RequestBody User user, @PathVariable("tag") int tag, HttpSession session){
        User realUser = null;
        if( tag == 0 ){
            realUser = studentService.getStudentByEmail(user.getEmail());
        } else if( tag == 1 ){
            realUser = teacherService.getTeacherByEmail(user.getEmail());
        } else{
            return new HttpResponseWrapperUtil(null, -1, "tag参数错误");
        }
        if( realUser != null ){
            return new HttpResponseWrapperUtil(null, -1, "该邮箱已注册");
        }
        String codeInSession = (String) session.getAttribute("verifyCode");
        if( !checkVerifyCode(codeInSession, user.getVerifyCode()) ) {
            return new HttpResponseWrapperUtil(null, -1, "验证码错误");
        }
        user.setPassword(MD5Util.encryptedByMD5(user.getPassword()));
        User newUser = null;
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

    @ApiOperation("查看用户信息接口")
    @RequestMapping(value = "/getUserInfo/{tag}/{userId}", method = RequestMethod.GET)
    public HttpResponseWrapperUtil getUserInfo(@PathVariable("tag") int tag, @PathVariable("userId") long userId){
        User user = null;
        if( tag == 0 ){
            user = studentService.getStudentById(userId);
        }else if( tag == 1 ){
            user = teacherService.getTeacherById(userId);
        }else{
            return new HttpResponseWrapperUtil(null, -1, "tag参数错误");
        }
        if( user == null ) {
            return new HttpResponseWrapperUtil(null, -1, "failure");
        }
        Map<String,Object> data = new HashMap<>();
        data.put("email",user.getEmail());
        data.put("userName",user.getUserName());
        data.put("userNumber",user.getUserNumber());
        data.put("departmentId",user.getDepartmentId());
        data.put("introduction",user.getIntroduction());
        return new HttpResponseWrapperUtil(data);
    }

    @ApiOperation("用户修改个人信息接口")
    @RequestMapping(value = "/updateUserInfo/{tag}", method = RequestMethod.POST)
    public HttpResponseWrapperUtil updateUserInfo(@RequestBody User user, @PathVariable("tag") int tag){
        User newUser = null;
        if( tag == 0 ){
            studentService.updateStudent(new Student(user));
            newUser = studentService.getStudentById(user.getUserId());
        }else if( tag == 1 ){
            teacherService.updateTeacher(new Teacher(user));
            newUser = teacherService.getTeacherById(user.getUserId());
        }else{
            return new HttpResponseWrapperUtil(null, -1, "tag参数错误");
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

    @ApiOperation("用户修改密码接口")
    @RequestMapping(value = "/resetUserPassword/{tag}", method = RequestMethod.POST)
    public HttpResponseWrapperUtil resetUserPassword(@PathVariable("tag") int tag,
                                                     @RequestParam(value = "userId") long userId,
                                                     @RequestParam(value = "oldPassword") String oldPassword,
                                                     @RequestParam(value = "newPassword") String newPassword){
        User user = null;
        if( tag == 0 ){
            user = studentService.getStudentById(userId);
        }else if( tag == 1 ){
            user = teacherService.getTeacherById(userId);
        }else{
            return new HttpResponseWrapperUtil(null, -1, "tag参数错误");
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
        if(StringUtils.isEmpty(codeInSession) ||
                !codeInSession.equals(verfiyCode.toUpperCase())) {
            return false;
        }
        return true;
    }
}
