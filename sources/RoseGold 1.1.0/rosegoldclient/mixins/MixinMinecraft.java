package rosegoldclient.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rosegoldclient.Main;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    private Entity renderViewEntity;

    @Inject(method = { "getRenderViewEntity" }, at = { @At("HEAD") })
    public void getRenderViewEntity(CallbackInfoReturnable<Entity> cir) {
        if (!Main.configFile.showServerSideRotations || renderViewEntity == null || renderViewEntity != Main.mc.player) return;
        ((EntityLivingBase) renderViewEntity).rotationYawHead = ((PlayerSPAccessor) renderViewEntity).getLastReportedYaw();
        ((EntityLivingBase) renderViewEntity).renderYawOffset = ((PlayerSPAccessor) renderViewEntity).getLastReportedYaw();
    }
}
