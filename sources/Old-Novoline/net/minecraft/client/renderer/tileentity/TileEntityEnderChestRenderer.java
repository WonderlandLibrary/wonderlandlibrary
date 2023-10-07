package net.minecraft.client.renderer.tileentity;

import cc.novoline.Novoline;
import cc.novoline.modules.visual.ChestESP;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest> {
    private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
    private final ModelChest modelChest = new ModelChest();

    public void renderTileEntityAt(TileEntityEnderChest te, double x, double y, double z, float partialTicks, int destroyStage) {
        int i = 0;

        if (te.hasWorldObj()) {
            i = te.getBlockMetadata();
        }

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(ENDER_CHEST_TEXTURE);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int j = 0;

        if (i == 2) {
            j = 180;
        }

        if (i == 3) {
            j = 0;
        }

        if (i == 4) {
            j = 90;
        }

        if (i == 5) {
            j = -90;
        }

        GlStateManager.rotate((float) j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        this.modelChest.chestLid.rotateAngleX = -(f * (float) Math.PI / 2.0F);

        if (Novoline.getInstance().getModuleManager().getModule(ChestESP.class).isEnabled() && te.hasWorldObj()) {
            ChestESP chestESP = Novoline.getInstance().getModuleManager().getModule(ChestESP.class);

            if (chestESP.getMode().equalsIgnoreCase("Outline")) {
                float[] hexColor = chestESP.getColorForTileEntity();
                int color = chestESP.toRGBAHex(hexColor[0] / 255.0f, hexColor[1] / 255.0f, hexColor[2] / 255.0f, 1.0f);
                modelChest.renderAll();
                chestESP.pre3D();
                modelChest.renderAll();
                chestESP.setupStencilFirst();
                modelChest.renderAll();
                chestESP.setupStencilSecond();
                chestESP.renderOutline(color);
                modelChest.renderAll();
                chestESP.post3D();
            } else if (chestESP.getMode().equalsIgnoreCase("Chams")) {
                int sexyHidden = chestESP.getHidden().getAwtColor().getRGB();
                GL11.glPushMatrix();
                GL11.glDisable(GL_TEXTURE_2D);
                GL11.glEnable(GL_BLEND);
                GL11.glDisable(GL_LIGHTING);
                GL11.glEnable(GL_STENCIL_TEST);
                GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.disableDepth();
                float[] hexColor = chestESP.getColorForTileEntity();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
                GL11.glColor4f(
                        (sexyHidden >> 16 & 0xFF) / 255.0F,
                        (sexyHidden >> 8 & 0xFF) / 255.0F,
                        (sexyHidden & 0xFF) / 255.0F,
                        255f);
                modelChest.renderAll();
                GlStateManager.enableDepth();
                sexyHidden = chestESP.getVisible().getAwtColor().getRGB();
                GL11.glColor4f(
                        (sexyHidden >> 16 & 0xFF) / 255.0F,
                        (sexyHidden >> 8 & 0xFF) / 255.0F,
                        (sexyHidden & 0xFF) / 255.0F,
                        255f);
                modelChest.renderAll();

                GL11.glEnable(GL_LIGHTING);
                GL11.glDisable(GL_BLEND);
                GL11.glEnable(GL_TEXTURE_2D);
                GL11.glPopMatrix();
            } else {
                modelChest.renderAll();
            }
        } else {
            modelChest.renderAll();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }

}
