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

public class AntWorkerTest {
    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }

    @Test
    void isSpeedDynamicallyFetched() {
        Animal animal = new AntWorker(new ToricPosition(10, 10), Uid.createUid());
        Assertions.assertEquals(101.0, animal.getSpeed());
    }

    @Test
    void attributesAreCorrectlyInitialized() {
        Animal animal = new AntWorker(new ToricPosition(10, 10), 15, Time.ZERO, Uid.createUid());
        Assertions.assertEquals(15, animal.getHitpoints());
    }

    @Test
    void isDeadReturnsTrueForNegativeHpOrLs() {
        Animal animal = new AntWorker(new ToricPosition(10, 10), -15, Time.fromSeconds(133), Uid.createUid());
        Assertions.assertTrue(animal.isDead());
    }

    @Test
    void hitpointsAndLifespanAreCorrect() {
        Animal animal = new AntWorker(new ToricPosition(10, 10), 225, Time.fromSeconds(110), Uid.createUid());
        Assertions.assertEquals(225, animal.getHitpoints());
    }

    @Test
    void isDeadReturnsTrueForNullHpOrLs() {
        Animal animal = new AntWorker(new ToricPosition(10, 10), 0, Time.fromSeconds(1), Uid.createUid());
        Assertions.assertTrue(animal.isDead());
    }
}
