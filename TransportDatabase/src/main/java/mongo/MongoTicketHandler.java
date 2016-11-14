package mongo;

import com.mongodb.client.result.UpdateResult;
import mongo.dao.CarriageDAO;
import mongo.dao.OrderDAO;
import mongo.dao.TrainDAO;
import org.bson.conversions.Bson;

import org.bson.Document;
import po.OrderPO;
import util.CarriageType;

import java.util.List;

/**
 * Created by yuminchen on 16/11/13.
 */
public class MongoTicketHandler {

    OrderDAO orderDAO;
    TrainDAO trainDAO;
    CarriageDAO carriageDAO;

    public MongoTicketHandler() {
        orderDAO = new OrderDAO();
        trainDAO = new TrainDAO();
        carriageDAO = new CarriageDAO();
    }

    public synchronized int checkRemainTicket(String trainName, String begin, String end, CarriageType type) {
        Document filter = new Document();
        filter.append("name",trainName);
        Document train = trainDAO.findBy(filter).get(0);

        int beginIndex = 0;
        int endIndex = 0;
        for(int i = 1; i <= train.size()-2; i ++){
            if(train.get(i+"").equals(begin)){
                beginIndex = i;
                continue;
            }
            if(train.get(i+"").equals(end)){
                endIndex = i;
                break;
            }
        }

        Document carriageFilter = new Document();
        carriageFilter.append("name",trainName).append("type",type.name());
        List<Document> carriages = carriageDAO.findBy(carriageFilter);

        int availableSeat = 0;
        for(Document carriage : carriages){
            for(int i = 1; i <= CarriageType.getNumber(type); i++){
                if(hasSeat((Document)carriage.get(i+""),beginIndex,endIndex)){
                    availableSeat++;
                }
            }
        }

        return availableSeat;
    }


    private boolean hasSeat(Document seat, int begin , int end){

        for(int i = begin; i<=end-1;i++) {
            int isUsed = (int)seat.get(i + "");
            if(isUsed==1){
                return false;
            }
        }

        return true;
    }

    public synchronized OrderPO orderTicket(int userId, String trainName, String begin, String end, CarriageType type) {
        Document filter = new Document();
        filter.append("name",trainName);
        Document train = trainDAO.findBy(filter).get(0);

        int beginIndex = 0;
        int endIndex = 0;
        for(int i = 1; i <= train.size()-2; i ++){
            if(train.get(i+"").equals(begin)){
                beginIndex = i;
                continue;
            }
            if(train.get(i+"").equals(end)){
                endIndex = i;
                break;
            }
        }

        Document carriageFilter = new Document();
        carriageFilter.append("name",trainName).append("type",type.name());
        List<Document> carriages = carriageDAO.findBy(carriageFilter);

        OrderPO orderPO = null;
        for(Document carriage : carriages){
            for(int i = 1; i <= CarriageType.getNumber(type); i++){
                if(hasSeat((Document)carriage.get(i+""),beginIndex,endIndex)){
                    int carriageNum = (int)carriage.get("number");
                    int seatNum = i;
                    orderPO = new OrderPO(userId,trainName,begin,end,type,carriageNum,seatNum);

                    /*
                    save order
                     */
                    Document order = new Document();
                    order.append("user",userId);
                    order.append("train",trainName);
                    order.append("begin",begin);
                    order.append("end",end);
                    order.append("type",type.name());
                    order.append("carriageNum",carriageNum);
                    order.append("seatNum",seatNum);

                    orderDAO.insert(order);


                    /*
                    update seat
                     */
                    Document updateSeat = new Document();
                    Document oldSeat = (Document) carriage.get(i+"");
                    int length = oldSeat.size();

                    for(int j = 1 ; j<=length;j++){
                        if(j>=beginIndex&&j<endIndex){
                            updateSeat.put(j+"",1);
                        }
                        else {
                            updateSeat.put(j + "", oldSeat.get(j+""));
                        }
                    }

                    Document updateFilter = new Document();
                    updateFilter.append("name",carriage.get("name")).append("number",carriage.get("number"));


                    Document update = new Document();
                    update.append("$set", new Document(i+"", updateSeat));

                    UpdateResult result = carriageDAO.updateOne(updateFilter, update);
//                    System.out.println("matched count = " + result.getMatchedCount());

                    return orderPO;



                }
            }
        }

        return orderPO;
    }

    public static void main(String[] args) {
        MongoTicketHandler handler = new MongoTicketHandler();
//        System.out.println(handler.checkRemainTicket("G5", "北京南","南京南",CarriageType.二等座));

        handler.orderTicket(1,"G5", "北京南","南京南",CarriageType.二等座).printOrder();
    }
}