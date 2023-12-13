// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import com.elementars.eclient.event.Event;

public class EventPostRenderLayers extends Event
{
    public RenderLivingBase renderer;
    public ModelBase modelBase;
    public EntityLivingBase entity;
    public float limbSwing;
    public float limbSwingAmount;
    public float partialTicks;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;
    public float scaleIn;
    
    public RenderLivingBase getRenderer() {
        return this.renderer;
    }
    
    public EventPostRenderLayers(final RenderLivingBase renderer, final ModelBase modelBase, final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleIn) {
        this.renderer = renderer;
        this.modelBase = modelBase;
        this.entity = entitylivingbaseIn;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.partialTicks = partialTicks;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scaleIn = scaleIn;
    }
    
    public ModelBase getModelBase() {
        return this.modelBase;
    }
    
    public EntityLivingBase getEntitylivingbaseIn() {
        return this.entity;
    }
    
    public float getLimbSwing() {
        return this.limbSwing;
    }
    
    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }
    
    @Override
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public float getAgeInTicks() {
        return this.ageInTicks;
    }
    
    public float getNetHeadYaw() {
        return this.netHeadYaw;
    }
    
    public float getHeadPitch() {
        return this.headPitch;
    }
    
    public float getScaleIn() {
        return this.scaleIn;
    }
}
