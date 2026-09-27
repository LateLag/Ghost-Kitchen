package ghostkitchen.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import ghostkitchen.Main;
import ghostkitchen.core.ingredient.Ingredient;
import ghostkitchen.core.ingredient.Plate;

public class Grabbable implements Iterable<Ingredient> {
    private AABB aabb;
    private Deque<Ingredient> ingredients;
    private boolean grabbed;

    public Grabbable() {
        this(0.0, 0.0);
    }

    public Grabbable(double x, double y) {
        this(new Plate(), x, y);
    }

    public Grabbable(Ingredient base) {
        this.ingredients = new ArrayDeque<>();
        if(base != null) this.push(base);
    }

    public Grabbable(Ingredient base, double x, double y) {
        this(base);
        this.setPos(x, y);
    }

    public void push(Ingredient ingredient) {
        if(isEmpty()) {
            this.aabb = ingredient.getAABB();
            this.aabb.referencedObject = this;
        }
        else {
            Ingredient prev = peek();
            prev.setActiveAABB(false);
            ingredient.setLocalPos(prev.getLocalPosX(), prev.getLocalPosY() - prev.getBaseline() - prev.getSandwichHeight() + ingredient.getBaseline());
        }
        resize(ingredient);
        //System.out.println("Stacked " + ingredient + " on " + this);
    }

    public Ingredient pop() {
        Ingredient popped = resize(ingredients.peekLast());
        if(!isEmpty()) peek().setActiveAABB(true);
        return popped;
    }

    public Ingredient peek() {
        return this.ingredients.peekLast();
    }

    public int size() {
        return this.ingredients.size();
    }

    public boolean isEmpty() {
        return this.ingredients.isEmpty();
    }

    public Grabbable grab(double x, double y) {
        AABB point = new AABB(x, y, x, y, null);

        //Only attempts a grab if the AABB is active
        if(aabb.active) {
            Grabbable g = null;
            //If the point collides with the top ingredient, pop and return it instead
            if(ingredients.size() > 1 && peek().getOffsetAABB(getPosX(), getPosY()).collidesWith(point)) {
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
    }

    public boolean merge(Grabbable g) {
        if(g == null) return false;

        if(!g.grabbed && aabb.active && g.aabb.active && aabb.collidesWith(g.aabb)) {
            for(Ingredient i : this) {
                g.push(i);
            }
            return true;
        }
        return false;
    }

    public void update(double delta) {
    }

    public double getPosX() { return (this.aabb.x1 + this.aabb.x2) / 2.0; }
    public double getPosY() { return (this.aabb.y2 - ingredients.getFirst().getHeight() / 2.0); }

    public void limitPos(AABB area) {
        if(area != null) {
            double distanceX = peek().getCollisionWidth() / 2.0;
            double distanceY = ingredients.getFirst().getBaseline();
            setPos(
                Math.max(area.x1 + distanceX, Math.min(area.x2 - distanceX, getPosX())),
                Math.max(area.y1, Math.min(area.y2 + distanceY, getPosY()))
            );
        }
    }

    public void setPos(double posX, double posY) {
        aabb.x1 = posX - aabb.width / 2;
        aabb.x2 = posX + aabb.width / 2;
        aabb.y2 = posY + ingredients.peekFirst().getHeight() / 2;
        aabb.y1 = aabb.y2 - aabb.height;
    }

    private Ingredient resize(Ingredient ingredient) {
        if(ingredient != null) {
            int previousSize = size();
            Ingredient previousTop = peek();
            boolean pop = ingredient == previousTop;

            if(!pop) ingredients.add(ingredient);

            //Intentionally uses the pre-addition value to ensure the AABB is only recalculated starting from the second addition
            if(previousSize > 0) {
                int amount = pop ? -1 : 1;
                double posX = getPosX();
                double previousY = previousTop.getAABB().y1;
                double collisionWidth = previousTop.getCollisionWidth();

                //Recalculate the average width
                aabb.width = (aabb.width * previousSize + amount * ingredient.getWidth()) / (previousSize + amount);
                aabb.x1 = posX - aabb.width / 2;
                aabb.x2 = posX + aabb.width / 2;

                if(pop) {
                    ingredient = ingredients.removeLast();
                    ingredient.setLocalPos(0.0, 0.0);
                    collisionWidth = ingredient.getWidth();
                }

                ingredient.setCollisionWidth(Math.max(collisionWidth, ingredient.getWidth()));

                if(isEmpty()) return ingredient;
                aabb.y1 += peek().getAABB().y1 - previousY;
                aabb.height = aabb.y2 - aabb.y1;
            }
        }

        return ingredient;
    }

    public void setActiveAABB(boolean active) { aabb.active = active; }

    public void draw(EngineFrame e) {
        double baseAnimOffset = 10.0 * e.getTime();
        double posX = getPosX();
        double posY = getPosY();

        for(Ingredient i : ingredients) {
            double layerAnimOffset = (-i.getLocalPosY()) / i.getScale();

            //Only animate when the grabbable is grabbed
            i.draw(e, posX, posY, grabbed ? baseAnimOffset + layerAnimOffset : 0.0);
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
    public Iterator<Ingredient> iterator() {
        return this.ingredients.iterator();
    }

    @Override
    public String toString() {
        return "Grabbable #" + String.valueOf(hashCode());
    }
}