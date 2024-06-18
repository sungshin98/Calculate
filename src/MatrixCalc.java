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
        inputSpace.setBounds(8, 10, 560, 70);
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

        add(inputSpace);
        setTitle("Matrix Calculator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private String processInput(String input) {
        Pattern pattern = Pattern.compile("\\[(.*?)\\](\\+|\\-|\\*)(\\[(.*?)\\])");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            return "Invalid input!";
        }

        String matrix1Str = matcher.group(1);
        String operation = matcher.group(2);
        String matrix2Str = matcher.group(4);

        double[][] matrix1 = parseMatrix(matrix1Str);
        double[][] matrix2 = parseMatrix(matrix2Str);

        if (matrix1 == null || matrix2 == null) {
            return "Invalid matrix format!";
        }

        double[][] resultMatrix;
        switch (operation) {
            case "+":
                resultMatrix = addMatrices(matrix1, matrix2);
                break;
            case "-":
                resultMatrix = subtractMatrices(matrix1, matrix2);
                break;
            case "*":
                resultMatrix = multiplyMatrices(matrix1, matrix2);
                break;
            default:
                return "Unknown operation!";
        }

        if (resultMatrix == null) {
            return "Matrix dimensions do not match for this operation!";
        }

        return formatMatrix(resultMatrix);
    }

    private double[][] parseMatrix(String matrixStr) {
        String[] rows = matrixStr.split("],\\[");
        int rowCount = rows.length;
        int colCount = rows[0].split(",").length;

        double[][] matrix = new double[rowCount][colCount];
        try {
            for (int i = 0; i < rowCount; i++) {
                String[] elements = rows[i].replaceAll("[\\[\\]]", "").split(",");
                for (int j = 0; j < colCount; j++) {
                    matrix[i][j] = Double.parseDouble(elements[j]);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return matrix;
    }

    private double[][] addMatrices(double[][] matrix1, double[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;

        if (rows != matrix2.length || cols != matrix2[0].length) {
            return null;
        }

        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }

    private double[][] subtractMatrices(double[][] matrix1, double[][] matrix2) {
        int rows = matrix1.length;
        int cols = matrix1[0].length;

        if (rows != matrix2.length || cols != matrix2[0].length) {
            return null;
        }

        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return result;
    }

    private double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int cols2 = matrix2[0].length;

        if (cols1 != rows2) {
            return null;
        }

        double[][] result = new double[rows1][cols2];
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    private String formatMatrix(double[][] matrix) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < matrix.length; i++) {
            if (i > 0) {
                sb.append("],[");
            } else {
                sb.append("[");
            }
            for (int j = 0; j < matrix[i].length; j++) {
                if (j > 0) {
                    sb.append(",");
                }
                sb.append(matrix[i][j]);
            }
        }
        sb.append("]]");
        return sb.toString();
    }

    public static void main(String[] args) {
        new MatrixCalc();
    }
}