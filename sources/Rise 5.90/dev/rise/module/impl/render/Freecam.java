/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.BlockCollideEvent;
import dev.rise.event.impl.other.MoveButtonEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.MoveUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Freecam", description = "Allows you to view your surroundings", category = Category.RENDER)
public final class Freecam extends Module {

    private final BooleanSetting fly = new BooleanSetting("Fly", this, true);
    private final NumberSetting flySpeed = new NumberSetting("Fly Speed", this, 1, 0.1, 9.5, 0.1);
    private final BooleanSetting noClip = new BooleanSetting("No Clip", this, true);

    private EntityOtherPlayerMP freecamEntity;
    public static double startX, startY, startZ;
    private float startYaw, startPitch;

    @Override
    public void onUpdateAlwaysInGui() {
        flySpeed.hidden = noClip.hidden = !fly.isEnabled();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.ticksExisted < 5) {
            this.toggleModule();
            this.registerNotification("Disabled " + this.getModuleInfo().name() + " due to world change.");
            return;
        }

        if (noClip.isEnabled() && fly.isEnabled())
            mc.thePlayer.noClip = true;

        if (fly.isEnabled()) {
            mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? flySpeed.getValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -flySpeed.getValue() : 0;

            if (MoveUtil.isMoving())
                MoveUtil.strafe(flySpeed.getValue());
            else
                MoveUtil.stop();
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Packet<?> p = event.getPacket();

        if (!(p instanceof C01PacketChatMessage || p instanceof C08PacketPlayerBlockPlacement || p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive || p instanceof C09PacketHeldItemChange || p instanceof C12PacketUpdateSign || p instanceof C10PacketCreativeInventoryAction || p instanceof C0EPacketClickWindow || p instanceof C0DPacketCloseWindow || p instanceof C16PacketClientStatus || p instanceof C0APacketAnimation || p instanceof C02PacketUseEntity))
            event.setCancelled(true);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();

        if (p instanceof S08PacketPlayerPosLook && mc.thePlayer.ticksExisted > 1)
            event.setCancelled(true);
    }

    @Override
    public void onMoveButton(final MoveButtonEvent event) {
        if (fly.isEnabled())
            event.setSneak(false);
    }

    @Override
    public void onBlockCollide(final BlockCollideEvent event) {
        if (noClip.isEnabled() && fly.isEnabled())
            event.setCollisionBoundingBox(null);
    }

    @Override
    protected void onEnable() {
        freecamEntity = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        freecamEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        freecamEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
        freecamEntity.setSprinting(mc.thePlayer.isSprinting());
        freecamEntity.setInvisible(mc.thePlayer.isInvisible());
        freecamEntity.setSneaking(mc.thePlayer.isSneaking());

        mc.theWorld.addEntityToWorld(freecamEntity.getEntityId(), freecamEntity);

        startPitch = mc.thePlayer.rotationPitch;
        startYaw = mc.thePlayer.rotationYaw;
        startX = mc.thePlayer.posX;
        startY = mc.thePlayer.posY;
        startZ = mc.thePlayer.posZ;
    }

    @Override
    protected void onDisable() {
        if (freecamEntity != null) {
            mc.theWorld.removeEntityFromWorld(freecamEntity.getEntityId());
            mc.thePlayer.setPositionAndRotation(startX, startY, startZ, startYaw, startPitch);
        }
        mc.thePlayer.noClip = false;
        mc.thePlayer.motionY = 0;
        MoveUtil.strafe(0);
    }
}
