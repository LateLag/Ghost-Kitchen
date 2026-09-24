package ghostkitchen.core.ingredient;

public class Plate extends Ingredient {
    public Plate() {
        super("plate.png", 100, 38, -10, 8);
    }

    @Override
    public String toString() { return "Plate"; }
}
