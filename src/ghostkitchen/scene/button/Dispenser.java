package ghostkitchen.scene.button;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import ghostkitchen.Main;
import ghostkitchen.core.Grabbable;
import ghostkitchen.core.item.Item;
import ghostkitchen.util.ItemUtils;

public class Dispenser extends Button {
    Class<? extends Item> type;

    public Dispenser(Class<? extends Item> item, double x, double y) {
        super("dispenser.png", 4, x - 60.0, y - 60.0, x + 60.0, y + 60.0);
        this.type = item;
    }

    public Grabbable dispense() {
        return new Grabbable(ItemUtils.instantiateItem(type), getCenterX(), getCenterY());
    }

    @Override
    public Object onClick() {
        return dispense();
    }

    @Override
    public Object onRelease() {
        return null;
    }

    @Override
    public void draw(EngineFrame e) {
        super.draw(e);
        Item i = ItemUtils.instantiateItem(type);
        i.setScale(Math.min(aabb.width, aabb.height) / Math.max(i.getWidth(), i.getHeight()) * 1.75);
        i.setActiveAABB(false);
        i.draw(e, getCenterX(), getCenterY(), hovered ? 10.0 * e.getTime() : 0.0);

        if(Main.debug()) aabb.draw(e, EngineFrame.LIME);
    }
}
