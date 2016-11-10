package po;

/**
 * Created by yuminchen on 16/11/10.
 */
public class SeatPO {

    public int remainId;
    public int number;
    public int used;

    public SeatPO() {
    }

    /**

     *
     * @param remainId
     * @param number
     * @param used
     */
    public SeatPO(int remainId, int number, int used) {
        this.remainId = remainId;
        this.number = number;
        this.used = used;
    }
}
