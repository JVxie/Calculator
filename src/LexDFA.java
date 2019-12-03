import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.Vector;

class LexDFA {

    private String state; // 当前状态
    private String sentence; // 当前句子
    private int flag; // -1:未分析 0:词法错误 1:词法正确
    private Vector<Pair<String, String>> result = new Vector<Pair<String, String>>();
    private final static String[] STATE_SET = {"OPT", "ZERO", "OCT_I", "OCT_P", "OCT_R", "DEC_I", "DEC_P", "DEC_R", "HEX_Q", "HEX_I", "HEX_P", "HEX_R", "ER"};
    private final static char[] OPT_SET = {'+', '-', '*', '/', '(', ')'};
    private final static char[] VAL_SET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    LexDFA() {
        this("");
    }

    LexDFA(String sentence) {
        this.sentence = sentence;
        flag = -1;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
        flag = -1;
    }

    private boolean isOpt(char ch) {
        for (int i = 0; i < OPT_SET.length; ++i)
            if (OPT_SET[i] == ch)
                return true;
        return false;
    }

    private boolean isVal(char ch, int SIZE) {
        for (int i = 0; i < SIZE; ++i)
            if (VAL_SET[i] == ch)
                return true;
        return false;
    }

    public void lexDFA() {
        if (flag != -1) return;
        result.clear();
        state = "S";
        char nowCh;
        String nowStr = null;
        for (int i = 0; i < sentence.length(); ) {
            nowCh = sentence.charAt(i);
            if (state.equals("S")) {
                if (isOpt(nowCh)) {
                    state = "OPT";
                } else if (isVal(nowCh, 1)) {
                    state = "ZERO";
                } else if (isVal(nowCh, 10)) {
                    state = "DEC_I";
                } else {
                    state = "ER";
                }
                nowStr = String.valueOf(nowCh);
                ++i;
            } else if (state.equals("OPT")) {
                result.addElement(new Pair<>("运算符", nowStr));
                state = "S";
            } else if (state.equals("ZERO")) {
                if (isOpt(nowCh)) {
                    result.addElement(new Pair<>("十进制整数", nowStr));
                    state = "S";
                } else {
                    if (isVal(nowCh, 8)) {
                        state = "OCT_I";
                    } else if (nowCh == 'x') {
                        state = "HEX_Q";
                    } else if (nowCh == '.') {
                        state = "DEC_P";
                    } else {
                        state = "ER";
                    }
                    nowStr += String.valueOf(nowCh);
                    ++i;
                }
            } else if (state.equals("OCT_I")) {
                if (isVal(nowCh, 8)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                } else if (isOpt(nowCh)) {
                    result.addElement(new Pair<>("八进制整数", nowStr));
                    state = "S";
                } else if (nowCh == '.') {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "OCT_P";
                } else {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "ER";
                }
            } else if (state.equals("OCT_P")) {
                if (isVal(nowCh, 8)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "OCT_R";
                } else {
                    state = "ER";
                }
            } else if (state.equals("OCT_R")) {
                if (isVal(nowCh, 8)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                } else if (isOpt(nowCh)) {
                    result.addElement(new Pair<>("八进制实数", nowStr));
                    state = "S";
                } else {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "ER";
                }
            } else if (state.equals("DEC_I")) {
                if (isVal(nowCh, 10)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                } else if (isOpt(nowCh)) {
                    result.addElement(new Pair<>("十进制整数", nowStr));
                    state = "S";
                } else if (nowCh == '.') {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "DEC_P";
                } else {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "ER";
                }
            } else if (state.equals("DEC_P")) {
                if (isVal(nowCh, 10)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "DEC_R";
                } else {
                    state = "ER";
                }
            } else if (state.equals("DEC_R")) {
                if (isVal(nowCh, 10)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                } else if (isOpt(nowCh)) {
                    result.addElement(new Pair<>("十进制实数", nowStr));
                    state = "S";
                } else {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "ER";
                }
            } else if (state.equals("HEX_Q")) {
                if (isVal(nowCh, 16)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "HEX_I";
                } else {
                    state = "ER";
                }
            } else if (state.equals("HEX_I")) {
                if (isVal(nowCh, 16)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                } else if (isOpt(nowCh)) {
                    result.addElement(new Pair<>("十六进制整数", nowStr));
                    state = "S";
                } else if (nowCh == '.') {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "HEX_P";
                } else {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "ER";
                }
            } else if (state.equals("HEX_P")) {
                if (isVal(nowCh, 16)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "HEX_R";
                } else {
                    state = "ER";
                }
            } else if (state.equals("HEX_R")) {
                if (isVal(nowCh, 16)) {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                } else if (isOpt(nowCh)) {
                    result.addElement(new Pair<>("十六进制实数", nowStr));
                    state = "S";
                } else {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                    state = "ER";
                }
            } else if (state.equals("ER")) {
                if (isOpt(nowCh)) {
                    flag = 0;
                    result.addElement(new Pair<>("错误数据", nowStr));
                    state = "S";
                } else {
                    nowStr += String.valueOf(nowCh);
                    ++i;
                }
            } else {
                return;
            }
        }
        if (nowStr != null) {
            if (state.equals("OPT")) {
                result.addElement(new Pair<>("运算符", nowStr));
            } else if (state.equals("OCT_I")) {
                result.addElement(new Pair<>("八进制整数", nowStr));
            } else if (state.equals("OCT_R")) {
                result.addElement(new Pair<>("八进制实数", nowStr));
            } else if (state.equals("DEC_I") || state.equals("ZERO")) {
                result.addElement(new Pair<>("十进制整数", nowStr));
            } else if (state.equals("DEC_R")) {
                result.addElement(new Pair<>("十进制实数", nowStr));
            } else if (state.equals("HEX_I")) {
                result.addElement(new Pair<>("十六进制整数", nowStr));
            } else if (state.equals("HEX_R")) {
                result.addElement(new Pair<>("十六进制实数", nowStr));
            } else if (state.equals("ER") || state.equals("OCT_P") || state.equals("DEC_P") || state.equals("HEX_Q") || state.equals("HEX_P")) {
                result.addElement(new Pair<>("错误数据", nowStr));
                flag = 0;
            } else return;
        }
        if (sentence.length() > 0) {
            if (flag == -1) flag = 1;
        }
    }

