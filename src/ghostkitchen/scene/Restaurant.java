package ghostkitchen.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import br.com.davidbuzatto.jsge.collision.aabb.AABB;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.Main;
import ghostkitchen.core.Grabbable;
import ghostkitchen.core.Order;
import ghostkitchen.core.client.Client;
import ghostkitchen.core.client.Placeholder;
import ghostkitchen.core.item.Bun;
import ghostkitchen.core.item.BunBottom;
import ghostkitchen.core.item.BunTop;
import ghostkitchen.core.item.Cheese;
import ghostkitchen.core.item.Cone;
import ghostkitchen.core.item.IceCreamStrawberry;
import ghostkitchen.core.item.Item;
import ghostkitchen.core.item.Knife;
import ghostkitchen.core.item.Patty;
import ghostkitchen.core.item.Plate;
import ghostkitchen.scene.button.Button;
import ghostkitchen.scene.button.Dispenser;

public class Restaurant extends Scene {
    private static final double doorOpeningTime = 0.5;

    private double doorTimer;
    private Image rollUpDoor;
    private Image cabinetDoor;
    private List<Grabbable> grabbables;
    private List<Button> buttons;
    private Queue<Order> orders;
    private Client[] clients;
    private Grabbable grabbed;
    private AABB cookingArea;

    public Restaurant(EngineFrame e) {
        super(e, "park.png", "restaurant.png");
        this.doorTimer = doorOpeningTime;
        this.rollUpDoor = ImageUtils.loadImage("resources/images/scene/roll_up_door.png");
        this.cabinetDoor = ImageUtils.loadImage("resources/images/scene/cabinet_door.png");
        this.grabbables = new LinkedList<>();
        this.orders = new LinkedList<>();
        this.clients = new Client[] {
            new Placeholder()
        };
    }

    public void resetCookingArea() {
        grabbables.clear();
        this.cookingArea = new AABB(80.0, 0.0, (double)(engine.getWidth() - 80), (double)(engine.getHeight() - 282), this);
        this.buttons = new ArrayList<>();
        buttons.add(new Dispenser(Bun.class, 144, 768));
        buttons.add(new Dispenser(Patty.class, 300, 768));
        buttons.add(new Dispenser(Cheese.class, 456, 768));
        buttons.add(new Dispenser(Cone.class, 612, 768));
        buttons.add(new Dispenser(IceCreamStrawberry.class, 768, 768));
        buttons.add(new Dispenser(Knife.class, 924, 768));
        buttons.add(new Dispenser(Plate.class, 1080, 768));
    }

    public void spawnItem(Item item, double posX, double posY) {
        Grabbable g = new Grabbable(item);
        g.setPos(posX, posY);
        grabbables.add(g);
    }

    public void generateOrder() {
        orders.add(new Order(clients[ThreadLocalRandom.current().nextInt(clients.length)]));
    }

