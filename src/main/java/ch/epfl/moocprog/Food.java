package ch.epfl.moocprog;

public final class Food extends Positionable {
    private double quantity;

    public Food(ToricPosition position, double quantity) {
        super.setPosition(position);
        this.quantity = quantity < 0 ? 0 : quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public double takeQuantity(double quantity) {
        double taken;
        if (quantity < 0)
            throw new IllegalArgumentException("quantity must be positive");
        if (this.quantity - quantity > 0) {
            this.quantity -= quantity;
            taken = quantity;
        } else {
            taken = this.quantity;
            this.quantity = 0;
        }
        return taken;
    }

    @Override
    public String toString() {
        return getPosition().toString() + "\n" + String.format("Quantity : %.2f", getQuantity());
    }
}
