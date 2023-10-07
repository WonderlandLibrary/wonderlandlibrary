/*
 * Copyright (c) 2018-2021, for DirtyMod by dirt, Deleteboys, Phantom.
 * All rights reserved.
 *
 * Copyright (c) for Minecraft by Mojang.
 * (This license is not in contact with Mojangs)
 */

package koks.api.font;

import koks.api.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * todo: remove the mc color code autism i used lol
 */
public class DirtyFontRenderer {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzüöäABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ÜÖÄ!\"§$%&/()=?{[]}@`´'#*+-~|<>^°;,:._ ";
    private static final int SPACING = 4; // the spacing is only for the pictures
    private final HashMap<Character, DirtyCharacter> characterHashMap = new HashMap<>();
    private final HashMap<Character, Double> characterHeightHashMap = new HashMap<>();
    private boolean drawNextCharShadow;

    // https://open.gl/textures
    // https://stackoverflow.com/questions/1524855/how-to-calculate-the-fonts-width
    public DirtyFontRenderer(Font font) {
        Graphics2D graphics2D = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        graphics2D.setFont(font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        for (char c : CHARS.toCharArray()) {
            Rectangle2D bounds = fontMetrics.getStringBounds(Character.toString(c), graphics2D);
            double cWidth = bounds.getWidth() + SPACING * 2; // spacing on both sides
            double cHeight = bounds.getHeight() + SPACING;
            characterHeightHashMap.put(c, cHeight);
            BufferedImage bufferedImage = new BufferedImage(MathHelper.ceiling_double_int(cWidth), MathHelper.ceiling_double_int(cHeight), BufferedImage.TYPE_INT_ARGB); // round up if the char is a point number that chars are not cut off.
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setFont(font);
            // make the full picture transparent
            graphics.setColor(new Color(255, 255, 255, 0));
            graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            graphics.setColor(Color.white);
            // some anti aliasing things that the font looks not ugly
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.drawString(Character.toString(c), SPACING, fontMetrics.getAscent());
            DynamicTexture dynamicTexture = new DynamicTexture(bufferedImage);
            characterHashMap.put(c, new DirtyCharacter(dynamicTexture.getGlTextureId(), cWidth, cHeight));
        }
    }

    public void drawString(String text, int x, int y, float size, Color color, boolean dropShadow) {
        GL11.glScalef(size, size, size);
        final float mSize = (float) Math.pow(size, -1);
        drawString(text, Math.round(x / size), Math.round(y / size), color, dropShadow);
        GL11.glScalef(mSize, mSize, mSize);
    }

    public void drawString(String str, float x, float y, Color color, boolean dropShadow) {
        if (str == null || str.equals("")) {
            return;
        }

        if (dropShadow) { // this shit is from mc
            int c = color.getRGB();
            c = (c & 16579836) >> 2 | c & -16777216;
            drawNextCharShadow = true;
            drawString(str, x + 1f, y + 1f, new Color((c >> 16 & 255) / 255.0F, (c >> 8 & 255) / 255.0F, (c & 255) / 255.0F, (c >> 24 & 255) / 255.0F), false);
        }

        GL11.glPushMatrix();
        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        GL11.glScaled(0.5, 0.5, 0.5);

        // remove the spacing
        x -= SPACING / 2d;

        //scaling
        x *= 2;
        y *= 2;
        float currentX = x;

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '§' && i + 1 < chars.length) {
                int mcColor = Minecraft.getMinecraft().fontRendererObj.getColorCode(chars[i + 1]);
                if (drawNextCharShadow) {
                    mcColor = (mcColor & 16579836) >> 2 | mcColor & -16777216;
                }
                float red = (float) (mcColor >> 16 & 255) / 255.0F;
                float green = (float) (mcColor >> 8 & 255) / 255.0F;
                float blue = (float) (mcColor  & 255) / 255.0F;
                i++; // skip next letter
                GL11.glColor4d(red, green, blue, color.getAlpha() / 255f);
            } else if (characterHashMap.containsKey(c)) {
                DirtyCharacter dirtyCharacter = characterHashMap.get(c);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, dirtyCharacter.getTextureID());
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);

                GL11.glTexCoord2d(0, 0);
                GL11.glVertex2d(currentX, y);

                GL11.glTexCoord2d(0, 1);
                GL11.glVertex2d(currentX, y + dirtyCharacter.getHeight());

                GL11.glTexCoord2d(1, 1);
                GL11.glVertex2d(currentX + dirtyCharacter.getWidth(), y + dirtyCharacter.getHeight());

                GL11.glTexCoord2d(1, 0);
                GL11.glVertex2d(currentX + dirtyCharacter.getWidth(), y);

                GL11.glEnd();
                currentX += dirtyCharacter.getWidth() - SPACING * 2; // removes the right spacing
            } else {
                Logger.getInstance().log("Unknown letter: " + c);
            }
        }
        drawNextCharShadow = false;
        GlStateManager.bindTexture(0); // unbind with GlStateManager to prevent bugs in the mc font
        // disable blend it it wasn't enabled
        if (!blend) {
            GL11.glDisable(GL11.GL_BLEND);
        }
        // unbind texture
        GL11.glPopMatrix();
        // resets color
        GL11.glColor4d(1, 1, 1, 1);
    }

    public void drawCenteredString(String str, float x, float y, Color color, boolean dropShadow) {
        String s = StringUtils.stripControlCodes(str);
        drawString(str, x - getStringWidth(s) / 2f, y - getStringHeight(s) / 2f, color, dropShadow);
    }

    public float getStringWidth(String string) {
        if (string.equals("")) {
            return 0;
        }
        string = StringUtils.stripControlCodes(string);

        float currentX = 0;
        for (char c : string.toCharArray()) {
            if (characterHashMap.containsKey(c)) {
                DirtyCharacter dirtyCharacter = characterHashMap.get(c);
                currentX += dirtyCharacter.getWidth() - SPACING * 2; // removes the spacing
            } else {
                currentX += 5;
                Logger.getInstance().log("Unknown letter: " + c);
            }
        }
        return currentX / 2f; // remove the scaling of the pos and spacing
    }

    public float getStringHeight(String string) {
        float cHigh = 0;
        for (char c : string.toCharArray()) {
            if (characterHashMap.containsKey(c)) {
                float charHeight = (float) (characterHeightHashMap.get(c) - SPACING);
                if (charHeight > cHigh) {
                    cHigh = charHeight;
                }
            } else {
                cHigh += 5;
                Logger.getInstance().log("Unknown letter: " + c);
            }
        }
        return cHigh / 2 + 1;
    }

    private static final class DirtyCharacter {
        private final int textureID;
        private final double width, height;

        public DirtyCharacter(int textureID, double width, double height) {
            this.textureID = textureID;
            this.width = width;
            this.height = height;
        }

        public int getTextureID() {
            return textureID;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }
}
