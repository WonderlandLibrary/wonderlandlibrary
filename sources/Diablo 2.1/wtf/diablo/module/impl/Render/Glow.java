package wtf.diablo.module.impl.Render;

import lombok.Getter;
import lombok.Setter;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.render.ColorUtil;

import java.awt.*;

@Getter@Setter
public class Glow extends Module {
    public static NumberSetting red = new NumberSetting("Red",45,1,1,255);
    public static NumberSetting green = new NumberSetting("Green",24,1,1,255);
    public static NumberSetting blue = new NumberSetting("Blue",158,1,1,255);
    public static NumberSetting size = new NumberSetting("Size",1,1,1,5);
    public static BooleanSetting rainbow = new BooleanSetting("Sync", true);

    public Glow(){
        super("Glow","we love autism", Category.RENDER, ServerType.All);
        this.addSettings(red,green,blue,size,rainbow);
    }

    public static int getColor() {
        return rainbow.getValue() ? ColorUtil.getColor(0) : new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()).getRGB();
    }

}