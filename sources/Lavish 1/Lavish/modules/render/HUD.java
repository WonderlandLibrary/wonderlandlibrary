// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.render;

import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import Lavish.utils.misc.TimeUtil;
import net.minecraft.client.gui.Gui;
import Lavish.utils.render.Render;
import net.minecraft.client.Minecraft;
import Lavish.utils.math.MathUtils;
import net.minecraft.util.EnumChatFormatting;
import Lavish.utils.color.ColorUtil;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import net.minecraft.client.gui.ScaledResolution;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class HUD extends Module
{
    public static int arrayColor1;
    public static int arrayListColor;
    public static int ylevel;
    
    public HUD() {
        super("HUD", 0, false, Category.Render, "Heads up display");
        Client.instance.setmgr.rSetting(new Setting("Pulse", this, true));
        Client.instance.setmgr.rSetting(new Setting("Red", this, 255.0, 0.0, 255.0, false));
        Client.instance.setmgr.rSetting(new Setting("Green", this, 255.0, 0.0, 255.0, false));
        Client.instance.setmgr.rSetting(new Setting("Blue", this, 255.0, 0.0, 255.0, false));
    }
    
    public static void draw() {
        if (Client.instance.moduleManager.getModuleByName("HUD").isEnabled()) {
            final ScaledResolution sr = new ScaledResolution(HUD.mc);
            Collections.sort(Client.instance.moduleManager.getModules(), new ModuleComparator());
            if (Client.instance.setmgr.getSettingByName("Pulse").getValBoolean()) {
                HUD.arrayListColor = ColorUtil.fadeBetween(HUD.arrayColor1, ColorUtil.darker(HUD.arrayColor1, 0.5f), (System.currentTimeMillis() + 500L) % 1000L / 500.0f);
            }
            else {
                HUD.arrayListColor = HUD.arrayColor1;
            }
            if (Client.instance.HUDName == null) {
                Client.instance.HUDName = "Lavish";
            }
            HUD.mc.fontRendererObj.drawStringWithShadow(String.valueOf(Client.instance.HUDName.charAt(0)) + EnumChatFormatting.WHITE + Client.instance.HUDName.substring(1), 4.0f, 4.0f, HUD.arrayListColor);
            if (Client.instance.uid != null) {
                HUD.mc.fontRendererObj.drawStringWithShadow("UID: " + EnumChatFormatting.GRAY + Client.instance.uid, (float)(sr.getScaledWidth() - 44), (float)(sr.getScaledHeight() - 20 - HUD.ylevel), -1);
            }
            else {
                HUD.mc.fontRendererObj.drawStringWithShadow("UID: Null", 910.0f, (float)(500 - HUD.ylevel), -1);
            }
            HUD.mc.fontRendererObj.drawStringWithShadow("BPS: " + EnumChatFormatting.GRAY + MathUtils.getSpeed(), 4.0f, (float)(sr.getScaledHeight() - 20 - HUD.ylevel), -1);
            HUD.mc.fontRendererObj.drawStringWithShadow("Release: " + EnumChatFormatting.GRAY + "v" + Client.instance.version, (float)(sr.getScaledWidth() - 60), (float)(sr.getScaledHeight() - 10 - HUD.ylevel), -1);
            final FontRenderer fontRendererObj = HUD.mc.fontRendererObj;
            final StringBuilder append = new StringBuilder("FPS: ").append(EnumChatFormatting.GRAY);
            final Minecraft mc = HUD.mc;
            fontRendererObj.drawStringWithShadow(append.append(Minecraft.getDebugFPS()).toString(), 4.0f, (float)(sr.getScaledHeight() - 10 - HUD.ylevel), -1);
            Render.color(-1090519040);
            Render.drawRoundedRect(10.0, 30.0, 145.0, 50.0, 5.0f);
            Gui.drawRect(14.0, 30.0, 151.0, 32.0, HUD.arrayListColor);
            HUD.mc.fontRendererObj.drawString("Session Information", 35, 35, -1);
            Gui.drawRect(14.0, 46.0, 151.0, 47.0, -8355712);
            HUD.mc.fontRendererObj.drawString("Session Time: " + EnumChatFormatting.GRAY + TimeUtil.formatMillis((int)(-(Client.instance.startTime - System.currentTimeMillis()))), 17, 50, -1);
            HUD.mc.fontRendererObj.drawString("Username: " + EnumChatFormatting.GRAY + HUD.mc.getSession().getUsername(), 17, 60, -1);
            HUD.mc.fontRendererObj.drawString("Kills: " + EnumChatFormatting.GRAY + Client.instance.kills, 17, 70, -1);
            int count = 0;
            for (final Module m : Client.instance.moduleManager.getEnabledModules()) {
                final double theOffset = count * HUD.mc.fontRendererObj.FONT_HEIGHT;
                HUD.arrayColor1 = new Color((int)Client.instance.setmgr.getSettingByName("Red").getValDouble(), (int)Client.instance.setmgr.getSettingByName("Green").getValDouble(), (int)Client.instance.setmgr.getSettingByName("Blue").getValDouble(), 255).getRGB();
                HUD.arrayListColor = -1;
                if (Client.instance.setmgr.getSettingByName("Pulse").getValBoolean()) {
                    HUD.arrayListColor = ColorUtil.fadeBetween(HUD.arrayColor1, ColorUtil.darker(HUD.arrayColor1, 0.5f), (System.currentTimeMillis() + count * 100) % 1000L / 500.0f);
                }
                else {
                    HUD.arrayListColor = HUD.arrayColor1;
                }
                Gui.drawRect(sr.getScaledWidth() - HUD.mc.fontRendererObj.getStringWidth(m.name) - 6, theOffset, sr.getScaledWidth() + 1, HUD.mc.fontRendererObj.FONT_HEIGHT + theOffset, 1342177280);
                Gui.drawRect((int)(sr.getScaledWidth() - 1.5), theOffset, sr.getScaledWidth() + 1, HUD.mc.fontRendererObj.FONT_HEIGHT + theOffset, HUD.arrayListColor);
                HUD.mc.fontRendererObj.drawStringWithShadow(m.getName(), (float)(sr.getScaledWidth() - HUD.mc.fontRendererObj.getStringWidth(m.name) - 4), (float)(4 + count * HUD.mc.fontRendererObj.FONT_HEIGHT - 4), HUD.arrayListColor);
                ++count;
            }
        }
    }
    
    public static class ModuleComparator implements Comparator<Module>
    {
        @Override
        public int compare(final Module arg0, final Module arg1) {
            if (HUD.mc.fontRendererObj.getStringWidth(arg0.name) > HUD.mc.fontRendererObj.getStringWidth(arg1.name)) {
                return -1;
            }
            if (HUD.mc.fontRendererObj.getStringWidth(arg0.name) < HUD.mc.fontRendererObj.getStringWidth(arg1.name)) {
                return 1;
            }
            return 0;
        }
    }
}
