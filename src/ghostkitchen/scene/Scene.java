package ghostkitchen.scene;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;

public abstract class Scene {
    protected EngineFrame engine;
    protected Image background;

    public Scene(EngineFrame e, String image) {
        this.engine = e;
        //this.background = ImageUtils.loadImage("resources/images/background/" + image);
    }

    public abstract void update(double delta);
    public abstract void draw();
}
