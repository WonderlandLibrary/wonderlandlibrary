/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.ResourcePackRepository
 */
package net.dev.important.injection.forge.mixins.bugfixes.crashes;

import java.io.File;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ResourcePackRepository.class})
public class ResourcePackRepositoryMixin_ResolveCrash {
    @Shadow
    @Final
    private File field_148534_e;

    @Inject(method={"deleteOldServerResourcesPacks"}, at={@At(value="HEAD")})
    private void patcher$createDirectory(CallbackInfo ci) {
        if (!this.field_148534_e.exists()) {
            this.field_148534_e.mkdirs();
        }
    }
}

