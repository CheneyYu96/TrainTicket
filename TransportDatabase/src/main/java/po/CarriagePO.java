package po;

import util.CarriageType;

/**
 * Created by yuminchen on 16/11/9.
 */
public class CarriagePO {

    public int id;
    public CarriageType type;
    public String train_name;
    public int number;

    /**
     *
     * @param type
     */
    public CarriagePO(CarriageType type, String name, int number) {
        this.type = type;
        this.train_name = name;
        this.number = number;
    }

    /**
     *
     * @param id
     * @param type
     * @param train_name
     * @param number
     */
    public CarriagePO(int id, CarriageType type, String train_name, int number) {
        this.id = id;
        this.type = type;
        this.train_name = train_name;
        this.number = number;
    }

    public CarriagePO() {
    }
}
