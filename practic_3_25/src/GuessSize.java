public class GuessSize extends Lottery3D {
    private String userNumber; // "大" 或 "小"

    public GuessSize(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        return s.equals("大") || s.equals("小");
    }

    @Override
    public void setUserNumber(String userInput) {
        this.userInput = userInput;
        this.userNumber = userInput;
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));
        int sum = arr[0] + arr[1] + arr[2];

        // 和值9-17既不大也不小，不中奖
        if (sum >= 19 && sum <= 27) {
            return userNumber.equals("大") ? 6 : 0;
        } else if (sum >= 0 && sum <= 8) {
            return userNumber.equals("小") ? 6 : 0;
        }
        return 0;
    }
}