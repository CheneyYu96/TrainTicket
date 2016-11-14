package mongo;

import java.util.List;

import com.mongodb.client.result.UpdateResult;
import mongo.dao.MongoDB;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
/**
 * Created by yuminchen on 16/11/13.
 */
public class mongoTest {

    MongoDB MongoDB=new MongoDB();

    @Before
    public void before(){
        MongoDB.connect("test", "mongo_test", "127.0.0.1", 27017);
    }

    @Test
    public void testInsert(){
        Document document = new Document();
        Document another = new Document();
        document.append("name", "wang").append("gender", "female");
        another.append("value","test").append("sub",document);
        MongoDB.insert(another);
    }

    @Test
    public void testFindAll(){
        List<Document> results = MongoDB.findAll();
        for(Document doc : results){
            System.out.println(doc.toJson());
        }
    }

    @Test
    public void testFindBy(){
        Document filter = new Document();
        filter.append("value", "test");
        List<Document> results = MongoDB.findBy(filter);
        for(Document doc : results){
            System.out.println(doc.get("sub"));
        }
    }

    @Test
    public void testUpdateOne(){
        Document filter = new Document();
        filter.append("gender", "female");

        //注意update文档里要包含"$set"字段
        Document update = new Document();
        update.append("$set", new Document("gender", "male"));
        UpdateResult result = MongoDB.updateOne(filter, update);
        System.out.println("matched count = " + result.getMatchedCount());
    }

    @Test
    public void testUpdateMany(){
        Document filter = new Document();
        filter.append("gender", "female");

        //注意update文档里要包含"$set"字段
        Document update = new Document();
        update.append("$set", new Document("gender", "male"));
        UpdateResult result = MongoDB.updateMany(filter, update);
        System.out.println("matched count = " + result.getMatchedCount());
    }

    @Test
    public void testReplace(){
        Document filter = new Document();
        filter.append("name", "wang");

        //注意：更新文档时，不需要使用"$set"
        Document replacement = new Document();
        replacement.append("value", 123);
        MongoDB.replace(filter, replacement);
    }

    @Test
    public void testDeleteOne(){
        Document filter = new Document();
        filter.append("name", "li");
        MongoDB.deleteOne(filter);
    }

    @Test
    public void testDeleteMany(){
        Document filter = new Document();
        filter.append("gender", "male");
        MongoDB.deleteMany(filter);
    }
}
