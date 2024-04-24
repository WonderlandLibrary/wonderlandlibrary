package me.offeex.bloomware.mixin.accessor;

import net.minecraft.client.render.RenderPhase;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

public interface IRenderPhase {
    @Mixin(RenderPhase.TextureBase.class)
    interface TextureBase {
        @Invoker("getId")
        Optional<Identifier> getId();
    }
}
