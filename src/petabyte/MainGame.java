package petabyte;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static petabyte.MainFrame.highscore;

/**
 * *
 * Class holding the game mechanics
 *
 * @author Md. Sharif Siddique
 */
public class MainGame extends JPanel {

    private final int NUM_IMAGES = 12;
    private final int CELL_SIZE = 25;
    private static int base;
    private static int baseColor;
    private static int selected;
    private static int[][] field;
    private static boolean[][] flag;
    private static boolean inGame;
    private int blocks;
    private final Image[] img;
    private static int rows;
    private static int cols;
    private int all_cells;
    private final JLabel statusbar;
    private final String[] current = {
        "Yellow", "Blue", "Green", "Pink", "Purple", "Red"
    };
    Object[] options = {"Start new game",
        "Exit"};
    public int score;
    private String blockStr;
    private int clickCount;
    private static int difficulty;
    private static boolean solved = false;
    private static final int xx[] = {0, 0, -1, 1, -1, -1, 1, 1};
    private static final int yy[] = {1, -1, 0, 0, 1, -1, -1, 1};

    /**
     * *
     * Constructor
     */
    public MainGame(JLabel statusbar, int noOfRows, int noOfCols) {
        this.statusbar = statusbar;
        rows = noOfRows;
        cols = noOfCols;

        //Declare Image Array
        img = new Image[NUM_IMAGES];

        //Load images into array
        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = (new ImageIcon(this.getClass().getResource((i) + ".png"))).getImage();
        }
        setDoubleBuffered(true);
        addMouseListener(new BlocksAdapter());
        newGame();
    }

    /**
     * *
     * set solved (mutator/setter)
     *
     * @param newState
     */
    public static void setSolved(boolean newState) {
        solved = newState;
    }

    /**
     * *
     * set inGame (mutator/setter)
     *
     * @param newState
     */
    public static void setInGame(boolean newState) {
        inGame = newState;
    }

    /**
     * *
     * set difficulty (mutator/setter)
     *
     * @param newDifficulty
     */
    public static void setDifficulty(int newDifficulty) {
        difficulty = newDifficulty;
    }

    /**
     * *
     * Gets the field and returns it (getter)
     *
     * @return field
     */
    public static int[][] getField() {
        return field;
    }

    /**
     * *
     * Sets the field with a new array (mutator/setter)
     *
     * @param arr
     */
    public static void setField(int[][] arr) {
        field = arr;
    }

    /**
     * *
     * method for starting a new game
     */
    public void newGame() {
        Random random;
        int current_col;
        difficulty = 0;

        int position = 0;
        int cell = 0;
        clickCount = 0;

        random = new Random();

        all_cells = rows * cols;

        field = new int[rows][cols];
        flag = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flag[i][j] = true;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                field[i][j] = random.nextInt(6);
            }
        }
        baseColor = random.nextInt(6);
        inGame = true;
        statusbar.setText("To win finish in 40 clicks.");
    }

    /**
     * *
     * Method to Traverse the blocks using Depth First Search
     *
     * @param cRow
     * @param cCol
     */
    public void dfsColor(int cRow, int cCol) {
        if (cRow < 0 || cRow > 22 || cCol < 0 || cCol > 22) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            int xxx = cRow + xx[i];
            int yyy = cCol + yy[i];
            if (xxx >= 0 && xxx < 23 && yyy >= 0 && yyy < 23 && field[cRow][cCol] == field[xxx][yyy] && flag[xxx][yyy] == true) {
                flag[xxx][yyy] = false;
                dfsColor(xxx, yyy);
                field[cRow][cCol]=selected;
            }
        }
    }

    /**
     * *
     * Method for painting the blocks according to the values in field
     *
     * @param g
     */
    @Override
    public void paint(Graphics g){
        int cell = 0;
        int blocks = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (flag[i][j] == false && inGame) {
                    field[i][j] = selected;
                    dfsColor(i, j);
                    blocks++;
                }
                cell = field[i][j];
                g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);
            }
        }
        if (blocks == 529) {
            inGame = false;
            repaint();
            System.out.println("end");
            if (!MainFrame.fnf) {
                statusbar.setText("Highscore:" + highscore);
            }
            gameEnded();
        }
    }

    /**
     * *
     * Click event when user clicked a field
     */
    class BlocksAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            selected = field[cRow][cCol];

            boolean rep = false;

            dfsColor(cRow, cCol);
            repaint();

            clickCount++;
            if (MainFrame.fnf) {
                statusbar.setText("Total Clicks:" + clickCount + "          "
                        + "Selected Color: " + current[selected]);
            } else {
                statusbar.setText("Highscore: " + highscore + "         "
                        + "Total Clicks:" + clickCount + "          "
                        + "Selected Color: " + current[selected]);
            }
        }
    }

    /**
     * *
     * This method runs when a game ends
     */
    public void gameEnded() {
        score = clickCount;
        if (score < highscore) {
            highscore = score;
            JOptionPane.showMessageDialog(this, "New Highscore!!!\nYou took only " + highscore + " click!", "Congrats!", JOptionPane.INFORMATION_MESSAGE);
        }
        try {
            File fac = new File("Customized.gg");
            if (!fac.exists()) {
                fac.createNewFile();
            }
            System.out.println("\n----------------------------------");
            System.out.println("The file has been created.");
            System.out.println("------------------------------------");
            FileWriter wr = new FileWriter(fac);
            System.out.println(highscore);
            wr.write(new Integer(highscore).toString());
            wr.close();
        } catch (IOException e) {
            Logger.getLogger(MainGame.class.getName()).log(Level.SEVERE, null, e);
        }
        if(clickCount>40){
            blockStr="You Lost!";
        }
        else{
            blockStr="You Win!";
        }
        int n = JOptionPane.showOptionDialog(this,
                "Total mouse clicks: " + score
                + ". Would you like to play again?",
                blockStr,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (n == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
//        MainFrame.frame.setVisible(false);
//        new MainFrame();
        MainFrame.frame.dispose();
        new MainFrame();
    }
}
