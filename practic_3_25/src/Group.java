import java.util.HashMap;
import java.util.Map;

public class Group extends Lottery3D {
    private int[]userNumber;
    public Group(String winNumber){
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s){
        //0~999
        try {
            int n=Integer.parseInt(s);
            return n>=0 &&n<=999;
        }catch(NumberFormatException e){
            return false;
        }
    }

    @Override
    public void setUserNumber(String input){
        this.userInput=input;
        this.userNumber=stringToIntArray(String.format("%03d",Integer.parseInt(input)));
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber);

        //统计次数
        Map<Integer, Integer> mapwin = new HashMap<>();
        for (int num : arr) {
            mapwin.put(num, mapwin.getOrDefault(num, 0) + 1);
            //getOrDefault() 方法获取指定 key 对应对 value，如果找不到 key ，则返回设置的默认值
        }
        Map<Integer, Integer> mapinput = new HashMap<>();
        for (int num : userNumber) {
            mapinput.put(num, mapinput.getOrDefault(num, 0) + 1);
        }

        if (mapwin.containsValue(2)) {
            //属于组选3
            if (mapwin.equals(mapinput)) {
                return 346;
            } else {
                if (mapwin.equals(mapinput))
                    return 173;
            }
        }
        return 0;
    }
}