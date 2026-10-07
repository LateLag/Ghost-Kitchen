package ghostkitchen.core.item;

import ghostkitchen.core.Grabbable;

public class Plate extends Ingredient {
    public Plate() {
        super("plate.png", 100, 38, -8, 6);
    }

    @Override
    public boolean idleInteraction(Grabbable g) {
        if(super.idleInteraction(g)) {
            g.setAsPotentialMainDish();
            return true;
        }
        return false;
    }

    @Override
    public String toString() { return "Plate"; }
}
