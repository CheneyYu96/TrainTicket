package po;

/**
 * Created by yuminchen on 16/11/9.
 */
public class RoutinePO {

    public int id;
    public String name;
    public String begin;
    public String end;

    public RoutinePO() {
    }

    /**
     *
     * @param name
     * @param begin
     * @param end
     */
    public RoutinePO(String name, String begin, String end) {
        this.name = name;
        this.begin = begin;
        this.end = end;
    }

    public RoutinePO(int id, String name, String begin, String end) {
        this.id = id;
        this.name = name;
        this.begin = begin;
        this.end = end;
    }
}
