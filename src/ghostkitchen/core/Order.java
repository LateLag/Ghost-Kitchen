package ghostkitchen.core;

import java.util.ArrayList;
import java.util.List;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.core.client.Client;
import ghostkitchen.core.item.Item;
import ghostkitchen.util.PanelUtils;

public class Order {
    private static final double ingredientScale = 1.0;
    private static final Image check = ImageUtils.loadImage("resources/images/scene/misc/checkmark.png");
    private static final Image cross = ImageUtils.loadImage("resources/images/scene/misc/cross.png");
    private static final Image question = ImageUtils.loadImage("resources/images/scene/misc/questionmark.png");

    private static int currentId = 1;

    private int id;
    private Client client;
    private List<Item> dish;
    private List<Boolean> validation;
    private Image bg;

    public Order(Client client) {
        this.id = currentId++;
        this.client = client;
        this.dish = generateWantedDish();
        this.validation = new ArrayList<>(dish.size());
        this.bg = ImageUtils.loadImage("resources/images/scene/nineslice/order.png");
    }

    public Client getClient() { return client; }

    private List<Item> generateWantedDish() {
        List<LayerPrefs> preferences = client.getLayerPrefs();
        if(preferences == null) throw new IllegalStateException("Preferences not yet initialized!");

        List<Item> dish = new ArrayList<>();

        //Fake grabbable for easy repositioning of added ingredients
        Grabbable g = new Grabbable();
        g.pop(); //No plate

        //Pick random ingredient for each layer from top to bottom
        for(LayerPrefs layer : preferences) {
            Item item = layer.pickRandom();
            if(item != null) {
                item.setScale(ingredientScale);
                item.setActiveAABB(false);
                g.push(item);
                dish.add(item);
            }
        }
        
        return dish;
    }

    public boolean validateNext(Item i) {
        if(i == null) return false;

        if(validation.size() <= dish.size()) {
            int index = dish.size() - 1 - validation.size();
            if(i == null || index < 0) validation.add(false);
            else validation.add(dish.get(index).getClass() == i.getClass());
            return validation.getLast();
        }
        return false;
    }

    public int getValidityScore() {
        int score = 0;
        for(Boolean b : validation) {
            score += b ? 1 : -1;
        }
        return score;
    }

    public void draw(EngineFrame e, double posX, double posY) {
        Item top = dish.getLast();
        double width = top.getCollisionWidth();
        double height = dish.getFirst().getAABB().y2 - top.getAABB().y1;
        double offset = -top.getLocalPosY();
        double padding = 48.0;

        PanelUtils.drawNineSlice(e, bg, new Rectangle(posX - width / 2 - padding, posY - padding, width + padding * 2, height + padding * 2), 5);

        for(Item i : dish) {
            i.draw(e, posX, posY + offset + padding / 2);
        }

        int dishSize = dish.size();
        int length = Math.min(validation.size(), dishSize + 1); // Allow one additional check to be drawn to represent all extras
        for(int i = 0; i < length; i++) {
            Image img = i == dishSize ? question : validation.get(i) ? check : cross;
            int ingredientIndex = dishSize - 1 - i;
            double validationOffset = ingredientIndex < 0 ? (dish.getFirst().getHeight() + padding / 2) / 2 : dish.get(ingredientIndex).getLocalPosY();

            double scaledWidth = img.getWidth() * ingredientScale;
            double scaledHeight = img.getHeight() * ingredientScale;
            Rectangle src = new Rectangle(0.0, 0.0, img.getWidth(), img.getHeight());
            Rectangle dest = new Rectangle(
                posX + (padding - scaledWidth) / 2,
                posY + (padding - scaledHeight) / 2 + offset + validationOffset,
                scaledWidth,
                scaledHeight
            );
            img.draw(e, src, dest);
        }
    }

    @Override
    public String toString() {
        return "Order #" + Integer.toString(id);
    }
}