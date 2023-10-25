/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventBlockInteract;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.combat.KillAura;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;
import org.lwjgl.input.Mouse;

public class ClickTP
extends Feature {
    public static NumberSetting maxBlockReachValue;
    public static BooleanSetting posESP;
    public static BooleanSetting autoDisable;
    public static ColorSetting posESPColor;
    private int x;
    private int y;
    private int z;
    private boolean wasClick;

    public ClickTP() {
        super("ClickTP", "\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0432\u0430\u0441 \u043d\u0430 \u0442\u043e\u0447\u043a\u0443 \u043a\u0443\u0434\u0430 \u0432\u044b \u0437\u0430\u0436\u0430\u043b\u0438 \u041b\u041a\u041c", Type.Movement);
        maxBlockReachValue = new NumberSetting("Max reach value", 120.0f, 10.0f, 500.0f, 10.0f, () -> true);
        autoDisable = new BooleanSetting("Auto Disable", true, () -> true);
        posESP = new BooleanSetting("Position ESP", true, () -> true);
        posESPColor = new ColorSetting("Color", Color.WHITE.getRGB(), posESP::getCurrentValue);
        this.addSettings(maxBlockReachValue, autoDisable, posESP, posESPColor);
    }

    @Override
    public void onDisable() {
        this.x = (int)ClickTP.mc.player.posX;
        this.y = (int)ClickTP.mc.player.posY;
        this.z = (int)ClickTP.mc.player.posZ;
        this.wasClick = false;
        super.onDisable();
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (ClickTP.mc.player == null || ClickTP.mc.world == null) {
            return;
        }
        if (ClickTP.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            Color color = new Color(posESPColor.getColor());
            BlockPos pos = ClickTP.mc.objectMouseOver.getBlockPos();
            if (posESP.getCurrentValue()) {
                GlStateManager.pushMatrix();
                RenderHelper.blockEsp(pos, color, true, 1.0, 1.0);
                GlStateManager.popMatrix();
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        if (ClickTP.mc.player == null || ClickTP.mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            if (Mouse.isButtonDown(0) && KillAura.target == null) {
                if (this.wasClick) {
                    packet.x = (double)this.x + 0.5;
                    packet.y = this.y + 1;
                    packet.z = (double)this.z + 0.5;
                }
                if (ClickTP.mc.player.posX == (double)this.x + 0.5 && ClickTP.mc.player.posY == (double)(this.y + 1) && ClickTP.mc.player.posZ == (double)this.z + 0.5) {
                    if (this.wasClick) {
                        NotificationManager.publicity("Click TP", (Object)((Object)ChatFormatting.GREEN) + "Successfully " + (Object)((Object)ChatFormatting.WHITE) + "teleported to " + (Object)((Object)ChatFormatting.RED) + "X: " + (int)ClickTP.mc.player.posX + " Y: " + (int)ClickTP.mc.player.posY + " Z: " + (int)ClickTP.mc.player.posZ, 4, NotificationType.SUCCESS);
                        this.wasClick = false;
                    }
                    if (autoDisable.getCurrentValue()) {
                        this.toggle();
                        EventManager.unregister(this);
                    }
                }
            }
        }
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        if (ClickTP.mc.player == null || ClickTP.mc.world == null) {
            return;
        }
        if (event.getPos() != null) {
            BlockPos pos = event.getPos();
            if (!this.wasClick) {
                this.x = pos.getX();
                this.y = pos.getY();
                this.z = pos.getZ();
                this.wasClick = true;
            }
        }
    }
}

