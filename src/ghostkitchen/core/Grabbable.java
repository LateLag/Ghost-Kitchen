package ghostkitchen.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import ghostkitchen.core.ingredient.Ingredient;
import ghostkitchen.core.ingredient.Plate;

public class Grabbable implements Iterable<Ingredient> {
    private AABB aabb;
    private Deque<Ingredient> ingredients;
    protected boolean grabbed;

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
            ingredient.setPos(prev.getPosX(), prev.getPosY() - prev.getBaseline() - prev.getSandwichHeight() + ingredient.getBaseline());
        }
        resize(ingredient);
        //System.out.println("Stacked " + ingredient + " on " + this);
    }

    public Ingredient pop() {
        Ingredient popped = resize(ingredients.peekLast(), true);
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
            if(ingredients.size() > 1 && peek().getAABB().collidesWith(point)) g = new Grabbable(pop());
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
        if(aabb.active && g.aabb.active && aabb.collidesWith(g.aabb)) {
            for(Ingredient i : this) {
                g.push(i);
            }
            return true;
        }
        return false;
    }

    public void update(double delta) {
        
    }

    public double getPosX() { return (this.aabb.x1 + this.aabb.x2) / 2; }
    public double getPosY() { return (this.aabb.y1 + this.aabb.y2) / 2; }

    public void setPos(double posX, double posY) {
        this.setPos(posX, posY, 0.0);
    }

    private void setPos(double posX, double posY, double offsetX) {
        Ingredient base = ingredients.peekFirst();
        double deltaY = posY - base.getPosY();
        double deltaX = posX - base.getPosX();

        for(Ingredient g : ingredients) {
            g.setPos(g.getPosX() + deltaX, g.getPosY() + deltaY);
            deltaX += offsetX;
            offsetX *= 2;
        }

        aabb.x1 = posX - aabb.width / 2;
        aabb.x2 = posX + aabb.width / 2;
        aabb.y2 = posY + base.getHeight() / 2;
        aabb.y1 = aabb.y2 - aabb.height;
    }

    private Ingredient resize(Ingredient ingredient) {
        return resize(ingredient, false);
    }

    private Ingredient resize(Ingredient ingredient, boolean pop) {
        int size = size();

        if(!pop) ingredients.add(ingredient);

        if(!isEmpty()) {
            int amount = pop ? -1 : 1;
            double posX = getPosX();

            aabb.width = (aabb.width * size + amount * ingredient.getWidth()) / (size + amount);
            aabb.x1 = posX - aabb.width / 2;
            aabb.x2 = posX + aabb.width / 2;

            if(pop) ingredient = ingredients.removeLast();

            aabb.y1 = peek().getPosY() - peek().getHeight() / 2;
            aabb.height = aabb.y2 - aabb.y1;
        }

        return ingredient;
    }

    public void draw(EngineFrame e) {
        for(Ingredient i : ingredients) {
            i.draw(e, grabbed);
        }
        aabb.draw(e, EngineFrame.RED);
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