// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.ClickGUI.MoonClickGUI;

import java.io.IOException;
import java.util.Iterator;
import Lavish.config.Config;
import Lavish.modules.Module;
import Lavish.utils.fonts.FontUtil;
import net.minecraft.client.gui.Gui;
import Lavish.modules.Category;
import java.awt.Color;
import Lavish.Client;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen
{
    public int categoryPos;
    public static boolean isOpen;
    public int textColor;
    public boolean expanded;
    public float x;
    public float y;
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int r = (int)Client.instance.setmgr.getSettingByName("ClickGUI Red").getValDouble();
        final int g = (int)Client.instance.setmgr.getSettingByName("ClickGUI Green").getValDouble();
        final int b = (int)Client.instance.setmgr.getSettingByName("ClickGUI Blue").getValDouble();
        int catOffset = 0;
        final int Color = new Color(r, g, b, 255).getRGB();
        Category[] values;
        for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
            final Category cat = values[i];
            Gui.drawRect(10 + catOffset * 130, 8.0, 130 + catOffset * 130, 35.0, -15395563);
            if (cat == Category.Combat) {
                FontUtil.categoryFont.drawString(cat.name(), 75 + catOffset * 130, 15.5, -1);
                FontUtil.coolFont.drawString("E", 15 + catOffset * 130, 15.5, -1);
                this.x = (float)(75 + catOffset * 130);
            }
            if (cat == Category.Movement) {
                FontUtil.categoryFont.drawString(cat.name(), 56 + catOffset * 130, 15.5, -1);
                FontUtil.movementicon.drawString("E", 12 + catOffset * 130, 13.0, -1);
                this.x = (float)(56 + catOffset * 130);
            }
            if (cat == Category.Player) {
                FontUtil.categoryFont.drawString(cat.name(), 85 + catOffset * 130, 15.5, -1);
                FontUtil.csgoFont.drawString("f", 15 + catOffset * 130, 15.5, -1);
                this.x = (float)(85 + catOffset * 130);
            }
            if (cat == Category.Render) {
                FontUtil.categoryFont.drawString(cat.name(), 78 + catOffset * 130, 15.5, -1);
                FontUtil.csgoFont.drawString("c", 15 + catOffset * 130, 15.5, -1);
                this.x = (float)(78 + catOffset * 130);
            }
            if (cat == Category.Misc) {
                FontUtil.categoryFont.drawString(cat.name(), 95 + catOffset * 130, 15.5, -1);
                FontUtil.movementicon.drawString("W", 12 + catOffset * 130, 13.0, -1);
                this.x = (float)(95 + catOffset * 130);
            }
            if (cat == Category.Exploit) {
                FontUtil.categoryFont.drawString(cat.name(), 83 + catOffset * 130, 15.5, -1);
                FontUtil.csgoFont.drawString("d", 15 + catOffset * 130, 15.5, -1);
                this.x = (float)(83 + catOffset * 130);
            }
            ++catOffset;
            int modOffset = 0;
            for (final Module m : Client.instance.moduleManager.getModulesInCategory(cat)) {
                Gui.drawRect(catOffset * 130 - 120, 35.2f + modOffset * 25, catOffset * 130, 60.2f + modOffset * 25, m.isEnabled() ? Color : -14606047);
                FontUtil.moduleFont.drawString(m.getName(), catOffset * 130 - 115, 44 + modOffset * 25, -1);
                ++modOffset;
            }
        }
        int configOffset = 0;
        Gui.drawRect(10 + catOffset * 130, 8.0, 130 + catOffset * 130, 35.0, -15395563);
        FontUtil.categoryFont.drawString("Configs", 75 + catOffset * 130, 15.5, -1);
        FontUtil.movementicon.drawString("D", 14 + catOffset * 130, 13.0, -1);
        for (final Config config : Client.instance.configManager.getContents()) {
            Gui.drawRect(catOffset * 130 + 10, 35.2f + configOffset * 25, catOffset * 130 + 130, 60.2f + configOffset * 25, -14606047);
            FontUtil.moduleFont.drawString(config.getName(), catOffset * 130 + 15, 44 + configOffset * 25, -1);
            ++configOffset;
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    public void initGui() {
        ClickGui.isOpen = true;
    }
}
