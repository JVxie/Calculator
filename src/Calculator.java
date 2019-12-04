import com.alee.laf.WebLookAndFeel;

import javax.swing.*;

public class Calculator {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WebLookAndFeel.install();
            CalculatorFrame calculatorFrame = new CalculatorFrame();
            calculatorFrame.view();
        });
    }

}
