package ghostkitchen.scene;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.Main;
import ghostkitchen.scene.button.Button;
import ghostkitchen.scene.button.SceneChanger;

public class Menu extends Scene {
    private Image restaurant;
    private Image rollUpDoor;
    private Image cabinetDoor;
    private Button playButton;

    public Menu(EngineFrame e) {
        super(e);
        this.restaurant = ImageUtils.loadImage(imgPath + "restaurant.png");
        this.rollUpDoor = ImageUtils.loadImage(imgPath + "roll_up_door.png");
        this.cabinetDoor = ImageUtils.loadImage(imgPath + "cabinet_door.png");
    }

    @Override
    public void setup() {
        playButton = new SceneChanger((Main)engine, new Restaurant(engine), "generic.png", engine.getScreenWidth() / 2, engine.getScreenHeight() * 121.0 / 216.0, 120.0, 40.0);
    }

    @Override
    public void update(double delta) {
        playButton.tryInteraction(engine);
    }

    @Override
    public void draw() {
        //Roll up door
        Rectangle src = new Rectangle(0.0, 0.0, rollUpDoor.getWidth(), rollUpDoor.getHeight());
        Rectangle dest = new Rectangle((engine.getScreenWidth() - rollUpDoor.getWidth() * scale) / 2, 0.0, rollUpDoor.getWidth() * scale, rollUpDoor.getHeight() * scale);
        rollUpDoor.draw(engine, src, dest);

        //Background
        src = new Rectangle(0.0, 0.0, restaurant.getWidth(), restaurant.getHeight());
        dest = new Rectangle(0.0, 0.0, engine.getScreenWidth(), engine.getScreenHeight());
        restaurant.draw(engine, src, dest);

        //Cabinet doors
        double halfStroke = 2.0 * scale;
        double scaledWidth = cabinetDoor.getWidth() * scale;
        double scaledHeight = cabinetDoor.getHeight() * scale;

        //Left cabinet door
        src = new Rectangle(0.0, 0.0, cabinetDoor.getWidth(), cabinetDoor.getHeight());
        dest = new Rectangle(engine.getScreenWidth() / 2 + halfStroke - scaledWidth, engine.getScreenHeight() - scaledHeight, scaledWidth, scaledHeight);
        cabinetDoor.draw(engine, src, dest);

        //Right cabinet door
        dest.x = engine.getScreenWidth() / 2 - halfStroke;
        cabinetDoor.draw(engine, src, dest, scaledWidth / 2, scaledHeight / 2, 180.0);

        playButton.draw(engine);

        super.draw();
    }
}
