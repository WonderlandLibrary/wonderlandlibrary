// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.HUD;

import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;
import ru.tuskevich.Minced;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "ClickGUI", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.HUD)
public class ClickGui extends Module
{
    public ColorSetting color;
    public BooleanSetting optimize;
    
    public ClickGui() {
        this.color = new ColorSetting("Color", -1);
        this.optimize = new BooleanSetting("Optimize Gui", true);
        this.bind = 54;
        this.add(new Setting[0]);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        ClickGui.mc.displayGuiScreen(Minced.getInstance().menuMain);
        Minced.getInstance().manager.getModule(ClickGui.class).setState(false);
    }
    
    public static Color getColor() {
        return ((ClickGui)Minced.getInstance().manager.getModule(ClickGui.class)).color.getColorValueColor();
    }
    
    public static boolean getOptimize() {
        return !((ClickGui)Minced.getInstance().manager.getModule(ClickGui.class)).optimize.state;
    }
}
