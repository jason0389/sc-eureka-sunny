package org.jason.sc.consumer.entity;

import lombok.Data;

@Data
public class Product {

    private int id;
    private String name;
    private int age;
    private String add;
    private String email;

    public Product() {
        this.id = 1;
        this.name = "name";
        this.age = 12;
        this.add = "北京市历史互通";
        this.email = "6666.qq.com";
    }
}
