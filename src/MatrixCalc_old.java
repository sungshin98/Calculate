import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class MatrixCalc_old extends JFrame {
    private JTextField inputSpace;
    private String num = "";
    private String prev_operation = "";
    private ArrayList<String> equation = new ArrayList<String>();

    public static void Matrix(String[] args) {
        Scanner scan = new Scanner(System.in);

        // 첫 번째 행렬 크기 입력 받기
        System.out.print("첫 번째 행렬의 행(row) : ");
        int rowA = Integer.parseInt(scan.nextLine());

        System.out.print("첫 번째 행렬의 열(col) : ");
        int colA = Integer.parseInt(scan.nextLine());

        int[][] matrixA = inputMatrix(rowA, colA, scan);

        // 두 번째 행렬 크기 입력 받기
        System.out.print("두 번째 행렬의 행(row) : ");
        int rowB = Integer.parseInt(scan.nextLine());

        System.out.print("두 번째 행렬의 열(col) : ");
        int colB = Integer.parseInt(scan.nextLine());

        int[][] matrixB = inputMatrix(rowB, colB, scan);

        // 행렬 출력
        System.out.println("\n첫 번째 행렬:");
        printMatrix(matrixA);
        System.out.println("\n두 번째 행렬:");
        printMatrix(matrixB);

        // 연산: 덧셈, 뺄셈, 곱셈
        if (colA == rowB) {
            System.out.println("\n첫 번째 행렬 + 두 번째 행렬:");
            printMatrix(addMatrices(matrixA, matrixB));
        } else {
            System.out.println("\n덧셈 불가: 첫 번째 행렬의 열과 두 번째 행렬의 행이 일치하지 않습니다.");
        }

        if (rowA == rowB && colA == colB) {
            System.out.println("\n첫 번째 행렬 - 두 번째 행렬:");
            printMatrix(subtractMatrices(matrixA, matrixB));
        } else {
            System.out.println("\n뺄셈 불가: 두 행렬의 크기가 다릅니다.");
        }

        if (colA == rowB) {
            System.out.println("\n첫 번째 행렬 * 두 번째 행렬:");
            printMatrix(multiplyMatrices(matrixA, matrixB));
        } else {
            System.out.println("\n곱셈 불가: 첫 번째 행렬의 열과 두 번째 행렬의 행이 일치하지 않습니다.");
        }
    }

    // 함수: 행렬 입력 받기
    public static int[][] inputMatrix(int rows, int cols, Scanner scan) {
        int[][] matrix = new int[rows][cols];
        System.out.println("행렬의 요소를 입력하세요 (각 요소는 공백으로 구분됩니다):");
        for (int i = 0; i < rows; i++) {
            String[] elements = scan.nextLine().split(" ");
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Integer.parseInt(elements[j]);
            }
        }
        return matrix;
    }

    // 함수: 두 행렬의 덧셈
    public static int[][] addMatrices(int[][] A, int[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }

    // 함수: 두 행렬의 뺄셈
    public static int[][] subtractMatrices(int[][] A, int[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }

    // 함수: 두 행렬의 곱셈
    public static int[][] multiplyMatrices(int[][] A, int[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int colsB = B[0].length;
        int[][] result = new int[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    // 함수: 행렬 출력
    public static void printMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
