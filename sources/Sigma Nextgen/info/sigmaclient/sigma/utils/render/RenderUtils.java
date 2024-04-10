package info.sigmaclient.sigma.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;

import static info.sigmaclient.sigma.sigma5.utils.SigmaRenderUtils.*;
import static info.sigmaclient.sigma.modules.Module.mc;
import static net.minecraft.client.gui.AbstractGui.drawModalRectWithCustomSizedTexture;
import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {
    public static class renderPos{
        public double renderPosX, renderPosY, renderPosZ;
        public float playerViewX, playerViewY;
        public boolean thirdPersonView;
    }
    public static renderPos getRenderPos(){
        ActiveRenderInfo activerenderinfo = Minecraft.getInstance().getRenderManager().info;
        Vector3d vector3d = activerenderinfo.getProjectedView();
        renderPos l = new renderPos();
        l.renderPosX = vector3d.x;
        l.renderPosY = vector3d.y;
        l.renderPosZ = vector3d.z;

        l.playerViewX = activerenderinfo.getPitch();
        l.playerViewY = activerenderinfo.getYaw();

        l.thirdPersonView = activerenderinfo.isThirdPerson();
        return l;
    }
    public static void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha)
    {
        drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
    }

    public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        drawBoundingBox(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
        tessellator.draw();
    }

    public static void drawBoundingBox(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha)
    {
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
    }
    public static void drawEntityServerESP2(Entity entity, float red, float green, float blue, float alpha, float lineAlpha, float lineWidth) {
        if (entity == null) return;
        double d0 = (double) entity.getServerPos().x / 1;
        double d1 = (double) entity.getServerPos().y / 1;
        double d2 = (double) entity.getServerPos().z / 1;
        float x = (float) (d0 - RenderUtils.getRenderPos().renderPosX);
        float y = (float) (d1 - RenderUtils.getRenderPos().renderPosY);
        float z = (float) (d2 - RenderUtils.getRenderPos().renderPosZ);

        final AxisAlignedBB entityBox = entity.getBoundingBox();
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entityBox.minX - 
                entity.getPosX() + x - 0.05D, entityBox.minY - 
                entity.getPosY() + y, entityBox.minZ - 
                entity.getPosZ() + z - 0.05D, entityBox.maxX - 
                entity.getPosX() + x + 0.05D, entityBox.maxY - 
                entity.getPosY() + y + 0.15D, entityBox.maxZ - 
                entity.getPosZ() + z + 0.05D);

        GlStateManager.resetColor();
        GL11.glLineWidth(3);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        drawSelectionBoundingBox(axisAlignedBB, 1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
//        GlStateManager.enableLighting();
        GlStateManager.resetColor();
    }

    public static void otherDrawBoundingBoxGL11(Entity entity, float x, float y, float z, double width, double height) {
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        float newYaw4;
        float newYaw3;
        float newYaw2;
        float newYaw1;
        width *= 1.5;
        float yaw1 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 45.0f;
        if (yaw1 < 0.0f) {
            newYaw1 = 0.0f;
            newYaw1 += 360.0f - Math.abs(yaw1);
        } else {
            newYaw1 = yaw1;
        }
        newYaw1 *= -1.0f;
        newYaw1 = (float) ((double) newYaw1 * (Math.PI / 180));
        float yaw2 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 135.0f;
        if (yaw2 < 0.0f) {
            newYaw2 = 0.0f;
            newYaw2 += 360.0f - Math.abs(yaw2);
        } else {
            newYaw2 = yaw2;
        }
        newYaw2 *= -1.0f;
        newYaw2 = (float) ((double) newYaw2 * (Math.PI / 180));
        float yaw3 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 225.0f;
        if (yaw3 < 0.0f) {
            newYaw3 = 0.0f;
            newYaw3 += 360.0f - Math.abs(yaw3);
        } else {
            newYaw3 = yaw3;
        }
        newYaw3 *= -1.0f;
        newYaw3 = (float) ((double) newYaw3 * (Math.PI / 180));
        float yaw4 = MathHelper.wrapAngleTo180_float(entity.getRotationYawHead()) + 315.0f;
        if (yaw4 < 0.0f) {
            newYaw4 = 0.0f;
            newYaw4 += 360.0f - Math.abs(yaw4);
        } else {
            newYaw4 = yaw4;
        }
        newYaw4 *= -1.0f;
        newYaw4 = (float) ((double) newYaw4 * (Math.PI / 180));
        double x1 = Math.sin(newYaw1) * width + (double) x;
        double z1 = Math.cos(newYaw1) * width + (double) z;
        double x2 = Math.sin(newYaw2) * width + (double) x;
        double z2 = Math.cos(newYaw2) * width + (double) z;
        double x3 = Math.sin(newYaw3) * width + (double) x;
        double z3 = Math.cos(newYaw3) * width + (double) z;
        double x4 = Math.sin(newYaw4) * width + (double) x;
        double z4 = Math.cos(newYaw4) * width + (double) z;
        double y2 = (double) y + height;
        GL11.glBegin(7);
        GL11.glVertex3d(x1, y, z1);
        GL11.glVertex3d(x1, y2, z1);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glVertex3d(x1, y2, z1);
        GL11.glVertex3d(x1, y, z1);
        GL11.glVertex3d(x1, y, z1);
        GL11.glVertex3d(x2, y, z2);
        GL11.glVertex3d(x3, y, z3);
        GL11.glVertex3d(x4, y, z4);
        GL11.glVertex3d(x1, y2, z1);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glVertex3d(x3, y2, z3);
        GL11.glVertex3d(x4, y2, z4);
        GL11.glEnd();
        GlStateManager.enableDepth();
//        GlStateManager.enableLighting();
    }

    public static void drawRoundedRectWithGlow(double paramXStart, double paramYStart, double paramXEnd, double paramYEnd, float radius, float glowradius, int color) {
        for (float i = 1; i > 0.1f; i /= 1.17f) {
            if (i / 1.17f <= 0.1f) {
                i = 0;
            }
            drawRoundedRect(paramXStart - glowradius * i, paramYStart - glowradius * i, paramXEnd + glowradius * i, paramYEnd + glowradius * i, radius, ColorUtils.reAlpha(color, (int) ((1f - i) * new Color(color).getAlpha()) / 4).getRGB(), true);
        }
    }

    public static void drawRoundedRect(double paramXStart, double paramYStart, double paramXEnd, double paramYEnd, float radius, int color) {
        drawRoundedRect(paramXStart, paramYStart, paramXEnd, paramYEnd, radius, color, true);
    }
    public static void drawRoundedRectRect(double paramXStart, double paramYStart, double width, double height, float radius, int color) {
        drawRoundedRect(paramXStart, paramYStart, paramXStart + width, paramYStart + height, radius, color, true);
    }
    public static int 霥瀳놣㠠釒(final int n, final float n2) {
        return (int)(n2 * 255.0f) << 24 | (n & 0xFFFFFF);
    }
    public static void sigma_drawShadow(float n, float n2, float n3, float n4, float n5, float n6) {
//        GL11.glAlphaFunc(519, 0.0f);
        Color 霥瀳놣㠠釒 = ColorUtils.reAlpha(new Color(-65794), n6);
        drawTextureLocation(n - n5, n2 - n5, n5, n5, "shadow/shadow_corner", 霥瀳놣㠠釒);
        drawTextureLocation(n + n3, n2 - n5, n5, n5, "shadow/shadow_corner_2", 霥瀳놣㠠釒);
        drawTextureLocation(n - n5, n2 + n4, n5, n5, "shadow/shadow_corner_3", 霥瀳놣㠠釒);
        drawTextureLocation(n + n3, n2 + n4, n5, n5, "shadow/shadow_corner_4", 霥瀳놣㠠釒);
        霥瀳놣㠠釒 = ColorUtils.reAlpha(new Color(-65794), n6 * 0.5f);
        drawTextureLocation(n - n5, n2, n5, n4, "shadow/shadow_left", 霥瀳놣㠠釒);
        drawTextureLocation(n + n3, n2, n5, n4, "shadow/shadow_right", 霥瀳놣㠠釒);
        drawTextureLocation(n, n2 - n5, n3, n5, "shadow/shadow_top", 霥瀳놣㠠釒);
        drawTextureLocation(n, n2 + n4, n3, n5, "shadow/shadow_bottom", 霥瀳놣㠠釒);
    }
    public static void drawOutinShadow(final double x, final double y, double x2, double y2, float alpha) {
        RenderUtils.drawCustomShader(x, y, x2, 2, alpha * 0.4f);
        RenderUtils.drawCustomShader(x, y, x2, 0, alpha * 0.4f);
        RenderUtils.drawOutInShader(x, y, x2, y2, true, alpha * 0.4f);
        RenderUtils.drawOutInShader(x, y, x2, y2, false, alpha * 0.4f);
    }

    public static void drawOutInShader(final double x, final double y, double x2, double y2, boolean right, float alpha) {
        x2 -= x;
        y2 -= y;
        if (right) {
            drawTexture(x2 + x - 9.0f, y, 9.0f, y2, "panelleft", alpha);
        } else {
            drawTexture(x, y, 9.0f, y2, "panelright", alpha);
        }
    }

    public static void drawCustomShader2(final double x, final double y, double x2, double stat, float alpha, float a) {
        x2 -= x;
        if (stat <= 1) drawTexture(x, y, x2, a, "panelbottom", alpha);
        else drawTexture(x, y - a, x2, a, "paneltop", alpha);
    }


    public static void drawCustomShader(final double x, final double y, double x2, double stat, float alpha) {
        x2 -= x;
        if (stat <= 1) drawTexture(x, y, x2, 9.0f, "panelbottom", alpha);
        else drawTexture(x, y - 9.0f, x2, 9.0f, "paneltop", alpha);
    }

    public static void drawShadow(final double x, final double y, double x2, double y2, float alpha) {
        x2 -= x;
        y2 -= y;
        drawTexture(x - 9.0f, y - 9.0f, 9.0f, 9.0f, "paneltopleft", alpha);
        drawTexture(x - 9.0f, y + y2, 9.0f, 9.0f, "panelbottomleft", alpha);
        drawTexture(x + x2, y + y2, 9.0f, 9.0f, "panelbottomright", alpha);
        drawTexture(x + x2, y - 9.0f, 9.0f, 9.0f, "paneltopright", alpha);
        drawTexture(x - 9.0f, y, 9.0f, y2, "panelleft", alpha);
        drawTexture(x + x2, y, 9.0f, y2, "panelright", alpha);
        drawTexture(x, y - 9.0f, x2, 9.0f, "paneltop", alpha);
        drawTexture(x, y + y2, x2, 9.0f, "panelbottom", alpha);
    }

    static ArrayList<Double> lastRenderCircleY = new ArrayList<>();
    static TimerUtil lastRender = new TimerUtil();

    public static void 牰蓳躚唟捉璧(final float n, final float n2, final float n3, final int n4) {
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        final float n5 = (n4 >> 24 & 0xFF) / 255.0f;
        final float n6 = (n4 >> 16 & 0xFF) / 255.0f;
        final float n7 = (n4 >> 8 & 0xFF) / 255.0f;
        final float n8 = (n4 & 0xFF) / 255.0f;
        startBlend();
        GlStateManager.color(n6, n7, n8, n5);
        GL11.glEnable(2832);
        GL11.glEnable(GL_BLEND);
        GlStateManager.disableTexture2D();
        GL11.glPointSize(n3 * SigmaNG.lineWidth);
        GL11.glBegin(0);
        GL11.glVertex2f(n, n2);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GL11.glDisable(2832);
    }
    public static void drawCircle(final Entity entity, final Color color, final boolean shade, double alpha) {
        AxisAlignedBB bb = entity.getBoundingBox();
        double rad = ((bb.maxX - bb.minX) + (bb.maxZ - bb.minZ)) * 0.5f;
        GL11.glPushMatrix();
        GlStateManager.disableLighting();
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glEnable(GL_LINE_SMOOTH);
        GL11.glEnable(GL_POINT_SMOOTH);
        GL11.glEnable(GL_BLEND);
        GL11.glDisable(GL_ALPHA_TEST);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        GL11.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL_GREATER, 0.0f);
        GL11.glDepthMask(false);
        GlStateManager.disableDepth();
        if (shade) {
            GL11.glShadeModel(GL_SMOOTH);
        }

        startBlend();
        GlStateManager.disableCull();
        GL11.glBegin(5);
        double camX = RenderUtils.getRenderPos().renderPosX;
        double camY = RenderUtils.getRenderPos().renderPosY;
        double camZ = RenderUtils.getRenderPos().renderPosZ;
        final double n = entity.lastTickPosX + (entity.getPosX() - entity.lastTickPosX) * mc.timer.renderPartialTicks;
        final double x = n - camX;
        double percent = Math.sin(System.currentTimeMillis() / 300.0) + 1; // 0 - 1000 -> 0 - 4
        final double n2 = entity.lastTickPosY + (entity.getPosY() - entity.lastTickPosY) * mc.timer.renderPartialTicks;
        final double y = n2 - camY + percent;
        final double n3 = entity.lastTickPosZ + (entity.getPosZ() - entity.lastTickPosZ) * mc.timer.renderPartialTicks;
        final double z = n3 - camZ;
        float alphas = 1f;
        for (int lastCount = 1; lastCount <= 10; lastCount++) {
            double percent2 = Math.sin(System.currentTimeMillis() / 300.0 - lastCount / 20f) + 1; // 0 - 1000 -> 0 - 4
            for (float i = 0.0f; i < 6.283185307179586; i += (float) 0.09817477042468103) {
                final double vecX = x + rad * Math.cos(i);
                final double vecZ = z + rad * Math.sin(i);
                GL11.glColor4d(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.03f * alpha * alphas);
                if (shade) {
                    GL11.glVertex3d(vecX, n2 + percent2 - camY, vecZ);
                }
                GL11.glVertex3d(vecX, y, vecZ);
            }
        }

        for (float i = 0.0f; i < 6.283185307179586; i += (float) 0.09817477042468103) {
            final double vecX = x + rad * Math.cos(i);
            final double vecZ = z + rad * Math.sin(i);
            GL11.glColor4d(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.7f * alpha);
            GL11.glVertex3d(vecX, y, vecZ);
            GL11.glVertex3d(vecX, y - 0.015F, vecZ);
        }
        GL11.glEnd();
        if (shade) {
            GL11.glShadeModel(GL_FLAT);
        }
