/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.awt.Color;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventFullCube;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.player.EventPush;
import org.celestial.client.event.events.impl.player.EventUpdateLiving;
import org.celestial.client.event.events.impl.render.EventRender2D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class FreeCam
extends Feature {
    public NumberSetting speed = new NumberSetting("Flying Speed", 1.0f, 0.1f, 5.0f, 0.1f, () -> true);
    public BooleanSetting disableOnDamage = new BooleanSetting("Disable on damage", false, () -> true);
    public BooleanSetting clipOnDisable = new BooleanSetting("Clip on disable", false, () -> true);
    public BooleanSetting autoTeleportDisable = new BooleanSetting("Auto teleport disable", true, () -> true);
    public BooleanSetting reallyWorld = new BooleanSetting("Really World", false, () -> true);
    private double oldX;
    private double oldY;
    private double oldZ;
    private int oldPosY;

    public FreeCam() {
        super("FreeCam", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043b\u0435\u0442\u0430\u0442\u044c \u0432 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0439 \u043a\u0430\u043c\u0435\u0440\u0435", Type.Misc);
        this.addSettings(this.speed, this.reallyWorld, this.autoTeleportDisable, this.clipOnDisable, this.disableOnDamage);
    }

    @Override
    public void onEnable() {
        this.oldX = FreeCam.mc.player.posX;
        this.oldY = FreeCam.mc.player.posY;
        this.oldZ = FreeCam.mc.player.posZ;
        this.oldPosY = (int)FreeCam.mc.player.posY;
        FreeCam.mc.player.noClip = true;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(FreeCam.mc.world, FreeCam.mc.player.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(FreeCam.mc.player);
        fakePlayer.posY -= 0.0;
        fakePlayer.rotationYawHead = FreeCam.mc.player.rotationYawHead;
        FreeCam.mc.world.addEntityToWorld(-69, fakePlayer);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (this.clipOnDisable.getCurrentValue()) {
            this.oldX = FreeCam.mc.player.posX;
            this.oldY = FreeCam.mc.player.posY;
            this.oldZ = FreeCam.mc.player.posZ;
        }
        FreeCam.mc.player.capabilities.isFlying = false;
        FreeCam.mc.world.removeEntityFromWorld(-69);
        FreeCam.mc.player.motionZ = 0.0;
        FreeCam.mc.player.motionX = 0.0;
        FreeCam.mc.player.noClip = true;
        FreeCam.mc.player.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, FreeCam.mc.player.rotationYaw, FreeCam.mc.player.rotationPitch);
        super.onDisable();
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (!this.reallyWorld.getCurrentValue()) {
            return;
        }
        if (FreeCam.mc.player == null || FreeCam.mc.world == null || FreeCam.mc.player.ticksExisted < 1) {
            if (this.autoTeleportDisable.getCurrentValue()) {
                this.toggle();
                EventManager.unregister(this);
            }
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (FreeCam.mc.player == null || FreeCam.mc.world == null || FreeCam.mc.player.ticksExisted < 1) {
            if (this.autoTeleportDisable.getCurrentValue()) {
                this.toggle();
                EventManager.unregister(this);
            }
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
    public void onFullCube(EventFullCube event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (this.clipOnDisable.getCurrentValue()) {
            ScaledResolution sr = new ScaledResolution(mc);
            String pos = "" + (int)(-((double)this.oldPosY - FreeCam.mc.player.posY));
            String plusOrMinus = pos.contains("-") || pos.equals("0") ? "" : "+";
            String clipValue = "VClip " + plusOrMinus + pos;
            RenderHelper.renderBlurredShadow(Color.BLACK, (double)((float)sr.getScaledWidth() / 2.0f + 10.0f), (double)((float)sr.getScaledHeight() / 2.0f), (double)(FreeCam.mc.robotoRegularFontRender.getStringWidth(plusOrMinus + clipValue) + 10), 6.0, 10);
            FreeCam.mc.robotoRegularFontRender.drawStringWithShadow(clipValue, (float)sr.getScaledWidth() / 2.0f + 15.0f, (float)sr.getScaledHeight() / 2.0f, -1);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdateLiving event) {
        if (FreeCam.mc.player == null || FreeCam.mc.world == null) {
            return;
        }
        if (this.disableOnDamage.getCurrentValue()) {
            if (FreeCam.mc.player.hurtTime <= 8) {
                FreeCam.mc.player.noClip = true;
                FreeCam.mc.player.capabilities.isFlying = true;
                if (FreeCam.mc.gameSettings.keyBindJump.isKeyDown()) {
                    FreeCam.mc.player.motionY = this.speed.getCurrentValue() / 1.5f;
                }
                if (FreeCam.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    FreeCam.mc.player.motionY = -this.speed.getCurrentValue() / 1.5f;
                }
                MovementHelper.setSpeed(this.speed.getCurrentValue());
            } else if (!MovementHelper.isUnderBedrock()) {
                FreeCam.mc.player.capabilities.isFlying = false;
                FreeCam.mc.renderGlobal.loadRenderers();
                FreeCam.mc.player.noClip = false;
                FreeCam.mc.player.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, FreeCam.mc.player.rotationYaw, FreeCam.mc.player.rotationPitch);
                FreeCam.mc.world.removeEntityFromWorld(-69);
                FreeCam.mc.player.motionZ = 0.0;
                FreeCam.mc.player.motionX = 0.0;
                this.toggle();
                NotificationManager.publicity("FreeCam", "FreeCam was toggle off because of damage!", 4, NotificationType.INFO);
                ChatHelper.addChatMessage("FreeCam was toggle off because of damage!");
            }
        } else {
            FreeCam.mc.player.noClip = true;
            FreeCam.mc.player.onGround = false;
            if (FreeCam.mc.gameSettings.keyBindJump.isKeyDown()) {
                FreeCam.mc.player.motionY = this.speed.getCurrentValue() / 1.5f;
            }
            if (FreeCam.mc.gameSettings.keyBindSneak.isKeyDown()) {
                FreeCam.mc.player.motionY = -this.speed.getCurrentValue() / 1.5f;
            }
            MovementHelper.setSpeed(this.speed.getCurrentValue());
            FreeCam.mc.player.capabilities.isFlying = true;
        }
        if (this.clipOnDisable.getCurrentValue()) {
            this.oldX = FreeCam.mc.player.posX;
            this.oldY = FreeCam.mc.player.posY;
            this.oldZ = FreeCam.mc.player.posZ;
        }
    }
}

