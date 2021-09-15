package org.jason.sc.consumer.service.impl;

import org.jason.sc.consumer.entity.Order;
import org.jason.sc.consumer.entity.Product;
import org.jason.sc.consumer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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

    @Autowired
    private LoadBalancerClient loadBalancerClient;


    @Override
    public Order getOrderById(Integer id) {
        Order order = new Order();
        order.setId(id);
        order.setAdd("123");
        order.setEmail("123.@qq.com");
        order.setName("123123");
//        order.setProducts(selectProductListByDiscoverClient());
        order.setProducts(selectProductListByLoadBalance());
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
        System.out.println("selectProductListByDiscoverClient: "+ sb.toString());
        ResponseEntity<List<Product>> response = restTemplate.exchange(sb.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });
        return  response.getBody();

    }

    private  List<Product> selectProductListByLoadBalance(){
        StringBuffer sb = null;
        ServiceInstance si = loadBalancerClient.choose("product");
        if(si == null){
            return null;
        }
        sb = new StringBuffer();
        sb.append("http://"+si.getHost()+":"+si.getPort()+"/product/getProduct");
        System.out.println("selectProductListByLoadBalance: "+ sb.toString());
        ResponseEntity<List<Product>> response = restTemplate.exchange(sb.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        });
        return  response.getBody();
    }
}
