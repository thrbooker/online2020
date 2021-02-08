package com.atguigu.edu.controller.front;

import com.atguigu.commonUtils.R;
import com.atguigu.edu.entity.Course;
import com.atguigu.edu.entity.frontvo.CourseFrontVo;
import com.atguigu.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

    @Autowired
    private CourseService courseService;

    //1 条件查询带分页查询课程
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<Course> coursePage = new Page<>(page,limit);
        Map<String,Object> map = courseService.getFrontList(coursePage,courseFrontVo);
        //返回分页数据
        return R.ok().data(map);
    }

}
