/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraftforge.client.MinecraftForgeClient
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.injection.forge.mixins.accessors.MinecraftForgeClientAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Minecraft.class})
public class MinecraftMixin_MemoryIssues {
    @Shadow
    public WorldClient field_71441_e;
    @Shadow
    public EntityRenderer field_71460_t;

    @Inject(method={"loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V"}, at={@At(value="HEAD")})
    private void patcher$clearLoadedMaps(WorldClient worldClientIn, String loadingMessage, CallbackInfo ci) {
        if (worldClientIn != this.field_71441_e) {
            this.field_71460_t.func_147701_i().func_148249_a();
        }
    }

    @Inject(method={"loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V"}, at={@At(value="FIELD", target="Lnet/minecraft/client/Minecraft;theWorld:Lnet/minecraft/client/multiplayer/WorldClient;", opcode=181, shift=At.Shift.AFTER)})
    private void patcher$clearRenderCache(CallbackInfo ci) {
        MinecraftForgeClient.getRenderPass();
        MinecraftForgeClientAccessor.getRegionCache().invalidateAll();
        MinecraftForgeClientAccessor.getRegionCache().cleanUp();
    }
}

