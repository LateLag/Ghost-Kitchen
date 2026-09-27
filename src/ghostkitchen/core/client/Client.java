package ghostkitchen.core.client;

import java.util.List;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.core.ingredient.Ingredient;

public abstract class Client {
    private Image image;
    protected List<Class<? extends Ingredient>>[] preferences;

    @SafeVarargs
    protected Client(String image, List<Class<? extends Ingredient>> ... layers) {
        this.image = ImageUtils.loadImage("resources/images/clients/" + image);
        this.preferences = layers.clone();
    }

    public List<Class<? extends Ingredient>>[] getPreferences() { return preferences.clone(); }

    public void update(double delta) {

    }

    public void draw(EngineFrame e) {

    }

    @Override
    public String toString() { return "Placeholder"; }
}