//        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GL11.glDepthMask(true);
        GL11.glAlphaFunc(GL_GREATER, 0.1f);
        GlStateManager.enableCull();
        GL11.glDisable(GL_LINE_SMOOTH);
        GL11.glEnable(GL_POINT_SMOOTH);
        GL11.glEnable(GL_TEXTURE_2D);
        
        endBlend();
        GL11.glPopMatrix();
        GL11.glColor3f(1, 1, 1);
    }

    public static void 汌ꪕ蒕姮Ⱋ樽(final float n, final float n2, final float n3, final float n4, final int n51, final float n6, final float n7) {
        Color n5 = new Color(n51, true);
        final int n8 = 36 / 2;
        final int n9 = 10 / 2;
        final int n10 = n8 - n9;
        drawRect((float)(n + n9), (float)(n2 + n9), (float)(n + n3 - n9), (float)(n2 + n4 - n9), n5.getRGB());
        drawTextureLocation((float)(n - n10), (float)(n2 - n10), (float)n8, (float)n8, "jello/floating_corner", n5);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(n + n3 - n8 / 2), (float)(n2 + n8 / 2), 0.0f);
        GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-n - n3 - n8 / 2), (float)(-n2 - n8 / 2), 0.0f);
        drawTextureLocation((float)(n + n3 - n10), (float)(n2 - n10), (float)n8, (float)n8, "jello/floating_corner", n5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(n + n3 - n8 / 2), (float)(n2 + n4 + n8 / 2), 0.0f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-n - n3 - n8 / 2), (float)(-n2 - n4 - n8 / 2), 0.0f);
        drawTextureLocation((float)(n + n3 - n10), (float)(n2 + n9 + n4), (float)n8, (float)n8, "jello/floating_corner", n5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(n - n8 / 2), (float)(n2 + n4 + n8 / 2), 0.0f);
        GL11.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-n - n8 / 2), (float)(-n2 - n4 - n8 / 2), 0.0f);
        drawTextureLocation((float)(n + n9), (float)(n2 + n9 + n4), (float)n8, (float)n8, "jello/floating_corner", n5);
        GL11.glPopMatrix();
        퉧핇樽웨䈔属(n6 - n8, n7 + n9, n6 - n10 + n8, n7 - n9 + n4);
        for (int i = 0; i < n4; i += n8) {
            drawTextureLocation((float)(n - n10), (float)(n2 + n9 + i), (float)n8, (float)n8, "jello/floating_border", n5);
        }
        롤婯鷏붛浣弻();
        퉧핇樽웨䈔属(n6, n7 - n10, n6 + n3 - n9, n7 + n9);
        for (int j = 0; j < n3; j += n8) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(n + n8 / 2), (float)(n2 + n8 / 2), 0.0f);
            GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-n - n8 / 2), (float)(-n2 - n8 / 2), 0.0f);
            drawTextureLocation((float)(n - n10), (float)(n2 - n9 - j), (float)n8, (float)n8, "jello/floating_border", n5);
            GL11.glPopMatrix();
        }
        롤婯鷏붛浣弻();
        퉧핇樽웨䈔属(n6 + n3 - n9, n7 - n10, n + n3 + n10, n7 + n4 - n9);
        for (int k = 0; k < n4; k += n8) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(n + n8 / 2), (float)(n2 + n8 / 2), 0.0f);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-n - n8 / 2), (float)(-n2 - n8 / 2), 0.0f);
            drawTextureLocation((float)(n - n3 + n9), (float)(n2 - n9 - k), (float)n8, (float)n8, "jello/floating_border", n5);
            GL11.glPopMatrix();
        }
        롤婯鷏붛浣弻();
        퉧핇樽웨䈔属(n6 - n9, n7 - n10 + n4 - n8, n6 + n3 - n9, n7 + n4 + n9 * 2);
        for (int l = 0; l < n3; l += n8) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(n + n8 / 2), (float)(n2 + n8 / 2), 0.0f);
            GL11.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-n - n8 / 2), (float)(-n2 - n8 / 2), 0.0f);
            drawTextureLocation((float)(n - n4 + n9), (float)(n2 + n9 + l), (float)n8, (float)n8, "jello/floating_border", n5);
            GL11.glPopMatrix();
        }
        롤婯鷏붛浣弻();
    }
    public static void drawRoundShadow(final float n, final float n2, final float n3, final float n4, final int n71) {
        final int n6 = 36 / 2;
        final int n7 = 10 / 2;
        final int n8 = n6 - n7;
        Color n5 = new Color(n71, true);
        float a = n5.getAlpha() / 255f;
        㠠Ꮤ曞佉䩜鱀((float)(n + n7), (float)(n2 + n7), (float)(n + n3 - n7), (float)(n2 + n4 - n7), n71);
        drawTextureLocation((float)(n - n8), (float)(n2 - n8), (float)n6, (float)n6, "jello/floating_corner", n5);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(n + n3 - n6 / 2), (float)(n2 + n6 / 2), 0.0f);
        GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-n - n3 - n6 / 2), (float)(-n2 - n6 / 2), 0.0f);
        drawTextureLocation((float)(n + n3 - n8), (float)(n2 - n8), (float)n6, (float)n6, "jello/floating_corner", n5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(n + n3 - n6 / 2), (float)(n2 + n4 + n6 / 2), 0.0f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-n - n3 - n6 / 2), (float)(-n2 - n4 - n6 / 2), 0.0f);
        drawTextureLocation((float)(n + n3 - n8), (float)(n2 + n7 + n4), (float)n6, (float)n6, "jello/floating_corner", n5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(n - n6 / 2), (float)(n2 + n4 + n6 / 2), 0.0f);
        GL11.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-n - n6 / 2), (float)(-n2 - n4 - n6 / 2), 0.0f);
        drawTextureLocation((float)(n + n7), (float)(n2 + n7 + n4), (float)n6, (float)n6, "jello/floating_corner", n5);
        GL11.glPopMatrix();
        퉧핇樽웨䈔属(n - n6, n2 + n7, n - n8 + n6, n2 - n7 + n4, true, a);
        for (int i = 0; i < n4; i += n6) {
            drawTextureLocation((float)(n - n8), n2 + n7 + i - 0.2f, (float)n6, n6 + 0.2f, "jello/floating_border", n5);
        }
        롤婯鷏붛浣弻();
        퉧핇樽웨䈔属(n, n2 - n8, n + n3 - n7, n2 + n7, true, a);
        for (int j = 0; j < n3; j += n6) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(n + n6 / 2), (float)(n2 + n6 / 2), 0.0f);
            GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-n - n6 / 2), (float)(-n2 - n6 / 2), 0.0f);
            drawTextureLocation((float)(n - n8), n2 - n7 - j - 0.2f, (float)n6, n6 + 0.2f, "jello/floating_border", n5);
            GL11.glPopMatrix();
        }
        롤婯鷏붛浣弻();
        퉧핇樽웨䈔属(n + n3 - n7, n2 - n8, n + n3 + n8, n2 + n4 - n7, true, a);
        for (int k = 0; k < n4; k += n6) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(n + n6 / 2), (float)(n2 + n6 / 2), 0.0f);
            GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-n - n6 / 2), (float)(-n2 - n6 / 2), 0.0f);
            drawTextureLocation((float)(n - n3 + n7), n2 - n7 - k - 0.2f, (float)n6, n6 + 0.2f, "jello/floating_border", n5);
            GL11.glPopMatrix();
        }
        롤婯鷏붛浣弻();
        퉧핇樽웨䈔属(n - n7, n2 - n8 + n4 - n6, n + n3 - n7, n2 + n4 + n7 * 2, true, a);
        for (int l = 0; l < n3; l += n6) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(n + n6 / 2), (float)(n2 + n6 / 2), 0.0f);
            GL11.glRotatef(270.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-n - n6 / 2), (float)(-n2 - n6 / 2), 0.0f);
            drawTextureLocation((float)(n - n4 + n7), n2 + n7 + l - 0.2f, (float)n6, n6 + 0.2f, "jello/floating_border", n5);
            GL11.glPopMatrix();
        }
        롤婯鷏붛浣弻();
    }
    public static void drawCircle(final BlockPos entity, final Color color) {
        GlStateManager.disableDepth();
        AxisAlignedBB bb = new AxisAlignedBB(entity.getX(), entity.getY(), entity.getZ(), entity.getX() + 1, entity.getY() + 1, entity.getZ() + 1);
        double rad = ((bb.maxX - bb.minX) + (bb.maxZ - bb.minZ)) * 0.5f;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDepthMask(false);
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GL11.glBegin(5);
        double camX = RenderUtils.getRenderPos().renderPosX;
        double camY = RenderUtils.getRenderPos().renderPosY;
        double camZ = RenderUtils.getRenderPos().renderPosZ;
        final double n = entity.getX();
        final double x = n - camX;
        final double n2 = entity.getY();
        final double y = n2 - camY;
        final double n3 = entity.getZ();
        final double z = n3 - camZ;
        GlStateManager.resetColor();
        RenderUtils.color(color.getRGB());
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x + 1, y, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + 1, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z + 1);
        GL11.glVertex3d(x, y + 1, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z + 1);
        GL11.glVertex3d(x + 1, y, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z + 1);
        GL11.glVertex3d(x + 1, y + 1, z);
        GL11.glEnd();

        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glAlphaFunc(516, 0.1f);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor3f(255.0f, 255.0f, 255.0f);
        GlStateManager.enableDepth();
