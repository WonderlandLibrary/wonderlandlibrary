/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.module.modules.Killaura;
import cow.milkgod.cheese.people.EnemyManager;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.utils.R2DUtils;
import cow.milkgod.cheese.utils.Wrapper;
import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class R3DUtils {
    public static void setup3DLightlessModel() {
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
    }

    public static void shutdown3DLightlessModel() {
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
    }

    public static Color getRainbow(long offset, float fade) {
        float hue = (float)(System.nanoTime() + offset) / 5.0E9f % 1.0f;
        long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        Color c2 = new Color((int)color);
        return new Color((float)c2.getRed() / 255.0f * fade, (float)c2.getGreen() / 255.0f * fade, (float)c2.getBlue() / 255.0f * fade, (float)c2.getAlpha() / 255.0f);
    }

    public static final void finish3DOGLConstants() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
    }

    public static final void start3DOGLConstants() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
    }

    public static void NonLivingEntityBox(Entity entity) {
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        R3DUtils.drawOutlinedBox(new AxisAlignedBB(entity.boundingBox.minX - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.minY - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.minZ - entity.posZ + (entity.posZ - RenderManager.renderPosZ), entity.boundingBox.maxX - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.maxY - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.maxZ - entity.posZ + (entity.posZ - RenderManager.renderPosZ)));
        R2DUtils.glColor(820529726);
        R3DUtils.drawColorBox(new AxisAlignedBB(entity.boundingBox.minX - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.minY - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.minZ - entity.posZ + (entity.posZ - RenderManager.renderPosZ), entity.boundingBox.maxX - entity.posX + (entity.posX - RenderManager.renderPosX), entity.boundingBox.maxY - entity.posY + (entity.posY - RenderManager.renderPosY), entity.boundingBox.maxZ - entity.posZ + (entity.posZ - RenderManager.renderPosZ)));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
    }

    public static void RenderESPPlayers(Entity entity, float partialTicks) {
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
        double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
        double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
        R3DUtils.drawOutlinedBox(new AxisAlignedBB(entity.boundingBox.minX - entity.posX + posX, entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + posY, entity.boundingBox.minZ - entity.posZ + posZ, entity.boundingBox.maxX - entity.posX + posX, entity.boundingBox.maxY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + posY, entity.boundingBox.maxZ - entity.posZ + posZ));
        R2DUtils.glColor(FriendManager.isFriend(entity.getName()) ? 805551871 : 820529726);
        R3DUtils.drawColorBox(new AxisAlignedBB(entity.boundingBox.minX - entity.posX + posX, entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + posY, entity.boundingBox.minZ - entity.posZ + posZ, entity.boundingBox.maxX - entity.posX + posX, entity.boundingBox.maxY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + posY, entity.boundingBox.maxZ - entity.posZ + posZ));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
    }

    public static void RenderLivingEntityBox(Entity entity, float partialTicks) {
        GlStateManager.pushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(2896);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
        double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
        double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
        boolean isPlayer = entity instanceof EntityPlayer;
        R3DUtils.drawOutlinedBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + posY, entity.boundingBox.minZ - (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ, entity.boundingBox.maxX + (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.maxY + (isPlayer ? 0.2 : 0.0) - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + posY, entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ));
        int color = FriendManager.isFriend(entity.getName()) ? 805551871 : (EnemyManager.isEnemy(entity.getName()) ? 820529726 : (Killaura.attackList.contains(entity) ? 553632000 : 536939779));
        R2DUtils.glColor(color);
        R3DUtils.drawColorBox(new AxisAlignedBB(entity.boundingBox.minX - (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.minY - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + posY, entity.boundingBox.minZ - (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ, entity.boundingBox.maxX + (isPlayer ? 0.12 : 0.0) - entity.posX + posX, entity.boundingBox.maxY + (isPlayer ? 0.2 : 0.0) - (entity.isSneaking() ? entity.posY + 0.2 : entity.posY) + posY, entity.boundingBox.maxZ + (isPlayer ? 0.12 : 0.0) - entity.posZ + posZ));
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GlStateManager.popMatrix();
    }

    public static int getBlockColor(Block block) {
        int color = block.getMaterial().getMaterialMapColor().colorValue;
        switch (Block.getIdFromBlock(block)) {
            case 14: 
            case 41: {
                color = -1711477173;
                break;
            }
            case 15: 
            case 42: {
                color = -1715420992;
                break;
            }
            case 16: 
            case 173: {
                color = -1724434633;
                break;
            }
            case 21: 
            case 22: {
                color = -1726527803;
                break;
            }
            case 49: {
                color = -1724108714;
                break;
            }
            case 54: 
            case 146: {
                color = -1711292672;
                break;
            }
            case 56: 
            case 57: 
            case 138: {
                color = -1721897739;
                break;
            }
            case 61: 
            case 62: {
                color = -1711395081;
                break;
            }
            case 73: 
            case 74: 
            case 152: {
                color = -1711341568;
                break;
            }
            case 89: {
                color = -1712594866;
                break;
            }
            case 129: 
            case 133: {
                color = -1726489246;
                break;
            }
            case 130: {
                color = -1713438249;
                break;
            }
            case 52: {
                color = 805728308;
                break;
            }
            default: {
                color = -1711276033;
            }
        }
        return color == 0 ? 806752583 : color;
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB) {
        Tessellator var16 = Tessellator.getInstance();
        WorldRenderer var17 = var16.getWorldRenderer();
        var17.startDrawing(3);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var16.draw();
        var17.startDrawing(3);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var16.draw();
        var17.startDrawing(1);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var17.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        var17.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        var16.draw();
    }

    public static void renderItemStack(ItemStack stack, int x2, int y2) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableLighting();
        Wrapper.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.func_179090_x();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.func_179098_w();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        Wrapper.mc.getRenderItem().func_180450_b(stack, x2, y2);
        Wrapper.mc.getRenderItem().func_175030_a(Wrapper.mc.fontRendererObj, stack, x2, y2);
        Wrapper.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        R3DUtils.drawitemStackEnchants(stack, x2 * 2, y2 * 2);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }

    public static void drawBoundingBox(AxisAlignedBB aabb) {
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        Tessellator tessellator = Tessellator.getInstance();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        tessellator.draw();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
        worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
        worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
        worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
        tessellator.draw();
    }

    public static void drawitemStackEnchants(ItemStack stak, int x2, int y2) {
        NBTTagList enchants = stak.getEnchantmentTagList();
        if (enchants != null) {
            int ency = 0;
            int index = 0;
            while (index < enchants.tagCount()) {
                short id2 = enchants.getCompoundTagAt(index).getShort("id");
                short level = enchants.getCompoundTagAt(index).getShort("lvl");
                if (Enchantment.field_180311_a[id2] != null) {
                    Enchantment enc = Enchantment.field_180311_a[id2];
                    String encName = enc.getTranslatedName(level).substring(0, 2).toLowerCase();
                    String[] ShownEnchants = new String[]{"Efficiency", "Unbreaking", "Sharpness", "FireAspect", ""};
                    Wrapper.fr.drawStringWithShadow(String.valueOf(String.valueOf(encName)) + "\u00a7b" + level, x2, y2 + ency, -5592406);
                    ency += Wrapper.fr.FONT_HEIGHT;
                    if (index > 4) {
                        Wrapper.fr.drawStringWithShadow("\u00a7f+ others", x2, y2 + ency, -5592406);
                        break;
                    }
                }
                ++index;
            }
        }
    }

    public static void drawRect(float g2, float h2, float i2, float j2, int col1) {
        float f2 = (float)(col1 >> 24 & 255) / 255.0f;
        float f22 = (float)(col1 >> 16 & 255) / 255.0f;
        float f3 = (float)(col1 >> 8 & 255) / 255.0f;
        float f4 = (float)(col1 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f22, f3, f4, f2);
        GL11.glBegin(7);
        GL11.glVertex2d(i2, h2);
        GL11.glVertex2d(g2, h2);
        GL11.glVertex2d(g2, j2);
        GL11.glVertex2d(i2, j2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawOutlinedBox(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        GL11.glBegin(3);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(1);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb) {
        Tessellator var16 = Tessellator.getInstance();
        WorldRenderer tessellator = var16.getWorldRenderer();
        tessellator.startDrawingQuads();
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        var16.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        var16.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        var16.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        var16.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        var16.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
        tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        var16.draw();
    }

    public static class Camera {
        private final Minecraft mc = Minecraft.getMinecraft();
        private Timer timer;
        private double posX;
        private double posY;
        private double posZ;
        private float rotationYaw;
        private float rotationPitch;

        public Camera(Entity entity) {
            if (this.timer == null) {
                this.timer = this.mc.timer;
            }
            this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.timer.renderPartialTicks;
            this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.timer.renderPartialTicks;
            this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.timer.renderPartialTicks;
            this.setRotationYaw(entity.rotationYaw);
            this.setRotationPitch(entity.rotationPitch);
            if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
                EntityPlayer living1 = (EntityPlayer)entity;
                this.setRotationYaw(this.getRotationYaw() + living1.prevCameraYaw + (living1.cameraYaw - living1.prevCameraYaw) * this.timer.renderPartialTicks);
                this.setRotationPitch(this.getRotationPitch() + living1.prevCameraPitch + (living1.cameraPitch - living1.prevCameraPitch) * this.timer.renderPartialTicks);
            } else if (entity instanceof EntityLivingBase) {
                EntityLivingBase living2 = (EntityLivingBase)entity;
                this.setRotationYaw(living2.rotationYawHead);
            }
        }

        public Camera(Entity entity, double offsetX, double offsetY, double offsetZ, double offsetRotationYaw, double offsetRotationPitch) {
            this.posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)this.timer.renderPartialTicks;
            this.posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)this.timer.renderPartialTicks;
            this.posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)this.timer.renderPartialTicks;
            this.setRotationYaw(entity.rotationYaw);
            this.setRotationPitch(entity.rotationPitch);
            if (entity instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.viewBobbing && entity == Minecraft.getMinecraft().thePlayer) {
                EntityPlayer player = (EntityPlayer)entity;
                this.setRotationYaw(this.getRotationYaw() + player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * this.timer.renderPartialTicks);
                this.setRotationPitch(this.getRotationPitch() + player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * this.timer.renderPartialTicks);
            }
            this.posX += offsetX;
            this.posY += offsetY;
            this.posZ += offsetZ;
            this.rotationYaw += (float)offsetRotationYaw;
            this.rotationPitch += (float)offsetRotationPitch;
        }

        public Camera(double posX, double posY, double posZ, float rotationYaw, float rotationPitch) {
            this.setPosX(posX);
            this.posY = posY;
            this.posZ = posZ;
            this.setRotationYaw(rotationYaw);
            this.setRotationPitch(rotationPitch);
        }

        public double getPosX() {
            return this.posX;
        }

        public void setPosX(double posX) {
            this.posX = posX;
        }

        public double getPosY() {
            return this.posY;
        }

        public void setPosY(double posY) {
            this.posY = posY;
        }

        public double getPosZ() {
            return this.posZ;
        }

        public void setPosZ(double posZ) {
            this.posZ = posZ;
        }

        public float getRotationYaw() {
            return this.rotationYaw;
        }

        public void setRotationYaw(float rotationYaw) {
            this.rotationYaw = rotationYaw;
        }

        public float getRotationPitch() {
            return this.rotationPitch;
        }

        public void setRotationPitch(float rotationPitch) {
            this.rotationPitch = rotationPitch;
        }

        public static float[] getRotation(double posX1, double posY1, double posZ1, double posX2, double posY2, double posZ2) {
            float[] rotation = new float[2];
            double diffX = posX2 - posX1;
            double diffZ = posZ2 - posZ1;
            double diffY = posY2 - posY1;
            double dist = Math.sqrt(diffZ * diffZ + diffX * diffX);
            double pitch = - Math.toDegrees(Math.atan(diffY / dist));
            rotation[1] = (float)pitch;
            double yaw = 0.0;
            if (diffZ >= 0.0 && diffX >= 0.0) {
                yaw = Math.toDegrees(- Math.atan(diffX / diffZ));
            } else if (diffZ >= 0.0 && diffX <= 0.0) {
                yaw = Math.toDegrees(- Math.atan(diffX / diffZ));
            } else if (diffZ <= 0.0 && diffX >= 0.0) {
                yaw = -90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
            } else if (diffZ <= 0.0 && diffX <= 0.0) {
                yaw = 90.0 + Math.toDegrees(Math.atan(diffZ / diffX));
            }
            rotation[0] = (float)yaw;
            return rotation;
        }
    }

    public static final class Stencil {
        private static final Stencil INSTANCE = new Stencil();
        private final HashMap<Integer, StencilFunc> stencilFuncs = new HashMap();
        private int layers = 1;
        private boolean renderMask;

        public static Stencil getInstance() {
            return INSTANCE;
        }

        public void setRenderMask(boolean renderMask) {
            this.renderMask = renderMask;
        }

        public void startLayer() {
            if (this.layers == 1) {
                GL11.glClearStencil(0);
                GL11.glClear(1024);
            }
            GL11.glEnable(2960);
            ++this.layers;
            if (this.layers > this.getMaximumLayers()) {
                System.out.println("StencilUtil: Reached maximum amount of layers!");
                this.layers = 1;
            }
        }

        public void stopLayer() {
            if (this.layers == 1) {
                System.out.println("StencilUtil: No layers found!");
                return;
            }
            --this.layers;
            if (this.layers == 1) {
                GL11.glDisable(2960);
            } else {
                StencilFunc lastStencilFunc = this.stencilFuncs.remove(this.layers);
                if (lastStencilFunc != null) {
                    lastStencilFunc.use();
                }
            }
        }

        public void clear() {
            GL11.glClearStencil(0);
            GL11.glClear(1024);
            this.stencilFuncs.clear();
            this.layers = 1;
        }

        public void setBuffer() {
            this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, this.layers, this.getMaximumLayers(), 7681, 7680, 7680));
        }

        public void setBuffer(boolean set) {
            this.setStencilFunc(new StencilFunc(this, this.renderMask ? 519 : 512, set ? this.layers : this.layers - 1, this.getMaximumLayers(), 7681, 7681, 7681));
        }

        public void cropOutside() {
            this.setStencilFunc(new StencilFunc(this, 517, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
        }

        public void cropInside() {
            this.setStencilFunc(new StencilFunc(this, 514, this.layers, this.getMaximumLayers(), 7680, 7680, 7680));
        }

        public void setStencilFunc(StencilFunc stencilFunc) {
            GL11.glStencilFunc(StencilFunc.func_func, StencilFunc.func_ref, StencilFunc.func_mask);
            GL11.glStencilOp(StencilFunc.op_fail, StencilFunc.op_zfail, StencilFunc.op_zpass);
            this.stencilFuncs.put(this.layers, stencilFunc);
        }

        public StencilFunc getStencilFunc() {
            return this.stencilFuncs.get(this.layers);
        }

        public int getLayer() {
            return this.layers;
        }

        public int getStencilBufferSize() {
            return GL11.glGetInteger(3415);
        }

        public int getMaximumLayers() {
            return (int)(Math.pow(2.0, this.getStencilBufferSize()) - 1.0);
        }

        public void createCirlce(double x2, double y2, double radius) {
            GL11.glBegin(6);
            int i2 = 0;
            while (i2 <= 360) {
                double sin = Math.sin((double)i2 * 3.141592653589793 / 180.0) * radius;
                double cos2 = Math.cos((double)i2 * 3.141592653589793 / 180.0) * radius;
                GL11.glVertex2d(x2 + sin, y2 + cos2);
                ++i2;
            }
            GL11.glEnd();
        }

        public void createRect(double x2, double y2, double x22, double y22) {
            GL11.glBegin(7);
            GL11.glVertex2d(x2, y22);
            GL11.glVertex2d(x22, y22);
            GL11.glVertex2d(x22, y2);
            GL11.glVertex2d(x2, y2);
            GL11.glEnd();
        }

        public static class StencilFunc {
            public static int func_func;
            public static int func_ref;
            public static int func_mask;
            public static int op_fail;
            public static int op_zfail;
            public static int op_zpass;

            public StencilFunc(Stencil paramStencil, int func_func, int func_ref, int func_mask, int op_fail, int op_zfail, int op_zpass) {
                StencilFunc.func_func = func_func;
                StencilFunc.func_ref = func_ref;
                StencilFunc.func_mask = func_mask;
                StencilFunc.op_fail = op_fail;
                StencilFunc.op_zfail = op_zfail;
                StencilFunc.op_zpass = op_zpass;
            }

            public void use() {
                GL11.glStencilFunc(func_func, func_ref, func_mask);
                GL11.glStencilOp(op_fail, op_zfail, op_zpass);
            }
        }

    }

}

