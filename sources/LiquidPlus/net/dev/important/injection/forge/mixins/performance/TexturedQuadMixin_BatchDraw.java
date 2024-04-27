/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.TexturedQuad
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexFormat
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.injection.forge.mixins.accessors.WorldRendererAccessor;
import net.dev.important.modules.module.modules.misc.Patcher;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={TexturedQuad.class})
public class TexturedQuadMixin_BatchDraw {
    @Unique
    private boolean patcher$drawOnSelf;

    @Redirect(method={"draw"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/WorldRenderer;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
    private void patcher$beginDraw(WorldRenderer renderer, int glMode, VertexFormat format) {
        boolean bl = this.patcher$drawOnSelf = !((WorldRendererAccessor)renderer).isDrawing();
        if (this.patcher$drawOnSelf || !((Boolean)Patcher.batchModelRendering.get()).booleanValue()) {
            renderer.func_181668_a(glMode, DefaultVertexFormats.field_181710_j);
        }
    }

    @Redirect(method={"draw"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/Tessellator;draw()V"))
    private void patcher$endDraw(Tessellator tessellator) {
        if (this.patcher$drawOnSelf || !((Boolean)Patcher.batchModelRendering.get()).booleanValue()) {
            tessellator.func_78381_a();
        }
    }
}

