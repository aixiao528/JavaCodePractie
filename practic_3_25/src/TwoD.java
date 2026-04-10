public class TwoD extends Lottery3D {
    private char[] userNumber; // 如 {'*','1','2'} 两个数字一个*

    public TwoD(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        if (s.length() != 3) return false;
        int digitCount = 0, starCount = 0;
        for (char c : s.toCharArray()) {
            if (c == '*') starCount++;
            else if (Character.isDigit(c)) digitCount++;
            else return false;
        }
        // 必须恰好2个数字，1个*
        return digitCount == 2 && starCount == 1;
    }

    @Override
    public void setUserNumber(String userInput) {
        this.userInput = userInput;
        this.userNumber = userInput.toCharArray();
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));
        // 按位比较非*的位置
        for (int i = 0; i < 3; i++) {
            if (userNumber[i] != '*') {
                if (arr[i] != (userNumber[i] - '0')) return 0;
            }
        }
        return 104;
    }
}