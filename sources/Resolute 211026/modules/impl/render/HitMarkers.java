// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import vip.Resolute.util.misc.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import vip.Resolute.util.misc.PlaySound;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.modules.Module;

public class HitMarkers extends Module
{
    public ColorSetting hitColorProp;
    public ColorSetting killColorProp;
    public NumberSetting xOffsetProp;
    public NumberSetting lengthProp;
    public NumberSetting thicknessProp;
    public BooleanSetting soundProp;
    private final TimerUtil attackTimeOut;
    private final TimerUtil killTimeOut;
    private int color;
    private double progress;
    private int lastAttackedEntity;
    private int toBeKilledEntity;
    
    public HitMarkers() {
        super("HitMarkers", 0, "", Category.RENDER);
        this.hitColorProp = new ColorSetting("Hit Color", new Color(255, 255, 255));
        this.killColorProp = new ColorSetting("Kill Color", new Color(255, 0, 0));
        this.xOffsetProp = new NumberSetting("X Offset", 2.0, 0.5, 10.0, 0.5);
        this.lengthProp = new NumberSetting("Length", 4.0, 0.5, 10.0, 0.5);
        this.thicknessProp = new NumberSetting("Thickness", 1.0, 0.5, 3.0, 0.5);
        this.soundProp = new BooleanSetting("Sounds", false);
        this.attackTimeOut = new TimerUtil();
        this.killTimeOut = new TimerUtil();
        this.addSettings(this.hitColorProp, this.killColorProp, this.xOffsetProp, this.lengthProp, this.thicknessProp, this.soundProp);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventPacket) {
            final Packet<?> packet = ((EventPacket)e).getPacket();
            if (packet instanceof S19PacketEntityStatus) {
                final S19PacketEntityStatus packetEntityStatus = (S19PacketEntityStatus)packet;
                final int entityId = packetEntityStatus.getEntityId();
                if (entityId == this.lastAttackedEntity || (!this.killTimeOut.hasElapsed(50L) && entityId == this.toBeKilledEntity)) {
                    switch (packetEntityStatus.getOpCode()) {
                        case 2: {
                            if (this.soundProp.isEnabled()) {
                                PlaySound.playSound("retard.wav");
                            }
                            this.color = this.hitColorProp.getColor();
                            this.progress = 1.0;
                            this.killTimeOut.reset();
                            this.toBeKilledEntity = this.lastAttackedEntity;
                            break;
                        }
                        case 3: {
                            if (this.soundProp.isEnabled()) {
                                PlaySound.playSound("retard.wav");
                            }
                            this.color = this.killColorProp.getColor();
                            this.progress = 1.0;
                            this.toBeKilledEntity = -1;
                            break;
                        }
                    }
                    this.lastAttackedEntity = -1;
                }
            }
        }
        if (e instanceof EventPacket) {
            final Packet<?> packet2 = ((EventPacket)e).getPacket();
            if (packet2 instanceof C02PacketUseEntity) {
                final C02PacketUseEntity packetUseEntity = (C02PacketUseEntity)packet2;
                if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    this.lastAttackedEntity = packetUseEntity.getEntityId();
                    this.attackTimeOut.reset();
                }
            }
            if (packet2 instanceof C03PacketPlayer && this.lastAttackedEntity != -1 && this.attackTimeOut.hasElapsed(500L)) {
                this.lastAttackedEntity = -1;
            }
        }
        if (e instanceof EventRender2D) {
            final EventRender2D event = (EventRender2D)e;
            if (this.progress > 0.0) {
                this.progress = linearAnimation(this.progress, 0.0, 0.02);
                final ScaledResolution resolution = new ScaledResolution(HitMarkers.mc);
                final double xMiddle = resolution.getScaledWidth() / 2.0;
                final double yMiddle = resolution.getScaledHeight() / 2.0;
                GL11.glPushMatrix();
                RenderUtils.enableBlending();
                GL11.glDisable(3553);
                GL11.glHint(3155, 4354);
                GL11.glEnable(2881);
                GL11.glTranslated(xMiddle, yMiddle, 0.0);
                GL11.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
                RenderUtils.color(RenderUtils.fadeTo(removeAlphaComponent(this.color), this.color, (float)this.progress));
                for (int i = 0; i < 4; ++i) {
                    drawHitMarker(this.xOffsetProp.getValue(), this.lengthProp.getValue(), this.thicknessProp.getValue());
                    if (i != 3) {
                        GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                    }
                }
                GL11.glDisable(2881);
                GL11.glDisable(3042);
                GL11.glEnable(3553);
                GL11.glPopMatrix();
            }
        }
    }
    
    private static int removeAlphaComponent(final int color) {
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    private static void drawHitMarker(final double xOffset, final double length, final double width) {
        final double halfWidth = width * 0.5;
        GL11.glBegin(7);
        GL11.glVertex2d(-(xOffset + length), -halfWidth);
        GL11.glVertex2d(-(xOffset + length), halfWidth);
        GL11.glVertex2d(-xOffset, halfWidth);
        GL11.glVertex2d(-xOffset, -halfWidth);
        GL11.glEnd();
    }
    
    public static double linearAnimation(final double now, final double desired, final double speed) {
        final double dif = Math.abs(now - desired);
        final int fps = Minecraft.getDebugFPS();
        if (dif > 0.0) {
            double animationSpeed = MathUtils.roundToDecimalPlace(Math.min(10.0, Math.max(0.005, 144.0 / fps * speed)), 0.005);
            if (dif != 0.0 && dif < animationSpeed) {
                animationSpeed = dif;
            }
            if (now < desired) {
                return now + animationSpeed;
            }
            if (now > desired) {
                return now - animationSpeed;
            }
        }
        return now;
    }
}
