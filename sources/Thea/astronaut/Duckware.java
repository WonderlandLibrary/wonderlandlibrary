package astronaut;


import astronaut.modules.Module;
import astronaut.modules.ModuleManager;
import astronaut.utils.FileUtil;
import astronaut.utils.UnicodeFontRenderer;
import de.Hero.clickgui.ClickGUI;
import de.Hero.settings.SettingsManager;
import eventapi.EventManager;
import org.lwjgl.opengl.Display;

public class Duckware {

    public static final String name = "Thea Client";
    public static final String version = "b1";
    public static final String[] developer = {"Verschmxtzt", "Chip"};
    public static ModuleManager moduleManager;
    public static FileUtil fileUtil;
    public static UnicodeFontRenderer arial22, arial19;
    public static SettingsManager setmgr;
    public static ClickGUI clickgui;

    public static void onStart() {
        EventManager.register(Duckware.class);
        System.out.println(name + " has started!");
        Display.setTitle(name + " " + version + " by " + developer[0] + "," + developer[1]);
        moduleManager = new ModuleManager();
        fileUtil = new FileUtil();
        fileUtil.createFolder();
        fileUtil.loadKeys();
        fileUtil.loadModules();
        arial22 = UnicodeFontRenderer.getFontOnPC("Arial", 22, 0, 0, 1);
        arial19 = UnicodeFontRenderer.getFontOnPC("Arial", 19, 0, 0, 1);

        setmgr = new SettingsManager();

        Duckware.moduleManager.modules.forEach(Module::setup);

        clickgui = new ClickGUI();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fileUtil.saveKeys();
            fileUtil.saveModules();
        }));
    }
}
