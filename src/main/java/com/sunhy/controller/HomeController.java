package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sunhy.common.BaseContext;
import com.sunhy.common.R;
import com.sunhy.dto.UserDto;
import com.sunhy.entity.Test;
import com.sunhy.entity.User;
import com.sunhy.entity.UserDetail;
import com.sunhy.service.IUserDetailService;
import com.sunhy.service.IUserService;
import com.sunhy.utils.JavaWebToken;
import com.sunhy.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private final IUserService userService;

    private final IUserDetailService userDetailService;

    private final RedisTemplate<String, String> redisTemplate;
    public HomeController(IUserService userService, IUserDetailService userDetailService, RedisTemplate<String, String> redisTemplate) {
        this.userService = userService;
        this.userDetailService = userDetailService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping()
    public String home(@RequestBody Test test){

        log.info("id是"+test.getId());
        return "载入成功";
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
            LambdaQueryWrapper<UserDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserDetail::getUserid, userid);
            UserDetail userDetail = userDetailService.getOne(wrapper);
            String avatar = userDetail.getAvatar();

            BaseContext.setUserid(userid);

            return R.success("登录成功", token, one.getId().toString(),avatar);
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
        LambdaQueryWrapper<UserDetail> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(UserDetail::getUserid, userid);
        UserDetail userDetail = userDetailService.getOne(wrapper2);
        String avatar = userDetail.getAvatar();

        BaseContext.setUserid(userid);

        return R.success("登录成功", token, theUser.getId().toString(),avatar);//把token返回给前端
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
