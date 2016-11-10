package mysql;

import mysql.dao.DBHelper;
import po.*;
import util.CarriageType;
import util.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuminchen on 16/11/8.
 */
public class DataFactory {

    private DBHelper helper;

    public DataFactory() {
        helper = DBHelper.getInstance();
        initialData();
    }

    /**
     * add data to database, including routine and train, carriage and relationship between train and carriage
     */
    private void initialData(){

        String routineFileName = "src/main/resources/routine.txt";

        File routineFile = new File(routineFileName);

        try {
            BufferedReader br = new BufferedReader(new FileReader(routineFile));
            String line ;
            while ((line=br.readLine())!=null){

                String[] name_rout = line.split(" ");
                String name = name_rout[0];
                /*
                decide the length of the train : 8 / 16
                 */
                int num = 8;
                if(Math.random()>0.5) {
                    num = 16;
                }
                else {
                    num = 8;
                }

                helper.insertOneTrain(new TrainPO(num,name));

                /*
                1/4 特等
                1/4 一等
                1/2 二等
                 */
                List<CarriagePO> carriagePOs = new ArrayList<>();
                for(int i = 0; i< num; i++){
                    if(i<num/4){
                        carriagePOs.add(new CarriagePO(++Utility.carriageID,CarriageType.特等座,name,i+1));
                    }
                    else if(i>=num/4&&i<num/2){
                        carriagePOs.add(new CarriagePO(++Utility.carriageID,CarriageType.一等座,name,i+1));
                    }
                    else {
                        carriagePOs.add(new CarriagePO(++Utility.carriageID,CarriageType.二等座,name,i+1));
                    }
                }


                String[] routines = name_rout[1].split("-");
                List<RoutinePO> routinePOs = new ArrayList<>();

                for(int index = 0; index < routines.length - 1; index++ ){
                    routinePOs.add(new RoutinePO(++Utility.routineID, name,routines[index], routines[index+1]));
                }

                /*
                add remain
                 */
                List<RemainPO> remainPOs = new ArrayList<>();
                List<SeatPO> seatPOs = new ArrayList<>();
                for(CarriagePO carriagePO : carriagePOs){
                    int number = CarriageType.getNumber(carriagePO.type);
                    routinePOs.forEach( o -> {
                            remainPOs.add(new RemainPO(++Utility.remainID,o.id,carriagePO.id,carriagePO.type,number,number));
                            for(int i = 1 ;i <= number; i++){
                                int seatNumber = i;
                                seatPOs.add(new SeatPO(Utility.remainID,seatNumber,0));
                            }

                    });

                    if(carriagePO.type == CarriageType.二等座){
                        routinePOs.forEach( o -> remainPOs.add(new RemainPO(++Utility.remainID,o.id,carriagePO.id,CarriageType.无座,20,20)));
                    }
                }

                helper.insertCarriage(carriagePOs);
                helper.insertRoutine(routinePOs);
                helper.insertRemain(remainPOs);
                helper.insertSeats(seatPOs);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DataFactory factory = new DataFactory();
    }
}
