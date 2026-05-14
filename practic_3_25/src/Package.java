import java.util.HashMap;
import java.util.Map;

public class Package extends Lottery3D {
    private int[] userNumber;

    public Package(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        try {
            int n = Integer.parseInt(s);
            return n >= 0 && n <= 999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void setUserNumber(String userInput) {
        this.userInput = userInput;
        this.userNumber = stringToIntArray(
                String.format("%03d", Integer.parseInt(userInput))
        );
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));

        // 先判断全中（按位完全相同）
        boolean fullMatch = (userNumber[0]==arr[0]
                && userNumber[1]==arr[1]
                && userNumber[2]==arr[2]);

        // 再判断组中（号码相同，顺序不限）
        Map<Integer,Integer> mapWin = new HashMap<>();
        for (int n : arr) mapWin.put(n, mapWin.getOrDefault(n,0)+1);
        Map<Integer,Integer> mapUser = new HashMap<>();
        for (int n : userNumber) mapUser.put(n, mapUser.getOrDefault(n,0)+1);
        boolean groupMatch = mapWin.equals(mapUser);

        // 判断是包选3还是包选6
        boolean hasDouble = mapUser.containsValue(2); // 有两个相同数字=包选3

        if (hasDouble) {
            if (fullMatch)  return 693; // 包选三全中
            if (groupMatch) return 173; // 包选三组中
        } else {
            if (fullMatch)  return 606; // 包选六全中
            if (groupMatch) return 86;  // 包选六组中
        }
        return 0;
    }
}