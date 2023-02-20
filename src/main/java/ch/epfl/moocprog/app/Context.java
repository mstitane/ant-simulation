package ch.epfl.moocprog.app;
import java.util.Objects;

import ch.epfl.moocprog.config.ConfigManager;

/**
 * Contexte de la simulation.
 */
public final class Context {

    private Context() {
    }

    private static Application theApp;
    public static final String CONFIG_PATH = Objects.requireNonNull(Context.class.getResource("/app.cfg")).getFile();
    public static final String INIT_PATH = Objects.requireNonNull(Context.class.getResource("/config.cfg")).getFile();

    /*package*/
    static void initializeApplication(Application application) {
        theApp = application;
    }

    /**
     * Retourne l'instance de l'application de ce contexte.
     *
     * @return L'instance de l'application
     */
    public static Application getApplication() {
        assert theApp != null;
        return theApp;
    }
    
    public static ConfigManager getConfig() {
        return theApp.getConfigManager();
    }
}
