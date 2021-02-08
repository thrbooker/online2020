package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传文件到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        String endpoint = ConstantPropertiesUtils.END_POINT;

        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;

        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;

        String bucketName = ConstantPropertiesUtils.BUCKE_TNAME;



        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            InputStream inputStream = file.getInputStream();

            //1、在文件名称里面生成唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            String fileName = uuid + file.getOriginalFilename();

            //2.把文件按照日期分类
            String datepPath = new DateTime().toString("yyyy/MM/dd");
            fileName = datepPath + "/" + fileName;

            //1.Bucket名称
            //2.获取文件名称
            //3.上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //需要把上传到阿里云oss路径手动拼接出来
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

