package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRespawn
        extends Module {
    public Setting<Boolean> antiDeathScreen = this.register(new Setting<Boolean>("AntiDeathScreen", true));
    public Setting<Boolean> deathCoords = this.register(new Setting<Boolean>("DeathCoords", false));
    public Setting<Boolean> respawn = this.register(new Setting<Boolean>("Respawn", true));

    public AutoRespawn() {
        super("AutoRespawn", "Respawns you when you die.", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (this.deathCoords.getValue().booleanValue() && event.getGui() instanceof GuiGameOver) {
                Command.sendMessage(String.format("You died at x %d y %d z %d", (int) AutoRespawn.mc.player.posX, (int) AutoRespawn.mc.player.posY, (int) AutoRespawn.mc.player.posZ));
            }
            if (this.respawn.getValue() != false && AutoRespawn.mc.player.getHealth() <= 0.0f || this.antiDeathScreen.getValue().booleanValue() && AutoRespawn.mc.player.getHealth() > 0.0f) {
                event.setCanceled(true);
                AutoRespawn.mc.player.respawnPlayer();
            }
        }
    }
}

