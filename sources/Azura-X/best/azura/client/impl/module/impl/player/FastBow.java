package best.azura.client.impl.module.impl.player;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.ModeValue;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;

@SuppressWarnings("unused")
@ModuleInfo(name = "Fast Bow", description = "Use bows faster", category = Category.PLAYER)
public class FastBow extends Module {
    private final ModeValue mode = new ModeValue("Mode", "The mode", "Vanilla", "Vanilla", "Minigun");

    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
            switch (mode.getObject()) {
                case "Vanilla": {
                    int n = 32;
                    for (int i = 1; i <= n; ++i)
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer(false));
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    break;
                }
                case "Minigun": {
                    for (int i = 1; i <= 32; ++i)
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer(false));
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    break;
                }
            }
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
