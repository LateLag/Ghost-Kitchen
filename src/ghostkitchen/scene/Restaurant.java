package ghostkitchen.scene;

import java.util.LinkedList;
import java.util.List;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import ghostkitchen.core.Grabbable;
import ghostkitchen.core.ingredient.Bun;
import ghostkitchen.core.ingredient.BunBottom;
import ghostkitchen.core.ingredient.BunTop;
import ghostkitchen.core.ingredient.Cheese;
import ghostkitchen.core.ingredient.Cone;
import ghostkitchen.core.ingredient.IceCreamStrawberry;
import ghostkitchen.core.ingredient.Ingredient;
import ghostkitchen.core.ingredient.Patty;
import ghostkitchen.core.ingredient.Plate;

public class Restaurant extends Scene {
    private List<Grabbable> grabbables;
    private Grabbable grabbed;

    public Restaurant(EngineFrame e) {
        this.engine = e;
        this.grabbables = new LinkedList<>();
    }

    public void spawnIngredient(Ingredient ingredient, double posX, double posY) {
        Grabbable g = new Grabbable(ingredient);
        g.setPos(posX, posY);
        grabbables.add(g);
    }

    public void update(double delta) {
        double midX = engine.getScreenWidth() / 2;
        double midY = engine.getScreenHeight() / 2;
        if(engine.isKeyPressed(EngineFrame.KEY_ONE)) spawnIngredient(new Plate(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_TWO)) spawnIngredient(new Bun(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_THREE)) spawnIngredient(new BunBottom(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_FOUR)) spawnIngredient(new BunTop(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_FIVE)) spawnIngredient(new Patty(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_SIX)) spawnIngredient(new Cheese(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_SEVEN)) spawnIngredient(new Cone(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_EIGHT)) spawnIngredient(new IceCreamStrawberry(), midX, midY);

        boolean attemptGrab = false;
        //Update logic for the grabbed grabbable
        if(grabbed != null) {
            grabbed.setPos(engine.getMouseX(), engine.getMouseY());
            if(engine.isMouseButtonReleased(EngineFrame.MOUSE_BUTTON_LEFT)) {
                grabbed.drop();
                grabbed = null;
            }
        }
        else if(engine.isMouseButtonPressed(EngineFrame.MOUSE_BUTTON_LEFT)) attemptGrab = true;

        for(int i = grabbables.size() - 1; i >= 0; i--) {
            Grabbable g = grabbables.get(i);
            g.update(delta);

            //Grab logic
            if(attemptGrab && grabbed == null) {
                grabbed = g.grab(engine.getMouseX(), engine.getMouseY());

                //Move the current grabbable to the top
                if(grabbed == g) grabbables.add(grabbables.remove(i));
                //If the grabbed grabbable is not the current one and is not null
                else if(grabbed != null) grabbables.add(grabbed);
            }

            //Merge logic
            if(g != grabbed) {
                for(int j = i - 1; j >= 0; j--) {
                    boolean merged = g.merge(grabbables.get(j));
                    if(merged) {
                        grabbables.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public void draw() {
        for(Grabbable g : grabbables) {
            g.draw(engine);
        }
    }
}
