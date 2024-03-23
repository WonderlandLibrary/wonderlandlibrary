package xyz.northclient.features.modules;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.Render2DEvent;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.theme.ColorUtil;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@ModuleInfo(name = "2D ESP",category = Category.RENDER,description = "nig")
public class ESP2D extends AbstractModule {

    public BoolValue chest = new BoolValue("Chest",this)
            .setDefault(false);

    private static Vector3f project(final int factor, final double x, final double y, final double z) {
        if (GLU.gluProject((float) x, (float) y, (float) z, ActiveRenderInfo.MODELVIEW, ActiveRenderInfo.PROJECTION, ActiveRenderInfo.VIEWPORT, ActiveRenderInfo.OBJECTCOORDS)) {
            return new Vector3f((ActiveRenderInfo.OBJECTCOORDS.get(0) / factor), ((Display.getHeight() - ActiveRenderInfo.OBJECTCOORDS.get(1)) / factor), ActiveRenderInfo.OBJECTCOORDS.get(2));
        }

        return null;
    }

    public static Vector4f calc(Entity entity, float partialTicks, double renderX, double renderY, double renderZ, int factor) {
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - renderX;
        final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) - renderY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - renderZ;
        final double width = (entity.width + 0.2) / 2;
        final double height = entity.height + (entity.isSneaking() ? -0.3f : 0.2D) + 0.05;
        final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
        final List<Vector3f> vectors = Arrays.asList(new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ), new Vector3f((float) aabb.minX, (float) aabb.maxY, (float) aabb.minZ), new Vector3f((float) aabb.maxX, (float) aabb.minY, (float) aabb.minZ), new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.minZ), new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.maxZ), new Vector3f((float) aabb.minX, (float) aabb.maxY, (float) aabb.maxZ), new Vector3f((float) aabb.maxX, (float) aabb.minY, (float) aabb.maxZ), new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ));

        Vector4f position = null;
        for (Vector3f vector : vectors) {
            vector = project(factor, vector.x, vector.y, vector.z);

            if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                if (position == null) {
                    position = new Vector4f(vector.x, vector.y, vector.z, 0.0f);
                }


                position = new Vector4f(Math.min(vector.x, position.x), Math.min(vector.y, position.y), Math.max(vector.x, position.z), Math.max(vector.y, position.w));
            }
        }

        if (position == null) {
            position = new Vector4f(0, 0, 0, 0);
        }



        return position;
    }

    public static Vector4f calc(BlockPos pos, float partialTicks, double renderX, double renderY, double renderZ, int factor) {
        final double x = pos.getX() + (0) * partialTicks - renderX;
        final double y = (pos.getY() + (0) * partialTicks) - renderY;
        final double z = pos.getZ()+0.5 + (0) * partialTicks - renderZ;
        final double width = (0.9 + 0.2) / 2;
        final double height = 0.9 + 0.05;
        final AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
        final List<Vector3f> vectors = Arrays.asList(new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.minZ), new Vector3f((float) aabb.minX, (float) aabb.maxY, (float) aabb.minZ), new Vector3f((float) aabb.maxX, (float) aabb.minY, (float) aabb.minZ), new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.minZ), new Vector3f((float) aabb.minX, (float) aabb.minY, (float) aabb.maxZ), new Vector3f((float) aabb.minX, (float) aabb.maxY, (float) aabb.maxZ), new Vector3f((float) aabb.maxX, (float) aabb.minY, (float) aabb.maxZ), new Vector3f((float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ));

        Vector4f position = null;
        for (Vector3f vector : vectors) {
            vector = project(factor, vector.x, vector.y, vector.z);

            if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                if (position == null) {
                    position = new Vector4f(vector.x, vector.y, vector.z, 0.0f);
                }


                position = new Vector4f(Math.min(vector.x, position.x), Math.min(vector.y, position.y), Math.max(vector.x, position.z), Math.max(vector.y, position.w));
            }
        }

        if (position == null) {
            position = new Vector4f(0, 0, 0, 0);
        }



        return position;
    }

    @EventLink
    public void onRender2D(Render2DEvent event) {
        final double renderX = mc.getRenderManager().renderPosX;
        final double renderY = mc.getRenderManager().renderPosY;
        final double renderZ = mc.getRenderManager().renderPosZ;
        final int factor = new ScaledResolution(mc).getScaleFactor();
        final float partialTicks = event.getPartialTicks();
        for (Entity player : mc.theWorld.loadedEntityList) {
            if (mc.getRenderManager() == null || player == mc.thePlayer || player.isDead || player.isInvisible() || player instanceof EntityArmorStand) {
                continue;
            }
            if(!(player instanceof EntityLivingBase))
                continue;


            Vector4f pos = calc(player,partialTicks,renderX,renderY,renderZ,factor);

            if (pos == null) {
                return;
            }

            // Black outline
            rectangle(pos.x, pos.y, pos.z - pos.x, 1.5, Color.BLACK); // Top
            rectangle(pos.x, pos.y, 1.5, pos.w - pos.y + 1.5, Color.BLACK); // Left
            rectangle(pos.z, pos.y, 1.5, pos.w - pos.y + 1.5, Color.BLACK); // Right
            rectangle(pos.x, pos.w, pos.z - pos.x, 1.5, Color.BLACK); // Bottom

            final Vector2f first = new Vector2f(0, 0), second = new Vector2f(0, 500);

            horizontalGradient(pos.x + 0.5, pos.y + 0.5, pos.z - pos.x, 0.5, // Top
                    ColorUtil.GetColor(0),  ColorUtil.GetColor(1500));
            verticalGradient(pos.x + 0.5, pos.y + 0.5, 0.5, pos.w - pos.y + 0.5, // Left
                    ColorUtil.GetColor(0), ColorUtil.GetColor(500));
            verticalGradient(pos.z + 0.5, pos.y + 0.5, 0.5, pos.w - pos.y + 0.5, // Right
                    ColorUtil.GetColor(500),  ColorUtil.GetColor(0));
            horizontalGradient(pos.x + 0.5, pos.w + 0.5, pos.z - pos.x, 0.5, // Bottom
                    ColorUtil.GetColor(1500),  ColorUtil.GetColor(0));

        }

        if(chest.get()) {
            for (final TileEntity te : mc.theWorld.loadedTileEntityList) {
                if (te instanceof TileEntityHopper || te instanceof TileEntityDispenser) {
                    drawBlockEsp(te.getPos(),event);
                }
                if (te instanceof TileEntityEnderChest) {
                    drawBlockEsp(te.getPos(),event);
                }
                if (te instanceof TileEntityChest) {
                    drawBlockEsp(te.getPos(),event);
                }
            }
        }
    }

    public void drawBlockEsp(BlockPos posb,Render2DEvent event) {
        final double renderX = mc.getRenderManager().renderPosX;
        final double renderY = mc.getRenderManager().renderPosY;
        final double renderZ = mc.getRenderManager().renderPosZ;
        final int factor = new ScaledResolution(mc).getScaleFactor();
        final float partialTicks = event.getPartialTicks();
        Vector4f pos = calc(posb,partialTicks,renderX,renderY,renderZ,factor);

        if (pos == null) {
            return;
        }

        // Black outline
        rectangle(pos.x, pos.y, pos.z - pos.x, 1.5, Color.BLACK); // Top
        rectangle(pos.x, pos.y, 1.5, pos.w - pos.y + 1.5, Color.BLACK); // Left
        rectangle(pos.z, pos.y, 1.5, pos.w - pos.y + 1.5, Color.BLACK); // Right
        rectangle(pos.x, pos.w, pos.z - pos.x, 1.5, Color.BLACK); // Bottom

        final Vector2f first = new Vector2f(0, 0), second = new Vector2f(0, 500);

        horizontalGradient(pos.x + 0.5, pos.y + 0.5, pos.z - pos.x, 0.5, // Top
                ColorUtil.GetColor(0),  ColorUtil.GetColor(1500));
        verticalGradient(pos.x + 0.5, pos.y + 0.5, 0.5, pos.w - pos.y + 0.5, // Left
                ColorUtil.GetColor(0), ColorUtil.GetColor(500));
        verticalGradient(pos.z + 0.5, pos.y + 0.5, 0.5, pos.w - pos.y + 0.5, // Right
                ColorUtil.GetColor(500),  ColorUtil.GetColor(0));
        horizontalGradient(pos.x + 0.5, pos.w + 0.5, pos.z - pos.x, 0.5, // Bottom
                ColorUtil.GetColor(1500),  ColorUtil.GetColor(0));
    }

    public static void rectangle(final double x, final double y, final double width, final double height, final Color color) {
        start();

        if (color != null) {
            glColor(color.getRGB());
        }

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x, y + height);
        GL11.glEnd();

        stop();
    }

    public void horizontalGradient(final double x, final double y, final double width, final double height, final int leftColor, final int rightColor) {
        start();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);

        glColor(leftColor);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y + height);

        glColor(rightColor);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y);

        GL11.glEnd();
        GL11.glShadeModel(GL11.GL_FLAT);
        stop();
    }

    public void verticalGradient(final double x, final double y, final double width, final double height, final int topColor, final int bottomColor) {
        start();
        GlStateManager.alphaFunc(516, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);

        glColor(topColor);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + width, y);

        glColor(bottomColor);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x, y + height);

        GL11.glEnd();
        GL11.glShadeModel(GL11.GL_FLAT);
        stop();
    }


    static void glColor(final int hex) {
        final float a = (hex >> 24 & 0xFF) / 255.0F;
        final float r = (hex >> 16 & 0xFF) / 255.0F;
        final float g = (hex >> 8 & 0xFF) / 255.0F;
        final float b = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(r, g, b, a);
    }

    public static void start() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }


    public static void stop() {
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }
}