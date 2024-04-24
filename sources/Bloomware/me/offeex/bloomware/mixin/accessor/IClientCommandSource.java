package me.offeex.bloomware.mixin.accessor;

import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientCommandSource.class)
public interface IClientCommandSource {
    @Mutable @Accessor("networkHandler")
    void setNetworkHandler(ClientPlayNetworkHandler networkHandler);
}
