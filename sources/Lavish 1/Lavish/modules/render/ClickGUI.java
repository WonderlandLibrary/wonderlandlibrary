// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.render;

import net.minecraft.client.gui.GuiScreen;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import java.util.ArrayList;
import Lavish.modules.Category;
import Lavish.ClickGUI.LavishClickGUI.ClickGui;
import Lavish.modules.Module;

public class ClickGUI extends Module
{
    public ClickGui clickgui;
    
    public ClickGUI() {
        super("ClickGUI", 54, false, Category.Render, "Manage all of your modules");
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Normal");
        options.add("Moon");
        options.add("Rise");
        Client.instance.setmgr.rSetting(new Setting("ClickGUI Mode", this, "Normal", options));
        Client.instance.setmgr.rSetting(new Setting("ClickGUI Pulse", this, true));
        Client.instance.setmgr.rSetting(new Setting("ClickGUI Red", this, 212.0, 0.0, 255.0, false));
        Client.instance.setmgr.rSetting(new Setting("ClickGUI Green", this, 67.0, 0.0, 255.0, false));
        Client.instance.setmgr.rSetting(new Setting("ClickGUI Blue", this, 67.0, 0.0, 255.0, false));
    }
    
    @Override
    public void onEnable() {
        if (Client.instance.setmgr.getSettingByName("ClickGUI Mode").getValString().equalsIgnoreCase("Normal")) {
            if (this.clickgui == null) {
                this.clickgui = new ClickGui();
            }
            ClickGUI.mc.displayGuiScreen(this.clickgui);
            this.toggle();
            super.onEnable();
        }
        if (Client.instance.setmgr.getSettingByName("ClickGUI Mode").getValString().equalsIgnoreCase("Rise")) {
            ClickGUI.mc.displayGuiScreen(Client.instance.RiseGUI);
            Client.instance.RiseGUI.slideanim = 0;
            Client.instance.RiseGUI.animation = 20;
            this.toggle();
            super.onEnable();
        }
        if (Client.instance.setmgr.getSettingByName("ClickGUI Mode").getValString().equalsIgnoreCase("Moon")) {
            ClickGUI.mc.displayGuiScreen(Client.instance.MoonGUI);
            this.toggle();
            super.onEnable();
        }
    }
    
    @Override
    public void onUpdate() {
    }
}
