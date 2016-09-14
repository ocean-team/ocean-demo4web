package org.ocean4j.webdemo.service.org.ocean4j.webdemo.test;

import org.junit.Test;
import org.ocean4j.webdemo.model.Customer;

/**
 * Created by richey on 16-9-14.
 */
public class MyTest {

    @Test
    public void testGetTableName(){
        System.out.println(Customer.class.getName());
    }
}
