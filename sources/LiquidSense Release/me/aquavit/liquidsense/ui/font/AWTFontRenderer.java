package me.aquavit.liquidsense.ui.font;

import me.aquavit.liquidsense.module.modules.client.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class AWTFontRenderer {
    private int fontHeight;
    private CharLocation[] charLocations;
    private HashMap<String, CachedFont> cachedStrings;
    private int textureID;
    private int textureWidth;
    private int textureHeight;
    private Font font;
    private static boolean assumeNonVolatile = false;
    private static ArrayList<AWTFontRenderer> activeFontRenderers = new ArrayList<AWTFontRenderer>();
    private static int gcTicks;
    private static int GC_TICKS = 600;
    private static int CACHED_FONT_REMOVAL_TIME = 30000;
    
    public AWTFontRenderer(Font font, int startChar, int stopChar){
        this.font = font;
        this.fontHeight = -1;
        this.charLocations = new CharLocation[stopChar];
        this.cachedStrings = new HashMap();
        this.renderBitmap(startChar, stopChar);
        activeFontRenderers.add(this);
    }
    
    public AWTFontRenderer(Font font){
        this(font, 0, 255);
    }

    public static class Companion {
        public boolean getAssumeNonVolatile() {
            return assumeNonVolatile;
        }

        public static void setAssumeNonVolatile(boolean assumeNonVolatile) {
            assumeNonVolatile = assumeNonVolatile;
        }

        public static ArrayList<AWTFontRenderer> getActiveFontRenderers() {
            return activeFontRenderers;
        }

        public static void garbageCollectionTick() {
            if (gcTicks++ > GC_TICKS) {
                activeFontRenderers.forEach(AWTFontRenderer::collectGarbage);
                gcTicks = 0;
            }
        }

    }

    private void collectGarbage() {
        long currentTime = System.currentTimeMillis();

        cachedStrings.entrySet().stream().filter(it ->
                currentTime - it.getValue().getLastUsage() > CACHED_FONT_REMOVAL_TIME).forEach(it -> {
            GL11.glDeleteLists(it.getValue().getDisplayList(), 1);

            it.getValue().setDeleted(true);

            cachedStrings.remove(it.getKey());
        });
    }

    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    public void drawString(String text, double x, double y, int color) {
        double scale = 0.5;
        double reverse = 1 / scale;

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        GL11.glTranslated(x * 2F, y * 2.0 - 2.0, 0.0);
        GlStateManager.bindTexture(textureID);

        float red = (float) ((color >> 16) & 0xFF) / 255F;
        float green = (float) ((color >> 8) & 0xFF) / 255F;
        float blue = (float) (color & 0xFF) / 255F;
        float alpha = (float) ((color >> 24) & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);

        double currX = 0.0;

        CachedFont cached = cachedStrings.get(text);

        if (cached != null) {
            GL11.glCallList(cached.getDisplayList());

            cached.setLastUsage(System.currentTimeMillis());

            GlStateManager.popMatrix();

            return;
        }

        int list = -1;

        if (assumeNonVolatile) {
            list = GL11.glGenLists(1);

            GL11.glNewList(list, GL11.GL_COMPILE_AND_EXECUTE);
        }

        GL11.glBegin(GL11.GL_QUADS);

        for (char ch : text.toCharArray()) {
            if (ch >= charLocations.length) {
                GL11.glEnd();

                // Ugly solution, because floating point numbers, but I think that shouldn't be that much of a problem
                GlStateManager.scale(reverse, reverse, reverse);
                Minecraft.getMinecraft().fontRendererObj.drawString(String.valueOf(ch), (float) (currX * scale + 1), 2f, color, false);
                currX += Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf(ch)) * reverse;

                GlStateManager.scale(scale, scale, scale);
                GlStateManager.bindTexture(textureID);
                GlStateManager.color(red, green, blue, alpha);

                GL11.glBegin(GL11.GL_QUADS);
            } else {
                CharLocation fontChar = charLocations[ch];

                if (fontChar != null) {
                    drawChar(fontChar, (float) currX, 0f);
                    currX += (float) fontChar.getWidth() - HUD.fontWidth.get();
                }
            }
        }


        GL11.glEnd();

        if (assumeNonVolatile) {
            cachedStrings.put(text, new CachedFont(list, System.currentTimeMillis(), false));
            GL11.glEndList();
        }

        GlStateManager.popMatrix();
    }

    private void drawChar(CharLocation charLocation, float x, float y) {
        float width = charLocation.getWidth();
        float height = charLocation.getHeight();
        float srcX = charLocation.getX();
        float srcY = charLocation.getY();
        float renderX = srcX / (float)textureWidth;
        float renderY = srcY / (float)textureHeight;
        float renderWidth = width / (float)textureWidth;
        float renderHeight = height / (float)textureHeight;


        GL11.glTexCoord2f(renderX, renderY);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(renderX, renderY + renderHeight);
        GL11.glVertex2f(x, y + height);
        GL11.glTexCoord2f(renderX + renderWidth, renderY + renderHeight);
        GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(renderX + renderWidth, renderY);
        GL11.glVertex2f(x + width, y);
    }

    private void renderBitmap(int startChar, int stopChar) {
        BufferedImage[] fontImages = new BufferedImage[stopChar];
        int rowHeight = 0;
        int charX = 0;
        int charY = 0;

        for (int targetChar = startChar; targetChar < stopChar; ++targetChar) {

            BufferedImage fontImage = drawCharToImage((char)targetChar);
            CharLocation fontChar = new CharLocation(charX, charY, fontImage.getWidth(), fontImage.getHeight());

            if (fontChar.getHeight() > fontHeight) fontHeight = fontChar.getHeight();
            if (fontChar.getHeight() > rowHeight) rowHeight = fontChar.getHeight();

            charLocations[targetChar] = fontChar;
            fontImages[targetChar] = fontImage;

            charX += fontChar.getWidth();

            if (charX > 2048) {
                if (charX > textureWidth) textureWidth = charX;

                charX = 0;
                charY += rowHeight;
                rowHeight = 0;
            }
        }
        textureHeight = charY + rowHeight;

        BufferedImage bufferedImage = new BufferedImage(textureWidth, textureHeight, 2);
        Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
        graphics2D.setFont(font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, textureWidth, textureHeight);
        graphics2D.setColor(Color.white);

        for (int targetChar = startChar; targetChar < stopChar; ++targetChar) if (fontImages[targetChar] != null && charLocations[targetChar] != null)
            graphics2D.drawImage(fontImages[targetChar], charLocations[targetChar].getX(), charLocations[targetChar].getY(), null);

        textureID = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), bufferedImage, false, true);
    }

    private BufferedImage drawCharToImage(char ch) {
        Graphics2D graphics2D = (Graphics2D) new BufferedImage(1, 1, 2).getGraphics();
        graphics2D.setFont(this.font);

        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int charWidth = fontMetrics.charWidth(ch) + 8;
        if (charWidth <= 0) charWidth = 7;

        int charHeight = fontMetrics.getHeight() + 3;
        if (charHeight <= 0) charHeight = font.getSize();

        BufferedImage fontImage = new BufferedImage(charWidth, charHeight, 2);
        Graphics2D graphics = (Graphics2D) fontImage.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        graphics.setFont(this.font);
        graphics.setColor(Color.WHITE);
        graphics.drawString(String.valueOf(ch), 3, 1 + fontMetrics.getAscent());
        return fontImage;
    }

    public int getStringWidth(String text) {
        int width = 0;

        for (int c : text.toCharArray()) {
            CharLocation fontChar = charLocations[c < charLocations.length ? c : (int) '\u0003'];
            if (fontChar == null) continue;

            width += fontChar.getWidth() - HUD.fontWidth.get();
        }

        return width / 2;
    }

    public Font getFont() {
        return this.font;
    }

}
