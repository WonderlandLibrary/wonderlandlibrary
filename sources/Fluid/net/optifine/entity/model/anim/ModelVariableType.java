// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

import net.minecraft.src.Config;
import net.minecraft.client.model.ModelRenderer;

public enum ModelVariableType
{
    POS_X("POS_X", 0, "tx"), 
    POS_Y("POS_Y", 1, "ty"), 
    POS_Z("POS_Z", 2, "tz"), 
    ANGLE_X("ANGLE_X", 3, "rx"), 
    ANGLE_Y("ANGLE_Y", 4, "ry"), 
    ANGLE_Z("ANGLE_Z", 5, "rz"), 
    OFFSET_X("OFFSET_X", 6, "ox"), 
    OFFSET_Y("OFFSET_Y", 7, "oy"), 
    OFFSET_Z("OFFSET_Z", 8, "oz"), 
    SCALE_X("SCALE_X", 9, "sx"), 
    SCALE_Y("SCALE_Y", 10, "sy"), 
    SCALE_Z("SCALE_Z", 11, "sz");
    
    private String name;
    public static ModelVariableType[] VALUES;
    
    static {
        ModelVariableType.VALUES = values();
    }
    
    private ModelVariableType(final String s, final int n, final String name) {
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
                return;
            }
            case POS_Y: {
                mr.rotationPointY = val;
                return;
            }
            case POS_Z: {
                mr.rotationPointZ = val;
                return;
            }
            case ANGLE_X: {
                mr.rotateAngleX = val;
                return;
            }
            case ANGLE_Y: {
                mr.rotateAngleY = val;
                return;
            }
            case ANGLE_Z: {
                mr.rotateAngleZ = val;
                return;
            }
            case OFFSET_X: {
                mr.offsetX = val;
                return;
            }
            case OFFSET_Y: {
                mr.offsetY = val;
                return;
            }
            case OFFSET_Z: {
                mr.offsetZ = val;
                return;
            }
            case SCALE_X: {
                mr.scaleX = val;
                return;
            }
            case SCALE_Y: {
                mr.scaleY = val;
                return;
            }
            case SCALE_Z: {
                mr.scaleZ = val;
                return;
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
}
