package ghostkitchen.scene;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.image.ImageUtils;
import ghostkitchen.scene.button.Button;
import ghostkitchen.scene.button.SceneChanger;

public class Menu extends Scene {
    private Image rollUpDoor;
    private Image cabinetDoor;
    private Scene gameScene;
    private Button playButton;

    public Menu(EngineFrame e) {
        super(e, null, "restaurant.png");
        this.rollUpDoor = ImageUtils.loadImage("resources/images/scene/roll_up_door.png");
        this.cabinetDoor = ImageUtils.loadImage("resources/images/scene/cabinet_door.png");
        this.playButton = new SceneChanger("play.png", 768.0, 472.0, 120.0, 40.0, new Restaurant(e));
    }

    public void update(double delta) {
        if(gameScene == null) {
            Object o = playButton.tryInteraction(engine);
            if(o instanceof Scene) {
                if(o instanceof Restaurant) ((Restaurant)o).resetCookingArea();
                gameScene = (Scene)o;
            }
        }
        else gameScene.update(delta);
    }

    @Override
    public void draw() {
        if(gameScene == null) super.draw();
        else gameScene.draw();
    }

    @Override
    public void drawBefore() {
        super.drawBefore();

        double scaledWidth = rollUpDoor.getWidth() * scale;
        double scaledHeight = rollUpDoor.getHeight() * scale;

        Rectangle src = new Rectangle(0.0, 0.0, rollUpDoor.getWidth(), rollUpDoor.getHeight());
        Rectangle dest = new Rectangle((engine.getScreenWidth() - scaledWidth) / 2, 0.0, scaledWidth, scaledHeight);

        rollUpDoor.draw(engine, src, dest);
    }

    @Override
    public void drawAfter() {
        double halfStroke = 2.0 * scale;
        double scaledWidth = cabinetDoor.getWidth() * scale;
        double scaledHeight = cabinetDoor.getHeight() * scale;

        //Left cabinet door
        Rectangle src = new Rectangle(0.0, 0.0, cabinetDoor.getWidth(), cabinetDoor.getHeight());
        Rectangle dest = new Rectangle(engine.getScreenWidth() / 2 + halfStroke - scaledWidth, engine.getScreenHeight() - scaledHeight, scaledWidth, scaledHeight);
        cabinetDoor.draw(engine, src, dest);

        //Right cabinet door
        dest.x = engine.getScreenWidth() / 2 - halfStroke;
        cabinetDoor.draw(engine, src, dest, scaledWidth / 2, scaledHeight / 2, 180.0);

        playButton.draw(engine);

        super.drawAfter();
    }
}
