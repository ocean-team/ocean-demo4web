package org.ocean4j.webdemo.utils;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by richey on 16-9-14.
 */
public final class JdbcUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUtil.class);
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    /**dbcp数据源*/
    private static final BasicDataSource DBCP_DATA_SOURCE;
    /**HikariCP数据源*/
//    private HikariDataSource HIKARI_DATA_SOURCE;


    private static final  String DRIVER;
    private static final  String URL;
    private static final  String USERNAME;
    private static final  String PASSWORD;

    static {
        Properties conf = PropsUtil.loadProps("jdbc.properties");
        DRIVER = conf.getProperty("jdbc.driver");
        URL = conf.getProperty("jdbc.url");
        USERNAME = conf.getProperty("jdbc.username");
        PASSWORD = conf.getProperty("jdbc.password");

        /**方式1:注册jdbc驱动*/
//        try {
//            Class.forName(DRIVER);
//        } catch (ClassNotFoundException e) {
//            LOGGER.error("can't load jdbc driver ",e);
//        }

        /**方式2:配置dbcp连接池*/
        DBCP_DATA_SOURCE = new BasicDataSource();
        DBCP_DATA_SOURCE.setDriverClassName(DRIVER);
        DBCP_DATA_SOURCE.setUrl(URL);
        DBCP_DATA_SOURCE.setUsername(USERNAME);
        DBCP_DATA_SOURCE.setPassword(PASSWORD);

        /**方式3:HikariCP数据源*/
//        HIKARI_DATA_SOURCE


    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        if(conn == null){
            try{
//                conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                conn = DBCP_DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure ",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return  conn;
    }

    /**
     * 关闭数据库连接
     * 目前采用dbcp连接池的方式获取数据库连接,所以不需要关闭
     */
    public static void closeConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        if(conn != null){
            try{
                conn.close();
            }catch (SQLException e){
                LOGGER.error("close connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList;
        Connection conn = getConnection();
        try {
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entityList failure",e);
            throw new RuntimeException(e);
        }
        //因为目前采用dbcp连接池的方式,所以不需要关闭连接
//        finally {
//            closeConnection();
//        }
        return entityList;
    }

    /**
     * 查询实体
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass,String sql,Object... params){
        T entity;
        try {
            Connection conn = getConnection();
           entity =  QUERY_RUNNER.query(conn,sql,new BeanHandler<T>(entityClass),params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure",e);
            throw new RuntimeException(e);
        }
        //因为目前采用dbcp连接池的方式,所以不需要关闭连接
//        finally {
//            closeConnection();
//        }
        return entity;
    }

    /**
     * 执行查询语句,返回一个键值对的列表,可用于多表查询
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String,Object>> executeQuery(String sql,Object... params){
        List<Map<String,Object>> result;
        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn,sql,new MapListHandler(),params);
        } catch (SQLException e) {
            LOGGER.error("execute query failure",e);
            throw  new RuntimeException(e);
        }
        //因为目前采用dbcp连接池的方式,所以不需要关闭连接
//        finally {
//            closeConnection();
//        }
        return result;
    }

    /**
     * 执行更新语句,包括update、insert、delete等
     * @param sql
     * @param params
     * @return 更新成功的行数
     */
    public static int executeUpdate(String sql,Object... params){
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn,sql,params);
        } catch (SQLException e) {
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        }
        //因为目前采用dbcp连接池的方式,所以不需要关闭连接
//        finally {
//            closeConnection();
//        }
        return rows;
    }

    /**
     * 插入实体
     * @param entityClass
     * @param fieldMap  键值对,用来描述要插入的实体
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }
        String sql = " insert into " + getTablename(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for(String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");
        sql += columns + " values " + values;
        Object[] params = fieldMap.values().toArray();
        return  executeUpdate(sql,params) == 1;
    }

    /**
     * 更新实体
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            LOGGER.error("can not update eneity:fieldMap is empty");
            return false;
        }
        String sql = "update " + getTablename(entityClass)+ " set ";
        StringBuilder columns = new StringBuilder();
        for(String fileName : fieldMap.keySet()){
            columns.append(fileName).append("=?, ");
        }
        sql += columns.substring(0,columns.lastIndexOf(", "))+" where id = ? ";
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return  executeUpdate(sql,params) == 1;
    }

    /**
     * 删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = " delete from " + getTablename(entityClass) + " where id = ? ";
        return  executeUpdate(sql,id) == 1;
    }

    public static String getTablename(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

    /**
     * 执行sql文件
     * @param filePath
     */
    public static void executeSqlFile(String filePath){
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while((sql=reader.readLine())!=null){
                executeUpdate(sql);
            }
        } catch (Exception e) {
            LOGGER.error("execute sql file failure",e);
            throw  new RuntimeException(e);
        }
    }
}
