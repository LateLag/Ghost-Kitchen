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
import ghostkitchen.core.item.Cheese;
import ghostkitchen.core.item.Coin;
import ghostkitchen.core.item.Cone;
import ghostkitchen.core.item.IceCreamStrawberry;
import ghostkitchen.core.item.Item;
import ghostkitchen.core.item.Knife;
import ghostkitchen.core.item.Patty;
import ghostkitchen.core.item.Plate;
import ghostkitchen.scene.button.Button;
import ghostkitchen.scene.button.ItemDispenser;
import ghostkitchen.scene.button.ItemGenerator;

public class Restaurant extends Scene {
    private static final double doorOpeningTime = 0.5;
    private static final double appraisalTime = 1.0;
    private static final double orderSpacing = 156.0;

    private boolean lockedInteractions;
    private Image park;
    private Image restaurant;
    private Image rollUpDoor;
    private Image cabinetDoor;
    private double doorTimer;
    private double queueOffset;
    private double appraisalTimer;
    private List<Grabbable> grabbables;
    private List<Button> buttons;
    private Queue<Order> orders;
    private Client[] clients;
    private Grabbable grabbed;
    private Grabbable dish;
    private AABB cookingArea;

    public Restaurant(EngineFrame e) {
        super(e);
        this.doorTimer = doorOpeningTime;
        this.appraisalTimer = appraisalTime * 2;
        this.lockedInteractions = false;
        this.park = ImageUtils.loadImage(imgPath + "park.png");
        this.restaurant = ImageUtils.loadImage(imgPath + "restaurant.png");
        this.rollUpDoor = ImageUtils.loadImage(imgPath + "roll_up_door.png");
        this.cabinetDoor = ImageUtils.loadImage(imgPath + "cabinet_door.png");
        this.grabbables = new LinkedList<>();
        this.orders = new LinkedList<>();
        this.clients = new Client[] {
            new Placeholder()
        };
    }

    @Override
    public void setup() {
        grabbables.clear();
        cookingArea = new AABB(80.0, -280.0, (double)(engine.getScreenWidth() - 80), (double)(engine.getScreenHeight() - 280), this);
        spawnItem(new Plate(), (cookingArea.x1 + cookingArea.x2) / 2, cookingArea.y2);
        spawnItem(new Plate(), (cookingArea.x1 + cookingArea.x2) / 2, cookingArea.y2);
        spawnItem(new Plate(), (cookingArea.x1 + cookingArea.x2) / 2, cookingArea.y2);
        dish = grabbables.getFirst();
        buttons = new ArrayList<>();
        double space = 154.0;
        double pos = engine.getWidth() / 2 - space * 5;
        buttons.add(new ItemGenerator(this, Bun.class, pos += space, 768));
        buttons.add(new ItemGenerator(this, Patty.class, pos += space, 768));
        buttons.add(new ItemGenerator(this, Cheese.class, pos += space, 768));
        buttons.add(new ItemGenerator(this, Cone.class, pos += space, 768));
        buttons.add(new ItemGenerator(this, IceCreamStrawberry.class, pos += space, 768));
        buttons.add(new ItemDispenser(this, Knife.class, pos += space + space / 2, 768, 120 + space, 120.0));

        //TODO: real finish action
        Button b = new Button(200, 500);
        b.setPressAction(() -> {
            startAppraisal();
        });
        buttons.add(b);
    }

    public void spawnItem(Item item, double posX, double posY) {
        Grabbable g = new Grabbable(item);
        g.setPos(posX, posY);
        grabbables.add(g);
    }

    public void generateOrder() {
        generateOrder(null);
    }

    public void generateOrder(Client c) {
        if(c == null) c = clients[ThreadLocalRandom.current().nextInt(clients.length)];
        orders.add(new Order(c));
    }

    public List<Grabbable> getGrabbables() { return grabbables; }
    public Grabbable getGrabbed() { return grabbed; }

    public void setGrabbed(Grabbable g) {
        if(grabbed != null && g != grabbed) grabbed.drop();
        if(g != null) {
            g.grab();
            int index = grabbables.indexOf(g);
            if(index >= 0) grabbables.add(grabbables.remove(index));
            else grabbables.add(g);

            if(g.isPotentialMainDish()) dish = g;
        }
        grabbed = g;
    }

    public void startAppraisal() {
        if(!orders.isEmpty() && dish != null) {
            lockedInteractions = true;
            appraisalTimer = appraisalTime;

            setGrabbed(null);
            if(dish.size() > 1) {
                Item i = dish.peek();
                if(!(i instanceof Plate)) {
                    double offsetY = i.getLocalPosY();
                    setGrabbed(new Grabbable(dish.pop()));
                    grabbed.setPos(dish.getPosX(), dish.getPosY() + offsetY);
                }
            }
        }
    }

