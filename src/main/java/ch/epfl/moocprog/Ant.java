package ch.epfl.moocprog;

import ch.epfl.moocprog.utils.Time;

public abstract class Ant extends Animal {

    private final Uid anthillId;

    protected Ant(ToricPosition toricPosition, int hitPoints, Time lifespan, Uid uid) {
        super(toricPosition, hitPoints, lifespan);
        this.anthillId = uid;
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {

    }

    public final Uid getAnthillId() {
        return this.anthillId;
    }
}
