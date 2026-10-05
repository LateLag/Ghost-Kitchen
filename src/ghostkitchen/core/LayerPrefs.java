package ghostkitchen.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ghostkitchen.core.item.Ingredient;
import ghostkitchen.util.ItemUtils;

public record LayerPrefs(List<Class<? extends Ingredient>> ingredients) {
    public LayerPrefs {
        ingredients = ingredients == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<>(ingredients));
    }

    @SafeVarargs
    public LayerPrefs(Class<? extends Ingredient> ... ingredients) {
        this(ingredients == null ? null : Arrays.asList(ingredients));
    }

    public Ingredient pickRandom() {
        if(ingredients.isEmpty()) return null;

        //Pick a random possible ingredient from the list
        Class<? extends Ingredient> type = ingredients.get(ThreadLocalRandom.current().nextInt(ingredients.size()));
        if(type == null) return null;

        return (Ingredient)ItemUtils.instantiateItem(type);
    }
}
