package today.flux.gui.hud.window.impl;

import net.minecraft.client.Minecraft;
import today.flux.Flux;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.gui.hud.window.HudWindow;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.implement.World.StaffAnalyser;

public class WindowSessInfo extends HudWindow {

    public WindowSessInfo() {
        super("SessionInfo", 5, 25, 135, 65, "Session info", "", 12, 1, .5f);
    }

    @Override
    public void draw() {
        super.draw();

        String time;
        if(Minecraft.getMinecraft().isSingleplayer()) {
            time = "SinglePlayer";
        } else {
            long durationInMillis = System.currentTimeMillis() - Flux.playTimeStart;
            long second = (durationInMillis / 1000) % 60;
            long minute = (durationInMillis / (1000 * 60)) % 60;
            long hour = (durationInMillis / (1000 * 60 * 60)) % 24;
            time = String.format("%02dh %02dm %02ds", hour, minute, second);
        }

        FontManager.icon20.drawString("F", x + 5, y + 15, 0xffffffff);
        FontManager.sans16.drawString("PlayTime: " + time, x + 20, y + 14, 0xffffffff);

        FontManager.icon20.drawString("G", x + 5, y + 31, 0xffffffff);
        FontManager.sans16.drawString("Kills: " + KillAura.killed, x + 20, y + 30, 0xffffffff);

        FontManager.icon25.drawString("H", x + 4, y + 44, 0xffffffff);
        FontManager.sans16.drawString("Win / Total: " + ModuleManager.autoGGMod.win + " / " + ModuleManager.autoGGMod.total, x + 20, y + 45, 0xffffffff);

        FontManager.icon25.drawString("I", x + 4, y + 59, 0xffffffff);
        FontManager.sans16.drawString("Staff ban last " + ((int) StaffAnalyser.delay.getValueState()) + "s: " + (ModuleManager.staffAnalyserMod.isEnabled() ? ModuleManager.staffAnalyserMod.lastBanned : "Disabled"), x + 20, y + 59.5f, 0xffffffff);
    }
}
