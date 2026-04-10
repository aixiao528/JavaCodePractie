import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String winStr;
        int n;
        winStr = sc.next();
        n = sc.nextInt();

        //循环n次
        int maxwin = 0;
        for (int i = 0; i < n; i++) {
            String typeStr = sc.next().trim();


            // 验证玩法名是否合法
            LotteryType type;
            try {
                type = LotteryType.valueOf(typeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("您输入的投注方式不存在，请重新输入");
                continue;
            }

            // 根据玩法创建对应子类对象（工厂方法）
            Lottery3D lottery = createLottery(type, winStr);


            // 在循环读取输入之前，判断是否需要输入
            boolean needInput = !(type == LotteryType.TRACTOR
                    || type == LotteryType.GUESSTHREE);

            String userInput = "";
            if (needInput) {
                while (true) {
                    userInput = sc.next().trim();
                    if (lottery.isAvailable(userInput)) break;
                    System.out.println("请输入正确的投注内容");
                }
            }


            lottery.setUserNumber(userInput);
            // 【关键修改】：只调用一次 getWins()，把结果存入变量中
            int currentWin = lottery.getWins();
            //System.out.println(currentWin);
            if (maxwin < currentWin) {
                maxwin = currentWin;
                //System.out.println(maxwin);
            }

        }
        System.out.println(maxwin);

    }


    // 工厂方法：根据类型创建对应子类
    private static Lottery3D createLottery(LotteryType type, String winStr) {
        switch (type) {
            case SINGLE:  return new Single(winStr);
            case GROUP:   return new Group(winStr);
            case ONED:    return new OneD(winStr);
            case GUESS1D:  return new Guess1D(winStr);
            case TOWD:  return new TwoD(winStr);
            case GUESS2D:  return new Guess2D(winStr);
            case GENERAL:  return new General(winStr);
            case SUM:  return new Sum(winStr);
            case PACKAGE:  return new Package(winStr);
            case GUESSSIZE:  return new GuessSize(winStr);
            case  GUESSTHREE:  return new GuessThree(winStr);
            case TRACTOR:  return new Tractor(winStr);
            case GUESSOE: return new GuessOE(winStr);

            default: throw new UnsupportedOperationException("暂未实现");
        }
    }

}