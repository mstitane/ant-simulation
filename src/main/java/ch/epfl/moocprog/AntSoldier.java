package ch.epfl.moocprog;

import ch.epfl.moocprog.config.ConfigManager;
import ch.epfl.moocprog.utils.Time;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_HP;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_LIFESPAN;
import static ch.epfl.moocprog.config.Config.ANT_SOLDIER_SPEED;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_HP;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_LIFESPAN;

public final class AntSoldier extends Ant {
    private static final ConfigManager config = getConfig();

    public AntSoldier(ToricPosition tp, Uid hillId) {
        super(tp, getConfig().getInt(ANT_SOLDIER_HP), getConfig().getTime(ANT_SOLDIER_LIFESPAN), hillId);
    }

    public AntSoldier(ToricPosition tp, Uid hillId, AntRotationProbabilityModel probabilityModel) {
        super(tp, getConfig().getInt(ANT_SOLDIER_HP), getConfig().getTime(ANT_SOLDIER_LIFESPAN), hillId, probabilityModel);
    }

    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
    }

    @Override
    public double getSpeed() {
        return 1 + config.getDouble(ANT_SOLDIER_SPEED);
    }

    @Override
    void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectSpecificBehaviorDispatch(this, dt);
    }

    protected void seekForEnemies(AntEnvironmentView env, Time dt) {
        if (!isDead())
            move(env, dt);
    }


}
