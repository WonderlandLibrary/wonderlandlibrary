package net.caffeinemc.phosphor.mixin;

import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.api.event.events.EntityRemoveEvent;
import net.caffeinemc.phosphor.api.event.events.WorldTickEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        Phosphor.EVENTBUS.post(WorldTickEvent.get());
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    private void onEntityRemove(int entityId, Entity.RemovalReason removalReason, CallbackInfo info) {
        Phosphor.EVENTBUS.post(EntityRemoveEvent.get(MinecraftClient.getInstance().world.getEntityById(entityId)));
    }
}