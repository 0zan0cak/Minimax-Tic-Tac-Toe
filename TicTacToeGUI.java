
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TicTacToeGUI extends JFrame implements ActionListener {

    JButton[][] buttons = new JButton[3][3];

    char[][] board = {
        {'_', '_', '_'},
        {'_', '_', '_'},
        {'_', '_', '_'}
    };

    boolean playerTurn = true;

    public TicTacToeGUI() {
        setTitle("Impossible AI - Minimax XOX");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));
        setLocationRelativeTo(null);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 100));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (clickedButton == buttons[i][j] && board[i][j] == '_') {
                    if (playerTurn) {
                        board[i][j] = 'X';
                        buttons[i][j].setText("X");
                        buttons[i][j].setForeground(new Color(40, 40, 200));
                        playerTurn = false;

                        checkGameOver();

                        if (!playerTurn && isMovesLeft(board)) {
                            int[] bestMove = findBestMove();
                            int aiRow = bestMove[0];
                            int aiCol = bestMove[1];

                            board[aiRow][aiCol] = 'O';
                            buttons[aiRow][aiCol].setText("O");
                            buttons[aiRow][aiCol].setForeground(new Color(200, 40, 40));

                            playerTurn = true;

                            checkGameOver();
                        }
                    }
                }
            }
        }
    }

    public void checkGameOver() {
        int score = evaluate(board);

        if (score == 10) {
            JOptionPane.showMessageDialog(this, "AI Won! (As expected...)");
            System.exit(0);
        } else if (score == -10) {
            JOptionPane.showMessageDialog(this, "You won! (This is impossible, there must be a mistake in the code!)");
            System.exit(0);
        } else if (!isMovesLeft(board)) {
            JOptionPane.showMessageDialog(this, "A draw! (Not bad for a human.)");
            System.exit(0);
        }
    }

    public static boolean isMovesLeft(char[][] b) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b[i][j] == '_') {
                    return true;
                }
            }
        }
        return false;
    }

    public static int evaluate(char[][] b) {
        for (int i = 0; i < 3; i++) {
            if (b[i][0] == b[i][1] && b[i][1] == b[i][2]) {
                if (b[i][0] == 'O') {
                    return 10;
                } else if (b[i][0] == 'X') {
                    return -10;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            if (b[0][i] == b[1][i] && b[1][i] == b[2][i]) {
                if (b[0][i] == 'O') {
                    return 10;
                } else if (b[0][i] == 'X') {
                    return -10;
                }
            }
        }

        if ((b[0][0] == b[1][1] && b[1][1] == b[2][2])) {
            if (b[0][0] == 'O') {
                return 10;
            } else if (b[0][0] == 'X') {
                return -10;
            }
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
            if (b[0][2] == 'O') {
                return 10;
            } else if (b[0][2] == 'X') {
                return -10;
            }
        }
        return 0;
    }

    public static int minimax(char[][] board, int depth, boolean isMax, int alpha, int beta) {
        int score = evaluate(board);

        if (score == 10) {
            return score - depth;
        }

        if (score == -10) {
            return score + depth;
        }

        if (!isMovesLeft(board)) {
            return 0;
        }

        if (isMax) {
            int best = -1000;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '_') {
                        board[i][j] = 'O';

                        int currentScore = minimax(board, depth + 1, false, alpha, beta);
                        best = Math.max(best, currentScore);
                        board[i][j] = '_';

                        alpha = Math.max(alpha, best);
                        if (beta <= alpha) {
                            return best;
                        }
                    }
                }
            }
            return best;
        } else {
            int best = 1000;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '_') {
                        board[i][j] = 'X';

                        int currentScore = minimax(board, depth + 1, true, alpha, beta);
                        best = Math.min(best, currentScore);
                        board[i][j] = '_';

                        beta = Math.min(beta, best);
                        if (beta <= alpha) {
                            return best;
                        }
                    }
                }
            }
            return best;
        }
    }

    public int[] findBestMove() {
        int bestVal = -1000;
        int bestRow = -1;
        int bestCol = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '_') {
                    board[i][j] = 'O';

                    int moveVal = minimax(board, 0, false, -1000, 1000);

                    board[i][j] = '_';

                    if (moveVal > bestVal) {
                        bestRow = i;
                        bestCol = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return new int[]{bestRow, bestCol};
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}
