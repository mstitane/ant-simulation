package ch.epfl.moocprog;

import ch.epfl.moocprog.config.Config;
import ch.epfl.moocprog.random.UniformDistribution;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Utils;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANTHILL_WORKER_PROB_DEFAULT;

public final class Anthill extends Positionable {

    private Time counter;
    private double foodQuantity;
    private double probability;
    private final Uid anthillId;

    public Anthill(ToricPosition toricPosition) {
        super(toricPosition);
        this.foodQuantity = 0;
        this.anthillId = Uid.createUid();
        this.probability = getConfig().getDouble(ANTHILL_WORKER_PROB_DEFAULT);
        this.counter = Time.ZERO;
    }

    public Anthill(ToricPosition toricPosition, double foodQuantity) {
        super(toricPosition);
        this.foodQuantity = foodQuantity;
        this.anthillId = Uid.createUid();
        this.probability = getConfig().getDouble(ANTHILL_WORKER_PROB_DEFAULT);
        this.counter = Time.ZERO;
    }

    public void update(AnthillEnvironmentView env, Time dt) {
        Time delay = getConfig().getTime(Config.ANTHILL_SPAWN_DELAY);
        double value = UniformDistribution.getValue(0, 1);
        this.counter = this.counter.plus(dt);
        while (counter.compareTo(delay) >= 0) {
            if (value <= probability) {
                env.addAnt(new AntWorker(getPosition(), getAnthillId()));
            } else {
                env.addAnt(new AntSoldier(getPosition(), getAnthillId()));
            }
            this.counter = this.counter.minus(delay);
        }
    }

    public void dropFood(double toDrop) {
        Utils.require(toDrop > 0);
        foodQuantity += toDrop;
    }

    public double getQuantity() {
        return 0.0;
    }

    public double getFoodQuantity() {
        return foodQuantity;
    }

    public Uid getAnthillId() {
        return anthillId;
    }

    @Override
    public String toString() {
        return getPosition().toString() + String.format("%nQuantity : %.2f", getFoodQuantity());
    }
}
