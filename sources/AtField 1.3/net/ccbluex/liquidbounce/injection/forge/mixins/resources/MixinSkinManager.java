/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.SkinManager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.resources;

import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SkinManager.class})
public class MixinSkinManager {
    @Inject(method={"loadSkinFromCache"}, cancellable=true, at={@At(value="HEAD")})
    private void injectSkinProtect(GameProfile gameProfile, CallbackInfoReturnable callbackInfoReturnable) {
        if (gameProfile == null) {
            return;
        }
        NameProtect nameProtect = (NameProtect)LiquidBounce.moduleManager.getModule(NameProtect.class);
        if (nameProtect.getState() && ((Boolean)nameProtect.skinProtectValue.get()).booleanValue() && (((Boolean)nameProtect.allPlayersValue.get()).booleanValue() || Objects.equals(gameProfile.getId(), Minecraft.func_71410_x().func_110432_I().func_148256_e().getId()))) {
            callbackInfoReturnable.setReturnValue(new HashMap());
            callbackInfoReturnable.cancel();
        }
    }
}

