// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;
import vip.Resolute.ui.click.drop.ClickGui;
import vip.Resolute.ui.click.skeet.SkeetUI;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import net.minecraft.client.Minecraft;
import vip.Resolute.modules.Module;

public class ClickGUI extends Module
{
    public static boolean isClosing;
    private final Minecraft mc;
    public static ModeSetting mode;
    public static BooleanSetting rainbow;
    public static ColorSetting color;
    
    public ClickGUI() {
        super("ClickGUI", 54, "Module GUI", Category.RENDER);
        this.mc = Minecraft.getMinecraft();
        this.addSettings(ClickGUI.mode, ClickGUI.rainbow, ClickGUI.color);
    }
    
    @Override
    public void onEnable() {
        ClickGUI.isClosing = false;
        if (ClickGUI.mode.is("Radium")) {
            SkeetUI.init();
        }
        if (ClickGUI.mode.is("Dropdown")) {
            this.mc.displayGuiScreen(new ClickGui());
        }
        this.toggled = false;
    }
    
    static {
        ClickGUI.mode = new ModeSetting("Mode", "Radium", new String[] { "Radium", "Dropdown" });
        ClickGUI.rainbow = new BooleanSetting("Rainbow", false, () -> ClickGUI.mode.is("Radium"));
        ClickGUI.color = new ColorSetting("Color", new Color(112, 0, 207), () -> ClickGUI.mode.is("Dropdown"));
    }
}
