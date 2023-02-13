package ch.epfl.moocprog;

import ch.epfl.moocprog.config.ConfigManager;
import ch.epfl.moocprog.utils.Time;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_HP;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_LIFESPAN;
import static ch.epfl.moocprog.config.Config.ANT_WORKER_SPEED;

public final class AntWorker extends Ant {

    private static final ConfigManager config = getConfig();
    private double foodQuantity;

    public AntWorker(ToricPosition toricPosition, Uid uid) {
        super(toricPosition, config.getInt(ANT_WORKER_HP), config.getTime(ANT_WORKER_LIFESPAN), uid);
        this.foodQuantity = 0;
    }

    public AntWorker(ToricPosition toricPosition, int hitPoints, Time lifespan, Uid uid) {
        super(toricPosition, hitPoints, lifespan, uid);
        this.foodQuantity = 0;
    }


    @Override
    public void accept(AnimalVisitor visitor, RenderingMedia s) {
        visitor.visit(this, s);
    }

    @Override
    public double getSpeed() {
        return config.getDouble(ANT_WORKER_SPEED);
    }

    public final double getFoodQuantity() {
        return foodQuantity;
    }

    protected void seekForFood(AntWorkerEnvironmentView env, Time dt) {
        if (!isDead())
            move(dt);
    }

    @Override
    public String toString() {
        return super.toString() + String.format("%nQuantity : %.2f", getFoodQuantity());
    }

    @Override
    void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt) {
        env.selectSpecificBehaviorDispatch(this, dt);
    }
}
