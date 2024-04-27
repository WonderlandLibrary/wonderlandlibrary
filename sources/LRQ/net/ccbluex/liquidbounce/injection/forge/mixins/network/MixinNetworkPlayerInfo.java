/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.client.resources.DefaultPlayerSkin
 *  net.minecraft.util.ResourceLocation
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import com.mojang.authlib.GameProfile;
import java.util.Objects;
import java.util.UUID;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={NetworkPlayerInfo.class})
public class MixinNetworkPlayerInfo {
    @Shadow
    @Final
    private GameProfile field_178867_a;

    @Inject(method={"getLocationSkin"}, cancellable=true, at={@At(value="HEAD")})
    private void injectSkinProtect(CallbackInfoReturnable<ResourceLocation> cir) {
        NameProtect nameProtect = (NameProtect)LiquidBounce.moduleManager.getModule(NameProtect.class);
        if (nameProtect.getState() && ((Boolean)nameProtect.skinProtectValue.get()).booleanValue() && (((Boolean)nameProtect.allPlayersValue.get()).booleanValue() || Objects.equals(this.field_178867_a.getId(), Minecraft.func_71410_x().func_110432_I().func_148256_e().getId()))) {
            cir.setReturnValue((Object)DefaultPlayerSkin.func_177334_a((UUID)this.field_178867_a.getId()));
            cir.cancel();
        }
    }
}

