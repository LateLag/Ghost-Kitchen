package ghostkitchen.core.item;

public class IceCreamStrawberry extends Item {
    public IceCreamStrawberry() {
        super("icecream_strawberry.png", 42, 40, -14, 28, true);
    }

    @Override
    public String toString() { return "Strawberry Ice Cream"; }
}
