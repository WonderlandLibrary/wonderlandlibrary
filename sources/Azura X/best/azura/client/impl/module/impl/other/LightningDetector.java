package best.azura.client.impl.module.impl.other;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.other.ChatUtil;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;

@ModuleInfo(name = "Lightning Detector", category = Category.OTHER, description = "Detects lightning strikes")
public class LightningDetector extends Module {

    @EventHandler
    public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
        if (e.getPacket() instanceof S2CPacketSpawnGlobalEntity) {
            S2CPacketSpawnGlobalEntity s2C = e.getPacket();
            if (s2C.func_149053_g() == 1) {
                double d0 = (double)s2C.func_149051_d() / 32.0D;
                double d1 = (double)s2C.func_149050_e() / 32.0D;
                double d2 = (double)s2C.func_149049_f() / 32.0D;
                ChatUtil.sendChat("Detected lightning strike at " + d0 + ", " + d1 + ", " + d2);
                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Lightning Detector", "Detected lightning strike at " + d0 + ", " + d1 + ", " + d2, 5000, Type.INFO));
            }
        }
    };

}