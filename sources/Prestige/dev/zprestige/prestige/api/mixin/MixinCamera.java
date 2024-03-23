package dev.zprestige.prestige.api.mixin;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.impl.CameraOffsetEvent;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={Camera.class})
public class MixinCamera {
    @ModifyArgs(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    void update(Args args) {
        if (Prestige.Companion.getSelfDestructed()) {
            return;
        }
        CameraOffsetEvent event = new CameraOffsetEvent(args.get(0), args.get(1), args.get(2));
        if (event.invoke()) {
            args.set(0, (Object) event.getX());
            args.set(1, (Object) event.getY());
            args.set(2, (Object) event.getZ());
        }
    }
}
