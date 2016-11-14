package mongo.dao;

import util.MongoUtility;

/**
 * Created by yuminchen on 16/11/13.
 */
public class CarriageDAO extends MongoDB{

    public CarriageDAO() {
        connect(MongoUtility.databaseName,MongoUtility.carriage,MongoUtility.hostName,MongoUtility.port);
    }
}
