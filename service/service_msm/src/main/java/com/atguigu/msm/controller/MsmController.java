package com.atguigu.msm.controller;

import com.atguigu.commonUtils.R;
import com.atguigu.msm.service.Msmservice;
import com.atguigu.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/msm")
@CrossOrigin
public class MsmController {
    @Autowired
    private Msmservice msmservice;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping(value = "send/{mobile}")
    public R sendMsm(@PathVariable String mobile){
        //1.从Redis中取验证码，如果获取直接返回
        String code = redisTemplate.opsForValue().get(mobile);
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //2.如果获取不到，进行阿里云发送

        //随机生成验证码，传递到阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);
        boolean isSend = msmservice.send(param,mobile);
        if(isSend){
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(mobile,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }
    }
}
