package ch.epfl.moocprog;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.moocprog.app.ApplicationInitializer;
import ch.epfl.moocprog.config.ImmutableConfigManager;
import ch.epfl.moocprog.tests.Main;
import ch.epfl.moocprog.utils.Time;
import ch.epfl.moocprog.utils.Vec2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.epfl.moocprog.app.Context.getConfig;
import static ch.epfl.moocprog.config.Config.FOOD_GENERATOR_DELAY;
import static ch.epfl.moocprog.config.Config.WORLD_HEIGHT;
import static ch.epfl.moocprog.config.Config.WORLD_WIDTH;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Step03Tests {

    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }

    @Test
    void foodTest() {

        ToricPosition tp2 = new ToricPosition(1.2, 2.3);
        ToricPosition tp3 = new ToricPosition(new Vec2d(4.5, 6.7));
        Food f1 = new Food(tp2, 4.7);
        Food f2 = new Food(tp3, 6.7);


        Assertions.assertEquals("Position : 1,2, 2,3\nQuantity : 4,70", f1.toString());
        Assertions.assertEquals(4.7, f1.getQuantity());
        Assertions.assertEquals(4.7, f1.takeQuantity(5.0));
        Assertions.assertEquals(0.0, f1.getQuantity());
        Assertions.assertEquals(6.7, f2.getQuantity());
        Assertions.assertEquals(2.0, f2.takeQuantity(2.0));
        Assertions.assertEquals(4.7, f2.getQuantity());

        final Time foodGenDelta = getConfig().getTime(FOOD_GENERATOR_DELAY);
        Environment env = new Environment();
        env.addFood(f1);
        env.addFood(f2);

        Assertions.assertArrayEquals(Arrays.asList(0.0D, 4.7D).toArray(), env.getFoodQuantities().toArray());
        env.update(foodGenDelta);
        assertTrue(env.getFoodQuantities().stream().anyMatch(d -> d.equals(4.7)));
    }
}
