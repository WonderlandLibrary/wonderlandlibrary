package Method.Client.module.render;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;


public class ArmorRender extends Module {

    public static Setting enableColoredGlint;
    public static Setting useRuneTexture;
    public static Setting CustomColor;
    public static Setting Color;
    public static Setting RenderArmor;


    public ArmorRender() {
        super("ArmorRender", Keyboard.KEY_NONE, Category.RENDER, "ArmorRender");
    }

    @Override
    public void setup() {
        setmgr.add(enableColoredGlint = new Setting("enableColoredGlint", this, false));
        setmgr.add(CustomColor = new Setting("CustomColor", this, false));
        setmgr.add(Color = new Setting("OverlayColor", this, 0, 1, 1, .56, CustomColor));
        setmgr.add(useRuneTexture = new Setting("useRuneTexture", this, false));
        setmgr.add(RenderArmor = new Setting("RenderArmor", this, true));
    }


}

