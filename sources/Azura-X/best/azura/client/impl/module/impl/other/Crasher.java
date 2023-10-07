package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.other.crasher.MVCrasher;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.util.modes.ModeUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

import java.util.Arrays;

@ModuleInfo(name = "Crasher", category = Category.OTHER, description = "Crash servers")
public class Crasher extends Module {

    public static final ModeValue mode = new ModeValue("Mode", "Change the mode of the Disabler", "");

    public Crasher() {
        super();
        ModeUtil.registerModuleModes(this, Arrays.asList(new MVCrasher()), mode, true);
    }

    @EventHandler
    public Listener<EventUpdate> eventUpdateListener = e -> {
        setSuffix(mode.getObject());
    };


    @Override
    public void onEnable() {
        ModeUtil.onEnable(this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        ModeUtil.onDisable(this);
        super.onDisable();
    }
}
