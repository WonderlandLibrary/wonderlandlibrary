package dev.zprestige.prestige.api.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={WorldRenderer.class})
public interface IWorldRenderer {
    @Accessor(value="blockBreakingInfos")
    Int2ObjectMap getBlockBreakingInfos();
}
