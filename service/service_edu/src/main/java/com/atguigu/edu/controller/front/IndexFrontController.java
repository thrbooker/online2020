package com.atguigu.edu.controller.front;

import com.atguigu.commonUtils.R;
import com.atguigu.edu.entity.Course;
import com.atguigu.edu.entity.Teacher;
import com.atguigu.edu.service.CourseService;
import com.atguigu.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;


    //查询前8条热门课程，查询前4名讲师
    @GetMapping("index")
    public R index(){
        //查询前8条热门课程
        QueryWrapper<Course> wrapperCourese = new QueryWrapper<>();
        wrapperCourese.orderByDesc("id");
        wrapperCourese.last("limit 8");
        List<Course> courseList = courseService.list(wrapperCourese);

        //查询前4名讲师
        QueryWrapper<Teacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<Teacher> teacherList = teacherService.list(wrapperTeacher);

        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
