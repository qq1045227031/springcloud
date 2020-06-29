package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLB implements LoadBalancer {
    //初始值是0
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    //这个方法的作用是返回第几次访问，通过原子类增加线程安全
    public final int getAndIncrement(){
        int current;
        int next;
        do{
            //获得当前current
            current = this.atomicInteger.get();
            //获得当前next
            next = current>=2147483647?0:current+1;

        }while (!this.atomicInteger.compareAndSet(current,next));
        System.out.println("****next"+next);
        return next;
    }
    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        //处理该次请求的服务器坐标=访问次数%服务器数量
        int index = getAndIncrement()%serviceInstances.size();//算出处理该次请求服务器坐标
       //返回的是处理这个请求的客户机（有了索引坐标就可以得到台客户机）
        return serviceInstances.get(index);
    }
}
