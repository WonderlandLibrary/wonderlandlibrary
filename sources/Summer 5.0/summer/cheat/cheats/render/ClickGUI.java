package summer.cheat.cheats.render;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.security.HWID;
import net.minecraft.util.security.Verify;
import summer.Summer;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.ChatUtils;
import summer.cheat.guiutil.Setting;


public class ClickGUI extends Cheats {
    public static String mode;

    public static Setting guiHue;
    public static Setting rainbowGui;
    public static Setting guiSaturation;
    private Setting debug;

    public ClickGUI() {
        super("ClickGUI", "Shows the clickgui", Selection.RENDER, true);
        Summer.INSTANCE.settingsManager.Property(guiHue = new Setting("Gui Hue", this, 0.16F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(rainbowGui = new Setting("Rainbow Gui", this, false));
        Summer.INSTANCE.settingsManager.Property(guiSaturation = new Setting("Gui Saturation", this, 1.0F, 0.6F, 1.0F, false, rainbowGui::getValBoolean));
        this.setKey(Keyboard.KEY_RSHIFT);
    }

//    @Override
//    public void updateSettings() {
//        mode = Summer.instance.sm.getSettingByName(this, "Theme").getValString();
//    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Summer.INSTANCE.clickGui1);
        toggle();
        super.onEnable();

    }
}