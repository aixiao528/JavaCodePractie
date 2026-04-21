import java.util.Scanner;

public class turkeyPrice {
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        int num=scanner.nextInt();
        int x=scanner.nextInt();
        int y=scanner.nextInt();
        int z=scanner.nextInt();
        scanner.close();
        int maxprice=0;
        int finalA=0;
        int finalB=0;
        for (int A=1;A<=9;A++){
            for(int B=0;B<=9;B++){
                int totalprice=A*10000+x*1000+y*100+z*10+B;
                if(totalprice%num==0){
                    int perprice=totalprice/num;
                    if(perprice>maxprice){
                        maxprice=perprice;
                         finalA=A;
                         finalB=B;
                    }

                }

            }
        }
        if(finalA==0){
            System.out.println("0");
        }
        else {
            System.out.println(finalA + " " + finalB + " " + maxprice);
        }
    }
}
