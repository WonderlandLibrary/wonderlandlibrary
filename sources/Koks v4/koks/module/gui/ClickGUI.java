package koks.module.gui;

import koks.Koks;
import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.event.UpdateEvent;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "ClickGUI", description = "Its open the ClickGUI", category = Module.Category.GUI, key = Keyboard.KEY_RSHIFT)
public class ClickGUI extends Module {

    @Value(name = "Mode", modes = {"Periodic", "Sigma"})
    String mode = "Normal";

    @Value(name = "Color", colorPicker = true)
    int color = new Color(7, 224, 37, 255).getRGB();

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Color":
                return !mode.equalsIgnoreCase("Periodic");
        }
        return super.isVisible(value, name);
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if(event instanceof UpdateEvent) {
            GuiScreen screen = null;
            switch(mode) {
                case "Periodic":
                    screen = Koks.getKoks().periodicClickGUI;
                    break;
                default:
                    screen = Koks.getKoks().sigmaClickGUI;
                    break;
            }
            mc.displayGuiScreen(screen);
            setToggled(false);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
