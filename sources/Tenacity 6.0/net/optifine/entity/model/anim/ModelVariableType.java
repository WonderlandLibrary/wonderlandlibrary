// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model.anim;

import net.minecraft.src.Config;
import net.minecraft.client.model.ModelRenderer;

public enum ModelVariableType
{
    POS_X("tx"), 
    POS_Y("ty"), 
    POS_Z("tz"), 
    ANGLE_X("rx"), 
    ANGLE_Y("ry"), 
    ANGLE_Z("rz"), 
    OFFSET_X("ox"), 
    OFFSET_Y("oy"), 
    OFFSET_Z("oz"), 
    SCALE_X("sx"), 
    SCALE_Y("sy"), 
    SCALE_Z("sz");
    
    private String name;
    public static ModelVariableType[] VALUES;
    
    private ModelVariableType(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public float getFloat(final ModelRenderer mr) {
        switch (this) {
            case POS_X: {
                return mr.rotationPointX;
            }
            case POS_Y: {
                return mr.rotationPointY;
            }
            case POS_Z: {
                return mr.rotationPointZ;
            }
            case ANGLE_X: {
                return mr.rotateAngleX;
            }
            case ANGLE_Y: {
                return mr.rotateAngleY;
            }
            case ANGLE_Z: {
                return mr.rotateAngleZ;
            }
            case OFFSET_X: {
                return mr.offsetX;
            }
            case OFFSET_Y: {
                return mr.offsetY;
            }
            case OFFSET_Z: {
                return mr.offsetZ;
            }
            case SCALE_X: {
                return mr.scaleX;
            }
            case SCALE_Y: {
                return mr.scaleY;
            }
            case SCALE_Z: {
                return mr.scaleZ;
            }
            default: {
                Config.warn("GetFloat not supported for: " + this);
                return 0.0f;
            }
        }
    }
    
    public void setFloat(final ModelRenderer mr, final float val) {
        switch (this) {
            case POS_X: {
                mr.rotationPointX = val;
            }
            case POS_Y: {
                mr.rotationPointY = val;
            }
            case POS_Z: {
                mr.rotationPointZ = val;
            }
            case ANGLE_X: {
                mr.rotateAngleX = val;
            }
            case ANGLE_Y: {
                mr.rotateAngleY = val;
            }
            case ANGLE_Z: {
                mr.rotateAngleZ = val;
            }
            case OFFSET_X: {
                mr.offsetX = val;
            }
            case OFFSET_Y: {
                mr.offsetY = val;
            }
            case OFFSET_Z: {
                mr.offsetZ = val;
            }
            case SCALE_X: {
                mr.scaleX = val;
            }
            case SCALE_Y: {
                mr.scaleY = val;
            }
            case SCALE_Z: {
                mr.scaleZ = val;
            }
            default: {
                Config.warn("SetFloat not supported for: " + this);
            }
        }
    }
    
    public static ModelVariableType parse(final String str) {
        for (int i = 0; i < ModelVariableType.VALUES.length; ++i) {
            final ModelVariableType modelvariabletype = ModelVariableType.VALUES[i];
            if (modelvariabletype.getName().equals(str)) {
                return modelvariabletype;
            }
        }
        return null;
    }
    
    static {
        ModelVariableType.VALUES = values();
    }
}
