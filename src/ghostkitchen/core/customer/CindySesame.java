package ghostkitchen.core.customer;

import ghostkitchen.core.LayerPrefs;
import ghostkitchen.core.item.Bun;

public class CindySesame extends Customer {
    public CindySesame() {
        super (
            "cindy_sesame.png",
            62,
            new LayerPrefs(Bun.class)
        );
    }

    @Override
    public String toString() { return "CindySesame"; }
}
