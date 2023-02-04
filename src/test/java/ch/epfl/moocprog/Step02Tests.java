package ch.epfl.moocprog;

import java.io.File;
import java.util.Objects;

import ch.epfl.moocprog.app.ApplicationInitializer;
import ch.epfl.moocprog.config.ImmutableConfigManager;
import ch.epfl.moocprog.tests.Main;
import ch.epfl.moocprog.utils.Vec2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.WORLD_HEIGHT;
import static ch.epfl.moocprog.config.Config.WORLD_WIDTH;

public class Step02Tests {

    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }

    @Test
    void topicPositionTest() {
        final int width = getConfig().getInt(WORLD_WIDTH);
        final int height = getConfig().getInt(WORLD_HEIGHT);

        ToricPosition tp1 = new ToricPosition();
        ToricPosition tp2 = new ToricPosition(1.2, 2.3);
        ToricPosition tp3 = new ToricPosition(new Vec2d(4.5, 6.7));
        ToricPosition tp4 = tp3.add(tp2);
        ToricPosition tp5 = new ToricPosition(width, height);
        ToricPosition tp6 = new ToricPosition(width / 2.0, height / 2.0);
        ToricPosition tp7 = tp4.add(tp6.add(new Vec2d(width / 2.0, height / 2.0)));
        ToricPosition tp8 = new ToricPosition(3, 4);
        Vec2d v1 = tp2.toricVector(tp3);

        Assertions.assertEquals("Position : 0,0, 0,0", tp1.toString());
        Assertions.assertEquals("Position : 1,2, 2,3", tp2.toString());
        Assertions.assertEquals("Position : 4,5, 6,7", tp3.toString());
        Assertions.assertEquals("Position : 5,7, 9,0", tp4.toString());
        Assertions.assertEquals("Position : 3,3, 4,4", v1.toString());
        Assertions.assertEquals("Position : 0,0, 0,0", tp5.toString());
        Assertions.assertEquals("Position : 500,0, 350,0", tp6.toString());
        Assertions.assertEquals("Position : 5,7, 9,0", tp7.toString());
        Assertions.assertEquals(5.0, tp1.toricDistance(tp8));
    }

    @Test
    void positionableTest() {
        Positionable p1 = new Positionable();
        ToricPosition tp = new ToricPosition(5.7, 9.0);
        Positionable p2 = new Positionable(tp);

        Assertions.assertEquals("Position : 0,0, 0,0", p1.getPosition().toString());
        Assertions.assertEquals("Position : 5,7, 9,0", p2.getPosition().toString());

    }
}
