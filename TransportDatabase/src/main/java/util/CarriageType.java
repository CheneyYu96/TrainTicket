package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuminchen on 16/11/9.
 */
public enum CarriageType {
    特等座,一等座,二等座,无座;

    static CarriageType[] carriageTypes = new CarriageType[]{特等座,一等座,二等座,无座};
    static int[] map = new int[]{40,80,100,20};
    public static int getNumber(CarriageType type){
        int index = 0;
        for(int i = 0; i<carriageTypes.length; i++){
            if(type==carriageTypes[i]){
                index = i;
                break;
            }
        }
        return map[index];
    }
}
