// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.Xulu;
import net.minecraft.client.gui.GuiScreen;
import dev.xulu.clickgui.ClickGui;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class ExeterGui extends Module
{
    private final Value<Boolean> customFont;
    private final Value<Boolean> sound;
    private final Value<Boolean> rainbow;
    private final Value<Integer> rainbowspeed;
    
    public ExeterGui() {
        super("ExeterGui", "Exeter Gui", 0, Category.CORE, true);
        this.customFont = this.register(new Value<Boolean>("Custom Font", this, false));
        this.sound = this.register(new Value<Boolean>("Sound", this, true));
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.rainbowspeed = this.register(new Value<Integer>("Rainbow Speed", this, 20, 1, 50));
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        ExeterGui.mc.displayGuiScreen((GuiScreen)ClickGui.getClickGui());
        this.toggle();
    }
    
    public static boolean getCF() {
        return Xulu.MODULE_MANAGER.getModuleT(ExeterGui.class).customFont.getValue();
    }
    
    public static boolean getSound() {
        return Xulu.MODULE_MANAGER.getModuleT(ExeterGui.class).sound.getValue();
    }
    
    public static boolean getRainbow() {
        return Xulu.MODULE_MANAGER.getModuleT(ExeterGui.class).rainbow.getValue();
    }
    
    public static int getSpeed() {
        return Xulu.MODULE_MANAGER.getModuleT(ExeterGui.class).rainbowspeed.getValue();
    }
}
