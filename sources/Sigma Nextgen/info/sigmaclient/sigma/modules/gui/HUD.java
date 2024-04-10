package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.gui.hud.AnotherArrayList;
import info.sigmaclient.sigma.gui.hud.JelloArrayList;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.RenderModule;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.blurs.RoundRectShader;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class HUD extends Module {
    public static BooleanValue gradient = new BooleanValue("Gradient", true);
    public HUD() {
        super("Hud", Category.Gui, "new hud", true);
     registerValue(gradient);
    }

    @Override
    public void onRenderEvent(RenderEvent event) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String L = " §8- §rIamFrozenMilk §8- §r" + Minecraft.debugFPS + " fps" + " §8-§r " + formatter.format(new Date(System.currentTimeMillis()));
        String s1 = "Nursultan" + L;
        float width = FontUtil.sfuiFontBold17.getStringWidth(s1) + 2;
        int height = 14;
        float d = 0;
        Shader.drawRoundRectWithGlowing(10 - d, 10 - d, 10 + width + d, 10 + height + d, new Color(0.117647059f,0.117647059f,0.117647059f));
        float x = FontUtil.sfuiFontBold17.drawStringChroma("nursultan", 12, 15);
        FontUtil.sfuiFontBold17.drawString(L, 12 + x, 15, new Color(220,220,220).getRGB());
    }
    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
    }
}
