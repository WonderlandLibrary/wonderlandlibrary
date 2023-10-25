package ru.smertnix.celestial.feature.impl.player;

import java.awt.Color;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.packet.EventSendPacket;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.event.events.impl.player.EventUpdateLiving;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.movement.MovementUtils;

public class FreeCamera extends Feature {

    private final NumberSetting speed = new NumberSetting("Flying Speed", 0.5F, 0.1F, 1F, 0.1F, () -> true);

    private float old;
    double oldX, oldY, oldZ;

    public FreeCamera() {
        super("Free Camera", "Позволяет смотреть через стены летая ", FeatureCategory.Player);
        addSettings(speed);
    }

    @Override
    public void onEnable() {
        oldX = mc.player.posX;
        oldY = mc.player.posY;
        oldZ = mc.player.posZ;
        oldY = (int)mc.player.posY;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.posY -= 0.0;
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-69, fakePlayer);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.world.removeEntityFromWorld(-69);
        mc.player.motionZ = 0.0;
        mc.player.motionX = 0.0;
        mc.player.noClip = false;
        mc.player.setPositionAndRotation(oldX, oldY, oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
        super.onDisable();
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (mc.player == null || mc.world == null || mc.player.ticksExisted < 1) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
        	 event.setCancelled(true);
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (mc.player == null || mc.world == null || mc.player.ticksExisted < 1) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketEntityAction) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onUpdate(EventUpdateLiving event) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        mc.player.noClip = true;
        mc.player.onGround = false;
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY = speed.getNumberValue() / 1.5f;
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY = -speed.getNumberValue() / 1.5f;
        }
        MovementUtils.setSpeed(speed.getNumberValue());
        mc.player.capabilities.isFlying = true;
    }
}
