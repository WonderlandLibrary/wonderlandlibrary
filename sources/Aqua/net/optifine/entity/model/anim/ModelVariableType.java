package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.src.Config;
import net.optifine.entity.model.anim.ModelVariableType;

public enum ModelVariableType {
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

    private ModelVariableType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public float getFloat(ModelRenderer mr) {
        switch (1.$SwitchMap$net$optifine$entity$model$anim$ModelVariableType[this.ordinal()]) {
            case 1: {
                return mr.rotationPointX;
            }
            case 2: {
                return mr.rotationPointY;
            }
            case 3: {
                return mr.rotationPointZ;
            }
            case 4: {
                return mr.rotateAngleX;
            }
            case 5: {
                return mr.rotateAngleY;
            }
            case 6: {
                return mr.rotateAngleZ;
            }
            case 7: {
                return mr.offsetX;
            }
            case 8: {
                return mr.offsetY;
            }
            case 9: {
                return mr.offsetZ;
            }
            case 10: {
                return mr.scaleX;
            }
            case 11: {
                return mr.scaleY;
            }
            case 12: {
                return mr.scaleZ;
            }
        }
        Config.warn((String)("GetFloat not supported for: " + (Object)((Object)this)));
        return 0.0f;
    }

    public void setFloat(ModelRenderer mr, float val) {
        switch (1.$SwitchMap$net$optifine$entity$model$anim$ModelVariableType[this.ordinal()]) {
            case 1: {
                mr.rotationPointX = val;
                return;
            }
            case 2: {
                mr.rotationPointY = val;
                return;
            }
            case 3: {
                mr.rotationPointZ = val;
                return;
            }
            case 4: {
                mr.rotateAngleX = val;
                return;
            }
            case 5: {
                mr.rotateAngleY = val;
                return;
            }
            case 6: {
                mr.rotateAngleZ = val;
                return;
            }
            case 7: {
                mr.offsetX = val;
                return;
            }
            case 8: {
                mr.offsetY = val;
                return;
            }
            case 9: {
                mr.offsetZ = val;
                return;
            }
            case 10: {
                mr.scaleX = val;
                return;
            }
            case 11: {
                mr.scaleY = val;
                return;
            }
            case 12: {
                mr.scaleZ = val;
                return;
            }
        }
        Config.warn((String)("SetFloat not supported for: " + (Object)((Object)this)));
    }

    public static ModelVariableType parse(String str) {
        for (int i = 0; i < VALUES.length; ++i) {
            ModelVariableType modelvariabletype = VALUES[i];
            if (!modelvariabletype.getName().equals((Object)str)) continue;
            return modelvariabletype;
        }
        return null;
    }

    static {
        VALUES = ModelVariableType.values();
    }
}
