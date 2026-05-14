public class GuessOE extends Lottery3D {
    //猜奇偶
    private String userNumber; // "奇" 或 "偶"

    public GuessOE(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        return s.equals("奇") || s.equals("偶");
    }

    @Override
    public void setUserNumber(String userInput) {
        this.userInput = userInput;
        this.userNumber = userInput;
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));

        boolean allOdd  = (arr[0]%2==1 && arr[1]%2==1 && arr[2]%2==1);
        boolean allEven = (arr[0]%2==0 && arr[1]%2==0 && arr[2]%2==0);

        if (userNumber.equals("奇") && allOdd)  return 8;
        if (userNumber.equals("偶") && allEven) return 8;
        return 0;
    }
}