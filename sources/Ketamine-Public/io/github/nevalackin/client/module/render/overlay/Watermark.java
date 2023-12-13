package io.github.nevalackin.client.module.render.overlay;

import io.github.nevalackin.client.event.game.GetTimerSpeedEvent;
import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.module.combat.rage.Aura;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.ui.cfont.MipMappedFontRenderer;
import io.github.nevalackin.client.util.math.MathUtil;
import io.github.nevalackin.client.util.movement.MovementUtil;
import io.github.nevalackin.client.util.render.BlurUtil;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.lwjgl.opengl.GL11.glScaled;

public final class Watermark extends Module {

    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.NORMAL);
    private final ColourProperty colourProperty = new ColourProperty("Colour", 0xFFEA8F91);
    private final EnumProperty<Scale> scaleProperty = new EnumProperty<>("Scale", Scale.REGULAR, this::isCSGOStyle);
    private final BooleanProperty tpsProperty = new BooleanProperty("TPS", true, this::isCSGOStyle);
    private final BooleanProperty bpsProperty = new BooleanProperty("BPS", true, this::isCSGOStyle);

    private final List<Long> secondRecords = new ArrayList<>();
    private double blocksPerSecond;
    @EventLink
    private final Listener<UpdatePositionEvent> onUpdate = event -> {
        if (event.isPre() && this.bpsProperty.check() && this.bpsProperty.getValue()) {
            if (MovementUtil.isMoving(this.mc.thePlayer)) {
                this.blocksPerSecond = MathUtil.distance(event.getLastTickPosX(), event.getLastTickPosZ(),
                        event.getPosX(), event.getPosZ()) * 20.0;
            } else {
                this.blocksPerSecond = 0.0;
            }
        }
    };
    private long lastTimeUpdate;
    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S03PacketTimeUpdate && this.tpsProperty.check() && this.tpsProperty.getValue()) {
            final long current = System.currentTimeMillis();

            if (this.lastTimeUpdate != 0) {
                this.secondRecords.add(current - this.lastTimeUpdate);
            }

            this.lastTimeUpdate = System.currentTimeMillis();
        }
    };
    private Aura aura;
    private double timerSpeed = 1.0;
    @EventLink
    private final Listener<GetTimerSpeedEvent> getTimerSpeedEventListener = event -> {
        this.timerSpeed = event.getTimerSpeed();
    };

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {

        CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

        switch (this.modeProperty.getValue()) {
            case NORMAL:
                final String name = KetamineClient.getInstance().getName();

                fontRenderer.draw(name.charAt(0) + "\247F" + name.substring(1), 4, 4,
                        ColourUtil.fadeBetween(this.colourProperty.getValue(), ColourUtil.darker(this.colourProperty.getValue())));
                break;
            case CSGO:
                final Date now = new Date();

                final String clientName = KetamineClient.getInstance().getName().toLowerCase();
                final String username = "nevalack";
                final String ping = String.format("delay: %s ticks", this.aura.getPing());

                final String text = String.format("%s  %s  %s  %02d:%02d:%02d",
                        clientName, username, ping,
                        now.getHours(), now.getMinutes(), now.getSeconds());

                final double margin = 3;
                final double height = fontRenderer.getHeight(text) + margin;

                final double scale = this.scaleProperty.getValue().getScale();

                glScaled(scale, scale, 1);

                // Draw top watermark
            {
                final double width = 2 + fontRenderer.getWidth(text) + 2;

                // Draw background
                BlurUtil.blurArea(margin * scale, margin * scale, width * scale, height * scale);
                DrawUtil.glDrawFilledQuad(margin, margin, width, height, 0x50000000);
                // Draw top
                DrawUtil.glDrawFilledQuad(margin, margin, width, 2,
                        this.colourProperty.getValue(),
                        ColourUtil.darker(this.colourProperty.getValue()));

                final double topOff = margin * 2 + 1;

                // Draw lines separating
                {
                    double lx = margin + 2 + fontRenderer.getWidth(clientName + " ");

                    DrawUtil.glDrawLine(lx, topOff - 1, lx, topOff + height - margin * 2.0, 2, false, 0xFFFFFFFF);

                    lx += fontRenderer.getWidth(" " + username + " ");

                    DrawUtil.glDrawLine(lx, topOff - 1, lx, topOff + height - margin * 2.0, 2, false, 0xFFFFFFFF);

                    lx += fontRenderer.getWidth(" " + ping + " ");

                    DrawUtil.glDrawLine(lx, topOff - 1, lx, topOff + height - margin * 2.0, 2, false, 0xFFFFFFFF);
                }

                fontRenderer.draw(text, margin + 2, topOff - 2, 0xFFFFFFFF);
            }

            double x = margin;

            // Draw bps
            if (this.bpsProperty.getValue()) {
                final String bps = String.format("blocks/s: %.2f", this.blocksPerSecond * this.timerSpeed);

                final double width = 2 + fontRenderer.getWidth(bps) + 2;

                final double y = height + margin + 1;
                final double y1 = y + height;

                BlurUtil.blurArea(x * scale, y * scale, width * scale, height * scale);
                DrawUtil.glDrawFilledQuad(x, y, width, height, 0x50000000);
                DrawUtil.glDrawGradientLine(x, y1, x + width, y1, 1.0F, 0xFFFFFF80);

                fontRenderer.draw(bps, x + 2, y + 1, 0xFFFFFFFF);

                x += width + 1;
            }

            // Draw tps
            if (this.tpsProperty.getValue()) {
                final String tps = String.format("server ticks/s: %.2f", this.calculateTPS(10));

                final double width = 2 + fontRenderer.getWidth(tps) + 2;

                final double y = height + margin + 1;
                final double y1 = y + height;

                BlurUtil.blurArea(x * scale, y * scale, width * scale, height * scale);
                DrawUtil.glDrawFilledQuad(x, y, width, height, 0x50000000);
                DrawUtil.glDrawGradientLine(x, y1, x + width, y1, 1.0F, 0xFF5EDCFF);

                fontRenderer.draw(tps, x + 2, y + 1, 0xFFFFFFFF);
            }

            glScaled(1 / scale, 1 / scale, 1);
            break;
        }
    };

    public Watermark() {
        super("Watermark", Category.RENDER, Category.SubCategory.RENDER_OVERLAY);

        this.setHidden(true);

        this.register(this.modeProperty, this.scaleProperty,
                this.colourProperty, this.tpsProperty, this.bpsProperty);
    }

    private boolean isCSGOStyle() {
        return this.modeProperty.getValue() == Mode.CSGO;
    }

    private double calculateTPS(int nSamples) {
        final int samples = this.secondRecords.size();

        if (samples < 2) {
            // Too few to calculate
            return 20.0;
        }

        nSamples = Math.min(nSamples, samples);

        long total = 0;

        for (int i = 0; i < nSamples; i++) {
            total += this.secondRecords.get(samples - 1 - i);
        }

        final double avg = (double) total / nSamples;

        return avg / 50;
    }

    @Override
    public void onEnable() {
        if (this.aura == null) {
            this.aura = KetamineClient.getInstance().getModuleManager().getModule(Aura.class);
        }

        this.lastTimeUpdate = 0;
    }

    @Override
    public void onDisable() {
        this.secondRecords.clear();
        this.blocksPerSecond = 0.0;
    }

    private enum Mode {
        NORMAL("Normal"),
        CSGO("CSGO");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum Scale {
        SMALL("Small", 0.5),
        REGULAR("Reg", 1),
        BIG("Big", 2.0);

        private final String name;
        private final double scale;

        Scale(String name, double scale) {
            this.name = name;
            this.scale = scale;
        }

        public double getScale() {
            return scale;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}