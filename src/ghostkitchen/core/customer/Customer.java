package ghostkitchen.core.customer;

import java.util.List;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import ghostkitchen.Main;
import ghostkitchen.core.LayerPrefs;
import ghostkitchen.core.ImageCache;

public abstract class Customer implements ImageCache {
    private static final int maxAnger = 3;

    protected Image image;
    private List<LayerPrefs> layerPrefs;
    private int mouthY;
    private int anger;

    protected Customer(String image, int mouthY, LayerPrefs ... layers) {
        this.image = getImage("clients/" + image);
        this.layerPrefs = List.of(layers).reversed();
        this.mouthY = mouthY;
        this.anger = 0;
    }

    public List<LayerPrefs> getLayerPrefs() { return List.copyOf(layerPrefs); }
    public double getMouthY() { return 2.4 * mouthY; }
    public int getAnger() { return anger; }

    public void increaseAnger() { anger = Math.max(anger + 1, maxAnger); }

    public void resetAnger() { anger = 0; }

    public void update(double delta) {

    }

    public void draw(EngineFrame e, double posX, double baseY) {
        double width = image.getWidth();
        double height = image.getHeight();
        
        double scale = 2.4;

        //Scaled and centered
        Rectangle src = new Rectangle(0.0, 0.0, width, height);
        Rectangle dest = new Rectangle(posX - width / 2.0 * scale, baseY - height * scale, width * scale, height * scale);

        image.draw(e, src, dest);

        if(Main.debug()) {
            e.drawRectangle(dest, EngineFrame.MAGENTA);
            double offsetY = dest.y + dest.height - mouthY;
            e.drawLine(dest.x, offsetY, dest.x + dest.width, offsetY, EngineFrame.MAGENTA);
        }
    }
}
