package io.github.nevalackin.client.module.render.esp.esp.components;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.module.render.esp.esp.EnumPosition;
import io.github.nevalackin.client.util.render.DrawUtil;

public final class Tag {

    private final String text;
    private final boolean drawBackground;
    private final EnumPosition position;
    private final int colour;

    public Tag(String text, EnumPosition position, int colour, boolean drawBackground) {
        this.text = text;
        this.position = position;
        this.colour = colour;
        this.drawBackground = drawBackground;
    }

    public void draw(final CustomFontRenderer renderer,
                     final double[] boundingBox,
                     final double[] sideOffset) {
        final double left = boundingBox[0];
        final double top = boundingBox[1];
        final double right = boundingBox[2];
        final double bottom = boundingBox[3];

        final String text = this.text;

        final double textWidth = renderer.getWidth(text);

        final EnumPosition side = this.getPosition();
        final boolean background = this.drawBackground;

        final double backgroundBufferX = 2;
        final double backgroundBufferY = 1;

        double tLeft = side.isUseRightAsLeft() ? right : left;
        double tTop = side.isUseBottomAsTop() ? bottom : top;

        // Position adjustments
        tLeft += sideOffset[0];
        tTop += sideOffset[1];

        // If on left or top minus text width / height
        if (side.isNeedSubtractTextHeight()) {
            tTop -= 9;

            if (background) {
                tTop -= backgroundBufferY * 2.0;
            }
        }

        if (side.isNeedSubtractTextWidth()) {
            tLeft -= textWidth;
        }

        if (side.isCentreText()) {
            final double width = right - left;
            final double centerOfTag = tLeft + (width / 2.0);
            tLeft = centerOfTag - (textWidth + backgroundBufferX * 2.0) / 2.0;

            if (background) {
                if (side == EnumPosition.TOP)
                    tTop -= backgroundBufferY;
                else tTop += backgroundBufferY;
            }
        } else if (background) {
            if (side == EnumPosition.LEFT)
                tLeft -= backgroundBufferX;
            else tLeft += backgroundBufferX;
        }

        if (this.drawBackground) {
            // Draw background
            DrawUtil.glDrawFilledQuad(tLeft, tTop, textWidth + backgroundBufferX * 2.0,
                                      9 + backgroundBufferY * 2.0, 0x96000000);
        }

        // Draw text
        renderer.draw(text, tLeft + backgroundBufferX, tTop + backgroundBufferY, this.colour);
    }

    public EnumPosition getPosition() {
        return position;
    }
}
