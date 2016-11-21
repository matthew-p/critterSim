import java.awt.*; 
/***********************************************************
GVcritter represents a generic critter with several
characteristics: location, species, color and the number
of steps taken.  All other critters in the simulation
extend this class and add a few methods.

@author Scott Grissom
@version August 2016
***********************************************************/
public abstract class GVcritter {

	/** critter location */
	protected Location myLocation;

	/** critter color */
	private Color myColor;

	/** critter species */
	private Species mySpecies;

	/** number of steps taken during the simulation */
	protected int steps;

/***********************************************************
These enubmerated types are used throughout the simulation
classes.
***********************************************************/ 
	public static enum Direction {
		NORTH, SOUTH, EAST, WEST
	};

	public static enum Attack {
		ROAR, POUNCE, SCRATCH, FORFEIT
	};

	public static enum Species {
		NONE, ANT, BIRD, HIPPO, VULTURE, LAKER
	};
	
/***********************************************************
These abstract methods MUST BE IMPLEMENTED by all classes
that extend GVcritter.
***********************************************************/   
	public abstract Attack getAttack(GVcritter opponent);
	public abstract Direction getMoveDirection();
	
/***********************************************************
Instantiate and initialize the instance variables.

@param loc location of the critter
***********************************************************/     
	public GVcritter(Location loc){
		myLocation = loc;
		myColor = Color.WHITE;
		mySpecies = Species.NONE;
		steps = 0;
	}

/***********************************************************
Returns the critter species
@returns the species
***********************************************************/ 	   
	public final Species getSpecies(){
		return mySpecies;
	}
	   
/***********************************************************
Sets the critter species
@param s the species
***********************************************************/ 	   
	public final void setSpecies(Species s){
		mySpecies = s;
	}	
	   
/***********************************************************
Returns the critter color
@returns the color
***********************************************************/ 	   
	public final Color getColor(){
		return myColor;
	}
	   
/***********************************************************
Sets the critter color
@param c the color
***********************************************************/ 	   
	public final void setColor(Color c){
		myColor = c;
	}   

/***********************************************************
Sets the critter location
@param loc the location
***********************************************************/ 	   
	public final void setLocation(Location loc){
		myLocation = loc;
	}
	      
/***********************************************************
Returns the critter location
@returns the location
***********************************************************/ 	   
	public final Location getLocation(){
		return myLocation;
	}
}
