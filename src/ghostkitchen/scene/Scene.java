package ghostkitchen.scene;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.Main;

public abstract class Scene {
    protected static final double scale = 2.4;

    protected EngineFrame engine;
    protected Image background;
    protected Image foreground;

    public Scene(EngineFrame e, String bg, String fg) {
        this.engine = e;
        if(bg != null) this.background = ImageUtils.loadImage("resources/images/scene/" + bg);
        if(fg != null) this.foreground = ImageUtils.loadImage("resources/images/scene/" + fg);
    }

    public abstract void update(double delta);

    public void draw() {
        Rectangle dest = new Rectangle(0.0, 0.0, engine.getScreenWidth(), engine.getScreenHeight());

        if(background != null) {
            Rectangle src = new Rectangle(0.0, 0.0, background.getWidth(), background.getHeight());
            background.draw(engine, src, dest);
        }
        drawBefore();
        if(foreground != null) {
            Rectangle src = new Rectangle(0.0, 0.0, foreground.getWidth(), foreground.getHeight());
            foreground.draw(engine, src, dest);
        }
        drawAfter();
    }

    public void drawBefore() {}
    public void drawAfter() {
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
