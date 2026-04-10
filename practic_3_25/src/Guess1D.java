public class Guess1D extends Lottery3D {
    private int userNumber; // 单个数字 0-9

    public Guess1D(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        try {
            int n = Integer.parseInt(s);
            return n >= 0 && n <= 9;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void setUserNumber(String userInput) {
        this.userInput = userInput;
        this.userNumber = Integer.parseInt(userInput);
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));
        // 统计出现次数
        int count = 0;
        for (int n : arr) {
            if (n == userNumber) count++;
        }
        // 出现次数对应不同奖金
        switch (count) {
            case 1: return 2;
            case 2: return 12;
            case 3: return 230;
            default: return 0;
        }
    }
}