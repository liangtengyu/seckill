package com.liangtengyu.seckill.utils;


/**
 * Created by lty on 2018/3/5.
 */
public class ResultUtil {

    public static String REQUESTFAILD = "request faild";  //请求失败


    /**
     * 请求成功
     * @param data
     * @return
     */
    public static Result
    requestSuccess(Object data) {
        Result result = new Result();
        result.setCode("00");
        result.setMsg("操作成功!");
        if(data==null||data.equals("")){
            result.setData("");
        }else{
            //RSA加密
            /*result.setData(splitEncrypt(data));*/

            result.setData(data);
        }
        return  result;
    }
    /**
     * 请求成功
     * @param data
     * @return
     */
    public static Result
    requestSuccess(Object data,Object count) {
        Result result = new Result();
        result.setCode("00");
        result.setMsg("request success");
        result.setData(data);
        return  result;
    }




    /**
     * 请求成功
     * @param
     * @return
     */
    public static Result success(Object o){
        Result result = new Result();
        result.setCode("00");
        result.setMsg("request success");
        result.setData(o);
        return  result;
    }

    /**
     * 请求失败
     * @return
     */
    public static  Result requestFaild(String msg){
        Result result = new Result();
        result.setCode("-1");
        result.setData(null);
        if(msg==null||"".equals(msg)){
            result.setMsg(REQUESTFAILD);
        }else{
            result.setMsg(msg);
        }
        return  result;
    }


    /**
     * 请求失败
     * @return
     */
    public static  Result requestFaild(String msg, String code){
        Result result = new Result();
        result.setData(null);
        if(msg==null||"".equals(msg)){
            result.setCode("-1");
        }else{
            result.setCode(code);
        }
        if(msg==null||"".equals(msg)){
            result.setMsg(REQUESTFAILD);
        }else{
            result.setMsg(msg);
        }
        return  result;
    }

    /**
     * 请求失败
     * @return
     */
    public static  Result requestFaild(Object data, String msg, String code){
        Result result = new Result();
        if(data==null||"".equals(data)){
            result.setData(null);
        }else{
            result.setData(data);
        }
        if(msg==null||"".equals(msg)){
            result.setCode("-1");
        }else{
            result.setCode(code);
        }
        if(msg==null||"".equals(msg)){
            result.setMsg(REQUESTFAILD);
        }else{
            result.setMsg(msg);
        }
        return  result;
    }



}
