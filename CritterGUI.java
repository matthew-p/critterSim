import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
/**********************************************************
GUI for a critter simulation.  Impements Runnable to allow
a method to run in the background while the user continues
to click on buttons.

@author Scott Grissom
@version August 2016
 ***********************************************************/
public class CritterGUI extends JFrame implements ActionListener, Runnable{

    /** simulation speed */
    private final int DELAY = 50;

    /** is simulation currently runnning? */
    private boolean isRunning;  

    /** the simulation object that controls everything */
    private Simulation world; 

    /** displays updated statistics */
    JTextArea statsArea;

    private final int NUM_CRIT = 10;
    // define buttons as neeeded
    private JButton ants, birds, hippos, vultures, myCritters, start, stop;

    // define menu items as needed
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem clear;
    private JMenuItem quit;

    /************************************************************
    Main method displays the simulation GUI
     ************************************************************/
    public static void main(String arg[]){
        CritterGUI gui = new CritterGUI();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setTitle("Critter Simulation");
        gui.setSize(600,600);
        gui.pack();
        gui.setVisible(true);
    }

    /************************************************************
    Create the GUI
     ************************************************************/
    public CritterGUI(){

        // simulation is turned off 
        isRunning = false;

        // create the lay out
        setLayout(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.insets = new Insets(10,10,10,10);
        // Place the simulation on the screen
        position.gridx = 0;
        position.gridy = 1;
        position.gridwidth = 6;           
        world = new Simulation();
        add(world, position);
        
        // menu
        fileMenu = new JMenu("File");
        clear = new JMenuItem("Clear");
        quit = new JMenuItem("Quit");
        clear.addActionListener(this);
        quit.addActionListener(this);
        fileMenu.add(clear);
        fileMenu.add(quit);
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Place a label
        position.gridx = 6;
        position.gridy = 0;  
        position.gridwidth = 1;
        add(new JLabel("Live Stats"),position);

        // Place stats area below the label
        statsArea = new JTextArea(7,12);
        statsArea.setBackground(Color.YELLOW);
        position.gridx = 6;
        position.gridy = 1;    
        position.anchor = GridBagConstraints.PAGE_START;
        add(statsArea, position);  
        statsArea.setText(world.getStats());
        

        // place each button
        start = new JButton("Start");
        start.setForeground(Color.BLACK);
        position.gridx = 2;
        position.gridy = 0;
        add(start, position);
        start.addActionListener(this);
        
        stop = new JButton("Stop");
        position.gridx = 3;
        add(stop, position);
        stop.addActionListener(this);
        
        ants = new JButton( "Ants" );
        ants.setForeground(Color.RED);
        position.gridx = 0;
        position.gridy = 2;   
        add(ants, position);
        ants.addActionListener(this);
          
        birds = new JButton("Birds");
        birds.setForeground(Color.BLUE);
        position.gridx = 1;
        add(birds, position);
        birds.addActionListener(this);
        
        hippos = new JButton("Hippos");
        hippos.setForeground(Color.GRAY);
        position.gridx = 2;
        add(hippos, position);
        hippos.addActionListener(this);
        
        vultures = new JButton("Vultures");
        vultures.setForeground(Color.BLACK);
        position.gridx = 3;
        add(vultures, position);
        vultures.addActionListener(this);
        
        myCritters = new JButton("My Critters");
        myCritters.setForeground(Color.GREEN);
        position.gridx = 4;
        add(myCritters, position);
        myCritters.addActionListener(this);

        // this must be at the end of this method to
        // start the simulation in separate thread
        new Thread(this).start();
    }

    /************************************************************
    Respond to button clicks
    @param e action even triggered by user
     ************************************************************/
    public void actionPerformed(ActionEvent e){
        /*
         *     private JButton ants, birds, hippos, vultures, myCritters, start, stop;
                private JMenuBar menuBar;
                private JMenu, fileMenu;
                private JMenuItem clear;
                private JMenuItem quit;
         */
        // exit application if QUIT menu item
        if (e.getSource() == quit) {
            System.exit(1);
        }
        // set running variable to true if START button
        if (e.getSource() == start) {
            isRunning = true;
        }
        // set running variable to false if STOP button
        if (e.getSource() == stop) {
            isRunning = false;
        }    
        // reset simulation if CLEAR menu item
        if (e.getSource() == clear) {
            world.reset();
        }
        //inject animals for each button
        if(e.getSource() == ants){ 
            world.addAnts(NUM_CRIT);
        }
        if (e.getSource() == birds) {
            world.addBirds(NUM_CRIT);
        }
        if (e.getSource() == hippos) {
            world.addHippos(NUM_CRIT);
        }
        if (e.getSource() == vultures) {
            world.addVultures(NUM_CRIT);
        }
        if (e.getSource() == myCritters) {
            world.addMyCritters(NUM_CRIT);
        }      

        // Afterwards, update display and statistics
        world.repaint();
        statsArea.setText(world.getStats());
    }

    /************************************************************
    Once started, this method runs forever in a separate thread
    The simulation only advances and displays if the boolean
    variable is currently true
     ************************************************************/
    public void run(){
        try {

            // run forever
            while(true) {

                // only update simulation if it is running
                if (isRunning) {
                    world.oneStep();
                    statsArea.setText(world.getStats());
                }

                // pause between steps.  Otherwise, the simulation
                // would move too quickly to see
                Thread.sleep(DELAY);
            }
        }
        catch (InterruptedException ex) {
        }
    }    
}