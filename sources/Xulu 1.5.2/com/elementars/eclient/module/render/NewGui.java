// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import java.util.Iterator;
import dev.xulu.newgui.Panel;
import dev.xulu.newgui.NewGUI;
import net.minecraft.client.gui.GuiScreen;
import com.elementars.eclient.Xulu;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class NewGui extends Module
{
    public static Value<Boolean> customfont;
    public static Value<Boolean> rainbowgui;
    public static Value<Integer> rainbowspeed;
    public static Value<Boolean> blur;
    public static Value<Boolean> outline;
    public static Value<String> moduleSetting;
    public static Value<String> toggleSetting;
    public static Value<String> sliderSetting;
    public static Value<Integer> bgAlpha;
    public static Value<Integer> red;
    public static Value<Integer> green;
    public static Value<Integer> blue;
    public static Value<Boolean> resetGui;
    
    public NewGui() {
        super("NewGui", "New gui for the client", 21, Category.CORE, false);
        NewGui.resetGui = this.register(new Value<Boolean>("Reset Gui", this, false));
        NewGui.customfont = this.register(new Value<Boolean>("Custom Font", this, false));
        NewGui.rainbowgui = this.register(new Value<Boolean>("Rainbow ClickGui", this, false));
        NewGui.rainbowspeed = this.register(new Value<Integer>("Rainbow Speed", this, 20, 1, 50));
        NewGui.blur = this.register(new Value<Boolean>("Blur", this, true));
        NewGui.outline = this.register(new Value<Boolean>("Outline", this, true));
        NewGui.moduleSetting = this.register(new Value<String>("Module Setting", this, "Normal", new ArrayList<String>(Arrays.asList("Normal", "MiniButton", "Text"))));
        NewGui.toggleSetting = this.register(new Value<String>("Toggle Setting", this, "Full-box", new ArrayList<String>(Arrays.asList("Checkbox", "Full-box"))));
        NewGui.sliderSetting = this.register(new Value<String>("Slider Setting", this, "Box", new ArrayList<String>(Arrays.asList("Line", "Box"))));
        NewGui.bgAlpha = this.register(new Value<Integer>("Background alpha", this, 130, 0, 255));
        NewGui.red = this.register(new Value<Integer>("GuiRed", this, 255, 0, 255));
        NewGui.green = this.register(new Value<Integer>("GuiGreen", this, 26, 0, 255));
        NewGui.blue = this.register(new Value<Integer>("GuiBlue", this, 42, 0, 255));
    }
    
    @Override
    public void onEnable() {
        NewGui.mc.displayGuiScreen((GuiScreen)Xulu.newGUI);
        this.toggle();
    }
    
    public static void resetGui() {
        if (NewGui.resetGui.getValue()) {
            int startY = 10;
            for (final Panel panel : NewGUI.getPanels()) {
                panel.x = 10.0;
                panel.y = startY;
                startY += 23;
            }
            NewGui.resetGui.setValue(false);
        }
    }
}
