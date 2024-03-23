package moonsense.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;


public class GuiUtils {

    private static final Map<String, ResourceLocation> playerSkins = new HashMap<>();
	final static Minecraft mc = Minecraft.getMinecraft();
    final static FontRenderer fr = mc.fontRendererObj;
    
    private static ResourceLocation LOGO;
    
    public static void drawChromaString(String string, int x, int y, boolean shadow)
    {
        int xTmp = x;
        for (char textChar : string.toCharArray())
        {
            long l = System.currentTimeMillis() - (xTmp * 10 - y * 10);
            int i = Color.HSBtoRGB(l % (int) 2000.0F / 2000.0F, 0.8F, 0.8F);
            String tmp = String.valueOf(textChar);
        }
    }

    public static int chromaColor(){
       int i = Color.HSBtoRGB(System.currentTimeMillis() % (int) 2000.0F / 2000.0F, 0.8F, 0.8F);
        return i;
    }

    public static ResourceLocation getHeadLocation(String displayName) {
        ResourceLocation playerSkin = (ResourceLocation)playerSkins.getOrDefault(displayName, new ResourceLocation("WizClient/heads/steve.png" + displayName + ".png"));
        if (!playerSkins.containsKey(displayName)) {
            ThreadDownloadImageData skinData = new ThreadDownloadImageData(null, "https://minotar.net/helm/" + displayName + "/32.png", new ResourceLocation("WizClient/heads/steve.png"), null);
            (Minecraft.getMinecraft()).getTextureManager().loadTexture(playerSkin, skinData);
            playerSkins.put(displayName, playerSkin);
        }
        return playerSkin;
    }

