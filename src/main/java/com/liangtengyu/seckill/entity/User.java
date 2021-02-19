package com.liangtengyu.seckill.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lty
 * @since 2021-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * MD5(MD5(pass明文+固定salt)+salt
     */
    private String password;

    /**
     * 混淆盐
     */
    private String salt;

    /**
     * 头像，云存储的ID
     */
    private String head;

    /**
     * 注册时间
     */
    private LocalDateTime registerDate;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;


    /**
     * address
     */
    private String address;


}
