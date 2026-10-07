package ghostkitchen.core.item;

public class Coin extends Ingredient {
    public Coin() {
        super("coin.png", 24, 12, -4, 8);
        this.mixable = false;
    }

    @Override
    public String toString() { return "Coin"; }
}
