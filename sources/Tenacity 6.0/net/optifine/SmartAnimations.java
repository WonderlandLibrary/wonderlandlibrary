// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;
import net.optifine.shaders.Shaders;
import java.util.BitSet;

public class SmartAnimations
{
    private static boolean active;
    private static BitSet spritesRendered;
    private static BitSet texturesRendered;
    
    public static boolean isActive() {
        return SmartAnimations.active && !Shaders.isShadowPass;
    }
    
    public static void update() {
        SmartAnimations.active = Config.getGameSettings().ofSmartAnimations;
    }
    
    public static void spriteRendered(final int animationIndex) {
        if (animationIndex >= 0) {
            SmartAnimations.spritesRendered.set(animationIndex);
        }
    }
    
    public static void spritesRendered(final BitSet animationIndexes) {
        if (animationIndexes != null) {
            SmartAnimations.spritesRendered.or(animationIndexes);
        }
    }
    
    public static boolean isSpriteRendered(final int animationIndex) {
        return animationIndex >= 0 && SmartAnimations.spritesRendered.get(animationIndex);
    }
    
    public static void resetSpritesRendered() {
        SmartAnimations.spritesRendered.clear();
    }
    
    public static void textureRendered(final int textureId) {
        if (textureId >= 0) {
            SmartAnimations.texturesRendered.set(textureId);
        }
    }
    
    public static boolean isTextureRendered(final int texId) {
        return texId >= 0 && SmartAnimations.texturesRendered.get(texId);
    }
    
    public static void resetTexturesRendered() {
        SmartAnimations.texturesRendered.clear();
    }
    
    static {
        SmartAnimations.spritesRendered = new BitSet();
        SmartAnimations.texturesRendered = new BitSet();
    }
}
