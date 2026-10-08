package ghostkitchen.core.item;

import java.awt.Color;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;

import ghostkitchen.Main;
import ghostkitchen.core.Grabbable;
import ghostkitchen.core.ImageCache;
import ghostkitchen.util.AABBUtils;

public abstract class Item implements ImageCache {
    protected Image image;
    protected AABB aabb;
    protected double collisionWidth;
    protected int baseline;
    protected int sandwichHeight;
    protected double scale;
    protected boolean mixable;

    protected Item(String image, int width, int height, int baseline) {
        this(image, width, height, baseline, 0);
    }

    protected Item(String image, int width, int height, int baseline, int sandwichHeight) {
        this(image, width, height, baseline, sandwichHeight, false);
    }

    protected Item(String image, int width, int height, int baseline, int sandwichHeight, boolean mixable) {
        double halfDimX = (double)width / 2.0;
        double halfDimY = (double)height / 2.0;
        if(image == null) image = "icon.png";
        this.image = getImage("items/" + image);
        this.aabb = new AABB(-halfDimX, -halfDimY, halfDimX, halfDimY, this);
        this.aabb.type = AABB.Type.DYNAMIC;
        this.collisionWidth = (double)width;
        this.baseline = baseline;
        this.sandwichHeight = sandwichHeight;
        this.mixable = mixable;
        setScale(2.4);
    }

    public AABB getAABB() {
        return AABBUtils.copy(aabb);
    }

    public AABB getOffsetAABB(double x, double y) {
        return AABBUtils.offsetCopy(aabb, x, y);
    }

    public double getLocalPosX() { return (this.aabb.x1 + this.aabb.x2) / 2; }
    public double getLocalPosY() { return (this.aabb.y1 + this.aabb.y2) / 2; }

    public double getWidth() { return this.aabb.width; }
    public double getHeight() { return this.aabb.height; }

    public double getScale() { return this.scale; } 
    public double getCollisionWidth() { return this.collisionWidth; };
    public double getBaseline() { return getScale() * this.baseline; }
    public double getSandwichHeight() { return getScale() * this.sandwichHeight; }
    public boolean isMixable() { return this.mixable; }

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
        draw(e, offsetX, offsetY, animationOffset, 1.0, null);
    }

    public void draw(EngineFrame e, double offsetX, double offsetY, double animationOffset, double scale) {
        draw(e, offsetX, offsetY, animationOffset, scale, null);
    }

    public void draw(EngineFrame e, double offsetX, double offsetY, double animationOffset, double scale, Color tint) {
        Image image = tint == null || tint == Color.WHITE ? this.image : ImageUtils.imageColorTint(this.image, tint);

        double width = image.getWidth();
        double height = image.getHeight();

        double rotation = animationOffset == 0.0 ? 0.0 : Math.sin(animationOffset) * 5.0;
        scale = scale < 0.0 ? 0.0 : scale * this.scale;

        //Scaled and centered
        Rectangle src = new Rectangle(0.0, 0.0, width, height);
        Rectangle dest = new Rectangle(getLocalPosX() - (width / 2.0) * scale + offsetX, getLocalPosY() - height / 2.0 * scale + offsetY, width * scale, height * scale);
        image.draw(e, src, dest, width / 2.0 * scale, height / 2.0 * scale, rotation);

        if(Main.debug() && aabb.active) {
            AABB tempAABB = AABBUtils.offsetCopy(aabb, offsetX, offsetY);
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
    public boolean pressInteraction(Grabbable g) {
        return g.size() > 1;
    }

    /**
     * Behavior when interacting with a grabbable
     * @param g The grabbable it will interact with
     * @return whether the grabbable should merge or not
     */
    public boolean hoverInteraction(Grabbable g) {
        return false;
    }

    /**
     * Behavior when interacting with a grabbable
     * @param g The grabbable it will interact with
     * @return whether the grabbable should merge or not
     */
    public boolean idleInteraction(Grabbable g) {
        Item i = g.peek();
        if(i.isMixable() && isMixable() || i.getClass() == getClass()) {
            g.push(this);
            return true;
        }
        return false;
    }
}
