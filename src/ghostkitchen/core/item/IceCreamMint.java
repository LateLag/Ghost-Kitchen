package ghostkitchen.core.item;

public class IceCreamMint extends Item {
    public IceCreamMint() {
        super("icecream_mint.png", 42, 40, -14, 28, true);
    }

    @Override
    public String toString() { return "Mint Ice Cream"; }
}
