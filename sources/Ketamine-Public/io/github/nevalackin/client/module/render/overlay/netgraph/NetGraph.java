package io.github.nevalackin.client.module.render.overlay.netgraph;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.packet.SendPacketEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Collection;

import static org.lwjgl.opengl.GL11.*;

public final class NetGraph extends Module {

    private final PacketMonitor incomingPackets = new PacketMonitor();
    private final PacketMonitor outgoingPackets = new PacketMonitor();
    private final PacketMonitor sentC03Packets = new PacketMonitor();
    @EventLink
    private final Listener<SendPacketEvent> onSendPacket = event -> {
        if (event.getPacket() instanceof C03PacketPlayer) {
            this.sentC03Packets.increment();
        }

        this.outgoingPackets.increment();
    };
    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        this.incomingPackets.increment();
    };
    @EventLink
    private final Listener<RenderGameOverlayEvent> onGameOverlay = event -> {
        final int samples = 15;
        final int width = samples * 6;

        this.incomingPackets.update(samples + 1);
        this.outgoingPackets.update(samples + 1);
        this.sentC03Packets.update(samples + 1);

        glScaled(0.5, 0.5, 1);
        this.mc.fontRendererObj.drawStringWithShadow("Incoming packets", 120 * 2, 4, 0xFFFFFFFF);

        this.mc.fontRendererObj.drawStringWithShadow("Outgoing packets", (120 + width + 30) * 2, 4, 0xFFFFFFFF);
        glScaled(2.0, 2.0, 1);

        this.drawNetGraph(120, 7, width, 25, samples,
                this.incomingPackets.getPacketRecord());
        this.drawNetGraph(120 + width + 30, 7, width, 25, samples,
                this.outgoingPackets.getPacketRecord());
    };

    public NetGraph() {
        super("Net Graph", Category.RENDER, Category.SubCategory.RENDER_OVERLAY);
    }

    private void drawNetGraph(final int x,
                              final int y,
                              final int width,
                              final int height,
                              final int nSamplesDisplayed,
                              final Collection<Integer> packetSeconds) {
        DrawUtil.glDrawFilledQuad(x, y, width, height, 0x40000000);

        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable line anti-aliasing
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        // Enable blending (required for anti-aliasing)
        glEnable(GL_BLEND);
        // Set width to 2px
        glLineWidth(1.0F);
        // Translate matrix to top right of graph
        glTranslated(x + width, y, 0);

        final double sampleWidth = (double) width / nSamplesDisplayed;
        double lineAlignment = 0;

        DrawUtil.glColour(0xFFFFFFFF);

        final int maxPackets = packetSeconds.stream()
                .max(Integer::compareTo)
                .orElse(1);

        final int packetDenominator = (int) (maxPackets * 1.25);

        int totalPackets = 0;

        DrawUtil.glColour(0xFFFFFFFF);

        // Start drawing lines
        glBegin(GL_LINE_STRIP);
        {
            for (final int packets : packetSeconds) {
                final double percentage = Math.min(1.0, (double) packets / packetDenominator);
                final double yPosition = height * percentage;

                glVertex2d(-lineAlignment, height - yPosition);

                lineAlignment += sampleWidth;

                totalPackets += packets;
            }
        }
        // Draw the line graph
        glEnd();

        final int avgPackets = totalPackets / nSamplesDisplayed;
        final double avgPercent = Math.min(1.0, (double) avgPackets / packetDenominator);
        final double avgPosition = height * avgPercent;

        DrawUtil.glColour(0xFF00FF00);

        // Draw average packets line
        glBegin(GL_LINES);
        {
            glVertex2d(-width, height - avgPosition);
            glVertex2d(0, height - avgPosition);
        }
        glEnd();

        // Enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable line anti-aliasing
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        // Disable blending
        glDisable(GL_BLEND);

        glScaled(0.5, 0.5, 1);
        this.mc.fontRendererObj.drawStringWithShadow(avgPackets + " avg", 2, (float) ((height - avgPosition) * 2) - 9 / 2.0F, 0xFFFFFFFF);
        glScaled(2.0, 2.0, 1);

        // Restore matrix
        glTranslated(-x - width, -y, 0);

        DrawUtil.glDrawOutlinedQuad(x, y, width, height, 0.5F, 0xFFFFFFFF);
    }

    @Override
    public void onEnable() {
        this.outgoingPackets.reset();
        this.sentC03Packets.reset();
        this.incomingPackets.reset();
    }

    @Override
    public void onDisable() {

    }

}