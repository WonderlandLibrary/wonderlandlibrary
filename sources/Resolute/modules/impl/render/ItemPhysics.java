// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import net.minecraft.client.Minecraft;
import vip.Resolute.modules.Module;

public class ItemPhysics extends Module
{
    public static boolean enabled;
    public static Minecraft mc;
    public static long tick;
    public static double rotation;
    public static Random random;
    
    public ItemPhysics() {
        super("ItemPhysics", 0, "Realistic item animations", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        ItemPhysics.enabled = true;
    }
    
    @Override
    public void onDisable() {
        ItemPhysics.enabled = false;
    }
    
    public static ResourceLocation getEntityTexture() {
        return TextureMap.locationBlocksTexture;
    }
    
    public static void doRender(final RenderEntityItem renderer, final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        ItemPhysics.rotation = (System.nanoTime() - ItemPhysics.tick) / 2500000.0 * 0.0010000000474974513;
        if (!ItemPhysics.mc.inGameHasFocus) {
            ItemPhysics.rotation = 0.0;
        }
        final EntityItem item = (EntityItem)entity;
        final ItemStack itemstack = item.getEntityItem();
        int i;
        if (itemstack != null && itemstack.getItem() != null) {
            i = Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
        }
        else {
            i = 187;
        }
        ItemPhysics.random.setSeed(i);
        final boolean flag = true;
        renderer.bindTexture(getEntityTexture());
        renderer.getRenderManager().renderEngine.getTexture(getEntityTexture()).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = ItemPhysics.mc.getRenderItem().getItemModelMesher().getItemModel(itemstack);
        final boolean flag2 = ibakedmodel.isGui3d();
        final boolean is3D = ibakedmodel.isGui3d();
        final int j = getModelCount(itemstack);
        final float f = 0.25f;
        final float f2 = 0.0f;
        final float f3 = ibakedmodel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float)x, (float)y, (float)z);
        if (ibakedmodel.isGui3d()) {
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
        }
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(item.rotationYaw, 0.0f, 0.0f, 1.0f);
        if (is3D) {
            GlStateManager.translate(0.0, 0.0, -0.08);
        }
        else {
            GlStateManager.translate(0.0, 0.0, -0.04);
        }
        Label_0448: {
            if (!is3D) {
                ItemPhysics.mc.getRenderManager();
                if (RenderManager.options == null) {
                    break Label_0448;
                }
            }
            if (is3D) {
                if (!item.onGround) {
                    final EntityItem entityItem3;
                    final EntityItem entityItem = entityItem3 = item;
                    entityItem3.rotationPitch += (float)ItemPhysics.rotation;
                }
            }
            else if (item != null && !Double.isNaN(item.posX) && !Double.isNaN(item.posY) && !Double.isNaN(item.posZ) && item.worldObj != null) {
                if (item.onGround) {
                    item.rotationPitch = 0.0f;
                }
                else {
                    ItemPhysics.rotation *= 2.0;
                    final EntityItem entityItem4;
                    final EntityItem entityItem2 = entityItem4 = item;
                    entityItem4.rotationPitch += (float)ItemPhysics.rotation;
                }
            }
            GlStateManager.rotate(item.rotationPitch, 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int k = 0; k < j; ++k) {
            if (flag2) {
                GlStateManager.pushMatrix();
                if (k > 0) {
                    final float f4 = (ItemPhysics.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f5 = (ItemPhysics.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f6 = (ItemPhysics.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(f4, f5, f6);
                }
                ibakedmodel = handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND);
                ItemPhysics.mc.getRenderItem().renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            }
            else {
                GlStateManager.pushMatrix();
                if (k > 0) {}
                ibakedmodel = handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND);
                ItemPhysics.mc.getRenderItem().renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0f, 0.0f, 0.05375f);
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        renderer.bindTexture(getEntityTexture());
        renderer.getRenderManager().renderEngine.getTexture(getEntityTexture()).restoreLastBlurMipmap();
    }
    
    public static int getModelCount(final ItemStack stack) {
        int i = 1;
        if (stack.stackSize > 48) {
            i = 5;
        }
        else if (stack.stackSize > 32) {
            i = 4;
        }
        else if (stack.stackSize > 16) {
            i = 3;
        }
        else if (stack.stackSize > 1) {
            i = 2;
        }
        return i;
    }
    
    public static IBakedModel handleCameraTransforms(final IBakedModel model, final ItemCameraTransforms.TransformType cameraTransformType) {
        model.getItemCameraTransforms().applyTransform(cameraTransformType);
        return model;
    }
    
    static {
        ItemPhysics.enabled = false;
        ItemPhysics.rotation = 0.0;
        ItemPhysics.mc = Minecraft.getMinecraft();
        ItemPhysics.random = new Random();
    }
}
