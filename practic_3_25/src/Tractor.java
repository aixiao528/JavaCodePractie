public class Tractor extends Lottery3D {
    public Tractor(String winNumber) {
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s) {
        return true; // 不需要用户输入
    }

    @Override
    public void setUserNumber(String userInput) {
        // 不需要用户输入，什么都不做
    }

    @Override
    public int getWins() {
        int[] arr = stringToIntArray(winNumber.replace(" ", ""));
        int a = arr[0], b = arr[1], c = arr[2];

        // 判断是否升序或降序连续，排除890,098,901,109
        boolean isSeq = (b == a+1 && c == b+1) || (b == a-1 && c == b-1);

        // 排除特殊号码
        String s = winNumber.replace(" ", "");
        boolean isExcluded = s.equals("890") || s.equals("098")
                || s.equals("901") || s.equals("109");

        return (isSeq && !isExcluded) ? 65 : 0;
    }
}