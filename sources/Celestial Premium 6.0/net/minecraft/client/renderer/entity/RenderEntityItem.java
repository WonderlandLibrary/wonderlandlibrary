/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.ItemPhysics;
import org.lwjgl.opengl.GL11;

public class RenderEntityItem
extends Render<EntityItem> {
    private final RenderItem itemRenderer;
    private final Random random = new Random();
    public static Random randomm = new Random();
    public static Minecraft mc = Minecraft.getMinecraft();
    public static RenderItem renderItem = mc.getRenderItem();
    public static long tick;
    public static double rotation;

    public RenderEntityItem(RenderManager renderManagerIn, RenderItem p_i46167_2_) {
        super(renderManagerIn);
        this.itemRenderer = p_i46167_2_;
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }

    private int transformModelCount(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_) {
        ItemStack itemstack = itemIn.getItem();
        Item item = itemstack.getItem();
        if (item == null) {
            return 0;
        }
        boolean flag = p_177077_9_.isGui3d();
        int i = this.getModelCount(itemstack);
        float f = 0.25f;
        float f1 = Celestial.instance.featureManager.getFeatureByClass(ItemPhysics.class).getState() ? 0.0f : MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0f + itemIn.hoverStart) * 0.1f + 0.1f;
        GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
        float f2 = p_177077_9_.getItemCameraTransforms().getTransform((ItemCameraTransforms.TransformType)ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25f * f2, (float)p_177077_6_);
        if (!Celestial.instance.featureManager.getFeatureByClass(ItemPhysics.class).getState() && (flag || this.renderManager.options != null)) {
            float f3 = (((float)itemIn.getAge() + p_177077_8_) / 20.0f + itemIn.hoverStart) * 57.295776f;
            GlStateManager.rotate(f3, 0.0f, 1.0f, 0.0f);
        }
        this.shadowSize = Celestial.instance.featureManager.getFeatureByClass(ItemPhysics.class).getState() ? 0.0f : 0.15f;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return i;
    }

    private int getModelCount(ItemStack stack) {
        int i = 1;
        if (stack.getCount() > 48) {
            i = 5;
        } else if (stack.getCount() > 32) {
            i = 4;
        } else if (stack.getCount() > 16) {
            i = 3;
        } else if (stack.getCount() > 1) {
            i = 2;
        }
        return i;
    }

    public static boolean shouldSpreadItems() {
        return true;
    }

    @Override
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!Celestial.instance.featureManager.getFeatureByClass(ItemPhysics.class).getState()) {
            ItemStack itemstack = entity.getItem();
            int i = itemstack.isEmpty() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
            this.random.setSeed(i);
            boolean flag = false;
            if (this.bindEntityTexture(entity)) {
                this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
                flag = true;
            }
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.world, null);
            int j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
            float f = ibakedmodel.getItemCameraTransforms().ground.scale.x;
            float f1 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
            float f2 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
            boolean flag1 = ibakedmodel.isGui3d();
            if (!flag1) {
                float f3 = -0.0f * (float)(j - 1) * 0.5f * f;
                float f4 = -0.0f * (float)(j - 1) * 0.5f * f1;
                float f5 = -0.09375f * (float)(j - 1) * 0.5f * f2;
                GlStateManager.translate(f3, f4, f5);
            }
            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            }
            for (int k = 0; k < j; ++k) {
                if (flag1) {
                    GlStateManager.pushMatrix();
                    if (k > 0) {
                        float f7 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                        float f9 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                        float f6 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                        GlStateManager.translate(f7, f9, f6);
                    }
                    ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                    this.itemRenderer.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                    continue;
                }
                GlStateManager.pushMatrix();
                if (k > 0) {
                    float f8 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    float f10 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    GlStateManager.translate(f8, f10, 0.0f);
                }
                ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0f * f, 0.0f * f1, 0.09375f * f2);
            }
            if (this.renderOutlines) {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            this.bindEntityTexture(entity);
            if (flag) {
                this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
            }
        } else {
            ItemStack itemstack;
            rotation = (double)(System.nanoTime() - tick) / 3000000.0 * (double)ItemPhysics.physicsSpeed.getCurrentValue();
            if (!RenderEntityItem.mc.inGameHasFocus) {
                rotation = 0.0;
            }
            if ((itemstack = entity.getItem()).getItem() != null) {
                randomm.setSeed(187L);
                boolean flag2 = false;
                if (TextureMap.LOCATION_BLOCKS_TEXTURE != null) {
                    RenderEntityItem.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    RenderEntityItem.mc.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                    flag2 = true;
                }
                GlStateManager.enableRescaleNormal();
                GlStateManager.alphaFunc(516, 0.1f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(itemstack);
                int k = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
                BlockPos pos = new BlockPos(entity);
                if (entity.rotationPitch > 360.0f) {
                    entity.rotationPitch = 0.0f;
                }
                if (!(entity == null || Double.isNaN(entity.getAge()) || Double.isNaN(entity.getAir()) || Double.isNaN(entity.getEntityId()) || entity.getPosition() == null || entity.onGround)) {
                    BlockPos posUp = new BlockPos(entity);
                    posUp.add(0, 1, 0);
                    Material m1 = entity.world.getBlockState(posUp).getBlock().getMaterial(null);
                    Material m2 = entity.world.getBlockState(pos).getBlock().getMaterial(null);
                    boolean m3 = entity.isInsideOfMaterial(Material.WATER);
                    boolean m4 = entity.isInWater();
                    entity.rotationPitch = m3 | m1 == Material.WATER | m2 == Material.WATER | m4 ? (entity.rotationPitch += (float)(rotation / 4.0)) : (entity.rotationPitch += (float)(rotation * 2.0));
                }
                GL11.glRotatef(entity.rotationYaw, 0.0f, 1.0f, 0.0f);
                GL11.glRotatef(entity.rotationPitch + 90.0f, 1.0f, 0.0f, 0.0f);
                for (int l = 0; l < k; ++l) {
                    if (ibakedmodel.isAmbientOcclusion()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.scale(0.3f, 0.3f, 0.3f);
                        renderItem.renderItem(itemstack, ibakedmodel);
                        GlStateManager.popMatrix();
                        continue;
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.6f, 0.6f, 0.6f);
                    if (l > 0 && RenderEntityItem.shouldSpreadItems()) {
                        GlStateManager.translate(0.0f, 0.0f, 0.046875f * (float)l);
                    }
                    renderItem.renderItem(itemstack, ibakedmodel);
                    if (!RenderEntityItem.shouldSpreadItems()) {
                        GlStateManager.translate(0.0f, 0.0f, 0.046875f);
                    }
                    GlStateManager.popMatrix();
                }
                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                RenderEntityItem.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                if (flag2) {
                    RenderEntityItem.mc.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
                }
            }
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityItem entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}

