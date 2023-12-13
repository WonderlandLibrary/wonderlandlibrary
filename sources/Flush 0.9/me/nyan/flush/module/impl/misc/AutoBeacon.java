package me.nyan.flush.module.impl.misc;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.NumberSetting;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

public class AutoBeacon extends Module {
    private final NumberSetting speed = new NumberSetting("Speed", this, 2, 0, 2, 1);

    private C07PacketPlayerDigging c07;

    public AutoBeacon() {
        super("AutoBeacon", Category.MISC);
    }

    private boolean b;

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        b = !b;

        if (speed.getValue() == 0 && b) {
            return;
        }

        if (c07 != null && Math.sqrt(mc.thePlayer.getDistanceSqToCenter(c07.getPosition())) <= 6) {
            for (int i = 0; i < Math.max(speed.getValue(), 1); i++) {
                mc.getNetHandler().addToSendQueueNoEvent(new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                        c07.getPosition(), c07.getFacing()
                ));
            }
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof C07PacketPlayerDigging &&
                ((C07PacketPlayerDigging) e.getPacket()).getStatus() ==
                        C07PacketPlayerDigging.Action.START_DESTROY_BLOCK &&
                ((C07PacketPlayerDigging) e.getPacket()).getPosition().getBlock() == Blocks.beacon) {
            c07 = e.getPacket();
        }
    }
}
