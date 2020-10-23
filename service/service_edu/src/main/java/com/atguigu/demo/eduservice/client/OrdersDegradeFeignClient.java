package com.atguigu.demo.eduservice.client;

public class OrdersDegradeFeignClient implements OrdersClient{
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
