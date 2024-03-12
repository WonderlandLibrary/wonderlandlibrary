// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.render;

import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.utils.animations.Animation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.client.shader.Framebuffer;
import dev.tenacity.utils.Utils;

public class RenderUtil implements Utils
{
    public static double ticks;
    public static long lastFrame;
    
    public static Framebuffer createFrameBuffer(final Framebuffer framebuffer) {
        return createFrameBuffer(framebuffer, false);
    }
    
    public static Framebuffer createFrameBuffer(final Framebuffer framebuffer, final boolean depth) {
        if (needsNewFramebuffer(framebuffer)) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(RenderUtil.mc.displayWidth, RenderUtil.mc.displayHeight, depth);
        }
        return framebuffer;
    }
    
    public static boolean needsNewFramebuffer(final Framebuffer framebuffer) {
        return framebuffer == null || framebuffer.framebufferWidth != RenderUtil.mc.displayWidth || framebuffer.framebufferHeight != RenderUtil.mc.displayHeight;
    }
    
    public static void drawTracerLine(final Entity entity, final float width, final Color color, final float alpha) {
        final float ticks = RenderUtil.mc.timer.renderPartialTicks;
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        RenderUtil.mc.entityRenderer.orientCamera(ticks);
        final double[] pos = ESPUtil.getInterpolatedPos(entity);
        GL11.glDisable(2929);
        GLUtil.setup2DRendering();
        final double yPos = pos[1] + entity.height / 2.0f;
        GL11.glEnable(2848);
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        color(color.getRGB(), alpha);
        GL11.glVertex3d(pos[0], yPos, pos[2]);
        GL11.glVertex3d(0.0, (double)RenderUtil.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GLUtil.end2DRendering();
        GL11.glPopMatrix();
    }
    
    public static void drawMicrosoftLogo(final float x, final float y, final float size, final float spacing, final float alpha) {
        final float rectSize = size / 2.0f - spacing;
        final int alphaVal = (int)(255.0f * alpha);
        Gui.drawRect2(x, y, rectSize, rectSize, new Color(244, 83, 38, alphaVal).getRGB());
        Gui.drawRect2(x + rectSize + spacing, y, rectSize, rectSize, new Color(130, 188, 6, alphaVal).getRGB());
        Gui.drawRect2(x, y + spacing + rectSize, rectSize, rectSize, new Color(5, 166, 241, alphaVal).getRGB());
        Gui.drawRect2(x + rectSize + spacing, y + spacing + rectSize, rectSize, rectSize, new Color(254, 186, 7, alphaVal).getRGB());
    }
    
    public static void drawMicrosoftLogo(final float x, final float y, final float size, final float spacing) {
        drawMicrosoftLogo(x, y, size, spacing, 1.0f);
    }
    
    public static void drawImage(final ResourceLocation resourceLocation, final float x, final float y, final float imgWidth, final float imgHeight) {
        GLUtil.startBlend();
        RenderUtil.mc.getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, imgWidth, imgHeight, imgWidth, imgHeight);
        GLUtil.endBlend();
    }
    
    public static void fixBlendIssues() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }
    
    public static void drawUnfilledCircle(final double x, final double y, final float radius, final float lineWidth, final int color) {
        GLUtil.setup2DRendering();
        color(color);
        GL11.glLineWidth(lineWidth);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141526 / 180.0) * radius, y + Math.cos(i * 3.141526 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GLUtil.end2DRendering();
    }
    
    public static void drawCircle(final Entity entity, final float partialTicks, final double rad, final int color, final float alpha) {
        RenderUtil.ticks += 0.004 * (System.currentTimeMillis() - RenderUtil.lastFrame);
        RenderUtil.lastFrame = System.currentTimeMillis();
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glShadeModel(7425);
        GlStateManager.disableCull();
        final double x = MathUtils.interpolate(entity.lastTickPosX, entity.posX, RenderUtil.mc.timer.renderPartialTicks) - RenderUtil.mc.getRenderManager().renderPosX;
        final double y = MathUtils.interpolate(entity.lastTickPosY, entity.posY, RenderUtil.mc.timer.renderPartialTicks) - RenderUtil.mc.getRenderManager().renderPosY + Math.sin(RenderUtil.ticks) + 1.0;
        final double z = MathUtils.interpolate(entity.lastTickPosZ, entity.posZ, RenderUtil.mc.timer.renderPartialTicks) - RenderUtil.mc.getRenderManager().renderPosZ;
        GL11.glBegin(5);
        for (float i = 0.0f; i < 6.283185307179586; i += (float)0.09817477042468103) {
            final double vecX = x + rad * Math.cos(i);
            final double vecZ = z + rad * Math.sin(i);
            color(color, 0.0f);
            GL11.glVertex3d(vecX, y - Math.sin(RenderUtil.ticks + 1.0) / 2.700000047683716, vecZ);
            color(color, 0.52f * alpha);
            GL11.glVertex3d(vecX, y, vecZ);
        }
        GL11.glEnd();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(3);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        color(color, 0.5f * alpha);
        for (int j = 0; j <= 180; ++j) {
            GL11.glVertex3d(x - Math.sin(j * MathHelper.PI2 / 90.0f) * rad, y, z + Math.cos(j * MathHelper.PI2 / 90.0f) * rad);
        }
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        resetColor();
        setAlphaLimit(0.0f);
        GLUtil.setup2DRendering();
        color(c);
        GL11.glBegin(6);
        for (int i = 0; i <= 360 / quality; ++i) {
            final double x2 = Math.sin(i * quality * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * quality * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
        GLUtil.end2DRendering();
    }
    
    public static void renderBoundingBox(final EntityLivingBase entityLivingBase, final Color color, final float alpha) {
        final AxisAlignedBB bb = ESPUtil.getInterpolatedBoundingBox(entityLivingBase);
        GlStateManager.pushMatrix();
        GLUtil.setup2DRendering();
        GLUtil.enableCaps(3042, 2832, 2881, 2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(3.0f);
        final float actualAlpha = 0.3f * alpha;
        GL11.glColor4f((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), actualAlpha);
        color(color.getRGB(), actualAlpha);
        RenderGlobal.renderCustomBoundingBox(bb, true, true);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GLUtil.disableCaps();
        GLUtil.end2DRendering();
        GlStateManager.popMatrix();
    }
    
    public static void circleNoSmoothRGB(final double x, final double y, double radius, final int color) {
        radius /= 2.0;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        color(color);
        GL11.glBegin(6);
        for (double i = 0.0; i <= 360.0; ++i) {
            final double angle = i * 6.283185307179586 / 360.0;
            GL11.glVertex2d(x + radius * Math.cos(angle) + radius, y + radius * Math.sin(angle) + radius);
        }
        GL11.glEnd();
        GL11.glEnable(2884);
        GL11.glEnable(3553);
    }
    
    public static void drawBorderedRect(final float x, final float y, final float width, final float height, final float outlineThickness, final int rectColor, final int outlineColor) {
        Gui.drawRect2(x, y, width, height, rectColor);
        GL11.glEnable(2848);
        color(outlineColor);
        GLUtil.setup2DRendering();
        GL11.glLineWidth(outlineThickness);
        final float cornerValue = (float)(outlineThickness * 0.19);
        GL11.glBegin(1);
        GL11.glVertex2d((double)x, (double)(y - cornerValue));
        GL11.glVertex2d((double)x, (double)(y + height + cornerValue));
        GL11.glVertex2d((double)(x + width), (double)(y + height + cornerValue));
        GL11.glVertex2d((double)(x + width), (double)(y - cornerValue));
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glEnd();
        GLUtil.end2DRendering();
        GL11.glDisable(2848);
    }
    
    public static void renderRoundedRect(final float x, final float y, final float width, final float height, final float radius, final int color) {
        drawGoodCircle(x + radius, y + radius, radius, color);
        drawGoodCircle(x + width - radius, y + radius, radius, color);
        drawGoodCircle(x + radius, y + height - radius, radius, color);
        drawGoodCircle(x + width - radius, y + height - radius, radius, color);
        Gui.drawRect2(x + radius, y, width - radius * 2.0f, height, color);
        Gui.drawRect2(x, y + radius, width, height - radius * 2.0f, color);
    }
    
    public static void scaleStart(final float x, final float y, final float scale) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glScalef(scale, scale, 1.0f);
        GL11.glTranslatef(-x, -y, 0.0f);
    }
    
    public static void scaleEnd() {
        GL11.glPopMatrix();
    }
    
    public static void drawGoodCircle(final double x, final double y, final float radius, final int color) {
        color(color);
        GLUtil.setup2DRendering();
        GL11.glEnable(2832);
        GL11.glHint(3153, 4354);
        GL11.glPointSize(radius * (2 * RenderUtil.mc.gameSettings.guiScale));
        GL11.glBegin(0);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
        GLUtil.end2DRendering();
    }
    
    public static void fakeCircleGlow(final float posX, final float posY, final float radius, final Color color, final float maxAlpha) {
        setAlphaLimit(0.0f);
        GL11.glShadeModel(7425);
        GLUtil.setup2DRendering();
        color(color.getRGB(), maxAlpha);
        GL11.glBegin(6);
        GL11.glVertex2d((double)posX, (double)posY);
        color(color.getRGB(), 0.0f);
        for (int i = 0; i <= 100; ++i) {
            final double angle = i * 0.06283 + 3.1415;
            final double x2 = Math.sin(angle) * radius;
            final double y2 = Math.cos(angle) * radius;
            GL11.glVertex2d(posX + x2, posY + y2);
        }
        GL11.glEnd();
        GLUtil.end2DRendering();
        GL11.glShadeModel(7424);
        setAlphaLimit(1.0f);
    }
    
    public static double animate(final double endPoint, final double current, double speed) {
        final boolean shouldContinueAnimation = endPoint > current;
        if (speed < 0.0) {
            speed = 0.0;
        }
        else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        final double factor = dif * speed;
        return current + (shouldContinueAnimation ? factor : (-factor));
    }
    
    public static void rotateStart(float x, float y, final float width, final float height, final float rotation) {
        GL11.glPushMatrix();
        x += width / 2.0f;
        y += height / 3.0f;
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-x, -y, 0.0f);
    }
    
    public static void rotateStartReal(final float x, final float y, final float width, final float height, final float rotation) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0f);
        GL11.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-x, -y, 0.0f);
    }
    
    public static void rotateEnd() {
        GL11.glPopMatrix();
    }
    
    public static void drawClickGuiArrow(final float x, final float y, final float size, final Animation animation, final int color) {
        GL11.glTranslatef(x, y, 0.0f);
        color(color);
        GLUtil.setup2DRendering();
        GL11.glBegin(5);
        final double interpolation = MathUtils.interpolate(0.0, size / 2.0, animation.getOutput().floatValue());
        if (animation.getOutput().floatValue() >= 0.48) {
            GL11.glVertex2d((double)(size / 2.0f), (double)MathUtils.interpolate(size / 2.0, 0.0, animation.getOutput().floatValue()));
        }
        GL11.glVertex2d(0.0, interpolation);
        if (animation.getOutput().floatValue() < 0.48) {
            GL11.glVertex2d((double)(size / 2.0f), (double)MathUtils.interpolate(size / 2.0, 0.0, animation.getOutput().floatValue()));
        }
        GL11.glVertex2d((double)size, interpolation);
        GL11.glEnd();
        GLUtil.end2DRendering();
        GL11.glTranslatef(-x, -y, 0.0f);
    }
    
    public static void drawCircleNotSmooth(final double x, final double y, double radius, final int color) {
        radius /= 2.0;
        GLUtil.setup2DRendering();
        GL11.glDisable(2884);
        color(color);
        GL11.glBegin(6);
        for (double i = 0.0; i <= 360.0; ++i) {
            final double angle = i * 0.01745;
            GL11.glVertex2d(x + radius * Math.cos(angle) + radius, y + radius * Math.sin(angle) + radius);
        }
        GL11.glEnd();
        GL11.glEnable(2884);
        GLUtil.end2DRendering();
    }
    
    public static void scissor(final double x, final double y, final double width, final double height, final Runnable data) {
        GL11.glEnable(3089);
        scissor(x, y, width, height);
        data.run();
        GL11.glDisable(3089);
    }
    
    public static void scissor(final double x, final double y, final double width, final double height) {
        final ScaledResolution sr = new ScaledResolution(RenderUtil.mc);
        final double scale = sr.getScaleFactor();
        final double finalHeight = height * scale;
        final double finalY = (sr.getScaledHeight() - y) * scale;
        final double finalX = x * scale;
        final double finalWidth = width * scale;
        GL11.glScissor((int)finalX, (int)(finalY - finalHeight), (int)finalWidth, (int)finalHeight);
    }
    
    public static void scissorStart(final double x, final double y, final double width, final double height) {
        GL11.glEnable(3089);
        final ScaledResolution sr = new ScaledResolution(RenderUtil.mc);
        final double scale = sr.getScaleFactor();
        final double finalHeight = height * scale;
        final double finalY = (sr.getScaledHeight() - y) * scale;
        final double finalX = x * scale;
        final double finalWidth = width * scale;
        GL11.glScissor((int)finalX, (int)(finalY - finalHeight), (int)finalWidth, (int)finalHeight);
    }
    
    public static void scissorEnd() {
        GL11.glDisable(3089);
    }
    
    public static void setAlphaLimit(final float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, (float)(limit * 0.01));
    }
    
    public static void color(final int color, final float alpha) {
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GlStateManager.color(r, g, b, alpha);
    }
    
    public static void color(final int color) {
        color(color, (color >> 24 & 0xFF) / 255.0f);
    }
    
    public static void bindTexture(final int texture) {
        GL11.glBindTexture(3553, texture);
    }
    
    public static void resetColor() {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static boolean isHovered(final float mouseX, final float mouseY, final float x, final float y, final float width, final float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
    
    public static void drawGradientRect(final double left, final double top, final double right, final double bottom, final int startColor, final int endColor) {
        GLUtil.setup2DRendering();
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        color(startColor);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        color(endColor);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glDisable(2848);
        GLUtil.end2DRendering();
        resetColor();
    }
    
    public static void drawGradientRectBordered(final double left, final double top, final double right, final double bottom, final double width, final int startColor, final int endColor, final int borderStartColor, final int borderEndColor) {
        drawGradientRect(left + width, top + width, right - width, bottom - width, startColor, endColor);
        drawGradientRect(left + width, top, right - width, top + width, borderStartColor, borderEndColor);
        drawGradientRect(left, top, left + width, bottom, borderStartColor, borderEndColor);
        drawGradientRect(right - width, top, right, bottom, borderStartColor, borderEndColor);
        drawGradientRect(left + width, bottom - width, right - width, bottom, borderStartColor, borderEndColor);
    }
    
    private static float drawExhiOutlined(final String text, final float x, final float y, final int borderColor, final int mainColor) {
        RenderUtil.tenacityBoldFont14.drawString(text, x, y - 0.35f, borderColor);
        RenderUtil.tenacityBoldFont14.drawString(text, x, y + 0.35f, borderColor);
        RenderUtil.tenacityBoldFont14.drawString(text, x - 0.35f, y, borderColor);
        RenderUtil.tenacityBoldFont14.drawString(text, x + 0.35f, y, borderColor);
        RenderUtil.tenacityBoldFont14.drawString(text, x, y, mainColor);
        return x + RenderUtil.tenacityBoldFont14.getStringWidth(text) - 2.0f;
    }
    
    public static void drawExhiEnchants(final ItemStack stack, final float x, float y) {
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        final int darkBorder = -16777216;
        if (stack.getItem() instanceof ItemArmor) {
            final int prot = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
            final int unb = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            final int thorn = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
            if (prot > 0) {
                drawExhiOutlined(prot + "", drawExhiOutlined("P", x, y, -16777216, -1) + 2.0f, y, getBorderColor(prot), getMainColor(prot));
                y += 5.0f;
            }
            if (unb > 0) {
                drawExhiOutlined(unb + "", drawExhiOutlined("U", x, y, -16777216, -1) + 2.0f, y, getBorderColor(unb), getMainColor(unb));
                y += 5.0f;
            }
            if (thorn > 0) {
                drawExhiOutlined(thorn + "", drawExhiOutlined("T", x, y, -16777216, -1) + 2.0f, y, getBorderColor(thorn), getMainColor(thorn));
                y += 5.0f;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            final int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
            final int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
            final int flame = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
            final int unb2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (power > 0) {
                drawExhiOutlined(power + "", drawExhiOutlined("Pow", x, y, -16777216, -1) + 2.0f, y, getBorderColor(power), getMainColor(power));
                y += 5.0f;
            }
            if (punch > 0) {
                drawExhiOutlined(punch + "", drawExhiOutlined("Pun", x, y, -16777216, -1) + 2.0f, y, getBorderColor(punch), getMainColor(punch));
                y += 5.0f;
            }
            if (flame > 0) {
                drawExhiOutlined(flame + "", drawExhiOutlined("F", x, y, -16777216, -1) + 2.0f, y, getBorderColor(flame), getMainColor(flame));
                y += 5.0f;
            }
            if (unb2 > 0) {
                drawExhiOutlined(unb2 + "", drawExhiOutlined("U", x, y, -16777216, -1) + 2.0f, y, getBorderColor(unb2), getMainColor(unb2));
                y += 5.0f;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            final int sharp = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
            final int kb = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
            final int fire = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
            final int unb2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
            if (sharp > 0) {
                drawExhiOutlined(sharp + "", drawExhiOutlined("S", x, y, -16777216, -1) + 2.0f, y, getBorderColor(sharp), getMainColor(sharp));
                y += 5.0f;
            }
            if (kb > 0) {
                drawExhiOutlined(kb + "", drawExhiOutlined("K", x, y, -16777216, -1) + 2.0f, y, getBorderColor(kb), getMainColor(kb));
                y += 5.0f;
            }
            if (fire > 0) {
                drawExhiOutlined(fire + "", drawExhiOutlined("F", x, y, -16777216, -1) + 2.0f, y, getBorderColor(fire), getMainColor(fire));
                y += 5.0f;
            }
            if (unb2 > 0) {
                drawExhiOutlined(unb2 + "", drawExhiOutlined("U", x, y, -16777216, -1) + 2.0f, y, getBorderColor(unb2), getMainColor(unb2));
            }
        }
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
    }
    
    private static int getMainColor(final int level) {
        if (level == 4) {
            return -5636096;
        }
        return -1;
    }
    
    private static int getBorderColor(final int level) {
        if (level == 2) {
            return 1884684117;
        }
        if (level == 3) {
            return 1879091882;
        }
        if (level == 4) {
            return 1890189312;
        }
        if (level >= 5) {
            return 1895803392;
        }
        return 1895825407;
    }
    
    static {
        RenderUtil.ticks = 0.0;
        RenderUtil.lastFrame = 0L;
    }
}
