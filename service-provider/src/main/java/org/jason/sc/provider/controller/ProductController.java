package org.jason.sc.provider.controller;

import org.jason.sc.provider.entity.Product;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @RequestMapping(value = "getProduct")
    public List<Product> getProduct() {
        Product product = new Product();
        List result = new ArrayList();
        result.add(product);
        return result;
    }

    @RequestMapping(value = "/home")
    public String home() throws Exception {
        System.out.println("123111");
        throw new Exception("101，错误");
    }
}
