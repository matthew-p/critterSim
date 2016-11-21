import java.util.*;
import javax.swing.*;
import java.awt.*;

/****************************************************
 * Simulates a 2D world of critters that move around
 * and fight if they inhabit the same location.
 * 
 * @author Scott Grissom 
 * @version August 2016
 ***************************************************/
public class Simulation extends JPanel{
   
    /** a 2D world of critters */
    private GVcritter[][] theWorld;
    
    /** a collection of all live critters */
    private ArrayList <GVcritter> allCritters;
    
    /** control size of the world */
    private final int ROWS=50, COLUMNS=70, SIZE=10;
    
    /** number of Ants alive in the simulation */
    private int steps, numAnts, numBirds, numHippos, numVultures, numLakers;
        
   
/****************************************************
Constructor instantiates and initializes all 
instance members.
****************************************************/
    public Simulation(){
        theWorld = new GVcritter[ROWS][COLUMNS];
        allCritters = new ArrayList<GVcritter>();   
        numAnts=0;
        numBirds=0;
        numHippos=0;
        numVultures=0;
        numLakers=0;
        setPreferredSize(new Dimension(COLUMNS*SIZE, ROWS*SIZE));
        steps = 0;
    }

/****************************************************
Add the requested number of Ants into the simulation.
Repeatedly ask for a random location that is free.
Increment the number of Ants in the simulation.

@param num number of ants
****************************************************/ 
    public void addAnts(int num){
        numAnts += num;
        for(int i=1;i<=num;i++){
            // create a new Ant at an open location
            Location loc = getOpenLocation();
            Ant c = new Ant(loc);
            placeCritter(c);
        }
    }


/******************************************************
Move forward on step of the simulation
*****************************************************/  
    public void oneStep(){
        
        // shuffle the arraylist of critters for better performance
        Collections.shuffle(allCritters);
        //stepCount++;
        
        // step throgh all critters using traditional for loop
        for(int i=0; i<allCritters.size(); i++){
            GVcritter attacker = allCritters.get(i);

            // what location does critter want to move to?
            GVcritter.Direction dir = attacker.getMoveDirection();
            Location previousLoc = attacker.getLocation();
            Location nextLoc = getRelativeLocation(previousLoc, dir);  
            
            // who is at the next location?
            GVcritter defender = theWorld[nextLoc.getRow()][nextLoc.getCol()];

            // no critters here so OK for critter 1 to move
            if(defender == null){
                theWorld[nextLoc.getRow()][nextLoc.getCol()] = attacker;
                attacker.setLocation(nextLoc);
                theWorld[previousLoc.getRow()][previousLoc.getCol()] = null;

            // both critters the same species so peacefully bypass 
            }else if(attacker.getSpecies() == defender.getSpecies()){
                
                // update critter locations
                attacker.setLocation(nextLoc);
                defender.setLocation(previousLoc);
                
                // update positions in the world
                theWorld[nextLoc.getRow()][nextLoc.getCol()] = attacker;
                theWorld[previousLoc.getRow()][previousLoc.getCol()] = defender;
            
            //different species so they fight at location of critter 2
            }else if(attacker.getSpecies() != defender.getSpecies()){
                fight(attacker, defender);
            }
        }
        
        // update drawing of the world
        repaint();
    }
    
/******************************************************
Step through the 2D world and paint each location white
(for no critter) or the critter's color.  The SIZE of 
each location is constant.
 
@param g graphics element used for display
*****************************************************/      
    public void paintComponent(Graphics g){
        for(int row=0; row<ROWS; row++){
            for(int col=0; col<COLUMNS; col++){
                GVcritter c = theWorld[row][col];

                // set color to white if no critter here
                if(c == null){
                    g.setColor(Color.WHITE);
                // set color to critter color   
                }else{    
                    g.setColor(c.getColor());
                }
                
                // paint the location
                g.fillRect(col*SIZE, row*SIZE, SIZE, SIZE);
            }
        }
    }
    
    public String getStats() {
        return String.format("Steps:\t%1$d \nAnts:\t%2$d \nBirds:\t%3$d \nHippos:\t%4$d \nVultures:\t%5$d",
                            steps, numAnts, numBirds, numHippos, numVultures); 
    }
    private Location getOpenLocation() {
        int x = Math.random() * COLUMNS;
        int y = Math.random() * ROWS;
        // NOTE: this will break if no open locations, 
        // and possibly run a long time if very few open
        while (theWorld[y][x] != null) {
            x = Math.random() * COLUMNS;
            y = Math.random() * ROWS;
        }
        return new Location(y, x);
    }
    private void placeCritter(GVcritter c) {
        allCritters.add(c);
        loc = c.getLocation();
        theWorld[loc.getRow()][loc.getCol()] = c;
    }
    private Location getRelativeLocation(Location loc, GVcritter.Direction d) {
        int y = loc.getRow();
        int x = loc.getCol();
        switch (d) {
            case GVcritter.Direction.NORTH: 
                if (y == 0)
                    y = ROWS -1;
                else
                    y--;
                break;
            case GVcritter.Direction.SOUTH:
                if (y == ROWS - 1)
                    y = 0;
                else 
                    y++;
                break;
            case GVcritter.Direction.EAST:
                if (x == COLUMNS - 1)
                    x = 0;
                else
                    x++;
                break;
            case GVcritter.Direction.WEST:
                if (x == 0)
                    x = COLUMNS - 1;
                else 
                    x--;
                break;
        }
        return new Location(y,x);
    }
}
