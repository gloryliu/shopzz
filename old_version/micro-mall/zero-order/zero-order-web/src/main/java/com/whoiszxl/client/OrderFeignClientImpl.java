package com.whoiszxl.client;

import com.whoiszxl.bean.ResponseResult;
import com.whoiszxl.constants.OrderStatusConstants;
import com.whoiszxl.dto.OrderInfoDTO;
import com.whoiszxl.entity.Order;
import com.whoiszxl.feign.*;
import com.whoiszxl.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单feign client
 *
 * @author whoiszxl
 * @date 2021/8/3
 */
@Slf4j
@RestController
public class OrderFeignClientImpl implements OrderFeignClient {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private InventoryFeignClient inventoryFeignClient;

    @Autowired
    private WmsFeignClient wmsFeignClient;

    @Autowired
    private MemberFeignClient memberFeignClient;

    @Override
    public ResponseResult<Boolean> notifyDcPaySuccess(List<Long> orderIds) {
        //1. 更新订单到发货状态
        List<Order> orderList = orderIds.stream().map(orderId -> {
            Order order = new Order();
            order.setId(orderId);
            order.setOrderStatus(OrderStatusConstants.WAIT_FOR_DELIVERY);
            order.setUpdatedBy("admin");
            return order;
        }).collect(Collectors.toList());
        boolean updateFlag = orderService.updateBatchById(orderList);


        for (Long orderId : orderIds) {
            OrderInfoDTO orderInfo = orderService.getOrderInfo(orderId);

            //2. 清空购物车
            ResponseResult<Boolean> clearCartResult = cartFeignClient.clearCheckedCartByMemberId(orderInfo.getMemberId());
            if(!clearCartResult.getData()) {
                log.error("下单成功，但是清空购物车失败了");
            }

            //3. 通知库存中心更新库存
            inventoryFeignClient.notifyPayOrderEvent(orderInfo);

            //4. 通知WMS订单支付成功，更新WMS中心库存，WMS新增出库单
            wmsFeignClient.notifyPayOrderSuccess(orderInfo);

            //5. 更新会员中心等级与积分
            memberFeignClient.notifyPayOrderSuccess(orderInfo.getMemberId(), orderInfo.getTotalAmount());
        }








        return ResponseResult.buildByFlag(updateFlag);
    }
}
