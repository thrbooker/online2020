package com.atguigu.edu.service.impl;

import com.atguigu.edu.client.VodClient;
import com.atguigu.edu.entity.Video;
import com.atguigu.edu.mapper.VideoMapper;
import com.atguigu.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    @Override
    public void removeVideoByCourseId(String courseId) {
        //1.根据课程id查询课程所有的视频id
        QueryWrapper<Video> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.select("video_source_id");
        List<Video> videoList = baseMapper.selectList(wrapperVideo);

        //List<EduVideo>变成List<String>
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++){
            Video video = videoList.get(i);
            String videoSourceId = video.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                //放到videoIds集合里面
                videoIds.add(videoSourceId);
            }

        }
        //根据多个视频id删除多个视频id
        if(videoIds.size() > 0){
            vodClient.deleteBatch(videoIds);
        }
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
