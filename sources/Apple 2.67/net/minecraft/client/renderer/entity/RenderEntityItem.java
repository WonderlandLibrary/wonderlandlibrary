package net.minecraft.client.renderer.entity;

import java.util.Random;

import appu26j.Apple;
import appu26j.mods.visuals.ItemPhysics;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderEntityItem extends Render<EntityItem>
{
    private final RenderItem itemRenderer;
    private Random random = new Random();

    public RenderEntityItem(RenderManager renderManagerIn, RenderItem p_i46167_2_)
    {
        super(renderManagerIn);
        this.itemRenderer = p_i46167_2_;
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    private int func_177077_a(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_)
    {
        ItemStack itemstack = itemIn.getEntityItem();
        Item item = itemstack.getItem();

        if (item == null)
        {
            return 0;
        }
        else
        {
            boolean flag = p_177077_9_.isGui3d();
            int i = this.func_177078_a(itemstack);
            float f = 0.25F;
            float f1 = MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F;
            float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25F * f2, (float)p_177077_6_);

            if (flag || this.renderManager.options != null)
            {
                float f3 = (((float)itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * (180F / (float)Math.PI);
                GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
            }

            if (!flag)
            {
                float f6 = -0.0F * (float)(i - 1) * 0.5F;
                float f4 = -0.0F * (float)(i - 1) * 0.5F;
                float f5 = -0.046875F * (float)(i - 1) * 0.5F;
                GlStateManager.translate(f6, f4, f5);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    private int func_177078_a(ItemStack stack)
    {
        int i = 1;

        if (stack.stackSize > 48)
        {
            i = 5;
        }
        else if (stack.stackSize > 32)
        {
            i = 4;
        }
        else if (stack.stackSize > 16)
        {
            i = 3;
        }
        else if (stack.stackSize > 1)
        {
            i = 2;
        }

        return i;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        ItemPhysics itemPhysics = (ItemPhysics) Apple.CLIENT.getModsManager().getMod("Item Physics");
        
        if (itemPhysics.isEnabled() && entity.getEntityItem() != null && this.renderItemPhysics(entity, x, y, z))
        {
            return;
        }
        
        ItemStack itemstack = entity.getEntityItem();
        this.random.setSeed(187L);
        boolean flag = false;

        if (this.bindEntityTexture(entity))
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.itemRenderer.getItemModelMesher().getItemModel(itemstack);
        int i = this.func_177077_a(entity, x, y, z, partialTicks, ibakedmodel);

        for (int j = 0; j < i; ++j)
        {
            if (ibakedmodel.isGui3d())
            {
                GlStateManager.pushMatrix();

                if (j > 0)
                {
                    float f = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(f, f1, f2);
                }

                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            }
            else
            {
                GlStateManager.pushMatrix();
                ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                float f3 = ibakedmodel.getItemCameraTransforms().ground.scale.x;
                float f4 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
                float f5 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
                GlStateManager.translate(0.0F * f3, 0.0F * f4, 0.046875F * f5);
            }
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);

        if (flag)
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private boolean renderItemPhysics(EntityItem entity, double x, double y, double z)
    {
        ItemPhysics.updateLastRenderTime();
        
        if (entity.getAge() == 0)
        {
            return false;
        }
        
        ItemStack itemStack = entity.getEntityItem();
        boolean empty = itemStack.getItem() == null || itemStack.stackSize <= 0;
        this.random.setSeed(empty ? 187L : Item.getIdFromItem(itemStack.getItem()) + itemStack.getItemDamage());
        boolean flag = false;
        
        if (this.bindEntityTexture(entity))
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }
        
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel iBakedModel = this.itemRenderer.getItemModelMesher().getItemModel(itemStack);
        boolean isThreeDimensional = iBakedModel.isGui3d();
        float rotateBy = ItemPhysics.getRotation() * 40.0f;
        
        if (Minecraft.getMinecraft().isGamePaused())
        {
            rotateBy = 0;
        }
        
        if (entity.rotationPitch > 360)
        {
            entity.rotationPitch = 0;
        }
        
        if (!Double.isNaN(entity.posX) && !Double.isNaN(entity.posY) && !Double.isNaN(entity.posZ))
        {
            if (entity.onGround)
            {
                float rotationPitch = entity.rotationPitch;
                
                if (rotationPitch % 90 != 0)
                {
                    double d = Math.abs(rotationPitch), e = Math.abs(rotationPitch - 90), f = Math.abs(rotationPitch - 180), g = Math.abs(rotationPitch - 270);
                    
                    if (d <= e && d <= f && d <= g)
                    {
                        if (entity.rotationPitch < 0)
                        {
                            entity.rotationPitch += rotateBy;
                        }
                        
                        else
                        {
                            entity.rotationPitch -= rotateBy;
                        }
                    }
                    
                    if (e < d && e <= f && e <= g)
                    {
                        if ((entity.rotationPitch - 90) < 0)
                        {
                            entity.rotationPitch += rotateBy;
                        }
                        
                        else
                        {
                            entity.rotationPitch -= rotateBy;
                        }
                    }
                    
                    if (f < e && f < d && f <= g)
                    {
                        if ((entity.rotationPitch - 180) < 0)
                        {
                            entity.rotationPitch += rotateBy;
                        }
                        
                        else
                        {
                            entity.rotationPitch -= rotateBy;
                        }
                    }
                    
                    if (g < e && g < f && g < d)
                    {
                        if ((entity.rotationPitch - 270) < 0)
                        {
                            entity.rotationPitch += rotateBy;
                        }
                        
                        else
                        {
                            entity.rotationPitch -= rotateBy;
                        }
                    }
                }
            }
            
            else
            {
                if (this.isNearWater(entity))
                {
                    rotateBy /= 4.0;
                }
                
                entity.rotationPitch += rotateBy;
            }
        }
        
        GlStateManager.translate(x, y + 0.15, z);
        GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
        GlStateManager.rotate(entity.rotationPitch + 90, 1, 0, 0);
        int modelCount = this.func_177078_a(itemStack);
        ItemCameraTransforms itemCameraTransforms = iBakedModel.getItemCameraTransforms();
        double f1 = itemCameraTransforms.ground.scale.x;
        double f2 = itemCameraTransforms.ground.scale.y;
        double f3 = itemCameraTransforms.ground.scale.z;
        
        if (!isThreeDimensional)
        {
            double f4 = -0.0 * (modelCount - 1) * 0.5 * f1;
            double f5 = -0.0 * (modelCount - 1) * 0.5 * f2;
            double f6 = -0.09375 * (modelCount - 1) * 0.5 * f3;
            GlStateManager.translate(f4, f5, f6);
        }
        
        for (int k = 0; k < modelCount; k++)
        {
            if (isThreeDimensional)
            {
                GlStateManager.pushMatrix();
                
                if (k > 0)
                {
                    double translateX = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    double translateY = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    double translateZ = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(translateX, translateY, translateZ);
                }
                
                GlStateManager.scale(0.5, 0.5, 0.5);
                itemCameraTransforms.applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemStack, iBakedModel);
                GlStateManager.popMatrix();
            }
            
            else
            {
                GlStateManager.pushMatrix();
                itemCameraTransforms.applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemStack, iBakedModel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0, 0, 0.046875 * f3);
            }
        }
        
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);
        
        if (flag)
        {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }
        
        return true;
    }

    private boolean isNearWater(Entity entity)
    {
        if (entity.isInWater())
        {
            return true;
        }
        
        World world = entity.getEntityWorld();
        BlockPos blockPos = entity.getPosition();
        return Block.getIdFromBlock(world.getBlockState(blockPos).getBlock()) == 9 || Block.getIdFromBlock(world.getBlockState(blockPos.down()).getBlock()) == 9;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityItem entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
