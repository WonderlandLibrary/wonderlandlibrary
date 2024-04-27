/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.WorldManager
 */
package net.dev.important.injection.forge.mixins.optimize;

import net.dev.important.injection.access.IMixinWorldAccess;
import net.minecraft.world.WorldManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={WorldManager.class})
public abstract class MixinWorldManager
implements IMixinWorldAccess {
    @Override
    public void notifyLightSet(int n, int n2, int n3) {
    }
}

