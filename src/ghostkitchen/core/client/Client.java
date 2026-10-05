package ghostkitchen.core.client;

import java.util.List;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.Main;
import ghostkitchen.core.LayerPrefs;

public abstract class Client {
    private Image image;
    private List<LayerPrefs> layerPrefs;

    protected Client(String image, LayerPrefs ... layers) {
        this.image = ImageUtils.loadImage("resources/images/clients/" + image);
        this.layerPrefs = List.of(layers).reversed();
    }

    public List<LayerPrefs> getLayerPrefs() { return List.copyOf(layerPrefs); }

    public void update(double delta) {

    }

    public void draw(EngineFrame e, double posX, double baseY) {
        double width = image.getWidth();
        double height = image.getHeight();
        
        double scale = 2.4;
        double rotation = 0.0;

        //Scaled and centered
        Rectangle src = new Rectangle(0.0, 0.0, width, height);
        Rectangle dest = new Rectangle(posX - width / 2.0 * scale, baseY - height * scale, width * scale, height * scale);

        image.draw(e, src, dest, width / 2.0 * scale, height / 2.0 * scale, rotation);

        if(Main.debug()) e.drawRectangle(dest, EngineFrame.MAGENTA);
    }

    @Override
    public String toString() { return "Placeholder"; }
}
