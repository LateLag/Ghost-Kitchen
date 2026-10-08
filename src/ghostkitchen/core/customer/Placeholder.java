package ghostkitchen.core.customer;

import ghostkitchen.core.LayerPrefs;
import ghostkitchen.core.item.BunBottom;
import ghostkitchen.core.item.BunTop;
import ghostkitchen.core.item.Cone;
import ghostkitchen.core.item.IceCreamStrawberry;
import ghostkitchen.core.item.Patty;

public class Placeholder extends Customer {
    public Placeholder() {
        super (
            "customer.png",
            88,
            new LayerPrefs(BunTop.class, IceCreamStrawberry.class),
            new LayerPrefs(Patty.class, IceCreamStrawberry.class, null),
            new LayerPrefs(BunBottom.class, Cone.class)
        );
    }

    @Override
    public String toString() { return "Placeholder"; }
}
