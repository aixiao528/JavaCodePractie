public class Sum extends Lottery3D {
    private int userNumber; // 0-27

    // 奖金表，索引就是和数
    private static final int[] BONUS = {
            1040, 345, 172, 104, 69, 49, 37, 29, 23,  // 0-8
            19, 16, 15, 15, 14, 14, 15, 15, 16, 19,   // 9-18
            23, 29, 37, 49, 69, 104, 172, 345, 1040    // 19-27
    };

    public Sum(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        try {
            int n = Integer.parseInt(s);
            return n >= 0 && n <= 27;
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
        int sum = arr[0] + arr[1] + arr[2];
        return (sum == userNumber) ? BONUS[sum] : 0;
    }
}