package best.azura.client.impl.module.impl.other;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.client.impl.module.impl.combat.KillAura;
import best.azura.client.impl.module.impl.movement.Flight;
import best.azura.client.impl.module.impl.movement.LongJump;
import best.azura.client.impl.module.impl.movement.Phase;
import best.azura.client.impl.module.impl.player.ChestStealer;
import best.azura.client.impl.module.impl.player.InvManager;
import best.azura.client.impl.module.impl.player.Scaffold;
import best.azura.client.impl.value.BooleanValue;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

@ModuleInfo(name = "Auto Disable", description = "Disable modules on world change.", category = Category.OTHER)
public class AutoDisable extends Module {

    private final BooleanValue disableAura = new BooleanValue("Disable Aura", "Disable Aura on world change", false),
            disableInvManager = new BooleanValue("Disable Inventory Manager", "Disable Inventory Manager on world change", false),
            disableChestStealer = new BooleanValue("Disable Chest Stealer", "Disable Chest Stealer on world change", false),
            disableLongJump = new BooleanValue("Disable Long Jump", "Disable Long Jump on world change", false),
            disableFlight = new BooleanValue("Disable Flight", "Disable Flight on world change", false),
            disableScaffold = new BooleanValue("Disable Scaffold", "Disable Scaffold on world change", false),
            disableTimer = new BooleanValue("Disable Timer", "Disable Timer on world change", false),
            disablePhase = new BooleanValue("Disable Phase", "Disable Phase on world change", false);

    @SuppressWarnings("unused")
    @EventHandler
    public final Listener<EventWorldChange> eventWorldChangeListener = e -> {
        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Auto Disable", "Disabled modules on world change!", 1000, Type.INFO));
        if (disableAura.getObject()) Client.INSTANCE.getModuleManager().getModule(KillAura.class).setEnabled(false);
        if (disableInvManager.getObject())
            Client.INSTANCE.getModuleManager().getModule(InvManager.class).setEnabled(false);
        if (disableChestStealer.getObject())
            Client.INSTANCE.getModuleManager().getModule(ChestStealer.class).setEnabled(false);
        if (disableLongJump.getObject()) Client.INSTANCE.getModuleManager().getModule(LongJump.class).setEnabled(false);
        if (disableFlight.getObject()) Client.INSTANCE.getModuleManager().getModule(Flight.class).setEnabled(false);
        if (disableScaffold.getObject()) Client.INSTANCE.getModuleManager().getModule(Scaffold.class).setEnabled(false);
        if (disableTimer.getObject()) Client.INSTANCE.getModuleManager().getModule(Timer.class).setEnabled(false);
        if (disablePhase.getObject()) Client.INSTANCE.getModuleManager().getModule(Phase.class).setEnabled(false);
    };
}