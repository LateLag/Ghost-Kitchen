package ghostkitchen.util;

import ghostkitchen.core.item.Item;

public class ItemUtils {
    private ItemUtils(){}

    public static Item instantiateItem(Class<? extends Item> c) {
        if(c == null) return null;
        try {
            return c.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create item: " + c.getSimpleName(), e);
        }
    }
}
