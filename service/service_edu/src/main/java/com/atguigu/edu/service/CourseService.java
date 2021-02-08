package com.atguigu.edu.service;

import com.atguigu.edu.entity.Course;
import com.atguigu.edu.entity.frontvo.CourseFrontVo;
import com.atguigu.edu.entity.vo.CourseInfoVo;
import com.atguigu.edu.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    //删除课程
    void removeCourse(String courseId);

    Map<String, Object> getFrontList(Page<Course> coursePage, CourseFrontVo courseFrontVo);
}
