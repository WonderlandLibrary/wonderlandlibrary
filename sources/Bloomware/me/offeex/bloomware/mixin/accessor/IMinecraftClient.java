package me.offeex.bloomware.mixin.accessor;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface IMinecraftClient {
    @Accessor("currentFps")
    int getCurrentFps();

    @Accessor("itemUseCooldown")
    void setItemUseCooldown(int cooldown);

}
