package me.offeex.bloomware.mixin.network;

import me.offeex.bloomware.api.util.MixinUtil;
import me.offeex.bloomware.event.events.EventVelocity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Redirect(method = "onEntityVelocityUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocityClient(DDD)V"))
    public void onEntityVelocityUpdate(Entity instance, double x, double y, double z) {
        if (MixinUtil.isClientEntity(instance)) {
            EventVelocity.Player event = new EventVelocity.Player((ClientPlayerEntity) instance, x, y, z).post();
            instance.setVelocityClient(event.getX(), event.getY(), event.getZ());
        } else instance.setVelocityClient(x, y, z);
    }

    @ModifyArgs(method = "onExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;"))
    public void onExplosion(Args args) {
        EventVelocity.Explosion event = new EventVelocity.Explosion(args.get(0), args.get(1), args.get(2)).post();
        args.setAll(event.getX(), event.getY(), event.getZ());
    }
}
