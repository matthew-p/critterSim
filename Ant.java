import java.awt.*;
/******************************************************
The Ant class extends GVcritter.  This gives us access
to all public or protected members in GVcriter.

@author Scott Grissom
@version August 2016
******************************************************/
public class Ant extends GVcritter{

    // start direction for this Ant: south or north
    private Direction dir;    

/*****************************************************
Create starting values for this Ant.
@param loc given location for this critter
*****************************************************/  
    public Ant(Location loc){
        super(loc); 
        setColor(Color.RED);
        setSpecies(Species.ANT);
        
        // pick a random start direction (north or south)
        if(Math.random() < 0.5)
            dir = Direction.SOUTH;
        else
            dir = Direction.NORTH;
    }

/*****************************************************
Ants always SCRATCH their opponents

@param opponent who is the critter fighting?
@return attack strategy
*****************************************************/     
    public Attack getAttack(GVcritter opponent) {
        return Attack.SCRATCH;
    }

/*****************************************************
Ants move in a southeast or norteast direction

@return desired direction of next step
*****************************************************/     
    public Direction getMoveDirection(){
        
        // increment the steps for this Ant
        steps++;
        
        // alternate directions every other step
        if(steps%2 == 0)
            return dir;
        else    
            return Direction.EAST;
    }
}