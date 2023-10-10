/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.rektsky.Cape;

public class LayerCape
implements LayerRenderer<EntityLivingBase> {
    private final RenderPlayer playerRenderer;
    private static final String __OBFID = "CL_00002425";
    private Cape mod = ModulesManager.getModuleByClass(Cape.class);
    private String capeSelected = "rektsky/capes/rektsky.png";

    public LayerCape(RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() && (entitylivingbaseIn.getName().startsWith("RektUser") || entitylivingbaseIn.getName().startsWith("Reeker")) && ModulesManager.getModuleByClass(Cape.class).isToggled()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (this.mod.mode.getValue().equals("RektSky")) {
                this.capeSelected = "rektsky/capes/rektsky.png";
            } else if (this.mod.mode.getValue().equals("Legacy")) {
                this.capeSelected = "rektsky/capes/legacy.png";
            } else if (this.mod.mode.getValue().equals("Astolfo")) {
                this.capeSelected = "rektsky/capes/astolfo.png";
            } else if (this.mod.mode.getValue().equals("Kant")) {
                this.capeSelected = "rektsky/capes/kant.png";
            } else if (this.mod.mode.getValue().equals("Cats")) {
                this.capeSelected = "rektsky/capes/cat.png";
            }
            this.playerRenderer.bindTexture(new ResourceLocation(this.capeSelected));
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.0f, 0.125f);
            double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double)partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks);
            double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double)partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks);
            double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double)partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks);
            float f2 = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
            double d3 = MathHelper.sin(f2 * (float)Math.PI / 180.0f);
            double d4 = -MathHelper.cos(f2 * (float)Math.PI / 180.0f);
            float f1 = (float)d1 * 10.0f;
            f1 = MathHelper.clamp_float(f1, -6.0f, 32.0f);
            float f22 = (float)(d0 * d3 + d2 * d4) * 100.0f;
            float f3 = (float)(d0 * d4 - d2 * d3) * 100.0f;
            if (f22 < 0.0f) {
                f22 = 0.0f;
            }
            if (f22 > 165.0f) {
                f22 = 165.0f;
            }
            float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
            f1 += MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f4;
            if (entitylivingbaseIn.isSneaking()) {
                f1 += 25.0f;
                GlStateManager.translate(0.0f, 0.142f, -0.0178f);
            }
            GlStateManager.rotate(6.0f + f22 / 2.0f + f1, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(f3 / 2.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(-f3 / 2.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            this.playerRenderer.getMainModel().renderCape(0.0625f);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        this.doRenderLayer((AbstractClientPlayer)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks, p_177141_5_, p_177141_6_, p_177141_7_, scale);
    }
}

