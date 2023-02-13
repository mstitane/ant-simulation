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

public class TermiteTest {
    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }

    @Test
    void isSpeedDynamicallyFetched() {
        Animal animal = new Termite(new ToricPosition(10, 10));
        Assertions.assertEquals(101.0, animal.getSpeed());
    }

    @Test
    void attributesAreCorrectlyInitialized() {
        Animal animal = new Termite(new ToricPosition(10, 10), 15, Time.ZERO);
        Assertions.assertEquals(15, animal.getHitpoints());
    }

    @Test
    void isDeadReturnsTrueForNegativeHpOrLs() {
        Animal animal = new Termite(new ToricPosition(10, 10), -15, Time.fromSeconds(133));
        Assertions.assertTrue(animal.isDead());
    }

    @Test
    void hitpointsAndLifespanAreCorrect() {
        Animal animal = new Termite(new ToricPosition(10, 10), 225, Time.fromSeconds(110));
        Assertions.assertEquals(225, animal.getHitpoints());
    }

    @Test
    void isDeadReturnsTrueForNullHpOrLs() {
        Animal animal = new Termite(new ToricPosition(10, 10), 0, Time.fromSeconds(1));
        Assertions.assertTrue(animal.isDead());
    }
}
