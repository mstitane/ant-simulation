package ch.epfl.moocprog;

import ch.epfl.moocprog.config.Config;
import ch.epfl.moocprog.utils.Time;

import static ch.epfl.moocprog.app.Context.getConfig;

public final class Pheromone extends Positionable {
    private double quantity;

    public Pheromone(ToricPosition toricPosition) {
        super(toricPosition);
        this.quantity = 0.0;
    }

    public Pheromone(ToricPosition toricPosition, double quantity) {
        super(toricPosition);
        this.quantity = quantity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getFoodQuantity() {
        return 0.0;
    }

    public boolean isNegligible() {
        return quantity < getConfig().getDouble(Config.PHEROMONE_THRESHOLD);
    }

    public void update(Time dt) {
        double rate = getConfig().getDouble(Config.PHEROMONE_EVAPORATION_RATE);
        if (!isNegligible()) {
            this.quantity -= dt.toSeconds() * rate;
        }
        if (quantity < 0)
            this.quantity = 0;
    }
}
