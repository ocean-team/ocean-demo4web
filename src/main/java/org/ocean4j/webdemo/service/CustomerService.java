package org.ocean4j.webdemo.service;

import jdk.nashorn.internal.scripts.JD;
import org.ocean4j.webdemo.model.Customer;
import org.ocean4j.webdemo.utils.JdbcUtil;

import java.sql.Connection;
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
     * @return
     */
    public List<Customer> getCustomerList(){
        String sql = "select * from customer";
        return JdbcUtil.queryEntityList(Customer.class,sql);
    }

    /**
     * 获取客户
     * @param id
     * @return
     */
    public Customer getCustomer(long id){
        String sql = " select * from customer where id = ? ";
        return JdbcUtil.queryEntity(Customer.class,sql,id);
    }

    /**
     * 创建客户
     * @param fieldMap
     * @return
     */
    public boolean createCustomer(Map<String,Object> fieldMap){
        return JdbcUtil.insertEntity(Customer.class,fieldMap);
    }

    /**
     * 更新客户
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return JdbcUtil.updateEntity(Customer.class,id,fieldMap);
    }
    public boolean deleteCustomer(long id){
        return JdbcUtil.deleteEntity(Customer.class,id);
    }
}
