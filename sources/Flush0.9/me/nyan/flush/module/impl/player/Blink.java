package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.util.ArrayList;

public class Blink extends Module {
    private final ArrayList<Packet<?>> blinkpackets = new ArrayList<>();

    private final BooleanSetting playerCopy = new BooleanSetting("Player Copy", this, true);

    public Blink() {
        super("Blink", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (mc.theWorld == null) {
            toggle();
        }

        if (playerCopy.getValue()) {
            EntityOtherPlayerMP entity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());

            entity.rotationYawHead = mc.thePlayer.rotationYawHead;
            entity.renderYawOffset = mc.thePlayer.renderYawOffset;
            entity.copyLocationAndAnglesFrom(mc.thePlayer);

            mc.theWorld.addEntityToWorld(-1, entity);
        }

        blinkpackets.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.theWorld.removeEntityFromWorld(-1);

        if (mc.isIntegratedServerRunning())
            return;

        for (Packet<?> packet : blinkpackets)
            mc.getNetHandler().addToSendQueue(packet);
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.isOutgoing() && !(e.getPacket() instanceof C01PacketChatMessage)) {
            blinkpackets.add(e.getPacket());
            e.cancel();
        }
    }
}
