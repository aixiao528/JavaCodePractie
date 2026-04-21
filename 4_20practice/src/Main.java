import java.util.*;

public class Main {
    static String expr;  //整个表达式字符串
    static int pos; //当前扫描到的位置
    //跳过空格
    static void skipSpaces(){
        while(pos<expr.length()&&expr.charAt(pos)==' '){
            pos++;
        }
    }

    //读取一个数字
    static Number parseNumber(){
        skipSpaces();
        int start =pos;
        boolean isFloat=false;
        while(pos<expr.length()&&
                (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos)=='.' ) ){
            if(expr.charAt(pos)=='.') isFloat=true;
            //if(expr.charAt(pos)==' ') pos++;
            pos++;
        }
        String numStr = expr.substring(start,pos);
        String Str=numStr.replaceAll("\\s+","");
        if(isFloat){
            return Float.parseFloat(Str);
        }else{
            return Integer.parseInt(Str);
        }
    }

    //执行运算并且做类型提升
    static Number applyOp(char op,Number a,Number b){
        boolean useFloat=(a instanceof Float)||(b instanceof Float);
        /*一定要区分float和int的原因：
        * 虽然本质都是直接计算，但是整数运算和浮点数运算在计算机底层的指令集（CPU 指令）是完全不同的
        * 编译器在编译阶段只知道 a 和 b 是 Number。你不能直接对两个 Number 对象进行 + 操作，因为 Number 类里并没有定义 + 运算符
        * 所以必须先转换成具体的类型
        * */
        if(useFloat){
            float x=a.floatValue();
            float y=b.floatValue();
            switch(op){
                case '+':return x+y;
                case '-':return x-y;
                case '*': return x*y;
                case '/':return x/y;
                case '%':return x%y;
                default:throw new IllegalArgumentException("未知运算符");
            }
        }else {
            int x = a.intValue();
            int y = b.intValue();
            switch (op) {
                case '+':
                    return x + y;
                case '-':
                    return x - y;
                case '*':
                    return x * y;
                case '/':
                    return x / y;
                case '%':
                    return x % y;
                default:throw new IllegalArgumentException("未知运算符");
            }
        }
    }


    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        //while(sc.hasNextLine()){
            String line =  sc.nextLine().trim();
            //if(line.isEmpty()) break;
            if(line.endsWith("=")){
                line=line.substring(0,line.length()-1);

            }
            expr=line;
            pos=0; //每处理一行就要重置
            Number result=parseExpr();
            //用Number来统一Integer Float

            if(result instanceof Float){
                System.out.println(result);
            }else{
                System.out.println(result);
            }
        //}

    }

    //解析加减（最低优先级）
    static Number parseExpr(){
        Number left = parseTerm();
        while(true){
            skipSpaces();//关键:在查看op之间跳过空格
            if(pos>=expr.length()) break;
            char op=expr.charAt(pos);
            if(op == '+' || op=='-'){
                pos++;
                Number right=parseTerm();
                left=applyOp(op,left,right);
            }else{
                break;
            }
        }
        return left;
    }


    //解析乘除余（中优先级）
    static Number parseTerm(){

        Number left = parseFactor();
        while(true){
            skipSpaces();
            if(pos>=expr.length()) break;

            char op=expr.charAt(pos);
            if(op=='*' || op=='/' ||op== '%'){
                pos++;
                Number right=parseFactor();
                left=applyOp(op,left,right);
            }else{
                break;
            }
        }

        return left;
    }

    //解析括号和数字（高优先级）
    static Number parseFactor(){

            skipSpaces();

        //处理一元负号，例如（-5+3）
        if(expr.charAt(pos)=='-'){
            pos++;
            return applyOp('*',-1,parseFactor());
        }

        if(pos<expr.length() && expr.charAt(pos) == '('){
            pos++;  //跳过‘（’
            //括号内重新从最低级优先级开始算
            Number val = parseExpr();
            pos++; //跳过“）”
            return val;
        }
        return parseNumber();
    }
}
