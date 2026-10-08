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
import ghostkitchen.Main;
import ghostkitchen.core.Grabbable;
import ghostkitchen.core.Order;
import ghostkitchen.core.customer.ChiffonVelvet;
import ghostkitchen.core.customer.CindySesame;
import ghostkitchen.core.customer.Customer;
import ghostkitchen.core.customer.HansBerger;
import ghostkitchen.core.customer.StraussBerry;
import ghostkitchen.core.item.Bell;
import ghostkitchen.core.item.Bun;
import ghostkitchen.core.item.BunBottom;
import ghostkitchen.core.item.BunTop;
import ghostkitchen.core.item.Cheese;
import ghostkitchen.core.item.Coin;
import ghostkitchen.core.item.Cone;
import ghostkitchen.core.item.CreamChantilly;
import ghostkitchen.core.item.CreamPastry;
import ghostkitchen.core.item.CupcakeBase;
import ghostkitchen.core.item.CupcakeRedVelvet;
import ghostkitchen.core.item.IceCreamChocolate;
import ghostkitchen.core.item.IceCreamMint;
import ghostkitchen.core.item.IceCreamStrawberry;
import ghostkitchen.core.item.Item;
import ghostkitchen.core.item.Knife;
import ghostkitchen.core.item.Lettuce;
import ghostkitchen.core.item.LettuceChopped;
import ghostkitchen.core.item.Patty;
import ghostkitchen.core.item.Plate;
import ghostkitchen.core.item.Tomato;
import ghostkitchen.core.item.TomatoSlices;
import ghostkitchen.scene.button.Button;
import ghostkitchen.scene.button.CustomAction;
import ghostkitchen.scene.button.ItemDispenser;

public class Restaurant extends Scene {
    private static final double doorOpeningTime = 0.5;
    private static final double appraisalTime = 1.0;
    private static final double orderSpacing = 156.0;

    private Image park;
    private Image restaurant;
    private Image rollUpDoor;
    private Image cabinetDoor;
    private boolean appraising;
    private boolean finishedAppraisal;
    private boolean gameOver;
    private double orderTimer;
    private double doorTimer;
    private double queueOffset;
    private double appraisalTimer;
    private List<Grabbable> grabbables;
    private List<Button> buttons;
    private Queue<Order> orders;
    private Customer[] customers;
    private Grabbable grabbed;
    private Grabbable dish;
    private AABB cookingArea;
    private CustomAction returnToMenu;

    public Restaurant(EngineFrame e, Scene menu) {
        super(e);
        this.park = getImage("scene/park.png");
        this.restaurant = getImage("scene/restaurant.png");
        this.rollUpDoor = getImage("scene/roll_up_door.png");
        this.cabinetDoor = getImage("scene/cabinet_door.png");
        this.doorTimer = doorOpeningTime;
        this.appraisalTimer = appraisalTime * 2;
        this.appraising = false;
        this.gameOver = false;
        this.grabbables = new LinkedList<>();
        this.orders = new LinkedList<>();
        this.customers = new Customer[] {
            new ChiffonVelvet(),
            new CindySesame(),
            new HansBerger(),
            new StraussBerry()
        };

        this.returnToMenu = () -> {
            ((Main)e).setScene(menu);
        };
    }

    @Override
    public void setup() {
        grabbables.clear();
        cookingArea = new AABB(80.0, -80.0, (double)(engine.getScreenWidth() - 80), (double)(engine.getScreenHeight() - 280), this);
        buttons = new ArrayList<>();
        int amount = 3;
        double height = 768;
        double space = 108.0;
        double pos = engine.getScreenWidth() / 2 - space * 7;
        buttons.add(new ItemDispenser(this, Bun.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, Patty.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, Cheese.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, Lettuce.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, Tomato.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, CupcakeBase.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, CupcakeRedVelvet.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, CreamChantilly.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, CreamPastry.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, Cone.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, IceCreamChocolate.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, IceCreamStrawberry.class, pos += space, height, amount));
        buttons.add(new ItemDispenser(this, IceCreamMint.class, pos += space, height, amount));
        //buttons.add(new ItemDispenser(this, Knife.class, pos += space + space / 2, height, 108 + space, 108.0));
    }

    public void spawnItem(Item item, double posX, double posY) {
        Grabbable g = new Grabbable(item);
        g.setPos(posX, posY);
        grabbables.add(g);
    }

    public void generateOrder() {
        generateOrder(null);
    }

