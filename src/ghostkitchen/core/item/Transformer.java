package ghostkitchen.core.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ghostkitchen.core.Grabbable;
import ghostkitchen.util.ItemUtils;

public abstract class Transformer extends Item {
    private Map<Class<? extends Item>, List<Class<? extends Item>>> transformations;

    protected Transformer(String image, int width, int height, int baseline, int sandwichHeight, boolean mixable) {
        super(image, width, height, baseline, sandwichHeight, mixable);
        this.transformations = new HashMap<>();
    }

    protected void addTransform(Class<? extends Item> source, List<Class<? extends Item>> target) {
        transformations.put(source, target);
    }

    @Override
    public boolean hoverInteraction(Grabbable g) {
        List<Class<? extends Item>> target = transformations.get(g.peek().getClass());
        if(target != null) {
            double x = g.getPosX();
            double y = g.getPosY() - g.size();
            g.pop();
            for(Class<? extends Item> c : target) {
                g.push(ItemUtils.instantiateItem(c));
            }
            g.setPos(x, y);
        }
        return false;
    }
}