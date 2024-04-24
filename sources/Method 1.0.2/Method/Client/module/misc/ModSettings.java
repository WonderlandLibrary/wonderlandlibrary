package Method.Client.module.misc;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import org.lwjgl.input.Keyboard;

import static Method.Client.Main.setmgr;

public class ModSettings extends Module {
    public static Setting Spherelines;
    public static Setting SphereDist;
    public static Setting Rendernonsee;
    public static Setting GuiSpeed;
    public static Setting disconnectconfirm;

    public ModSettings() {
        super("ModSettings", Keyboard.KEY_NONE, Category.MISC, "Mod Settings for other modules");
        setmgr.add(Spherelines = new Setting("Shapelines", this, 10, 0, 20, true));
        setmgr.add(SphereDist = new Setting("ShapeDist", this, 10, 0, 20, true));
        setmgr.add(GuiSpeed = new Setting("GuiSpeed", this, 20, 0, 50, true));
        setmgr.add(Rendernonsee = new Setting("Unseen Render", this, false));
        setmgr.add(disconnectconfirm = new Setting("Confirm Disconnect", this, true));
    }

}
