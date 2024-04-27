/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.renderer.GlStateManager
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class})
public class EntityRendererMixin_PolygonOffset {
    @Inject(method={"renderWorldPass"}, slice={@Slice(from=@At(value="FIELD", target="Lnet/minecraft/util/EnumWorldBlockLayer;TRANSLUCENT:Lnet/minecraft/util/EnumWorldBlockLayer;"))}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/RenderGlobal;renderBlockLayer(Lnet/minecraft/util/EnumWorldBlockLayer;DILnet/minecraft/entity/Entity;)I", ordinal=0)})
    private void patcher$enablePolygonOffset(CallbackInfo ci) {
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a((float)-0.325f, (float)-0.325f);
    }

    @Inject(method={"renderWorldPass"}, slice={@Slice(from=@At(value="FIELD", target="Lnet/minecraft/util/EnumWorldBlockLayer;TRANSLUCENT:Lnet/minecraft/util/EnumWorldBlockLayer;"))}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/RenderGlobal;renderBlockLayer(Lnet/minecraft/util/EnumWorldBlockLayer;DILnet/minecraft/entity/Entity;)I", ordinal=0, shift=At.Shift.AFTER)})
    private void patcher$disablePolygonOffset(CallbackInfo ci) {
        GlStateManager.func_179113_r();
    }
}

