import java.util.Stack;
import java.util.Vector;

public class Syntax {
    private Stack<Integer> statu = new Stack<>();    // 建立状态栈[integer]
    private Stack<Integer> StatuOutput = new Stack<>();// 建立状态栈的倒推输出栈[integer]
    private Stack<Character> signer = new Stack<>();    // 建立符号栈[char]
    private Stack<Character> SignerOutput = new Stack<>();    // 建立符号栈的倒推输出栈[char]
    private String input; // 写入输入串
    private Vector<Vector<Integer>> status = new Vector<>(); // 状态栈输出数组
    private Vector<Vector<Character>> singners = new Vector<>(); // 符号栈输出数组
    private Vector<String> outputs = new Vector<>(); // 储存剩余串
    private Vector<Character> chars = new Vector<>(); // 当前读入符号
    private Vector<String> actions = new Vector<>(); // 当前的动作是什么
    private int flag; // 该语法分析状态 -1:未分析 0:语法错误 1:语法正确
    private final static String table[][] = {
            {"$", "+", "-", "*", "/", "(", ")", "d", "#", "E", "T", "F", "I"},
            {"0", "$", "$", "$", "$", "S6", "$", "S5", "$", "1", "2", "3", "4"},
            {"1", "S7", "S8", "$", "$", "$", "$", "$", "acc", "$", "$", "$", "$"},
            {"2", "R3", "R3", "S9", "S10", "$", "R3", "$", "R3", "$", "$", "$", "$"},
            {"3", "R6", "R6", "R6", "R6", "R6", "R6", "R6", "R6", "$", "$", "$", "$"},
            {"4", "R8", "R8", "R8", "R8", "R8", "R8", "R8", "R8", "$", "$", "$", "$"},
            {"5", "R9", "R9", "R9", "R9", "R9", "R9", "R9", "R9", "$", "$", "$", "$"},
            {"6", "$", "$", "$", "$", "S6", "$", "S5", "$", "11", "2", "3", "4"},
            {"7", "$", "$", "$", "$", "S6", "$", "S5", "$", "$", "14", "3", "4"},
            {"8", "$", "$", "$", "$", "S6", "$", "S5", "$", "$", "15", "3", "4"},
            {"9", "$", "$", "$", "$", "S6", "$", "S5", "$", "$", "$", "12", "4"},
            {"10", "$", "$", "$", "$", "S6", "$", "S5", "$", "$", "$", "13", "4"},
            {"11", "S7", "S8", "$", "$", "$", "S16", "$", "$", "$", "$", "$", "$"},
            {"12", "R4", "R4", "R4", "R4", "R4", "R4", "R4", "R4", "$", "$", "$", "$"},
            {"13", "R5", "R5", "R5", "R5", "R5", "R5", "R5", "R5", "$", "$", "$", "$"},
            {"14", "R1", "R1", "S9", "S10", "$", "R1", "$", "R1", "$", "$", "$", "$"},
            {"15", "R2", "R2", "S9", "S10", "$", "R2", "$", "R2", "$", "$", "$", "$"},
            {"16", "R7", "R7", "R7", "R7", "R7", "R7", "R7", "R7", "$", "$", "$", "$"},
    };
    private final static String produce[] = {//九个产生式
            "E->E+T",
            "E->E-T",
            "E->T",
            "T->T*F",
            "T->T/F",
            "T->F",
            "F->(E)",
            "F->I",
            "I->d"
    };

    Syntax() {
        this("");
    }

    Syntax(String input) {
        this.input = input;
        flag = -1;
    }

    public void setInput(String input) {
        this.input = input; // 传入字符串
        flag = -1;
    }

    public int getOperationCol(char i) {
        int OperationCol = -1;
        for (int x = 0; x < table[0].length; x++) {
            if (table[0][x].charAt(0) == i) {
                OperationCol = x;
            }
        }
        return OperationCol;
    }

