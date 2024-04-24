package me.crimp.claudius.mixins.mixins;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.modules.client.HUD;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer {

    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callback){
        UUID uuid = Objects.requireNonNull(getPlayerInfo()).getGameProfile().getId();

        if (claudius.capeManager.isOg(uuid) && HUD.INSTANCE.Capes.getValue()) {
            callback.setReturnValue(new ResourceLocation("textures/ogcape.png"));
        }

        if (claudius.capeManager.isContributor(uuid) && HUD.INSTANCE.Capes.getValue()) {
            callback.setReturnValue(new ResourceLocation("textures/devcape.png"));
        }
    }
}

