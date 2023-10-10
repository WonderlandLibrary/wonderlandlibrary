package client.module.impl.render;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.Render2DEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.util.MamaUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;

@ModuleInfo(name = "HUD", description = "Heads-up display", category = Category.RENDER)
public class HUD extends Module {
    @EventLink
    public final Listener<Render2DEvent> onRender2D = event -> {
      //  GlStateManager.rotate(4,82,25,53);
        final String splitter = EnumChatFormatting.GRAY + " | " + EnumChatFormatting.RESET;
        final String colon = EnumChatFormatting.GRAY + ": " + EnumChatFormatting.RESET;
        final String s = new DecimalFormat("#0.00").format(Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * 20.0F * mc.timer.timerSpeed);
        final String s1 = Client.NAME + splitter + "BPS" + colon + s + splitter + "User" + colon + MamaUtil.getUsername();
        final int i = mc.fontRendererObj.getStringWidth(s1);
        Gui.drawRect(0, 0, i + 3, 11, Integer.MIN_VALUE);
        mc.fontRendererObj.drawString(s1, 2, 2, Integer.MAX_VALUE);
    };
}
