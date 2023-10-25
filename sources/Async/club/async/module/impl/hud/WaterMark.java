package club.async.module.impl.hud;

import club.async.Async;
import club.async.event.impl.Event2D;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import rip.hippo.lwjeb.annotation.Handler;

import java.awt.Color;

@ModuleInfo(name = "WaterMark", description = "Async's WaterMark", category = Category.HUD)
public class WaterMark extends Module {

    @Handler
    public void on2D(Event2D event2D) {
        final String text = Async.INSTANCE.getName() + " " + Async.INSTANCE.getVersion() + " | " + (mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() == 0 ? "offline" : mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + "ms");
        Gui.drawRect(4, 4, 11 + Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(text), 21, new Color(60,60,60));
        Gui.drawRect(5, 5, 10 + Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(text), 20, new Color(20,20,20));
        Gui.drawRect(6, 6, 9 + Async.INSTANCE.getFontManager().getFont("Arial 20").getWidth(text), 7, ColorUtil.getMainColor());
        Async.INSTANCE.getFontManager().getFont("Arial 20").drawString(text.replace("A", "A§f"), 8, 9, ColorUtil.getMainColor());
    }

}
