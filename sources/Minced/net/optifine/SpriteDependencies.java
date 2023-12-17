// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import java.util.Iterator;
import net.optifine.reflect.ReflectorForge;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.List;

public class SpriteDependencies
{
    private static int countDependenciesTotal;
    
    public static TextureAtlasSprite resolveDependencies(final List<TextureAtlasSprite> listRegisteredSprites, final int ix, final TextureMap textureMap) {
        TextureAtlasSprite textureatlassprite;
        for (textureatlassprite = listRegisteredSprites.get(ix); resolveOne(listRegisteredSprites, ix, textureatlassprite, textureMap); textureatlassprite = listRegisteredSprites.get(ix)) {}
        textureatlassprite.isDependencyParent = false;
        return textureatlassprite;
    }
    
    private static boolean resolveOne(final List<TextureAtlasSprite> listRegisteredSprites, final int ix, final TextureAtlasSprite sprite, final TextureMap textureMap) {
        int i = 0;
        for (final ResourceLocation resourcelocation : sprite.getDependencies()) {
            Config.detail("Sprite dependency: " + sprite.getIconName() + " <- " + resourcelocation);
            ++SpriteDependencies.countDependenciesTotal;
            TextureAtlasSprite textureatlassprite = textureMap.getRegisteredSprite(resourcelocation);
            if (textureatlassprite == null) {
                textureatlassprite = textureMap.registerSprite(resourcelocation);
            }
            else {
                final int j = listRegisteredSprites.indexOf(textureatlassprite);
                if (j <= ix + i) {
                    continue;
                }
                if (textureatlassprite.isDependencyParent) {
                    final String s = "circular dependency: " + sprite.getIconName() + " -> " + textureatlassprite.getIconName();
                    final ResourceLocation resourcelocation2 = textureMap.getResourceLocation(sprite);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation2, s);
                    break;
                }
                listRegisteredSprites.remove(j);
            }
            sprite.isDependencyParent = true;
            listRegisteredSprites.add(ix + i, textureatlassprite);
            ++i;
        }
        return i > 0;
    }
    
    public static void reset() {
        SpriteDependencies.countDependenciesTotal = 0;
    }
    
    public static int getCountDependencies() {
        return SpriteDependencies.countDependenciesTotal;
    }
}
