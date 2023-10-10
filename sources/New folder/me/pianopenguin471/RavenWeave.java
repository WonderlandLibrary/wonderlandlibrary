package me.pianopenguin471;

import me.pianopenguin471.command.GUICommand;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.CommandBus;
import net.weavemc.loader.api.event.*;
import ravenweave.client.main.Raven;
import ravenweave.client.module.Module;

public class RavenWeave implements ModInitializer {
    @Override
    public void preInit() {
        CommandBus.register(new GUICommand());
        EventBus.subscribe(this);
    }

    @SubscribeEvent
    public void onKeyPress(KeyboardEvent e) {
        for (Module module: Raven.moduleManager.getModules()) {
            module.keybind();
        }
    }

    @SubscribeEvent
    public void onGameStart(StartGameEvent.Post e) {
        Raven.init();
    }

    @SubscribeEvent
    public void onShutdown(ShutdownEvent e) {
        Raven.moduleManager.getModuleByName("Blink").disable();
    }
}
