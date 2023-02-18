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

public class AntSoldierTest {
    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }

    @Test
    void isSpeedDynamicallyFetched() {
        Animal animal = new AntSoldier(new ToricPosition(10, 10), Uid.createUid());
        Assertions.assertEquals(101.0, animal.getSpeed());
    }

    @Test
    void attributesAreCorrectlyInitialized() {
        Animal animal = new AntSoldier(new ToricPosition(10, 10), Uid.createUid());
        Assertions.assertEquals(500, animal.getHitpoints());
    }

    @Test
    void isDeadReturnsTrueForNegativeHpOrLs() {
        Animal animal = new AntSoldier(new ToricPosition(10, 10), Uid.createUid());
        Assertions.assertTrue(animal.isDead());
    }

    @Test
    void hitpointsAndLifespanAreCorrect() {
        Animal animal = new AntSoldier(new ToricPosition(10, 10), Uid.createUid());
        Assertions.assertEquals(225, animal.getHitpoints());
    }

    @Test
    void isDeadReturnsTrueForNullHpOrLs() {
        Animal animal = new AntSoldier(new ToricPosition(10, 10), Uid.createUid());
        Assertions.assertTrue(animal.isDead());
    }

    @Test
    void spreadingPheromonesCorrectly() {

        Environment env = new Environment();
        ToricPosition p1 = new ToricPosition(200, 100);
        ToricPosition p2 = new ToricPosition(500, 250);
        Ant animal = new AntSoldier(p1, Uid.createUid());
        env.addAnt(animal);
        animal.setDirection(0.4636476090008061);
        animal.update(env, Time.fromSeconds(1));
        Assertions.assertEquals(new ToricPosition(290.3, 145.2).toString(), animal.getPosition().toString());
    }
}
