package com.atguigu.edu.controller;


import com.atguigu.commonUtils.R;
import com.atguigu.edu.client.VodClient;
import com.atguigu.edu.entity.Video;
import com.atguigu.edu.entity.chapter.ChapterVo;
import com.atguigu.edu.service.VideoService;
import com.atguigu.servicebase.excetionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class VideoController {

   @Autowired
   private VideoService videoService;

   //注入vodClient
   @Autowired
   private VodClient vodClient;

   @GetMapping("getVideoById/{id}")
   public R getVideo(@PathVariable String id){
       Video video = videoService.getById(id);
       return R.ok().data("video",video);
   }

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody Video video){
        videoService.save(video);
        return R.ok();
    }

    //修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody Video video){
       videoService.updateById(video);
       return R.ok();
    }

    //删除小节
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节id获取视频id，调用方法实现删除
        Video video = videoService.getById(id);
        String videoSourceId = video.getVideoSourceId();

        if(!StringUtils.isEmpty(videoSourceId)){
            //根据视频id，远程调用实现视频删除
            R result = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode() == 20001){
                throw new GuliException(20001,"删除视频失败，熔断器执行");
            }
        }
        //删除小节
        videoService.removeById(id);
        return R.ok();
    }

}

