package po;

import util.CarriageType;

/**
 * Created by yuminchen on 16/11/10.
 */
public class OrderPO {

    public int userId;
    public String trainName;
    public String begin;
    public String end;
    public CarriageType type;
    public int number;
    public int seat;


    /**
     *
     * @param userId
     * @param trainName
     * @param begin
     * @param end
     * @param type
     * @param number
     * @param seat
     */
    public OrderPO(int userId, String trainName, String begin, String end, CarriageType type, int number, int seat) {
        this.userId = userId;
        this.trainName = trainName;
        this.begin = begin;
        this.end = end;
        this.type = type;
        this.number = number;
        this.seat = seat;
    }

    public void printOrder(){
        System.out.println(userId + " : " + begin + "-- " + trainName +" ->" +  end +
                "\n"+type.name() + ": "+ number+"车 " +seat+"座");
    }
}
