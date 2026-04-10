import java.util.HashMap;
import java.util.Map;

public class Guess2D extends Lottery3D {
    private int[] userNumber; // 两个数字，如{1,2}或{1,1}

    public Guess2D(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        // 必须恰好2位数字
        if (s.length() != 2) return false;
        return Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1));
    }

    @Override
    public void setUserNumber(String userInput) {
        this.userInput = userInput;
        this.userNumber = stringToIntArray(userInput);
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));

        // 用Map统计中奖号和用户投注的数字频率
        Map<Integer,Integer> mapWin = new HashMap<>();
        for (int n : arr) mapWin.put(n, mapWin.getOrDefault(n,0)+1);

        Map<Integer,Integer> mapUser = new HashMap<>();
        for (int n : userNumber) mapUser.put(n, mapUser.getOrDefault(n,0)+1);

        if (userNumber[0] == userNumber[1]) {
            // 两同号：中奖号里必须包含这两个相同数字
            int digit = userNumber[0];
            return (mapWin.getOrDefault(digit,0) >= 2) ? 37 : 0;
        } else {
            // 两不同号：中奖号里必须同时包含这两个数字（顺序不限）
            boolean hasFirst  = mapWin.containsKey(userNumber[0]);
            boolean hasSecond = mapWin.containsKey(userNumber[1]);
            return (hasFirst && hasSecond) ? 19 : 0;
        }
    }
}