package com.enjoytheban.module.modules.render;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import com.enjoytheban.api.EventHandler;
import com.enjoytheban.api.events.rendering.EventRender3D;
import com.enjoytheban.module.Module;
import com.enjoytheban.module.ModuleType;
import com.enjoytheban.utils.math.Vec4f;
import com.enjoytheban.utils.render.GLUProjection;
import com.enjoytheban.utils.render.RenderUtil;

import javax.vecmath.Vector3d;
import java.awt.*;

public class ChestESP extends Module {

    public ChestESP() {
        super("ChestESP", new String[]{"chesthack"}, ModuleType.Render);
        setColor(new Color(90,209,165).getRGB());
    }

    @EventHandler
    public void onRender(EventRender3D eventRender) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        block4 : for (Object o : mc.theWorld.loadedTileEntityList) {
            TileEntity tileEntity = (TileEntity)o;
            if (tileEntity == null || !isStorage(tileEntity)) continue;
            double posX = tileEntity.getPos().getX();
            double posY = tileEntity.getPos().getY();
            double posZ = tileEntity.getPos().getZ();
            Block block;
            AxisAlignedBB axisAlignedBB = null;
            if (tileEntity instanceof TileEntityChest) {
                block = mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
                Block x1 = mc.theWorld.getBlockState(new BlockPos(posX + 1.0, posY, posZ)).getBlock();
                Block x2 = mc.theWorld.getBlockState(new BlockPos(posX - 1.0, posY, posZ)).getBlock();
                Block z1 = mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ + 1.0)).getBlock();
                Block z2 = mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ - 1.0)).getBlock();
                if (block != Blocks.trapped_chest) {
                    if (x1 == Blocks.chest) {
                        axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().renderPosX, posY - mc.getRenderManager().renderPosY, posZ + 0.05000000074505806 - mc.getRenderManager().renderPosZ, posX + 1.9500000476837158 - mc.getRenderManager().renderPosX, posY + 0.8999999761581421 - mc.getRenderManager().renderPosY, posZ + 0.949999988079071 - mc.getRenderManager().renderPosZ);
                    } else if (z2 == Blocks.chest) {
                        axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().renderPosX, posY - mc.getRenderManager().renderPosY, posZ + 0.05000000074505806 - mc.getRenderManager().renderPosZ - 1.0, posX + 0.949999988079071 - mc.getRenderManager().renderPosX, posY + 0.8999999761581421 - mc.getRenderManager().renderPosY, posZ + 0.949999988079071 - mc.getRenderManager().renderPosZ);
                    } else if (x1 != Blocks.chest && x2 != Blocks.chest && z1 != Blocks.chest && z2 != Blocks.chest) {
                        axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().renderPosX, posY - mc.getRenderManager().renderPosY, posZ + 0.05000000074505806 - mc.getRenderManager().renderPosZ, posX + 0.949999988079071 - mc.getRenderManager().renderPosX, posY + 0.8999999761581421 - mc.getRenderManager().renderPosY, posZ + 0.949999988079071 - mc.getRenderManager().renderPosZ);
                    }
                } else if (x1 == Blocks.trapped_chest) {
                    axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().renderPosX, posY - mc.getRenderManager().renderPosY, posZ + 0.05000000074505806 - mc.getRenderManager().renderPosZ, posX + 1.9500000476837158 - mc.getRenderManager().renderPosX, posY + 0.8999999761581421 - mc.getRenderManager().renderPosY, posZ + 0.949999988079071 - mc.getRenderManager().renderPosZ);
                } else if (z2 == Blocks.trapped_chest) {
                    axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().renderPosX, posY - mc.getRenderManager().renderPosY, posZ + 0.05000000074505806 - mc.getRenderManager().renderPosZ - 1.0, posX + 0.949999988079071 - mc.getRenderManager().renderPosX, posY + 0.8999999761581421 - mc.getRenderManager().renderPosY, posZ + 0.949999988079071 - mc.getRenderManager().renderPosZ);
                } else if (x1 != Blocks.trapped_chest && x2 != Blocks.trapped_chest && z1 != Blocks.trapped_chest && z2 != Blocks.trapped_chest) {
                    axisAlignedBB = new AxisAlignedBB(posX + 0.05000000074505806 - mc.getRenderManager().renderPosX, posY - mc.getRenderManager().renderPosY, posZ + 0.05000000074505806 - mc.getRenderManager().renderPosZ, posX + 0.949999988079071 - mc.getRenderManager().renderPosX, posY + 0.8999999761581421 - mc.getRenderManager().renderPosY, posZ + 0.949999988079071 - mc.getRenderManager().renderPosZ);
                }
            } else {
                axisAlignedBB = new AxisAlignedBB(posX - mc.getRenderManager().renderPosX, posY - mc.getRenderManager().renderPosY, posZ - mc.getRenderManager().renderPosZ, posX + 1.0 - mc.getRenderManager().renderPosX, posY + 1.0 - mc.getRenderManager().renderPosY, posZ + 1.0 - mc.getRenderManager().renderPosZ);
            }
            if (axisAlignedBB == null) continue block4;
            float[] colors = this.getColorForTileEntity(tileEntity);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GL11.glEnable(2848);
            RenderHelper.drawCompleteBox(axisAlignedBB, 1.0f, toRGBAHex(colors[0] / 255.0f, colors[1] / 255.0f, colors[2] / 255.0f, 0.1254902f), toRGBAHex(colors[0] / 255.0f, colors[1] / 255.0f, colors[2] / 255.0f, 1.0f));
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();

            AxisAlignedBB bb = null;
            if (tileEntity instanceof TileEntityChest) {

                block = mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
                Block posX1 = mc.theWorld.getBlockState(new BlockPos(posX + 1.0, posY, posZ)).getBlock();
                Block posX2 = mc.theWorld.getBlockState(new BlockPos(posX - 1.0, posY, posZ)).getBlock();
                Block posZ1 = mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ + 1.0)).getBlock();
                Block posZ2 = mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ - 1.0)).getBlock();
                if (block != Blocks.trapped_chest) {
                    if (posX1 == Blocks.chest) {
                        bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806, posX + 1.9500000476837158, posY + 0.8999999761581421, posZ + 0.949999988079071);
                    } else if (posZ2 == Blocks.chest) {
                        bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806 - 1.0, posX + 0.949999988079071, posY + 0.8999999761581421, posZ + 0.949999988079071);
                    } else if (posX1 != Blocks.chest && posX2 != Blocks.chest && posZ1 != Blocks.chest && posZ2 != Blocks.chest) {
                        bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806, posX + 0.949999988079071, posY + 0.8999999761581421, posZ + 0.949999988079071);
                    }
                } else if (posX1 == Blocks.trapped_chest) {
                    bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806, posX + 1.9500000476837158, posY + 0.8999999761581421, posZ + 0.949999988079071);
                } else if (posZ2 == Blocks.trapped_chest) {
                    bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806 - 1.0, posX + 0.949999988079071, posY + 0.8999999761581421, posZ + 0.949999988079071);
                } else if (posX1 != Blocks.trapped_chest && posX2 != Blocks.trapped_chest && posZ1 != Blocks.trapped_chest && posZ2 != Blocks.trapped_chest) {
                    bb = new AxisAlignedBB(posX + 0.05000000074505806, posY, posZ + 0.05000000074505806, posX + 0.949999988079071, posY + 0.8999999761581421, posZ + 0.949999988079071);
                }
            } else {
                bb = new AxisAlignedBB(posX, posY, posZ, posX + 1.0, posY + 1.0, posZ + 1.0);
            }
            if (bb == null) break;
            Vector3d[] corners = new Vector3d[]{new Vector3d(bb.minX, bb.minY, bb.minZ), new Vector3d(bb.maxX, bb.maxY, bb.maxZ), new Vector3d(bb.minX, bb.maxY, bb.maxZ), new Vector3d(bb.minX, bb.minY, bb.maxZ), new Vector3d(bb.maxX, bb.minY, bb.maxZ), new Vector3d(bb.maxX, bb.minY, bb.minZ), new Vector3d(bb.maxX, bb.maxY, bb.minZ), new Vector3d(bb.minX, bb.maxY, bb.minZ)};
            GLUProjection.Projection result = null;
            Vec4f transformed = new Vec4f((float)scaledResolution.getScaledWidth() * 2.0f, (float)scaledResolution.getScaledHeight() * 2.0f, -1.0f, -1.0f);
            for (Vector3d vec : corners) {
                result = GLUProjection.getInstance().project(vec.x - mc.getRenderManager().viewerPosX, vec.y - mc.getRenderManager().viewerPosY, vec.z - mc.getRenderManager().viewerPosZ, GLUProjection.ClampMode.NONE, true);
                transformed.setX((float)Math.min((double)transformed.getX(), result.getX()));
                transformed.setY((float)Math.min((double)transformed.getY(), result.getY()));
                transformed.setW((float)Math.max((double)transformed.getW(), result.getX()));
                transformed.setH((float)Math.max((double)transformed.getH(), result.getY()));
            }
            if (result == null) break;

        }
    }

    public static boolean isStorage(TileEntity entity) {
        return entity instanceof TileEntityChest || entity instanceof TileEntityBrewingStand || entity instanceof TileEntityDropper || entity instanceof TileEntityDispenser || entity instanceof TileEntityFurnace || entity instanceof TileEntityHopper || entity instanceof TileEntityEnderChest;
    }
    public static int toRGBAHex(float r, float g, float b, float a) {
        return ((int)(a * 255.0f) & 255) << 24 | ((int)(r * 255.0f) & 255) << 16 | ((int)(g * 255.0f) & 255) << 8 | (int)(b * 255.0f) & 255;
    }

    private float[] getColorForTileEntity(TileEntity entity) {
        if (entity instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest)entity;
            if (chest.getChestType() == 0) {
                return new float[]{180.0f, 160.0f, 0.0f, 255.0f};
            }
            if (chest.getChestType() == 1) {
                return new float[]{160.0f, 10.0f, 10.0f, 255.0f};
            }
        }
        if (entity instanceof TileEntityEnderChest) {
            return new float[]{0.0f, 160.0f, 100.0f, 255.0f};
        }
        if (entity instanceof TileEntityFurnace) {
            return new float[]{120.0f, 120.0f, 120.0f, 255.0f};
        }
        return new float[]{255.0f, 255.0f, 255.0f, 255.0f};
    }
}
