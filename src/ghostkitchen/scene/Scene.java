package ghostkitchen.scene;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import ghostkitchen.Main;
import ghostkitchen.core.ImageCache;

public abstract class Scene implements ImageCache {
    protected static final double scale = 2.4;

    protected EngineFrame engine;

    public Scene(EngineFrame e) {
        this.engine = e;
    }

    public void setup() {}

    public EngineFrame getEngineFrame() { return engine; }

    public void update(double delta) {}

    public void draw() {
        if(Main.debug()) {
            String text = "DEBUG";
            int size = 40;
            Rectangle r = engine.measureTextBounds(text, size);
            r.width += (double)size / 2;
            double padding = (double)size / 4;
            engine.fillRectangle(r, EngineFrame.BLACK);
            engine.drawText(text, padding, padding, size, EngineFrame.WHITE);
        }
    }
}
