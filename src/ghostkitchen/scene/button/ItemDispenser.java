package ghostkitchen.scene.button;

import java.awt.Color;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import ghostkitchen.core.Grabbable;
import ghostkitchen.core.item.Coin;
import ghostkitchen.core.item.Item;
import ghostkitchen.scene.Restaurant;
import ghostkitchen.util.ItemUtils;

public class ItemDispenser extends Button {
    protected Class<? extends Item> output;
    private int available;
    private int initialAmount;

    public ItemDispenser(Restaurant restaurant, Class<? extends Item> item, double x, double y, double width, double height, int initialAmount) {
        super("dispenser.png", 14, x - width / 2, y - height / 2, x + width / 2, y + height / 2);
        this.output = item;
        this.available = initialAmount;
        this.initialAmount = initialAmount;

        setPressAction(() -> {
            if(available > 0) {
                available--;
                restaurant.setGrabbed(generate());
            }
        });

        setReleaseAction(() -> {
            Grabbable g = restaurant.getGrabbed();
            if(g != null) {
                int consecutiveMatches = g.consecutiveMatches(item, Coin.class);
                if(consecutiveMatches > 0) {
                    available += consecutiveMatches;
                    if(consecutiveMatches == g.size()) {
                        restaurant.getGrabbables().remove(restaurant.getGrabbed());
                        restaurant.setGrabbed(null);
                    }
                    else {
                        for(int i = 0; i < consecutiveMatches; i++) {
                            g.pop();
                        }
                    }
                }
            }
        });

        setHoverAction(() -> {
            Grabbable g = restaurant.getGrabbed();
            hovered = g == null && available > 0 || g != null && g.peek().getClass() == item;
        });
    }

    public ItemDispenser(Restaurant restaurant, Class<? extends Item> item, double x, double y, double width, double height) {
        this(restaurant, item, x, y, width, height, 1);
    }

    public ItemDispenser(Restaurant restaurant, Class<? extends Item> item, double x, double y, int initialAmount) {
        this(restaurant, item, x, y, 108.0, 108.0, initialAmount);
    }

    public ItemDispenser(Restaurant restaurant, Class<? extends Item> item, double x, double y) {
        this(restaurant, item, x, y, 1);
    }

    public Grabbable generate() {
        return new Grabbable(ItemUtils.instantiateItem(output), getCenterX(), getCenterY());
    }

    public void replenish() {
        available = initialAmount;
    }

    @Override
    public void draw(EngineFrame e) {
        super.draw(e);
        Item i = ItemUtils.instantiateItem(output);
        i.setScale(Math.max(aabb.width, aabb.height) / Math.max(i.getWidth(), i.getHeight()) * 1.75);
        i.setActiveAABB(false);
        boolean hoveredAndAvailable = hovered && available > 0;
        i.draw(e, getCenterX(), getCenterY(), hoveredAndAvailable ? 10.0 * e.getTime() : 0.0, hoveredAndAvailable ? 1.1 : 1.0, available > 0 ? null : Color.BLACK);
    }
}
