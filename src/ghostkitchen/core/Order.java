package ghostkitchen.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import ghostkitchen.core.client.Client;
import ghostkitchen.core.ingredient.Ingredient;

public class Order {
    Client client;
    List<Ingredient> dish;

    public Order(Client client) {
        this.client = client;
        this.dish = generateWantedDish();
    }

    private List<Ingredient> generateWantedDish() {
        List<Class<? extends Ingredient>>[] preferences = client.getPreferences();
        if(preferences == null) throw new IllegalStateException("Preferences not yet initialized!");

        List<Ingredient> dish = new ArrayList<>();

        //Fake grabbable for easy repositioning of added ingredients
        Grabbable g = new Grabbable();
        g.pop(); //No plate

        for(List<Class<? extends Ingredient>> layer : preferences) {
            Ingredient ingredient = generateRandomIngredient(layer);
            ingredient.setScale(1.0);
            ingredient.setActiveAABB(false);
            g.push(ingredient);
            dish.add(ingredient);
        }
        
        return dish;
    }

    private static Ingredient generateRandomIngredient(List<Class<? extends Ingredient>> possibleTypes) {
        //Pick a random possible ingredient from the list
        Class<? extends Ingredient> type = possibleTypes.get(ThreadLocalRandom.current().nextInt(possibleTypes.size()));
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to create ingredient", e);
        }
    }

    public void draw(EngineFrame e, double posX, double posY) {
        double halfDimY = (dish.getFirst().getAABB().y2 - dish.getLast().getAABB().y1) / 2.0;
        for(Ingredient i : dish) {
            i.draw(e, posX, posY + halfDimY);
        }
    }
}
