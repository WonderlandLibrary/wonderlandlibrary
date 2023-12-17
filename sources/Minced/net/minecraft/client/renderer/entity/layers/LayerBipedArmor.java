// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.model.ModelBiped;

public class LayerBipedArmor extends LayerArmorBase<ModelBiped>
{
    public LayerBipedArmor(final RenderLivingBase<?> rendererIn) {
        super(rendererIn);
    }
    
    @Override
    protected void initArmor() {
        this.modelLeggings = (T)new ModelBiped(0.5f);
        this.modelArmor = (T)new ModelBiped(1.0f);
    }
    
    @Override
    protected void setModelSlotVisible(final ModelBiped p_188359_1_, final EntityEquipmentSlot slotIn) {
        this.setModelVisible(p_188359_1_);
        switch (slotIn) {
            case HEAD: {
                p_188359_1_.bipedHead.showModel = true;
                p_188359_1_.bipedHeadwear.showModel = true;
                break;
            }
            case CHEST: {
                p_188359_1_.bipedBody.showModel = true;
                p_188359_1_.bipedRightArm.showModel = true;
                p_188359_1_.bipedLeftArm.showModel = true;
                break;
            }
            case LEGS: {
                p_188359_1_.bipedBody.showModel = true;
                p_188359_1_.bipedRightLeg.showModel = true;
                p_188359_1_.bipedLeftLeg.showModel = true;
                break;
            }
            case FEET: {
                p_188359_1_.bipedRightLeg.showModel = true;
                p_188359_1_.bipedLeftLeg.showModel = true;
                break;
            }
        }
    }
    
    protected void setModelVisible(final ModelBiped model) {
        model.setVisible(false);
    }
}
