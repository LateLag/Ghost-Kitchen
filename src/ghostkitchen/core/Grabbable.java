package ghostkitchen.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import ghostkitchen.Main;
import ghostkitchen.core.item.Item;
import ghostkitchen.core.item.Plate;

public class Grabbable implements Iterable<Item> {
    private static double gravity = 100.0;

    private AABB aabb;
    private Vector2 previousPos;
    private Vector2 velocity;
    private Deque<Item> items;
    private boolean grabbed;

    public Grabbable() {
        this(0.0, 0.0);
    }

    public Grabbable(double x, double y) {
        this(new Plate(), x, y);
    }

    public Grabbable(Item base) {
        this.items = new ArrayDeque<>();
        this.previousPos = new Vector2();
        this.velocity = new Vector2();
        if(base != null) this.push(base);
    }

    public Grabbable(Item base, double x, double y) {
        this(base);
        this.setPos(x, y);
    }

    public void push(Item item) {
        if(isEmpty()) {
            this.aabb = item.getAABB();
            this.aabb.referencedObject = this;
        }
        else {
            Item prev = peek();
            prev.setActiveAABB(false);
            item.setLocalPos(prev.getLocalPosX(), prev.getLocalPosY() - prev.getBaseline() - prev.getSandwichHeight() + item.getBaseline());
        }
        resize(item);
        //System.out.println("Stacked " + ingredient + " on " + this);
    }

    public Item pop() {
        Item popped = resize(items.peekLast());
        if(!isEmpty()) peek().setActiveAABB(true);
        return popped;
    }

    public Item peek() {
        return this.items.peekLast();
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public Grabbable grab(double x, double y) {
        AABB point = new AABB(x, y, x, y, null);

        //Only attempts a grab if the AABB is active
        if(aabb.active) {
            Grabbable g = null;
            //If the point collides with the top ingredient, pop and return it instead
            if(items.size() > 1 && peek().getOffsetAABB(getPosX(), getPosY()).collidesWith(point)) {
                g = new Grabbable(pop());
            }
            else if(aabb.collidesWith(point)) g = this;

            if(g != null) g.grabbed = true;
            return g;
        }
        return null;
    }

    public void drop() {
        grabbed = false;
        velocity.x = (getPosX() - previousPos.x) * 0.5;
        velocity.y = (getPosY() - previousPos.y) * 0.5;
    }

    public boolean interact(Grabbable g) {
        if(g == null) return false;

        if(!g.grabbed && aabb.active && g.aabb.active && aabb.collidesWith(g.aabb)) {
            boolean merge = false;
            for(Item i : this) {
                merge = grabbed ? i.hoverInteraction(g) : i.idleInteraction(g);
            }
            return merge;
        }
        return false;
    }

    public void update(double delta) {
        setPreviousPos();
        if(!grabbed) {
            velocity.y += gravity * delta;
            aabb.move(velocity.x, velocity.y);
        }
    }

    public double getPosX() { return (this.aabb.x1 + this.aabb.x2) / 2.0; }
    public double getPosY() { return (this.aabb.y2 - items.getFirst().getHeight() / 2.0); }

    public void limitPos(AABB area) {
        if(area != null) {
            double distanceX = peek().getCollisionWidth() / 2.0;
            double distanceY = items.getFirst().getBaseline();
            setPos(
                Math.max(area.x1 + distanceX, Math.min(area.x2 - distanceX, getPosX())),
                Math.max(area.y1, Math.min(area.y2 + distanceY, getPosY()))
            );
            if(previousPos.x == getPosX()) velocity.x *= -0.3;
            if(previousPos.y == getPosY()) { velocity.y *= -0.3; velocity.x *= 0.5; }
        }
    }

    public void setPos(double posX, double posY) {
        aabb.x1 = posX - aabb.width / 2;
        aabb.x2 = posX + aabb.width / 2;
        aabb.y2 = posY + items.peekFirst().getHeight() / 2;
        aabb.y1 = aabb.y2 - aabb.height;
    }

    private void setPreviousPos() {
        previousPos.x = getPosX();
        previousPos.y = getPosY();
    }

    private Item resize(Item item) {
        if(item == null) return null;

        int currentSize = size();
        Item currentTop = peek();
        boolean pop = item == currentTop;

        if(!pop) items.add(item);

        //Intentionally uses the pre-addition value to ensure the AABB is only recalculated starting from the second addition
        if(currentSize > 0) {
            int amount = pop ? -1 : 1;
            double posX = getPosX();
            double currentLocalY = currentTop.getAABB().y1;

            //Recalculate the average width
            if(currentSize + amount != 0) {
                aabb.width = (aabb.width * currentSize + amount * item.getWidth()) / (currentSize + amount);
                aabb.x1 = posX - aabb.width / 2;
                aabb.x2 = posX + aabb.width / 2;
            }

            if(pop) {
                item = items.removeLast();
                item.setLocalPos(0.0, 0.0);
                item.setCollisionWidth(item.getWidth());
            }
            else item.setCollisionWidth(Math.max(currentTop.getCollisionWidth(), item.getWidth()));

            //Set the new height after the deque has changed
            if(!isEmpty()) {
                aabb.y1 += peek().getAABB().y1 - currentLocalY;
                aabb.height = aabb.y2 - aabb.y1;
            }
        }

        return item;
    }

    public void setActiveAABB(boolean active) { aabb.active = active; }

    public void draw(EngineFrame e) {
        double baseAnimOffset = 10.0 * e.getTime();
        double posX = getPosX();
        double posY = getPosY();

        for(Item i : items) {
            double layerAnimOffset = -i.getLocalPosY() / i.getScale();
            double offsetX = -velocity.x * 0.02 * layerAnimOffset;
            double offsetY = velocity.y < 0.0 ? 0.0 : -velocity.y * 0.05 * layerAnimOffset;

            //Only animate when the grabbable is grabbed
            i.draw(e, posX + offsetX, posY + offsetY, grabbed ? baseAnimOffset + layerAnimOffset : 0.0);
        }

        if(Main.debug()) {
            double collisionWidth = peek().getCollisionWidth();
            //Collision box
            e.drawRectangle(posX - collisionWidth / 2.0, aabb.y1, collisionWidth, aabb.height, EngineFrame.RED);
            //Interaction box
            if(aabb.active) aabb.draw(e, EngineFrame.GREEN);
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return this.items.iterator();
    }

    @Override
    public String toString() {
        return "Grabbable #" + String.valueOf(hashCode());
    }
}