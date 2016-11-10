package mysql.dao;

import po.*;
import util.CarriageType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuminchen on 16/11/8.
 */
public class DBHelper {

    private static DBHelper helper;

    private ConnectionHelper connectionHelper;

    private String driven = "com.mysql.jdbc.Driver";

    private String url = "jdbc:mysql://localhost:8889/transport?useUnicode=true&characterEncoding=utf-8";

    private String userName = "root";

    private String password = "root";

    public static DBHelper getInstance(){
        if(helper==null){
            helper = new DBHelper();
        }
        return helper;
    }

    private DBHelper(){
        connectionHelper = ConnectionHelper.getInstance();
    }

    /**
     *
     * @param trainName
     * @param begin
     * @param end
     * @param type
     * @return
     */
    public synchronized List<RemainPO> getRemain(String trainName, String begin, String end, CarriageType type){

        Connection connection = connectionHelper.getConnection();

//        try {
//            connection.setAutoCommit(false);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        List<RoutinePO> routinePOs = new ArrayList<>();
        String routineSql = "SELECT * FROM routine WHERE name = '" + trainName +"'";

        try {

            PreparedStatement ps = connection.prepareStatement(routineSql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                RoutinePO routinePO = new RoutinePO();
                routinePO.id = rs.getInt(1);
                routinePO.name = rs.getString(2);
                routinePO.begin = rs.getString(3);
                routinePO.end = rs.getString(4);
                routinePOs.add(routinePO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Integer> routineIds = new ArrayList<>();
        boolean isInRoutine = false;
        for(RoutinePO po : routinePOs){
            if(po.begin.equals(begin)){
                isInRoutine = true;
            }
            if(isInRoutine == true){
                routineIds.add(po.id);
            }
            if(po.end.equals(end)){
                break;
            }
        }

        /*
        get remain
         */

        List<RemainPO> remainPOs = new ArrayList<>();
        for(int id : routineIds) {

            String remainSql = "SELECT * FROM remain WHERE routine_id = " + id;
            try {

                PreparedStatement pst = connection.prepareStatement(remainSql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()){
                    RemainPO remainPO = new RemainPO();
                    remainPO.id = rs.getInt(1);
                    remainPO.routineId = rs.getInt(2);
                    remainPO.carriageId = rs.getInt(3);
                    remainPO.type = CarriageType.valueOf(rs.getString(4));
                    if(remainPO.type != type){
                        continue;
                    }
                    remainPO.remainNumber = rs.getInt(5);
                    remainPO.allNumber = rs.getInt(6);

                    remainPOs.add(remainPO);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remainPOs;

    }


    /**
     *
     * @param remainIds
     * @return
     */
    public List<SeatPO> getSeats(List<Integer> remainIds){
        Connection connection = connectionHelper.getConnection();
        String sql = "SELECT * FROM seat WHERE remain_id = ";
        List<SeatPO> seatPOs = new ArrayList<>();

        for(int id : remainIds){

            try {
                PreparedStatement ps = connection.prepareStatement(sql+id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()){
                    SeatPO seatPO = new SeatPO();

                    seatPO.remainId = rs.getInt(1);
                    seatPO.number = rs.getInt(2);
                    seatPO.used = rs.getInt(3);

                    seatPOs.add(seatPO);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seatPOs;
    }
    /**
     *
     * @param carriageId
     * @return
     */
    public synchronized CarriagePO getCarriage(int carriageId){

        Connection connection = connectionHelper.getConnection();
        String sql = "SELECT * FROM carriage WHERE id = " + carriageId;
        CarriagePO carriagePO = new CarriagePO();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                carriagePO.id = rs.getInt(1);
                carriagePO.type = CarriageType.valueOf(rs.getString(2));
                carriagePO.train_name = rs.getString(3);
                carriagePO.number = rs.getInt(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carriagePO;

    }


    /**
     *
     * @param trainPOList
     */
    public void insertTrain(List<TrainPO> trainPOList){
        trainPOList.forEach( o -> insertOneTrain(o));
    }

    public  void insertOneTrain(TrainPO trainPO){
        Connection connection = connectionHelper.getConnection();

        try {
            PreparedStatement pstmt = connection.prepareStatement("insert into `train` (name, carriage_num) VALUES (?,?)");
            pstmt.clearParameters();
            pstmt.setString(1, trainPO.name);
            pstmt.setInt(2, trainPO.number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param routinePOList
     */
    public void insertRoutine(List<RoutinePO> routinePOList){
        Connection connection = connectionHelper.getConnection();

        for(RoutinePO routinePO : routinePOList){
            try {
                PreparedStatement pstmt = connection.prepareStatement("insert into `routine` (id, name, begin, end) VALUES (?,?,?,?)");
                pstmt.clearParameters();
                pstmt.setInt(1,routinePO.id);
                pstmt.setString(2, routinePO.name);
                pstmt.setString(3, routinePO.begin);
                pstmt.setString(4, routinePO.end);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param carriagePOList
     */
    public  void insertCarriage(List<CarriagePO> carriagePOList){
        Connection connection = connectionHelper.getConnection();

        for(CarriagePO carriagePO : carriagePOList){
            try {
                PreparedStatement pstmt = connection.prepareStatement("insert into carriage(id,type,train_name,number) VALUES (?,?,?,?)");
                pstmt.clearParameters();
                pstmt.setInt(1, carriagePO.id);
                pstmt.setString(2, carriagePO.type.name());
                pstmt.setString(3, carriagePO.train_name);
                pstmt.setInt(4, carriagePO.number);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param remainPOs
     */
    public  void insertRemain(List<RemainPO> remainPOs){
        Connection connection = connectionHelper.getConnection();

        for(RemainPO remainPO : remainPOs) {

            try {
                PreparedStatement pstmt = connection.prepareStatement("insert into remain(id,routine_id,carriage_id,type,remainNumber, allNumber) VALUES (?,?,?,?,?,?)");
                pstmt.clearParameters();
                pstmt.setInt(1, remainPO.id);
                pstmt.setInt(2, remainPO.routineId);
                pstmt.setInt(3, remainPO.carriageId);
                pstmt.setString(4, remainPO.type.name());
                pstmt.setInt(5, remainPO.remainNumber);
                pstmt.setInt(6, remainPO.allNumber);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param seatPOs
     */
    public void insertSeats(List<SeatPO> seatPOs){
        Connection connection = connectionHelper.getConnection();

        String sql = "insert into seat(remain_id,number,used) VALUES (?,?,?)";

        for(SeatPO seatPO : seatPOs) {
            try {
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.clearParameters();
                pstmt.setInt(1, seatPO.remainId);
                pstmt.setInt(2, seatPO.number);
                pstmt.setInt(3, seatPO.used);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param orderPO
     */
    public  void saveOrder(OrderPO orderPO){
        Connection connection = connectionHelper.getConnection();

        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "insert into `order` (userId,trainName,BEGIN ,END ,type,number,seat) VALUES (?,?,?,?,?,?,?)");
            pstmt.clearParameters();
            pstmt.setInt(1, orderPO.userId);
            pstmt.setString(2, orderPO.trainName);
            pstmt.setString(3,orderPO.begin);
            pstmt.setString(4,orderPO.end);
            pstmt.setString(5,orderPO.type.name());
            pstmt.setInt(6,orderPO.number);
            pstmt.setInt(7,orderPO.seat);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param remainPOs
     */
    public synchronized void updateRemain(List<RemainPO> remainPOs){
        Connection connection = connectionHelper.getConnection();

        String updateSql = "Update remain set remainNumber = ? where id = ?";
        for(RemainPO remainPO : remainPOs){
            try {
                PreparedStatement pstmt = connection.prepareStatement(updateSql);
                pstmt.clearParameters();
                pstmt.setInt(1, remainPO.remainNumber);
                pstmt.setInt(2, remainPO.id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public synchronized void updateSeat(List<SeatPO> seatPOs){
        Connection connection = connectionHelper.getConnection();

        String updateSql = "Update seat set used = ? where remain_id = ? and number = ?";
        for(SeatPO seatPO : seatPOs){
            try {
                PreparedStatement pstmt = connection.prepareStatement(updateSql);
                pstmt.clearParameters();
                pstmt.setInt(1, 1);
                pstmt.setInt(2, seatPO.remainId);
                pstmt.setInt(3, seatPO.number);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
//        DBHelper.getInstance().insertOneCarriage(new CarriagePO(CarriageType.特等座));
    }
}
