package mysql;

import mysql.service.TicketHandler;
import po.OrderPO;
import util.CarriageType;
import util.Utility;

/**
 * Created by yuminchen on 16/11/10.
 */
public class NormalTicketTest {
    TicketHandler handler;

    public NormalTicketTest() {
        handler = new TicketHandler();
    }

    /**
     * test
     */
    public void checkTicketTest(){
        int num = handler.checkRemainTicket("G5","北京南","南京南", CarriageType.无座);
        System.out.println(num);
    }


    public void orderTicketTest(){
        String name = "G27";
        String[] rou = new String[]{"北京南","德州东","济南西","合肥南","泾县","黄山北","上饶","武夷山东","福州"};

        for(int i = 0; i < 100; i++){
            int first = (int)(Math.random()*8);
            int second = (int)(Math.random()*8);
            if(first==second){
                continue;
            }
            String begin = rou[first];
            String end = rou[second];
            if(first>second){
                begin = rou[second];
                end = rou[first];
            }

            OrderPO orderPO = handler.orderTicket(++Utility.userID,name,begin,end, CarriageType.二等座);
            if(orderPO!=null){
                orderPO.printOrder();
            }
        }

    }

    public static void main(String[] args) {
        NormalTicketTest normalTicketTest = new NormalTicketTest();
        normalTicketTest.checkTicketTest();
        normalTicketTest.orderTicketTest();
    }
}
