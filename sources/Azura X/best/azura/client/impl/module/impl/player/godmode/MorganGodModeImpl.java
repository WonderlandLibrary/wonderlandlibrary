package best.azura.client.impl.module.impl.player.godmode;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.player.GodMode;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.player.MovementUtil;

public class MorganGodModeImpl implements ModeImpl<GodMode> {

    @Override
    public GodMode getParent() {
        return (GodMode) Client.INSTANCE.getModuleManager().getModule(GodMode.class);
    }

    @Override
    public String getName() {
        return "Morgan";
    }

    @Override
    public void onEnable() {
        if (!mc.thePlayer.onGround) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("God Mode", "Please stand on ground.", 4000, Type.WARNING));
            getParent().setEnabled(false);
            return;
        }
        MovementUtil.vClip(10000);
    }

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        if (e.isPost()) {
        }
    };

    @Override
    public void onDisable() {

    }
}