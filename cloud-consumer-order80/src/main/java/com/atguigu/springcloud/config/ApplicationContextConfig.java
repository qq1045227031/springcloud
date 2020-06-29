package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {
    //让restTemplate有负载均衡的能力
    //使用自己编写的调用规则要注释，如果注释了这也没有使用自己编写的调用规则会报错
    //和主配置类的自定义规则@RibbonClient应该不冲突
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