    public void update(double delta) {
        //Timer that controls the door opening progress at the start
        if(doorTimer > 0.0) {
            doorTimer -= delta;
            if(doorTimer < 0.0) doorTimer = 0.0;
        }

        //Appraisal logic
        if(lockedInteractions) {
            appraisalTimer -= delta;

            if(appraisalTimer <= 0.0 && appraisalTimer > -appraisalTime) {
                orders.peek().validateNext(grabbed == null ? null : grabbed.peek());
                grabbables.remove(grabbed);
                appraisalTimer = appraisalTime;

                if(dish.size() > 1 && !(dish.peek() instanceof Plate)) {
                    double offsetY = dish.peek().getLocalPosY();
                    setGrabbed(new Grabbable(dish.pop()));
                    grabbed.setPos(dish.getPosX(), dish.getPosY() + offsetY);
                }
                else appraisalTimer = -appraisalTime;
            }
            else if(appraisalTimer <= -2.0 * appraisalTime) {
                lockedInteractions = false;
                queueOffset -= orderSpacing;
                int score = orders.poll().getValidityScore();
                for(int i = 0; i < score; i++) {
                    spawnItem(new Coin(), engine.getScreenWidth() / 2, engine.getScreenHeight() / 2);
                }
            }
        }

        if(queueOffset < orderSpacing * orders.size()) queueOffset += orderSpacing * 10.0 * delta;

        //TODO: Order generation logic
        if(engine.isKeyPressed(EngineFrame.KEY_ZERO)) generateOrder();

        boolean interact = !lockedInteractions ? engine.isMouseButtonPressed(EngineFrame.MOUSE_BUTTON_LEFT) : false;

        //Update logic for all grabbables. Order is reversed to ensure the most recently interacted with grabbables are prioritized
        for(int i = grabbables.size() - 1; i >= 0; i--) {
            Grabbable g = grabbables.get(i);

            //Grab logic
            if(interact && grabbed == null) setGrabbed(g.getFromCoordinates(engine.getMouseX(), engine.getMouseY()));

            //Interaction logic
            for(int j = i - 1; j >= 0; j--) {
                Grabbable other = grabbables.get(j);
                boolean merge = g.interact(other);
                if(merge) {
                    if(other.isPotentialMainDish()) dish = other;
                    grabbables.remove(i);
                    break;
                }
            }

            g.update(delta);
            g.limitPos(cookingArea);
        }

        if(!lockedInteractions) {
            for(int i = buttons.size() - 1; i >= 0; i--) {
                buttons.get(i).tryInteraction(engine);
            }
        }

        //Update logic for the grabbed grabbable
        if(grabbed != null && !lockedInteractions) {
            grabbed.setPos(engine.getMouseX(), engine.getMouseY());
            if(engine.isMouseButtonReleased(EngineFrame.MOUSE_BUTTON_LEFT)) {
                setGrabbed(null);
            }
        }
    }

    @Override
    public void draw() {
        Rectangle src;
        Rectangle dest = new Rectangle(0.0, 0.0, engine.getScreenWidth(), engine.getScreenHeight());
        double doorProgress = doorTimer / doorOpeningTime;

        //Park
        src = new Rectangle(0.0, 0.0, park.getWidth(), park.getHeight());
        park.draw(engine, src, dest);

        //Client
        if(lockedInteractions) {
            if(!orders.isEmpty()) orders.peek().getClient().draw(engine, engine.getScreenWidth() / 2, cookingArea.y2);
        }

        //Roll up door
        if(doorTimer > 0.0) {
            double scaledWidth = rollUpDoor.getWidth() * scale;
            double scaledHeight = rollUpDoor.getHeight() * scale * doorProgress;

            src = new Rectangle(0.0, rollUpDoor.getHeight() * (1.0 - doorProgress), rollUpDoor.getWidth(), rollUpDoor.getHeight() * doorProgress);
            dest = new Rectangle((engine.getScreenWidth() - scaledWidth) / 2, 0.0, scaledWidth, scaledHeight);

            rollUpDoor.draw(engine, src, dest);
        }

        //Restaurant
        src = new Rectangle(0.0, 0.0, restaurant.getWidth(), restaurant.getHeight());
        dest = new Rectangle(0.0, 0.0, engine.getScreenWidth(), engine.getScreenHeight());
        restaurant.draw(engine, src, dest);
        
        //Order queue
        double orderOffset = queueOffset - orderSpacing / 2;
        for(Order o : orders) {
            o.draw(engine, orderOffset, 24.0);
            orderOffset -= orderSpacing;
        }

        for(Button p : buttons) {
            p.draw(engine);
        }

        for(Grabbable g : grabbables) {
            g.draw(engine);
        }

        if(Main.debug()) cookingArea.draw(engine, EngineFrame.GREEN);

        //Cabinet doors
        if(doorTimer > 0.0) {
            //Cabinet doors
            double halfStroke = 2.0 * scale;
            double scaledWidth = cabinetDoor.getWidth() * scale;
            double scaledHeight = cabinetDoor.getHeight() * scale;

            //Left cabinet door
            src = new Rectangle(cabinetDoor.getWidth() * (1.0 - doorProgress), 0.0, cabinetDoor.getWidth(), cabinetDoor.getHeight());
            dest = new Rectangle(engine.getScreenWidth() / 2 + halfStroke - scaledWidth, engine.getScreenHeight() - scaledHeight, scaledWidth, scaledHeight);
            cabinetDoor.draw(engine, src, dest);

            //Right cabinet door
            dest.x = engine.getScreenWidth() / 2 - halfStroke;
            cabinetDoor.draw(engine, src, dest, scaledWidth / 2, scaledHeight / 2, 180.0);
        }

        super.draw();
    }
}
