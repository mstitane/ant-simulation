package ch.epfl.moocprog;

import ch.epfl.moocprog.config.ConfigManager;
import ch.epfl.moocprog.utils.Time;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.TERMITE_HP;
import static ch.epfl.moocprog.config.Config.TERMITE_LIFESPAN;
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

    }
}
