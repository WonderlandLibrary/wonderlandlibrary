package rosegoldclient.mixins;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rosegoldclient.Main;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRender {

    @Redirect(method="orientCamera", at=@At(value="INVOKE", target="Lnet/minecraft/util/math/Vec3d;distanceTo(Lnet/minecraft/util/math/Vec3d;)D"))
    public double onCamera(Vec3d instance, Vec3d vec) {
        if(Main.configFile.phase) return 4.0F;
        return instance.distanceTo(vec);
    }
}
