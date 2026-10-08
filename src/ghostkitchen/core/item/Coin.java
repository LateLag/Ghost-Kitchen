package ghostkitchen.core.item;

public class Coin extends Item {
    public Coin() {
        super("coin.png", 24, 12, -4, 8, false);
    }

    @Override
    public String toString() { return "Coin"; }
}
