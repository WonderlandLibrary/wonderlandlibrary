package de.theBest.MythicX;


import de.theBest.MythicX.modules.Module;
import de.theBest.MythicX.modules.ModuleManager;
import de.theBest.MythicX.utils.FileUtil;
import de.theBest.MythicX.utils.UnicodeFontRenderer;
import de.Hero.clickgui.ClickGUI;
import de.Hero.settings.SettingsManager;
import eventapi.EventManager;
import org.lwjgl.opengl.Display;

public class MythicX {

    public static final String name = "MythicX";
    public static final String version = "1.1";
    public static final String developer = "theBest";
    public static ModuleManager moduleManager;
    public static FileUtil fileUtil;
    public static UnicodeFontRenderer arial22, arial19;
    public static SettingsManager setmgr;
    public static ClickGUI clickgui;

    public static void onStart() {
        EventManager.register(MythicX.class);
        System.out.println(name + " has started!");
        Display.setTitle(name + " " + version + " by " + developer);
        moduleManager = new ModuleManager();
        fileUtil = new FileUtil();
        fileUtil.createFolder();
        fileUtil.loadKeys();
        fileUtil.loadModules();
        arial22 = UnicodeFontRenderer.getFontOnPC("Arial", 22, 0, 0, 1);
        arial19 = UnicodeFontRenderer.getFontOnPC("Arial", 19, 0, 0, 1);

        setmgr = new SettingsManager();

        MythicX.moduleManager.modules.forEach(Module::setup);

        clickgui = new ClickGUI();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fileUtil.saveKeys();
            fileUtil.saveModules();
        }));
    }
}
