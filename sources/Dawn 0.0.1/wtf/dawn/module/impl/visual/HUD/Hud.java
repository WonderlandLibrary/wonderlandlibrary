package wtf.dawn.module.impl.visual.HUD;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import wtf.dawn.utils.font.FontUtil;
import wtf.dawn.utils.ColorUtil;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Comparator;



public class Hud {
    public Minecraft mc = Minecraft.getMinecraft();

    public void draw() {
        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer fr = mc.fontRendererObj;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();



        int count = 0;

        GlStateManager.pushMatrix();
        GlStateManager.translate(4, 4, 0);
        GlStateManager.translate(-4, -4, 0);
        LocalTime time = LocalTime.now();
       // old mode FontUtil.normal.drawStringWithShadow("Dawn " + "§f[§f" + time.withSecond(0).withNano(0) + "§f] " + "§f[§f" + mc.getDebugFPS() + "§f]", 3, 3, -1);
        FontUtil.normal.drawStringWithShadow("d", 3, 3, ColorUtil.getRainbow(4, 0.8f, 1, count * 200));
        FontUtil.normal.drawStringWithShadow("awnsense", 9, 3, -1);

        GlStateManager.popMatrix();


    }
}
