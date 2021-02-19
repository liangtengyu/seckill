package com.liangtengyu.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lty
 * @since 2021-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSeckill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String orderId;

    private Long goodsId;

    private Long orderInfoId;


}
