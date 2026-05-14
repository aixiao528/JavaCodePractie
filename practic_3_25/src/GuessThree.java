public class GuessThree extends Lottery3D {
    public GuessThree(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        return true; // 票面无数字，无需输入
    }

    @Override
    public void setUserNumber(String userInput) {
        // 无需输入
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));
        // 三个数字全相同（豹子号）
        return (arr[0] == arr[1] && arr[1] == arr[2]) ? 104 : 0;
    }
}