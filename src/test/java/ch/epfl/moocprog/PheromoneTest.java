package ch.epfl.moocprog;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ch.epfl.moocprog.app.ApplicationInitializer;
import ch.epfl.moocprog.config.ImmutableConfigManager;
import ch.epfl.moocprog.tests.Main;
import ch.epfl.moocprog.utils.Time;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.PHEROMONE_THRESHOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PheromoneTest {
    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }

    @Test
    void isGetPheromoneQuantitiesPerIntervalForAntWorking() {
        double minQty = getConfig().getDouble(PHEROMONE_THRESHOLD);
        Pheromone pher1 = new Pheromone(new ToricPosition(10., 10.), minQty);

        assertEquals(0.01, minQty);
        assertEquals(new ToricPosition(10.0, 10.0).toString(), pher1.getPosition().toString());
        assertEquals(0.01, minQty);
        assertTrue((pher1.getQuantity() == minQty));
        assertFalse(pher1.isNegligible());
        Environment env = new Environment();
        env.addPheromone(pher1);
        env.update(Time.fromSeconds(1.));
        assertEquals(0.0, pher1.getQuantity());
        double offset = minQty / 5.;

        Pheromone pher2 = new Pheromone(new ToricPosition(20., 20.), minQty - offset);
        assertEquals(0.002, offset);
        assertEquals(new ToricPosition(20.0, 20.0).toString(), pher2.getPosition().toString());
        assertTrue(pher2.isNegligible());
        env.addPheromone(pher2);
        List<Double> doubles = Arrays.asList(0.0D, 0.008D);
        assertTrue(Arrays.deepEquals(doubles.toArray(), env.getPheromonesQuantities().toArray()));
        env.update(Time.fromSeconds(1.));
        assertTrue(env.getPheromonesQuantities().isEmpty());

        ToricPosition antPosition = new ToricPosition(100., 100.);
        Pheromone pher3 = new Pheromone(new ToricPosition(105., 105.), 1.0);
        Pheromone pher4 = new Pheromone(new ToricPosition(95., 95.), 2.0);
        // cette quantité est trop éloignée (ne doit pas être perçue) :
        Pheromone pher5 = new Pheromone(new ToricPosition(500., 500.), 4.0);
        env.addPheromone(pher3);
        env.addPheromone(pher4);
        env.addPheromone(pher5);

        List<Double> doubleList = Arrays.asList(1.0, 2.0, 4.0);
        assertTrue(Arrays.deepEquals(doubleList.toArray(), env.getPheromonesQuantities().toArray()));

        double[] pheromonesAroundPosition = env.getPheromoneQuantitiesPerIntervalForAnt(antPosition, 0.,
                new double[] { Math.toRadians(-180), Math.toRadians(-100), Math.toRadians(-55), Math.toRadians(-25), Math.toRadians(-10), Math.toRadians(0), Math.toRadians(10),
                        Math.toRadians(25), Math.toRadians(55), Math.toRadians(100), Math.toRadians(180) });
        assertEquals("[0.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0]", Arrays.toString(pheromonesAroundPosition));
        env.update(Time.fromSeconds(30.));
        doubleList = Arrays.asList(0.0, 0.0, 0.0);
        assertTrue(Arrays.deepEquals(doubleList.toArray(), env.getPheromonesQuantities().toArray()));

    }

    @Test
    void getPheromoneQuantitiesPerIntervalForAntThrowsIfArrayIsNull() {
        ToricPosition antPosition = new ToricPosition(100., 100.);
        Environment env = new Environment();

        assertThrows(IllegalArgumentException.class, () -> env.getPheromoneQuantitiesPerIntervalForAnt(antPosition, 0., null));
    }
    @Test
    void getPheromoneQuantitiesPerIntervalForAntThrowsIfTPIsNull() {
        Environment env = new Environment();

        assertThrows(IllegalArgumentException.class, () -> env.getPheromoneQuantitiesPerIntervalForAnt(null, 0., null));
    }
}
