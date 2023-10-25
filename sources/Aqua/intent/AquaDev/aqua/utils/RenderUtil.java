package intent.AquaDev.aqua.utils;

import intent.AquaDev.aqua.modules.visual.HUD;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
    static Minecraft mc = Minecraft.getMinecraft();

    public static void polygon(double x, double y, double sideLength, double amountOfSides, boolean filled, Color color) {
        sideLength /= 2.0;
        RenderUtil.start();
        if (color != null) {
            RenderUtil.setGLColor(color);
        }
        if (!filled) {
            GL11.glLineWidth((float)2.0f);
        }
        GL11.glEnable((int)2848);
        RenderUtil.begin(filled ? 6 : 3);
        for (double i = 0.0; i <= amountOfSides / 4.0; i += 1.0) {
            double angle = i * 4.0 * (Math.PI * 2) / 360.0;
            RenderUtil.vertex(x + sideLength * Math.cos((double)angle) + sideLength, y + sideLength * Math.sin((double)angle) + sideLength);
        }
        RenderUtil.end();
        GL11.glDisable((int)2848);
        RenderUtil.stop();
    }

    public static void start() {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    public void circle(double x, double y, double radius, boolean filled, Color color) {
        RenderUtil.polygon(x, y, radius, 360.0, filled, color);
    }

    public void circle(double x, double y, double radius, boolean filled) {
        this.polygon(x, y, radius, 360, filled);
    }

    public static void circle(double x, double y, double radius, Color color) {
        RenderUtil.polygon(x, y, radius, 360, color);
    }

    public static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        RenderUtil.color(Color.white);
    }

    public static void vertex(double x, double y) {
        GL11.glVertex2d((double)x, (double)y);
    }

    public static void begin(int glMode) {
        GL11.glBegin((int)glMode);
    }

    public static void end() {
        GL11.glEnd();
    }

    public void polygon(double x, double y, double sideLength, int amountOfSides, boolean filled) {
        RenderUtil.polygon(x, y, sideLength, amountOfSides, filled, null);
    }

    public static void polygon(double x, double y, double sideLength, int amountOfSides, Color color) {
        RenderUtil.polygon(x, y, sideLength, amountOfSides, true, color);
    }

    public void polygon(double x, double y, double sideLength, int amountOfSides) {
        RenderUtil.polygon(x, y, sideLength, amountOfSides, true, null);
    }

    public static void drawEntityServerESP(Entity entity, float red, float green, float blue, float alpha, float lineAlpha, float lineWidth) {
        double d0 = (double)entity.serverPosX / 32.0;
        double d1 = (double)entity.serverPosY / 32.0;
        double d2 = (double)entity.serverPosZ / 32.0;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            d0 = livingBase.realPos.xCoord;
            d1 = livingBase.realPos.yCoord;
            d2 = livingBase.realPos.zCoord;
        }
        float x = (float)(d0 - mc.getRenderManager().getRenderPosX());
        float y = (float)(d1 - mc.getRenderManager().getRenderPosY());
        float z = (float)(d2 - mc.getRenderManager().getRenderPosZ());
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.otherDrawBoundingBox(entity, x, y, z, entity.width - 0.2f, entity.height + 0.1f);
        if (lineWidth > 0.0f) {
            GL11.glLineWidth((float)lineWidth);
            GL11.glColor4f((float)red, (float)green, (float)blue, (float)lineAlpha);
            RenderUtil.otherDrawOutlinedBoundingBox(entity, x, y, z, entity.width - 0.2f, entity.height + 0.1f);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void otherDrawOutlinedBoundingBox(Entity entity, float x, float y, float z, double width, double height) {
        float newYaw4;
        float newYaw3;
        float newYaw2;
        float newYaw1;
        width *= 1.5;
        float yaw1 = MathHelper.wrapAngleTo180_float((float)entity.getRotationYawHead()) + 45.0f;
        if (yaw1 < 0.0f) {
            newYaw1 = 0.0f;
            newYaw1 += 360.0f - Math.abs((float)yaw1);
        } else {
            newYaw1 = yaw1;
        }
        newYaw1 *= -1.0f;
        newYaw1 = (float)((double)newYaw1 * (Math.PI / 180));
        float yaw2 = MathHelper.wrapAngleTo180_float((float)entity.getRotationYawHead()) + 135.0f;
        if (yaw2 < 0.0f) {
            newYaw2 = 0.0f;
            newYaw2 += 360.0f - Math.abs((float)yaw2);
        } else {
            newYaw2 = yaw2;
        }
        newYaw2 *= -1.0f;
        newYaw2 = (float)((double)newYaw2 * (Math.PI / 180));
        float yaw3 = MathHelper.wrapAngleTo180_float((float)entity.getRotationYawHead()) + 225.0f;
        if (yaw3 < 0.0f) {
            newYaw3 = 0.0f;
            newYaw3 += 360.0f - Math.abs((float)yaw3);
        } else {
            newYaw3 = yaw3;
        }
        newYaw3 *= -1.0f;
        newYaw3 = (float)((double)newYaw3 * (Math.PI / 180));
        float yaw4 = MathHelper.wrapAngleTo180_float((float)entity.getRotationYawHead()) + 315.0f;
        if (yaw4 < 0.0f) {
            newYaw4 = 0.0f;
            newYaw4 += 360.0f - Math.abs((float)yaw4);
        } else {
            newYaw4 = yaw4;
        }
        newYaw4 *= -1.0f;
        newYaw4 = (float)((double)newYaw4 * (Math.PI / 180));
        float x1 = (float)(Math.sin((double)newYaw1) * width + (double)x);
        float z1 = (float)(Math.cos((double)newYaw1) * width + (double)z);
        float x2 = (float)(Math.sin((double)newYaw2) * width + (double)x);
        float z2 = (float)(Math.cos((double)newYaw2) * width + (double)z);
        float x3 = (float)(Math.sin((double)newYaw3) * width + (double)x);
        float z3 = (float)(Math.cos((double)newYaw3) * width + (double)z);
        float x4 = (float)(Math.sin((double)newYaw4) * width + (double)x);
        float z4 = (float)(Math.cos((double)newYaw4) * width + (double)z);
        float y2 = (float)((double)y + height);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)x1, (double)y, (double)z1).endVertex();
        worldrenderer.pos((double)x1, (double)y2, (double)z1).endVertex();
        worldrenderer.pos((double)x2, (double)y2, (double)z2).endVertex();
        worldrenderer.pos((double)x2, (double)y, (double)z2).endVertex();
        worldrenderer.pos((double)x1, (double)y, (double)z1).endVertex();
        worldrenderer.pos((double)x4, (double)y, (double)z4).endVertex();
        worldrenderer.pos((double)x3, (double)y, (double)z3).endVertex();
        worldrenderer.pos((double)x3, (double)y2, (double)z3).endVertex();
        worldrenderer.pos((double)x4, (double)y2, (double)z4).endVertex();
        worldrenderer.pos((double)x4, (double)y, (double)z4).endVertex();
        worldrenderer.pos((double)x4, (double)y2, (double)z4).endVertex();
        worldrenderer.pos((double)x3, (double)y2, (double)z3).endVertex();
        worldrenderer.pos((double)x2, (double)y2, (double)z2).endVertex();
        worldrenderer.pos((double)x2, (double)y, (double)z2).endVertex();
        worldrenderer.pos((double)x3, (double)y, (double)z3).endVertex();
        worldrenderer.pos((double)x4, (double)y, (double)z4).endVertex();
        worldrenderer.pos((double)x4, (double)y2, (double)z4).endVertex();
        worldrenderer.pos((double)x1, (double)y2, (double)z1).endVertex();
        worldrenderer.pos((double)x1, (double)y, (double)z1).endVertex();
        worldrenderer.endVertex();
        tessellator.draw();
    }

    public static void otherDrawBoundingBox(Entity entity, float x, float y, float z, double width, double height) {
        float newYaw4;
        float newYaw3;
        float newYaw2;
        float newYaw1;
        width *= 1.5;
        float yaw1 = MathHelper.wrapAngleTo180_float((float)entity.getRotationYawHead()) + 45.0f;
        if (yaw1 < 0.0f) {
            newYaw1 = 0.0f;
            newYaw1 += 360.0f - Math.abs((float)yaw1);
        } else {
            newYaw1 = yaw1;
        }
        newYaw1 *= -1.0f;
        newYaw1 = (float)((double)newYaw1 * (Math.PI / 180));
        float yaw2 = MathHelper.wrapAngleTo180_float((float)entity.getRotationYawHead()) + 135.0f;
        if (yaw2 < 0.0f) {
            newYaw2 = 0.0f;
            newYaw2 += 360.0f - Math.abs((float)yaw2);
        } else {
            newYaw2 = yaw2;
        }
        newYaw2 *= -1.0f;
        newYaw2 = (float)((double)newYaw2 * (Math.PI / 180));
        float yaw3 = MathHelper.wrapAngleTo180_float((float)entity.getRotationYawHead()) + 225.0f;
        if (yaw3 < 0.0f) {
            newYaw3 = 0.0f;
            newYaw3 += 360.0f - Math.abs((float)yaw3);
        } else {
            newYaw3 = yaw3;
        }
        newYaw3 *= -1.0f;
        newYaw3 = (float)((double)newYaw3 * (Math.PI / 180));
        float yaw4 = MathHelper.wrapAngleTo180_float((float)entity.getRotationYawHead()) + 315.0f;
        if (yaw4 < 0.0f) {
            newYaw4 = 0.0f;
            newYaw4 += 360.0f - Math.abs((float)yaw4);
        } else {
            newYaw4 = yaw4;
        }
        newYaw4 *= -1.0f;
        newYaw4 = (float)((double)newYaw4 * (Math.PI / 180));
        float x1 = (float)(Math.sin((double)newYaw1) * width + (double)x);
        float z1 = (float)(Math.cos((double)newYaw1) * width + (double)z);
        float x2 = (float)(Math.sin((double)newYaw2) * width + (double)x);
        float z2 = (float)(Math.cos((double)newYaw2) * width + (double)z);
        float x3 = (float)(Math.sin((double)newYaw3) * width + (double)x);
        float z3 = (float)(Math.cos((double)newYaw3) * width + (double)z);
        float x4 = (float)(Math.sin((double)newYaw4) * width + (double)x);
        float z4 = (float)(Math.cos((double)newYaw4) * width + (double)z);
        float y2 = (float)((double)y + height);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)x1, (double)y, (double)z1).endVertex();
        worldrenderer.pos((double)x1, (double)y2, (double)z1).endVertex();
        worldrenderer.pos((double)x2, (double)y2, (double)z2).endVertex();
        worldrenderer.pos((double)x2, (double)y, (double)z2).endVertex();
        worldrenderer.pos((double)x2, (double)y, (double)z2).endVertex();
        worldrenderer.pos((double)x2, (double)y2, (double)z2).endVertex();
        worldrenderer.pos((double)x3, (double)y2, (double)z3).endVertex();
        worldrenderer.pos((double)x3, (double)y, (double)z3).endVertex();
        worldrenderer.pos((double)x3, (double)y, (double)z3).endVertex();
        worldrenderer.pos((double)x3, (double)y2, (double)z3).endVertex();
        worldrenderer.pos((double)x4, (double)y2, (double)z4).endVertex();
        worldrenderer.pos((double)x4, (double)y, (double)z4).endVertex();
        worldrenderer.pos((double)x4, (double)y, (double)z4).endVertex();
        worldrenderer.pos((double)x4, (double)y2, (double)z4).endVertex();
        worldrenderer.pos((double)x1, (double)y2, (double)z1).endVertex();
        worldrenderer.pos((double)x1, (double)y, (double)z1).endVertex();
        worldrenderer.pos((double)x1, (double)y, (double)z1).endVertex();
        worldrenderer.pos((double)x2, (double)y, (double)z2).endVertex();
        worldrenderer.pos((double)x3, (double)y, (double)z3).endVertex();
        worldrenderer.pos((double)x4, (double)y, (double)z4).endVertex();
        worldrenderer.pos((double)x1, (double)y2, (double)z1).endVertex();
        worldrenderer.pos((double)x2, (double)y2, (double)z2).endVertex();
        worldrenderer.pos((double)x3, (double)y2, (double)z3).endVertex();
        worldrenderer.pos((double)x4, (double)y2, (double)z4).endVertex();
        worldrenderer.endVertex();
        tessellator.draw();
    }

    public static void drawTriangleFilled(float x, float y, float width, float height, int color) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)1.0f);
        RenderUtil.glColor(color);
        GL11.glBegin((int)9);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glVertex2d((double)(x + width * 2.0f), (double)y);
        GL11.glVertex2d((double)(x + width * 2.0f), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void drawImage(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        mc.getTextureManager().bindTexture(resourceLocation);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawImageDarker(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        mc.getTextureManager().bindTexture(resourceLocation);
        GL11.glColor4f((float)0.19607843f, (float)0.19607843f, (float)0.19607843f, (float)1.0f);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRoundedRect2(double x, double y, double width, double height, double cornerRadius, int color) {
        RenderUtil.drawRoundedRect2(x, y, width, height, cornerRadius, true, true, true, true, color);
    }

    public static void drawRoundedRect(double x, double y, double width, double height, double cornerRadius, int color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtil.setGLColor(color);
        GL11.glBegin((int)9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (i = 0; i <= 90; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 90; i <= 180; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 180; i <= 270; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        for (i = 270; i <= 360; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        GL11.glEnd();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        RenderUtil.setGLColor(Color.white);
    }

    public static void drawRoundedRect2(double x, double y, double width, double height, double cornerRadius, boolean leftTop, boolean rightTop, boolean rightBottom, boolean leftBottom, int color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        RenderUtil.setGLColor(color);
        GL11.glBegin((int)9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        if (rightBottom) {
            for (i = 0; i <= 90; ++i) {
                GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
            }
        } else {
            GL11.glVertex2d((double)(x + width), (double)(y + height));
        }
        if (rightTop) {
            cornerX = x + width - cornerRadius;
            cornerY = y + cornerRadius;
            for (i = 90; i <= 180; ++i) {
                GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
            }
        } else {
            GL11.glVertex2d((double)(x + width), (double)y);
        }
        if (leftTop) {
            cornerX = x + cornerRadius;
            cornerY = y + cornerRadius;
            for (i = 180; i <= 270; ++i) {
                GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
            }
        } else {
            GL11.glVertex2d((double)x, (double)y);
        }
        if (leftBottom) {
            cornerX = x + cornerRadius;
            cornerY = y + height - cornerRadius;
            for (i = 270; i <= 360; ++i) {
                GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
            }
        } else {
            GL11.glVertex2d((double)x, (double)(y + height));
        }
        GL11.glEnd();
        RenderUtil.setGLColor(new Color(255, 255, 255, 255));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void setGLColor(Color color) {
        float r = (float)color.getRed() / 255.0f;
        float g = (float)color.getGreen() / 255.0f;
        float b = (float)color.getBlue() / 255.0f;
        float a = (float)color.getAlpha() / 255.0f;
        GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
    }

    public static void setGLColor(int color) {
        RenderUtil.setGLColor(new Color(color));
    }

    public static void drawRoundedRectGradient(double x, double y, double width, double height, double cornerRadius, Color start, Color end) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        RenderUtil.color(start);
        GL11.glBegin((int)9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (i = 0; i <= 90; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 90; i <= 180; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        RenderUtil.color(end);
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 180; i <= 270; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        for (i = 270; i <= 360; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        GL11.glEnd();
        GL11.glShadeModel((int)7424);
        RenderUtil.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void drawGradientRectHorizontal(double x, double y, double width, double height, int startColor, int endColor) {
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        RenderUtil.glColor(startColor);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        RenderUtil.glColor(endColor);
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glShadeModel((int)7424);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void color(float red, float green, float blue, float alpha) {
        GL11.glColor4f((float)(red / 255.0f), (float)(green / 255.0f), (float)(blue / 255.0f), (float)(alpha / 255.0f));
    }

    public static void color(Color color) {
        RenderUtil.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void drawRoundedRect3(double x, double y, double width, double height, double cornerRadius, boolean leftTop, boolean rightTop, boolean rightBottom, boolean leftBottom, Color color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtil.setGLColor(color);
        GL11.glBegin((int)9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        if (rightBottom) {
            for (i = 0; i <= 90; i += 30) {
                GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
            }
        } else {
            GL11.glVertex2d((double)(x + width), (double)(y + height));
        }
        if (rightTop) {
            cornerX = x + width - cornerRadius;
            cornerY = y + cornerRadius;
            for (i = 90; i <= 180; i += 30) {
                GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
            }
        } else {
            GL11.glVertex2d((double)(x + width), (double)y);
        }
        if (leftTop) {
            cornerX = x + cornerRadius;
            cornerY = y + cornerRadius;
            for (i = 180; i <= 270; i += 30) {
                GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
            }
        } else {
            GL11.glVertex2d((double)x, (double)y);
        }
        if (leftBottom) {
            cornerX = x + cornerRadius;
            cornerY = y + height - cornerRadius;
            for (i = 270; i <= 360; i += 30) {
                GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
            }
        } else {
            GL11.glVertex2d((double)x, (double)(y + height));
        }
        GL11.glEnd();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
        RenderUtil.setGLColor(Color.white);
    }

    public static void drawRoundedRect2Alpha(double x, double y, double width, double height, double cornerRadius, Color color) {
        int i;
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        RenderUtil.color(color);
        GL11.glBegin((int)9);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (i = 0; i <= 90; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 90; i <= 180; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        for (i = 180; i <= 270; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        for (i = 270; i <= 360; i += 30) {
            GL11.glVertex2d((double)(cornerX + Math.sin((double)((double)i * Math.PI / 180.0)) * cornerRadius), (double)(cornerY + Math.cos((double)((double)i * Math.PI / 180.0)) * cornerRadius));
        }
        GL11.glEnd();
        RenderUtil.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    public static void drawRoundRectTest(float x1, float y1, float x2, float y2, float radius, int steps, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom, Color color) {
        int i;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        RenderUtil.color(color);
        GL11.glBegin((int)9);
        radius = Math.min((float)Math.min((float)(Math.abs((float)(y2 - y1)) / 2.0f), (float)(Math.abs((float)(x2 - x1)) / 2.0f)), (float)radius);
        steps = Math.max((int)1, (int)(steps - steps % 3));
        if (rightBottom) {
            for (i = 0; i <= 90; i += steps) {
                GL11.glVertex2d((double)((double)(x2 - radius) + Math.sin((double)Math.toRadians((double)i)) * (double)radius), (double)((double)(y2 - radius) + Math.cos((double)Math.toRadians((double)i)) * (double)radius));
            }
        } else {
            GL11.glVertex2d((double)x2, (double)y2);
        }
        if (rightTop) {
            for (i = 90; i <= 180; i += steps) {
                GL11.glVertex2d((double)((double)(x2 - radius) + Math.sin((double)Math.toRadians((double)i)) * (double)radius), (double)((double)(y1 + radius) + Math.cos((double)Math.toRadians((double)i)) * (double)radius));
            }
        } else {
            GL11.glVertex2d((double)x2, (double)y1);
        }
        if (leftTop) {
            for (i = 180; i <= 270; i += steps) {
                GL11.glVertex2d((double)((double)(x1 + radius) + Math.sin((double)Math.toRadians((double)i)) * (double)radius), (double)((double)(y1 + radius) + Math.cos((double)Math.toRadians((double)i)) * (double)radius));
            }
        } else {
            GL11.glVertex2d((double)x1, (double)y1);
        }
        if (leftBottom) {
            for (i = 270; i <= 360; i += steps) {
                GL11.glVertex2d((double)((double)(x1 + radius) + Math.sin((double)Math.toRadians((double)i)) * (double)radius), (double)((double)(y2 - radius) + Math.cos((double)Math.toRadians((double)i)) * (double)radius));
            }
        } else {
            GL11.glVertex2d((double)x1, (double)y2);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        RenderUtil.color(Color.white);
        GL11.glPopMatrix();
    }

    public static void setupViewBobbing(float partialTicks) {
        if (mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)mc.getRenderViewEntity();
            float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
            GlStateManager.translate((float)(MathHelper.sin((float)(f1 * (float)Math.PI)) * f2 * 0.5f), (float)(-Math.abs((float)(MathHelper.cos((float)(f1 * (float)Math.PI)) * f2))), (float)0.0f);
            GlStateManager.rotate((float)(MathHelper.sin((float)(f1 * (float)Math.PI)) * f2 * 3.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.rotate((float)(Math.abs((float)(MathHelper.cos((float)(f1 * (float)Math.PI - 0.2f)) * f2)) * 5.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)f3, (float)1.0f, (float)0.0f, (float)0.0f);
        }
    }

    public static boolean isSliderHovered(float x1, float y1, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x1 && (float)mouseX <= x2 && (float)mouseY >= y1 && (float)mouseY <= y2;
    }

    public static Color getColorAlpha(int color, int alpha) {
        Color color2 = new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), alpha);
        return color2;
    }

    public static void drawCircle(double x, double y, double radius, int color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)3);
        for (int i = 0; i < 360; ++i) {
            RenderUtil.setGLColor(color);
            GL11.glVertex2d((double)x, (double)y);
            double sin = Math.sin((double)Math.toRadians((double)i)) * radius;
            double cos = Math.cos((double)Math.toRadians((double)i)) * radius;
            GL11.glVertex2d((double)(x + sin), (double)(y + cos));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)2929);
        GL11.glPopMatrix();
    }

    public static void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        double scale = sr.getScaleFactor();
        y = (double)sr.getScaledHeight() - y;
        GL11.glScissor((int)((int)(x *= scale)), (int)((int)((y *= scale) - (height *= scale))), (int)((int)(width *= scale)), (int)((int)height));
    }

    public static void drawRGBLineHorizontal(double x, double y, double width, float linewidth, float colors, boolean reverse) {
        GlStateManager.shadeModel((int)7425);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GL11.glLineWidth((float)linewidth);
        GL11.glBegin((int)3);
        colors = (float)((double)colors * width);
        double steps = width / (double)colors;
        double cX = x;
        double cX2 = x + steps;
        if (reverse) {
            for (float i = colors; i > 0.0f; i -= 1.0f) {
                int argbColor = HUD.rainbow((int)((int)(i * 10.0f)));
                float a = (float)(argbColor >> 24 & 0xFF) / 255.0f;
                float r = (float)(argbColor >> 16 & 0xFF) / 255.0f;
                float g = (float)(argbColor >> 8 & 0xFF) / 255.0f;
                float b = (float)(argbColor & 0xFF) / 255.0f;
                GlStateManager.color((float)r, (float)g, (float)b, (float)a);
                GL11.glVertex2d((double)cX, (double)y);
                GL11.glVertex2d((double)cX2, (double)y);
                cX = cX2;
                cX2 += steps;
            }
        } else {
            int i = 0;
            while ((float)i < colors) {
                int argbColor = HUD.rainbow((int)(i * 10));
                float a = (float)(argbColor >> 24 & 0xFF) / 255.0f;
                float r = (float)(argbColor >> 16 & 0xFF) / 255.0f;
                float g = (float)(argbColor >> 8 & 0xFF) / 255.0f;
                float b = (float)(argbColor & 0xFF) / 255.0f;
                GlStateManager.color((float)r, (float)g, (float)b, (float)a);
                GL11.glVertex2d((double)cX, (double)y);
                GL11.glVertex2d((double)cX2, (double)y);
                cX = cX2;
                cX2 += steps;
                ++i;
            }
        }
        GL11.glEnd();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
