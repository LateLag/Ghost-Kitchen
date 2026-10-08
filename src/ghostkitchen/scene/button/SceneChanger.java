package ghostkitchen.scene.button;

import ghostkitchen.Main;
import ghostkitchen.scene.Scene;

public class SceneChanger extends Button {
    public SceneChanger(double x, double y, double width, double height, Main entry, Scene target) {
        super("generic.png", 4, x - width / 2, y - height / 2, x + width / 2, y + height / 2);

        setPressAction(() -> {
            entry.setScene(target);
        });
    }

    public SceneChanger(double x, double y, Main entry, Scene target) {
        this(x, y, 100.0, 100.0, entry, target);
    }
}
