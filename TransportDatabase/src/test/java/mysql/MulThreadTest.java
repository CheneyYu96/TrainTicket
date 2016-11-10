package mysql;

import mysql.service.TicketHandler;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import po.OrderPO;
import util.CarriageType;
import util.Utility;

/**
 * Created by yuminchen on 16/11/10.
 */
public class MulThreadTest {
    String name = "G33";
    String[] rous = new String[]{"北京南","沧州西","济南西","滕州东","徐州东","宿州东","南京南",
            "宜兴","湖州","杭州东","义乌","金华","衢州","玉山南","上饶","鹰潭北","进贤南","南昌西"};

    TicketHandler handler;
    @BeforeTest
    public void initial(){
        handler = new TicketHandler();
    }


    @Test(invocationCount = 100, threadPoolSize = 50)
    public void orderTicket() {
        int first = (int)(Math.random()*8);
        int second = (int)(Math.random()*8);
        if(first==second){
            return;
        }
        String begin = rous[first];
        String end = rous[second];
        if(first>second){
            begin = rous[second];
            end = rous[first];
        }

        OrderPO orderPO = handler.orderTicket(++Utility.userID,name,begin,end, CarriageType.二等座);
        if(orderPO!=null){
            orderPO.printOrder();
        }
    }

}
