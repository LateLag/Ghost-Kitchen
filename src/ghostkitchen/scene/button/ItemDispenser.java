package ghostkitchen.scene.button;

import java.awt.Color;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import ghostkitchen.core.Grabbable;
import ghostkitchen.core.item.Item;
import ghostkitchen.scene.Restaurant;
import ghostkitchen.util.ItemUtils;

public class ItemDispenser extends Button {
    protected Class<? extends Item> output;
    private int available;
    private int max;

    ItemDispenser(Restaurant restaurant, Class<? extends Item> item, double x, double y, double width, double height, int maxAmount) {
        super("dispenser.png", 14, x - width / 2, y - height / 2, x + width / 2, y + height / 2);
        this.output = item;
        this.available = maxAmount;
        this.max = maxAmount;

        setPressAction(() -> {
            if(available > 0) {
                available--;
                restaurant.setGrabbed(generate());
            }
        });

        setReleaseAction(() -> {
            Grabbable g = restaurant.getGrabbed();
            if(g != null && g.isInstanceOf(item)) {
                restaurant.getGrabbables().remove(restaurant.getGrabbed());
                restaurant.setGrabbed(null);
                available++;
            }
        });

        setHoverAction(() -> {
            Grabbable g = restaurant.getGrabbed();
            hovered = g == null && available > 0 || g != null && g.isInstanceOf(item);
        });
    }

    public ItemDispenser(Restaurant restaurant, Class<? extends Item> item, double x, double y, double width, double height) {
        this(restaurant, item, x, y, width, height, 1);
    }

    public ItemDispenser(Restaurant restaurant, Class<? extends Item> item, double x, double y, int maxAmount) {
        this(restaurant, item, x, y, 120.0, 120.0, maxAmount);
    }

    public ItemDispenser(Restaurant restaurant, Class<? extends Item> item, double x, double y) {
        this(restaurant, item, x, y, 1);
    }

    public Grabbable generate() {
        return new Grabbable(ItemUtils.instantiateItem(output), getCenterX(), getCenterY());
    }

    public void replenish() {
        available = max;
    }

    @Override
    public void draw(EngineFrame e) {
        super.draw(e);
        Item i = ItemUtils.instantiateItem(output);
        i.setScale(Math.max(aabb.width, aabb.height) / Math.max(i.getWidth(), i.getHeight()) * 1.75);
        i.setActiveAABB(false);
        i.draw(e, getCenterX(), getCenterY(), hovered && available > 0 ? 10.0 * e.getTime() : 0.0, available > 0 ? null : Color.BLACK);
    }
}
