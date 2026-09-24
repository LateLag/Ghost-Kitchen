package ghostkitchen.core.ingredient;

public class Plate extends Ingredient {
    public Plate() {
        super("plate.png", 100, 38, -8, 6);
    }

    @Override
    public String toString() { return "Plate"; }
}
