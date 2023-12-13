package io.github.nevalackin.client.module.render.esp.esp.components;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.module.render.esp.esp.EnumPosition;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;

import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11.*;

public final class Bar {

    private final double thickness;
    private final EnumPosition position;
    private final boolean drawBackground;
    private final boolean drawPercentageText;
    private final int colour;
    private final int secondaryColour;
    private final boolean gradient;
    private final Supplier<Double> percentageSupplier;

    public Bar(final double thickness,
               final EnumPosition position,
               final boolean drawBackground,
               final boolean drawPercentageText,
               final int colour,
               final int secondaryColour,
               final boolean gradient,
               final Supplier<Double> percentageSupplier) {
        this.thickness = thickness;
        this.position = position;
        this.drawBackground = drawBackground;
        this.drawPercentageText = drawPercentageText;
        this.colour = colour;
        this.secondaryColour = secondaryColour;
        this.gradient = gradient;
        this.percentageSupplier = percentageSupplier;
    }

    public void draw(final CustomFontRenderer renderer,
                     final double[] boundingBox,
                     final double[] offsets) {
        final double left = boundingBox[0];
        final double top = boundingBox[1];
        final double right = boundingBox[2];
        final double bottom = boundingBox[3];

        final double width = right - left;
        final double height = bottom - top;

        final double percentage = this.getPercentage();

        final EnumPosition side = this.getPosition();
        final double thickness = this.getThickness();

        final double totalThickness = 0.5 * 2.0 + thickness;

        double bLeft = side.isUseRightAsLeft() ? right : left;
        double bTop = side.isUseBottomAsTop() ? bottom : top;

        if (side.isDrawHorizontalBar()) {
            bTop += side.isUseBottomAsTop() ? offsets[side.ordinal()] : -(offsets[side.ordinal()] + totalThickness);
        } else {
            bLeft += side.isUseRightAsLeft() ? offsets[side.ordinal()] + totalThickness : -(offsets[side.ordinal()] + totalThickness);
        }

        offsets[side.ordinal()] += totalThickness + 0.5;

        final boolean drawText = this.drawPercentageText && percentage < 1.0;

        double xText = 0;
        double yText = 0;

        if (side.isDrawHorizontalBar()) {
            final double filledWidth = (width - 0.5 * 2.0) * percentage;

            if (drawText) {
                xText = filledWidth;
                yText = (bTop + thickness / 2.0);
            }

            if (this.drawBackground) {
                // Draw background
                DrawUtil.glDrawFilledQuad(bLeft, bTop, width, totalThickness, 0x96000000);
            }

            this.drawBar(bLeft + 0.5, bTop + 0.5, filledWidth, thickness, percentage);
        } else {
            final double filledHeight = (height - 0.5 * 2.0) * percentage;
            final double healthHeight = bTop + height - 0.5 - filledHeight;

            if (drawText) {
                xText = (bLeft + thickness / 2.0);
                yText = healthHeight - 9;
            }

            if (this.drawBackground) {
                // Draw background
                DrawUtil.glDrawFilledQuad(bLeft, bTop, totalThickness, height, 0x96000000);
            }

            this.drawBar(bLeft + 0.5, healthHeight, thickness, filledHeight, percentage);
        }

        if (drawText) {
            final String text = String.valueOf((int) (percentage * 100.0));

            // Centre the text
            if (!side.isDrawHorizontalBar()) {
                xText -= renderer.getWidth(text) / 2.0;
            }

            // Render numbers in front of everything else
            glTranslated(0, 0, 3);
            renderer.draw(text, xText, yText, 0.2, 0xFFFFFFFF);

            glTranslated(0, 0, -3);
        }
    }

    private void drawBar(final double x,
                         final double y,
                         final double width,
                         final double height,
                         final double percentage) {
        if (this.gradient) {
            // Enable blending
            final boolean restore = DrawUtil.glEnableBlend();
            // Disable texture drawing
            glDisable(GL_TEXTURE_2D);
            // Calculate the colour at the top of the bar
            final int topColour = ColourUtil.fadeBetween(this.secondaryColour, this.colour, percentage);
            // Enable colour blending
            glShadeModel(GL_SMOOTH);

            // Begin rect
            glBegin(GL_QUADS);
            {
                if (this.position.isDrawHorizontalBar()) {
                    // Left is colour->secondary colour
                    DrawUtil.glColour(topColour);
                    glVertex2d(x, y);
                    glVertex2d(x, y + height);
                    // Right is secondary colour
                    DrawUtil.glColour(this.secondaryColour);
                    glVertex2d(x + width, y + height);
                    glVertex2d(x + width, y);
                } else {
                    // Top is colour->secondary colour
                    DrawUtil.glColour(topColour);
                    glVertex2d(x, y);
                    // Bottom is secondary colour
                    DrawUtil.glColour(this.secondaryColour);
                    glVertex2d(x, y + height);
                    glVertex2d(x + width, y + height);
                    // Top
                    DrawUtil.glColour(topColour);
                    glVertex2d(x + width, y);
                }
            }
            // Draw the rect
            glEnd();

            // Disable colour blending
            glShadeModel(GL_FLAT);
            // Disable blending
            DrawUtil.glRestoreBlend(restore);
            // Re-enable texture drawing
            glEnable(GL_TEXTURE_2D);
        } else {
            // Draw filled
            DrawUtil.glDrawFilledQuad(x, y, width, height, this.colour);
        }
    }

    public double getThickness() {
        return thickness;
    }

    public EnumPosition getPosition() {
        return position;
    }

    public double getPercentage() {
        return this.percentageSupplier.get();
    }
}
