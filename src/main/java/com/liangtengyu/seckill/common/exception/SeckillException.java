package com.liangtengyu.seckill.common.exception;

import lombok.Data;

/**
 * @Author: lty
 * @Date: 2021/2/7 09:37
 */
@Data
public class SeckillException extends RuntimeException {
    private String msg;
    private String code;
    private Object data;

    public SeckillException(String msg) {
        this.msg = msg;
        this.code = "-1";
        this.data = null;

    }
}
