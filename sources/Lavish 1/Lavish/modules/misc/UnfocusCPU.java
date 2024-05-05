// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.misc;

import org.lwjgl.opengl.Display;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class UnfocusCPU extends Module
{
    public int fps;
    
    public UnfocusCPU() {
        super("UnfocusCPU", 0, true, Category.Misc, "Unfocuses your CPU when you aren't using it");
        Client.instance.setmgr.rSetting(new Setting("UnfocusCPU FPS", this, 20.0, 1.0, 100.0, false));
    }
    
    @Override
    public void onUpdate() {
        if (!Display.isActive()) {
            UnfocusCPU.mc.gameSettings.limitFramerate = (int)Client.instance.setmgr.getSettingByName("UnfocusCPU FPS").getValDouble();
        }
        else {
            UnfocusCPU.mc.gameSettings.limitFramerate = this.fps;
        }
    }
    
    @Override
    public void onEnable() {
        this.fps = UnfocusCPU.mc.gameSettings.limitFramerate;
    }
}
