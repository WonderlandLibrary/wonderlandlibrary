/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.LoadingCache
 *  net.minecraft.client.renderer.RegionRenderCache
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.client.MinecraftForgeClient
 *  org.apache.commons.lang3.tuple.Pair
 */
package net.dev.important.injection.forge.mixins.accessors;

import com.google.common.cache.LoadingCache;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={MinecraftForgeClient.class})
public interface MinecraftForgeClientAccessor {
    @Accessor(remap=false)
    public static LoadingCache<Pair<World, BlockPos>, RegionRenderCache> getRegionCache() {
        throw new AssertionError();
    }
}

