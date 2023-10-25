// Decompiled with: CFR 0.152
// Class Version: 8
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
import ru.smertnix.celestial.Celestial;

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
        float f1 =  MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0f + itemIn.hoverStart) * 0.1f + 0.1f;
        GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
        float f2 = p_177077_9_.getItemCameraTransforms().getTransform((ItemCameraTransforms.TransformType)ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25f * f2, (float)p_177077_6_);
        this.shadowSize = 0.15f;
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
            ItemStack itemstack = entity.getItem();
            int i = itemstack.func_190926_b() ? 187 : Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
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
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityItem entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
