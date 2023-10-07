// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.model;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;

public abstract class ModelBase
{
    public float swingProgress;
    public boolean isRiding;
    public boolean isChild;
    public List boxList;
    private Map modelTextureMap;
    public int textureWidth;
    public int textureHeight;
    private static final String __OBFID = "CL_00000845";
    
    public ModelBase() {
        this.isChild = true;
        this.boxList = Lists.newArrayList();
        this.modelTextureMap = Maps.newHashMap();
        this.textureWidth = 64;
        this.textureHeight = 32;
    }
    
    public void render(final Entity p_78088_1_, final float p_78088_2_, final float p_78088_3_, final float p_78088_4_, final float p_78088_5_, final float p_78088_6_, final float p_78088_7_) {
    }
    
    public void setRotationAngles(final float p_78087_1_, final float p_78087_2_, final float p_78087_3_, final float p_78087_4_, final float p_78087_5_, final float p_78087_6_, final Entity p_78087_7_) {
    }
    
    public void setLivingAnimations(final EntityLivingBase p_78086_1_, final float p_78086_2_, final float p_78086_3_, final float p_78086_4_) {
    }
    
    public ModelRenderer getRandomModelBox(final Random p_85181_1_) {
        return this.boxList.get(p_85181_1_.nextInt(this.boxList.size()));
    }
    
    protected void setTextureOffset(final String p_78085_1_, final int p_78085_2_, final int p_78085_3_) {
        this.modelTextureMap.put(p_78085_1_, new TextureOffset(p_78085_2_, p_78085_3_));
    }
    
    public TextureOffset getTextureOffset(final String p_78084_1_) {
        return this.modelTextureMap.get(p_78084_1_);
    }
    
    public static void func_178685_a(final ModelRenderer p_178685_0_, final ModelRenderer p_178685_1_) {
        p_178685_1_.rotateAngleX = p_178685_0_.rotateAngleX;
        p_178685_1_.rotateAngleY = p_178685_0_.rotateAngleY;
        p_178685_1_.rotateAngleZ = p_178685_0_.rotateAngleZ;
        p_178685_1_.rotationPointX = p_178685_0_.rotationPointX;
        p_178685_1_.rotationPointY = p_178685_0_.rotationPointY;
        p_178685_1_.rotationPointZ = p_178685_0_.rotationPointZ;
    }
    
    public void setModelAttributes(final ModelBase p_178686_1_) {
        this.swingProgress = p_178686_1_.swingProgress;
        this.isRiding = p_178686_1_.isRiding;
        this.isChild = p_178686_1_.isChild;
    }
}
