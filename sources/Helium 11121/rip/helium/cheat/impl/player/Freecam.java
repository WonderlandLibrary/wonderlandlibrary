package rip.helium.cheat.impl.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MovementInput;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.*;

import java.util.Objects;

public class Freecam extends Cheat {
    private double x, y, z, yaw, pitch;

    public Freecam() {
        super("Freecam", "Just freecam", CheatCategory.PLAYER);
    }

    @Override
    public void onEnable() {
        if (Objects.nonNull(mc.theWorld)) {
            this.x = mc.thePlayer.posX;
            this.y = mc.thePlayer.posY;
            this.z = mc.thePlayer.posZ;
            this.yaw = mc.thePlayer.rotationYaw;
            this.pitch = mc.thePlayer.rotationPitch;
            EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
            entityOtherPlayerMP.inventory = mc.thePlayer.inventory;
            entityOtherPlayerMP.inventoryContainer = mc.thePlayer.inventoryContainer;
            entityOtherPlayerMP.setPositionAndRotation(this.x, mc.thePlayer.getEntityBoundingBox().minY, this.z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            entityOtherPlayerMP.rotationYawHead = mc.thePlayer.rotationYawHead;
            entityOtherPlayerMP.setSneaking(mc.thePlayer.isSneaking());

            mc.theWorld.addEntityToWorld(-6969, entityOtherPlayerMP);
        }
    }

    @Override
    public void onDisable() {
        if (Objects.nonNull(mc.theWorld)) {
            mc.thePlayer.jumpMovementFactor = 0.02f;
            mc.thePlayer.setPosition(this.x, this.y, this.z);
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, mc.thePlayer.onGround));
            mc.thePlayer.noClip = false;
            mc.theWorld.removeEntityFromWorld(-6969);
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.rotationPitch = (float) pitch;
            mc.thePlayer.rotationYaw = (float) yaw;
            yaw = pitch = 0;
        }
        mc.renderGlobal.loadRenderers();
    }

    @Collect
    public void onUpdate(PlayerUpdateEvent event) {
        mc.thePlayer.setVelocity(0.0, 0.0, 0.0);
        mc.thePlayer.jumpMovementFactor = 1;
        if (mc.currentScreen == null) {
            if (GameSettings.isKeyDown(mc.gameSettings.keyBindJump)) {
                mc.thePlayer.motionY += 1;
            }
            if (GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
                mc.thePlayer.motionY -= 1;
            }
        }
        mc.thePlayer.noClip = true;
        mc.thePlayer.renderArmPitch = 5000.0f;
    }

    @Collect
    public void onMotion(PlayerMoveEvent event) {
        setMoveSpeed(event, 1);
        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && !GameSettings.isKeyDown(mc.gameSettings.keyBindJump)) {
            event.setY(1 * 2.0 * -(mc.thePlayer.rotationPitch / 180.0f) * (int) MovementInput.moveForward);
        }
    }

    @Collect
    public void onPacket(SendPacketEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer) event.setCancelled(true);
        if (event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C0APacketAnimation)
            event.setCancelled(true);
    }

    @Collect
    public void onBB(BoundingBoxEvent event) {
        event.setBoundingBox(null);
    }

    @Collect
    public void onPush(BlockPushEvent event) {
        event.setCancelled(true);
    }

    private void setMoveSpeed(final PlayerMoveEvent event, final double speed) {
        double forward = MovementInput.moveForward;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
        }
    }
}
