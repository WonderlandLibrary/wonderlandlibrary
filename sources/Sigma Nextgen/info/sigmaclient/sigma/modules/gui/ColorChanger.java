package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.config.values.ColorValue;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.render.ColorUtils;

import java.awt.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ColorChanger extends Module {
    public static ColorValue r1 = new ColorValue("Color1", 0);
    public static ColorValue r2 = new ColorValue("Color2", 0);

    public ColorChanger() {
        super("Color", Category.Gui, "Change colors");
     registerValue(r1);
     registerValue(r2);
    }
    public static Color getColor(int index, int speed){
        return ColorUtils.interpolateColorsBackAndForth(speed, index * 2,
                r1.getColor(),
                r2.getColor()
                , false);
    }
}
