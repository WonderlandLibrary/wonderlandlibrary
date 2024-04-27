/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.network.play.INetHandlerPlayClient
 *  net.minecraft.network.play.server.S2EPacketCloseWindow
 */
package net.dev.important.injection.forge.mixins.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={S2EPacketCloseWindow.class})
public class MixinS2EPacketCloseWindow {
    @Inject(method={"processPacket(Lnet/minecraft/network/play/INetHandlerPlayClient;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$noCloseMyChat(INetHandlerPlayClient handler, CallbackInfo ci) {
        if (Minecraft.func_71410_x().field_71462_r instanceof GuiChat) {
            ci.cancel();
        }
    }
}

