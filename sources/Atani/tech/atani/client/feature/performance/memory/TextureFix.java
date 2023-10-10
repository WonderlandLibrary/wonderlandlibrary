package tech.atani.client.feature.performance.memory;

import java.text.*;
import net.minecraft.client.*;
import java.lang.reflect.*;
import java.awt.image.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.data.*;
import net.minecraft.util.*;
import net.minecraft.client.resources.*;
import java.util.*;

public class TextureFix
{
    public static final DecimalFormat DECIMALFORMAT;
    public static LinkedList<TextureFix.UnloadEntry> toUnload;
    
    public void runFix() {
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(resourceManager -> {
            final TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
            if (textureMapBlocks == null) {
                return;
            }
            long n = 0L;
            int n2 = 0;
            for (final TextureAtlasSprite textureAtlasSprite : this.getData(textureMapBlocks).values()) {
                if (!textureAtlasSprite.hasAnimationMetadata()) {
                    ++n2;
                    n += (long) textureAtlasSprite.getIconWidth() * textureAtlasSprite.getIconHeight() * 4;
                    textureAtlasSprite.setFramesTextureData(new FixList(textureAtlasSprite));
                }
            }
            final long n3 = n * (1 + Minecraft.getMinecraft().gameSettings.mipmapLevels);
            System.out.println("Fixed Textures: " + n2 + " Saved: " + TextureFix.DECIMALFORMAT.format(this.toMB(n3)) + "MB (" + n3 + " bytes)");
        });
    }
    
    public static void markForUnload(final TextureAtlasSprite textureAtlasSprite) {
        TextureFix.toUnload.add(new TextureFix.UnloadEntry(textureAtlasSprite));
    }
    
    public static void onClientTick() {
        if (TextureFix.toUnload.size() > 0 && TextureFix.toUnload.getFirst().unload()) {
            TextureFix.toUnload.removeFirst();
        }
    }
    
    private long toMB(final long n) {
        return n / 1024L / 1024L;
    }
    
    Map<String, TextureAtlasSprite> getData(final TextureMap textureMap) {
        try {
            for (final Field field : textureMap.getClass().getDeclaredFields()) {
                if (field.getType() == Map.class) {
                    field.setAccessible(true);
                    return (Map<String, TextureAtlasSprite>)field.get(textureMap);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static void reloadTextureData(final TextureAtlasSprite textureAtlasSprite) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        reloadTextureData(textureAtlasSprite, minecraft.getResourceManager(), minecraft.getTextureMapBlocks());
    }
    
    private static void reloadTextureData(final TextureAtlasSprite textureAtlasSprite, final IResourceManager resourceManager, final TextureMap textureMap) {
        final ResourceLocation resourceLocation = getResourceLocation(textureAtlasSprite, textureMap);
        if (textureAtlasSprite.hasCustomLoader(resourceManager, resourceLocation)) {
            textureAtlasSprite.load(resourceManager, resourceLocation);
        }
        else {
            try {
                final IResource resource = resourceManager.getResource(resourceLocation);
                final BufferedImage[] array = new BufferedImage[1 + Minecraft.getMinecraft().gameSettings.mipmapLevels];
                array[0] = TextureUtil.readBufferedImage(resource.getInputStream());
                textureAtlasSprite.loadSprite(array, (AnimationMetadataSection)null);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static ResourceLocation getResourceLocation(final TextureAtlasSprite textureAtlasSprite, final TextureMap textureMap) {
        final ResourceLocation resourceLocation = new ResourceLocation(textureAtlasSprite.getIconName());
        return new ResourceLocation(resourceLocation.getResourceDomain(), String.format("%s/%s%s", "texures", resourceLocation.getResourcePath(), ".png"));
    }
    
    static {
        DECIMALFORMAT = new DecimalFormat("#.###");
        TextureFix.toUnload = new LinkedList<TextureFix.UnloadEntry>();
    }


    public static class UnloadEntry
    {
        int count;
        final TextureAtlasSprite sprite;

        public UnloadEntry(final TextureAtlasSprite sprite) {
            this.count = 2;
            this.sprite = sprite;
        }

        public boolean unload() {
            --this.count;
            if (this.count <= 0) {
                this.sprite.clearFramesTextureData();
                return true;
            }
            return false;
        }
    }

}
