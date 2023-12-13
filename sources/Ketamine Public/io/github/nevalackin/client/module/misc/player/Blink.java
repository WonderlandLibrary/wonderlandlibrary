package io.github.nevalackin.client.module.misc.player;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.packet.SendPacketEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.event.render.world.Render3DEvent;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public final class Blink extends Module {

    private final DoubleProperty maxPacketsProperty = new DoubleProperty("Max Packets", 80, 0, 200, 1);

    private final List<Packet<?>> packets = new ArrayList<>();

    private Vec3 lastLocation;

    private int clientUpdatePositionPackets;

    public Blink() {
        super("Blink", Category.MISC, Category.SubCategory.MISC_PLAYER);

        this.maxPacketsProperty.addValueAlias(0, "Unlimited");

        this.setSuffix(() -> String.valueOf(this.clientUpdatePositionPackets));

        this.register(this.maxPacketsProperty);
    }

    @EventLink
    private final Listener<SendPacketEvent> onSendPacket = event -> {
        this.packets.add(event.getPacket());
        event.setCancelled();
    };

    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook posLook = (S08PacketPlayerPosLook) packet;
            this.lastLocation = new Vec3(posLook.getX(), posLook.getY(), posLook.getZ());
            this.packets.clear();
        } else if (packet instanceof S18PacketEntityTeleport) {
            final S18PacketEntityTeleport teleport = (S18PacketEntityTeleport) packet;

            if (teleport.getEntityId() == this.mc.thePlayer.getEntityId()) {
                this.lastLocation = new Vec3(teleport.getX(), teleport.getY(), teleport.getZ());
                this.packets.clear();
            }
        }
    };

    private boolean isPulseBlink() {
        return this.maxPacketsProperty.getValue() != 0;
    }

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre()) {
            if (this.isPulseBlink()) {
                if (this.clientUpdatePositionPackets > this.maxPacketsProperty.getValue().intValue()) {
                    while (!this.packets.isEmpty()) {
                        this.mc.thePlayer.sendQueue.sendPacketDirect(this.packets.remove(0));
                    }

                    this.clientUpdatePositionPackets = 0;

                    this.lastLocation = this.mc.thePlayer.getPositionVector();
                }

                ++this.clientUpdatePositionPackets;
            }
        }
    };

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
        if (this.isPulseBlink()) {

        }
    };

    @EventLink
    private final Listener<Render3DEvent> onRender3D = event -> {
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glLineWidth(2.5f);
        final boolean restore = DrawUtil.glEnableBlend();

        glTranslated(this.lastLocation.xCoord, this.lastLocation.yCoord, this.lastLocation.zCoord);

        DrawUtil.glColour(0x80FFFFFF);

        glBegin(GL_POLYGON);
        {
            addCircleVertices(20, 0.6);
        }
        glEnd();

        DrawUtil.glColour(0xFFFFFFFF);

        glBegin(GL_LINE_LOOP);
        {
            addCircleVertices(20, 0.6);
        }
        glEnd();

        glTranslated(-this.lastLocation.xCoord, -this.lastLocation.yCoord, -this.lastLocation.zCoord);

        DrawUtil.glRestoreBlend(restore);
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glEnable(GL_TEXTURE_2D);
    };

    private static void addCircleVertices(final int points, final double radius) {
        for (int i = 0; i <= points; i++) {
            final double delta = i * Math.PI * 2 / points;
            glVertex3d(radius * Math.cos(delta), 0, radius * Math.sin(delta));
        }
    }

    @Override
    public void onEnable() {
        if (this.mc.thePlayer == null) {
            this.setEnabled(false);
            return;
        }

        this.lastLocation = this.mc.thePlayer.getPositionVector();
    }

    @Override
    public void onDisable() {
        if (this.mc.thePlayer != null) {
            while (!this.packets.isEmpty()) {
                this.mc.thePlayer.sendQueue.sendPacketDirect(this.packets.remove(0));
            }
        }
    }
}
