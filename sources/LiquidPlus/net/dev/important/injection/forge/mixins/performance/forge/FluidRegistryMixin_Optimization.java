/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fluids.Fluid
 *  net.minecraftforge.fluids.FluidRegistry
 */
package net.dev.important.injection.forge.mixins.performance.forge;

import java.util.Collections;
import java.util.Set;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={FluidRegistry.class})
public class FluidRegistryMixin_Optimization {
    @Shadow(remap=false)
    static Set<Fluid> bucketFluids;

    @Overwrite(remap=false)
    public static Set<Fluid> getBucketFluids() {
        return Collections.unmodifiableSet(bucketFluids);
    }
}

