package ghostkitchen.core.item;

import java.util.List;

public class Knife extends Transformer {
    public Knife() {
        super("knife.png", 120, 12, -8);
        addTransform(Bun.class, List.of(BunBottom.class, BunTop.class));
    }

    @Override
    public String toString() { return "Knife"; }
}
