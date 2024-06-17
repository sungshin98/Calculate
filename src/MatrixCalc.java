import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatrixCalc extends JFrame {
    private JTextField inputSpace;
    private String num = "";
    private String prev_operation = "";
    private ArrayList<String> equation = new ArrayList<String>();
    public MatrixCalc() {
        setLayout(null);

        inputSpace = new JTextField();
        inputSpace.setEditable(false);
        inputSpace.setBackground(Color.WHITE);
        inputSpace.setHorizontalAlignment(JTextField.RIGHT);
        inputSpace.setFont(new Font("Arial", Font.BOLD, 50));
        inputSpace.setBounds(8, 10, 360, 70);
        inputSpace.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char ch = e.getKeyChar();
                if (Character.isDigit(ch) || ch == '[' || ch == ']' || ch == ',' || ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '.') {
                    inputSpace.setText(inputSpace.getText() + ch);
                } else if (ch == '\b') { // Backspace key
                    String text = inputSpace.getText();
                    if (text.length() > 0) {
                        inputSpace.setText(text.substring(0, text.length() - 1));
                    }
                } else if (ch == '\n') { // Enter key
                    String result = processInput(inputSpace.getText());
                    inputSpace.setText(result);
                    num = "";
                }
            }
        }
}