package koks.module.visual;


import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.manager.value.annotation.Value;
import koks.event.GlintEvent;

import java.awt.*;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
@Module.Info(name = "Glint", description = "Custom Enchant Colors", category = Module.Category.VISUAL)
public class Glint extends Module implements Module.NotUnToggle {


    @Value(name = "Color", colorPicker = true)
    int color = Color.green.getRGB();

    @Value(name = "Rainbow")
    boolean rainbow = false;

    @Value(name = "Rainbow-Speed", displayName = "Speed", minimum = 1, maximum = 10000)
    int rainbowSpeed = 1000;

    @Value(name = "Rainbow-Saturation", displayName = "Saturation", minimum = 0, maximum = 1)
    double saturation = 1;

    @Value(name = "Rainbow-Brightness", displayName = "Brightness", minimum = 0, maximum = 1)
    double brightness = 1;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        if(name.contains("-")) {
            if(name.contains("Rainbow"))
                return rainbow;
        }
        switch (name) {
            case "Color":
                return !rainbow;
        }
        return super.isVisible(value, name);
    }

    @Override
    @Event.Info(priority = Event.Priority.EXTREME)
    public void onEvent(Event event) {
        if (event instanceof final GlintEvent glintEvent) {
            if (rainbow)
                glintEvent.setColor(getRainbow(200, rainbowSpeed, (float) saturation, (float) brightness).getRGB());
            else
                glintEvent.setColor(color);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}

