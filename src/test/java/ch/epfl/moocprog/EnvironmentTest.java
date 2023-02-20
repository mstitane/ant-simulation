package ch.epfl.moocprog;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ch.epfl.moocprog.app.ApplicationInitializer;
import ch.epfl.moocprog.config.ImmutableConfigManager;
import ch.epfl.moocprog.tests.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EnvironmentTest {
    @BeforeAll
    public static void init() {
        File appFile = new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile());
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(appFile));
    }

    @Test
    void getVisibleEnemiesForAnimalNonEmptyForNearEnemies() {
        String expected = "[Position : 1,0, 1,0\nSpeed : 100,0\nHitPoints : 500\nLifeSpan : 50,000000\nState : IDLE\n"
                + "Quantity : 0,00, Position : 2,0, 2,0\nSpeed : 101,0\nHitPoints : 500\nLifeSpan : 50,000000\nState : IDLE]";
        // - Nous créons 2 Uid.
        Uid uid1 = Uid.createUid();
        Uid uid2 = Uid.createUid();
        // - Nous créons un nouvel Environment.
        Environment env = new Environment();
        // Nous créons 3 ToricPosition.
        ToricPosition p1 = new ToricPosition();
        ToricPosition p2 = new ToricPosition(1.0, 1.0);
        ToricPosition p3 = new ToricPosition(2.0, 2.0);
        // - Nous créons un AntWorker à la position  avec le premier Uid.
        AntWorker ant1 = new AntWorker(p1, uid1);
        // - Nous l'ajoutons à l'Environment.
        env.addAnt(ant1);
        // - Nous créons un ennemi, à savoir un AntWorker avec le deuxième Uid.
        AntWorker enemy1 = new AntWorker(p2, uid2);
        // - Nous l'ajoutons à l'Environment.
        env.addAnt(enemy1);
        // - Nous créons un deuxième ennemi, un AntSoldier avec le deuxième Uid.
        AntSoldier enemy2 = new AntSoldier(p3, uid2);
        // - Nous l'ajoutons à l'Environment.
        env.addAnt(enemy2);
        // - Nous appelons la méthode getVisibleEnemiesForAnimal() avec comme argument le premier AntWorker.
        List<Animal> enemies = env.getVisibleEnemiesForAnimal(ant1);
        Assertions.assertEquals(2, enemies.size());
        Object[] strings = enemies.stream().map(Animal::toString).toArray();
        Assertions.assertEquals(expected, Arrays.deepToString(strings));
    }

}
