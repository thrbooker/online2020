package com.atguigu.edu.service;

import com.atguigu.edu.entity.Teacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-30
 */
public interface TeacherService extends IService<Teacher> {

    Map<String, Object> getTeacherFrontList(Page<Teacher> pageTeacher);
}
