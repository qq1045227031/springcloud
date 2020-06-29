package com.atguigu.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //当访问/bili转发到https://www.bilibili.com/   这个路由id是 path_route_atguigu
        routes.route("path_route_atguigu",
                r->r.path("/BV18E411x7eT?p=70")
        .uri("https://www.bilibili.com/video/BV18E411x7eT?p=70")).build();
        return routes.build();
    }
}
