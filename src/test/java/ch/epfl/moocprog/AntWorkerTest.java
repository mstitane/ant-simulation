package ch.epfl.moocprog;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ch.epfl.moocprog.app.ApplicationInitializer;
import ch.epfl.moocprog.config.ImmutableConfigManager;
import ch.epfl.moocprog.tests.Main;
import ch.epfl.moocprog.utils.Time;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.ANT_PHEROMONE_DENSITY;
import static ch.epfl.moocprog.config.Config.ANT_PHEROMONE_ENERGY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void antIsSensibleToCloseFoodUTurnOnly() {
        Environment env = new Environment();
        ToricPosition position = new ToricPosition(0, 0);
        Animal animal = new AntWorker(position, 50, Time.fromSeconds(10), Uid.createUid());
        double initialDirection = animal.getDirection();
        Food f1 = new Food(position, 10);
        Food f2 = new Food(new ToricPosition(1, 1), 10);
        Food f3 = new Food(new ToricPosition(2, 3), 10);
        env.addFood(f1);
        env.addFood(f2);
        env.addFood(f3);
        env.addAnimal(animal);
        animal.update(env, Time.fromSeconds(1));
        Assertions.assertNotEquals(initialDirection, animal.getDirection());
    }

    @Test
    void antCorrectlyDropsFoodToItsAnthillUTurnOnly() {
        Environment env = new Environment();
        ToricPosition position = new ToricPosition(0, 0);
        Animal animal = new AntWorker(position, 50, Time.fromSeconds(10), Uid.createUid());
        double initialDirection = animal.getDirection();
        Anthill anthill = new Anthill(position, 10);
        Food f1 = new Food(position, 10);
        env.addFood(f1);
        env.addAnimal(animal);
        env.addAnthill(anthill);

        animal.update(env, Time.fromSeconds(1));
        Assertions.assertNotEquals(initialDirection, animal.getDirection());
    }

    @Test
    void antIsSensibleToCloseFoodFoodQtiesOnly() {
        Environment env = new Environment();
        ToricPosition position = new ToricPosition(0, 0);
        AntWorker animal = new AntWorker(position, 50, Time.fromSeconds(10), Uid.createUid());
        Food f1 = new Food(position, 5);
        Food f2 = new Food(new ToricPosition(1, 1), 10);
        Food f3 = new Food(new ToricPosition(2, 3), 10);
        env.addFood(f1);
        env.addFood(f2);
        env.addFood(f3);
        env.addAnimal(animal);
        animal.update(env, Time.fromSeconds(1));
        Assertions.assertEquals(5.0, animal.getFoodQuantity());
    }

    @Test
    void antTakesAndDropsFood() {
        Environment env = new Environment();
        ToricPosition zero = new ToricPosition(0, 0);
        Anthill anthill = new Anthill(zero, 10);
        env.addAnthill(anthill);
        AntWorker animal = new AntWorker(new ToricPosition(1, 1), 50, Time.fromSeconds(10), anthill.getAnthillId());
        env.addAnimal(animal);

        Food f1 = new Food(zero, 30);
        Food f2 = new Food(new ToricPosition(1, 1), 40);
        Food f3 = new Food(new ToricPosition(2, 2), 30);
        Food f4 = new Food(new ToricPosition(3, 3), 30);
        env.addFood(f1);
        env.addFood(f2);
        env.addFood(f3);
        env.addFood(f4);
        Anthill anthill1 = new Anthill(new ToricPosition(3, 3), 10);
        Anthill anthill2 = new Anthill(new ToricPosition(4, 4), 10);
        Anthill anthill3 = new Anthill(new ToricPosition(5, 5), 10);
        env.addAnthill(anthill1);
        env.addAnthill(anthill2);
        env.addAnthill(anthill3);
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        Assertions.assertEquals(0.0, animal.getFoodQuantity());
        Assertions.assertEquals(20.0, anthill.getFoodQuantity());
    }

    @Test
    void antCorrectlyDropsFoodToItsAnthillBoth() {
        Environment env = new Environment();
        ToricPosition zero = new ToricPosition(0, 0);
        Anthill anthill = new Anthill(zero, 0);
        env.addAnthill(anthill);
        AntWorker animal = new AntWorker(new ToricPosition(1, 1), 50, Time.fromSeconds(10), anthill.getAnthillId());
        env.addAnimal(animal);

        Food f1 = new Food(zero, 20);
        env.addFood(f1);
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        Assertions.assertEquals(0.0, animal.getFoodQuantity());
        Assertions.assertEquals(20.0, anthill.getFoodQuantity());
    }

    @Test
    void antIsSensibleToCloseFoodBoth() {
        Environment env = new Environment();
        ToricPosition zero = new ToricPosition(0, 0);
        Anthill anthill = new Anthill(zero, 0);
        env.addAnthill(anthill);
        AntWorker animal = new AntWorker(new ToricPosition(1, 1), 50, Time.fromSeconds(10), anthill.getAnthillId());
        env.addAnimal(animal);

        Food f1 = new Food(zero, 20);
        env.addFood(f1);
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        animal.update(env, Time.fromSeconds(1));
        Assertions.assertEquals(0.0, animal.getFoodQuantity());
        Assertions.assertEquals(20.0, anthill.getFoodQuantity());
    }

    @Test
    void spreadPheromones() {
        AntWorker antWorker = new AntWorker(new ToricPosition(), Uid.createUid());
        Environment env = new Environment();
        env.addAnimal(antWorker);
        env.update(Time.fromSeconds(0.5));
        antWorker.afterMoveAnt(env, Time.ZERO);
        Assertions.assertEquals(50.5, new ToricPosition().toricDistance(antWorker.getPosition()),0.1);
        double density = getConfig().getDouble(ANT_PHEROMONE_DENSITY);
        double energy = getConfig().getDouble(ANT_PHEROMONE_ENERGY);
        Assertions.assertEquals(0.1, density);
        Assertions.assertEquals(5.0, energy);
        List<Double> doubleList = Arrays.asList(5.0, 5.0, 5.0, 5.0, 5.0);
        assertEquals(5, env.getPheromonesQuantities().size());
        assertTrue(Arrays.deepEquals(doubleList.toArray(), env.getPheromonesQuantities().toArray()));

    }
}
