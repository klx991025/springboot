package com.ypt.springboot.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ypt.springboot.common.Constants;
import com.ypt.springboot.common.Result;
import com.ypt.springboot.controller.dto.UserDTO;
import com.ypt.springboot.entity.AlarmLog;
import com.ypt.springboot.entity.LoginLog;
import com.ypt.springboot.entity.Role;
import com.ypt.springboot.service.AlarmLogService;
import com.ypt.springboot.service.IRoleService;
import com.ypt.springboot.service.LoginService;
import com.ypt.springboot.utils.TokenUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import com.ypt.springboot.service.IUserService;
import com.ypt.springboot.entity.User;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kkk
 * @since 2022-10-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private AlarmLogService alarmLogService;
    @Autowired
    private IRoleService iRoleService;
    @PostMapping
    public Result save(@RequestBody User user){
        return Result.success(userService.saveOrUpdate(user));
    }
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO, HttpServletRequest request){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        String userAgent = request.getHeader("User-Agent");
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        Browser browser = ua.getBrowser();
        OperatingSystem os = ua.getOperatingSystem();
        String ipAddress = getIpAddress(request);
        LoginLog log = new LoginLog();
        log.setIp(ipAddress);
        log.setUsername(dto.getUsername());
        log.setOs(os.toString());
        log.setWeb(browser.toString());
        log.setDate(new Date());
        log.setUserId(dto.getUserId());
        loginService.save(log);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                LoginLog log = new LoginLog();
//                log.setIp(ipAddress);
//                log.setSystem(os.toString());
//                log.setWeb(browser.toString());
//                log.setDate(new Date());
//                log.setUserId(id);
//                loginService.save(log);
//            }
//        }).start();
        return Result.success(dto);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(userService.register(userDTO));
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(userService.removeByIds(ids));
    }
    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //查询数据库中所有数据
        List<User> list = userService.list();
        //在内存操作，写入浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //一次性写出list中的对象到Excel,使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
    //excel导入
    @PostMapping("import")
    public Result imp(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);
        return Result.success(true);
    }
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String nickname,
                               @RequestParam(defaultValue = "") String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!"".equals(username)){
            queryWrapper.like("username",username);
        }
        if(!"".equals(nickname)){
            queryWrapper.like("nickname",nickname);
        }
        if(!"".equals(phone)){
            queryWrapper.like("phone",phone);
        }
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (User item: page.getRecords()) {
            if (StringUtils.isNotBlank(item.getRole())){
                QueryWrapper qw = new QueryWrapper();
                qw.eq("flag",item.getRole());
                Role one = iRoleService.getOne(qw);
                item.setRole(one.getName());
            }
        }
        return Result.success(page);
    }

    @GetMapping("/pageLog")
    public Result pageLog(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String nickname,
                           @RequestParam(defaultValue = "") String phone) {
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
        if(!"".equals(username)){
            queryWrapper.like("username",username);
        }
        Page<LoginLog> page = loginService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        for (LoginLog item: page.getRecords()) {
//            User byId = userService.getById(item.getUserId());
//            item.setUsername(byId.getUsername());
//        }
        return Result.success(page);
    }

    @GetMapping("/pageAlarmLog")
    public Result pageAlarmLog(@RequestParam Integer pageNum,
                          @RequestParam Integer pageSize,
                          @RequestParam(defaultValue = "") String updateBy) {
        QueryWrapper<AlarmLog> queryWrapper = new QueryWrapper<>();
        if(!"".equals(updateBy)){
            queryWrapper.like("update_by",updateBy);
        }
        Page<AlarmLog> page = alarmLogService.page(new Page<>(pageNum, pageSize), queryWrapper);
//        for (LoginLog item: page.getRecords()) {
//            User byId = userService.getById(item.getUserId());
//            item.setUsername(byId.getUsername());
//        }
        return Result.success(page);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }
}

