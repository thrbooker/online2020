package com.atguigu.edu.mapper;

import com.atguigu.edu.entity.Course;
import com.atguigu.edu.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
public interface CourseMapper extends BaseMapper<Course> {

    public CoursePublishVo getPublishCourseInfo(String courseId);

}
