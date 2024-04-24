package Method.Client.module.combat;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.utils.proxy.renderers.ModEnderCrystal;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;

public class CrystalRender extends Module {

    public CrystalRender() {
        super("CrystalRender", Keyboard.KEY_NONE, Category.COMBAT, "CrystalRender");
    }

    public static Setting ChangeColor;
    public static Setting Color;
    public static Setting Outlines;
    public static Setting scale;
    public static Setting RenderCube;
    public static Setting RenderGlass;
    public static Setting RenderBase;
    public static Setting NoMotion;
    public static Setting NoSpin;

    @Override
    public void setup() {
        setmgr.add(ChangeColor = new Setting("ChangeColor", this, false));
        setmgr.add(Color = new Setting("Crystal", this, 0, 1, 1, 1));
        setmgr.add(scale = new Setting("scale", this, 1, .001, 3, false));
        setmgr.add(Outlines = new Setting("Outlines", this, false));
        setmgr.add(RenderCube = new Setting("RenderCube", this,  true));
        setmgr.add(RenderGlass = new Setting("RenderGlass", this,  true));
        setmgr.add(NoMotion = new Setting("NoMotion", this,  false));
        setmgr.add(NoSpin = new Setting("NoSpin", this,  false));
    }

    @Override
    public void onToggle() {
        ModEnderCrystal.Toggle();
    }


}
