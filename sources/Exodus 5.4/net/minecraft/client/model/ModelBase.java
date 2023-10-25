/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public abstract class ModelBase {
    public float swingProgress;
    private Map<String, TextureOffset> modelTextureMap;
    public int textureWidth = 64;
    public int textureHeight = 32;
    public boolean isChild = true;
    public List<ModelRenderer> boxList = Lists.newArrayList();
    public boolean isRiding;

    public void setModelAttributes(ModelBase modelBase) {
        this.swingProgress = modelBase.swingProgress;
        this.isRiding = modelBase.isRiding;
        this.isChild = modelBase.isChild;
    }

    public ModelBase() {
        this.modelTextureMap = Maps.newHashMap();
    }

    public void setLivingAnimations(EntityLivingBase entityLivingBase, float f, float f2, float f3) {
    }

    public void setRotationAngles(float f, float f2, float f3, float f4, float f5, float f6, Entity entity) {
    }

    public static void copyModelAngles(ModelRenderer modelRenderer, ModelRenderer modelRenderer2) {
        modelRenderer2.rotateAngleX = modelRenderer.rotateAngleX;
        modelRenderer2.rotateAngleY = modelRenderer.rotateAngleY;
        modelRenderer2.rotateAngleZ = modelRenderer.rotateAngleZ;
        modelRenderer2.rotationPointX = modelRenderer.rotationPointX;
        modelRenderer2.rotationPointY = modelRenderer.rotationPointY;
        modelRenderer2.rotationPointZ = modelRenderer.rotationPointZ;
    }

    public void render(Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
    }

    public TextureOffset getTextureOffset(String string) {
        return this.modelTextureMap.get(string);
    }

    protected void setTextureOffset(String string, int n, int n2) {
        this.modelTextureMap.put(string, new TextureOffset(n, n2));
    }

    public ModelRenderer getRandomModelBox(Random random) {
        return this.boxList.get(random.nextInt(this.boxList.size()));
    }
}

