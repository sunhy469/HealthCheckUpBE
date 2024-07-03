package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sunhy.common.BaseContext;
import com.sunhy.common.R;
import com.sunhy.dto.UserDto;
import com.sunhy.entity.User;
import com.sunhy.service.IUserService;
import com.sunhy.utils.JavaWebToken;
import com.sunhy.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final IUserService userService;

    private final RedisTemplate<String, String> redisTemplate;

    public UserController(IUserService userService, RedisTemplate<String, String> redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/modpwd")
    public R<String> modPwd(@RequestBody UserDto userDto) {
        log.info("修改密码");
        //注册验证码
        String captcha = userDto.getCaptcha();
        String mobile = userDto.getMobile();
        assert mobile != null;
        String correctCaptcha = redisTemplate.opsForValue().get(mobile);
        assert correctCaptcha != null;

        if (!correctCaptcha.equals(captcha))
            return R.error("验证码错误，请检查后重试");

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMobile, mobile);
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        userService.update(userDto,wrapper);
        return R.success("修改密码成功");
    }

    @PostMapping("/getinfo")
    public R<User> getInfo(@RequestBody User user) {
        log.info("获取用户的基本信息");
        User one = userService.getById(user.getId());
        if (one !=null)
            return R.success(one);
        else return R.error("用户不存在!");
    }

    /*
    查询所有患者信息
     */
    @GetMapping("/getpatient")
    public R<List<User>> getPatient() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRoleId, 0);
        List<User> patients = userService.getPatient(wrapper);
        return R.success(patients);
    }

    /*
    查询所有用户信息
     */
    @GetMapping("/getall")
    public R<List<User>> getAll() {
        log.info("获取所有用户信息");
        return R.success(userService.list());
    }

    /*
    修改用户基本信息
     */
    @PostMapping("/editinfo")
    public R<User> editInfo(@RequestBody User user) {
        log.info("修改用户的基本信息");
        userService.updateById(user);
        return R.success(user,"修改成功");
    }

    //用户注册
    @PostMapping("/register")
    @Transactional
    public R<User> register(@RequestBody UserDto userDto) {
        log.info("用户注册");
        //手机号
        String mobile = userDto.getMobile();
        //注册验证码
        String captcha = userDto.getCaptcha();
        assert mobile != null;
        String correctCaptcha = redisTemplate.opsForValue().get(mobile);
        assert correctCaptcha != null;

        if (!correctCaptcha.equals(captcha))
            return R.error("验证码错误，请检查后重试");

        //对密码MD5加密
        userDto.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes()));
        long l = System.currentTimeMillis();
        userDto.setUserid("sunny_" + l);
        //注册数据
        userService.save(userDto);
        //删除注册时的验证码
        redisTemplate.delete(mobile);
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
        String token = handleToken(user);

        String userid ="sunny_" + l;

        BaseContext.setUserid(userid);

        return R.success(user,"注册成功",token);
    }

    //用户登录
    @PostMapping("/login")
    public R<User> login(@RequestBody UserDto userDto) {
        log.info("用户登录");

        //手机号登录
        if (StringUtils.isNotEmpty(userDto.getMobile())) {
            String mobile = userDto.getMobile();
            String captcha = userDto.getCaptcha();

            User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
            if (one == null)
                return R.error("手机号未注册，请注册后重试^_^");

            // 修改上次登录时间
            one.setLastLoginTime(LocalDateTime.now());
            userService.updateById(one);

            //验证码校验
            String correctCaptcha = redisTemplate.opsForValue().get(mobile);
            if (StringUtils.isEmpty(correctCaptcha) || !correctCaptcha.equals(captcha))
                return R.error("验证码错误，请检查后重试");

            redisTemplate.delete(mobile);
            //生成token
            String token = handleToken(one);

            String userid = one.getUserid();

            BaseContext.setUserid(userid);

            return R.success(one,"登录成功",token);
        }

        //账号登录
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDto.getUsername());

        User theUser = userService.getOne(wrapper);
        //得到用户提交的代码并加密
        String password = userDto.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (theUser == null)
            return R.error("用户名不存在，请检查后重试");

        if (!theUser.getPassword().equals(password))
            return R.error("密码错误，请检查后重试");

        // 修改上次登录时间
        theUser.setLastLoginTime(LocalDateTime.now());
        userService.updateById(theUser);

        //生成token
        String token = handleToken(theUser);

        String userid = theUser.getUserid();

        BaseContext.setUserid(userid);

        return R.success(theUser,"登录成功",token);//把token返回给前端
    }

    //获取验证码
    @PostMapping("/captcha")
    public R<String> captcha(@RequestBody User user) {
        log.info("获取验证码");
        String mobile = user.getMobile();

        if (StringUtils.isNotEmpty(mobile)) {
            String captcha = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码为：" + captcha);
            //阿里云短信服务
//            SMSUtils.sendMessage("Reggie外卖","",mobile,captcha);
            redisTemplate.opsForValue().set(mobile, captcha, 5, TimeUnit.MINUTES);
            return R.success("验证码发送成功，有效期为5分钟");
        }
        return R.error("手机号不能为空");
    }

    private String handleToken(User user) {
        //生成token
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        String token = JavaWebToken.createJavaWebToken(map);
        //3天过期或者用户退出登录过期
        redisTemplate.opsForValue().set(user.getId().toString(), token, 3, TimeUnit.DAYS);
        return token;
    }
}
