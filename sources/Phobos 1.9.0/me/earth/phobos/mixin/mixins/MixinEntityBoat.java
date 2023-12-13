package me.earth.phobos.mixin.mixins;

import me.earth.phobos.features.modules.movement.BoatFly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityBoat.class})
public abstract class MixinEntityBoat/* extends MixinEntity*/ {
    @Shadow
    public abstract double getMountedYOffset();

    @Inject(method={"applyOrientationToEntity"}, at={@At(value="HEAD")}, cancellable=true)
    public void applyOrientationToEntity(Entity entity, CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method={"controlBoat"}, at={@At(value="HEAD")}, cancellable=true)
    public void controlBoat(CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method={"updatePassenger"}, at={@At(value="HEAD")}, cancellable=true)
    public void updatePassenger(Entity passenger, CallbackInfo ci) {
        if (BoatFly.INSTANCE.isEnabled() && passenger == Minecraft.getMinecraft().player) {
            ci.cancel();
            float f = 0.0f;
            float f1 = (float)((((Entity)(Object)this).isDead ? (double)0.01f : this.getMountedYOffset()) + passenger.getYOffset());
            Vec3d vec3d = new Vec3d((double)f, 0.0, 0.0).rotateYaw(-(((Entity)(Object)this).rotationYaw * ((float)Math.PI / 180) - 1.5707964f));
            passenger.setPosition(((Entity)(Object)this).posX + vec3d.x, ((Entity)(Object)this).posY + (double)f1, ((Entity)(Object)this).posZ + vec3d.z);
        }
    }

}

