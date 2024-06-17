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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calculator extends JFrame {

    private JTextField inputSpace;
    private String num = "";
    private String prev_operation = "";
    private ArrayList<String> equation = new ArrayList<String>();

    public Calculator() {
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
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 5, 10, 10));
        buttonPanel.setBounds(8, 90, 320, 230);

        String button_names[] = { "C", "÷", "×", "=", "7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "0", "Mat", "NULL", "NULL", "NULL"};
        JButton buttons[] = new JButton[button_names.length];

        for (int i = 0; i < button_names.length; i++) {
            buttons[i] = new JButton(button_names[i]);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            if (button_names[i].equals("C")) buttons[i].setBackground(Color.RED);
            else if ((i >= 4 && i <= 6) || (i >= 8 && i <= 10) || (i >= 12 && i <= 14)) buttons[i].setBackground(Color.BLACK);
            else buttons[i].setBackground(Color.GRAY);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBorderPainted(false);
            buttons[i].addActionListener(new PadActionListener());
            buttonPanel.add(buttons[i]);
        }

        add(inputSpace);
        add(buttonPanel);

        setTitle("Calculator");
        setVisible(true);
        setSize(390,410);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class PadActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String operation = e.getActionCommand();

            if (operation.equals("C")) {
                inputSpace.setText("");
            } else if (operation.equals("=")) {
                String result = processInput(inputSpace.getText());
                inputSpace.setText(result);
                num = "";
            } else if (operation.equals("Mat")) {
                // MatrixCalc 클래스를 호출하여 실행
                new MatrixCalc_old();
            } else if (operation.equals("Null")) {
                // 아무 일도 하지 않음
                return;
            } else if (operation.equals("+") || operation.equals("-") || operation.equals("×") || operation.equals("÷")) {
                if (inputSpace.getText().equals("") && operation.equals("-")) {
                    inputSpace.setText(inputSpace.getText() + e.getActionCommand());
                } else if (!inputSpace.getText().equals("") && !prev_operation.equals("+") && !prev_operation.equals("-") && !prev_operation.equals("×") && !prev_operation.equals("÷")) {
                    inputSpace.setText(inputSpace.getText() + e.getActionCommand());
                }
            } else {
                inputSpace.setText(inputSpace.getText() + e.getActionCommand());
            }
            prev_operation = e.getActionCommand();
        }
    }

    private String processInput(String inputText) {
        if (isVector(inputText)) {
            return processVectorCalculation(inputText);
        } else {
            return Double.toString(calculate(inputText));
        }
    }

    private boolean isVector(String input) {
        return input.matches("\\[(-?\\d+(\\.\\d+)?(,\\s*-?\\d+(\\.\\d+)?){2})\\]");
    }

    private String processVectorCalculation(String inputText) {
        String[] parts = inputText.split("\\s*(?=[+\\-])\\s*");
        String vector1Str = parts[0].trim();
        String vector2Str = parts[1].substring(1).trim(); // Remove the operator (+ or -)

        double[] vector1 = parseVector(vector1Str);
        double[] vector2 = parseVector(vector2Str);

        double[] result = new double[3];

        if (inputText.contains("+")) {
            for (int i = 0; i < 3; i++) {
                result[i] = vector1[i] + vector2[i];
            }
        } else if (inputText.contains("-")) {
            for (int i = 0; i < 3; i++) {
                result[i] = vector1[i] - vector2[i];
            }
        }

        return formatVector(result);
    }

    private double[] parseVector(String vectorStr) {
        Matcher matcher = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(vectorStr);
        double[] vector = new double[3];
        int index = 0;
        while (matcher.find()) {
            vector[index++] = Double.parseDouble(matcher.group());
        }
        return vector;
    }

    private String formatVector(double[] vector) {
        return String.format("[%.2f, %.2f, %.2f]", vector[0], vector[1], vector[2]);
    }

    private void fullTextParsing(String inputText) {
        equation.clear();
        num = "";

        for (int i = 0; i < inputText.length(); i++) {
            char ch = inputText.charAt(i);
            if (ch == '-' || ch == '+' || ch == '×' || ch == '÷') {
                equation.add(num);
                num = "";
                equation.add(ch + "");
            } else {
                num = num + ch;
            }
        }
        equation.add(num);
        equation.remove("");
    }

    public double calculate(String inputText) {
        fullTextParsing(inputText);

        double prev = 0;
        double current = 0;
        String mode = "";

        for (int i = 0; i < equation.size(); i++) {
            String s = equation.get(i);
            if (s.equals("+")) {
                mode = "add";
            } else if (s.equals("-")) {
                mode = "sub";
            } else if (s.equals("×")) {
                mode = "mul";
            } else if (s.equals("÷")) {
                mode = "div";
            } else {
                if ((mode.equals("mul") || mode.equals("div")) && !s.equals("+") && !s.equals("-") && !s.equals("×") && !s.equals("÷")) {
                    Double one = Double.parseDouble(equation.get(i - 2));
                    Double two = Double.parseDouble(equation.get(i));
                    Double result = 0.0;
                    if (mode.equals("mul")) {
                        result = one * two;
                    } else if (mode.equals("div")) {
                        result = one / two;
                    }
                    equation.add(i + 1, Double.toString(result));
                    for (int j = 0; j < 3; j++) {
                        equation.remove(i - 2);
                    }
                    i -= 2;
                }
            }
        }

        for (String s : equation) {
            if (s.equals("+")) {
                mode = "add";
            } else if (s.equals("-")) {
                mode = "sub";
            } else {
                current = Double.parseDouble(s);
                if (mode.equals("add")) {
                    prev += current;
                } else if (mode.equals("sub")) {
                    prev -= current;
                } else {
                    prev = current;
                }
            }
            prev = Math.round(prev * 100000) / 100000.0;
        }
        return prev;
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
