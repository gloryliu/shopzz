package com.whoiszxl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whoiszxl.entity.member.MemberPO;
import com.whoiszxl.entity.order.Order;

import java.util.Collection;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2022-04-09
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 批量插入（mysql）
     * @param entityList
     * @return
     */
    Integer insertBatchSomeColumn(Collection<Order> entityList);
}