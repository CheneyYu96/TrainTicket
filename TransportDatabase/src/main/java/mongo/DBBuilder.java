package mongo;

import mongo.dao.CarriageDAO;
import mongo.dao.TrainDAO;
import org.bson.Document;
import po.*;
import util.CarriageType;
import util.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuminchen on 16/11/13.
 */
public class DBBuilder {

    TrainDAO trainDAO;
    CarriageDAO carriageDAO;

    public DBBuilder() {
        trainDAO = new TrainDAO();
        carriageDAO = new CarriageDAO();
    }

    private void initialData(){

        String routineFileName = "src/main/resources/routine.txt";

        File routineFile = new File(routineFileName);

        try {
            BufferedReader br = new BufferedReader(new FileReader(routineFile));
            String line ;
            while ((line=br.readLine())!=null){
                Document train = new Document();

                String[] name_rout = line.split(" ");
                String name = name_rout[0];

                train.append("name",name);


                String[] routines = name_rout[1].split("-");

                for(int index = 0; index < routines.length ; index++ ){
                    train.append((index+1)+"",routines[index]);
                }

                trainDAO.insert(train);

                  /*
                decide the length of the train : 8 / 16
                 */
                int num = 8;
                if(Math.random()>0.5) {
                    num = 16;
                }

                /*
                1/4 特等
                1/4 一等
                1/2 二等
                 */
                for(int i = 0; i< num; i++){
                    Document carriage = new Document();
                    carriage.append("name",name);
                    carriage.append("number",i+1);
                    if(i<num/4){
                        carriage.append("type","特等座");
                        for(int j = 1 ; j<= CarriageType.getNumber(CarriageType.特等座) ; j++){
                            Document seat = new Document();
                            for(int k = 1; k <= routines.length-1; k++){
                                seat.append(k+"", 0);
                            }
                            carriage.append(j+"",seat);
                        }
                    }
                    else if(i>=num/4&&i<num/2){
                        carriage.append("type","一等座");

                        for(int j = 1 ; j<= CarriageType.getNumber(CarriageType.一等座) ; j++){
                            Document seat = new Document();
                            for(int k = 1; k <= routines.length-1; k++){
                                seat.append(k+"", 0);
                            }
                            carriage.append(j+"",seat);
                        }
                    }
                    else {
                        carriage.append("type","二等座");

                        for(int j = 1 ; j<= CarriageType.getNumber(CarriageType.二等座) ; j++){
                            Document seat = new Document();
                            for(int k = 1; k <= routines.length-1; k++){
                                seat.append(k+"", 0);
                            }
                            carriage.append(j+"",seat);
                        }
                    }
                    carriageDAO.insert(carriage);
                }


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DBBuilder builder = new DBBuilder();
        builder.initialData();
    }
}
