
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Calculator extends JFrame {

    private String title;
    private JButton[] buttons;
    private LexDFAFrame lexDFAFrame;
    private SyntaxFrame syntaxFrame;
    private CalcFrame calcFrame;

    Calculator() {
        this("编译原理实验");
    }

    Calculator(String title) {
        this.title = title;
    }

    public void init() {
        this.setLayout(null);
        buttons = new JButton[3];
        buttons[0] = new JButton("词法分析器");
        buttons[1] = new JButton("语法分析器");
        buttons[2] = new JButton("成型计算器");
        ButtonListener buttonListener = new ButtonListener();
        addButton(buttons[0], 20, 35, 107, 80, buttonListener);
        addButton(buttons[1], 147, 35, 107, 80, buttonListener);
        addButton(buttons[2], 277, 35, 107, 80, buttonListener);
        this.add(buttons[0]);
        this.add(buttons[1]);
        this.add(buttons[2]);
    }

    private void addButton(JButton button, int x, int y, int w, int h, ActionListener ac) {
        button.setBounds(x, y, w, h);
        button.addActionListener(ac);
    }

    public void view() {
        init();
        this.setTitle(title);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        Insets insets = this.getInsets();
        this.setSize(401, insets.top + 150);
        this.setLocationRelativeTo(null);
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("词法分析器")) {
                if (lexDFAFrame == null || !lexDFAFrame.isVisible()) {
                    lexDFAFrame = new LexDFAFrame();
                    lexDFAFrame.view();
                } else {
                    lexDFAFrame.setLocationRelativeTo(null);
                    lexDFAFrame.setState(Frame.NORMAL);
                }
            } else if (command.equals("语法分析器")) {
                if (syntaxFrame == null || !syntaxFrame.isVisible()) {
                    syntaxFrame = new SyntaxFrame();
                    syntaxFrame.view();
                } else {
                    syntaxFrame.setLocationRelativeTo(null);
                    syntaxFrame.setState(Frame.NORMAL);
                }
            } else if (command.equals("成型计算器")) {
                if (calcFrame == null || !calcFrame.isVisible()) {
                    calcFrame = new CalcFrame();
                    calcFrame.view();
                } else {
                    calcFrame.setLocationRelativeTo(null);
                    calcFrame.setState(Frame.NORMAL);
                }
            }
        }

    }

}
