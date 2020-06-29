package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {
    public String paymentInfo_OK(Integer id){
        return "线程池"+Thread.currentThread().getName()+"paymentInfo_OK,id:"+id+"\t"+"o(∩_∩)o 哈哈";
    }
    //fallbackMethod代表异常后处理的方法，commandProperties设置超时时间
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",
            commandProperties ={@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="3000")})
    public String paymentInfo_Timeout(Integer id){
//        1.计算异常,也会引发降级，执行下面的方法
        int age =10/0;
//        2.超时异常
        int timeOut = 5;
        try {
            TimeUnit.SECONDS.sleep(timeOut);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池"+Thread.currentThread().getName()+"paymentInfo_OK,id:"+id+"\t"+"o(∩_∩)o 哈哈";
    }
    //上面报错后执行下面
    public String paymentInfo_TimeOutHandler(Integer id){
        return "线程池:"+Thread.currentThread().getName()+"paymentInfo_TimeOutHandler id:"+id+"(┬＿┬)";

    }
    // 服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),              //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),    //请求数达到后才计算
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //休眠时间窗
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),  //错误率达到多少跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if(id < 0){
            throw  new RuntimeException("****id 不能为负数");
        }
        String serialNumber = IdUtil.simpleUUID();

        return  Thread.currentThread().getName() + "\t" + "调用成功，流水号：" + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能为负数,请稍后再试， o(╥﹏╥)o id: " + id;
    }
}
