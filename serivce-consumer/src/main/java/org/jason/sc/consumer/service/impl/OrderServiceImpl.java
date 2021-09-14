package org.jason.sc.consumer.service.impl;

import org.jason.sc.consumer.entity.Order;
import org.jason.sc.consumer.entity.Product;
import org.jason.sc.consumer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;


    @Override
    public Order getOrderById(Integer id) {
        Order order = new Order();
        order.setId(id);
        order.setAdd("123");
        order.setEmail("123.@qq.com");
        order.setName("123123");
        order.setProducts(selectProductListByDiscoverClient());
        return order;
    }

    private List<Product> selectProductListByDiscoverClient(){
        StringBuffer sb = new StringBuffer();
        List<String> serviceIds = discoveryClient.getServices();
        if(CollectionUtils.isEmpty(serviceIds)){
            return null;
        }
        //根据服务名获取服务
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("product");
        if(CollectionUtils.isEmpty(serviceInstances)){
            return null;
        }

        ServiceInstance si = serviceInstances.get(0);
        sb.append("http://"+si.getHost()+":"+si.getPort()+"/product/getProduct");
        ResponseEntity<List<Product>> response = restTemplate.exchange(sb.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });
        return  response.getBody();

    }
}
