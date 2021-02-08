package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.Course;
import com.atguigu.edu.entity.CourseDescription;
import com.atguigu.edu.entity.Teacher;
import com.atguigu.edu.entity.frontvo.CourseFrontVo;
import com.atguigu.edu.entity.vo.CourseInfoVo;
import com.atguigu.edu.entity.vo.CoursePublishVo;
import com.atguigu.edu.mapper.CourseMapper;
import com.atguigu.edu.service.ChapterService;
import com.atguigu.edu.service.CourseDescriptionService;
import com.atguigu.edu.service.CourseService;
import com.atguigu.edu.service.VideoService;
import com.atguigu.servicebase.excetionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
@Service
@Transactional
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    //注入描述
    @Autowired
    CourseDescriptionService courseDescriptionService;

    //注入小节
    @Autowired
    private VideoService videoService;

    //注入章节
    @Autowired
    private ChapterService chapterService;

    //注入课程
    @Autowired
    private  CourseService courseService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1.向课程表添加课程基本信息
        //2.将courseInfoVo装换成course对象
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoVo,course);
        int insert = baseMapper.insert(course);

        if(insert == 0){
            throw new GuliException(20001,"添加课程失败");
        }
        String id = course.getId();

        //2.向课程简介表添加课程简介
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(id);
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.save(courseDescription);
        return id;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1.先查询课程表
        Course course = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(course,courseInfoVo);

        //2.查询描述表
        CourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());

        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1 修改课程表
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoVo,course);
        int update = baseMapper.updateById(course);
        if(update == 0){
            throw new GuliException(20001,"修改课程信息失败");
        }

        //2 修改描述表
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(courseInfoVo.getId());
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(courseDescription);
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo coursePublishVo = baseMapper.getPublishCourseInfo(id);
        return coursePublishVo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //1 根据课程id删除小节
        videoService.removeVideoByCourseId(courseId);

        //2 根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);

        //3 根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //4 根据课程id删除课程
        int res = baseMapper.deleteById(courseId);
        if(res == 0){
            throw new GuliException(20001,"删除失败");
        }
    }

    @Override
    public Map<String, Object> getFrontList(Page<Course> coursePage, CourseFrontVo courseFrontVo) {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }

        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }

        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");
        }

        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){
            wrapper.orderByDesc("gmt_create");
        }

        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(coursePage,wrapper);
        List<Course> records = coursePage.getRecords();
        long current = coursePage.getCurrent();
        long pages = coursePage.getPages();
        long size = coursePage.getSize();
        long total = coursePage.getTotal();
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }
}
