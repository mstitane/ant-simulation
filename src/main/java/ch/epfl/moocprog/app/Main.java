package ch.epfl.moocprog.app;

import java.io.File;

import ch.epfl.moocprog.config.ImmutableConfigManager;
import ch.epfl.moocprog.gfx.JavaFXContainer;

public class Main {



    public static void main(String[] args) {
        Context.initializeApplication(new Application(
                new ImmutableConfigManager(
                        new File(Context.CONFIG_PATH)
                )
        ));

        javafx.application.Application.launch(JavaFXContainer.class, args);
    }
}
