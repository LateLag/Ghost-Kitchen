package ghostkitchen.core.customer;

import ghostkitchen.core.LayerPrefs;
import ghostkitchen.core.item.BunBottom;
import ghostkitchen.core.item.BunTop;
import ghostkitchen.core.item.Cheese;
import ghostkitchen.core.item.LettuceChopped;
import ghostkitchen.core.item.Patty;
import ghostkitchen.core.item.TomatoSlices;

public class HansBerger extends Customer {
    public HansBerger() {
        super (
            "hans_berger.png",
            103,
            new LayerPrefs(BunTop.class),
            new LayerPrefs(Patty.class, TomatoSlices.class, null),
            new LayerPrefs(Cheese.class, TomatoSlices.class, Patty.class),
            new LayerPrefs(Patty.class, null),
            new LayerPrefs(Cheese.class, LettuceChopped.class, Patty.class),
            new LayerPrefs(Patty.class),
            new LayerPrefs(BunBottom.class)
        );
    }

    @Override
    public String toString() { return "Hans Berger"; }
}
