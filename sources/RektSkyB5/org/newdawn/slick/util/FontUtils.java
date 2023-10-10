/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

public class FontUtils {
    public static void drawLeft(Font font, String s2, int x2, int y2) {
        FontUtils.drawString(font, s2, 1, x2, y2, 0, Color.white);
    }

    public static void drawCenter(Font font, String s2, int x2, int y2, int width) {
        FontUtils.drawString(font, s2, 2, x2, y2, width, Color.white);
    }

    public static void drawCenter(Font font, String s2, int x2, int y2, int width, Color color) {
        FontUtils.drawString(font, s2, 2, x2, y2, width, color);
    }

    public static void drawRight(Font font, String s2, int x2, int y2, int width) {
        FontUtils.drawString(font, s2, 3, x2, y2, width, Color.white);
    }

    public static void drawRight(Font font, String s2, int x2, int y2, int width, Color color) {
        FontUtils.drawString(font, s2, 3, x2, y2, width, color);
    }

    public static final int drawString(Font font, String s2, int alignment, int x2, int y2, int width, Color color) {
        int resultingXCoordinate = 0;
        if (alignment == 1) {
            font.drawString(x2, y2, s2, color);
        } else if (alignment == 2) {
            font.drawString(x2 + width / 2 - font.getWidth(s2) / 2, y2, s2, color);
        } else if (alignment == 3) {
            font.drawString(x2 + width - font.getWidth(s2), y2, s2, color);
        } else if (alignment == 4) {
            int leftWidth = width - font.getWidth(s2);
            if (leftWidth <= 0) {
                font.drawString(x2, y2, s2, color);
            }
            return FontUtils.drawJustifiedSpaceSeparatedSubstrings(font, s2, x2, y2, FontUtils.calculateWidthOfJustifiedSpaceInPixels(font, s2, leftWidth));
        }
        return resultingXCoordinate;
    }

    private static int calculateWidthOfJustifiedSpaceInPixels(Font font, String s2, int leftWidth) {
        int space = 0;
        int curpos = 0;
        while (curpos < s2.length()) {
            if (s2.charAt(curpos++) != ' ') continue;
            ++space;
        }
        if (space > 0) {
            space = (leftWidth + font.getWidth(" ") * space) / space;
        }
        return space;
    }

    private static int drawJustifiedSpaceSeparatedSubstrings(Font font, String s2, int x2, int y2, int justifiedSpaceWidth) {
        int curpos = 0;
        int endpos = 0;
        int resultingXCoordinate = x2;
        while (curpos < s2.length()) {
            endpos = s2.indexOf(32, curpos);
            if (endpos == -1) {
                endpos = s2.length();
            }
            String substring = s2.substring(curpos, endpos);
            font.drawString(resultingXCoordinate, y2, substring);
            resultingXCoordinate += font.getWidth(substring) + justifiedSpaceWidth;
            curpos = endpos + 1;
        }
        return resultingXCoordinate;
    }

    public class Alignment {
        public static final int LEFT = 1;
        public static final int CENTER = 2;
        public static final int RIGHT = 3;
        public static final int JUSTIFY = 4;
    }
}

