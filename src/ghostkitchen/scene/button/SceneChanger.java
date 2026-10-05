package ghostkitchen.scene.button;

import ghostkitchen.scene.Scene;

public class SceneChanger extends Button {
    Scene scene;

    public SceneChanger(String image, double x, double y, Scene scene) {
        this(image, x, y, 100.0, 100.0, scene);
    }

    public SceneChanger(String image, double x, double y, double width, double height, Scene scene) {
        super(image, 4, x - width / 2, y - height / 2, x + width / 2, y + height / 2);
        this.scene = scene;
    }

    @Override
    public Object onClick() {
        return scene;
    }

    @Override
    public Object onRelease() {
        return null;
    }
}
