package mongo.dao;

import util.MongoUtility;

/**
 * Created by yuminchen on 16/11/13.
 */
public class TrainDAO extends MongoDB{

    public TrainDAO() {
        connect(MongoUtility.databaseName,MongoUtility.train,MongoUtility.hostName,MongoUtility.port);
    }

}
