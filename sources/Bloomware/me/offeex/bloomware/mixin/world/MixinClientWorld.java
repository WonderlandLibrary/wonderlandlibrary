package me.offeex.bloomware.mixin.world;

import me.offeex.bloomware.client.module.visuals.BarrierView;
import me.offeex.bloomware.client.module.visuals.Environment;
import me.offeex.bloomware.event.events.EventEntity;
import me.offeex.bloomware.event.events.EventUpdate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {

    @Shadow @Nullable protected abstract Block getBlockParticle();

    @ModifyVariable(method = "setTimeOfDay", at = @At("HEAD"), index = 1, argsOnly = true)
    long onSetTimeOfDay(long value) {
        EventUpdate.TimeOfDay event = new EventUpdate.TimeOfDay(value).post();
        return event.getTime();
    }

    @Inject(method = "addEntityPrivate", at = @At("HEAD"))
    private void addEntity(int id, Entity entity, CallbackInfo info) {
        new EventEntity.Add(id, entity).post();
    }

    @Inject(
        method = "removeEntity",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setRemoved(Lnet/minecraft/entity/Entity$RemovalReason;)V"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void removeEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci, Entity entity) {
        new EventEntity.Remove(entityId, entity, removalReason).post();
    }

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    private void getSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        Environment module = Environment.INSTANCE;
        if (module.getEnabled() && Environment.INSTANCE.getSky().getToggled())
            cir.setReturnValue(Environment.INSTANCE.getSkyColor().getColor().getVec3d());
    }

    @Inject(method = "getCloudsColor", at = @At("RETURN"), cancellable = true)
    private void getCloudsColor(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Environment.INSTANCE.getEnabled() && Environment.INSTANCE.getClouds().getToggled())
            cir.setReturnValue(Environment.INSTANCE.getCloudsColor().getColor().getVec3d());
    }

    @Redirect(method = "doRandomBlockDisplayTicks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getBlockParticle()Lnet/minecraft/block/Block;"))
    private Block renderBarrier(ClientWorld instance) {
        BarrierView module = BarrierView.INSTANCE;
        return module.getEnabled() ? Blocks.BARRIER : getBlockParticle();
    }
}
