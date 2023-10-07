package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.event.GuiHandleEvent;
import net.minecraft.client.gui.GuiGameOver;

@Module.Info(name = "AutoRespawn", category = Module.Category.PLAYER, description = "You respawn automatically")
public class AutoRespawn extends Module implements Module.NotBypass {

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof GuiHandleEvent) {
            if(mc.currentScreen instanceof GuiGameOver)
                getPlayer().respawnPlayer();
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