    public static void drawScaledLogo(float x, float y, int size, final String clientSource, final String logoName) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glPopMatrix();
        drawTexture(new ResourceLocation(clientSource+"/"+logoName), x - (float) size / 2, y - (float) size / 2, size, size);
    }

    public static void bindTexture(final ResourceLocation resourceLocation) {
        ITextureObject texture = Minecraft.getMinecraft().getTextureManager().getTexture(resourceLocation);
        if (texture == null) {
            texture = new SimpleTexture(resourceLocation);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, texture);
        }
        GL11.glBindTexture(3553, texture.getGlTextureId());
    }

    public static void drawTexture(final ResourceLocation resourceLocation, final float x, final float y, final float width, final float height) {
        GL11.glPushMatrix();
        final float size = width / 2.0f;
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2848);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(resourceLocation);
        GL11.glBegin(7);
        GL11.glTexCoord2d(0.0f / size, 0.0f / size);
        GL11.glVertex2d(x, y);
        GL11.glTexCoord2d(0.0f / size, (0.0f + size) / size);
        GL11.glVertex2d(x, y + height);
        GL11.glTexCoord2d((0.0f + size) / size, (0.0f + size) / size);
        GL11.glVertex2d(x + width, y + height);
        GL11.glTexCoord2d((0.0f + size) / size, 0.0f / size);
        GL11.glVertex2d((x + width), y);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    /* 
     * 
     * NORMAL
     * 
     */
    
    /**
     * @param x : X pos
     * @param y : Y pos
     * @param x1 : X2 pos
     * @param y1 : Y2 pos
     * @param radius : round of edges;
     * @param color : color;
     */
    
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), (double)-150).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), (double)-150).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), (double)-150).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), (double)-150).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle using the texture currently bound to the TextureManager
     */
    public static void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(xCoord + 0.0F), (double)(yCoord + (float)maxV), (double)-150).tex((double)((float)(minU + 0) * f), (double)((float)(minV + maxV) * f1)).endVertex();
        worldrenderer.pos((double)(xCoord + (float)maxU), (double)(yCoord + (float)maxV), (double)-150).tex((double)((float)(minU + maxU) * f), (double)((float)(minV + maxV) * f1)).endVertex();
        worldrenderer.pos((double)(xCoord + (float)maxU), (double)(yCoord + 0.0F), (double)-150).tex((double)((float)(minU + maxU) * f), (double)((float)(minV + 0) * f1)).endVertex();
        worldrenderer.pos((double)(xCoord + 0.0F), (double)(yCoord + 0.0F), (double)-150).tex((double)((float)(minU + 0) * f), (double)((float)(minV + 0) * f1)).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    public static void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(xCoord + 0), (double)(yCoord + heightIn), (double)-150).tex((double)textureSprite.getMinU(), (double)textureSprite.getMaxV()).endVertex();
        worldrenderer.pos((double)(xCoord + widthIn), (double)(yCoord + heightIn), (double)-150).tex((double)textureSprite.getMaxU(), (double)textureSprite.getMaxV()).endVertex();
        worldrenderer.pos((double)(xCoord + widthIn), (double)(yCoord + 0), (double)-150).tex((double)textureSprite.getMaxU(), (double)textureSprite.getMinV()).endVertex();
        worldrenderer.pos((double)(xCoord + 0), (double)(yCoord + 0), (double)-150).tex((double)textureSprite.getMinU(), (double)textureSprite.getMinV()).endVertex();
        tessellator.draw();
    }

    public static void drawSmoothRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(3042);
        glDisable(3553);
        glEnable(GL_LINE_SMOOTH);
        setColor(color);
        glEnable(2848);
        glBegin(GL_POLYGON);
        int i;
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius); 
        glEnd();
        glBegin(GL_LINE_LOOP);
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius); 
        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glDisable(3042);
        glDisable(GL_LINE_SMOOTH);
        glEnable(3553);
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
      }
    public static void drawRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(3042);
        glDisable(3553);
        glEnable(GL_LINE_SMOOTH);
        setColor(color);
        glEnable(2848);
        glBegin(GL_POLYGON);
        int i;
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius); 
        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glDisable(3042);
        glDisable(GL_LINE_SMOOTH);
        glEnable(3553);
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
      }
    /**
     * @param x : X pos
     * @param y : Y pos
     * @param x1 : X2 pos
     * @param y1 : Y2 pos
     * @param radius : round of edges;
     * @param lineWidth : width of outline line;
     * @param color : color;
     */
    
    public static void drawRoundedOutline(float x, float y, float x1, float y1, float radius,float lineWidth, int color) {
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(3042);
        glDisable(3553);
        setColor(color);
        glEnable(2848);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        int i;
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius); 
        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glDisable(3042);
        glEnable(3553);
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
      }
    
    /*
     * 
     * SELECTED EDGES
     * 
     */
    
    /**
     * @param x : X pos
     * @param y : Y pos
     * @param x1 : X2 pos
     * @param y1 : Y2 pos
     * @param radius1 : round of left top edges;
     * @param radius2 : round of right top edges;
     * @param radius3 : round of left bottom edges;
     * @param radius4 : round of right bottom edges;
     * @param color : color;
     */
    
    public static void drawSelectRoundedRect(float x, float y, float x1, float y1, float radius1,float radius2,float radius3,float radius4, int color) {
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(3042);
        glDisable(3553);
        setColor(color);
        glEnable(2848);
        glBegin(9);
        int i;
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x + radius1 + Math.sin(i * Math.PI / 180.0D) * radius1 * -1.0D, y + radius1 + Math.cos(i * Math.PI / 180.0D) * radius1 * -1.0D); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x + radius2 + Math.sin(i * Math.PI / 180.0D) * radius2 * -1.0D, y1 - radius2 + Math.cos(i * Math.PI / 180.0D) * radius2 * -1.0D); 
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x1 - radius3 + Math.sin(i * Math.PI / 180.0D) * radius3, y1 - radius3 + Math.cos(i * Math.PI / 180.0D) * radius3); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x1 - radius4 + Math.sin(i * Math.PI / 180.0D) * radius4, y + radius4 + Math.cos(i * Math.PI / 180.0D) * radius4); 
        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glDisable(3042);
        glEnable(3553);
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
      }

    /**
     * @param x : X pos
     * @param y : Y pos
     * @param x1 : X2 pos
     * @param y1 : Y2 pos
     * @param radius1 : round of left top edges;
     * @param radius2 : round of right top edges;
     * @param radius3 : round of left bottom edges;
     * @param radius4 : round of right bottom edges;
     * @param lineWidth : width of outline line;
     * @param color : color;
     */
    
    public static void drawSelectRoundedOutline(float x, float y, float x1, float y1, float radius1,float radius2,float radius3,float radius4,float lineWidth, int color) {
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(3042);
        glDisable(3553);
        setColor(color);
        glEnable(2848);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        int i;
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x + radius1 + Math.sin(i * Math.PI / 180.0D) * radius1 * -1.0D, y + radius1 + Math.cos(i * Math.PI / 180.0D) * radius1 * -1.0D); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x + radius2 + Math.sin(i * Math.PI / 180.0D) * radius2 * -1.0D, y1 - radius2 + Math.cos(i * Math.PI / 180.0D) * radius2 * -1.0D); 
        for (i = 0; i <= 90; i += 3)
          glVertex2d(x1 - radius3 + Math.sin(i * Math.PI / 180.0D) * radius3, y1 - radius3 + Math.cos(i * Math.PI / 180.0D) * radius3); 
        for (i = 90; i <= 180; i += 3)
          glVertex2d(x1 - radius4 + Math.sin(i * Math.PI / 180.0D) * radius4, y + radius4 + Math.cos(i * Math.PI / 180.0D) * radius4); 
        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glDisable(3042);
        glEnable(3553);
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
      }
      public static void setColor(int color) {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        glColor4f(r, g, b, a);
      }
      
      /*
       * 
       * GRADIENT 
       * 
       */
      
      /**
       * @param x : X pos
       * @param y : Y pos
       * @param x1 : X2 pos
       * @param y1 : Y2 pos
       * @param radius : round of edges;
       * @param color : color;
       * @param color2 : color2;
       * @param color3 : color3;
       * @param color4 : color4;
       */
      public static void drawRoundedGradientRectCorner(float x, float y, float x1, float y1, float radius, int color, int color2, int color3, int color4) {
          GL11.glEnable(3042);
          GL11.glDisable(3553);
          GL11.glBlendFunc(770, 771);
          GL11.glEnable(2848);
          GL11.glShadeModel(7425);
          
          glPushAttrib(0);
          glScaled(0.5D, 0.5D, 0.5D);
          x *= 2.0D;
          y *= 2.0D;
          x1 *= 2.0D;
          y1 *= 2.0D;
          glEnable(3042);
          glDisable(3553);
          setColor(color);
          glEnable(2848);
          GL11.glShadeModel(7425);
          glBegin(9);
          int i;
          for (i = 0; i <= 90; i += 3)
              setColor(color);
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
          for (i = 90; i <= 180; i += 3)
              setColor(color2);
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
          for (i = 0; i <= 90; i += 3)
              setColor(color3);
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius); 
          for (i = 90; i <= 180; i += 3)
              setColor(color4);
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius); 
          glEnd();
          glEnable(3553);
          glDisable(3042);
          glDisable(2848);
          glDisable(3042);
          glEnable(3553);
          glScaled(2.0D, 2.0D, 2.0D);
          glPopAttrib();
          

          GL11.glEnable(3553);
          GL11.glDisable(3042);
          GL11.glDisable(2848);
          GL11.glShadeModel(7424);
        }
      /*NOT WORKING ATM*/
      /*
       * 
       * IMAGE 
       * 
       */
      
      /**
       * 
       * @see dont worget to bind a texture to it!
       * 
       * @param x : X pos
       * @param y : Y pos
       * @param x1 : X2 pos
       * @param y1 : Y2 pos
       * @param radius : round of edges;
       */
      
      public static void drawModalRectWithCustomSizedTexture(final float x, final float y, final float u, final float v, final int width, final int height, final float textureWidth, final float textureHeight) {
    	  final float f = 1.0F / textureWidth;
          final float f1 = 1.0F / textureHeight;
          Tessellator tessellator = Tessellator.getInstance();
          WorldRenderer worldrenderer = tessellator.getWorldRenderer();
          worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
          worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
          worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
          worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
          worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
          tessellator.draw();
      }

      public static void drawScaledCustomSizeModalRect(final float x, final float y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight) {
    	  final float f = 1.0F / tileWidth;
          final float f1 = 1.0F / tileHeight;
          Tessellator tessellator = Tessellator.getInstance();
          WorldRenderer worldrenderer = tessellator.getWorldRenderer();
          worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
          worldrenderer.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)vHeight) * f1)).endVertex();
          worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).endVertex();
          worldrenderer.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)uWidth) * f), (double)(v * f1)).endVertex();
          worldrenderer.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
          tessellator.draw();
      }

      public static int glToRGB(final float red, final float green, final float blue, final float alpha) {
          return new Color((int)red * 255, (int)green * 255, (int)blue * 255, (int)alpha * 255).getRGB();
      }

      public static float rgbToGl(final int rgb) {
          return rgb / 255.0f;
      }

      public static void setGlColor(final int color) {
          final float alpha = (color >> 24 & 0xFF) / 255.0f;
          final float red = (color >> 16 & 0xFF) / 255.0f;
          final float green = (color >> 8 & 0xFF) / 255.0f;
          final float blue = (color & 0xFF) / 255.0f;
          GlStateManager.color(red, green, blue, alpha);
      }

      public static void setGlColor(final int color, final float alpha) {
          final float red = (color >> 16 & 0xFF) / 255.0f;
          final float green = (color >> 8 & 0xFF) / 255.0f;
          final float blue = (color & 0xFF) / 255.0f;
          GlStateManager.color(red, green, blue, alpha);
      }

      public static int getRGB(final int color, final int alpha) {
          return new Color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, alpha).getRGB();
      }

      public static Color getColor(final int color) {
          return new Color(color, true);
      }

      public static int getAlpha(final int color) {
          return color >> 24 & 0xFF;
      }

      public static int hsvToRgb(int hue, final int saturation, final int value) {
          hue %= 360;
          final float s = saturation / 100.0f;
          final float v = value / 100.0f;
          final float c = v * s;
          final float h = hue / 60.0f;
          final float x = c * (1.0f - Math.abs(h % 2.0f - 1.0f));
          float r = 0.0f;
          float g = 0.0f;
          float b = 0.0f;
          switch (hue / 60) {
              case 0: {
                  r = c;
                  g = x;
                  b = 0.0f;
                  break;
              }
              case 1: {
                  r = x;
                  g = c;
                  b = 0.0f;
                  break;
              }
              case 2: {
                  r = 0.0f;
                  g = c;
                  b = x;
                  break;
              }
              case 3: {
                  r = 0.0f;
                  g = x;
                  b = c;
                  break;
              }
              case 4: {
                  r = x;
                  g = 0.0f;
                  b = c;
                  break;
              }
              case 5: {
                  r = c;
                  g = 0.0f;
                  b = x;
                  break;
              }
              default: {
                  return 0;
              }
          }
          final float m = v - c;
          return (int)((r + m) * 255.0f) << 16 | (int)((g + m) * 255.0f) << 8 | (int)((b + m) * 255.0f);
      }

      public static int[] rgbToHsv(final int rgb) {
          final float r = ((rgb & 0xFF0000) >> 16) / 255.0f;
          final float g = ((rgb & 0xFF00) >> 8) / 255.0f;
          final float b = (rgb & 0xFF) / 255.0f;
          final float M = (r > g) ? Math.max(r, b) : Math.max(g, b);
          final float m = (r < g) ? Math.min(r, b) : Math.min(g, b);
          final float c = M - m;
          float h;
          if (M == r) {
              for (h = (g - b) / c; h < 0.0f; h += 6.0f) {}
              h %= 6.0f;
          }
          else if (M == g) {
              h = (b - r) / c + 2.0f;
          }
          else {
              h = (r - g) / c + 4.0f;
          }
          h *= 60.0f;
          final float s = c / M;
          return new int[] { (c == 0.0f) ? -1 : ((int)h), (int)(s * 100.0f), (int)(M * 100.0f) };
      }

      public static int getIntermediateColor(final int a, final int b, final float percent) {
          final float avgRed = (a >> 16 & 0xFF) * percent + (b >> 16 & 0xFF) * (1.0f - percent);
          final float avgGreen = (a >> 8 & 0xFF) * percent + (b >> 8 & 0xFF) * (1.0f - percent);
          final float avgBlue = (a >> 0 & 0xFF) * percent + (b >> 0 & 0xFF) * (1.0f - percent);
          final float avgAlpha = (a >> 24 & 0xFF) * percent + (b >> 24 & 0xFF) * (1.0f - percent);
          try {
              return new Color(avgRed / 255.0f, avgGreen / 255.0f, avgBlue / 255.0f, avgAlpha / 255.0f).getRGB();
          }
          catch (IllegalArgumentException e) {
              return Integer.MIN_VALUE;
          }
      }

      public static int convertPercentToValue(final float percent) {
          return (int)(percent * 255.0f);
      }
}
