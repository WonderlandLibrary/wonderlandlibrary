package net.minecraft.client.resources.model;

import java.util.Set;
import net.minecraft.client.renderer.texture.IIconCreator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.ResourceLocation;

/*
 * Exception performing whole class analysis ignored.
 */
class ModelBakery.2
implements IIconCreator {
    final /* synthetic */ Set val$set;

    ModelBakery.2(Set set) {
        this.val$set = set;
    }

    public void registerSprites(TextureMap iconRegistry) {
        for (ResourceLocation resourcelocation : this.val$set) {
            TextureAtlasSprite textureatlassprite = iconRegistry.registerSprite(resourcelocation);
            ModelBakery.access$000((ModelBakery)ModelBakery.this).put((Object)resourcelocation, (Object)textureatlassprite);
        }
    }
}
