package dev.tenacity.module.impl.combat;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;

public class Reach extends Module {

    public final NumberSetting reach = new NumberSetting("Reach", 3.5, 0, 8.0, 0.01);


    private final ModeSetting mode = new ModeSetting("Mode", "Normal", "Packet");
    public Reach() {
        super("Reach", "Makes you reach further", ModuleCategory.COMBAT);
        initializeSettings(mode, reach);
    }

    private final IEventListener<UpdateEvent> onUpdateEvent = event -> {
        setSuffix(String.valueOf(reach.getCurrentValue()));
    };

    
}
