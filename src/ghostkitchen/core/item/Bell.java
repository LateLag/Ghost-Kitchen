package ghostkitchen.core.item;

import java.awt.Color;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.Main;
import ghostkitchen.core.Grabbable;
import ghostkitchen.scene.Restaurant;
import ghostkitchen.scene.button.CustomAction;
import ghostkitchen.util.AABBUtils;

public class Bell extends Item {
    private AABB buttonAABB;
    private CustomAction ring;
    private Image button;
    private Image base;
    private double ringTimeStamp;

    public Bell(Restaurant r) {
        super("bell/dome.png", 48, 20, -16, 38, false);
        this.buttonAABB = new AABB(scale * -8, scale * -26, scale * 8, scale * -10, this);
        this.button = getImage("items/bell/button.png");
        this.base = getImage("items/bell/base.png");
        this.ringTimeStamp = 0.0;

        this.ring = () -> {
            ringTimeStamp = r.getEngineFrame().getTime();
            r.scheduleAppraisal();
        };
    }

    @Override
    public AABB getOffsetAABB(double x, double y) {
        AABB copy = AABBUtils.offsetCopy(buttonAABB, x, y);
        copy.move(getLocalPosX(), getLocalPosY());
        return copy;
    }

    @Override
    public boolean pressInteraction(Grabbable g) {
        if(g.size() > 1 || g.peek() != this) return true;
        ring.onTrigger();
        return false;
    }

    @Override
    public void draw(EngineFrame e, double offsetX, double offsetY, double animationOffset, double scale, Color tint) {
        double timeSinceLastRing = e.getTime() - ringTimeStamp;
        double rotation = animationOffset == 0.0 ? 0.0 : Math.sin(animationOffset) * 5.0;
        double scaledScale = scale < 0.0 ? 0.0 : scale * this.scale;
        double width = image.getWidth();
        double height = image.getHeight();
        Rectangle src = new Rectangle(0.0, 0.0, width, height);
        Rectangle dest = new Rectangle(getLocalPosX() - (width / 2.0) * scaledScale + offsetX, getLocalPosY() - height / 2.0 * scaledScale + offsetY, width * scaledScale, height * scaledScale);

        Image base;
        Image button;
        if(tint == null || tint == Color.WHITE) {
            base = this.base;
            button = this.button;
        }
        else {
            base = ImageUtils.imageColorTint(this.base, tint);
            button = ImageUtils.imageColorTint(this.button, tint);
        }

        base.draw(e, src, dest, width / 2.0 * scaledScale, height / 2.0 * scaledScale, rotation);
        super.draw(e, offsetX, offsetY, timeSinceLastRing < 1.0 ? timeSinceLastRing * Math.PI * 5.0 : animationOffset, scale, tint);
        //The button is pushed down only for a brief moment
        if(timeSinceLastRing < 0.1) dest.y += 2.0 * scaledScale;
        button.draw(e, src, dest, width / 2.0 * scaledScale, height / 2.0 * scaledScale, rotation);

        if(Main.debug() && buttonAABB.active) {
            AABB tempAABB = getOffsetAABB(offsetX, offsetY);
            tempAABB.draw(e, EngineFrame.LIME);
        }
    }
}
