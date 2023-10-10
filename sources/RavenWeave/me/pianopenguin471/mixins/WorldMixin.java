package me.pianopenguin471.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ravenweave.client.event.impl.EntityJoinWorldEvent;

@Mixin(World.class)
public class WorldMixin {
    @Inject(method = "spawnEntityInWorld", at = @At("HEAD"))
    public void onSpawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        EventBus.callEvent(new EntityJoinWorldEvent(entity, entity.worldObj));
    }
}
