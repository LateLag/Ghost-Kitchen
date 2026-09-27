package ghostkitchen.core.client;

import java.util.List;

import ghostkitchen.core.ingredient.BunBottom;
import ghostkitchen.core.ingredient.BunTop;
import ghostkitchen.core.ingredient.Cone;
import ghostkitchen.core.ingredient.IceCreamStrawberry;
import ghostkitchen.core.ingredient.Patty;

public class Placeholder extends Client {
    public Placeholder() {
        super (
            "client.png",
            List.of(BunBottom.class, Cone.class),
            List.of(Patty.class, IceCreamStrawberry.class),
            List.of(BunTop.class, IceCreamStrawberry.class)
        );
    }
}
