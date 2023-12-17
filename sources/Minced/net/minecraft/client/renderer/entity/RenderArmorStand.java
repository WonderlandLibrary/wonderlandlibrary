// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityArmorStand;

public class RenderArmorStand extends RenderLivingBase<EntityArmorStand>
{
    public static final ResourceLocation TEXTURE_ARMOR_STAND;
    
    public RenderArmorStand(final RenderManager manager) {
        super(manager, new ModelArmorStand(), 0.0f);
        final LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.modelLeggings = (T)new ModelArmorStandArmor(0.5f);
                this.modelArmor = (T)new ModelArmorStandArmor(1.0f);
            }
        };
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(layerbipedarmor);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerElytra(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityArmorStand entity) {
        return RenderArmorStand.TEXTURE_ARMOR_STAND;
    }
    
    @Override
    public ModelArmorStand getMainModel() {
        return (ModelArmorStand)super.getMainModel();
    }
    
    @Override
    protected void applyRotations(final EntityArmorStand entityLiving, final float ageInTicks, final float rotationYaw, final float partialTicks) {
        GlStateManager.rotate(180.0f - rotationYaw, 0.0f, 1.0f, 0.0f);
        final float f = entityLiving.world.getTotalWorldTime() - entityLiving.punchCooldown + partialTicks;
        if (f < 5.0f) {
            GlStateManager.rotate(MathHelper.sin(f / 1.5f * 3.1415927f) * 3.0f, 0.0f, 1.0f, 0.0f);
        }
    }
    
    @Override
    protected boolean canRenderName(final EntityArmorStand entity) {
        return entity.getAlwaysRenderNameTag();
    }
    
    @Override
    public void doRender(final EntityArmorStand entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (entity.hasMarker()) {
            this.renderMarker = true;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.hasMarker()) {
            this.renderMarker = false;
        }
    }
    
    static {
        TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");
    }
}
