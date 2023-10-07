package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.module.impl.other.disabler.*;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.util.modes.ModeUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

import java.util.Arrays;

@ModuleInfo(name = "Disabler", category = Category.OTHER, description = "Disable anti cheat stuff")
public class Disabler extends Module {

    public static final ModeValue mode = new ModeValue("Mode", "Change the mode of the Disabler", "");

    public Disabler() {
        super();
        ModeUtil.registerModuleModes(this, Arrays.asList(new WatchdogDisablerImpl(), new WatchdogDevDisabler(),
                new LunarDisablerImpl(), new ResourceDisablerImpl(),
                new AntiACDisablerImpl(), new VerusTestDisablerImpl(),
                new VerusTest2DisablerImpl(), new GhostDisablerImpl(),
                new MatrixDisablerImpl(), new TransactionDisabler(),
                new KeepaliveDisablerImpl(), new AbilitiesDisablerImpl(),
                new VerusCombatDisabler(), new VerusScaffoldDisabler(),
                new GommeDisablerImpl(), new IntroubleDisablerImpl(),
                new MorganDisabler(), new HycraftDisablerImpl()), mode, true);
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
