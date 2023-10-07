package best.azura.client.impl.module.impl.player;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;

@SuppressWarnings("unused")
@ModuleInfo(name = "Fast Use", description = "Use items faster", category = Category.PLAYER)
public class FastUse extends Module {


    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (!mc.thePlayer.isBlocking() && mc.thePlayer.isUsingItem() && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword || mc.thePlayer.getHeldItem().getItem() instanceof ItemBow)) {
            int n = 32;
            for (int i = 1; i <= n; ++i) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer(false));
            }
            mc.thePlayer.clearItemInUse();
        }
    };

    @Override
    public void onEnable() {
        super.onEnable();
        Client.INSTANCE.getEventBus().register(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Client.INSTANCE.getEventBus().unregister(this);
    }
}
