package net.minecraft.client.renderer.tileentity;

import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.render.model.ChestModelRenderEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest>
{
    private static final ResourceLocation textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
    private static final ResourceLocation textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
    private static final ResourceLocation textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
    private static final ResourceLocation textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
    private ModelChest simpleChest = new ModelChest();
    private ModelChest largeChest = new ModelLargeChest();

    public static boolean isRenderingInsideInventory = false;

    public void renderTileEntityAt(TileEntityChest te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(515);
        GL11.glDepthMask(true);
        int i;

        if (!te.hasWorldObj())
        {
            i = 0;
        }
        else
        {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();

            if (block instanceof BlockChest && i == 0)
            {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }

            te.checkForAdjacentChests();
        }

        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null)
        {
            ModelChest modelchest;

            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null)
            {
                modelchest = this.simpleChest;

                if (destroyStage >= 0)
                {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GL11.glMatrixMode(5890);
                    GL11.glPushMatrix();
                    GL11.glScalef(4.0F, 4.0F, 1.0F);
                    GL11.glTranslatef(0.0625F, 0.0625F, 0.0625F);
                    GL11.glMatrixMode(5888);
                }
                else if (te.getChestType() == 1)
                {
                    this.bindTexture(textureTrapped);
                }
                else
                {
                    this.bindTexture(textureNormal);
                }
            }
            else
            {
                modelchest = this.largeChest;

                if (destroyStage >= 0)
                {
                    this.bindTexture(DESTROY_STAGES[destroyStage]);
                    GL11.glMatrixMode(5890);
                    GL11.glPushMatrix();
                    GL11.glScalef(8.0F, 4.0F, 1.0F);
                    GL11.glTranslatef(0.0625F, 0.0625F, 0.0625F);
                    GL11.glMatrixMode(5888);
                }
                else if (te.getChestType() == 1)
                {
                    this.bindTexture(textureTrappedDouble);
                }
                else
                {
                    this.bindTexture(textureNormalDouble);
                }
            }

            GL11.glPushMatrix();
            GlStateManager.enableRescaleNormal();

            if (destroyStage < 0)
            {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            int j = 0;

            if (i == 2)
            {
                j = 180;
            }

            if (i == 3)
            {
                j = 0;
            }

            if (i == 4)
            {
                j = 90;
            }

            if (i == 5)
            {
                j = -90;
            }

            if (i == 2 && te.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && te.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)j, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

            if (te.adjacentChestZNeg != null)
            {
                float f1 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;

                if (f1 > f)
                {
                    f = f1;
                }
            }

            if (te.adjacentChestXNeg != null)
            {
                float f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;

                if (f2 > f)
                {
                    f = f2;
                }
            }

            f = 1.0F - f;
            f = 1.0F - f * f * f;
            modelchest.chestLid.rotateAngleX = -(f * (float)Math.PI / 2.0F);
            modelchest.renderAll();

            if (!isRenderingInsideInventory) {
                final ChestModelRenderEvent event = new ChestModelRenderEvent(te, modelchest::renderAll, x, y, z);
                KetamineClient.getInstance().getEventBus().post(event);
            }

            GlStateManager.disableRescaleNormal();
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (destroyStage >= 0)
            {
                GL11.glMatrixMode(5890);
                GL11.glPopMatrix();
                GL11.glMatrixMode(5888);
            }
        }
    }
}
