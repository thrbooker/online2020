package com.atguigu.msm.service;

import java.util.Map;

public interface Msmservice {
    boolean send(Map<String, Object> param, String phone);
}
