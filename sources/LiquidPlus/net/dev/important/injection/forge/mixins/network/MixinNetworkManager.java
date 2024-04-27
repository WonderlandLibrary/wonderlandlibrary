/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 */
package net.dev.important.injection.forge.mixins.network;

import io.netty.channel.ChannelHandlerContext;
import net.dev.important.Client;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.modules.render.HUD;
import net.dev.important.utils.PacketUtils;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={NetworkManager.class})
public class MixinNetworkManager {
    @Inject(method={"channelRead0"}, at={@At(value="HEAD")}, cancellable=true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        PacketEvent event = new PacketEvent(packet);
        Client.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method={"sendPacket(Lnet/minecraft/network/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        if (PacketUtils.handleSendPacket(packet)) {
            return;
        }
        PacketEvent event = new PacketEvent(packet);
        Client.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method={"getIsencrypted"}, at={@At(value="HEAD")}, cancellable=true)
    private void injectEncryption(CallbackInfoReturnable<Boolean> cir) {
        HUD hud = (HUD)Client.moduleManager.getModule(HUD.class);
        if (hud != null && ((Boolean)hud.getTabHead().get()).booleanValue()) {
            cir.setReturnValue(true);
        }
    }
}

