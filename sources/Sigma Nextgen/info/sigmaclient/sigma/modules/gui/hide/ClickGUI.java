package info.sigmaclient.sigma.modules.gui.hide;

import info.sigmaclient.sigma.gui.other.clickgui.NursultanClickGui;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.gui.clickgui.JelloClickGui;
import net.minecraft.client.Minecraft;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ClickGUI extends Module {
    public static JelloClickGui clickGui = new JelloClickGui();
    public static boolean isEnableFirst = false;

    public ClickGUI() {
        super("ClickGUI", Category.Gui, "WHAT? you find this???");
    }

    @Override
    public void onEnable() {
        this.enabled = false;
        isEnableFirst = true;
        Minecraft.getInstance().displayGuiScreen(ClickGUI.clickGui);
        super.onEnable();
    }
}
