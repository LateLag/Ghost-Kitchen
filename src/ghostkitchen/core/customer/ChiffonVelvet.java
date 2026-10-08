package ghostkitchen.core.customer;

import ghostkitchen.core.LayerPrefs;
import ghostkitchen.core.item.CreamChantilly;
import ghostkitchen.core.item.CreamPastry;
import ghostkitchen.core.item.CupcakeBase;
import ghostkitchen.core.item.CupcakeRedVelvet;

public class ChiffonVelvet extends Customer {
    public ChiffonVelvet() {
        super (
            "chiffon_velvet.png",
            204, 12.0, 12.0,
            new LayerPrefs(CreamChantilly.class, CreamPastry.class),
            new LayerPrefs(CreamChantilly.class, CreamPastry.class, null),
            new LayerPrefs(CreamChantilly.class, CreamPastry.class, null),
            new LayerPrefs(CupcakeBase.class, CupcakeRedVelvet.class)
        );
    }

    @Override
    public String toString() { return "ChiffonVelvet"; }
}
