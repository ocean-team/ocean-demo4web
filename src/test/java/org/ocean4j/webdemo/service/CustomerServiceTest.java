package org.ocean4j.webdemo.service;

import org.junit.Before;
import org.junit.Test;
import org.ocean4j.webdemo.model.Customer;
import org.ocean4j.webdemo.utils.JdbcUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Created by richey on 16-9-14.
 * CustomerService 单元测试
 */
public class CustomerServiceTest {
    CustomerService customerService;
    @Before
    public void setUp() throws Exception {
        //TODO  初始化数据库
        customerService = new CustomerService();
    }

    @Test
    public void getCustomerList() throws Exception {
        List<Customer> customers = customerService.getCustomerList();
        System.out.println(customers);
    }

    @Test
    public void getCustomer() throws Exception {
        Customer customer = customerService.getCustomer(2);
        System.out.println(customer);
    }

    @Test
    public void createCustomer() throws Exception {
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        fieldMap.put("name","customer3");
        fieldMap.put("contact","kelly");
        fieldMap.put("telephone","1234555");
        fieldMap.put("email","aaa@asd.com");
        fieldMap.put("remark","hello kelly");
        customerService.createCustomer(fieldMap);
    }

    @Test
    public void updateCustomer() throws Exception {
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        fieldMap.put("name","update name");
        customerService.updateCustomer(3,fieldMap);
    }

    @Test
    public void deleteCustomer() throws Exception {
        customerService.deleteCustomer(2);
    }

}