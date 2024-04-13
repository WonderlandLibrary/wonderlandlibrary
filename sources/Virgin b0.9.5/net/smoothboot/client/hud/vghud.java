package net.smoothboot.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.client.ClientSetting;
import net.smoothboot.client.module.client.Hide;
import net.smoothboot.client.module.modmanager;

import java.awt.*;

public class vghud {

    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void render(DrawContext context, float tickDelta) {
        if (ClientSetting.displayFPS && mc.currentScreen == null && !Hide.hidden) {
            context.drawText(mc.textRenderer, "FPS: " + String.valueOf(mc.getCurrentFps()), 2, 2, new Color(255, 255, 255, 220).getRGB(), true);
        }
    }

    public static void renderArrayList(DrawContext context) {
        int index = 0;
        if (ClientSetting.displayFPS) index = 12;
        if (ClientSetting.modList && mc.currentScreen == null && !Hide.hidden) {
            for (Mod mod : modmanager.INSTANCE.getEnabledModules()) {
                context.drawText(mc.textRenderer, mod.getDisplayname(), 2, 2 + index, new Color(frame.menured, frame.menugreen, frame.menublue, 220).getRGB(), true);
                if (ClientSetting.listBG) {
                    context.fill(2 + mod.getDisplayname().length() * 6, index + 12, mod.getDisplayname().length() * 6, index, new Color(frame.menured, frame.menugreen, frame.menublue).getRGB());
                    context.fill(0, 12 + index, 2 + mod.getDisplayname().length() * 6, index, new Color(0, 0, 0, 100).getRGB());
                }
                index+=12;
            }
        }
    }
}