    public Vector<Pair<String, Object>> toCalculate() {
        if (flag == -1) lexDFA();
        if (!isRight()) return null;
        Vector<Pair<String, Object>> ret = new Vector<>();
        for (int i = 0; i < result.size(); ++i) {
            String nowKey = result.elementAt(i).getKey();
            String nowValue = result.elementAt(i).getValue();
            if (nowKey.equals("运算符")) {
                ret.addElement(new Pair<>("运算符", nowValue));
            } else {
                BigDecimal newValue = new BigDecimal(0);
                BigDecimal tmp = new BigDecimal(1);
                if (nowKey.equals("八进制整数")) {
                    for (int j = 1; j < nowValue.length(); ++j)
                        newValue = newValue.multiply(BigDecimal.valueOf(8)).add(BigDecimal.valueOf(nowValue.charAt(j) - '0'));
                } else if (nowKey.equals("八进制实数")) {
                    int j;
                    for (j = 1; nowValue.charAt(j) != '.'; ++j)
                        newValue = newValue.multiply(BigDecimal.valueOf(8)).add(BigDecimal.valueOf(nowValue.charAt(j) - '0'));
                    for (j = j + 1; j < nowValue.length(); ++j) {
                        tmp = tmp.divide(BigDecimal.valueOf(8));
                        newValue = newValue.add(BigDecimal.valueOf(nowValue.charAt(j) - '0').multiply(tmp));
                    }
                } else if (nowKey.equals("十进制整数")) {
                    newValue = new BigDecimal(nowValue);
                } else if (nowKey.equals("十进制实数")) {
                    newValue = new BigDecimal(nowValue);
                } else if (nowKey.equals("十六进制整数")) {
                    for (int j = 2; j < nowValue.length(); ++j) {
                        if (isVal(nowValue.charAt(j), 10))
                            newValue = newValue.multiply(BigDecimal.valueOf(16)).add(BigDecimal.valueOf(nowValue.charAt(j) - '0'));
                        else
                            newValue = newValue.multiply(BigDecimal.valueOf(16)).add(BigDecimal.valueOf(nowValue.charAt(j) - 'a' + 10));
                    }
                } else {
                    int j;
                    for (j = 2; nowValue.charAt(j) != '.'; ++j) {
                        if (isVal(nowValue.charAt(j), 10))
                            newValue = newValue.multiply(BigDecimal.valueOf(16)).add(BigDecimal.valueOf(nowValue.charAt(j) - '0'));
                        else
                            newValue = newValue.multiply(BigDecimal.valueOf(16)).add(BigDecimal.valueOf(nowValue.charAt(j) - 'a' + 10));
                    }
                    for (j = j + 1; j < nowValue.length(); ++j) {
                        tmp = tmp.divide(BigDecimal.valueOf(16));
                        if (isVal(nowValue.charAt(j), 10))
                            newValue = newValue.add(BigDecimal.valueOf(nowValue.charAt(j) - '0').multiply(tmp));
                        else newValue = newValue.add(BigDecimal.valueOf(nowValue.charAt(j) - 'a' + 10).multiply(tmp));
                    }
                }
                ret.addElement(new Pair<>("数字", newValue));
            }
        }
        return ret;
    }

    public Vector<Pair<String, String>> toLexDFAFrame() {
        if (flag == -1) lexDFA();
        return result;
    }

    public String toSyntax() {
        if (flag == -1) lexDFA();
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < result.size(); ++i) {
            if (result.elementAt(i).getKey().equals("运算符")) {
                ret.append(result.elementAt(i).getValue());
            } else ret.append("d");
        }
        return ret.toString() + "#";
    }


    public boolean isRight() {
        if (flag == -1) lexDFA();
        return flag == 1;
    }

}

