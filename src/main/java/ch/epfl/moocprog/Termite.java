package ch.epfl.moocprog;

public final class Termite extends Animal {
    public Termite(ToricPosition toricPosition) {
        super(toricPosition);
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
    }
}
