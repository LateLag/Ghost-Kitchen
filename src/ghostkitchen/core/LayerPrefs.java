package ghostkitchen.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ghostkitchen.core.item.Item;
import ghostkitchen.util.ItemUtils;

public record LayerPrefs(List<Class<? extends Item>> items) {
    public LayerPrefs {
        items = items == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(items));
    }

    @SafeVarargs
    public LayerPrefs(Class<? extends Item> ... items) {
        this(items == null ? null : Arrays.asList(items));
    }

    public Item pickRandom() {
        if(items.isEmpty()) return null;

        //Pick a random possible ingredient from the list
        Class<? extends Item> type = items.get(ThreadLocalRandom.current().nextInt(items.size()));
        if(type == null) return null;

        return ItemUtils.instantiateItem(type);
    }
}
