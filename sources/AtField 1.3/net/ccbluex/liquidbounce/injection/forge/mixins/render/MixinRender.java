/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.entity.Entity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.RenderEntityEvent;
import net.ccbluex.liquidbounce.injection.backend.EntityImplKt;
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
    protected abstract boolean func_180548_c(Entity var1);

    @Inject(method={"doRender"}, at={@At(value="HEAD")})
    private void doRender(Entity entity, double d, double d2, double d3, float f, float f2, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new RenderEntityEvent(EntityImplKt.wrap(entity), d, d2, d3, f, f2));
    }
}

