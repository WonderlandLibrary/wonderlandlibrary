/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.ResourcePackRepository
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.modules.module.modules.misc.Patcher;
import net.dev.important.patcher.hooks.misc.ResourcePackRepositoryHook;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ResourcePackRepository.class})
public class ResourcePackRepositoryMixin_FasterSearching {
    @Inject(method={"updateRepositoryEntriesAll"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$searchUsingSet(CallbackInfo ci) {
        if (((Boolean)Patcher.labyModMoment.get()).booleanValue()) {
            ResourcePackRepositoryHook.updateRepositoryEntriesAll((ResourcePackRepository)this);
            ci.cancel();
        }
    }
}

