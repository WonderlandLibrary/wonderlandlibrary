package me.nyan.flush.event.impl;


import me.nyan.flush.event.Event;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntityModel extends Event {
    private final ModelBase modelBase;
    private final EntityLivingBase entity;
    private final float var1;
    private final float var2;
    private final float var3;
    private final float var4;
    private final float var5;
    private final float scaleFactor;

    public EventRenderEntityModel(ModelBase modelBase, EntityLivingBase entity, float var1, float var2, float var3, float var4, float var5, float scaleFactor) {
        this.modelBase = modelBase;
        this.entity = entity;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.var4 = var4;
        this.var5 = var5;
        this.scaleFactor = scaleFactor;
    }

    public ModelBase getModelBase() {
        return modelBase;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public float getVar1() {
        return var1;
    }

    public float getVar2() {
        return var2;
    }

    public float getVar3() {
        return var3;
    }

    public float getVar4() {
        return var4;
    }

    public float getVar5() {
        return var5;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }
}
