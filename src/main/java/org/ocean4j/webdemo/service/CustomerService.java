package org.ocean4j.webdemo.service;

import org.ocean4j.webdemo.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * Created by richey on 16-9-14.
 * 标准的MVC框架是没有服务层的,我们将该层作为衔接控制层与数据库之间的桥梁
 * 提供客户数据服务
 */
public class CustomerService {
    /**
     * 获取客户列表
     * @param keyword
     * @return
     */
    public List<Customer> getCustomerList(String keyword){
        //TODO
        return null;
    }

    /**
     * 获取客户
     * @param id
     * @return
     */
    public Customer getCustomer(long id){
        //TODO
        return null;
    }

    /**
     * 创建客户
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String,Object> fieldMap){
        //TODO
        return false;
    }

    /**
     * 更新客户
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        //TODO
        return false;
    }
    public boolean deleteCustomer(long id){
        //TODO
        return false;
    }
}
