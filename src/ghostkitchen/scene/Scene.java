package ghostkitchen.scene;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;

public abstract class Scene {
    protected EngineFrame engine;

    public abstract void update(double delta);
    public abstract void draw();
}
