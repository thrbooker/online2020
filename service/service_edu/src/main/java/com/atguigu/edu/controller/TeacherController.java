package com.atguigu.edu.controller;


import com.atguigu.commonUtils.R;
import com.atguigu.edu.entity.Teacher;
import com.atguigu.edu.entity.vo.TeacherQuery;
import com.atguigu.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-09-30
 */

@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAll(){
        List<Teacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "逻辑删除讲师列表")
    @DeleteMapping("{id}")
    public R removeById(@ApiParam(name = "id", value = "讲师ID",required = true
        ) @PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if(flag){
            return R.ok();
        }
        else{
            return R.error();
        }
    }

    @ApiOperation(value = "分页查询讲师列表")
    //3 分页查询方法  current代表当前页  limit每页记录数
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current, @PathVariable long limit){
        //创建配置对象
        Page<Teacher> pageTeacher = new Page<>(current,limit);
        //调用方法实现分页
        //调用方法时候，底层封装，把所有的数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal(); //总记录数
        List<Teacher> records = pageTeacher.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //4.条件查询带分页方法
    @ApiOperation(value = "分页条件查询讲师列表")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建page对象
        Page<Teacher> page = new Page<>(current, limit);

        //构建条件
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();

        //多条件组合查询
        //判断条件值是否为空，如果不为空拼接条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if(!StringUtils.isEmpty(name)){
            //构建条件
            wrapper.like("name",name);
        }

        if(!StringUtils.isEmpty(level)){
            //构建条件
            wrapper.eq("level",level);
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
        teacherService.page(page,wrapper);

        long total = page.getTotal(); //总记录数
        List<Teacher> records = page.getRecords();//数据list集合
        return R.ok().data("total",total).data("rows",records);

    }

    //5.添加讲师接口方法
    @ApiOperation(value = "添加讲师接口")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody Teacher teacher){
        boolean save = teacherService.save(teacher);
        if(save){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //6.根据讲师id进行查询
    @ApiOperation(value = "根据讲师id进行查询")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable long id){
        Teacher teacher = teacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    //7.修改讲师功能
    @ApiOperation(value = "根据讲师id进行修改")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody Teacher teacher){
        boolean flag = teacherService.updateById(teacher);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }

}

