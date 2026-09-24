package ghostkitchen.core.ingredient;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;

public abstract class Ingredient {
    private AABB aabb;
    private Image image;
    private double baseline;
    private double sandwichHeight;
    private double scale;

    protected Ingredient(String image, double width, double height, double baseline, double sandwichHeight) {
        this.aabb = new AABB(0.0, 0.0, width, height, this);
        this.aabb.type = AABB.Type.DYNAMIC;
        this.image = ImageUtils.loadImage("resources/images/ingredients/" + image);
        this.baseline = baseline;
        this.sandwichHeight = sandwichHeight;
        setScale(2.0);
        setPos(0, -getBaseline());
    }

    public AABB getAABB() {
        AABB copy = new AABB(this.aabb.x1, this.aabb.y1, this.aabb.x2, this.aabb.y2, this);
        copy.type = this.aabb.type;
        copy.active = this.aabb.active;
        copy.nearby = this.aabb.nearby;
        return copy;
    }

    public double getPosX() { return (this.aabb.x1 + this.aabb.x2) / 2; }
    public double getPosY() { return (this.aabb.y1 + this.aabb.y2) / 2; }

    public double getWidth() { return this.aabb.width; }
    public double getHeight() { return this.aabb.height; }

    public double getBaseline() { return this.baseline * this.scale; }
    public double getSandwichHeight() { return this.sandwichHeight * this.scale; }

    public void setActiveAABB(boolean active) { this.aabb.active = active; }

    public void setPos(double posX, double posY) { aabb.move(posX - getPosX(), posY - getPosY()); }

    public void setScale(double scale) {
        this.scale = scale;
        double posX = this.getPosX();
        double posY = this.getPosY();
        this.aabb.setSize(this.aabb.width * scale, this.aabb.height * scale);
        this.setPos(posX, posY);
    }

    public void draw(EngineFrame e, boolean grabbed) {
        double width = image.getWidth();
        double height = image.getHeight();
        
        double scale = this.scale;
        double rotation = 0;

        if(grabbed) {
            rotation = Math.sin(15 * e.getTime()) * 5;
            scale *= 1.1;
        }

        //Scaled and centered
        Rectangle src = new Rectangle(0, 0, width, height);
        Rectangle dest = new Rectangle(getPosX() - width / 2 * scale, getPosY() - height / 2 * scale, width * scale, height * scale);

        image.draw(e, src, dest, width / 2 * scale, height / 2 * scale, rotation);
        aabb.draw(e, EngineFrame.BLUE);
    }
}
