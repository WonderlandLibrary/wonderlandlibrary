package net.smoothboot.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.AbstractTeam;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.render.PlayerESP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.smoothboot.client.module.Mod.mc;
import static net.smoothboot.client.module.render.PlayerESP.ESPTeamcheck;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (PlayerESP.outliningEntities && entity.getType() == EntityType.PLAYER && entity != Mod.mc.player) {
            if (ESPTeamcheck.isEnabled() && !entity.isTeammate(mc.player)) {
                ci.setReturnValue(true);
            }
            else if (!ESPTeamcheck.isEnabled()) {
                ci.setReturnValue(true);
            }
        }
    }
    @Inject(method = "scheduleStop", at = @At("HEAD"))
    public void stop(CallbackInfo ci){
    }
}
