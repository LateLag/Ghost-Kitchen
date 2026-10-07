package ghostkitchen.scene.button;

import ghostkitchen.Main;
import ghostkitchen.scene.Scene;

public class SceneChanger extends Button {
    public SceneChanger(Main entry, Scene target, String image, double x, double y, double width, double height) {
        super(image, 4, x - width / 2, y - height / 2, x + width / 2, y + height / 2);

        setPressAction(() -> {
            entry.setScene(target);
        });
    }

    public SceneChanger(Main entry, Scene target, String image, double x, double y) {
        this(entry, target, image, x, y, 100.0, 100.0);
    }
}
