package io.github.nevalackin.client.module.render.overlay;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.ScaledResolution;

import static org.lwjgl.opengl.GL11.*;

public final class Crosshair extends Module {

    private final BooleanProperty tShapeProperty = new BooleanProperty("T Shape", false);
    private final BooleanProperty dotProperty = new BooleanProperty("Dot", true);
    private final BooleanProperty dynamicProperty = new BooleanProperty("Dynamic", false);
    private final DoubleProperty lengthProperty = new DoubleProperty("Length", 3, 0, 20 , 0.5);
    private final BooleanProperty fadeOutProperty = new BooleanProperty("Fade Out", true);
    private final DoubleProperty gapProperty = new DoubleProperty("Gap", 2, 0, 10, 0.5);
    private final DoubleProperty widthProperty = new DoubleProperty("Width", 1, 0, 5, 0.5);
    private final DoubleProperty outlineWidthProperty = new DoubleProperty("Outline Width", 0.5, 0, 1, 0.5);
    private final ColourProperty colourProperty = new ColourProperty("Colour", ColourUtil.getClientColour());

    private double lastDist, prevLastDist;

    public Crosshair() {
        super("Crosshair", Category.RENDER, Category.SubCategory.RENDER_OVERLAY);

        this.register(this.tShapeProperty, this.dotProperty, this.dynamicProperty,
                      this.lengthProperty, this.gapProperty, this.widthProperty, this.outlineWidthProperty,
                      this.colourProperty, this.fadeOutProperty);
    }

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdate = event -> {
        if (event.isPre()) {
            final double xDist = event.getLastTickPosX() - event.getPosX();
            final double zDist = event.getLastTickPosZ() - event.getPosZ();

            this.prevLastDist = this.lastDist;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    };

    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
        event.setRenderCrossHair(false);

        final ScaledResolution scaledResolution = event.getScaledResolution();

        final double width = this.widthProperty.getValue();
        final double halfWidth = width / 2.0;
        double gap = this.gapProperty.getValue();

        if (this.dynamicProperty.getValue()) {
            gap *= Math.max(this.mc.thePlayer.isSneaking() ? 0.5 : 1.0, DrawUtil.interpolate(
                this.prevLastDist, this.lastDist,
                event.getPartialTicks()) * 10.0);
        }

        final double length = this.lengthProperty.getValue();
        final int color = this.colourProperty.getValue();
        final double outlineWidth = this.outlineWidthProperty.getValue();
        final boolean outline = outlineWidth > 0;
        final boolean tShape = this.tShapeProperty.getValue();

        final double middleX = scaledResolution.getScaledWidth() / 2.0;
        final double middleY = scaledResolution.getScaledHeight() / 2.0;

        if (outline) {
            // Left
            DrawUtil.glDrawFilledRect(middleX - gap - length - outlineWidth,
                         middleY - halfWidth - outlineWidth,
                         middleX - gap + outlineWidth,
                         middleY + halfWidth + outlineWidth, 0x96000000);
            // Right
            DrawUtil.glDrawFilledRect(middleX + gap - outlineWidth,
                         middleY - halfWidth - outlineWidth,
                         middleX + gap + length + outlineWidth,
                         middleY + halfWidth + outlineWidth, 0x96000000);
            // Bottom
            DrawUtil.glDrawFilledRect(middleX - halfWidth - outlineWidth,
                         middleY + gap - outlineWidth,
                         middleX + halfWidth + outlineWidth,
                         middleY + gap + length + outlineWidth, 0x96000000);
            if (!tShape)
                // Top
                DrawUtil.glDrawFilledRect(middleX - halfWidth - outlineWidth,
                             middleY - gap - length - outlineWidth,
                             middleX + halfWidth + outlineWidth,
                             middleY - gap + outlineWidth, 0x96000000);
        }

        final int clear = ColourUtil.removeAlphaComponent(color);

        glDisable(GL_ALPHA_TEST);

        final DrawLine drawLine = ((x, y, x1, y1) -> {
            if (this.fadeOutProperty.getValue()) {
                final boolean horizontal = y1 - y <= width;
                final boolean inverted = x > middleX || y > middleY;

                final int startColour = inverted ? color : clear;
                final int endColour = inverted ? clear : color;

                if (horizontal) {
                    DrawUtil.glDrawSidewaysGradientRect(x, y, x1 - x, y1 - y, startColour, endColour);
                } else {
                    DrawUtil.glDrawFilledRect(x, y, x1, y1, startColour, endColour);
                }
            } else {
                DrawUtil.glDrawFilledRect(x, y, x1, y1, color);
            }
        });

        // Left
        drawLine.draw(middleX - gap - length,
                      middleY - halfWidth,
                      middleX - gap,
                      middleY + halfWidth);
        // Right
        drawLine.draw(middleX + gap,
                      middleY - halfWidth,
                      middleX + gap + length,
                      middleY + halfWidth);
        // Bottom
        drawLine.draw(middleX - halfWidth,
                      middleY + gap,
                      middleX + halfWidth,
                      middleY + gap + length);

        // Top
        if (!tShape)
            drawLine.draw(middleX - halfWidth,
                          middleY - gap - length,
                          middleX + halfWidth,
                          middleY - gap);

        glEnable(GL_ALPHA_TEST);

        if (this.dotProperty.getValue()) {
            if (outline) {
                DrawUtil.glDrawFilledRect(middleX - halfWidth - outlineWidth, middleY - halfWidth - outlineWidth,
                             middleX + halfWidth + outlineWidth, middleY + halfWidth + outlineWidth, 0x96000000);
            }

            DrawUtil.glDrawFilledRect(middleX - halfWidth, middleY - halfWidth, middleX + halfWidth, middleY + halfWidth, color);
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @FunctionalInterface
    private interface DrawLine {
        void draw(final double x,
                  final double y,
                  final double x1,
                  final double y1);
    }
}
