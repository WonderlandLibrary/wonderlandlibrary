package me.Womas.Light.Core;

import javafx.scene.effect.Light;
import me.Womas.Light.Logger.AquaticLogger;
import me.Womas.Light.Module.ModuleManager;
import org.lwjgl.opengl.Display;

public class Aquatic {

    public static final String CLIENT_NAME = "Aquatic";
    public static final double CLIENT_VERSION = 1.0;
    public static final String FULLNAME = CLIENT_NAME + " | " + CLIENT_VERSION;

    private static final Aquatic INSTANCE = new Aquatic();
    private AquaticLogger logger = new AquaticLogger();
    private ModuleManager moduleManager;



    public void boot() {

        logger.logger("Client Booting");
        Display.setTitle(getName());
        moduleManager = new ModuleManager();
        moduleManager.loadModule();

    }







    public static Aquatic getInstance(){
        return INSTANCE;
    }

    public static String getClientName() {
        return CLIENT_NAME;
    }

    public static double getClientVersion() {
        return CLIENT_VERSION;
    }

    public static String getName() {
        return FULLNAME;
    }

    public AquaticLogger getLogger() {
        return logger;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}