    public void update(double delta) {
        //Timer that controls the door opening progress at the start
        if(doorTimer > 0.0) {
            doorTimer -= delta;
            if(doorTimer < 0.0) doorTimer = 0.0;
        }

        double midX = engine.getScreenWidth() / 2;
        double midY = engine.getScreenHeight() / 2;
        //TODO: Remove debug commands
        if(engine.isKeyPressed(EngineFrame.KEY_ONE)) spawnItem(new Plate(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_TWO)) spawnItem(new BunBottom(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_THREE)) spawnItem(new Patty(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_FOUR)) spawnItem(new Cheese(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_FIVE)) spawnItem(new BunTop(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_SIX)) spawnItem(new Cone(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_SEVEN)) spawnItem(new IceCreamStrawberry(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_EIGHT)) spawnItem(new Bun(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_NINE)) spawnItem(new Knife(), midX, midY);
        if(engine.isKeyPressed(EngineFrame.KEY_ZERO)) generateOrder();

        boolean interact = false;
        if(engine.isMouseButtonPressed(EngineFrame.MOUSE_BUTTON_LEFT)) interact = true;

        for(int i = buttons.size() - 1; i >= 0; i--) {
            Object o = buttons.get(i).tryInteraction(engine);
            if(o instanceof Grabbable) {
                Grabbable g = (Grabbable)o;
                grabbed = g.grab(g.getPosX(), g.getPosY()); //Ensures the created grabbable is grabbed regardless of how much the mouse has moved in the frame
                grabbables.add(grabbed);
                break;
            }
        }

        for(int i = grabbables.size() - 1; i >= 0; i--) {
            Grabbable g = grabbables.get(i);

            //Grab logic
            if(interact && grabbed == null) {
                grabbed = g.grab(engine.getMouseX(), engine.getMouseY());

                //Move the current grabbable to the top
                if(grabbed == g) grabbables.add(grabbables.remove(i));
                //If the grabbed grabbable is new and not null
                else if(grabbed != null) grabbables.add(grabbed);
            }

            //Interaction logic
            for(int j = i - 1; j >= 0; j--) {
                boolean merge = g.interact(grabbables.get(j));
                if(merge) {
                    grabbables.remove(i);
                    break;
                }
            }

            g.update(delta);
            g.limitPos(cookingArea);
        }

        //Update logic for the grabbed grabbable
        if(grabbed != null) {
            grabbed.setPos(engine.getMouseX(), engine.getMouseY());
            if(engine.isMouseButtonReleased(EngineFrame.MOUSE_BUTTON_LEFT)) {
                grabbed.drop();
                grabbed = null;
            }
        }
    }

    @Override
    public void drawBefore() {
        super.drawBefore();
        clients[0].draw(engine, engine.getScreenWidth() / 2, cookingArea.y2);

        if(doorTimer > 0.0) {
            double doorProgress = (doorTimer / doorOpeningTime);

            double scaledWidth = rollUpDoor.getWidth() * scale;
            double scaledHeight = rollUpDoor.getHeight() * scale * doorProgress;

            Rectangle src = new Rectangle(0.0, rollUpDoor.getHeight() * (1.0 - doorProgress), rollUpDoor.getWidth(), rollUpDoor.getHeight() * doorProgress);
            Rectangle dest = new Rectangle((engine.getScreenWidth() - scaledWidth) / 2, 0.0, scaledWidth, scaledHeight);

            rollUpDoor.draw(engine, src, dest);
        }
    }

    @Override
    public void drawAfter() {
        //TODO: proper order queue
        double tempX = 100.0;
        for(Order o : orders) {
            o.draw(engine, tempX, 100);
            tempX += 100.0;
        }

        for(Button p : buttons) {
            p.draw(engine);
        }

        for(Grabbable g : grabbables) {
            g.draw(engine);
        }

        if(doorTimer > 0.0) {
            double doorProgress = (doorTimer / doorOpeningTime);

            double halfStroke = 2.0 * scale;
            double scaledWidth = cabinetDoor.getWidth() * scale;
            double scaledHeight = cabinetDoor.getHeight() * scale;

            //Left cabinet door
            Rectangle src = new Rectangle(cabinetDoor.getWidth() * (1.0 - doorProgress), 0.0, cabinetDoor.getWidth(), cabinetDoor.getHeight());
            Rectangle dest = new Rectangle(engine.getScreenWidth() / 2 + halfStroke - scaledWidth, engine.getScreenHeight() - scaledHeight, scaledWidth, scaledHeight);
            cabinetDoor.draw(engine, src, dest);

            //Right cabinet door
            dest.x = engine.getScreenWidth() / 2 - halfStroke;
            cabinetDoor.draw(engine, src, dest, scaledWidth / 2, scaledHeight / 2, 180.0);

            super.drawAfter();
        }

        if(Main.debug()) cookingArea.draw(engine, EngineFrame.GREEN);

        super.drawAfter();
    }
}
