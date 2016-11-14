package mongo.dao;

import util.MongoUtility;

/**
 * Created by yuminchen on 16/11/13.
 */
public class OrderDAO extends MongoDB{
    public OrderDAO() {
        connect(MongoUtility.databaseName,MongoUtility.order,MongoUtility.hostName,MongoUtility.port);
    }
}
