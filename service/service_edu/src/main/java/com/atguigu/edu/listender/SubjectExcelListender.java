package com.atguigu.edu.listender;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.edu.entity.Subject;
import com.atguigu.edu.entity.excel.SubjectData;
import com.atguigu.edu.service.SubjectService;
import com.atguigu.servicebase.excetionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class SubjectExcelListender extends AnalysisEventListener<SubjectData> {
    public SubjectService subjectService;

    public SubjectExcelListender(){}

    public SubjectExcelListender(SubjectService subjectService){
        this.subjectService = subjectService;
    }

    //读取excel内容，一行一行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
            if(subjectData == null){
            throw new GuliException(20001,"文件数据为空");
        }

        //一行一行读取，每次读取有两个值，第一个一级分类，第二个二级分类
        //判断以及分类是否重复
        Subject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        if(existOneSubject == null){//没有相同一级分类,进行添加
            existOneSubject = new Subject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());//一级分类名称
            subjectService.save(existOneSubject);
        }

        String pid = existOneSubject.getId();
        //添加二级分类
        //判断二级分类是否重复
        Subject existTwoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        if(existTwoSubject == null){//没有相同一级分类,进行添加
            existTwoSubject = new Subject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());//一级分类名称
            subjectService.save(existTwoSubject);
        }
    }

    private Subject existOneSubject(SubjectService subjectService,String name){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        Subject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }

    private Subject existTwoSubject(SubjectService subjectService,String name,String pid){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        Subject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
