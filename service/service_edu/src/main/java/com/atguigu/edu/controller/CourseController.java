package com.atguigu.edu.controller;


import com.atguigu.commonUtils.R;
import com.atguigu.edu.entity.Course;
import com.atguigu.edu.entity.Teacher;
import com.atguigu.edu.entity.vo.CourseInfoVo;
import com.atguigu.edu.entity.vo.CoursePublishVo;
import com.atguigu.edu.entity.vo.CourseQuery;
import com.atguigu.edu.entity.vo.TeacherQuery;
import com.atguigu.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseService courseService;

    //条件查询带分页方法
    @ApiOperation(value = "分页条件查询讲师列表")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) CourseQuery courseQuery){
        //创建page对象
        Page<Course> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        //多条件组合查询
        //判断条件值是否为空，如果不为空拼接条件
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        String begin = courseQuery.getBegin();
        String end = courseQuery.getEnd();
        if(!StringUtils.isEmpty(title)){
            //构建条件
            wrapper.like("title",title);
        }

        if(!StringUtils.isEmpty(status)){
            //构建条件
            wrapper.eq("status",status);
        }

        if(!StringUtils.isEmpty(begin)){
            //构建条件
            wrapper.ge("gmt_create",begin);
        }

        if(!StringUtils.isEmpty(end)){
            //构建条件
            wrapper.le("gmt_create",end);
        }

        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        courseService.page(page,wrapper);

        long total = page.getTotal(); //总记录数
        List<Course> records = page.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //删除课程
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }


    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }


    //根据课程ID查询课程基本信息
    @GetMapping("getCourseInfo/{courseId}") 
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping ("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        Course course = new Course();
        course.setId(id);
        course.setStatus("Normal");
        courseService.updateById(course);
        return R.ok();
    }
}

