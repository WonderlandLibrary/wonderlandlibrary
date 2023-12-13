package vip.radium.module.impl.visuals;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import org.lwjgl.opengl.GL11;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.event.impl.packet.PacketSendEvent;
import vip.radium.event.impl.render.overlay.Render2DEvent;
import vip.radium.event.impl.sound.EntityHitSoundEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.OGLUtils;
import vip.radium.utils.render.RenderingUtils;

@ModuleInfo(label = "Hitmarkers", category = ModuleCategory.VISUALS)
public final class Hitmarkers extends Module {

    private final Property<Integer> hitColorProperty = new Property<>("Hit Color", 0xFFFFFFFF);
    private final Property<Integer> killColorProperty = new Property<>("Kill Color", 0xFFFF0000);

    private final DoubleProperty xOffsetProperty = new DoubleProperty(
            "X Offset", 2.0D, 0.5D, 10.0D, 0.5D);
    private final DoubleProperty lengthProperty = new DoubleProperty(
            "Length", 4.0D, 0.5D, 10.0D, 0.5D);
    private final DoubleProperty hitMarkerThicknessProperty = new DoubleProperty(
            "Thickness", 1.0D, 0.5D, 3.0D, 0.5D);

    private final Property<Boolean> soundsProperty = new Property<>("Sounds", true);
    public final DoubleProperty volumeProperty = new DoubleProperty("Volume",
            100, soundsProperty::getValue, 0, 100, 1, Representation.PERCENTAGE);
    private final EnumProperty<Sound> soundTypeProperty = new EnumProperty<>("Sound Type",
            Sound.SKEET, soundsProperty::getValue);

    private final TimerUtil attackTimeOut = new TimerUtil();
    private final TimerUtil killTimeOut = new TimerUtil();

    @EventLink
    private final Listener<EntityHitSoundEvent> onHitSound = event -> {
        event.setCancelled(!soundsProperty.getValue());
    };
    private int color;
    private double progress;
    @EventLink(EventBusPriorities.HIGHEST)
    private final Listener<Render2DEvent> onRender2D = event -> {
        if (progress > 0.0D) {
            progress = RenderingUtils.linearAnimation(progress, 0.0D, 0.02D);

            final LockedResolution resolution = event.getResolution();

            final double xMiddle = resolution.getWidth() / 2.0D;
            final double yMiddle = resolution.getHeight() / 2.0D;

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
            OGLUtils.enableBlending();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glTranslated(xMiddle, yMiddle, 0.0D);
            GL11.glRotatef(45.0F, 0.0F, 0.0F, 1.0F);
            OGLUtils.color(RenderingUtils.fadeTo(
                    removeAlphaComponent(this.color),
                    this.color,
                    (float) progress));
            for (int i = 0; i < 4; i++) {
                drawHitMarker(xOffsetProperty.getValue(), lengthProperty.getValue(), hitMarkerThicknessProperty.getValue());
                if (i != 3)
                    GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            }
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    };
    private int lastAttackedEntity;
    @EventLink
    private final Listener<PacketSendEvent> onPacketSend = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof C02PacketUseEntity) {
            final C02PacketUseEntity packetUseEntity = (C02PacketUseEntity) packet;
            if (packetUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                lastAttackedEntity = packetUseEntity.getEntityId();
                attackTimeOut.reset();
            }
        } else if (packet instanceof C03PacketPlayer) {
            if (lastAttackedEntity != -1 && attackTimeOut.hasElapsed(500))
                lastAttackedEntity = -1;
        }
    };
    private int toBeKilledEntity;
    @EventLink
    private final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S19PacketEntityStatus) {
            S19PacketEntityStatus packetEntityStatus = (S19PacketEntityStatus) packet;
            final int entityId = packetEntityStatus.getEntityId();
            if (entityId == lastAttackedEntity || (!killTimeOut.hasElapsed(50) && entityId == toBeKilledEntity)) {
                switch (packetEntityStatus.getOpCode()) {
                    case 2:
                        color = hitColorProperty.getValue();
                        progress = 1.0D;
                        killTimeOut.reset();
                        toBeKilledEntity = lastAttackedEntity;
                        if (soundsProperty.getValue())
                            playSound();
                        break;
                    case 3:
                        color = killColorProperty.getValue();
                        progress = 1.0D;
                        toBeKilledEntity = -1;
                        break;
                }
                lastAttackedEntity = -1;
            }
        }
    };

    private static int removeAlphaComponent(int color) {
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;

        return ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF) |
                ((0) << 24);
    }

    private static void drawHitMarker(double xOffset, double length, double width) {
        final double halfWidth = width * 0.5D;

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(-(xOffset + length), -halfWidth);
        GL11.glVertex2d(-(xOffset + length), halfWidth);
        GL11.glVertex2d(-xOffset, halfWidth);
        GL11.glVertex2d(-xOffset, -halfWidth);
        GL11.glEnd();
    }

    private void playSound() {
        switch(soundTypeProperty.getValue()) {
            case SKEET:
                Minecraft.getMinecraft().getSoundHandler().playSoundFromFile("skeet.ogg", Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
                break;
            case NEKO:
                Minecraft.getMinecraft().getSoundHandler().playSoundFromFile("neko.ogg", Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
                break;
            case RIFK:
                Minecraft.getMinecraft().getSoundHandler().playSoundFromFile("rifk.ogg", Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
                break;
            case BASIC:
                Minecraft.getMinecraft().getSoundHandler().playSoundFromFile("basic.ogg", Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
                break;
        }
    }

    private enum Sound {
        BASIC, NEKO, RIFK, SKEET
    }
}
