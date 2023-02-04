package ch.epfl.moocprog;

public abstract class Ant extends Animal {
    protected Ant(ToricPosition toricPosition) {
        super(toricPosition);
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {

    }
}
