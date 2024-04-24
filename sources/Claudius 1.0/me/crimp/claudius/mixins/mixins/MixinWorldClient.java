package me.crimp.claudius.mixins.mixins;

import me.crimp.claudius.event.events.EntitySpawnEvent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class MixinWorldClient {

    @Inject(method = "onEntityAdded", at = @At("HEAD"))
    public void onEntityAdded(Entity entity, CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new EntitySpawnEvent(entity, EntitySpawnEvent.Type.Spawn));
    }

    @Inject(method = "onEntityRemoved", at = @At("HEAD"))
    public void onEntityRemoved(Entity entity, CallbackInfo info) {
        MinecraftForge.EVENT_BUS.post(new EntitySpawnEvent(entity, EntitySpawnEvent.Type.Despawn));
    }
}
