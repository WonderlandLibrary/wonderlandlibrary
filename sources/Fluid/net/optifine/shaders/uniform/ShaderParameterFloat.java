// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.shaders.uniform;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.src.Config;
import net.optifine.shaders.Shaders;

public enum ShaderParameterFloat
{
    BIOME("BIOME", 0, "biome"), 
    TEMPERATURE("TEMPERATURE", 1, "temperature"), 
    RAINFALL("RAINFALL", 2, "rainfall"), 
    HELD_ITEM_ID("HELD_ITEM_ID", 3, (ShaderUniformBase)Shaders.uniform_heldItemId), 
    HELD_BLOCK_LIGHT_VALUE("HELD_BLOCK_LIGHT_VALUE", 4, (ShaderUniformBase)Shaders.uniform_heldBlockLightValue), 
    HELD_ITEM_ID2("HELD_ITEM_ID2", 5, (ShaderUniformBase)Shaders.uniform_heldItemId2), 
    HELD_BLOCK_LIGHT_VALUE2("HELD_BLOCK_LIGHT_VALUE2", 6, (ShaderUniformBase)Shaders.uniform_heldBlockLightValue2), 
    WORLD_TIME("WORLD_TIME", 7, (ShaderUniformBase)Shaders.uniform_worldTime), 
    WORLD_DAY("WORLD_DAY", 8, (ShaderUniformBase)Shaders.uniform_worldDay), 
    MOON_PHASE("MOON_PHASE", 9, (ShaderUniformBase)Shaders.uniform_moonPhase), 
    FRAME_COUNTER("FRAME_COUNTER", 10, (ShaderUniformBase)Shaders.uniform_frameCounter), 
    FRAME_TIME("FRAME_TIME", 11, (ShaderUniformBase)Shaders.uniform_frameTime), 
    FRAME_TIME_COUNTER("FRAME_TIME_COUNTER", 12, (ShaderUniformBase)Shaders.uniform_frameTimeCounter), 
    SUN_ANGLE("SUN_ANGLE", 13, (ShaderUniformBase)Shaders.uniform_sunAngle), 
    SHADOW_ANGLE("SHADOW_ANGLE", 14, (ShaderUniformBase)Shaders.uniform_shadowAngle), 
    RAIN_STRENGTH("RAIN_STRENGTH", 15, (ShaderUniformBase)Shaders.uniform_rainStrength), 
    ASPECT_RATIO("ASPECT_RATIO", 16, (ShaderUniformBase)Shaders.uniform_aspectRatio), 
    VIEW_WIDTH("VIEW_WIDTH", 17, (ShaderUniformBase)Shaders.uniform_viewWidth), 
    VIEW_HEIGHT("VIEW_HEIGHT", 18, (ShaderUniformBase)Shaders.uniform_viewHeight), 
    NEAR("NEAR", 19, (ShaderUniformBase)Shaders.uniform_near), 
    FAR("FAR", 20, (ShaderUniformBase)Shaders.uniform_far), 
    WETNESS("WETNESS", 21, (ShaderUniformBase)Shaders.uniform_wetness), 
    EYE_ALTITUDE("EYE_ALTITUDE", 22, (ShaderUniformBase)Shaders.uniform_eyeAltitude), 
    EYE_BRIGHTNESS("EYE_BRIGHTNESS", 23, (ShaderUniformBase)Shaders.uniform_eyeBrightness, new String[] { "x", "y" }), 
    TERRAIN_TEXTURE_SIZE("TERRAIN_TEXTURE_SIZE", 24, (ShaderUniformBase)Shaders.uniform_terrainTextureSize, new String[] { "x", "y" }), 
    TERRRAIN_ICON_SIZE("TERRRAIN_ICON_SIZE", 25, (ShaderUniformBase)Shaders.uniform_terrainIconSize), 
    IS_EYE_IN_WATER("IS_EYE_IN_WATER", 26, (ShaderUniformBase)Shaders.uniform_isEyeInWater), 
    NIGHT_VISION("NIGHT_VISION", 27, (ShaderUniformBase)Shaders.uniform_nightVision), 
    BLINDNESS("BLINDNESS", 28, (ShaderUniformBase)Shaders.uniform_blindness), 
    SCREEN_BRIGHTNESS("SCREEN_BRIGHTNESS", 29, (ShaderUniformBase)Shaders.uniform_screenBrightness), 
    HIDE_GUI("HIDE_GUI", 30, (ShaderUniformBase)Shaders.uniform_hideGUI), 
    CENTER_DEPT_SMOOTH("CENTER_DEPT_SMOOTH", 31, (ShaderUniformBase)Shaders.uniform_centerDepthSmooth), 
    ATLAS_SIZE("ATLAS_SIZE", 32, (ShaderUniformBase)Shaders.uniform_atlasSize, new String[] { "x", "y" }), 
    CAMERA_POSITION("CAMERA_POSITION", 33, (ShaderUniformBase)Shaders.uniform_cameraPosition, new String[] { "x", "y", "z" }), 
    PREVIOUS_CAMERA_POSITION("PREVIOUS_CAMERA_POSITION", 34, (ShaderUniformBase)Shaders.uniform_previousCameraPosition, new String[] { "x", "y", "z" }), 
    SUN_POSITION("SUN_POSITION", 35, (ShaderUniformBase)Shaders.uniform_sunPosition, new String[] { "x", "y", "z" }), 
    MOON_POSITION("MOON_POSITION", 36, (ShaderUniformBase)Shaders.uniform_moonPosition, new String[] { "x", "y", "z" }), 
    SHADOW_LIGHT_POSITION("SHADOW_LIGHT_POSITION", 37, (ShaderUniformBase)Shaders.uniform_shadowLightPosition, new String[] { "x", "y", "z" }), 
    UP_POSITION("UP_POSITION", 38, (ShaderUniformBase)Shaders.uniform_upPosition, new String[] { "x", "y", "z" }), 
    SKY_COLOR("SKY_COLOR", 39, (ShaderUniformBase)Shaders.uniform_skyColor, new String[] { "r", "g", "b" }), 
    GBUFFER_PROJECTION("GBUFFER_PROJECTION", 40, (ShaderUniformBase)Shaders.uniform_gbufferProjection, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    GBUFFER_PROJECTION_INVERSE("GBUFFER_PROJECTION_INVERSE", 41, (ShaderUniformBase)Shaders.uniform_gbufferProjectionInverse, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    GBUFFER_PREVIOUS_PROJECTION("GBUFFER_PREVIOUS_PROJECTION", 42, (ShaderUniformBase)Shaders.uniform_gbufferPreviousProjection, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    GBUFFER_MODEL_VIEW("GBUFFER_MODEL_VIEW", 43, (ShaderUniformBase)Shaders.uniform_gbufferModelView, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    GBUFFER_MODEL_VIEW_INVERSE("GBUFFER_MODEL_VIEW_INVERSE", 44, (ShaderUniformBase)Shaders.uniform_gbufferModelViewInverse, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    GBUFFER_PREVIOUS_MODEL_VIEW("GBUFFER_PREVIOUS_MODEL_VIEW", 45, (ShaderUniformBase)Shaders.uniform_gbufferPreviousModelView, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    SHADOW_PROJECTION("SHADOW_PROJECTION", 46, (ShaderUniformBase)Shaders.uniform_shadowProjection, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    SHADOW_PROJECTION_INVERSE("SHADOW_PROJECTION_INVERSE", 47, (ShaderUniformBase)Shaders.uniform_shadowProjectionInverse, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    SHADOW_MODEL_VIEW("SHADOW_MODEL_VIEW", 48, (ShaderUniformBase)Shaders.uniform_shadowModelView, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" }), 
    SHADOW_MODEL_VIEW_INVERSE("SHADOW_MODEL_VIEW_INVERSE", 49, (ShaderUniformBase)Shaders.uniform_shadowModelViewInverse, new String[] { "0", "1", "2", "3" }, new String[] { "0", "1", "2", "3" });
    
    private String name;
    private ShaderUniformBase uniform;
    private String[] indexNames1;
    private String[] indexNames2;
    
    private ShaderParameterFloat(final String s, final int n, final String name) {
        this.name = name;
    }
    
    private ShaderParameterFloat(final String s, final int n, final ShaderUniformBase uniform) {
        this.name = uniform.getName();
        this.uniform = uniform;
        if (!instanceOf(uniform, ShaderUniform1f.class, ShaderUniform1i.class)) {
            throw new IllegalArgumentException("Invalid uniform type for enum: " + this + ", uniform: " + uniform.getClass().getName());
        }
    }
    
    private ShaderParameterFloat(final String s, final int n, final ShaderUniformBase uniform, final String[] indexNames1) {
        this.name = uniform.getName();
        this.uniform = uniform;
        this.indexNames1 = indexNames1;
        if (!instanceOf(uniform, ShaderUniform2i.class, ShaderUniform2f.class, ShaderUniform3f.class, ShaderUniform4f.class)) {
            throw new IllegalArgumentException("Invalid uniform type for enum: " + this + ", uniform: " + uniform.getClass().getName());
        }
    }
    
    private ShaderParameterFloat(final String s, final int n, final ShaderUniformBase uniform, final String[] indexNames1, final String[] indexNames2) {
        this.name = uniform.getName();
        this.uniform = uniform;
        this.indexNames1 = indexNames1;
        this.indexNames2 = indexNames2;
        if (!instanceOf(uniform, ShaderUniformM4.class)) {
            throw new IllegalArgumentException("Invalid uniform type for enum: " + this + ", uniform: " + uniform.getClass().getName());
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public ShaderUniformBase getUniform() {
        return this.uniform;
    }
    
    public String[] getIndexNames1() {
        return this.indexNames1;
    }
    
    public String[] getIndexNames2() {
        return this.indexNames2;
    }
    
    public float eval(final int index1, final int index2) {
        if (this.indexNames1 != null && (index1 < 0 || index1 > this.indexNames1.length)) {
            Config.warn("Invalid index1, parameter: " + this + ", index: " + index1);
            return 0.0f;
        }
        if (this.indexNames2 != null && (index2 < 0 || index2 > this.indexNames2.length)) {
            Config.warn("Invalid index2, parameter: " + this + ", index: " + index2);
            return 0.0f;
        }
        switch (this) {
            case BIOME: {
                final BlockPos blockpos2 = Shaders.getCameraPosition();
                final BiomeGenBase biomegenbase2 = Shaders.getCurrentWorld().getBiomeGenForCoords(blockpos2);
                return (float)biomegenbase2.biomeID;
            }
            case TEMPERATURE: {
                final BlockPos blockpos3 = Shaders.getCameraPosition();
                final BiomeGenBase biomegenbase3 = Shaders.getCurrentWorld().getBiomeGenForCoords(blockpos3);
                return (biomegenbase3 != null) ? biomegenbase3.getFloatTemperature(blockpos3) : 0.0f;
            }
            case RAINFALL: {
                final BlockPos pos = Shaders.getCameraPosition();
                final BiomeGenBase biome = Shaders.getCurrentWorld().getBiomeGenForCoords(pos);
                return (biome != null) ? biome.getFloatRainfall() : 0.0f;
            }
            default: {
                if (this.uniform instanceof ShaderUniform1f) {
                    return ((ShaderUniform1f)this.uniform).getValue();
                }
                if (this.uniform instanceof ShaderUniform1i) {
                    return (float)((ShaderUniform1i)this.uniform).getValue();
                }
                if (this.uniform instanceof ShaderUniform2i) {
                    return (float)((ShaderUniform2i)this.uniform).getValue()[index1];
                }
                if (this.uniform instanceof ShaderUniform2f) {
                    return ((ShaderUniform2f)this.uniform).getValue()[index1];
                }
                if (this.uniform instanceof ShaderUniform3f) {
                    return ((ShaderUniform3f)this.uniform).getValue()[index1];
                }
                if (this.uniform instanceof ShaderUniform4f) {
                    return ((ShaderUniform4f)this.uniform).getValue()[index1];
                }
                if (this.uniform instanceof ShaderUniformM4) {
                    return ((ShaderUniformM4)this.uniform).getValue(index1, index2);
                }
                throw new IllegalArgumentException("Unknown uniform type: " + this);
            }
        }
    }
    
    private static boolean instanceOf(final Object obj, final Class... classes) {
        if (obj == null) {
            return false;
        }
        final Class oclass = obj.getClass();
        for (int i = 0; i < classes.length; ++i) {
            final Class oclass2 = classes[i];
            if (oclass2.isAssignableFrom(oclass)) {
                return true;
            }
        }
        return false;
    }
}
