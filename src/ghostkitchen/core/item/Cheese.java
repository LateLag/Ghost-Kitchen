package ghostkitchen.core.item;

public class Cheese extends Item {
    public Cheese() {
        super("cheese.png", 50, 16, -1, 6, true);
    }

    @Override
    public String toString() { return "Cheese"; }
}
