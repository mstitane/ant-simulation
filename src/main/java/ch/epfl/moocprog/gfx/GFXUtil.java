package ch.epfl.moocprog.gfx;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.image.Image;

class GFXUtil {
    private GFXUtil() {
        // Clin d'oeil au constructeur de Objects ;)
        throw new AssertionError("Pas d'instance de GFXUtil pour vous !");
    }
    
    public static String RES_PATH = GFXUtil.class.getResource("/res/").getFile() ;
    
    static Image loadSprite(String filename) {
        try (FileInputStream fis = new FileInputStream(filename)) {
            return new Image(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(
                "Erreur lors du chargement de "
              + filename + ".\n" + e.getMessage()
            );
        }
    }
}
