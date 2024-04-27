/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture$Type
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.SkinManager
 */
package net.dev.important.injection.forge.mixins.resources;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.dev.important.Client;
import net.dev.important.modules.module.modules.misc.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SkinManager.class})
public class MixinSkinManager {
    @Inject(method={"loadSkinFromCache"}, cancellable=true, at={@At(value="HEAD")})
    private void injectSkinProtect(GameProfile gameProfile, CallbackInfoReturnable<Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> cir) {
        if (gameProfile == null) {
            return;
        }
        NameProtect nameProtect = (NameProtect)Client.moduleManager.getModule(NameProtect.class);
        if (nameProtect.getState() && ((Boolean)nameProtect.skinProtectValue.get()).booleanValue() && (((Boolean)nameProtect.allPlayersValue.get()).booleanValue() || Objects.equals(gameProfile.getId(), Minecraft.func_71410_x().func_110432_I().func_148256_e().getId()))) {
            cir.setReturnValue(new HashMap());
            cir.cancel();
        }
    }
}

