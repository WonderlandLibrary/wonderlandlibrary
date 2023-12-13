package io.github.nevalackin.client.module.render.esp.esp;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.module.render.esp.esp.components.Bar;
import io.github.nevalackin.client.module.render.esp.esp.components.Box;
import io.github.nevalackin.client.module.render.esp.esp.components.Tag;
import io.github.nevalackin.client.util.render.DrawUtil;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public final class Drawable {

    private final double[] boundingBox;
    private final Box box;
    private final List<Bar> bars;
    private final List<Tag> tags;

    public Drawable(final double[] boundingBox,
                    final Box box,
                    final List<Bar> bars,
                    final List<Tag> tags) {
        this.boundingBox = boundingBox;
        this.box = box;
        this.bars = bars;
        this.tags = tags;
    }

    private double[] getTextOffsetForSide(final EnumPosition side) {
        double x = side.getXOffset();
        double y = side.getYOffset();

        if (this.bars != null) {
            for (final Bar bar : this.bars) {
                if (side == bar.getPosition()) {
                    final double totalThickness = bar.getThickness() + 0.5 * 2.0;
                    // Add thickness + outline on both sides
                    x += side.isUseRightAsLeft() ? totalThickness : -totalThickness;
                    y += side.isUseBottomAsTop() ? totalThickness : -totalThickness;
                }
            }
        }

        return new double[]{x, y};
    }

    void draw(final CustomFontRenderer renderer) {
        // Draw tags
        if (this.tags != null) {
            for (final Tag tag : this.tags) {
                tag.draw(renderer, this.boundingBox, this.getTextOffsetForSide(tag.getPosition()));
            }
        }

        // Draw box
        if (this.box != null) {
            final double left = this.boundingBox[0];
            final double top = this.boundingBox[1];
            final double right = this.boundingBox[2];
            final double bottom = this.boundingBox[3];

            final double width = right - left;
            final double height = bottom - top;

            // Enable blending
            glEnable(GL_BLEND);
            // Disable texture drawing
            glDisable(GL_TEXTURE_2D);
            // Translate matrix to top-left of bounding box
            glTranslated(left, top, 0);
            // Set to outline colour
            DrawUtil.glColour(0x96000000);

            final double bThickness = this.box.getWidth();
            final double oThickness = 0.5;
            final double total = bThickness + oThickness * 2.0;

            glBegin(GL_QUADS);
            {
                // Draw outline box
                {
                    // Top
                    glVertex2d(0, 0);
                    glVertex2d(0, total);
                    glVertex2d(width, total);
                    glVertex2d(width, 0);

                    // Bottom
                    glVertex2d(0, height - total);
                    glVertex2d(0, height);
                    glVertex2d(width, height);
                    glVertex2d(width, height - total);

                    // Left
                    glVertex2d(0, total);
                    glVertex2d(0, height - total);
                    glVertex2d(total, height - total);
                    glVertex2d(total, total);

                    // Right
                    glVertex2d(width - total, total);
                    glVertex2d(width - total, height - total);
                    glVertex2d(width, height - total);
                    glVertex2d(width, total);
                }

                // Set to box colour
                DrawUtil.glColour(this.box.getColour());

                // Draw coloured box
                {
                    // Top
                    glVertex2d(oThickness, oThickness);
                    glVertex2d(oThickness, oThickness + bThickness);
                    glVertex2d(width - oThickness, oThickness + bThickness);
                    glVertex2d(width - oThickness, oThickness);

                    // Bottom
                    glVertex2d(oThickness, height - total + oThickness);
                    glVertex2d(oThickness, height - oThickness);
                    glVertex2d(width - oThickness, height - oThickness);
                    glVertex2d(width - oThickness, height - total + oThickness);

                    // Left
                    glVertex2d(oThickness, total - oThickness);
                    glVertex2d(oThickness, height - total + oThickness);
                    glVertex2d(total - oThickness, height - total + oThickness);
                    glVertex2d(total - oThickness, total - oThickness);

                    // Right
                    glVertex2d(width - total + oThickness, total - oThickness);
                    glVertex2d(width - total + oThickness, height - total + oThickness);
                    glVertex2d(width - oThickness, height - total + oThickness);
                    glVertex2d(width - oThickness, total - oThickness);
                }
            }
            glEnd();

            // Restore matrix
            glTranslated(-left, -top, 0);
            // Enable texture 2d
            glEnable(GL_TEXTURE_2D);
        }

        // Draw bars
        if (this.bars != null) {
            final double[] offsets = {0.5, 0.5, 0.5, 0.5};
            for (final Bar bar : this.bars) {
                bar.draw(renderer, this.boundingBox, offsets);
            }
        }
    }

    public double[] getBoundingBox() {
        return boundingBox;
    }
}
