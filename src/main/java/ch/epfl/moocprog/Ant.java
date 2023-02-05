package ch.epfl.moocprog;

import ch.epfl.moocprog.utils.Time;

public abstract class Ant extends Animal {
    protected Ant(ToricPosition toricPosition) {
        super(toricPosition, 0, Time.ZERO);
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {

    }
}
