package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.Chapter;
import com.atguigu.edu.entity.Video;
import com.atguigu.edu.entity.chapter.ChapterVo;
import com.atguigu.edu.entity.chapter.VideoVo;
import com.atguigu.edu.mapper.ChapterMapper;
import com.atguigu.edu.service.ChapterService;
import com.atguigu.edu.service.VideoService;
import com.atguigu.servicebase.excetionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoService videoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1 根据课程id查询课程里面的所有章节
        QueryWrapper<Chapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<Chapter> chapterList = baseMapper.selectList(wrapperChapter);

        //2 根据课程id查询课程里面的所有小节
        QueryWrapper<Video> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<Video> videoList = videoService.list(wrapperVideo);

        //创建list集合，用于最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();

        //3 遍历查询章节list集合进行封装
        //编列查询章节list集合
        for (int i = 0; i < chapterList.size(); i++) {
            //每个章节
            Chapter chapter = chapterList.get(i);
            //chapter对象复制到chapterOV里面
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            //把chapterVo放到最终list集合
            finalList.add(chapterVo);

            //创建集合，用于封装章节的小节
            List<VideoVo> list = new ArrayList<>();
            //4 遍历查询小节list集合，进行封装
            for (int m = 0; m < videoList.size(); m++) {
                //得到每个小节
                Video video = videoList.get(m);
                //判断：小节里面chapterid和章节里面id是否一样
                if(video.getChapterId().equals(chapter.getId())){
                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    //放到小节封装集合
                    list.add(videoVo);
                }

            }
            //把封装之后小节list集合，放到章节对象里面
            chapterVo.setChildren(list);
        }
        return finalList;
    }

    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterId章节id  查询小节表，如果查询数据，不进行查询
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        //判断
        if(count > 0 ){ //查询出小节，不进行删除
            throw new GuliException(20001,"不能删除");
        }else {//不能查看数据，进行删除
            //删除章节
            int result = baseMapper.deleteById(chapterId);
            return result > 0;
        }

    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<Chapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
