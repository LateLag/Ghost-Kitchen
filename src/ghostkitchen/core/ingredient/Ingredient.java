package ghostkitchen.core.ingredient;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.Main;

public abstract class Ingredient {
    private AABB aabb;
    private Image image;
    private double collisionWidth;
    private double baseline;
    private double sandwichHeight;
    private double scale;

    protected Ingredient(String image, double width, double height, double baseline, double sandwichHeight) {
        double halfDimX = width / 2.0;
        double halfDimY = height / 2.0;
        this.aabb = new AABB(-halfDimX, -halfDimY, halfDimX, halfDimY, this);
        this.aabb.type = AABB.Type.DYNAMIC;
        this.image = ImageUtils.loadImage("resources/images/ingredients/" + image);
        this.collisionWidth = width;
        this.baseline = baseline;
        this.sandwichHeight = sandwichHeight;
        setScale(3.0);
    }

    public AABB getAABB() {
        AABB copy = new AABB(this.aabb.x1, this.aabb.y1, this.aabb.x2, this.aabb.y2, this);
        copy.type = this.aabb.type;
        copy.active = this.aabb.active;
        copy.nearby = this.aabb.nearby;
        return copy;
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
    public double getBaseline() { return this.baseline * getScale(); }
    public double getSandwichHeight() { return this.sandwichHeight * getScale(); }

    public void setActiveAABB(boolean active) { this.aabb.active = active; }

    public void setLocalPos(double posX, double posY) { aabb.move(posX - getLocalPosX(), posY - getLocalPosY()); }

    public void setScale(double scale) {
        double posX = this.getLocalPosX();
        double posY = this.getLocalPosY();

        if(this.scale == 0.0) this.scale = 1.0;
        this.aabb.setSize(this.aabb.width / this.scale * scale, this.aabb.height / this.scale * scale);
        this.collisionWidth = Math.max(collisionWidth, getWidth());
        this.scale = scale;
        this.setLocalPos(posX, posY);
    }

    public void setCollisionWidth(double width) { this.collisionWidth = width; }

    public void draw(EngineFrame e) { draw(e, 0.0); }
    public void draw(EngineFrame e, double offsetX, double offsetY) { draw(e, offsetX, offsetY, 0.0); }
    public void draw(EngineFrame e, double animationOffset) { draw(e, 0.0, 0.0, animationOffset); }

    public void draw(EngineFrame e, double offsetX, double offsetY, double animationOffset) {
        double width = image.getWidth();
        double height = image.getHeight();
        
        double scale = this.scale;
        double rotation = 0.0;

        if(animationOffset != 0.0) {
            rotation = Math.sin(animationOffset) * 5.0;
            scale *= 1.1;
        }

        //Scaled and centered
        Rectangle src = new Rectangle(0.0, 0.0, width, height);
        Rectangle dest = new Rectangle(getLocalPosX() - (width / 2.0) * scale + offsetX, getLocalPosY() - height / 2.0 * scale + offsetY, width * scale, height * scale);

        image.draw(e, src, dest, width / 2.0 * scale, height / 2.0 * scale, rotation);

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
}
