import java.util.Scanner;

public class Hourglass {
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        int totalnum=scanner.nextInt();
        String sysmbol=scanner.next();
        char ch=sysmbol.charAt(0);
        scanner.close();
        int remCh=0;

        //计算沙漏上下层数
        int k=0;
        int maxk=0;

        while((2*(k+1)+4)*(k+1)+1<=totalnum){//算K+1层，因为上一轮循环k+1了
            k++;
        }
        int totalused=(2*k+4)*k+1;
        maxk=k;
        remCh=totalnum-totalused;

        //打印上半层
        for(int i=k;i>=0;i--){
            int symcount=2*i+1;
            int fill=k-i;

            //打印左侧
            for(int j=0;j<fill;j++){
                if(i==0){
                    System.out.print("|");
                }else{
                    System.out.print("\\");
                }
            }

            //打印符号
            for(int j=0;j<symcount;j++){
                System.out.print(sysmbol);
            }
            //打印右边
            for(int j=0;j<fill;j++){
                if(i==0){
                    System.out.print("|");
                }else{
                    System.out.print("/");
                }
            }
            System.out.println();//换行
        }


        //打印下半层
        for(int i=1;i<=k;i++){
            int symcount=2*i+1;
            int fill=k-i;

            //左边
            for(int j=0;j<fill;j++){
                System.out.print("/");
            }

            //中间
            for(int j=0;j<symcount;j++){
                System.out.print(sysmbol);
            }
            //右边
            for(int j=0;j<fill;j++){
                System.out.print("\\");
            }
            System.out.println();
        }
        System.out.println(remCh);
    }
}
