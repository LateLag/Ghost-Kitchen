package ghostkitchen.core.item;

public class CreamPastry extends Item {
    public CreamPastry() {
        super("cream_pastry.png", 48, 48, -20, 20, true);
    }

    @Override
    public String toString() { return "Pastry Cream"; }
}
