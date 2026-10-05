package ghostkitchen.scene.button;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;

public abstract class Button {
    private static final double scale = 2.4;

    private Image image;
    private int strokeSize;
    protected AABB aabb;
    protected boolean hovered;

    public Button(String image, int strokeSize, double x1, double y1, double x2, double y2) {
        this.image = ImageUtils.loadImage("resources/images/scene/nineslice/" + image);
        this.strokeSize = strokeSize;
        this.aabb = new AABB(x1, y1, x2, y2, this);
        this.hovered = false;
    }

    public Object tryInteraction(EngineFrame e) {
        AABB point = new AABB(e.getMouseX(), e.getMouseY(), e.getMouseX(), e.getMouseY(), null);

        if(aabb.active && aabb.collidesWith(point)) {
            if(e.isMouseButtonPressed(EngineFrame.MOUSE_BUTTON_LEFT)) return onClick();
            else if(e.isMouseButtonReleased(EngineFrame.MOUSE_BUTTON_LEFT)) return onRelease();
            else if(e.isMouseButtonDown(EngineFrame.MOUSE_BUTTON_LEFT)) hovered = false;
            else hovered = true;
        }
        else hovered = false;

        return null;
    }

    protected void setPos(double x, double y) {
       aabb.move(x - getCenterX(), y - getCenterY());
    }

    protected double getCenterX() {
        return (aabb.x1 + aabb.x2) / 2;
    }

    protected double getCenterY() {
        return (aabb.y1 + aabb.y2) / 2;
    }

    public abstract Object onClick();
    public abstract Object onRelease();

    public void draw(EngineFrame e) {
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        double scaledSize = scale * strokeSize;
        double halvedSize = scaledSize / 2;
        Rectangle src = new Rectangle();
        Rectangle dest = new Rectangle();

        double hoverZoom = hovered ? scale : 0.0;

        //Center
        src.x = strokeSize;
        src.y = strokeSize;
        src.width = imgWidth - strokeSize * 2;
        src.height = imgHeight - strokeSize * 2;
        dest.x = aabb.x1 + halvedSize - hoverZoom;
        dest.y = aabb.y1 + halvedSize - hoverZoom;
        dest.width = aabb.width - scaledSize + hoverZoom * 2;
        dest.height = aabb.height - scaledSize + hoverZoom * 2;
        image.draw(e, src, dest);

        //Top stroke
        src.x = strokeSize;
        src.y = 0.0;
        src.width = imgWidth - strokeSize * 2;
        src.height = strokeSize;
        dest.x = aabb.x1 + halvedSize - hoverZoom;
        dest.y = aabb.y1 - halvedSize - hoverZoom;
        dest.width = aabb.width - scaledSize + hoverZoom * 2;
        dest.height = scaledSize;
        image.draw(e, src, dest);

        //Left stroke
        src.x = 0.0;
        src.y = strokeSize;
        src.width = strokeSize;
        src.height = imgHeight - strokeSize * 2;
        dest.x = aabb.x1 - halvedSize - hoverZoom;
        dest.y = aabb.y1 + halvedSize - hoverZoom;
        dest.width = scaledSize;
        dest.height = aabb.height - scaledSize + hoverZoom * 2;
        image.draw(e, src, dest);

        //Right stroke
        src.x = imgWidth - strokeSize;
        src.y = strokeSize;
        src.width = strokeSize;
        src.height = imgHeight - strokeSize * 2;
        dest.x = aabb.x2 - halvedSize + hoverZoom;
        dest.y = aabb.y1 + halvedSize - hoverZoom;
        dest.width = scaledSize;
        dest.height = aabb.height - scaledSize + hoverZoom * 2;
        image.draw(e, src, dest);

        //Bottom stroke
        src.x = strokeSize;
        src.y = imgHeight - strokeSize;
        src.width = imgWidth - strokeSize * 2;
        src.height = strokeSize;
        dest.x = aabb.x1 + halvedSize - hoverZoom;
        dest.y = aabb.y2 - halvedSize + hoverZoom;
        dest.width = aabb.width - scaledSize + hoverZoom * 2;
        dest.height = scaledSize;
        image.draw(e, src, dest);

        //Top left corner
        src.x = 0.0;
        src.y = 0.0;
        src.width = strokeSize;
        src.height = strokeSize;
        dest.x = aabb.x1 - halvedSize - hoverZoom;
        dest.y = aabb.y1 - halvedSize - hoverZoom;
        dest.width = scaledSize;
        dest.height = scaledSize;
        image.draw(e, src, dest);
        
        //Top right corner
        src.x = imgWidth - strokeSize;
        src.y = 0.0;
        src.width = strokeSize;
        src.height = strokeSize;
        dest.x = aabb.x2 - halvedSize + hoverZoom;
        dest.y = aabb.y1 - halvedSize - hoverZoom;
        dest.width = scaledSize;
        dest.height = scaledSize;
        image.draw(e, src, dest);

        //Bottom left corner
        src.x = 0.0;
        src.y = imgHeight - strokeSize;
        src.width = strokeSize;
        src.height = strokeSize;
        dest.x = aabb.x1 - halvedSize - hoverZoom;
        dest.y = aabb.y2 - halvedSize + hoverZoom;
        dest.width = scaledSize;
        dest.height = scaledSize;
        image.draw(e, src, dest);

        //Bottom right corner
        src.x = imgWidth - strokeSize;
        src.y = imgHeight - strokeSize;
        src.width = strokeSize;
        src.height = strokeSize;
        dest.x = aabb.x2 - halvedSize + hoverZoom;
        dest.y = aabb.y2 - halvedSize + hoverZoom;
        dest.width = scaledSize;
        dest.height = scaledSize;
        image.draw(e, src, dest);
    }
}
