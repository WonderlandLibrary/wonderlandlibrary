package wtf.diablo.module.impl.Render;

import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.render.ColorUtil;

import java.awt.*;

public class Chams extends Module {
    public static ModeSetting mode = new ModeSetting("Mode", "Box", "Chams");
    public static NumberSetting red = new NumberSetting("Red", 250, 1, 0, 255);
    public static NumberSetting green = new NumberSetting("Green", 250, 1, 0, 255);
    public static NumberSetting blue = new NumberSetting("Blue", 250, 1, 0, 255);
    public static NumberSetting redHidden = new NumberSetting("Red Hidden", 250, 1, 0, 255);
    public static NumberSetting greenHidden = new NumberSetting("Green Hidden", 250, 1, 0, 255);
    public static NumberSetting blueHidden = new NumberSetting("Blue Hidden", 250, 1, 0, 255);
    public static BooleanSetting material = new BooleanSetting("Material", true);
    public static BooleanSetting rainbow = new BooleanSetting("Sync", true);

    public Chams() {
        super("Chams", "Chams", Category.RENDER, ServerType.All);
        this.addSettings(mode, red, green, blue, redHidden, greenHidden, blueHidden, material,rainbow);
        red.setParent(mode, "Chams");
        green.setParent(mode, "Chams");
        blue.setParent(mode, "Chams");
        redHidden.setParent(mode, "Chams");
        greenHidden.setParent(mode, "Chams");
        blueHidden.setParent(mode, "Chams");
        material.setParent(mode, "Chams");
        rainbow.setParent(mode,"Chams");
    }

    public static int getColor() {
        return rainbow.getValue() ? ColorUtil.getColor(0) : new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()).getRGB();
    }

    public static int getColorHidden() {
        return rainbow.getValue() ? ColorUtil.getColor(0) : new Color((int) redHidden.getValue(), (int) greenHidden.getValue(), (int) blueHidden.getValue()).getRGB();
    }
}
