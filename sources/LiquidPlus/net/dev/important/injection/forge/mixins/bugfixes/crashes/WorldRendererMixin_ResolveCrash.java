/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.vertex.VertexFormat
 */
package net.dev.important.injection.forge.mixins.bugfixes.crashes;

import java.nio.IntBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={WorldRenderer.class})
public class WorldRendererMixin_ResolveCrash {
    @Shadow
    private IntBuffer field_178999_b;
    @Shadow
    private VertexFormat field_179011_q;

    @Inject(method={"finishDrawing"}, at={@At(value="INVOKE", target="Ljava/nio/ByteBuffer;limit(I)Ljava/nio/Buffer;", remap=false)})
    private void patcher$resetBuffer(CallbackInfo ci) {
        this.field_178999_b.position(0);
    }

    @Inject(method={"endVertex"}, at={@At(value="HEAD")})
    private void patcher$adjustBuffer(CallbackInfo ci) {
        this.field_178999_b.position(this.field_178999_b.position() + this.field_179011_q.func_181719_f());
    }
}

