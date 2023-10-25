/* 3eLeHyy#0089 */

package org.dreamcore.client.feature.impl.misc;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.event.events.impl.packet.EventSendPacket;
import org.dreamcore.client.event.events.impl.player.EventFullCube;
import org.dreamcore.client.event.events.impl.player.EventPush;
import org.dreamcore.client.event.events.impl.player.EventUpdateLiving;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

public class FreeCam extends Feature {

    public NumberSetting speed;
    public BooleanSetting AntiAction = new BooleanSetting("AntiAction", false, () -> true);
    public BooleanSetting autoDamageDisable = new BooleanSetting("Auto Damage Disable", false, () -> true);
    private float old;
    private double oldX;
    private double oldY;
    private double oldZ;

    public FreeCam() {
        super("FreeCam", "Позволяет летать в свободной камере", Type.Misc);
        speed = new NumberSetting("Flying Speed", 0.5F, 0.1F, 1F, 0.1F, () -> true);
        addSettings(speed, autoDamageDisable, AntiAction);
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(old);
        mc.player.noClip = false;
        mc.renderGlobal.loadRenderers();
        mc.player.noClip = false;
        mc.player.setPositionAndRotation(oldX, oldY, oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
        mc.world.removeEntityFromWorld(-69);
        mc.player.motionZ = 0;
        mc.player.motionX = 0;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        oldX = mc.player.posX;
        oldY = mc.player.posY;
        oldZ = mc.player.posZ;
        mc.player.noClip = true;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.posY -= 0;
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-69, fakePlayer);
        super.onEnable();
    }

    @EventTarget
    public void onFullCube(EventFullCube event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onUpdate(EventUpdateLiving event) {

        if (autoDamageDisable.getBoolValue() && mc.player.hurtTime > 0 && dreamcore.instance.featureManager.getFeatureByClass(FreeCam.class).getState()) {
            dreamcore.instance.featureManager.getFeatureByClass(FreeCam.class).toggle();
        }
        mc.player.noClip = true;
        mc.player.onGround = false;
        mc.player.capabilities.setFlySpeed(speed.getNumberValue() / 5);
        mc.player.capabilities.isFlying = true;
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
        mc.player.setSprinting(false);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (AntiAction.getBoolValue()) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (AntiAction.getBoolValue()) {
            if (event.getPacket() instanceof CPacketPlayer) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketPlayer.Position) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketEntityAction) {
                event.setCancelled(true);
            }
        }
    }
}
