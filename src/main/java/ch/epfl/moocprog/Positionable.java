package ch.epfl.moocprog;

import java.util.StringJoiner;

public class Positionable {

    private ToricPosition position;

    public Positionable() {
        position = new ToricPosition();
    }

    public Positionable(ToricPosition toricPosition) {
        position = toricPosition;
    }

    public ToricPosition getPosition() {
        return position;
    }

    protected final void setPosition(ToricPosition position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
