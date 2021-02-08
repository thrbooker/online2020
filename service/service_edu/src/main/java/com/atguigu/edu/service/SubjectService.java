package com.atguigu.edu.service;

import com.atguigu.edu.entity.Subject;
import com.atguigu.edu.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-11-03
 */
public interface SubjectService extends IService<Subject> {

    //添加课程分类
    void saveSubject(MultipartFile file,SubjectService subjectService);

    //课程分类列表（树形）
    List<OneSubject> getAllOneTwoSubject();
}
