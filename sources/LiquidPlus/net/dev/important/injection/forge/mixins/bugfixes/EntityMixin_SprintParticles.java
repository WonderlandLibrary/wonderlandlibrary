/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public class EntityMixin_SprintParticles {
    @Shadow
    public boolean field_70122_E;

    @Inject(method={"spawnRunningParticles"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$checkGroundState(CallbackInfo ci) {
        if (!this.field_70122_E) {
            ci.cancel();
        }
    }
}

