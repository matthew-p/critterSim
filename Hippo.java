
/**
 * @author Matthew Pische 
 */
import java.awt.*;
public class Hippo extends GVcritter
{
    // NOTE: project spec calls for instantiating a Random here, to make random directions
    // I didn't like that implimentation so I added a random method to the direction enum instead
    private int oldAge;
    private Direction dir;
    public Hippo (Location loc) {
        super(loc);
        oldAge = (int)(Math.random() * 200 + 300);
        setColor(Color.GRAY);
        setSpecies(Species.HIPPO);
        dir = Direction.random();
    }
    public Attack getAttack(GVcritter opponent) {
        Attack rtn = Attack.POUNCE;
        if (steps >= oldAge)
            rtn = Attack.FORFEIT;
        return rtn;
    }
    public Direction getMoveDirection() {
        steps++;
        if (steps % 5 == 0)
            dir = Direction.random();
        return dir;
    }
}
