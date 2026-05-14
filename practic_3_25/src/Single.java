public class Single extends Lottery3D{
    private int[]  userNumber;

    public Single(String winNumber){
        super(winNumber);
    }

    @Override
    public boolean isAvailable(String s){
        try{
            //检验长度
            if(s==null || s.length()!=3){return false;}
            for(char c:s.toCharArray()) {
                //toCharArray()方法把字符串转换为字符数组
                if(c<'0' || c > '9'){
                    return false;
                }
            }

            int[] numbers=stringToIntArray(s);
            this.userInput=s;

            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public void setUserNumber(String input){
        this.userInput=input;
        this.userNumber=stringToIntArray(String.format("%03d",Integer.parseInt(input)));
        //%03d,%d-整数，3-宽度至少3位，0-不够3位时用0补左边
        //Integer.parseInt() 把字符串变成整数
    }

    @Override
    public int getWins(){
        int[] setwinNumber=stringToIntArray(winNumber);
        if(userNumber[0]==setwinNumber[0]
                &&userNumber[1]==setwinNumber[1]
                &&userNumber[2]==setwinNumber[2])
        {return 1040;}
        return 0;
    }

}
