package ghostkitchen.core.item;

public class CupcakeBase extends Item {
    public CupcakeBase() {
        super("cupcake_base.png", 48, 40, -18, 32, true);
    }

    @Override
    public String toString() { return "Cupcake"; }
}
