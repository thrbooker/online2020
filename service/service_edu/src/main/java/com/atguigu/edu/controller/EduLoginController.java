package com.atguigu.edu.controller;


import com.atguigu.commonUtils.R;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
public class EduLoginController {


    //login
    @PostMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }

    @GetMapping("info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").
                data("avatar","https://edu-csp.oss-cn-beijing.aliyuncs.com/csp1010.jpg");
    }
}
