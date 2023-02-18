package ch.epfl.moocprog;

import ch.epfl.moocprog.config.ConfigManager;
import ch.epfl.moocprog.utils.Time;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.TERMITE_ATTACK_DURATION;
import static ch.epfl.moocprog.config.Config.TERMITE_HP;
import static ch.epfl.moocprog.config.Config.TERMITE_LIFESPAN;
import static ch.epfl.moocprog.config.Config.TERMITE_MAX_STRENGTH;
import static ch.epfl.moocprog.config.Config.TERMITE_MIN_STRENGTH;
import static ch.epfl.moocprog.config.Config.TERMITE_SPEED;

public final class Termite extends Animal {
    private static final ConfigManager configManager = getConfig();

    public Termite(ToricPosition toricPosition) {
        super(toricPosition, configManager.getInt(TERMITE_HP), configManager.getTime(TERMITE_LIFESPAN));
    }

    public Termite(ToricPosition toricPosition, int hitPoints, Time lifespan) {
        super(toricPosition, hitPoints, lifespan);
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
    }

    @Override
    public double getSpeed() {
        return configManager.getDouble(TERMITE_SPEED);
    }

    @Override
    void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectSpecificBehaviorDispatch(this, dt);
    }

    @Override
    RotationProbability computeRotationProbsDispatch(AnimalEnvironmentView env) {
        return env.selectComputeRotationProbsDispatch(this);
    }

    @Override
    public void afterMoveDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectAfterMoveDispatch(this, dt);
    }

    @Override
    boolean isEnemy(Animal animal) {
        return !this.isDead() && !animal.isDead() && animal.isEnemyDispatch(this);
    }

    @Override
    boolean isEnemyDispatch(Termite other) {
        return false;
    }

    @Override
    boolean isEnemyDispatch(Ant other) {
        return true;
    }

    public void seekForEnemies(AntEnvironmentView env, Time dt) {
        if (!isDead())
            move(env, dt);
    }

    public RotationProbability computeRotationProbs(TermiteEnvironmentView env) {
        return computeDefaultRotationProbs();
    }

    public void afterMoveTermite(TermiteEnvironmentView env, Time dt) {

    }

    @Override
    int getMinAttackStrength() {
        return getConfig().getInt(TERMITE_MIN_STRENGTH);
    }

    @Override
    int getMaxAttackStrength() {
        return getConfig().getInt(TERMITE_MAX_STRENGTH);
    }

    @Override
    Time getMaxAttackDuration() {
        return getConfig().getTime(TERMITE_ATTACK_DURATION);
    }
}
