package ghostkitchen.core.ingredient;

public class Patty extends Ingredient {
    public Patty() {
        super("patty.png", 56, 26, -5, 9);
    }

    @Override
    public String toString() { return "Cooked Patty"; }
}
