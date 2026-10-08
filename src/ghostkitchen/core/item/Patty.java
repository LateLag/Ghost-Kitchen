package ghostkitchen.core.item;

public class Patty extends Item {
    public Patty() {
        super("patty.png", 56, 26, -5, 9, true);
    }

    @Override
    public String toString() { return "Cooked Patty"; }
}
