/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;

public class LayerBipedArmor
extends LayerArmorBase<ModelBiped> {
    @Override
    protected void func_177179_a(ModelBiped modelBiped, int n) {
        this.func_177194_a(modelBiped);
        switch (n) {
            case 1: {
                modelBiped.bipedRightLeg.showModel = true;
                modelBiped.bipedLeftLeg.showModel = true;
                break;
            }
            case 2: {
                modelBiped.bipedBody.showModel = true;
                modelBiped.bipedRightLeg.showModel = true;
                modelBiped.bipedLeftLeg.showModel = true;
                break;
            }
            case 3: {
                modelBiped.bipedBody.showModel = true;
                modelBiped.bipedRightArm.showModel = true;
                modelBiped.bipedLeftArm.showModel = true;
                break;
            }
            case 4: {
                modelBiped.bipedHead.showModel = true;
                modelBiped.bipedHeadwear.showModel = true;
            }
        }
    }

    public LayerBipedArmor(RendererLivingEntity<?> rendererLivingEntity) {
        super(rendererLivingEntity);
    }

    protected void func_177194_a(ModelBiped modelBiped) {
        modelBiped.setInvisible(false);
    }

    @Override
    protected void initArmor() {
        this.field_177189_c = new ModelBiped(0.5f);
        this.field_177186_d = new ModelBiped(1.0f);
    }
}

