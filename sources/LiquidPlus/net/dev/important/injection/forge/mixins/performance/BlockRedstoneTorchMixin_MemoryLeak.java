/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockRedstoneTorch
 *  net.minecraft.block.BlockRedstoneTorch$Toggle
 *  net.minecraft.world.World
 */
package net.dev.important.injection.forge.mixins.performance;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={BlockRedstoneTorch.class})
public class BlockRedstoneTorchMixin_MemoryLeak {
    @Shadow
    private static Map<World, List<BlockRedstoneTorch.Toggle>> field_150112_b = new WeakHashMap<World, List<BlockRedstoneTorch.Toggle>>();
}

