public abstract class Lottery3D {
    //所有玩法共用的骨架
    protected String winNumber;
    protected String userInput;//包含有*号

    public Lottery3D(String winNumber){
        this.winNumber=winNumber;
    }

    //验证输入格式是否合法
    public abstract boolean isAvailable(String s);

    //设置用户密码（内部解析unerInput）
    public abstract void setUserNumber(String userInput);

    //核心：计算奖金（每个子类自己实现）
    public abstract int getWins();

    //工具方法：把字符串“456”转成int[]{4,5,6}
    protected int[] stringToIntArray(String s){
        int[]arr=new int[s.length()];
        for(int i=0;i<s.length();i++){
            arr[i]=s.charAt(i)-'0';
        }
        return arr;
    }

}

