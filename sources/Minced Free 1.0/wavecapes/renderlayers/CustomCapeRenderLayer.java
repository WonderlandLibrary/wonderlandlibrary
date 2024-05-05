package wavecapes.renderlayers;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import wavecapes.CapeHolder;
import wavecapes.stim.StickSimulation;
import wavecapes.util.Mth;

import java.io.FileNotFoundException;

public class CustomCapeRenderLayer implements LayerRenderer<AbstractClientPlayer> {

    static final int partCount = 16;
    private ModelRenderer[] customCape = new ModelRenderer[partCount];
    private final RenderPlayer playerRenderer;
    private SmoothCapeRenderer smoothCapeRenderer = new SmoothCapeRenderer();

    public CustomCapeRenderLayer(RenderPlayer playerRenderer, ModelBase model) {
        this.playerRenderer = playerRenderer;
        buildMesh(model);
    }

    private void buildMesh(ModelBase model) {
        customCape = new ModelRenderer[partCount];
        for (int i = 0; i < partCount; i++) {
            ModelRenderer base = new ModelRenderer(model, 0, i);
            base.setTextureSize(64, 32);
            this.customCape[i] = base.addBox(-5.0F, (float) i, -1.0F, 10, 1, 1);
        }
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer abstractClientPlayer, float paramFloat1, float paramFloat2, float deltaTick,
                              float animationTick, float paramFloat5, float paramFloat6, float paramFloat7) {
        if (abstractClientPlayer.isInvisible()) return;

        if (Minecraft.getMinecraft().player != abstractClientPlayer) {
        	return;
        }
        
        if (!abstractClientPlayer.hasPlayerInfo() || abstractClientPlayer.isInvisible()
                || !abstractClientPlayer.isWearing(EnumPlayerModelParts.CAPE)) {
            return;
        }

//        if(WaveyCapesBase.config.capeMovement == CapeMovement.BASIC_SIMULATION) {
        CapeHolder holder = (CapeHolder) abstractClientPlayer;
        holder.updateSimulation(abstractClientPlayer, partCount);
//        }
        this.playerRenderer.bindTexture(new ResourceLocation("celestial/cape.png"));

        smoothCapeRenderer.renderSmoothCape(this, abstractClientPlayer, deltaTick);
    }

    private void modifyPoseStackSimulation(AbstractClientPlayer abstractClientPlayer, float delta, int part) {
        StickSimulation simulation = ((CapeHolder) abstractClientPlayer).getSimulation();
        GlStateManager.translate(0.0D, 0.0D, 0.125D);

        float z = simulation.points.get(part).getLerpX(delta) - simulation.points.get(0).getLerpX(delta);
        if (z > 0) {
            z = 0;
        }
        float y = simulation.points.get(0).getLerpY(delta) - part - simulation.points.get(part).getLerpY(delta);

        float sidewaysRotationOffset = 0;
        float partRotation = (float) -Math.atan2(y, z);
        partRotation = Math.max(partRotation, 0);
        if (partRotation != 0)
            partRotation = (float) (Math.PI - partRotation);
        partRotation *= 57.2958;
        partRotation *= 2;

        float height = 0;
        if (abstractClientPlayer.isSneaking()) {
            height += 25.0F;
            GlStateManager.translate(0, 0.15F, 0);
        }

        float naturalWindSwing = getNatrualWindSwing(part);


        // vanilla rotating and wind
        GlStateManager.rotate(6.0F + height + naturalWindSwing, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sidewaysRotationOffset / 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-sidewaysRotationOffset / 2.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0, y / partCount, z / partCount); // movement from the simulation
        //offsetting so the rotation is on the cape part
        //float offset = (float) (part * (16 / partCount))/16; // to fold the entire cape into one position for debugging
        GlStateManager.translate(0, /*-offset*/ +(0.48 / 16), -(0.48 / 16)); // (0.48/16)
        GlStateManager.translate(0, part * 1f / partCount, part * (0) / partCount);
        //GlStateManager.rotate(-partRotation, 1.0F, 0.0F, 0.0F);
        // undoing the rotation
        GlStateManager.translate(0, -part * 1f / partCount, -part * (0) / partCount);
        GlStateManager.translate(0, -(0.48 / 16), (0.48 / 16));

    }

    void modifyPoseStackVanilla(AbstractClientPlayer abstractClientPlayer, float h, int part) {
        GlStateManager.translate(0.0D, 0.0D, 0.125D);
        double d = Mth.lerp(h, abstractClientPlayer.prevChasingPosX, abstractClientPlayer.chasingPosX)
                - Mth.lerp(h, abstractClientPlayer.prevPosX, abstractClientPlayer.posX);
        double e = Mth.lerp(h, abstractClientPlayer.prevChasingPosY, abstractClientPlayer.chasingPosY)
                - Mth.lerp(h, abstractClientPlayer.prevPosY, abstractClientPlayer.posY);
        double m = Mth.lerp(h, abstractClientPlayer.prevChasingPosZ, abstractClientPlayer.chasingPosZ)
                - Mth.lerp(h, abstractClientPlayer.prevPosZ, abstractClientPlayer.posZ);
        float n = abstractClientPlayer.prevRenderYawOffset + abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset;
        double o = Math.sin(n * 0.017453292F);
        double p = -Math.cos(n * 0.017453292F);
        float height = (float) e * 10.0F;
        height = MathHelper.clamp(height, -6.0F, 32.0F);
        float swing = (float) (d * o + m * p) * easeOutSine(1.0F / partCount * part) * 100;
        swing = MathHelper.clamp(swing, 0.0F, 150.0F * easeOutSine(1F / partCount * part));
        float sidewaysRotationOffset = (float) (d * p - m * o) * 100.0F;
        sidewaysRotationOffset = MathHelper.clamp(sidewaysRotationOffset, -20.0F, 20.0F);
        float t = Mth.lerp(h, abstractClientPlayer.prevCameraYaw, abstractClientPlayer.cameraYaw);
        height += Math.sin(Mth.lerp(h, abstractClientPlayer.prevDistanceWalkedModified, abstractClientPlayer.distanceWalkedModified) * 6.0F) * 32.0F * t;
        if (abstractClientPlayer.isSneaking()) {
            height += 25.0F;
            GlStateManager.translate(0, 0.15F, 0);
        }

        float naturalWindSwing = getNatrualWindSwing(part);

        GlStateManager.rotate(6.0F + swing / 2.0F + height + naturalWindSwing, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sidewaysRotationOffset / 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-sidewaysRotationOffset / 2.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
    }

    float getNatrualWindSwing(int part) {
        long highlightedPart = (System.currentTimeMillis() / 3) % 360;
        float relativePart = (float) (part + 1) / partCount;
        return (float) (Math.sin(Math.toRadians((relativePart) * 360 - (highlightedPart))) * 3);
    }


    /**
     * https://easings.net/#easeOutSine
     *
     * @param x
     * @return
     */
    private static float easeOutSine(float x) {
        return (float) Math.sin((x * Math.PI) / 2f);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

}