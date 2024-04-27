/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderPlayer
 */
package net.dev.important.injection.forge.mixins.render;

import java.util.Objects;
import net.dev.important.Client;
import net.dev.important.modules.module.modules.render.PlayerEdit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderPlayer.class})
public abstract class MixinRenderPlayer {
    @Inject(method={"renderLivingAt*"}, at={@At(value="HEAD")})
    protected void renderLivingAt(AbstractClientPlayer entityLivingBaseIn, double x, double y, double z, CallbackInfo callbackInfo) {
        if (Objects.requireNonNull(Client.moduleManager.getModule(PlayerEdit.class)).getState() & entityLivingBaseIn.equals((Object)Minecraft.func_71410_x().field_71439_g) && ((Boolean)PlayerEdit.editPlayerSizeValue.get()).booleanValue()) {
            GlStateManager.func_179152_a((float)((Float)PlayerEdit.playerSizeValue.get()).floatValue(), (float)((Float)PlayerEdit.playerSizeValue.get()).floatValue(), (float)((Float)PlayerEdit.playerSizeValue.get()).floatValue());
        }
    }
}

