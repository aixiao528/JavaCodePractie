public class General extends Lottery3D {
    private int[] userNumber;

    public General(String winNumber) {
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

        // 通选1：三位全中，奖470
        if (userNumber[0]==arr[0] && userNumber[1]==arr[1] && userNumber[2]==arr[2])
            return 470;

        // 通选2：任意两位按位相同，奖21
        int matchCount = 0;
        for (int i = 0; i < 3; i++) {
            if (userNumber[i] == arr[i]) matchCount++;
        }
        if (matchCount >= 2) return 21;

        return 0;
    }
}