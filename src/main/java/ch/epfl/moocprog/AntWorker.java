package ch.epfl.moocprog;

public final class AntWorker extends Ant {
    public AntWorker(ToricPosition toricPosition) {
        super(toricPosition);
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
    }
}
