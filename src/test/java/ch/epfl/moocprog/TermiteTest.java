package ch.epfl.moocprog;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import ch.epfl.moocprog.app.ApplicationInitializer;
import ch.epfl.moocprog.config.ConfigManager;
import ch.epfl.moocprog.config.ImmutableConfigManager;
import ch.epfl.moocprog.tests.Main;
import ch.epfl.moocprog.utils.Time;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.*;
import static org.junit.jupiter.api.Assertions.*;

public class TermiteTest {
    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }

    @Test
    void isSpeedDynamicallyFetched() {
        Animal animal = new Termite(new ToricPosition(10, 10));
        assertEquals(101.0, animal.getSpeed());
    }

    @Test
    void attributesAreCorrectlyInitialized() {
        Animal animal = new Termite(new ToricPosition(10, 10), 15, Time.ZERO);
        assertEquals(15, animal.getHitpoints());
    }

    @Test
    void isDeadReturnsTrueForNegativeHpOrLs() {
        Animal animal = new Termite(new ToricPosition(10, 10), -15, Time.fromSeconds(133));
        Assertions.assertTrue(animal.isDead());
    }

    @Test
    void hitpointsAndLifespanAreCorrect() {
        Animal animal = new Termite(new ToricPosition(10, 10), 225, Time.fromSeconds(110));
        assertEquals(225, animal.getHitpoints());
    }

    @Test
    void isDeadReturnsTrueForNullHpOrLs() {
        Animal animal = new Termite(new ToricPosition(10, 10), 0, Time.fromSeconds(1));
        Assertions.assertTrue(animal.isDead());
    }

    @Test
    void step13OK() {
        ToricPosition termitePosition = new ToricPosition(20, 30);
        Termite termit = new Termite(termitePosition);
        ToricPosition positionBeforeUpdate = termitePosition;
        Environment env = new Environment();
        env.addAnimal(termit);
        env.update(Time.fromSeconds(1.));
        // on teste si la termite est à nouveau capable de se déplacer
        boolean hasMoved = !positionBeforeUpdate.equals(termit.getPosition());
        Assertions.assertTrue(hasMoved);
        // on vérifie les probabilités de rotation
        RotationProbability rotProbs = env.selectComputeRotationProbsDispatch(termit);
        assertEquals(
                "[-3.141592653589793, -1.7453292519943295, -0.9599310885968813, -0.4363323129985824, -0.17453292519943295, 0.0, 0.17453292519943295, 0.4363323129985824, 0.9599310885968813, 1.7453292519943295, 3.141592653589793]",
                Arrays.toString(rotProbs.getAngles()));
        assertEquals("[0.0, 0.0, 5.0E-4, 0.001, 0.005, 0.987, 0.005, 0.001, 5.0E-4, 0.0, 0.0]", Arrays.toString(rotProbs.getProbabilities()));
        // On vérifie les tests sur les ennemis
        Termite termit2 = new Termite(termitePosition);
        env.addAnimal(termit2);
        assertFalse(termit2.isEnemy(termit));
        Anthill anthill1 = new Anthill(new ToricPosition(10, 20));
        AntWorker worker1 = new AntWorker(new ToricPosition(22, 28), anthill1.getAnthillId());
        env.addAnimal(worker1);
        assertTrue(termit2.isEnemy(worker1));

        assertTrue(env.isVisibleFromEnemies(worker1));
        assertEquals(1 , env.getVisibleEnemiesForAnimal(worker1).size());

        ConfigManager config = getConfig();
        assertEquals(termit2.getMinAttackStrength(), config.getInt(TERMITE_MIN_STRENGTH));
        assertEquals(termit2.getMaxAttackStrength(), config.getInt(TERMITE_MAX_STRENGTH));
        assertEquals(termit2.getMaxAttackDuration(), config.getTime(TERMITE_ATTACK_DURATION));
    }
}
