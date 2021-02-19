package com.liangtengyu.seckill.utils;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by lty on 2018/3/5.
 */
@Slf4j
@ToString
@Data
public class Result {

    private String code;
    private String msg;   //-1:异常    00:成功  01以上是其他原因
    private Object data;
}
