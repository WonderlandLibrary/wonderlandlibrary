package net.optifine.shaders;

import net.minecraft.util.EnumWorldBlockLayer;
import net.optifine.shaders.ProgramStage;
import net.optifine.shaders.config.EnumShaderOption;
import net.optifine.texture.TextureType;

static class Shaders.1 {
    static final /* synthetic */ int[] $SwitchMap$net$optifine$shaders$config$EnumShaderOption;
    static final /* synthetic */ int[] $SwitchMap$net$optifine$texture$TextureType;
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$util$EnumWorldBlockLayer;
    static final /* synthetic */ int[] $SwitchMap$net$optifine$shaders$ProgramStage;

    static {
        $SwitchMap$net$optifine$shaders$ProgramStage = new int[ProgramStage.values().length];
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$ProgramStage[ProgramStage.GBUFFERS.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$ProgramStage[ProgramStage.DEFERRED.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$ProgramStage[ProgramStage.COMPOSITE.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$ProgramStage[ProgramStage.SHADOW.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$net$minecraft$util$EnumWorldBlockLayer = new int[EnumWorldBlockLayer.values().length];
        try {
            Shaders.1.$SwitchMap$net$minecraft$util$EnumWorldBlockLayer[EnumWorldBlockLayer.SOLID.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$minecraft$util$EnumWorldBlockLayer[EnumWorldBlockLayer.CUTOUT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$minecraft$util$EnumWorldBlockLayer[EnumWorldBlockLayer.CUTOUT_MIPPED.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$minecraft$util$EnumWorldBlockLayer[EnumWorldBlockLayer.TRANSLUCENT.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$net$optifine$texture$TextureType = new int[TextureType.values().length];
        try {
            Shaders.1.$SwitchMap$net$optifine$texture$TextureType[TextureType.TEXTURE_1D.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$texture$TextureType[TextureType.TEXTURE_2D.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$texture$TextureType[TextureType.TEXTURE_3D.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$texture$TextureType[TextureType.TEXTURE_RECTANGLE.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        $SwitchMap$net$optifine$shaders$config$EnumShaderOption = new int[EnumShaderOption.values().length];
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.ANTIALIASING.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.NORMAL_MAP.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.SPECULAR_MAP.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.RENDER_RES_MUL.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.SHADOW_RES_MUL.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.HAND_DEPTH_MUL.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.CLOUD_SHADOW.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.OLD_HAND_LIGHT.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.OLD_LIGHTING.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.SHADER_PACK.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.TWEAK_BLOCK_DAMAGE.ordinal()] = 11;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.SHADOW_CLIP_FRUSTRUM.ordinal()] = 12;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.TEX_MIN_FIL_B.ordinal()] = 13;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.TEX_MIN_FIL_N.ordinal()] = 14;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.TEX_MIN_FIL_S.ordinal()] = 15;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_B.ordinal()] = 16;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_N.ordinal()] = 17;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Shaders.1.$SwitchMap$net$optifine$shaders$config$EnumShaderOption[EnumShaderOption.TEX_MAG_FIL_S.ordinal()] = 18;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
