package ch.epfl.moocprog;

import java.io.File;
import java.util.Objects;

import ch.epfl.moocprog.app.ApplicationInitializer;
import ch.epfl.moocprog.config.ImmutableConfigManager;
import ch.epfl.moocprog.tests.Main;
import ch.epfl.moocprog.utils.Time;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConcreteAntTest {
    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }

    @Test
    void spreadingPheromonesCorrectly() {

        Environment env = new Environment();
        ToricPosition p1 = new ToricPosition(200, 100);
        ToricPosition p2 = new ToricPosition(500, 250);
        ConcreteAnt animal = new ConcreteAnt(p1, 10, Time.fromSeconds(10), Uid.createUid());
        env.addAnt(animal);
        animal.setDirection(0.4636476090008061);
        animal.setSpeed(335.4101966249685);
        animal.move(env, Time.fromSeconds(1));
        animal.move(env, Time.fromSeconds(1));
        animal.move(env, Time.fromSeconds(1));
        Assertions.assertEquals(p2.toString(), animal.getPosition().toString());
    }


    private class ConcreteAnt extends Ant {
        private double speed;

        protected ConcreteAnt(ToricPosition toricPosition, int hitPoints, Time lifespan, Uid uid) {
            super(toricPosition, hitPoints, lifespan, uid);
        }

        @Override
        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        @Override
        void specificBehaviorDispatch(AnimalEnvironmentView env, Time dt) {

        }
    }
}
