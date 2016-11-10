package mysql.service;

import com.sun.org.apache.bcel.internal.generic.LUSHR;
import mysql.dao.DBHelper;
import po.OrderPO;
import po.RemainPO;
import po.SeatPO;
import util.CarriageType;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/11/10.
 */
public class TicketHandler {

    private DBHelper helper;

    public TicketHandler() {
        helper = DBHelper.getInstance();
    }

    /**
     *
     * @param trainName
     * @param begin
     * @param end
     * @param type
     * @return
     */
    public synchronized int checkRemainTicket(String trainName, String begin, String end, CarriageType type){
        List<RemainPO> remainPOs =  helper.getRemain(trainName,begin,end,type);

        return getRemainTicket(remainPOs);
    }

    private int getRemainTicket(List<RemainPO> remainPOs){
        Set<Integer> carriageIds = remainPOs
                .stream()
                .map( o -> o.carriageId)
                .collect(Collectors.toSet());

        int remain = 0;
        for (int id : carriageIds){
            int oneRemain = remainPOs
                    .stream()
                    .filter( o -> o.carriageId == id)
                    .map( o -> o.remainNumber)
                    .min(Comparator.comparing(num -> num))
                    .get();
            remain+=oneRemain;
        }

        return remain;
    }

    /**
     *
     * @param userId
     * @param trainName
     * @param begin
     * @param end
     * @param type
     * @return
     */
    public synchronized OrderPO orderTicket(int userId, String trainName, String begin, String end, CarriageType type){

        List<RemainPO> remainPOs = helper.getRemain(trainName,begin,end,type);

        remainPOs.stream().filter( o -> o.type==CarriageType.无座).forEach( o -> System.out.println(o.carriageId+o.type.name()));
        Set<Integer> carriageIds = remainPOs
                .stream()
                .map( o -> o.carriageId)
                .collect(Collectors.toSet());

        for(int id : carriageIds){
            int oneRemain = remainPOs
                    .stream()
                    .filter( o -> o.carriageId == id)
                    .map( o -> o.remainNumber)
                    .min(Comparator.comparing(num -> num))
                    .get();
            if(oneRemain > 0){

                List<RemainPO> updateRemains = remainPOs
                        .stream()
                        .filter( o -> o.carriageId==id)
                        .collect(Collectors.toList());

                List<SeatPO> seatPOs = helper.getSeats(updateRemains.stream().map( o -> o.id).collect(Collectors.toList()));

                /*
                filter to find available seat
                 */
                List<Integer> usedNumbers = seatPOs
                        .stream()
                        .filter(o -> o.used==1)
                        .map(o -> o.number)
                        .collect(Collectors.toList());

                int seatNumber = 0;
                for(int i = 1 ; i <= CarriageType.getNumber(type) ;i++){
                    if(!usedNumbers.contains(i)){
                        seatNumber = i;
                        break;
                    }
                }

                if(seatNumber == 0){
                    break;
                }

                int carriageNumber = helper.getCarriage(id).number;
                OrderPO orderPO = new OrderPO(userId, trainName, begin, end, type, carriageNumber, seatNumber);
                helper.saveOrder(orderPO);

                /*
                update remains
                 */
                int updateNumber = seatNumber;
                List<SeatPO> updateSeats = seatPOs.stream().filter( o -> o.number==updateNumber).collect(Collectors.toList());

                helper.updateSeat(updateSeats);
                updateRemains.forEach( o -> o.remainNumber--);
                helper.updateRemain(updateRemains);

                return orderPO;

            }
        }

        System.err.println(trainName + ":"+ begin + " to "+ end +" ticket sold out");
        return null;
    }

}
