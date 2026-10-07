package ghostkitchen.util;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;

public class PanelUtils {
    private static final double scale = 2.4;

    private PanelUtils() {}

    public static void drawNineSlice(EngineFrame e, Image image, Rectangle dest, int borderSize) {
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        double scaledSize = scale * borderSize;
        Rectangle src = new Rectangle();
        Rectangle dst = new Rectangle();

        //Center
        src.x = borderSize;
        src.y = borderSize;
        src.width = imgWidth - borderSize * 2;
        src.height = imgHeight - borderSize * 2;
        dst.x = dest.x + scaledSize;
        dst.y = dest.y + scaledSize;
        dst.width = dest.width - scaledSize * 2;
        dst.height = dest.height - scaledSize * 2;
        image.draw(e, src, dst);

        if(borderSize != 0) {
            //Top stroke
            src.x = borderSize;
            src.y = 0.0;
            src.width = imgWidth - borderSize * 2;
            src.height = borderSize;
            dst.x = dest.x + scaledSize;
            dst.y = dest.y;
            dst.width = dest.width - scaledSize * 2;
            dst.height = scaledSize;
            image.draw(e, src, dst);

            //Left stroke
            src.x = 0.0;
            src.y = borderSize;
            src.width = borderSize;
            src.height = imgHeight - borderSize * 2;
            dst.x = dest.x;
            dst.y = dest.y + scaledSize;
            dst.width = scaledSize;
            dst.height = dest.height - scaledSize * 2;
            image.draw(e, src, dst);

            //Right stroke
            src.x = imgWidth - borderSize;
            src.y = borderSize;
            src.width = borderSize;
            src.height = imgHeight - borderSize * 2;
            dst.x = dest.x + dest.width - scaledSize;
            dst.y = dest.y + scaledSize;
            dst.width = scaledSize;
            dst.height = dest.height - scaledSize * 2;
            image.draw(e, src, dst);

            //Bottom stroke
            src.x = borderSize;
            src.y = imgHeight - borderSize;
            src.width = imgWidth - borderSize * 2;
            src.height = borderSize;
            dst.x = dest.x + scaledSize;
            dst.y = dest.y + dest.height - scaledSize;
            dst.width = dest.width - scaledSize * 2;
            dst.height = scaledSize;
            image.draw(e, src, dst);

            //Top left corner
            src.x = 0.0;
            src.y = 0.0;
            src.width = borderSize;
            src.height = borderSize;
            dst.x = dest.x;
            dst.y = dest.y;
            dst.width = scaledSize;
            dst.height = scaledSize;
            image.draw(e, src, dst);
            
            //Top right corner
            src.x = imgWidth - borderSize;
            src.y = 0.0;
            src.width = borderSize;
            src.height = borderSize;
            dst.x = dest.x + dest.width - scaledSize;
            dst.y = dest.y;
            dst.width = scaledSize;
            dst.height = scaledSize;
            image.draw(e, src, dst);

            //Bottom left corner
            src.x = 0.0;
            src.y = imgHeight - borderSize;
            src.width = borderSize;
            src.height = borderSize;
            dst.x = dest.x;
            dst.y = dest.y + dest.height - scaledSize;
            dst.width = scaledSize;
            dst.height = scaledSize;
            image.draw(e, src, dst);

            //Bottom right corner
            src.x = imgWidth - borderSize;
            src.y = imgHeight - borderSize;
            src.width = borderSize;
            src.height = borderSize;
            dst.x = dest.x + dest.width - scaledSize;
            dst.y = dest.y + dest.height - scaledSize;
            dst.width = scaledSize;
            dst.height = scaledSize;
            image.draw(e, src, dst);
        }
    }
}
