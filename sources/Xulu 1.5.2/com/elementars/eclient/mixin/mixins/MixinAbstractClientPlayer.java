// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.cape.Capes;
import com.elementars.eclient.module.render.Cape;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractClientPlayer.class })
public abstract class MixinAbstractClientPlayer extends MixinEntityPlayer
{
    private Minecraft minecraft;
    
    public MixinAbstractClientPlayer() {
        this.minecraft = Minecraft.getMinecraft();
    }
    
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();
    
    @Inject(method = { "getLocationCape" }, at = { @At("HEAD") }, cancellable = true)
    public void getLocationCape(final CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (Cape.isEnabled()) {
            final NetworkPlayerInfo info = this.getPlayerInfo();
            if (info != null && Capes.isCapeUser(info.getGameProfile().getName())) {
                callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/eclient/cape.png"));
            }
        }
    }
}
