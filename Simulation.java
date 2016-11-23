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
    private int steps;// numAnts, numBirds, numHippos, numVultures, numMyCritters;
    private Map<GVcritter.Species, Integer> totals = new EnumMap<>(GVcritter.Species.class);
   
/****************************************************
Constructor instantiates and initializes all 
instance members.
****************************************************/
    public Simulation(){
        theWorld = new GVcritter[ROWS][COLUMNS];
        allCritters = new ArrayList<GVcritter>();  
        /*
        numAnts=0;
        numBirds=0;
        numHippos=0;
        numVultures=0;
        numMyCritters=0;
        */
        for (GVcritter.Species s : GVcritter.Species.values()) {
            totals.put(s, 0);
        }
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
        totals.put(GVcritter.Species.ANT, totals.get(GVcritter.Species.ANT) + num);
        //numAnts += num;
        for(int i=1;i<=num;i++){
            // create a new Ant at an open location
            Location loc = getOpenLocation();
            Ant c = new Ant(loc);
            placeCritter(c);
        }
    }
    public void addBirds(int num) {
        totals.put(GVcritter.Species.BIRD, totals.get(GVcritter.Species.BIRD) + num);
        //numBirds += num;
        for (int i = 0; i < num; i++) {
            Location loc = getOpenLocation();
            Bird b = new Bird(loc);
            placeCritter(b);
        }
    }
    public void addVultures(int num) {
        totals.put(GVcritter.Species.VULTURE, totals.get(GVcritter.Species.VULTURE) + num);
        //numVultures += num;
        for (int i = 0; i < num; i++)  {
            Location loc = getOpenLocation();
            Vulture v = new Vulture(loc);
            placeCritter(v);
        }
    }
    public void addHippos(int num) {
        totals.put(GVcritter.Species.HIPPO, totals.get(GVcritter.Species.HIPPO) + num);
        //numHippos += num;
        for (int i = 0; i < num; i++) {
            Location loc = getOpenLocation();
            Hippo h = new Hippo(loc);
            placeCritter(h);
        }
    }
    public void addMyCritters(int num) {
        totals.put(GVcritter.Species.MYCRITTER, totals.get(GVcritter.Species.MYCRITTER) + num);
        //numMyCritters += num;
        for (int i = 0; i < num; i++) {
            Location loc = getOpenLocation();
            MyCritter mc = new MyCritter(loc);
            placeCritter(mc);
        }
    }
    

/******************************************************
Move forward on step of the simulation
*****************************************************/  
    public void oneStep(){
        
        // shuffle the arraylist of critters for better performance
        Collections.shuffle(allCritters);
        steps++;
        
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
    
    private void fight (GVcritter attacker, GVcritter defender) {
        GVcritter winner = null;
        Location aLoc = attacker.getLocation();
        Location dLoc = defender.getLocation();
        
        GVcritter.Attack aAt = attacker.getAttack(defender);
        GVcritter.Attack dAt = defender.getAttack(attacker);
        if (aAt == dAt)
            winner = Math.random() < 0.5 ? attacker : defender;
        else if (aAt == GVcritter.Attack.FORFEIT) 
            winner = defender;
        else if (dAt == GVcritter.Attack.FORFEIT)
            winner = attacker;
        else if (aAt == GVcritter.Attack.POUNCE && dAt == GVcritter.Attack.ROAR)
            winner = attacker;
        else if (aAt == GVcritter.Attack.ROAR && dAt == GVcritter.Attack.SCRATCH)
            winner = attacker;
        else if (aAt == GVcritter.Attack.SCRATCH && dAt == GVcritter.Attack.POUNCE)
            winner = attacker;
        else 
            winner = defender;
        
        theWorld[dLoc.getRow()][dLoc.getCol()] = winner;
        theWorld[aLoc.getRow()][aLoc.getCol()] = null;
        critterDies(winner == defender ? attacker : defender);
        winner.setLocation(dLoc);
    }
    
    private void critterDies(GVcritter d) {
        for (int i = 0; i < allCritters.size(); i++) {
            if (allCritters.get(i) == d) {
                allCritters.remove(i);
                i = allCritters.size();
            }     
        }
        totals.put(d.getSpecies(), totals.get(d.getSpecies()) - 1);
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
        //return String.format("Steps:\t%1$d \nAnts:\t%2$d \nBirds:\t%3$d \nHippos:\t%4$d \nVultures:\t%5$d",
        //                    steps, numAnts, numBirds, numHippos, numVultures); 
        return String.format("Steps:\t%1$d \nAnts:\t%2$d \nBirds:\t%3$d \nHippos:\t%4$d \nVultures:\t%5$d \nMy Critters:\t%6$d",
                            steps, totals.get(GVcritter.Species.ANT), 
                            totals.get(GVcritter.Species.BIRD),
                            totals.get(GVcritter.Species.HIPPO), 
                            totals.get(GVcritter.Species.VULTURE),
                            totals.get(GVcritter.Species.MYCRITTER));                    
    }
    private Location getOpenLocation() {
        int x = (int)(Math.random() * COLUMNS);
        int y = (int)(Math.random() * ROWS);
        // NOTE: this will break if no open locations, 
        // and possibly run a long time if very few open
        while (theWorld[y][x] != null) {
            x = (int)(Math.random() * COLUMNS);
            y = (int)(Math.random() * ROWS);
        }
        return new Location(y, x);
    }
    private void placeCritter(GVcritter c) {
        allCritters.add(c);
        Location loc = c.getLocation();
        theWorld[loc.getRow()][loc.getCol()] = c;
    }
    private Location getRelativeLocation(Location loc, GVcritter.Direction d) {
        int y = loc.getRow();
        int x = loc.getCol();
        switch (d) {
            case NORTH: 
                if (y == 0)
                    y = ROWS -1;
                else
                    y--;
                break;
            case SOUTH:
                if (y == ROWS - 1)
                    y = 0;
                else 
                    y++;
                break;
            case EAST:
                if (x == COLUMNS - 1)
                    x = 0;
                else
                    x++;
                break;
            case WEST:
                if (x == 0)
                    x = COLUMNS - 1;
                else 
                    x--;
                break;
        }
        return new Location(y,x);
    }
    public void reset() {
        theWorld = new GVcritter[ROWS][COLUMNS];
        allCritters.clear();
        /*
        numAnts=0;
        numBirds=0;
        numHippos=0;
        numVultures=0;
        numMyCritters=0;
        */
        for (GVcritter.Species s : GVcritter.Species.values()) {
            totals.put(s, 0);
        }
        steps = 0;
    }

}
