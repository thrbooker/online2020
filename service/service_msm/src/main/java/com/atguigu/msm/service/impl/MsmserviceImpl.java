package com.atguigu.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msm.service.Msmservice;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmserviceImpl implements Msmservice {

    /**
     * 发送短信方法
     * @param param
     * @param phone
     * @return
     */
    @Override
    public boolean send(Map<String, Object> param, String phone) {
        if(StringUtils.isEmpty(phone)){
            return false;
        }
        DefaultProfile profile = DefaultProfile.getProfile("default",
                "LTAI4G2bLf9UDTX8SKrdTDrG","CehkgcV6QOgdVFRLmwXwAyIeLEiPDj");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "limerence");
        request.putQueryParameter("TemplateCode", "SMS_210063172");
        //验证码转换数据，转成Json数据传输
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            CommonResponse commonResponse = client.getCommonResponse(request);
            boolean success = commonResponse.getHttpResponse().isSuccess();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
