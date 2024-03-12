// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.model.ModelBiped;

public class LayerBipedArmor extends LayerArmorBase<ModelBiped>
{
    public LayerBipedArmor(final RendererLivingEntity<?> rendererIn) {
        super(rendererIn);
    }
    
    @Override
    protected void initArmor() {
        this.modelLeggings = (T)new ModelBiped(0.5f);
        this.modelArmor = (T)new ModelBiped(1.0f);
    }
    
    @Override
    protected void setModelPartVisible(final ModelBiped model, final int armorSlot) {
        this.setModelVisible(model);
        switch (armorSlot) {
            case 1: {
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                break;
            }
            case 2: {
                model.bipedBody.showModel = true;
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                break;
            }
            case 3: {
                model.bipedBody.showModel = true;
                model.bipedRightArm.showModel = true;
                model.bipedLeftArm.showModel = true;
                break;
            }
            case 4: {
                model.bipedHead.showModel = true;
                model.bipedHeadwear.showModel = true;
                break;
            }
        }
    }
    
    protected void setModelVisible(final ModelBiped model) {
        model.setInvisible(false);
    }
}
