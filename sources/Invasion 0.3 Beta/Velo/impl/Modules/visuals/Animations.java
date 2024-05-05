package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.impl.Event.EventRender;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;

public class Animations extends Module {

    public static ModeSetting mode = new ModeSetting("BlockMode", "Normal", "Normal", "OldExhibition", "Exhibobo", "Exhibobo2", "Tap", "Tap2", "MoonExhibobo", "Slide", "Swang", "Swank", "AstolfoSpin", "Ohareware", "Wtf");
    public static ModeSetting hitmode = new ModeSetting("HitMode", "Normal", "Normal", "Invasion");
    public static NumberSetting hitslowdown = new NumberSetting("HitSlowDown", 0, 0, 50, 1);
    public static NumberSetting transX = new NumberSetting("TranslateX", 0.56, -1, 1, 0.01);
    public static NumberSetting transY = new NumberSetting("TranslateY", -0.22, -1, 1, 0.01);
    public static NumberSetting idkmultiplier = new NumberSetting("IdkMultiplier", 1, 0, 1, 0.01);

    public static boolean isEnabled = false;

    public Animations() {
        super("Animations", "Animations", Keyboard.KEY_NONE, Category.VISUALS);
        this.loadSettings(mode, hitmode, hitslowdown, transX, transY, idkmultiplier);
    }

    public void onEnable() {

    }

    public void onDisable() {
        isEnabled = false;
    }

    public void onUpdate(EventUpdate event) {
    	this.setDisplayName("Animations " + mode.modes.get(mode.index));
        isEnabled = true;
    }
}