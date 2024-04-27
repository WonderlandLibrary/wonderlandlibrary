/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.GameSettings
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net.minecraft.client.gui.GuiScreenOptionsSounds$Button"})
public class GuiScreenOptionsSoundsMixin_PacketSpam {
    @Redirect(method={"mouseDragged(Lnet/minecraft/client/Minecraft;II)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/settings/GameSettings;saveOptions()V"))
    private void patcher$cancelSaving(GameSettings instance) {
    }

    @Inject(method={"mouseReleased(II)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/audio/SoundHandler;playSound(Lnet/minecraft/client/audio/ISound;)V")})
    private void patcher$save(int mouseX, int mouseY, CallbackInfo ci) {
        Minecraft.func_71410_x().field_71474_y.func_74303_b();
    }
}

