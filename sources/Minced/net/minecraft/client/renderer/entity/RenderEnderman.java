// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderman;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityEnderman;

public class RenderEnderman extends RenderLiving<EntityEnderman>
{
    private static final ResourceLocation ENDERMAN_TEXTURES;
    private final Random rnd;
    
    public RenderEnderman(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelEnderman(0.0f), 0.5f);
        this.rnd = new Random();
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerEndermanEyes(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldBlock(this));
    }
    
    @Override
    public ModelEnderman getMainModel() {
        return (ModelEnderman)super.getMainModel();
    }
    
    @Override
    public void doRender(final EntityEnderman entity, double x, final double y, double z, final float entityYaw, final float partialTicks) {
        final IBlockState iblockstate = entity.getHeldBlockState();
        final ModelEnderman modelenderman = this.getMainModel();
        modelenderman.isCarrying = (iblockstate != null);
        modelenderman.isAttacking = entity.isScreaming();
        if (entity.isScreaming()) {
            final double d0 = 0.02;
            x += this.rnd.nextGaussian() * 0.02;
            z += this.rnd.nextGaussian() * 0.02;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityEnderman entity) {
        return RenderEnderman.ENDERMAN_TEXTURES;
    }
    
    static {
        ENDERMAN_TEXTURES = new ResourceLocation("textures/entity/enderman/enderman.png");
    }
}
