package net.optifine;

import java.util.List;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.reflect.ReflectorForge;

public class SpriteDependencies
{
    private static int countDependenciesTotal;

    public static TextureAtlasSprite resolveDependencies(List<TextureAtlasSprite> listRegisteredSprites, int ix, TextureMap textureMap)
    {
        TextureAtlasSprite textureatlassprite;

        for (textureatlassprite = listRegisteredSprites.get(ix); resolveOne(listRegisteredSprites, ix, textureatlassprite, textureMap); textureatlassprite = listRegisteredSprites.get(ix))
        {
            ;
        }

        textureatlassprite.isDependencyParent = false;
        return textureatlassprite;
    }

    private static boolean resolveOne(List<TextureAtlasSprite> listRegisteredSprites, int ix, TextureAtlasSprite sprite, TextureMap textureMap)
    {
        int i = 0;

        for (ResourceLocation resourcelocation : sprite.getDependencies())
        {
            Config.detail("Sprite dependency: " + sprite.getIconName() + " <- " + resourcelocation);
            ++countDependenciesTotal;
            TextureAtlasSprite textureatlassprite = textureMap.getRegisteredSprite(resourcelocation);

            if (textureatlassprite == null)
            {
                textureatlassprite = textureMap.registerSprite(resourcelocation);
            }
            else
            {
                int j = listRegisteredSprites.indexOf(textureatlassprite);

                if (j <= ix + i)
                {
                    continue;
                }

                if (textureatlassprite.isDependencyParent)
                {
                    String s = "circular dependency: " + sprite.getIconName() + " -> " + textureatlassprite.getIconName();
                    ResourceLocation resourcelocation1 = textureMap.getResourceLocation(sprite);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation1, s);
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

    public static void reset()
    {
        countDependenciesTotal = 0;
    }

    public static int getCountDependencies()
    {
        return countDependenciesTotal;
    }
}
