package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    //上传头像到oss
    @Override
    public String uploadFileAvatar(MultipartFile file){

        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String buckedName = ConstantPropertiesUtils.BUCKET_NAME;

        try{
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 获取上传文件输入流。
            InputStream inputStream = file.getInputStream();

            // 获取文件名称
            String filename=file.getOriginalFilename();

            // 1 在文件名称中添加随机唯一的值
            String uuid=UUID.randomUUID().toString().replaceAll("-","");
            filename=uuid+filename;

            // 2 把文件按日期进行分类
            // 2020/9/15/01.jpg
            // 获取当前日期
            String datePath=new DateTime().toString("yyyy/MM/dd");

            //拼接
            filename=datePath+filename;

            // 调用oss方法实现上传，
            // 参数：BucketName;上传到oss的文件路径和文件名称;上传文件输入流
            ossClient.putObject(buckedName, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            // 把上传之后文件路径返回,需要把上传到阿里云oss路径手动拼接出来
            String url="https://"+buckedName+"."+endpoint+"/"+filename;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
