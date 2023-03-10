package ch.epfl.moocprog;

import java.util.List;
import java.util.stream.Stream;

import ch.epfl.moocprog.random.UniformDistribution;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Utils;
import ch.epfl.moocprog.utils.Vec2d;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANIMAL_LIFESPAN_DECREASE_FACTOR;
import static ch.epfl.moocprog.config.Config.ANIMAL_NEXT_ROTATION_DELAY;

public abstract class Animal extends Positionable {
    public enum State {
        ATTACK, IDLE, ESCAPING
    }

    private double direction;
    private int hitpoints;
    private Time lifespan;
    private Time rotationDelay;
    private Time attackDuration;
    private State state;

    protected Animal(ToricPosition toricPosition, int hitPoints, Time lifespan) {
        super(toricPosition);
        this.direction = UniformDistribution.getValue(0, 2 * Math.PI);
        this.hitpoints = hitPoints;
        this.lifespan = lifespan;
        rotationDelay = Time.ZERO;
        attackDuration = Time.ZERO;
        this.state = State.IDLE;
    }

    public abstract void accept(AnimalVisitor visitor, RenderingMedia s);

    public final void update(AnimalEnvironmentView env, Time dt) {
        double lifeSpanFactor = getConfig().getDouble(ANIMAL_LIFESPAN_DECREASE_FACTOR);
        this.lifespan = lifespan.minus(dt.times(lifeSpanFactor));
        switch (state) {
        case ATTACK:
            attack(env, dt);
            break;
        case ESCAPING:
            escape(env, dt);
            break;
        default:
            specificBehaviorDispatch(env, dt);
        }
    }

    private void attack(AnimalEnvironmentView env, Time dt) {
        if (canAttack())
            fight(env, dt);
        else {
            this.setState(State.ESCAPING);
            attackDuration = Time.ZERO;
        }
    }

    protected final void move(AnimalEnvironmentView env, Time dt) {
        rotate(env, dt);
        Vec2d vec2d = Vec2d.fromAngle(getDirection()).scalarProduct(dt.toSeconds() * getSpeed());
        setPosition(getPosition().add(vec2d));
        afterMoveDispatch(env, dt);
    }

    private void rotate(AnimalEnvironmentView env, Time dt) {
        Time delay = getConfig().getTime(ANIMAL_NEXT_ROTATION_DELAY);
        this.rotationDelay = this.rotationDelay.plus(dt);
        while (rotationDelay.compareTo(delay) >= 0) {
            this.rotationDelay = this.rotationDelay.minus(delay);
            RotationProbability rotationProbs = computeRotationProbsDispatch(env);
            double value = Utils.pickValue(rotationProbs.getAngles(), rotationProbs.getProbabilities());
            setDirection(getDirection() + value);
        }
    }

    public final boolean isDead() {
        return getHitpoints() <= 0 || Time.ZERO.compareTo(getLifespan()) >= 0;
    }

    protected final RotationProbability computeDefaultRotationProbs() {
        double[] angles = Stream.of(-180, -100, -55, -25, -10, 0, 10, 25, 55, 100, 180).map(Math::toRadians).mapToDouble(Double::doubleValue).toArray();
        double[] probs = new double[] { 0.0000, 0.0000, 0.0005, 0.0010, 0.0050, 0.9870, 0.0050, 0.0010, 0.0005, 0.0000, 0.0000 };
        return new RotationProbability(angles, probs);
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
        return getPosition().toString() + String.format("%nSpeed : %.1f", getSpeed()) + String.format("%nHitPoints : %d", hitpoints) + String.format("%nLifeSpan : %.6f",
                lifespan.toSeconds()) + String.format("%nState : %s", state.toString());
    }

    abstract void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt);

    abstract RotationProbability computeRotationProbsDispatch(AnimalEnvironmentView env);

    protected abstract void afterMoveDispatch(AnimalEnvironmentView env, Time dt);

    abstract boolean isEnemy(Animal entity);

    abstract boolean isEnemyDispatch(Termite other);

    abstract boolean isEnemyDispatch(Ant other);

    abstract int getMinAttackStrength();

    abstract int getMaxAttackStrength();

    abstract Time getMaxAttackDuration();

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean canAttack() {
        return !State.ESCAPING.equals(getState()) && attackDuration.compareTo(getMaxAttackDuration()) <= 0;
    }

    public void escape(AnimalEnvironmentView env, Time dt) {
        move(env, dt);
        if (!env.isVisibleFromEnemies(this))
            setState(State.IDLE);
    }

    public void fight(AnimalEnvironmentView env, Time dt) {
        List<Animal> enemies = env.getVisibleEnemiesForAnimal(this);
        if (!enemies.isEmpty()) {
            Animal closest = Utils.closestFromPoint(this, enemies);
            if (closest != null) {
                closest.setState(State.ATTACK);
                this.setState(State.ATTACK);
                int damage = (int) UniformDistribution.getValue(getMinAttackStrength(), getMaxAttackStrength());
                closest.hitpoints -= damage;
                closest.attackDuration = closest.attackDuration.plus(dt);
            } else {
                escaping();
            }
        } else {
            escaping();
        }
    }

    private void escaping() {
        this.attackDuration = Time.ZERO;
        if (State.ATTACK.equals(this.getState()))
            this.setState(State.ESCAPING);
    }
}
