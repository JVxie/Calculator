import javafx.util.Pair;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

class LexDFAFrame extends JFrame {

    private String title;
    private JLabel showLabel; // 显示文本框
    private JTextField writeField; // 输入框
    private JButton showButton; // 显示按钮
    private JScrollPane scrollPane; //滚动条

    LexDFAFrame() {
        this("Lex DFA");
    }

    LexDFAFrame(String title) {
        this.title = title;
    }

    public void init() {
        this.setLayout(null);
        writeField = new JTextField(10);
        writeField.setBounds(45, 45, 720, 45);

        showLabel = new JLabel("输入式子，点击按钮或者按下Enter进行语法分析，只会保留100个字符");

        scrollPane = new JScrollPane(showLabel);
        scrollPane.setBounds(45, 135, 810, 720);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        showButton = new JButton("词法分析");
        showButton.setBounds(765, 45, 90, 45);
        showButton.addActionListener(new ShowListener());

        writeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    showButton.doClick();
                }
            }
        });
        writeField.setDocument(new LimitDocument(80));
        this.add(writeField);
        this.add(scrollPane);
        this.add(showButton);
    }

    public void view() {
        init();
        this.setTitle(title);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        Insets insets = this.getInsets();
        this.setSize(insets.left + insets.right + 900, insets.top + insets.bottom + 900);
        this.setLocationRelativeTo(null);
    }

    class ShowListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            LexDFA lex = new LexDFA(writeField.getText());
            Vector<Pair<String, String>> result = lex.toLexDFAFrame();
            String text = "输入式子，点击按钮或者按下Enter进行词法分析，只会保留100个字符";
            if (result == null || result.size() == 0) {
                showLabel.setText(text);
                return;
            }
            text = "<html><body>";
            for (int i = 0; i < result.size(); ++i) {
                text += result.elementAt(i).getKey() + "：" + result.elementAt(i).getValue() + "<br />";
            }
            text += "</body></html>";
            showLabel.setText(text);
        }

    }

    class LimitDocument extends PlainDocument {

        private int maxLength;

        public LimitDocument(int newMaxLength) {
            super();
            maxLength = newMaxLength;
        }

        public LimitDocument() {
            this(10);
        }

        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            str = str.replaceAll(" ", "");
            if (getLength() + str.length() > maxLength) {
                if (maxLength - getLength() > 0)
                    str = str.substring(0, maxLength - getLength());
                else str = "";
            }
            super.insertString(offset, str, a);
        }

    }

}
