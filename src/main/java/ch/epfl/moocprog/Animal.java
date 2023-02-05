package ch.epfl.moocprog;

import ch.epfl.moocprog.random.UniformDistribution;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Vec2d;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANIMAL_LIFESPAN_DECREASE_FACTOR;

public abstract class Animal extends Positionable {
    private double direction;
    private int hitpoints;
    private Time lifespan;

    protected Animal(ToricPosition toricPosition, int hitPoints, Time lifespan) {
        super(toricPosition);
        this.direction = UniformDistribution.getValue(0, 2 * Math.PI);
        this.hitpoints = hitPoints;
        this.lifespan = lifespan;
    }

    public abstract void accept(AnimalVisitor visitor, RenderingMedia s);

    public void update(AnimalEnvironmentView env, Time dt) {
        double lifeSpanFactor = getConfig().getDouble(ANIMAL_LIFESPAN_DECREASE_FACTOR);
        this.lifespan = lifespan.minus(dt.times(lifeSpanFactor));
        if (!isDead())
            move(dt);
    }

    protected final void move(Time dt) {
        Vec2d vec2d = Vec2d.fromAngle(getDirection()).scalarProduct(dt.toSeconds() * getSpeed());
        setPosition(getPosition().add(vec2d));
    }

    public final boolean isDead() {
        return hitpoints <= 0 || lifespan.toMilliseconds() <= 0;
    }

    public final double getDirection() {
        return direction;
    }

    public final void setDirection(double direction) {
        this.direction = direction;
    }

    public final int getHitpoints() {
        return hitpoints;
    }

    public final Time getLifespan() {
        return lifespan;
    }

    public abstract double getSpeed();

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getPosition().toString()).append("\n");
        str.append(String.format("Speed : %.1f", getSpeed())).append("\n");
        str.append(String.format("HitPoints : %d", hitpoints)).append("\n");
        str.append(String.format("LifeSpan : %.6f", lifespan.toSeconds()));
        return str.toString();
    }
}
