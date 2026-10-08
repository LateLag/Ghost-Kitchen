package ghostkitchen.core.item;

public class IceCreamChocolate extends Item {
    public IceCreamChocolate() {
        super("icecream_chocolate.png", 42, 40, -14, 28, true);
    }

    @Override
    public String toString() { return "Chocolate Ice Cream"; }
}
