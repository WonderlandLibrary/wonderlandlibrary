package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.movement.MovementUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Freecam extends Module {
    private final NumberSetting speed = new NumberSetting("Fly Speed", this, 8, 1, 10, 0.1);

    public Freecam() {
        super("Freecam", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EntityOtherPlayerMP entity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());

        entity.rotationYawHead = mc.thePlayer.rotationYawHead;
        entity.renderYawOffset = mc.thePlayer.renderYawOffset;
        entity.copyLocationAndAnglesFrom(mc.thePlayer);
        entity.rotationYawHead = mc.thePlayer.rotationYawHead;

        mc.theWorld.addEntityToWorld(-1337, entity);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.theWorld.removeEntityFromWorld(-1337);
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (MovementUtils.isMoving()) {
            MovementUtils.hClip(speed.getValue() / 2);
        } else {
            MovementUtils.stopMotion();
        }

        mc.thePlayer.motionY = 0;

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            MovementUtils.vClip(speed.getValue() / 4);
        } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            MovementUtils.vClip(-speed.getValue() / 4);
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.isOutgoing() && e.getPacket() instanceof C03PacketPlayer) {
            e.setCancelled(true);
        }
    }
}
