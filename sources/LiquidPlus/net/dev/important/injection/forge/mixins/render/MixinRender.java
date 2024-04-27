/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.entity.Entity
 */
package net.dev.important.injection.forge.mixins.render;

import net.dev.important.Client;
import net.dev.important.event.RenderEntityEvent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Render.class})
public abstract class MixinRender {
    @Shadow
    protected abstract <T extends Entity> boolean func_180548_c(T var1);

    @Inject(method={"doRender"}, at={@At(value="HEAD")})
    private void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        Client.eventManager.callEvent(new RenderEntityEvent(entity, x, y, z, entityYaw, partialTicks));
    }
}

