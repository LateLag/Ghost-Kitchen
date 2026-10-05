package ghostkitchen.core;

import java.util.ArrayList;
import java.util.List;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import ghostkitchen.core.client.Client;
import ghostkitchen.core.item.Ingredient;

public class Order {
    Client client;
    List<Ingredient> dish;

    public Order(Client client) {
        this.client = client;
        this.dish = generateWantedDish();
    }

    private List<Ingredient> generateWantedDish() {
        List<LayerPrefs> preferences = client.getLayerPrefs();
        if(preferences == null) throw new IllegalStateException("Preferences not yet initialized!");

        List<Ingredient> dish = new ArrayList<>();

        //Fake grabbable for easy repositioning of added ingredients
        Grabbable g = new Grabbable();
        g.pop(); //No plate

        //Pick random ingredient for each layer from top to bottom
        for(LayerPrefs layer : preferences) {
            Ingredient ingredient = layer.pickRandom();
            if(ingredient != null) {
                ingredient.setScale(1.0);
                ingredient.setActiveAABB(false);
                g.push(ingredient);
                dish.add(ingredient);
            }
        }
        
        return dish;
    }

    public void draw(EngineFrame e, double posX, double posY) {
        double halfDimY = (dish.getFirst().getAABB().y2 - dish.getLast().getAABB().y1) / 2.0;
        for(Ingredient i : dish) {
            i.draw(e, posX, posY + halfDimY);
        }
    }
}
