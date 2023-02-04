package ch.epfl.moocprog;

import ch.epfl.moocprog.config.ConfigManager;
import ch.epfl.moocprog.random.NormalDistribution;
import ch.epfl.moocprog.random.UniformDistribution;
import ch.epfl.moocprog.utils.Time;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.FOOD_GENERATOR_DELAY;
import static ch.epfl.moocprog.config.Config.NEW_FOOD_QUANTITY_MAX;
import static ch.epfl.moocprog.config.Config.NEW_FOOD_QUANTITY_MIN;
import static ch.epfl.moocprog.config.Config.WORLD_HEIGHT;
import static ch.epfl.moocprog.config.Config.WORLD_WIDTH;

public final class FoodGenerator {
    private Time counter;
    private final ConfigManager config = getConfig();

    public FoodGenerator() {
        this.counter = Time.ZERO;
    }

    public void update(FoodGeneratorEnvironmentView env, Time dt) {
        Time delay = config.getTime(FOOD_GENERATOR_DELAY);
        double min = config.getDouble(NEW_FOOD_QUANTITY_MIN);
        double max = config.getDouble(NEW_FOOD_QUANTITY_MAX);
        int width = config.getInt(WORLD_WIDTH);
        int height = config.getInt(WORLD_HEIGHT);
        this.counter = this.counter.plus(dt);
        while (counter.compareTo(delay) >= 0) {
            this.counter = this.counter.minus(delay);
            double quantity = UniformDistribution.getValue(min, max);
            double mu = width / 2.0;
            double sigma2 = (width * width) / 16.0;
            double x = NormalDistribution.getValue(mu, sigma2);
            mu = height / 2.0;
            sigma2 = (height * height) / 16.0;
            double y = NormalDistribution.getValue(mu, sigma2);
            env.addFood(new Food(new ToricPosition(x, y), quantity));
        }
    }
}
