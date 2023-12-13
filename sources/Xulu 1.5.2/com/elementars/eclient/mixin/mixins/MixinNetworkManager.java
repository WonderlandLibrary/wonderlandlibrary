// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import com.elementars.eclient.event.events.EventSendPacket;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.event.events.EventReceivePacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetworkManager.class })
public class MixinNetworkManager
{
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    public void IchannelRead0(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo callback) {
        final EventReceivePacket event = new EventReceivePacket(packet);
        event.call();
        if (event.isCancelled()) {
            callback.cancel();
        }
    }
    
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    public void IsendPacket(final Packet<?> packet, final CallbackInfo callback) {
        final EventSendPacket event = new EventSendPacket(packet);
        event.call();
        if (event.isCancelled()) {
            callback.cancel();
        }
    }
}
