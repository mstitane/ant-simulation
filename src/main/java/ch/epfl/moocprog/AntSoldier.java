package ch.epfl.moocprog;

public final class AntSoldier extends Ant {
    public AntSoldier(ToricPosition toricPosition) {
        super(toricPosition);
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
    }

    @Override
    public double getSpeed() {
        return 0;
    }
}
