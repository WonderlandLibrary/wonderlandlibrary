package io.github.raze.modules.collection.movement;

import io.github.raze.Raze;
import io.github.raze.modules.collection.movement.flight.ModeFlight;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.NumberSetting;

public class Flight extends AbstractModule {

    private final ArraySetting mode;
    public final NumberSetting speed;

    private ModeFlight modeFlight;

    public Flight() {
        super("Flight", "Allows you to fly.", ModuleCategory.MOVEMENT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Vanilla", "Vanilla", "Vulcan AirJump", "Vulcan Glide", "Verus", "Verus AirJump", "Verus Fast", "Packet", "Blink"),

                speed = new NumberSetting(this, "Speed", 1, 5, 2)
                        .setHidden(() -> !mode.compare("Vanilla"))
        );

        ModeFlight.init();
        this.modeFlight = ModeFlight.getMode(mode.get());
        this.modeFlight.setParent(this);
    }

    @Override
    public void onEnable() {
        this.modeFlight = ModeFlight.getMode(mode.get());
        this.modeFlight.setParent(this);
        this.modeFlight.onEnable();
        Raze.INSTANCE.managerRegistry.eventManager.subscribe(this.modeFlight);
    }

    @Override
    public void onDisable() {
        this.modeFlight.onDisable();
        Raze.INSTANCE.managerRegistry.eventManager.unsubscribe(this.modeFlight);
    }
}
