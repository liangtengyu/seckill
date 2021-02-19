package com.liangtengyu.seckill.common.exception;

import com.liangtengyu.seckill.utils.Result;
import com.liangtengyu.seckill.utils.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Author: lty
 * @Date: 2021/2/7 09:30
 * 全局异常捕获
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        if (e instanceof SeckillException) {
            SeckillException e1 = (SeckillException) e;
            return ResultUtil.requestFaild(e1.getData(), e1.getMsg(), e1.getCode());
        } else {
            return ResultUtil.requestFaild(e.getMessage());
        }
    }
}
