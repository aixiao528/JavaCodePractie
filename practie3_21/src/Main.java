import java.util.Scanner;
class ThreeDlottery {
    private int[]winnumber;
    ThreeDlottery(int[]winnumber){
        this.winnumber=winnumber;
    }

    public int[] getwin(){
        return this.winnumber;
    }

    int countwin(int[] userwin){
        int sum=0;
        for(int i=0;i<3;i++){
            sum+=winnumber[i];
        }
        int usersum=0;
        for(int j=0;j<3;j++){
            usersum+=userwin[j];
        }

        if(sum==usersum){
            if (sum==0) return 1040;
            //中奖
            switch(sum){
                case 27:return 1040;//case 0 没有这种情况的
                case 1,26:return 345;
                case 2,25:return 172;
                case 3,24:return 104;
                case 4,23:return 69;
                case 5,22:return 49;
                case 6,21:return 37;
                case 7,20:return 29;
                case 8,19:return 23;
                case 9,18:return 19;
                case 10,17:return 16;
                case 11,16:return 15;
                case 12,15:return 15;
                case 13,14:return 14;
                default: return 0;

            }
        }
        else{
            return 0;
        }
    }

}
public class Main {
    public static void main(String[] args ){
        int[]win=new int[3];
        int[] userwin =new int[3];
        Scanner scanner=new Scanner(System.in);
        String A=scanner.next();
        String B=scanner.next();
        for(int m=0;m<A.length();m++){
            win[m]=A.charAt(m)-'0';//字符转数字
        }
        for(int m=0;m<3;m++){
            userwin[m]=B.charAt(m)-'0';//字符转数字
        }
        scanner.close();
        ThreeDlottery thD=new ThreeDlottery(win);
        int jiangli=thD.countwin(userwin);
        System.out.println(jiangli);
    }

}
