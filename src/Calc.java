import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

class Calc {

    private String sentence;
    private Map<String, Integer> FIRST = new HashMap<String, Integer>();

    void init() {
        FIRST.put("(", 0);
        FIRST.put(")", 0);
        FIRST.put("+", 1);
        FIRST.put("-", 1);
        FIRST.put("*", 2);
        FIRST.put("/", 2);
    }

    Calc() {
        this("");
    }

    Calc(String sentence) {
        init();
        this.sentence = sentence;
    }

    private final static char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public BigDecimal toInteger(BigDecimal bigDecimal) {
        String tmp = bigDecimal.toPlainString();
        //System.out.println(tmp);
        if(tmp.contains("."))
            tmp = tmp.substring(0, tmp.indexOf("."));
        return new BigDecimal((new BigInteger(tmp)).toString());
    }

    public BigDecimal toDecimal(BigDecimal bigDecimal) {
        return bigDecimal.subtract(toInteger(bigDecimal));
    }

    public String replaceZero(String x) {
        int i = x.length()-1;
        for( ; i >= 0; --i) {
            if(x.charAt(i) != '0') break ;
        }
        if(i < 0) return "";
        else return x.substring(0, i+1);
    }

    public String toUnsignedString(BigDecimal x, int shift) {
        String sign = "";
        if(x.compareTo(BigDecimal.ZERO) < 0) {
            sign = "-";
            x = x.abs();
        }
        BigDecimal divisor = new BigDecimal(shift);
        BigDecimal integer = toInteger(x);
        BigDecimal decimal = toDecimal(x);

        StringBuilder retInteger = new StringBuilder();
        do {
            BigDecimal[] ba = integer.divideAndRemainder(divisor);
            integer = ba[0];
            retInteger.append(digits[ba[1].intValue()]);
        } while (integer.compareTo(BigDecimal.ZERO) > 0);
        String retIntegerReal = retInteger.reverse().toString();

        StringBuilder retDecimal = new StringBuilder();
        int cnt = 0;
        do {
            decimal = decimal.multiply(divisor);
            retDecimal.append(digits[decimal.intValue()]);
            decimal = toDecimal(decimal);
            ++cnt;
        } while(cnt < 10);
        String retDecimalReal = replaceZero(retDecimal.toString());

        if(retDecimalReal.length() <= 0) {
            return sign + retIntegerReal;
        } else {
            return sign + retIntegerReal + "." + retDecimalReal;
        }
    }

    public String getResult() {
        String ret = "";
        LexDFA lexDFA = new LexDFA(sentence);
        if(!lexDFA.isRight()) {
            return "词法错误";
        } else  {
            // 预留语法分析
            Syntax syntax = new Syntax(lexDFA.toSyntax());
            if(!syntax.isRight()) {
                return "语法错误";
            } else {
                Vector<Pair<String, Object>> value = lexDFA.toCalculate();
                Stack<Pair<String, Object>> opts = new Stack<>();
                Vector<Pair<String, Object>> ans = new Vector<>();
                for(int i = 0; i < value.size(); ++i) {
                    String name = value.elementAt(i).getKey();
                    if(name.equals("数字")) {
                        ans.add(value.elementAt(i));
                    } else {
                        String opt = (String)value.elementAt(i).getValue();
                        if(opt.equals("(")) {
                            opts.push(value.elementAt(i));
                        } else if(opt.equals(")")) {
                            while(!opts.empty() && !(opts.peek().getValue()).equals("(")) {
                                ans.add(opts.pop());
                            } opts.pop();
                        } else {
                            while(!opts.empty() && FIRST.get(opts.peek().getValue()) >= FIRST.get(opt)) {
                                ans.add(opts.pop());
                            } opts.push(value.elementAt(i));
                        }
                    }
                }
                while(!opts.empty()) ans.add(opts.pop());
                Stack<BigDecimal> retAns = new Stack<>();
                for(int i = 0; i < ans.size(); ++i) {
                    if(ans.elementAt(i).getKey().equals("数字"))
                        retAns.push((BigDecimal)ans.elementAt(i).getValue());
                    else {
                        if(ans.elementAt(i).getValue().equals("+")) {
                            BigDecimal b = retAns.pop();
                            BigDecimal a = retAns.pop();
                            retAns.push(a.add(b));
                        } else if(ans.elementAt(i).getValue().equals("-")) {
                            BigDecimal b = retAns.pop();
                            BigDecimal a = retAns.pop();
                            retAns.push(a.subtract(b));
                        } else if(ans.elementAt(i).getValue().equals("*")) {
                            BigDecimal b = retAns.pop();
                            BigDecimal a = retAns.pop();
                            retAns.push(a.multiply(b));
                        } else if(ans.elementAt(i).getValue().equals("/")) {
                            BigDecimal b = retAns.pop();
                            BigDecimal a = retAns.pop();
                            if(b.equals(BigDecimal.ZERO))
                                return "Divide 0 ERROR!";
                            retAns.push(a.divide(b, 20, RoundingMode.HALF_UP));
                        } else return "ERROR!";
                    }
                }
                ret = retAns.pop().setScale(10, RoundingMode.HALF_UP).toPlainString();
                ret = replaceZero(ret);
                if(ret.charAt(ret.length()-1) == '.') ret = ret.substring(0, ret.length()-1);
            }
        }
        return ret;
    }

}
