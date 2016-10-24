package petabyte;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
   
    public static JFrame frame;
    private static JPanel gamePanel;
    private static JLabel statusbar;
    
    /***
     * Declaring Number of Rows and Column
     */
    private static int noOfRows = 23;
    private static int noOfCols = 23;
    
    /***
     * Static boolean to be accessed across all classes
     */
    public static boolean playingGame;

    /***
     * Dimension of the frame
     */
    private static int height;
    private static int width;
    
    public static int highscore;
    public static boolean fnf=false;
    
    /***
     * Declare the menu bar and its items (GUI elements)
     */
    private final JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu, viewMenu, helpMenu;
    private static JMenuItem pauseItem;
    private JMenuItem exitItem, newGameItem, highscoreItem, objective, aboutItem;
    
    /***
     * Constructor for MainFrame
     */
    public MainFrame(){
        frame = new JFrame();        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("ColorBlox");
        frame.setResizable(false);
        frame.setJMenuBar(buildMenuBar());        
        statusbar = new JLabel("");
        gamePanel = new JPanel(new BorderLayout(5,5));
        frame.add(gamePanel);
        startNewGame();
        frame.setVisible(true);  
    }
    
    /***
     * Method to start a new game
     */
    public static void startNewGame(){
        gamePanel.removeAll();
        gamePanel.add(statusbar, BorderLayout.SOUTH);
        gamePanel.add(new MainGame(statusbar,noOfRows,noOfCols), BorderLayout.CENTER);        
        calcDimensions();
        highscore = getHighscore();
        gamePanel.setPreferredSize(new Dimension(width,height));
        gamePanel.validate();
        gamePanel.repaint();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth()-width)/2);
        int y = (int) ((dimension.getHeight()-height)/2);
        frame.setLocation(x,y-40);
        frame.pack();
    }
    
    /***
     * Building the menu bar and it's elements 
     */
    private JMenuBar buildMenuBar() {
        //Create the fileMenu and it's items
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        highscoreItem = new JMenuItem("Highscore");
        highscoreItem.setMnemonic('H');
        highscoreItem.addActionListener(new HighscoreListener());
        exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('X');
        exitItem.addActionListener(new ExitListener());

        
        //Add file items to the fileMenu
        fileMenu.add(highscoreItem);
        fileMenu.add(exitItem);

        //Create the viewMenu and it's items
        viewMenu = new JMenu("Game");
        viewMenu.setMnemonic('G');
        newGameItem = new JMenuItem("New Game");
        newGameItem.setMnemonic('N');
        newGameItem.addActionListener(new newGameListener());
        
        
        //Add difficulty and view items to viewMenu
        viewMenu.add(newGameItem);

        //Create the helpMenu and it's item
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        objective = new JMenuItem("Objective");
        objective.addActionListener(new objectiveListener());
        objective.setMnemonic('O');
        aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new AboutListener());
        aboutItem.setMnemonic('A');
        
        
        helpMenu.add(objective);
        helpMenu.add(aboutItem);
        
        
        
        //Add File, View and Help Menus to the JMenuBar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }

    
    /***
     * Accessor for the number of columns
     * @return number of columns
    */
    public static int getNoOfCols()
    {
        return noOfCols;
    }

    /***
     * Mutator for the number of columns 
     * @param noOfCols
     */
    public static void setNoOfCols(int noOfCols)
    {
        MainFrame.noOfCols = noOfCols;
    }

    /***
     * Accessor for the number of rows
     * @return number of rows
     */
    public static int getNoOfRows()
    {
        return noOfRows;
    }

    /***
     * Mutator for the number of rows
     * @param noOfRows 
     */
    public static void setNoOfRows(int noOfRows)
    {
        MainFrame.noOfRows = noOfRows;
    }
    
    /***
     * Setter for width
     * @param width 
     */
    public static void setWidth(int width)
    {
        MainFrame.width = width;
    }
    
    /***
     * Setter for height
     * @param height 
     */
    public static void setHeight(int height)
    {
        MainFrame.height = height;
    }
    
    /***
     * Method for calculating dimensions needed to hold the images
     */
    public static void calcDimensions(){
    	width = noOfCols*25;
    	height = noOfRows*25+20;
    }

    /***
     * Event handler for high score menu item
     */
    private static class HighscoreListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,"Highscore is "+highscore,"Highscore",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /***
     * Event handler for objective menu item
     */
    private static class objectiveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Click and conquer all the blocks."
                    + "\nTo conquer other you must grow in numbers first."
                    + "\nREMEMBER: EVERY CLICK COUNTS.\nBest of luck.", "Objective", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
    /***
     * Event handler for New game menu item
     */
    public class newGameListener implements ActionListener
    {
        //Create a newGame after user agrees
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int ask = JOptionPane.showConfirmDialog(null, "Are you sure?");
            if (ask == 0)
            {
                MainFrame.startNewGame();
            }
        }
    }
    
    /***
     * Event handler for about menu item
     */
    public class AboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            JOptionPane.showMessageDialog(frame,"Developed by:\nMd. Sharif Siddique"
                    + "\nEmail: giantsrf@gmail.com", "About ColorBlox v1.01",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /***
     * Event handler for exit menu item
     */
    public class ExitListener implements ActionListener {

            @Override
    	public void actionPerformed(ActionEvent e) {
    		//Quit the program
    		System.exit(0);
    	}
    }
    
    /***
     * Method for getting high score from file
     * @return high score from file 
     */
    public static int getHighscore(){
        int hs = 0;
        try {
            Scanner scanner = new Scanner(new File("Customized.gg"));
            if (scanner.hasNextInt()) {
                hs = scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            hs=1000000;
            fnf=true;
        }
        System.out.println(hs);
        return hs;
        
    }
}
