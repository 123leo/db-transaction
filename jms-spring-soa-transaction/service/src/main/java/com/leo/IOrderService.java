package com.leo;

import com.leo.dto.OrderDTO;

/**
 * Created by mavlarn on 2018/2/14.
 */
public interface IOrderService {

    void create(OrderDTO dto);
    OrderDTO getMyOrder(Long id);
}
