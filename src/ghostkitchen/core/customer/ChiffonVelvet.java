package ghostkitchen.core.customer;

import ghostkitchen.core.LayerPrefs;
import ghostkitchen.core.item.CreamChantilly;
import ghostkitchen.core.item.CreamPastry;
import ghostkitchen.core.item.CupcakeBase;
import ghostkitchen.core.item.CupcakeRedVelvet;
import ghostkitchen.core.item.IceCreamMint;

public class ChiffonVelvet extends Customer {
    public ChiffonVelvet() {
        super (
            "chiffon_velvet.png",
            105,
            new LayerPrefs(CreamChantilly.class, CreamPastry.class),
            new LayerPrefs(CreamChantilly.class, CreamPastry.class, null),
            new LayerPrefs(CreamChantilly.class, CreamPastry.class, IceCreamMint.class, null),
            new LayerPrefs(CupcakeBase.class, CupcakeRedVelvet.class)
        );
    }

    @Override
    public String toString() { return "ChiffonVelvet"; }
}