//        GlStateManager.enableLighting();
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {

    }

    public static void drawTexture(final double x, final double y, final double width, final double height, final String image, float alpha) {
        GL11.glPushMatrix();

        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();
        startBlend();
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigma/" + image + ".png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, width, height, width, height);
        endBlend();
        GL11.glPopMatrix();
    }

    public static void drawTextureCustom(final double x, final double y, final double width, final double height, float alpha) {
        GL11.glPushMatrix();

        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();
        startBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, width, height, width, height);
        GL11.glPopMatrix();
    }

    public static void drawTextureLocation(final double x, final double y, final double width, final double height, final String image, Color c) {

        final float n8 = (c.getRGB() >> 24 & 0xFF) / 255.0f;
        final float n9 = (c.getRGB() >> 16 & 0xFF) / 255.0f;
        final float n10 = (c.getRGB() >> 8 & 0xFF) / 255.0f;
        final float n11 = (c.getRGB() & 0xFF) / 255.0f;
        startBlend();

        GL11.glPushMatrix();
        GlStateManager.enableTexture2D();
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigmang/images/" + image + ".png"));

        GlStateManager.resetColor();
        com.mojang.blaze3d.platform.GlStateManager.color4f(0,0,0,0);
        com.mojang.blaze3d.platform.GlStateManager.color4f(n9, n10, n11, n8);
        drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, width, height, width, height);
        endBlend();
        GL11.glPopMatrix();
    }
    public static void drawTextureLocationZoom(final double x, final double y, final double width, final double height, final String image, Color c) {
        GL11.glPushMatrix();
        startBlend();
        GlStateManager.enableTexture2D();
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigmang/images/" + (image.contains(".") ? image : (image + ".png"))));
        GlStateManager.resetColor();
        GlStateManager.color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, width, height, width, height);
        endBlend();
        GL11.glPopMatrix();
    }
    public static void drawTextureLocationZoom(final double x, final double y, final double width, final double height, final String image, int c2) {
        GL11.glPushMatrix();
        Color c = new Color(c2);
        startBlend();
        GlStateManager.enableTexture2D();
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("sigmang/images/" + image + ".png"));
        GlStateManager.resetColor();
        GlStateManager.color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, c.getAlpha() / 255.0f);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, width, height, width, height);
        endBlend();
        GL11.glPopMatrix();
    }

    public static void drawTexture(float x, float y, float u, float v, float width, float height, float uw, float uh) {
        GlStateManager.resetColor();
        startBlend();
        drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, uw, uh);
        endBlend();
    }

    public static void drawRoundedRect(double paramXStart, double paramYStart, double paramXEnd, double paramYEnd, float radius, int color, boolean popPush, boolean a, boolean b, boolean c, boolean d) {
        startBlend();
        if (popPush) glPushMatrix();
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        double z = 0;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }

        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }

        double x1 = (paramXStart + radius);
        double y1 = (paramYStart + radius);
        double x2 = (paramXEnd - radius);
        double y2 = (paramYEnd - radius);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(1);

        GL11.glColor4f(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        double degree = Math.PI / 180.0;
        if (a) {
            for (double i = 0; i <= 90; i += 45)
                GL11.glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        } else GL11.glVertex2d(x2, y2);
        if (b) {
            for (double i = 90; i <= 180; i += 45)
                GL11.glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        } else GL11.glVertex2d(x2, y1);
        if (c) {
            for (double i = 180; i <= 270; i += 45)
                GL11.glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        } else GL11.glVertex2d(x1, y1);
        if (d) {
            for (double i = 270; i <= 360; i += 45)
                GL11.glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        } else GL11.glVertex2d(x1, y2);

        GL11.glEnd();

        GL11.glColor4f(1F, 1F, 1F, 1F);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        if (popPush) glPopMatrix();
        endBlend();
    }

    public static void drawRoundedRect(double paramXStart, double paramYStart, double paramXEnd, double paramYEnd, float radius, int color, boolean popPush) {
        startBlend();
        GlStateManager.resetColor();
        if (popPush) glPushMatrix();
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        double z = 0;
        if (paramXStart > paramXEnd) {
            z = paramXStart;
            paramXStart = paramXEnd;
            paramXEnd = z;
        }

        if (paramYStart > paramYEnd) {
            z = paramYStart;
            paramYStart = paramYEnd;
            paramYEnd = z;
        }

        double x1 = (paramXStart + radius);
        double y1 = (paramYStart + radius);
        double x2 = (paramXEnd - radius);
        double y2 = (paramYEnd - radius);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(1);

        GL11.glColor4f(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        double degree = Math.PI / 180.0;
        for (double i = 0; i <= 90; i += 45)
            GL11.glVertex2d(x2 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);
        for (double i = 90; i <= 180; i += 45)
            GL11.glVertex2d(x2 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        for (double i = 180; i <= 270; i += 45)
            GL11.glVertex2d(x1 + Math.sin(i * degree) * radius, y1 + Math.cos(i * degree) * radius);
        for (double i = 270; i <= 360; i += 45)
            GL11.glVertex2d(x1 + Math.sin(i * degree) * radius, y2 + Math.cos(i * degree) * radius);

        GL11.glEnd();

        GL11.glColor4f(1F, 1F, 1F, 1F);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        if (popPush) glPopMatrix();
        endBlend();
    }

    // This will set the alpha limit to a specified value ranging from 0-1
    public static void setAlphaLimit(float limit) {
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * 0.01));
    }

    // This method colors the next avalible texture with a specified alpha value ranging from 0-1
    public static void color(int color, float alpha) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GlStateManager.color(r, g, b, alpha);
    }

    // Colors the next texture without a specified alpha value
    public static void color(int color) {
        color(color, (float) (color >> 24 & 255) / 255.0F);
    }

    // Sometimes colors get messed up in for loops, so we use this method to reset it to allow new colors to be used
    public static void resetColor() {
        GlStateManager.color(1, 1, 1, 1);
    }

    //From rise, alan gave me this
    public static void drawFilledCircleNoGL(double x, double y, double r, int c, int quality) {
//        牰蓳躚唟捉璧((float) x, (float) y, (float) r,c);
        GlStateManager.resetColor();
        setAlphaLimit(0);
        setup2DRendering(true);
        startBlend();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        color(c);
        GL11.glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i <= 360 / quality; i++) {
            final double x2 = Math.sin(((i * quality * Math.PI) / 180)) * r;
            final double y2 = Math.cos(((i * quality * Math.PI) / 180)) * r;
            glVertex2d(x + x2, y + y2);
        }

        GL11.glEnd();
        end2DRendering();
    }


    public static void preGlHints() {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        startBlend();
    }

    public static void postGlHints() {
        preGlHints();
    }
    public static void startBlend() {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void setup2DRendering(boolean blend) {
        if (blend) {
            startBlend();
        }
        GlStateManager.disableTexture2D();
    }

    public static void endBlend() {
        preGlHints();
//        com.mojang.blaze3d.platform.GlStateManager.disableBlendNoLock();
//        com.mojang.blaze3d.platform.GlStateManager.enableAlphaNoLock();
    }

    public static void setup2DRendering() {
        setup2DRendering(true);
    }

    public static void end2DRendering() {
        GlStateManager.enableTexture2D();
        endBlend();
    }


    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        return createFrameBuffer(framebuffer, false);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer, boolean depth) {
        if (needsNewFramebuffer(framebuffer) && mc.getMainWindow().getWidth() != 0 && mc.getMainWindow().getHeight() != 0) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight(), depth);
        }
        return framebuffer;
    }

    public static boolean needsNewFramebuffer(Framebuffer framebuffer) {
        return framebuffer == null
                || framebuffer.framebufferWidth != mc.getMainWindow().getWidth() || framebuffer.framebufferHeight != mc.getMainWindow().getHeight()
                ;
    }

    public static void bindTexture(int texture) {
        com.mojang.blaze3d.platform.GlStateManager.bindTexture(texture);
    }

    private static void drawRectAct(double left, double top, double right, double bottom, int color)
    {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderSystem.disableAlphaTest();
//        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
//        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();
    }
    public static void drawRect(double left, double top, double right, double bottom, int color) {
        GlStateManager.resetColor();
        startBlend();
        drawRectAct(left, top, right, bottom, color);
        endBlend();
    }

}
