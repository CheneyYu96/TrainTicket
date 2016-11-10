package po;


import util.CarriageType;

/**
 * Created by yuminchen on 16/11/9.
 */
public class RemainPO {

    public int id;
    public int routineId;
    public int carriageId;
    public CarriageType type;
    public int remainNumber;
    public int allNumber;

    public RemainPO() {
    }

    /**
     *
     * @param id
     * @param routineId
     * @param carriageId
     * @param type
     * @param remainNumber
     * @param allNumber
     */
    public RemainPO(int id, int routineId, int carriageId, CarriageType type, int remainNumber, int allNumber) {
        this.id = id;
        this.routineId = routineId;
        this.carriageId = carriageId;
        this.type = type;
        this.remainNumber = remainNumber;
        this.allNumber = allNumber;
    }
}
