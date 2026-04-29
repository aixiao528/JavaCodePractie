import java.util.*;
//static关键字意味着此变量属于类本身，而不属于main方法，相当于在Main类里大家共享
public class Main {
    static HashMap <String,variable>  va = new HashMap();
    static String expr;  //整个表达式字符串
    static int pos; //当前扫描到的位置

    //定义一个变量容器
    static class variable{
        String type;//int or float
        Number value;
        boolean initialized;//是否赋过初值
        public  variable(String type){
            this.type=type;
            this.value=null;
            this.initialized=false;

        }
        public variable(String type, Number value) throws Exception {
           this.type=type;
           this.value=value;
           if(type.equals("int") && value instanceof Integer){
              
           } else if (type.equals("float") && value instanceof Float) {
              
           }else if (type.equals("float") && value instanceof Integer) {
              float v=value.floatValue();
              Float fv=v;
              this.value=fv;
           }else if (type.equals("int") && value instanceof Float){
               int v=value.intValue();
               Integer iv=v;
               this.value=iv;
           }
           else throw new Exception("wrong - error expresion");//类型和变量不匹配
           initialized=true;//已初始化===========================
      }
    }
    //跳过空格
    static void skipSpaces(){
        while(pos<expr.length()&&expr.charAt(pos)==' '){
            pos++;
        }
    }

    //把source转换成数字
    /*public static Number toNumber(String s){
        try{
            return Integer.parseInt(s);
        }catch (Exception e){
            try{
                return Float.parseFloat(s);

            }catch(Exception o){}
            throw  new IllegalArgumentException("不是合法数字");
        }
    }*/

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

    //新增：用于根据变量名从hashMap中取值
    static Number getVariableVaulue(String varName)throws Exception{
        if(!va.containsKey(varName)) throw new Exception("wrong - variable undefined");
        variable v=va.get(varName);
        if(v==null) throw new Exception("wrong - variable undefined");
        if(!v.initialized) throw new Exception("wrong - variable unassigned");
        return v.value;
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
        while(sc.hasNext()){
        
            try{
                String stat=sc.nextLine().replaceAll("[;]$","").trim();
                if(stat.endsWith("?")) {
                    String stat1 = stat.replaceAll("[=?]+$","");
                    expr=stat1; 
                    pos=0;
                    Number result = parseExpr();
                    skipSpaces();
                    if(pos<expr.length()) throw new Exception("wrong - error expression");
                    if(result instanceof Integer){
                        System.out.println(result);
                    }
                    else{
                        System.out.printf("%.2f\n",result.floatValue());
                    }
                }
                else if(stat.contains("=")){
                    //赋值语句
                    String[]stats =stat.split("=");
                    String target = stats[0];
                    String source = stats[1].trim();
                    String typestr="";
                    String valuename="";

                    if(target.trim().contains(" ")){//声明并初始化的语句
                        typestr=target.trim().split(" ")[0];
                        valuename=target.trim().split(" ")[1];
                        if(va.containsKey(valuename)){
                            //重复声明
                            throw new Exception("Redifinition");
                        }else{
                            
                            //计算source的值
                            expr=source;
                            pos=0;
                            Number ss=parseExpr();
                            variable zhi=new variable(typestr,ss);
                            va.put(valuename,zhi);
                        }
                    }
                    else{
                        //不带类型的赋值语句
                        valuename=target.trim();

                        //计算source的值
                        expr=source;
                        pos=0;
                        Number ss=parseExpr();

                        variable v=va.get(valuename);
                        if(v==null) throw new Exception("wrong - variable undefined");
                        if(v.type.equals("int")){
                            v.value=ss.intValue();
                        } else if(v.type.equals("float")){
                            v.value=ss.floatValue();
                        } else{
                            throw new Exception("wrong - variable type");
                        }       
                        v.initialized=true;       
                    }

                    

                }
                else{
                    //声明语句 
                    String typestr="";
                    String valuename="";
                    //变量声明语句
                    
                    //Number fuzhi=getVariableVaulue(valuename); //检查变量类型，没有就报错

                    if(stat.trim().contains(" ")){
                        typestr=stat.trim().split(" ")[0];
                        valuename=stat.trim().split(" ")[1];
                        if(va.containsKey(valuename)){
                            //重复声明
                            throw new Exception("Redifinition");
                        }else{
                            variable zhi=new variable(typestr);
                            va.put(valuename,zhi);
                        }

                    }
                    else
                        throw new Exception("wrong - declaration");
                }
            }catch(NoSuchMethodError e){
                System.out.println("wrong - Constructor" );
            
            }catch(ClassNotFoundException e){
                System.out.println("Wrong Type");
            }catch(Exception e){
                System.out.println(e.getMessage() );
            }
        }
           


}

    //解析加减（最低优先级）
    static Number parseExpr() throws Exception {
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
    static Number parseTerm() throws Exception {

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
    static Number parseFactor()throws Exception{

            skipSpaces();
        char c=expr.charAt(pos);
        //处理一元负号，例如（-5+3）
        if(c=='-'){
            pos++;
            return applyOp('*',-1,parseFactor());
        }

        if(pos<expr.length() && c == '('){
            pos++;  //跳过‘（’
            //括号内重新从最低级优先级开始算
            Number val = parseExpr();
            //检查是否有括号
            if(pos < expr.length() && expr.charAt(pos)== ')'){
                pos++;
            } else{
                throw new Exception("wrong - error expression");
            }
            
            return val;
        }else if(Character.isDigit(c) || c=='.' ) {
            return parseNumber();
        }else if(Character.isLetter(c)){
            int start = pos;
            while(pos<expr.length() && Character.isLetterOrDigit(expr.charAt(pos)))pos++;
            String varname=expr.substring(start,pos);
            return getVariableVaulue(varname);
        }

        throw new Exception("wrong - error expression");
    }

}
