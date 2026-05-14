public class OneD extends Lottery3D {
    private char[] userNumber; // 如 {'*','*','2'} 或 {'9','*','*'}

    public OneD(String winNumber) {
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
        // 必须恰好1个数字，2个*
        return digitCount == 1 && starCount == 2;
    }

    @Override
    public void setUserNumber(String userInput) {
        this.userInput = userInput;
        this.userNumber = userInput.toCharArray();
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));
        for (int i = 0; i < 3; i++) {
            if (userNumber[i] != '*') {
                // 找到确定位置，比较是否相同
                return (arr[i] == (userNumber[i] - '0')) ? 10 : 0;
            }
        }
        return 0;
    }
}