package ghostkitchen.scene.button;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.Main;
import ghostkitchen.util.PanelUtils;

public class Button {
    protected static final double scale = 2.4;
    protected static final String imgPath = "resources/images/scene/nineslice/";

    protected Image image;
    protected int borderSize;
    protected AABB aabb;
    protected boolean hovered;
    protected ButtonAction pressAction;
    protected ButtonAction releaseAction;
    protected ButtonAction hoverAction;

    public Button(double x, double y) {
        this("generic.png", 4, x - 50.0, y - 50.0, x + 50.0, y + 50.0);
    }

    public Button(String image, int borderSize, double x1, double y1, double x2, double y2) {
        this.image = ImageUtils.loadImage(imgPath + image);
        this.borderSize = borderSize;
        this.aabb = new AABB(x1, y1, x2, y2, this);
        this.hovered = false;
        setHoverAction(() -> { hovered = true; });
    }

    public void setPressAction(ButtonAction pressAction) { this.pressAction = pressAction; }
    public void setReleaseAction(ButtonAction releaseAction) { this.releaseAction = releaseAction; }
    public void setHoverAction(ButtonAction hoverAction) { this.hoverAction = hoverAction; }

    public void tryInteraction(EngineFrame e) {
        AABB point = new AABB(e.getMouseX(), e.getMouseY(), e.getMouseX(), e.getMouseY(), null);

        if(aabb.active && aabb.collidesWith(point)) {
            if(pressAction != null && e.isMouseButtonPressed(EngineFrame.MOUSE_BUTTON_LEFT)) pressAction.onInteraction();
            else if(releaseAction != null && e.isMouseButtonReleased(EngineFrame.MOUSE_BUTTON_LEFT)) releaseAction.onInteraction();
            else if(hoverAction != null) hoverAction.onInteraction();
        }
        else hovered = false;
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

    public void draw(EngineFrame e) {
        double hoverZoom = hovered ? scale : 0.0;
        double scaledStroke = scale * 4;
        PanelUtils.drawNineSlice(e, image, new Rectangle(aabb.x1 - scaledStroke / 2 - hoverZoom, aabb.y1 - scaledStroke / 2 - hoverZoom, aabb.width + scaledStroke + hoverZoom * 2, aabb.height + scaledStroke + hoverZoom * 2), borderSize);
        if(Main.debug()) aabb.draw(e, EngineFrame.LIME);
    }
}
