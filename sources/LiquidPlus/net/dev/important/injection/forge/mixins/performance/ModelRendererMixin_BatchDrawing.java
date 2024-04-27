/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.modules.module.modules.misc.Patcher;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ModelRenderer.class})
public class ModelRendererMixin_BatchDrawing {
    @Shadow
    private boolean field_78812_q;
    private boolean patcher$compiledState;

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void patcher$resetCompiled(float j, CallbackInfo ci) {
        if (this.patcher$compiledState != (Boolean)Patcher.batchModelRendering.get()) {
            this.field_78812_q = false;
        }
    }

    @Inject(method={"compileDisplayList"}, at={@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/client/renderer/Tessellator;getWorldRenderer()Lnet/minecraft/client/renderer/WorldRenderer;")})
    private void patcher$beginRendering(CallbackInfo ci) {
        this.patcher$compiledState = (Boolean)Patcher.batchModelRendering.get();
        if (((Boolean)Patcher.batchModelRendering.get()).booleanValue()) {
            Tessellator.func_178181_a().func_178180_c().func_181668_a(7, DefaultVertexFormats.field_181703_c);
        }
    }

    @Inject(method={"compileDisplayList"}, at={@At(value="INVOKE", target="Lorg/lwjgl/opengl/GL11;glEndList()V", remap=false)})
    private void patcher$draw(CallbackInfo ci) {
        if (((Boolean)Patcher.batchModelRendering.get()).booleanValue()) {
            Tessellator.func_178181_a().func_78381_a();
        }
    }
}

