package ghostkitchen.core.item;

import ghostkitchen.core.Grabbable;

public abstract class Ingredient extends Item {
    protected Ingredient(String image, int width, int height, int baseline, int sandwichHeight) {
        super(image, width, height, baseline, sandwichHeight);
    }

    @Override
    public boolean hoverInteraction(Grabbable g) {
        return false;
    }

    @Override
    public boolean idleInteraction(Grabbable g) {
        if(g.peek().getSandwichHeight() != 0.0) {
            g.push(this);
            return true;
        }
        return false;
    }
}
