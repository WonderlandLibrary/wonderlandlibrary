package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.Phase;
import dev.zprestige.prestige.client.event.impl.EntityEvent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientWorld.class})
public abstract class MixinClientWorld {
    @Shadow
    public abstract Entity getEntityById(int var1);

    @Inject(method={"addEntityPrivate"}, at={@At(value="TAIL")})
    void onAddEntityPrivate(int id, Entity entity, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (entity != null) {
            new EntityEvent(entity, Phase.PRE).invoke();
        }
    }

    @Inject(method={"removeEntity"}, at={@At(value="HEAD")})
    void onRemoveEntity(int id, Entity.RemovalReason removalReason, CallbackInfo callbackInfo) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        Entity entity = getEntityById(id);
        if (entity != null) {
            new EntityEvent(entity, Phase.POST).invoke();
        }
    }
}
