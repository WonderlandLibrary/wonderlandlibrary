package dev.zprestige.prestige.api.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={MinecraftClient.class})
public interface IMinecraftClient {
    @Accessor(value="itemUseCooldown")
    void setItemUseCooldown(int var1);

    @Accessor(value="itemUseCooldown")
    int getItemUseCooldown();
}
