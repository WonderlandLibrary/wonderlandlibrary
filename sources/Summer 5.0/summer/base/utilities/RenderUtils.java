package summer.base.utilities;

import java.awt.Color;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.cheat.cheats.combat.KillAura;
import summer.base.font.TTFFontRenderer;

public class RenderUtils {

    static Minecraft mc = Minecraft.getMinecraft();
    static EntityLivingBase ent;
    private static Frustum frustrum;
    public static float delta;

    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1,
                                        final int col1, final int col2) {
        drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static int getColorFromPercentage(float current, float max) {
        float percentage = (current / max) / 3;
        return Color.HSBtoRGB(percentage, 1.0F, 1.0F);
    }

    // Alerithe code do not question
    // - From Alerithe :)
    public static void drawRectSized(float x, float y, float width, float height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    // Linear interpolation, https://en.wikipedia.org/wiki/Linear_interpolation
    // - Also from Alerithe
    public static double lerp(double v0, double v1, double t) {
        return (1.0 - t) * v0 + t * v1;
    }

    public static double interpolate(double old, double now, float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public static float interpolate(float old, float now, float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public static void prepareScissorBox(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scale = new ScaledResolution(RenderUtils.mc);
        final int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor),
                (int) ((y2 - y) * factor));
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (color >> 24 & 0xFF) / 255.0F;
        float f = (color >> 16 & 0xFF) / 255.0F;
        float f1 = (color >> 8 & 0xFF) / 255.0F;
        float f2 = (color & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    // i think this is from sigma but idk

    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);

        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;

        GL11.glDisable(3553);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glEnable(2848);

        GL11.glBegin(9);

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D,
                    y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D,
                    y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius,
                    y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius,
                    y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        }

        GL11.glEnd();

        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);

        GL11.glScaled(2.0D, 2.0D, 2.0D);

        GL11.glPopAttrib();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static void blockESPBox(BlockPos blockPos, float red, float green, float blue) {
        double x = blockPos.getX() - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y = blockPos.getY() - Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z = blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glColor4d(0, 1, 0, 0.15F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        // drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glColor4d(red, green, blue, 1000F);
        RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void renderEntity(final Entity e, int color, final int type) {
        if (e == null) {
            return;
        }
        final double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * Minecraft.getMinecraft().timer.renderPartialTicks
                - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        final double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * Minecraft.getMinecraft().timer.renderPartialTicks
                - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        final double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * Minecraft.getMinecraft().timer.renderPartialTicks
                - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        if (e instanceof EntityPlayer && ((EntityPlayer) e).hurtTime != 0) {
            color = Color.RED.getRGB();
        }
        if (type == 1) {
            GlStateManager.pushMatrix();
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(2.0f);
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            GL11.glColor4f(r, g, b, a);
            RenderGlobal.func_181561_a(new AxisAlignedBB(
                    e.getEntityBoundingBox().minX - 0.05 - e.posX
                            + (e.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
                    e.getEntityBoundingBox().minY - e.posY
                            + (e.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
                    e.getEntityBoundingBox().minZ - 0.05 - e.posZ
                            + (e.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ),
                    e.getEntityBoundingBox().maxX + 0.05 - e.posX
                            + (e.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
                    e.getEntityBoundingBox().maxY + 0.1 - e.posY
                            + (e.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
                    e.getEntityBoundingBox().maxZ + 0.05 - e.posZ
                            + (e.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)));
            dbb(new AxisAlignedBB(
                            e.getEntityBoundingBox().minX - 0.05 - e.posX
                                    + (e.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
                            e.getEntityBoundingBox().minY - e.posY
                                    + (e.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
                            e.getEntityBoundingBox().minZ - 0.05 - e.posZ
                                    + (e.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ),
                            e.getEntityBoundingBox().maxX + 0.05 - e.posX
                                    + (e.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
                            e.getEntityBoundingBox().maxY + 0.1 - e.posY
                                    + (e.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
                            e.getEntityBoundingBox().maxZ + 0.05 - e.posZ
                                    + (e.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)),
                    r, g, b);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GlStateManager.popMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        } else if (type == 2) {
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.0f);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            GL11.glColor4d((double) r, (double) g, (double) b, (double) a);
            RenderGlobal.func_181561_a(new AxisAlignedBB(
                    e.getEntityBoundingBox().minX - 0.88 - e.posX
                            + (e.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
                    e.getEntityBoundingBox().minY - e.posY
                            + (e.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
                    e.getEntityBoundingBox().minZ - 0.88 - e.posZ
                            + (e.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ),
                    e.getEntityBoundingBox().maxX + 0.88 - e.posX
                            + (e.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX),
                    e.getEntityBoundingBox().maxY + 0.88 - e.posY
                            + (e.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY),
                    e.getEntityBoundingBox().maxZ + 0.88 - e.posZ
                            + (e.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)));
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        } else if (type == 3) {
            final KillAura ka = CheatManager.getInstance(KillAura.class);
            TTFFontRenderer fr = Summer.INSTANCE.fontManager.getFont("EVO 11");

            int textColor = getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255),
                    (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();

            GL11.glPushMatrix();
            GL11.glTranslated(x, y - 0.2, z);
            GL11.glScalef(0.03f, 0.03f, 0.03f);
            GL11.glRotated((double) (-Minecraft.getMinecraft().getRenderManager().playerViewY), 0.0, 1.0, 0.0);
            GlStateManager.disableDepth();
            Gui.drawRect(-21.0, 0.0, -20.0, 75.0, color);
            Gui.drawRect(21.0, 0.0, 20.0, 76.0, color);

            Gui.drawRect(-21.0, 0.0, 20.0, 1.0, color);
            Gui.drawRect(-21.0, 76.0, 20.0, 75.0, color);
            GlStateManager.enableDepth();
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(x, y - 0.2, z);
            GL11.glScalef(0.03f, 0.03f, 0.03f);
            GL11.glRotated((double) (-Minecraft.getMinecraft().getRenderManager().playerViewY), 0.0, 1.0, 0.0);
            GlStateManager.disableDepth();
            /*
             * Gui.drawRect(4.0, 9.0, 5.2, 32.0, -1); //left leg Gui.drawRect(-4.0, 9.0,
             * -5.2, 32.0, -1); //right leg Gui.drawRect(1.0, 58.0, -0.3, 62.0, -1); //head
             * Gui.drawRect(1.0, 58.0, -0.3, 32.0, -1); //body Gui.drawRect(10.0, 35.0,
             * 11.0, 52.0, -1); //left arm Gui.drawRect(-10.0, 35.0, -11.0, 52.0, -1);
             * //right arm Gui.drawRect(-11.0, 52.0, 11.0, 53.0, -1); // sholders
             * Gui.drawRect(-5.9, 32.0, 5.1, 31.0, -1); // sholders
             */
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        } else if (type == 4) {
            TTFFontRenderer fr = Summer.INSTANCE.fontManager.getFont("EVO 11");
            int textColor = getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255),
                    (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
            GL11.glPushMatrix();
            GL11.glTranslated(x, y - 0.2, z);
            GL11.glScalef(0.03f, 0.03f, 0.03f);
            GL11.glRotated((double) (-Minecraft.getMinecraft().getRenderManager().playerViewY), 0.0, 1.0, 0.0);
            GlStateManager.disableDepth();
            Gui.drawRect(23.0, 0.0, 24.0, 71.0, textColor);
            Gui.drawRect(-21.0, 0.0, -20.0, 75.0, color);

            GlStateManager.enableDepth();
            GL11.glPopMatrix();
        }
    }

    public static void dbb(final AxisAlignedBB abb, final float r, final float g, final float b) {
        final float a = 0.25f;
        final Tessellator ts = Tessellator.getInstance();
        final WorldRenderer vb = ts.getWorldRenderer();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, 0.25f).endVertex();
        ts.draw();
    }

    public static void rectangle(double left, double top, double right, double bottom, int color) {
        double var5;
        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor,
                                         int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;

        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtils.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static ScaledResolution getResolution() {
        return new ScaledResolution(mc);
    }

    public static void prepareScissorBox(ScaledResolution sr, float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;
        int factor = sr.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((sr.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor),
                (int) ((y2 - y) * factor));
    }

    public static void drawArrow(float x, float y, int hexColor) {
        GL11.glPushMatrix();
        GL11.glScaled(1.3, 1.3, 1.3);

        x /= 1.3;
        y /= 1.3;
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        hexColor(hexColor);
        GL11.glLineWidth(2);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + 3, y + 4);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x + 3, y + 4);
        GL11.glVertex2d(x + 6, y);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }

    public static void hexColor(int hexColor) {
        float red = (hexColor >> 16 & 0xFF) / 255.0F;
        float green = (hexColor >> 8 & 0xFF) / 255.0F;
        float blue = (hexColor & 0xFF) / 255.0F;
        float alpha = (hexColor >> 24 & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void renderOne() {
        RenderUtils.checkSetupFBO();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(3);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glClearStencil(0xF);
        GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void checkSetupFBO() {
        final Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {
            setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }

    public static void setupFBO(final Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth,
                Minecraft.getMinecraft().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
    }

    public static void renderTwo() {
        GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }

    public static void renderThree() {
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void renderFour(EntityLivingBase base) {
        setColor(getTeamColor(base));
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glPolygonOffset(1.0F, -2000000F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void renderFour(int color) {
        setColor(color);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glPolygonOffset(1.0F, -2000000F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static int getTeamColor(Entity player) {
        int var2 = 16777215;

        if (player instanceof EntityPlayer) {
            ScorePlayerTeam var6 = (ScorePlayerTeam) ((EntityPlayer) player).getTeam();

            if (var6 != null) {
                String var7 = FontRenderer.getFormatFromString(var6.getColorPrefix());

                if (var7.length() >= 2) {
                    var2 = Minecraft.fontRendererObj.getColorCode(var7.charAt(1));
                }
            }
        }

        return var2;
    }

    public static void renderFive() {
        GL11.glPolygonOffset(1.0F, 2000000F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopAttrib();
    }

    public static void setColor(int colorHex) {
        float alpha = (float) (colorHex >> 24 & 255) / 255.0F;
        float red = (float) (colorHex >> 16 & 255) / 255.0F;
        float green = (float) (colorHex >> 8 & 255) / 255.0F;
        float blue = (float) (colorHex & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
    }

    public static void drawImage(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        float wScale = 1.0F / textureWidth;
        float hScale = 1.0F / textureHeight;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * wScale, (v + height) * hScale).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + width) * wScale, ((v + height) * hScale)).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + width) * wScale, v * hScale).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * wScale, v * hScale).endVertex();
        tessellator.draw();
    }

    public static double getAnimationState(double n, final double n2, final double n3) {
        final float n4 = (float) (RenderUtils.delta * n3);
        if (n < n2) {
            if (n + n4 < n2) {
                n += n4;
            } else {
                n = n2;
            }
        } else if (n - n4 > n2) {
            n -= n4;
        } else {
            n = n2;
        }
        return n;
    }

    public static void drawArc(float n, float n2, double n3, final int n4, final int n5, final double n6, final int n7) {
        n3 *= 2.0;
        n *= 2.0f;
        n2 *= 2.0f;
        final float n8 = (n4 >> 24 & 0xFF) / 255.0f;
        final float n9 = (n4 >> 16 & 0xFF) / 255.0f;
        final float n10 = (n4 >> 8 & 0xFF) / 255.0f;
        final float n11 = (n4 & 0xFF) / 255.0f;
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glLineWidth((float) n7);
        GL11.glEnable(2848);
        GL11.glColor4f(n9, n10, n11, n8);
        GL11.glBegin(3);
        int n12 = n5;
        while (n12 <= n6) {
            GL11.glVertex2d(n + Math.sin(n12 * 3.141592653589793 / 180.0) * n3, n2 + Math.cos(n12 * 3.141592653589793 / 180.0) * n3);
            ++n12;
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

}
