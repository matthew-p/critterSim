
/**
 * @author (your name) 
 */
import java.awt.*;
public class MyCritter extends GVcritter
{
    private Direction dir;
    private int cycleStart;
    public MyCritter (Location loc) {
        super(loc);
        setColor(Color.GREEN);
        setSpecies(Species.MYCRITTER);
        cycleStart = (int)(Math.random() * 12 + 1);
    }
    public Attack getAttack(GVcritter opponent) {
        Attack rtn = Attack.POUNCE;
        if (steps % 3 == 0)
            rtn = Attack.ROAR;
        return rtn;
    }
    public Direction getMoveDirection() {
        steps++;
        if (steps % 48 == 0)
            dir = Direction.random();
        else {
            if (steps % 12 < 3)
                dir = Direction.EAST;
            else if (steps % 12 < 6)
                dir = Direction.SOUTH;
            else if (steps % 12 < 9)
                dir = Direction.WEST;
            else
                dir = Direction.NORTH;
        }
        return dir;
    }
}
