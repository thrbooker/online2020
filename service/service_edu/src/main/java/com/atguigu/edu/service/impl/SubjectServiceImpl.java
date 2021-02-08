package com.atguigu.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.edu.entity.Subject;
import com.atguigu.edu.entity.excel.SubjectData;
import com.atguigu.edu.entity.subject.OneSubject;
import com.atguigu.edu.entity.subject.TwoSubject;
import com.atguigu.edu.listender.SubjectExcelListender;
import com.atguigu.edu.mapper.SubjectMapper;
import com.atguigu.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-03
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public void saveSubject(MultipartFile file, SubjectService subjectService) {
        try {
            InputStream in = file.getInputStream();
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListender(subjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1 查询所有的一级分类
        QueryWrapper<Subject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<Subject> onesSubjectList = baseMapper.selectList(wrapperOne);

        //2 查询所有的二级分类
        QueryWrapper<Subject> wrapperTwo = new QueryWrapper<>();
        wrapperOne.ne("parent_id","0");
        List<Subject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //创建list集合，用于存储最终封装数据
        List<OneSubject> finalSubject = new ArrayList<>();

        //3.封装一级分类
        //查询出来所有的一级分类list集合遍历，得到每个一级分类对象，获取每个一级分类对象值，
        //封装到要求的list集合里面
        for(int i = 0; i < onesSubjectList.size(); i++){
            Subject subject = onesSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(subject.getId());
//            oneSubject.setTitle(subject.getTitle());

            BeanUtils.copyProperties(subject,oneSubject);
            finalSubject.add(oneSubject);

            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            for (int m = 0; m < twoSubjectList.size(); m++) {
                Subject tSubject = twoSubjectList.get(m);
                //判断二级分类parentId和一级分类id是否相同
                if(tSubject.getParentId().equals(subject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoFinalSubjectList);
        }

        //4.封装二级分类
        return finalSubject;
    }
}
