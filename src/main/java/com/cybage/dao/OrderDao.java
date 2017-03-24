package com.cybage.dao;

import java.util.List;

import com.cybage.model.CartInfo;
import com.cybage.model.OrderDetailInfo;
import com.cybage.model.OrderInfo;
import com.cybage.model.PaginationResult;

public interface OrderDao
{
public void saveOrder(CartInfo cartInfo);

public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult,int maxNavigationPage);

public OrderInfo getOrderInfo(String orderId);

public List<OrderDetailInfo> listOrderDetailInfos(String orderId);
}
