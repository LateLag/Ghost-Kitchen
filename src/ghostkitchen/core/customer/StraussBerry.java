package ghostkitchen.core.customer;

import ghostkitchen.core.LayerPrefs;
import ghostkitchen.core.item.Cone;
import ghostkitchen.core.item.CreamChantilly;
import ghostkitchen.core.item.IceCreamChocolate;
import ghostkitchen.core.item.IceCreamStrawberry;

public class StraussBerry extends Customer {
    public StraussBerry() {
        super (
            "strauss_berry.png",
            84,
            new LayerPrefs(IceCreamChocolate.class, CreamChantilly.class, null),
            new LayerPrefs(IceCreamStrawberry.class),
            new LayerPrefs(IceCreamStrawberry.class, null),
            new LayerPrefs(IceCreamStrawberry.class, null),
            new LayerPrefs(IceCreamStrawberry.class, null),
            new LayerPrefs(IceCreamStrawberry.class, null),
            new LayerPrefs(Cone.class)
        );
    }

    @Override
    public String toString() { return "Strauss Berry"; }
}
