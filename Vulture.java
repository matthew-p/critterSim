
/**
 * @author Matthew Pische 
 */
import java.awt.*;
public class Vulture extends GVcritter
{
    private int cycleStart;
    private Direction dir; 
    public Vulture (Location loc) {
        super(loc);
        setColor(Color.BLACK);
        setSpecies(Species.VULTURE);
        cycleStart = (int)(Math.random() * 14 + 1);
    }
    public Attack getAttack (GVcritter opponent) {
        Attack rtn = Attack.ROAR;
        if (opponent.getSpecies() == Species.HIPPO)
            rtn = Attack.SCRATCH;
        return rtn;
        // return opponent == HIPPO ? Attack.SCRATCH : Attack.ROAR;
    }
    public Direction getMoveDirection() {
        steps++;
        // not clear from project spec if Vulture movement pattern 
        // is supposed to be different from Bird pattern, 
        // identical here
        if ((steps + cycleStart) % 14 < 4)
            dir = Direction.NORTH;
        else if ((steps + cycleStart) % 14 < 7)
            dir = Direction.WEST;
        else if ((steps + cycleStart) % 14 < 11)
            dir = Direction.SOUTH;
        else 
            dir = Direction.EAST;
        return dir;
    }
}
