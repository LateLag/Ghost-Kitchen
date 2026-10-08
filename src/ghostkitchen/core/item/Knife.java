package ghostkitchen.core.item;

import java.util.List;

public class Knife extends Transformer {
    public Knife() {
        super("knife.png", 120, 12, -6, 12, false);
        addTransform(Bun.class, List.of(BunBottom.class, BunTop.class));
        addTransform(Lettuce.class, List.of(LettuceChopped.class, LettuceChopped.class, LettuceChopped.class, LettuceChopped.class, LettuceChopped.class));
        addTransform(Tomato.class, List.of(TomatoSlices.class, TomatoSlices.class, TomatoSlices.class));
    }

    @Override
    public String toString() { return "Knife"; }
}
