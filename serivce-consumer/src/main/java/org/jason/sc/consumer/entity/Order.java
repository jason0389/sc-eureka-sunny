package org.jason.sc.consumer.entity;

import lombok.Data;

import java.util.List;

@Data
public class Order {

    private int id;
    private String name;
    private int age;
    private String add;
    private String email;
    private List<Product> products;

}
