package com.atguigu.springcloud.Controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;
    @Resource
    private DiscoveryClient discoveryClient;
    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("插入结果为:"+result);
        if (result>0){
            return new CommonResult(200,"插入成功server port:"+serverPort,result);
        }else {
            return new CommonResult(444,":插入失败");
        }
    }
    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id) {
        Payment payment= paymentService.getPaymentById(id);
        log.info("插入结果为:"+payment);
        if (payment!=null){
            return new CommonResult(200,"查询成功,server port:"+serverPort,payment);
        }else {
            return new CommonResult(444,"查询失败"+id,null);
        }
    }

    /**
     * 获取该服务名称下所有微服务
     * @return
     */
    @GetMapping(value = "/payment/discovery")
    public Object discover(){
        //获得所有服务名称这里返回cloud-payment-service cloud-order-service
        List<String> list= discoveryClient.getServices();
        for (String element:list){
            log.info("~~~~~~element:"+element);
        }
        //获得这个"CLOUD-PAYMENT-SERVICE"服务的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance:instances){
            //打印该实例的服务名称，主机地址，端口，url
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+ instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }
    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }
    //是用Feign模拟超时
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return serverPort;
    }
}
