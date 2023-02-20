package ch.epfl.moocprog;

import java.io.File;
import java.util.Arrays;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Step05Tests {

    @BeforeAll
    public static void init() {
        ApplicationInitializer.initializeApplication(new ImmutableConfigManager(new File(Objects.requireNonNull(Main.class.getResource("/app.cfg")).getFile())));
    }
}
