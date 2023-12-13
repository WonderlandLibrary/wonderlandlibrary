package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;

import java.awt.*;

/*
 * Created on 4/15/2021 by dirt.
 */
@Module.Info(name = "Chams", description = "You can see entities trough walls", category = Module.Category.VISUAL)
public class Chams extends Module{

    @Value(name = "Default Color", colorPicker = true)
    int defaultColor = new Color(7, 224, 37, 255).getRGB();

    @Value(name = "ThroughWalls Color", colorPicker = true)
    int throughWallsColor = Color.red.getRGB();

    @Override
    @Event.Info
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
