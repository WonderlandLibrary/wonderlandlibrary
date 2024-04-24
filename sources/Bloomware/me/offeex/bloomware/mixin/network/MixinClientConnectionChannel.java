package me.offeex.bloomware.mixin.network;

import io.netty.channel.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/network/ClientConnection$1")
public class MixinClientConnectionChannel {
    @Inject(method = "initChannel(Lio/netty/channel/Channel;)V", at = @At("HEAD"))
    public void connect(Channel channel, CallbackInfo cir) {
//        TODO: Rewrite
//        Proxy proxy = ProxyManager.getCurrentProxy();
//        if (proxy != null) {
//            if (proxy.type().equals(Proxy.ProxyType.SOCKS4)) channel.pipeline().addFirst(new Socks4ProxyHandler(new InetSocketAddress(proxy.getIp(), proxy.getPort()), proxy.username()));
//            else channel.pipeline().addFirst(new Socks5ProxyHandler(new InetSocketAddress(proxy.getIp(), proxy.getPort()), proxy.username(), proxy.password()));
//        }
    }
}
