package ghostkitchen.core.item;

import java.awt.Color;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;

import ghostkitchen.Main;
import ghostkitchen.core.Grabbable;
import ghostkitchen.util.AABBUtils;

public abstract class Item {
    protected static final String imgPath = "resources/images/items/";

    private AABB aabb;
    private Image image;
    private double collisionWidth;
    private int baseline;
    private int sandwichHeight;
    private double scale;

    protected Item(String image, int width, int height, int baseline) {
        this(image, width, height, baseline, 0);
    }

    protected Item(String image, int width, int height, int baseline, int sandwichHeight) {
        double halfDimX = (double)width / 2.0;
        double halfDimY = (double)height / 2.0;
        this.aabb = new AABB(-halfDimX, -halfDimY, halfDimX, halfDimY, this);
        this.aabb.type = AABB.Type.DYNAMIC;
        this.image = ImageUtils.loadImage(imgPath + image);
        this.collisionWidth = (double)width;
        this.baseline = baseline;
        this.sandwichHeight = sandwichHeight;
        setScale(2.4);
    }

    public AABB getAABB() {
        return AABBUtils.copy(aabb);
    }

    public AABB getOffsetAABB(double x, double y) {
        AABB copy = getAABB();
        copy.move(x, y);
        return copy;
    }

    public double getLocalPosX() { return (this.aabb.x1 + this.aabb.x2) / 2; }
    public double getLocalPosY() { return (this.aabb.y1 + this.aabb.y2) / 2; }

    public double getWidth() { return this.aabb.width; }
    public double getHeight() { return this.aabb.height; }

    public double getScale() { return this.scale; } 
    public double getCollisionWidth() { return this.collisionWidth; };
    public double getBaseline() { return getScale() * this.baseline; }
    public double getSandwichHeight() { return getScale() * this.sandwichHeight; }

    public void setActiveAABB(boolean active) { this.aabb.active = active; }

    public void setLocalPos(double posX, double posY) { aabb.move(posX - getLocalPosX(), posY - getLocalPosY()); }

    public void setScale(double scale) {
        double posX = this.getLocalPosX();
        double posY = this.getLocalPosY();
        double previousWidth = getWidth();

        if(this.scale == 0.0) this.scale = 1.0;
        this.aabb.setSize(this.aabb.width / this.scale * scale, this.aabb.height / this.scale * scale);
        collisionWidth = collisionWidth != previousWidth ? Math.max(collisionWidth, getWidth()) : getWidth();
        this.scale = scale;
        this.setLocalPos(posX, posY);
    }

    public void setCollisionWidth(double width) { this.collisionWidth = width; }

    public void draw(EngineFrame e) { draw(e, 0.0); }
    public void draw(EngineFrame e, double offsetX, double offsetY) { draw(e, offsetX, offsetY, 0.0); }
    public void draw(EngineFrame e, double animationOffset) { draw(e, 0.0, 0.0, animationOffset); }

    public void draw(EngineFrame e, double offsetX, double offsetY, double animationOffset) {
        draw(e, offsetX, offsetY, animationOffset, null);
    }

    public void draw(EngineFrame e, double offsetX, double offsetY, double animationOffset, Color tint) {
        Image img = tint == null || tint == Color.WHITE ? image : ImageUtils.imageColorTint(image, tint);

        double width = img.getWidth();
        double height = img.getHeight();
        
        double scale = this.scale;
        double rotation = 0.0;

        if(animationOffset != 0.0) {
            rotation = Math.sin(animationOffset) * 5.0;
            scale *= 1.1;
        }

        //Scaled and centered
        Rectangle src = new Rectangle(0.0, 0.0, width, height);
        Rectangle dest = new Rectangle(getLocalPosX() - (width / 2.0) * scale + offsetX, getLocalPosY() - height / 2.0 * scale + offsetY, width * scale, height * scale);
        img.draw(e, src, dest, width / 2.0 * scale, height / 2.0 * scale, rotation);

        if(Main.debug() && aabb.active) {
            AABB tempAABB = getAABB();
            tempAABB.move(offsetX, offsetY);
            double baseline = (tempAABB.y1 + tempAABB.y2) / 2 - getBaseline();
            double topline = baseline - getSandwichHeight();

            tempAABB.draw(e, EngineFrame.BLUE);
            e.drawLine(tempAABB.x1, baseline, tempAABB.x2, baseline, EngineFrame.VIOLET);
            e.drawLine(tempAABB.x1, topline, tempAABB.x2, topline, EngineFrame.VIOLET);
        }
    }

    /**
     * Behavior when interacting with a grabbable
     * @param g The grabbable it will interact with
     * @return whether the grabbable should merge or not
     */
    public abstract boolean hoverInteraction(Grabbable g);

    /**
     * Behavior when interacting with a grabbable
     * @param g The grabbable it will interact with
     * @return whether the grabbable should merge or not
     */
    public abstract boolean idleInteraction(Grabbable g);
}
