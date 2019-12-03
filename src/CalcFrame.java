import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

class CalcFrame extends JFrame {

    CalcFrame() {
    }

    private static String[] VALS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static String[] OPTS = {"(", ")", "+", "-", "*", "/", "=", ".", "x"};
    private static String[] CABS = {"Clear", "Back"};
    private JPanel btnPanel; //  按钮面板
    private JLabel showLabel, octLabel, decLabel, hexLabel; // 文本框
    private JButton[] btnVals, btnOpts, btnCabs;

    //private JButton testButton = new JButton("TEST");

    public void init() {
        btnVals = new JButton[VALS.length];
        btnOpts = new JButton[OPTS.length];
        btnCabs = new JButton[CABS.length];
        for (int i = 0; i < VALS.length; ++i)
            btnVals[i] = new JButton(VALS[i]);
        for (int i = 0; i < OPTS.length; ++i)
            btnOpts[i] = new JButton(OPTS[i]);
        for (int i = 0; i < CABS.length; ++i)
            btnCabs[i] = new JButton(CABS[i]);

        /*
            测试监听
         */
        CalcValsListener calcValsListener = new CalcValsListener();
        for (int i = 0; i < VALS.length; ++i)
            btnVals[i].addActionListener(calcValsListener);

        CalcOptsListener calcOptsListener = new CalcOptsListener();
        for (int i = 0; i < OPTS.length; ++i)
            btnOpts[i].addActionListener(calcOptsListener);

        CalcCabsListener calcCabsListener = new CalcCabsListener();
        for (int i = 0; i < CABS.length; ++i)
            btnCabs[i].addActionListener(calcCabsListener);

        // 初始化
        btnPanel = new JPanel();
        showLabel = new JLabel("", JLabel.RIGHT);
        octLabel = new JLabel("", JLabel.RIGHT);
        decLabel = new JLabel("", JLabel.RIGHT);
        hexLabel = new JLabel("", JLabel.RIGHT);

        this.setLayout(null);
        btnPanel.setLayout(null);

        showLabel.setBounds(30, 30, 360, 24);
        showLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        showLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(showLabel);

        octLabel.setBounds(30, 54, 360, 22);
        octLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        octLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
        this.add(octLabel);

        decLabel.setBounds(30, 76, 360, 22);
        decLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        decLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.add(decLabel);

        hexLabel.setBounds(30, 98, 360, 22);
        hexLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        hexLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        this.add(hexLabel);

        btnPanel.setBounds(30, 150, 360, 435);
        btnCabs[0].setForeground(Color.RED);
        // first floor
        this.addButton(btnCabs[0], 0, 0, 165, 60);
        this.addButton(btnCabs[1], 195, 0, 165, 60);
        // second floor
        this.addButton(btnVals[11], 0, 75, 60, 60);
        this.addButton(btnVals[10], 75, 75, 60, 60);
        this.addButton(btnOpts[0], 150, 75, 60, 60);
        this.addButton(btnOpts[1], 225, 75, 60, 60);
        this.addButton(btnOpts[5], 300, 75, 60, 60);
        // third floor
        this.addButton(btnVals[12], 0, 150, 60, 60);
        this.addButton(btnVals[7], 75, 150, 60, 60);
        this.addButton(btnVals[8], 150, 150, 60, 60);
        this.addButton(btnVals[9], 225, 150, 60, 60);
        this.addButton(btnOpts[4], 300, 150, 60, 60);
        // forth floor
        this.addButton(btnVals[13], 0, 225, 60, 60);
        this.addButton(btnVals[4], 75, 225, 60, 60);
        this.addButton(btnVals[5], 150, 225, 60, 60);
        this.addButton(btnVals[6], 225, 225, 60, 60);
        this.addButton(btnOpts[3], 300, 225, 60, 60);
        // fifth floor
        this.addButton(btnVals[14], 0, 300, 60, 60);
        this.addButton(btnVals[1], 75, 300, 60, 60);
        this.addButton(btnVals[2], 150, 300, 60, 60);
        this.addButton(btnVals[3], 225, 300, 60, 60);
        this.addButton(btnOpts[2], 300, 300, 60, 60);
        // sixth floor
        this.addButton(btnVals[15], 0, 375, 60, 60);
        this.addButton(btnVals[0], 75, 375, 60, 60);
        this.addButton(btnOpts[8], 150, 375, 60, 60);
        this.addButton(btnOpts[7], 225, 375, 60, 60);
        this.addButton(btnOpts[6], 300, 375, 60, 60);

        this.add(btnPanel);

        // 全局监听
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();
                char keyChar = e.getKeyChar();
                if (keyChar == '(') {
                    btnOpts[0].doClick();
                } else if (keyChar == ')') {
                    btnOpts[1].doClick();
                } else if (keyChar == '+') {
                    btnOpts[2].doClick();
                } else if (keyChar == '-') {
                    btnOpts[3].doClick();
                } else if (keyChar == '*') {
                    btnOpts[4].doClick();
                } else if (keyChar == '/') {
                    btnOpts[5].doClick();
                } else if (keyChar == '=' || key == KeyEvent.VK_ENTER) {
                    btnOpts[6].doClick();
                } else if (keyChar == '.') {
                    btnOpts[7].doClick();
                } else if (keyChar == 'x') {
                    btnOpts[8].doClick();
                } else if (keyChar >= '0' && keyChar <= '9') {
                    btnVals[keyChar - '0'].doClick();
                } else if (keyChar >= 'a' && keyChar <= 'f') {
                    btnVals[keyChar - 'a' + 10].doClick();
                } else if (key == KeyEvent.VK_BACK_SPACE) {
                    btnCabs[1].doClick();
                }
            }
        });
    }

    private void addButton(JButton button, int x, int y, int w, int h) {
        button.setBounds(x, y, w, h);
        btnPanel.add(button);
    }


    public void view() {
        init();
        this.setTitle("Calculator");
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setFocusable(true);
        this.setVisible(true);
        Insets insets = this.getInsets();
        this.setSize(420, insets.top + 615); // 140 * 205
        this.setLocationRelativeTo(null);
    }

    public String getShowText() {
        return showLabel.getText();
    }

    public void setShowText(String text) {
        showLabel.setText(text);
    }

    public void setOctLabel(String text) {
        octLabel.setText(text);
    }

    public void setDecLabel(String text) {
        decLabel.setText(text);
    }

    public void setHexLabel(String text) {
        hexLabel.setText(text);
    }

    private boolean isVals(String x) {
        for (int i = 0; i < VALS.length; ++i)
            if (x != null && x.equals(VALS[i]))
                return true;
        return false;
    }

    class CalcValsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String text = getShowText();
            if (text.length() < 1) {
                setShowText(command);
            } else if (text.length() < 45) {
                setShowText(text + command);
            }
        }

    }

    class CalcOptsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String text = getShowText();
            if (text.length() < 1) {
                if (command.equals("("))
                    setShowText("(");
                return;
            }
            if (command.equals("=")) {

                Calc calc = new Calc(getShowText());
                String result = calc.getResult();
                if (result.equals("词法错误")) {
                    setOctLabel("");
                    setDecLabel("词法错误");
                    setHexLabel("");
                } else if (result.equals("语法错误")) {
                    setOctLabel("");
                    setDecLabel("语法错误");
                    setHexLabel("");
                } else {
                    String oct = calc.toUnsignedString(new BigDecimal(result), 8);
                    if (oct.charAt(0) == '-') {
                        oct = oct.substring(1);
                        setOctLabel("-0" + oct);
                    } else setOctLabel("0" + oct);
                    setDecLabel(result);
                    String hex = calc.toUnsignedString(new BigDecimal(result), 16);
                    if (hex.charAt(0) == '-') {
                        hex = hex.substring(1);
                        setHexLabel("-0x" + hex);
                    } else setHexLabel("0x" + hex);
                }

            } else {
                String LastThing = text.substring(text.length() - 1);
                if (command.equals(".")) {
                    if (!isVals(LastThing))
                        return;
                } else if (command.equals("x")) {
                    if (!LastThing.equals("0"))
                        return;
                } else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/")) {
                    if (LastThing.equals("+") || LastThing.equals("-") || LastThing.equals("*") || LastThing.equals("/"))
                        text = text.substring(0, text.length() - 1);
                    else if (!LastThing.equals(")") && !isVals(LastThing))
                        return;
                }
                if (text.length() < 1) {
                    setShowText(command);
                } else if (text.length() < 45) {
                    setShowText(text + command);
                }
            }
        }

    }

    private void btnClear() {
        setShowText("");
        setDecLabel("");
        setOctLabel("");
        setHexLabel("");
    }

    class CalcCabsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            String text = getShowText();
            if (command.equals("Clear")) {
                btnClear();
            } else {
                if (text.length() >= 1) setShowText(text.substring(0, text.length() - 1));
            }
        }

    }

}
