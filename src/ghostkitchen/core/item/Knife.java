package ghostkitchen.core.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ghostkitchen.core.Grabbable;
import ghostkitchen.util.ItemUtils;

public class Knife extends Item {
    private Map<Class<? extends Ingredient>, List<Class<? extends Ingredient>>> transformer;

    public Knife() {
        super("knife.png", 120, 12, -8);
        this.transformer = new HashMap<>();
        addTransform(Bun.class, List.of(BunBottom.class, BunTop.class));
    }

    public void addTransform(Class<? extends Ingredient> source, List<Class<? extends Ingredient>> target) {
        transformer.put(source, target);
    }

    @Override
    public boolean hoverInteraction(Grabbable g) {
        Item top = g.peek();
        List<Class<? extends Ingredient>> target = transformer.get(top.getClass());
        if(target != null) {
            double x = g.getPosX();
            double y = g.getPosY() - g.size();
            g.pop();
            for(Class<? extends Ingredient> c : target) {
                g.push(ItemUtils.instantiateItem(c));
            }
            g.setPos(x, y);
        }
        return false;
    }

    @Override
    public boolean idleInteraction(Grabbable g) {
        return false;
    }

    @Override
    public String toString() { return "Cheese"; }
}