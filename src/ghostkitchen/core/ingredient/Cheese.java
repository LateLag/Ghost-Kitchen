package ghostkitchen.core.ingredient;

public class Cheese extends Ingredient {
    public Cheese() {
        super("cheese.png", 50, 16, -1, 6);
    }

    @Override
    public String toString() { return "Cheese"; }
}
