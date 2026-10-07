package ghostkitchen.scene.button;

import ghostkitchen.core.Grabbable;
import ghostkitchen.core.item.Item;
import ghostkitchen.scene.Restaurant;

public class ItemGenerator extends ItemDispenser {
    public ItemGenerator(Restaurant restaurant, Class<? extends Item> item, double x, double y, double width, double height) {
        super(restaurant, item, x, y, width, height);

        setPressAction(() -> {
            restaurant.setGrabbed(generate());
        });

        setReleaseAction(() -> {
            Grabbable g = restaurant.getGrabbed();
            if(g != null && g.isInstanceOf(item)) {
                restaurant.getGrabbables().remove(restaurant.getGrabbed());
                restaurant.setGrabbed(null);
            }
        });

        setHoverAction(() -> {
            Grabbable g = restaurant.getGrabbed();
            hovered = g == null || g.isInstanceOf(item);
        });
    }

    public ItemGenerator(Restaurant restaurant, Class<? extends Item> item, double x, double y) {
        this(restaurant, item, x, y, 120.0, 120.0);
    }
}
