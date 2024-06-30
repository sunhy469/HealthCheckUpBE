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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

    //用户注册
    @PostMapping("/register")
    @Transactional
    public R<String> register(@RequestBody UserDto userDto) {
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

        return R.success("注册成功", token, user.getId().toString(),"");

    }

    //用户登录
    @PostMapping("/login")
    public R<String> login(@RequestBody UserDto userDto) {
        log.info("用户登录");

        //手机号登录
        if (StringUtils.isNotEmpty(userDto.getMobile())) {
            String mobile = userDto.getMobile();
            String captcha = userDto.getCaptcha();

            User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
            if (one == null)
                return R.error("手机号未注册，请注册后重试^_^");

            //验证码校验
            String correctCaptcha = redisTemplate.opsForValue().get(mobile);
            if (StringUtils.isEmpty(correctCaptcha) || !correctCaptcha.equals(captcha))
                return R.error("验证码错误，请检查后重试");

            redisTemplate.delete(mobile);
            //生成token
            String token = handleToken(one);

            String userid = one.getUserid();

            BaseContext.setUserid(userid);

            return R.success("登录成功", token, one.getId().toString(),"");
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

        //生成token
        String token = handleToken(theUser);

        String userid = theUser.getUserid();

        BaseContext.setUserid(userid);

        return R.success("登录成功", token, theUser.getId().toString(),theUser.getRoleId().toString());//把token返回给前端
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