    public void generateOrder(Customer c) {
        if(c == null) c = customers[ThreadLocalRandom.current().nextInt(customers.length)];
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

    public void scheduleAppraisal() {
        appraising = orders.isEmpty() || dish == null ? false : true;

        if(appraising) {
            finishedAppraisal = false;
            appraisalTimer = appraisalTime;
        }
    }

    public void update(double delta) {
        boolean interact = !appraising ? engine.isMouseButtonPressed(EngineFrame.MOUSE_BUTTON_LEFT) : false;

        //The game is lost when the next order in the queue is no longer visible on screen
        if(queueOffset >= orderSpacing * 11) gameOver = true;

        if(gameOver) {
            //Timer that controls the door opening animation progress at the start and end
            if(doorTimer < doorOpeningTime) {
                doorTimer += delta;
                if(doorTimer > doorOpeningTime) {
                    doorTimer = doorOpeningTime;
                    returnToMenu.onTrigger();
                }
            }

            interact = false;
            if(grabbed != null) setGrabbed(null);
        }
        else {
            if(doorTimer > 0.0) {
                doorTimer -= delta;
                if(doorTimer < 0.0) {
                    doorTimer = 0.0;
                    spawnItem(new Bell(this), cookingArea.x1 + 180.0, cookingArea.y1);
                    spawnItem(new Knife(), (cookingArea.x1 + cookingArea.x2) / 2, cookingArea.y1);
                    spawnItem(new Plate(), cookingArea.x2 - 180.0, cookingArea.y1);
                    dish = grabbables.getLast();
                }
            }

            orderTimer += delta;
            if(orderTimer > ThreadLocalRandom.current().nextDouble(8.0, 15.0)) {
                orderTimer = 0.0;
                generateOrder();
            }

            //Appraisal logic
            if(appraising) {
                if(appraisalTimer == appraisalTime) {
                    if(dish.size() == 1) setGrabbed(dish);
                    else {
                        double posX = dish.getPosX();
                        double posY = dish.getPosY() + dish.peek().getLocalPosY();
                        setGrabbed(new Grabbable(dish.pop()));
                        grabbed.setPos(posX, posY);
                    }
                }

                appraisalTimer -= delta;

                if(appraisalTimer <= 0.0) {
                    if(finishedAppraisal) {
                        appraising = false;
                        queueOffset -= orderSpacing;
                        setGrabbed(null);
                        int score = orders.poll().getValidityScore();
                        for(int i = 0; i < score; i++) {
                            spawnItem(new Coin(), engine.getScreenWidth() / 2, engine.getScreenHeight() / 2);
                        }
                    }
                    else {
                        orders.peek().validateNext(grabbed == null ? null : grabbed.peek());
                        appraisalTimer = appraisalTime;
                        if(grabbed.peek() instanceof Plate) finishedAppraisal = true;
                        else grabbables.remove(grabbed);
                    }
                }
            }

            if(queueOffset < orderSpacing * orders.size()) queueOffset += orderSpacing * 10.0 * delta;

            if(Main.debug()) {
                double midX = engine.getScreenWidth() / 2;
                double midY = engine.getScreenHeight() / 2;

                if(engine.isKeyPressed(EngineFrame.KEY_ONE)) spawnItem(new Plate(), midX, midY);
                if(engine.isKeyPressed(EngineFrame.KEY_TWO)) spawnItem(new Knife(), midX, midY);
                if(engine.isKeyPressed(EngineFrame.KEY_THREE)) spawnItem(new Bell(this), midX, midY);
                if(engine.isKeyPressed(EngineFrame.KEY_FOUR)) spawnItem(new BunBottom(), midX, midY);
                if(engine.isKeyPressed(EngineFrame.KEY_FIVE)) spawnItem(new BunTop(), midX, midY);
                if(engine.isKeyPressed(EngineFrame.KEY_SIX)) spawnItem(new LettuceChopped(), midX, midY);
                if(engine.isKeyPressed(EngineFrame.KEY_SEVEN)) spawnItem(new TomatoSlices(), midX, midY);
                if(engine.isKeyPressed(EngineFrame.KEY_ZERO)) generateOrder();
            }
        }

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

        if(!appraising) {
            for(int i = buttons.size() - 1; i >= 0; i--) {
                buttons.get(i).tryInteraction(engine);
            }
        }

        //Update logic for the grabbed grabbable
        if(grabbed != null && !appraising) {
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
        if(appraising) {
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
        double orderY = 24.0 * (1.0 - 20.0 * doorProgress);
        for(Order o : orders) {
            o.draw(engine, orderOffset, orderY);
            orderOffset -= orderSpacing;
        }

        for(Button p : buttons) {
            p.draw(engine);
        }

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

        for(Grabbable g : grabbables) {
            g.draw(engine, 1.0 - doorProgress * 1.5);
        }

        if(Main.debug()) cookingArea.draw(engine, EngineFrame.GREEN);

        super.draw();
    }
}
