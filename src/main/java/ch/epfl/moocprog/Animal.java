package ch.epfl.moocprog;

public abstract class Animal extends Positionable {
    private double direction;

    protected Animal(ToricPosition toricPosition) {
        super(toricPosition);
        this.direction = 0.0;
    }

    public final double getDirection() {
        return direction;
    }

    public abstract void accept(AnimalVisitor visitor, RenderingMedia s);
}
