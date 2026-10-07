package ghostkitchen.core.item;

import ghostkitchen.core.Grabbable;

public abstract class Ingredient extends Item {
    protected Ingredient(String image, int width, int height, int baseline, int sandwichHeight) {
        super(image, width, height, baseline, sandwichHeight);
        this.mixable = true;
    }

    @Override
    public boolean hoverInteraction(Grabbable g) {
        return false;
    }

    @Override
    public boolean idleInteraction(Grabbable g) {
        Item i = g.peek();
        if(i.mixable && mixable || i.getClass() == getClass()) {
            g.push(this);
            return true;
        }
        return false;
    }
}
