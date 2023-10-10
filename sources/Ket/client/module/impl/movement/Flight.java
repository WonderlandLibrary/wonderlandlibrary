package client.module.impl.movement;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.WorldLoadEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.module.impl.movement.flight.*;
import client.value.impl.ModeValue;
import client.value.impl.NumberValue;
import lombok.Getter;

@ModuleInfo(name = "Flight", description = "Grants you the ability to fly", category = Category.MOVEMENT)
public class Flight extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
            .add(new VanillaFlight("Vanilla", this))
            .add(new MushFlight("Mush", this))
            .add(new Mush2Flight("Mush2", this))
            .add(new TestFlight("Test", this))
            .add(new VulcanFlight("Vulcan", this))
            .add(new AGCFlight("AGC", this
            ))
            .setDefault("Vanilla");
    @Getter
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 9.85, 0.1, () -> {
        final String[] modeNames = {"Vanilla"};
        for (final String s : modeNames) if (mode.getValue().getName().equals(s)) return true;
        return false;
    });
    @EventLink
    public final Listener<WorldLoadEvent> onWorldLoad = event -> toggle();
}
