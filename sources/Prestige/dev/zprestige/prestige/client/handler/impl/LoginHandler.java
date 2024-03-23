/*
Prestige cooked by Walmart Solutions
*/
package dev.zprestige.prestige.client.handler.impl;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.RunEvent;
import dev.zprestige.prestige.client.handler.Handler;
import dev.zprestige.prestige.client.protection.HWIDGrabber;
import dev.zprestige.prestige.client.protection.auth.HWIDLogin;
import dev.zprestige.prestige.client.ui.drawables.gui.screens.impl.LoginScreen;
import dev.zprestige.prestige.client.util.MC;
import net.minecraft.client.MinecraftClient;

public class LoginHandler implements MC, Handler {

    @Override
    public void register() {
        Prestige.Companion.getEventBus().registerListener(this);
    }

    @EventListener
    public void event(RunEvent event) {
        if (Prestige.Companion.getSession() == null) {
            new HWIDLogin().run(HWIDGrabber.getHWID());

        }
        if (getMc().currentScreen != null && !(getMc().currentScreen instanceof LoginScreen) && Prestige.Companion.getSession() == null) {
            getMc().setScreen(new LoginScreen());
        }
    }

    @Override
    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }
}
