package ghostkitchen.core.item;

public class LettuceChopped extends Item {
    public LettuceChopped() {
        super("lettuce_chopped.png", 50, 16, -4, 6, true);
    }

    @Override
    public String toString() { return "Chopped Lettuce"; }
}
