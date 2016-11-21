
/**
 * @author Matthew Pische
 */
public class Bird extends GVcritter
{
    private int cycleStart;
    public Bird (Location loc) {
        // call the parent class constructor w/ the given param 
        super(loc);
        setColor(Color.BLUE);
        setSpecies(Species.BIRD);
        cycleStart = Math.random() * 14 + 1;
    }
    public Attack getAttack (GVcritter opponent) {
        return Attack.ROAR;
    }
    public Direction getMoveDirection() {
        steps++;
        if ((steps + cycleStart) % 14 < 3)
            dir = Direction.NORTH;
        else if ((steps + cycleStart) % 14 < 7)
            dir = Direction.EAST;
        else if ((steps + cycleStart) % 14 < 10)
            dir = Direction.SOUTH;
        else 
            dir = Direction.WEST;
    }
}
