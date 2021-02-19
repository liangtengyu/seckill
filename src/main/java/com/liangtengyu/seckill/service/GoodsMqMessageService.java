package com.liangtengyu.seckill.service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * @Author: lty
 * @Date: 2021/2/18 09:52
 */
public interface GoodsMqMessageService {
    JSONObject covertToJsonObj(String msg);

    String HandlerSeckill(JSONObject jsonObject);
}
