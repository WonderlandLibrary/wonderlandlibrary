package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.EntityMarginEvent;
import dev.zprestige.prestige.client.event.impl.HitboxEvent;
import dev.zprestige.prestige.client.event.impl.JumpEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={Entity.class})
public abstract class MixinEntity {
    @Shadow
    public abstract World getEntityWorld();

    @Inject(method={"getTargetingMargin"}, at={@At(value="HEAD")}, cancellable=true)
    void onGetTargetingMargin(CallbackInfoReturnable callbackInfoReturnable) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (getEntityWorld() == null) {
            return;
        }
        Entity entity = getEntityWorld().getEntityById(getId());
        if (entity == null) {
            return;
        }
        EntityMarginEvent event = new EntityMarginEvent(entity, 0);
        if (event.invoke()) {
            callbackInfoReturnable.setReturnValue(event.getMargin());
        }
    }

    @Inject(method={"getBoundingBox"}, at={@At(value="RETURN")}, cancellable=true)
    void getBoundingBox(CallbackInfoReturnable callbackInfoReturnable) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        if (getEntityWorld() == null) {
            return;
        }
        Entity entity = getEntityWorld().getEntityById(getId());
        if (entity == null) {
            return;
        }
        Box box = (Box)callbackInfoReturnable.getReturnValue();
        HitboxEvent event = new HitboxEvent(entity, box);
        if (event.invoke()) {
            callbackInfoReturnable.setReturnValue(event.getBox());
        }
    }

    @Shadow
    public abstract int getId();

    @ModifyArgs(at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;movementInputToVelocity(Lnet/minecraft/util/math/Vec3d;FF)Lnet/minecraft/util/math/Vec3d;"), method={"updateVelocity"})
    void movementInputToVelocity(Args args) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        JumpEvent event = new JumpEvent(((Float)args.get(2)).floatValue());
        if (event.invoke()) {
            args.set(2, (Object)Float.valueOf(event.getYaw()));
        }
    }
}