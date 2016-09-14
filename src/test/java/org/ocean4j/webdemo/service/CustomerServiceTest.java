package org.ocean4j.webdemo.service;

import org.junit.Before;
import org.junit.Test;
import org.ocean4j.webdemo.model.Customer;

import java.util.List;

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

    }

    @Test
    public void createCustomer() throws Exception {

    }

    @Test
    public void updateCustomer() throws Exception {

    }

    @Test
    public void deleteCustomer() throws Exception {

    }

}