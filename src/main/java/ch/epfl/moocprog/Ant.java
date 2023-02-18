package ch.epfl.moocprog;

import ch.epfl.moocprog.app.Context;
import ch.epfl.moocprog.config.Config;
import ch.epfl.moocprog.config.ConfigManager;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Vec2d;

import static ch.epfl.moocprog.app.Context.getConfig;

public abstract class Ant extends Animal {

    private final Uid anthillId;
    private ToricPosition lastPos;

    protected Ant(ToricPosition toricPosition, int hitPoints, Time lifespan, Uid uid) {
        super(toricPosition, hitPoints, lifespan);
        this.anthillId = uid;
        lastPos = toricPosition;
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {

    }

    public final Uid getAnthillId() {
        return this.anthillId;
    }

    private void spreadPheromones(AntEnvironmentView env) {
        ConfigManager config = getConfig();
        double density = config.getDouble(Config.ANT_PHEROMONE_DENSITY);
        double d = lastPos.toricDistance(getPosition());
        int nbInstances = (int) (d * density);
        double quantity = config.getDouble(Config.ANT_PHEROMONE_ENERGY);
        Vec2d pas = lastPos.toricVector(getPosition()).normalized().scalarProduct(d/nbInstances);
        while (nbInstances-- > 0) {
            lastPos = lastPos.add(pas);
            env.addPheromone(new Pheromone(lastPos, quantity));
        }
    }

    public final RotationProbability computeRotationProbs(AntEnvironmentView env) {
        return computeDefaultRotationProbs();
    }

    @Override
    public final RotationProbability computeRotationProbsDispatch(AnimalEnvironmentView env) {
        return env.selectComputeRotationProbsDispatch(this);
    }

    final void afterMoveAnt(AntEnvironmentView env, Time dt) {
        spreadPheromones(env);
    }

    @Override
    protected final void afterMoveDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectAfterMoveDispatch(this, dt);
    }
}