    public void analysis() {
        statu.push(0);
        signer.push('#');
        int index = 0;//字符串索引
        while (true) {
            {//状态栈传输规则：状态栈->状态倒推栈->状态输出数组->状态栈【回传】
                Vector<Integer> tmp = new Vector<>();
                while (!statu.empty()) {
                    StatuOutput.push(statu.pop());//将状态栈的栈顶元素推出，然后输入给倒推栈
                }
                while (!StatuOutput.empty()) {
                    tmp.addElement(StatuOutput.pop()); //将倒推栈的栈顶输入给输出数组
                }
                for (int x = 0; x < tmp.size(); x++) {
                    statu.push(tmp.elementAt(x));//将输出数组的元素输入给状态栈
                }
                status.addElement(tmp);
            }
            {//符号栈传输规则：符号栈->符号倒推栈->符号输出数组->符号栈【回传】
                Vector<Character> tmp = new Vector<>();
                while (!signer.empty()) {
                    SignerOutput.push(signer.pop());//将符号栈的栈顶元素推出，然后输入给倒推栈
                }
                while (!SignerOutput.empty()) {
                    tmp.addElement(SignerOutput.pop());//将倒推栈的栈顶输入给输出数组
                }
                for (int x = 0; x < tmp.size(); x++) {
                    signer.push(tmp.elementAt(x));//回传
                }
                singners.addElement(tmp);
            }
            String output = input.substring(index);//剩余串
            outputs.addElement(output);
            char i = input.charAt(index);
            chars.addElement(i);
            String action = table[statu.peek() + 1][getOperationCol(i)];
            if (action.equals("$")) {
                flag = 0;
                actions.addElement(action + "分析错误" + input + "不符合该SLR(1)文法！");
                break;
            } else if (action.equals("acc")) {
                flag = 1;
                actions.addElement(action + "分析成功" + input + "符合该SLR(1)文法！");
                break;
            } else {
                String SorR = action.substring(0, 1);// 获取S或R
                if (SorR.equals("S")) {
                    String number_action = action.substring(1);// 获取S后面的值
                    int value = Integer.parseInt(number_action);// 将number_action转为integer
                    statu.push(value);//将value压入状态栈
                    signer.push(i);//将字符i压入符号栈
                    index++;
                    actions.addElement(action + "分析动作完成！");
                } else if (SorR.equals("R")) {
                    String number_action = action.substring(1);//获取R后面的值
                    int ReduceIndex = Integer.parseInt(number_action);//使用第几个产生式进行归约
                    String useProduce = produce[ReduceIndex - 1];//记录产生式
                    char leftProduce = useProduce.charAt(0);//获取该产生式的左部符号，即归约符号
                    String RightLength = useProduce.substring(3);//获取产生式的右部符号串
                    int LengthAboutPopStack = RightLength.length();//获取右部符号串长度
                    int PopStackValue[] = new int[LengthAboutPopStack];
                    for (int j = 0; j < LengthAboutPopStack; j++) {
                        PopStackValue[j] = statu.pop();
                        signer.pop();
                    }
                    String returnValue = table[statu.peek() + 1][getOperationCol(leftProduce)];//获取当前状态栈栈顶元素与归约非终结符的转向状态[String]
                    int ValueOfReturn = Integer.parseInt(returnValue);
                    signer.push(leftProduce);//将该左部符号压入符号栈
                    statu.push(ValueOfReturn);
                    actions.addElement(action + "转向动作完成！使用" + useProduce + "完成归约，跳转至" + ValueOfReturn);
                }
            }
        }
    }

    public boolean isRight() {
        if (flag == -1) analysis();
        return flag == 1;
    }

    public Vector<Vector<String>> toSyntaxFrame() {
        if (flag == -1) analysis();
        Vector<Vector<String>> ret = new Vector<>();
        Vector<String> tmp = new Vector<>();
        tmp.addElement("状态栈");
        tmp.addElement("符号栈");
        tmp.addElement("当前符号");
        tmp.addElement("剩余串");
        tmp.addElement("操作动作");
        ret.addElement(tmp);
        for (int count = 0; count < outputs.size(); ++count) {
            tmp = new Vector<>();
            tmp.addElement(status.elementAt(count).toString().replaceAll(" ", ""));
            tmp.addElement(singners.elementAt(count).toString().replaceAll(" ", ""));
            tmp.addElement(chars.elementAt(count).toString());
            tmp.addElement(outputs.elementAt(count));
            tmp.addElement(actions.elementAt(count));
            ret.addElement(tmp);
        }
        return ret;
    }

}
