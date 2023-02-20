package ch.epfl.moocprog;

import ch.epfl.moocprog.config.Config;
import ch.epfl.moocprog.config.ConfigManager;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Vec2d;

import static ch.epfl.moocprog.app.Context.getConfig;

public abstract class Ant extends Animal {

    private final Uid anthillId;
    private ToricPosition lastPos;
    private AntRotationProbabilityModel probModel;

    protected Ant(ToricPosition toricPosition, int hitPoints, Time lifespan, Uid uid) {
        super(toricPosition, hitPoints, lifespan);
        this.anthillId = uid;
        lastPos = toricPosition;
        probModel = new PheromoneRotationProbabilityModel();
    }

    protected Ant(ToricPosition toricPosition, int hitPoints, Time lifespan, Uid uid, AntRotationProbabilityModel probModel) {
        super(toricPosition, hitPoints, lifespan);
        this.anthillId = uid;
        lastPos = toricPosition;
        this.probModel = probModel;
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
        return probModel.computeRotationProbs(computeDefaultRotationProbs(), getPosition(), getDirection(), env);
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

    @Override
    boolean isEnemy(Animal animal) {
        return !this.isDead() && !animal.isDead() && animal.isEnemyDispatch(this);
    }

    @Override
    protected final boolean isEnemyDispatch(Termite other) {
        return true;
    }

    @Override
    protected final boolean isEnemyDispatch(Ant other) {
        return other.anthillId != this.anthillId;
    }
}
