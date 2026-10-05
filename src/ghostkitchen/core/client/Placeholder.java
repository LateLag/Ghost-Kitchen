package ghostkitchen.core.client;

import ghostkitchen.core.LayerPrefs;
import ghostkitchen.core.item.BunBottom;
import ghostkitchen.core.item.BunTop;
import ghostkitchen.core.item.Cone;
import ghostkitchen.core.item.IceCreamStrawberry;
import ghostkitchen.core.item.Patty;

public class Placeholder extends Client {
    public Placeholder() {
        super (
            "client.png",
            new LayerPrefs(BunTop.class, IceCreamStrawberry.class),
            new LayerPrefs(Patty.class, IceCreamStrawberry.class, null),
            new LayerPrefs(BunBottom.class, Cone.class)
        );
    }
}
