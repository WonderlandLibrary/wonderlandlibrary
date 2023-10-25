/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.combat.KillAura;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import org.lwjgl.opengl.GL11;

public class ItemRenderer {
    private float equippedProgress;
    private final RenderManager renderManager;
    private float prevEquippedProgress;
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private int equippedItemSlot = -1;
    private final RenderItem itemRenderer;
    private ItemStack itemToRender;
    private final Minecraft mc;
    private float delay = 0.0f;
    private TimerUtils rotateTimer = new TimerUtils();
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");

    private void Slide(float f, float f2) {
        float f3 = MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI);
        GlStateManager.translate(-0.05f, -0.0f, 0.35f);
        GlStateManager.rotate(-f3 * 60.0f / 2.0f, -15.0f, -0.0f, 9.0f);
        GlStateManager.rotate(-f3 * 70.0f, 1.0f, -0.4f, -0.0f);
    }

    private void renderRightArm(RenderPlayer renderPlayer) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(64.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-62.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(0.25f, -0.85f, 0.75f);
        renderPlayer.renderRightArm(Minecraft.thePlayer);
        GlStateManager.popMatrix();
    }

    private void func_178095_a(AbstractClientPlayer abstractClientPlayer, float f, float f2) {
        float f3 = -0.3f * MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI);
        float f4 = 0.4f * MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI * 2.0f);
        float f5 = -0.4f * MathHelper.sin(f2 * (float)Math.PI);
        GlStateManager.translate(f3, f4, f5);
        GlStateManager.translate(0.64000005f, -0.6f, -0.71999997f);
        GlStateManager.translate(0.0f, f * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f6 = MathHelper.sin(f2 * f2 * (float)Math.PI);
        float f7 = MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI);
        GlStateManager.rotate(f7 * 70.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f6 * -20.0f, 0.0f, 0.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
        GlStateManager.translate(-1.0f, 3.6f, 3.5f);
        GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(200.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.translate(5.6f, 0.0f, 0.0f);
        Render render = this.renderManager.getEntityRenderObject(Minecraft.thePlayer);
        GlStateManager.disableCull();
        RenderPlayer renderPlayer = (RenderPlayer)render;
        renderPlayer.renderRightArm(Minecraft.thePlayer);
        GlStateManager.enableCull();
    }

    private void renderWaterOverlayTexture(float f) {
        this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        float f2 = Minecraft.thePlayer.getBrightness(f);
        GlStateManager.color(f2, f2, f2, 0.5f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        float f3 = 4.0f;
        float f4 = -1.0f;
        float f5 = 1.0f;
        float f6 = -1.0f;
        float f7 = 1.0f;
        float f8 = -0.5f;
        float f9 = -Minecraft.thePlayer.rotationYaw / 64.0f;
        float f10 = Minecraft.thePlayer.rotationPitch / 64.0f;
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-1.0, -1.0, -0.5).tex(4.0f + f9, 4.0f + f10).endVertex();
        worldRenderer.pos(1.0, -1.0, -0.5).tex(0.0f + f9, 4.0f + f10).endVertex();
        worldRenderer.pos(1.0, 1.0, -0.5).tex(0.0f + f9, 0.0f + f10).endVertex();
        worldRenderer.pos(-1.0, 1.0, -0.5).tex(4.0f + f9, 0.0f + f10).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
    }

    private void func_178109_a(AbstractClientPlayer abstractClientPlayer) {
        int n = Minecraft.theWorld.getCombinedLight(new BlockPos(abstractClientPlayer.posX, abstractClientPlayer.posY + (double)abstractClientPlayer.getEyeHeight(), abstractClientPlayer.posZ), 0);
        float f = n & 0xFFFF;
        float f2 = n >> 16;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f2);
    }

    private float func_178100_c(float f) {
        float f2 = 1.0f - f / 45.0f + 0.1f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        f2 = -MathHelper.cos(f2 * (float)Math.PI) * 0.5f + 0.5f;
        return f2;
    }

    private void func_178103_d() {
        GlStateManager.translate(-1.0f, 0.2f, 0.0f);
        GlStateManager.rotate(30.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(60.0f, 0.0f, 1.0f, 0.0f);
    }

    public void resetEquippedProgress() {
        this.equippedProgress = 0.0f;
    }

    private void monsoon(float f, float f2) {
        GlStateManager.translate(0.26f, -0.18f, -0.71999997f);
        GlStateManager.translate(0.0f, f * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f3 = MathHelper.sin(f2 * f2 * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI);
        GlStateManager.rotate(f3 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f4 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.2f, 0.2f, 0.2f);
    }

    private boolean isBlockTranslucent(Block block) {
        return block != null && block.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }

    private void renderItemMap(AbstractClientPlayer abstractClientPlayer, float f, float f2, float f3) {
        float f4 = -0.4f * MathHelper.sin(MathHelper.sqrt_float(f3) * (float)Math.PI);
        float f5 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(f3) * (float)Math.PI * 2.0f);
        float f6 = -0.2f * MathHelper.sin(f3 * (float)Math.PI);
        GlStateManager.translate(f4, f5, f6);
        float f7 = this.func_178100_c(f);
        GlStateManager.translate(0.0f, 0.04f, -0.72f);
        GlStateManager.translate(0.0f, f2 * -1.2f, 0.0f);
        GlStateManager.translate(0.0f, f7 * -0.5f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f7 * -85.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        this.renderPlayerArms(abstractClientPlayer);
        float f8 = MathHelper.sin(f3 * f3 * (float)Math.PI);
        float f9 = MathHelper.sin(MathHelper.sqrt_float(f3) * (float)Math.PI);
        GlStateManager.rotate(f8 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f9 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f9 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.38f, 0.38f, 0.38f);
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-1.0f, -1.0f, 0.0f);
        GlStateManager.scale(0.015625f, 0.015625f, 0.015625f);
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f((float)0.0f, (float)0.0f, (float)-1.0f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
        worldRenderer.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
        worldRenderer.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
        worldRenderer.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        MapData mapData = Items.filled_map.getMapData(this.itemToRender, Minecraft.theWorld);
        if (mapData != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapData, false);
        }
    }

    private void transformFirstPersonItem(float f, float f2) {
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByName("Descale").getValBoolean();
        if (!bl) {
            GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        } else {
            GlStateManager.translate(0.56f, -0.42f, -0.71999997f);
        }
        GlStateManager.translate(0.0f, f * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        float f3 = MathHelper.sin(f2 * f2 * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI);
        GlStateManager.rotate(f3 * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f4 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f4 * -80.0f, 1.0f, 0.0f, 0.0f);
        if (!bl) {
            GlStateManager.scale(0.4f, 0.4f, 0.4f);
        } else {
            GlStateManager.scale(0.25f, 0.25f, 0.25f);
        }
    }

    private void func_178105_d(float f) {
        float f2 = -0.4f * MathHelper.sin(MathHelper.sqrt_float(f) * (float)Math.PI);
        float f3 = 0.2f * MathHelper.sin(MathHelper.sqrt_float(f) * (float)Math.PI * 2.0f);
        float f4 = -0.2f * MathHelper.sin(f * (float)Math.PI);
        GlStateManager.translate(f2, f3, f4);
    }

    private void func_178104_a(AbstractClientPlayer abstractClientPlayer, float f) {
        float f2 = (float)abstractClientPlayer.getItemInUseCount() - f + 1.0f;
        float f3 = f2 / (float)this.itemToRender.getMaxItemUseDuration();
        float f4 = MathHelper.abs(MathHelper.cos(f2 / 4.0f * (float)Math.PI) * 0.1f);
        if (f3 >= 0.8f) {
            f4 = 0.0f;
        }
        GlStateManager.translate(0.0f, f4, 0.0f);
        float f5 = 1.0f - (float)Math.pow(f3, 27.0);
        GlStateManager.translate(f5 * 0.6f, f5 * -0.5f, f5 * 0.0f);
        GlStateManager.rotate(f5 * 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f5 * 10.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f5 * 30.0f, 0.0f, 0.0f, 1.0f);
    }

    private void Matt(float f, float f2) {
        GlStateManager.translate(0.56f, -0.52f, -0.71999997f);
        GlStateManager.translate(0.0f, f * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI) * -35.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f, 0.4f, 0.4f);
    }

    private void Simple(float f, float f2) {
        float f3 = f2 * 0.78f - f2 * f2 * 0.78f;
        GlStateManager.scale(1.7f, 1.4f, 1.7f);
        GlStateManager.rotate(48.0f, 0.0f, -1.0f, 0.0f);
        GlStateManager.translate(-0.3f, 0.4f, 0.0f);
        GlStateManager.translate(0.0f, 0.08f, 0.0f);
        GlStateManager.translate(0.56f, -0.489f, -0.71999997f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(52.0f, 0.0f, 180.0f + f3 * 0.5f, f3 * 20.0f);
        float f4 = MathHelper.sin(f2 * f2 * (float)Math.PI);
        float f5 = MathHelper.sin(MathHelper.sqrt_float(f2) * (float)Math.PI);
        GlStateManager.rotate(f5 * -51.3f, 2.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, -0.2f, 0.0f);
    }

    private void renderLeftArm(RenderPlayer renderPlayer) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(41.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-0.3f, -1.1f, 0.45f);
        renderPlayer.renderLeftArm(Minecraft.thePlayer);
        GlStateManager.popMatrix();
    }

    private void func_178110_a(EntityPlayerSP entityPlayerSP, float f) {
        float f2 = entityPlayerSP.prevRenderArmPitch + (entityPlayerSP.renderArmPitch - entityPlayerSP.prevRenderArmPitch) * f;
        float f3 = entityPlayerSP.prevRenderArmYaw + (entityPlayerSP.renderArmYaw - entityPlayerSP.prevRenderArmYaw) * f;
        GlStateManager.rotate((entityPlayerSP.rotationPitch - f2) * 0.1f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate((entityPlayerSP.rotationYaw - f3) * 0.1f, 0.0f, 1.0f, 0.0f);
    }

    private void func_178108_a(float f, TextureAtlasSprite textureAtlasSprite) {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        float f2 = 0.1f;
        GlStateManager.color(0.1f, 0.1f, 0.1f, 0.5f);
        GlStateManager.pushMatrix();
        float f3 = -1.0f;
        float f4 = 1.0f;
        float f5 = -1.0f;
        float f6 = 1.0f;
        float f7 = -0.5f;
        float f8 = textureAtlasSprite.getMinU();
        float f9 = textureAtlasSprite.getMaxU();
        float f10 = textureAtlasSprite.getMinV();
        float f11 = textureAtlasSprite.getMaxV();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-1.0, -1.0, -0.5).tex(f9, f11).endVertex();
        worldRenderer.pos(1.0, -1.0, -0.5).tex(f8, f11).endVertex();
        worldRenderer.pos(1.0, 1.0, -0.5).tex(f8, f10).endVertex();
        worldRenderer.pos(-1.0, 1.0, -0.5).tex(f9, f10).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void renderItemInFirstPerson(float f) {
        float f2 = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * f);
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        float f3 = entityPlayerSP.getSwingProgress(f);
        float f4 = entityPlayerSP.prevRotationPitch + (entityPlayerSP.rotationPitch - entityPlayerSP.prevRotationPitch) * f;
        float f5 = entityPlayerSP.prevRotationYaw + (entityPlayerSP.rotationYaw - entityPlayerSP.prevRotationYaw) * f;
        this.func_178101_a(f4, f5);
        this.func_178109_a(entityPlayerSP);
        this.func_178110_a(entityPlayerSP, f);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        if (this.itemToRender != null) {
            if (this.itemToRender.getItem() == Items.filled_map) {
                this.renderItemMap(entityPlayerSP, f4, f2, f3);
            } else if (entityPlayerSP.getItemInUseCount() > 0) {
                EnumAction enumAction = this.itemToRender.getItemUseAction();
                String string = Exodus.INSTANCE.settingsManager.getSettingByName("Block Animation").getValString();
                BlockPos blockPos = new BlockPos(Minecraft.thePlayer);
                float f6 = 1.0f - this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * f;
                float f7 = Minecraft.thePlayer.getSwingProgress(f);
                switch (enumAction) {
                    case NONE: {
                        this.transformFirstPersonItem(f2, 0.0f);
                        break;
                    }
                    case EAT: 
                    case DRINK: {
                        this.func_178104_a(entityPlayerSP, f);
                        this.transformFirstPersonItem(f2, 0.0f);
                        break;
                    }
                    case BLOCK: {
                        float f8;
                        if (string.equalsIgnoreCase("Test1")) {
                            this.transformFirstPersonItem(f2 - 0.1f, f3);
                            break;
                        }
                        if (string.equalsIgnoreCase("Test2")) {
                            this.monsoon(f6 / 25.0f, 1.0f);
                            GlStateManager.scale(4.0f, 2.0f, 2.0f);
                            this.func_178103_d();
                            f8 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI);
                            GlStateManager.translate(1.0, -0.8, 1.0);
                            GlStateManager.rotate(-f8 * 50.0f / 2.0f, -8.0f, 0.4f, 9.0f);
                            GlStateManager.rotate(-f8 * 40.0f, 1.5f, -0.4f, -0.0f);
                        }
                        if (string.equalsIgnoreCase("Exodus")) {
                            this.transformFirstPersonItem(f2, 0.0f);
                            GlStateManager.scale(1.25f, 1.25f, 1.25f);
                            this.func_178103_d();
                            f8 = MathHelper.sin((float)((double)MathHelper.sqrt_float(f3) * Math.PI));
                            GlStateManager.translate((double)0.8f, -0.2, 1.0);
                            GlStateManager.rotate(-f8 * 50.0f / 2.0f, -8.0f, 0.4f, 9.0f);
                            GlStateManager.rotate(-f8 * 40.0f, 1.5f, -0.4f, -0.0f);
                            break;
                        }
                        if (string.equalsIgnoreCase("Simple")) {
                            this.Matt(-0.3f, f3);
                            this.func_178103_d();
                            break;
                        }
                        if (string.equalsIgnoreCase("1.7")) {
                            this.transformFirstPersonItem(-0.15f, f3);
                            GlStateManager.scale(0.75f, 0.75f, 0.75f);
                            this.func_178103_d();
                            GlStateManager.translate(-0.7f, 0.3f, 0.0f);
                            break;
                        }
                        if (string.equalsIgnoreCase("Exhibition")) {
                            f8 = MathHelper.sin((float)((double)MathHelper.sqrt_float(f3) * 3.0));
                            this.transformFirstPersonItem(f2, 0.0f);
                            GlStateManager.translate(0.8f, 0.4f, -0.1f);
                            GL11.glRotated((double)(-f8 * 20.0f), (double)(f8 / 2.0f), (double)0.0, (double)9.0);
                            GL11.glRotated((double)(-f8 * 50.0f), (double)0.8f, (double)(f8 / 2.0f), (double)0.0);
                            this.func_178103_d();
                        }
                        if (string.equalsIgnoreCase("Astolfo")) {
                            this.transformFirstPersonItem(f2, 0.0f);
                            GlStateManager.scale(2.0f, 2.0f, 2.0f);
                            this.func_178103_d();
                            f8 = MathHelper.sin((float)((double)MathHelper.sqrt_float(f3) * Math.PI));
                            GlStateManager.translate((double)0.8f, -0.8, 1.0);
                            GlStateManager.rotate(-f8 * 50.0f / 2.0f, -8.0f, 0.4f, 9.0f);
                            GlStateManager.rotate(-f8 * 40.0f, 1.5f, -0.4f, -0.0f);
                            break;
                        }
                        if (string.equalsIgnoreCase("Push")) {
                            this.transformFirstPersonItem(0.01f, f3 - 1.0f);
                            this.func_178103_d();
                            GlStateManager.translate(-0.3f, 0.3f, 0.0f);
                            break;
                        }
                        if (string.equalsIgnoreCase("Meme")) {
                            this.transformFirstPersonItem(0.01f, f3 * 20.0f);
                            this.func_178103_d();
                            GlStateManager.translate(-0.7f, 0.3f, 0.0f);
                            break;
                        }
                    }
                    case BOW: {
                        this.transformFirstPersonItem(f2, 0.0f);
                        this.func_178098_a(f, entityPlayerSP);
                    }
                }
            } else {
                EnumAction enumAction = this.itemToRender.getItemUseAction();
                String string = Exodus.INSTANCE.settingsManager.getSettingByName("Block Animation").getValString();
                BlockPos blockPos = new BlockPos(Minecraft.thePlayer);
                float f9 = 1.0f - this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * f;
                float f10 = Minecraft.thePlayer.getSwingProgress(f);
                if (Exodus.INSTANCE.moduleManager.getModuleByClass(KillAura.class).isToggled() && Exodus.INSTANCE.settingsManager.getSettingByName("Block Mode").getValString().equalsIgnoreCase("Fake") | Exodus.INSTANCE.settingsManager.getSettingByName("Block Mode").getValString().equalsIgnoreCase("Hypixel") && Minecraft.thePlayer.getHeldItem() != null && Minecraft.thePlayer.getHeldItem().getItem() != null && Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword && KillAura.target != null) {
                    float f11;
                    if (string.equalsIgnoreCase("Test1")) {
                        this.transformFirstPersonItem(f2 - 0.4f, f3);
                    }
                    if (string.equalsIgnoreCase("Test2")) {
                        this.monsoon(f9 / 25.0f, 1.0f);
                        GlStateManager.scale(4.0f, 2.0f, 2.0f);
                        this.func_178103_d();
                        f11 = MathHelper.sin(MathHelper.sqrt_float(f10) * (float)Math.PI);
                        GlStateManager.translate(1.0, -0.8, 1.0);
                        GlStateManager.rotate(-f11 * 50.0f / 2.0f, -8.0f, 0.4f, 9.0f);
                        GlStateManager.rotate(-f11 * 40.0f, 1.5f, -0.4f, -0.0f);
                    }
                    if (string.equalsIgnoreCase("Exodus")) {
                        this.transformFirstPersonItem(f2, 0.0f);
                        GlStateManager.scale(1.25f, 1.25f, 1.25f);
                        this.func_178103_d();
                        f11 = MathHelper.sin((float)((double)MathHelper.sqrt_float(f3) * Math.PI));
                        GlStateManager.translate((double)0.8f, -0.2, 1.0);
                        GlStateManager.rotate(-f11 * 50.0f / 2.0f, -8.0f, 0.4f, 9.0f);
                        GlStateManager.rotate(-f11 * 40.0f, 1.5f, -0.4f, -0.0f);
                    }
                    if (string.equalsIgnoreCase("Simple")) {
                        this.Matt(-0.3f, f3);
                        this.func_178103_d();
                    }
                    if (string.equalsIgnoreCase("1.7")) {
                        this.transformFirstPersonItem(-0.15f, f3);
                        GlStateManager.scale(0.75f, 0.75f, 0.75f);
                        this.func_178103_d();
                        GlStateManager.translate(-0.7f, 0.3f, 0.0f);
                    }
                    if (string.equalsIgnoreCase("Exhibition")) {
                        f11 = MathHelper.sin((float)((double)MathHelper.sqrt_float(f3) * 3.0));
                        this.transformFirstPersonItem(f2, 0.0f);
                        GlStateManager.translate(0.8f, 0.4f, -0.1f);
                        GL11.glRotated((double)(-f11 * 20.0f), (double)(f11 / 2.0f), (double)0.0, (double)9.0);
                        GL11.glRotated((double)(-f11 * 50.0f), (double)0.8f, (double)(f11 / 2.0f), (double)0.0);
                        this.func_178103_d();
                    }
                    if (string.equalsIgnoreCase("Astolfo")) {
                        this.transformFirstPersonItem(f2, 0.0f);
                        GlStateManager.scale(2.0f, 2.0f, 2.0f);
                        this.func_178103_d();
                        f11 = MathHelper.sin((float)((double)MathHelper.sqrt_float(f3) * Math.PI));
                        GlStateManager.translate((double)0.8f, -0.8, 1.0);
                        GlStateManager.rotate(-f11 * 50.0f / 2.0f, -8.0f, 0.4f, 9.0f);
                        GlStateManager.rotate(-f11 * 40.0f, 1.5f, -0.4f, -0.0f);
                    }
                    if (string.equalsIgnoreCase("Push")) {
                        this.transformFirstPersonItem(0.01f, f3 - 1.0f);
                        this.func_178103_d();
                        GlStateManager.translate(-0.3f, 0.3f, 0.0f);
                    }
                    if (string.equalsIgnoreCase("Meme")) {
                        this.transformFirstPersonItem(0.01f, f3 * 20.0f);
                        this.func_178103_d();
                        GlStateManager.translate(-0.7f, 0.3f, 0.0f);
                    }
                } else {
                    this.func_178105_d(f3);
                    this.transformFirstPersonItem(f2, f3);
                }
            }
            this.renderItem(entityPlayerSP, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!entityPlayerSP.isInvisible()) {
            this.func_178095_a(entityPlayerSP, f2, f3);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    public void renderItem(EntityLivingBase entityLivingBase, ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
        if (itemStack != null) {
            Item item = itemStack.getItem();
            Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            if (this.itemRenderer.shouldRenderItemIn3D(itemStack)) {
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                if (this.isBlockTranslucent(block)) {
                    GlStateManager.depthMask(false);
                }
            }
            this.itemRenderer.renderItemModelForEntity(itemStack, entityLivingBase, transformType);
            if (this.isBlockTranslucent(block)) {
                GlStateManager.depthMask(true);
            }
            GlStateManager.popMatrix();
        }
    }

    public void renderOverlays(float f) {
        GlStateManager.disableAlpha();
        if (Minecraft.thePlayer.isEntityInsideOpaqueBlock()) {
            IBlockState iBlockState = Minecraft.theWorld.getBlockState(new BlockPos(Minecraft.thePlayer));
            EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
            int n = 0;
            while (n < 8) {
                double d = entityPlayerSP.posX + (double)(((float)((n >> 0) % 2) - 0.5f) * entityPlayerSP.width * 0.8f);
                double d2 = entityPlayerSP.posY + (double)(((float)((n >> 1) % 2) - 0.5f) * 0.1f);
                double d3 = entityPlayerSP.posZ + (double)(((float)((n >> 2) % 2) - 0.5f) * entityPlayerSP.width * 0.8f);
                BlockPos blockPos = new BlockPos(d, d2 + (double)entityPlayerSP.getEyeHeight(), d3);
                IBlockState iBlockState2 = Minecraft.theWorld.getBlockState(blockPos);
                if (iBlockState2.getBlock().isVisuallyOpaque()) {
                    iBlockState = iBlockState2;
                }
                ++n;
            }
            if (iBlockState.getBlock().getRenderType() != -1) {
                this.func_178108_a(f, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iBlockState));
            }
        }
        if (!Minecraft.thePlayer.isSpectator()) {
            if (Minecraft.thePlayer.isInsideOfMaterial(Material.water)) {
                this.renderWaterOverlayTexture(f);
            }
            if (Minecraft.thePlayer.isBurning()) {
                this.renderFireInFirstPerson(f);
            }
        }
        GlStateManager.enableAlpha();
    }

    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        ItemStack itemStack = entityPlayerSP.inventory.getCurrentItem();
        boolean bl = false;
        if (this.itemToRender != null && itemStack != null) {
            if (!this.itemToRender.getIsItemStackEqual(itemStack)) {
                bl = true;
            }
        } else {
            bl = this.itemToRender != null || itemStack != null;
        }
        float f = 0.4f;
        float f2 = bl ? 0.0f : 1.0f;
        float f3 = MathHelper.clamp_float(f2 - this.equippedProgress, -f, f);
        this.equippedProgress += f3;
        if (this.equippedProgress < 0.1f) {
            this.itemToRender = itemStack;
            this.equippedItemSlot = entityPlayerSP.inventory.currentItem;
        }
    }

    private void func_178098_a(float f, AbstractClientPlayer abstractClientPlayer) {
        GlStateManager.rotate(-18.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-12.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-8.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-0.9f, 0.2f, 0.0f);
        float f2 = (float)this.itemToRender.getMaxItemUseDuration() - ((float)abstractClientPlayer.getItemInUseCount() - f + 1.0f);
        float f3 = f2 / 20.0f;
        f3 = (f3 * f3 + f3 * 2.0f) / 3.0f;
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        if (f3 > 0.1f) {
            float f4 = MathHelper.sin((f2 - 0.1f) * 1.3f);
            float f5 = f3 - 0.1f;
            float f6 = f4 * f5;
            GlStateManager.translate(f6 * 0.0f, f6 * 0.01f, f6 * 0.0f);
        }
        GlStateManager.translate(f3 * 0.0f, f3 * 0.0f, f3 * 0.1f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f + f3 * 0.2f);
    }

    public void resetEquippedProgress2() {
        this.equippedProgress = 0.0f;
    }

    public ItemRenderer(Minecraft minecraft) {
        this.mc = minecraft;
        this.renderManager = minecraft.getRenderManager();
        this.itemRenderer = minecraft.getRenderItem();
    }

    private void func_178101_a(float f, float f2) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void renderFireInFirstPerson(float f) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.9f);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        float f2 = 1.0f;
        int n = 0;
        while (n < 2) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureAtlasSprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float f3 = textureAtlasSprite.getMinU();
            float f4 = textureAtlasSprite.getMaxU();
            float f5 = textureAtlasSprite.getMinV();
            float f6 = textureAtlasSprite.getMaxV();
            float f7 = (0.0f - f2) / 2.0f;
            float f8 = f7 + f2;
            float f9 = 0.0f - f2 / 2.0f;
            float f10 = f9 + f2;
            float f11 = -0.5f;
            GlStateManager.translate((float)(-(n * 2 - 1)) * 0.24f, -0.3f, 0.0f);
            GlStateManager.rotate((float)(n * 2 - 1) * 10.0f, 0.0f, 1.0f, 0.0f);
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(f7, f9, f11).tex(f4, f6).endVertex();
            worldRenderer.pos(f8, f9, f11).tex(f3, f6).endVertex();
            worldRenderer.pos(f8, f10, f11).tex(f3, f5).endVertex();
            worldRenderer.pos(f7, f10, f11).tex(f4, f5).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            ++n;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    private void renderPlayerArms(AbstractClientPlayer abstractClientPlayer) {
        this.mc.getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
        Render render = this.renderManager.getEntityRenderObject(Minecraft.thePlayer);
        RenderPlayer renderPlayer = (RenderPlayer)render;
        if (!abstractClientPlayer.isInvisible()) {
            GlStateManager.disableCull();
            this.renderRightArm(renderPlayer);
            this.renderLeftArm(renderPlayer);
            GlStateManager.enableCull();
        }
    }
}

