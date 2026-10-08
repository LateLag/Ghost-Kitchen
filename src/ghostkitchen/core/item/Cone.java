package ghostkitchen.core.item;

public class Cone extends Item {
    public Cone() {
        super("cone.png", 28, 60, -30, 52, true);
    }

    @Override
    public String toString() { return "Ice Cream Cone"; }
}
