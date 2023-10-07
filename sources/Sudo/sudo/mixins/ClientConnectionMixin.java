package sudo.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import sudo.Client;
import sudo.events.EventReceivePacket;
import sudo.events.EventSendPacket;
import sudo.module.ModuleManager;
import sudo.module.client.PacketLogger;
import sudo.module.combat.Criticals;
import sudo.module.combat.Velocity;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
    public void send(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> callback, CallbackInfo ci) {
        EventSendPacket event = new EventSendPacket(packet);
        event.call();
        if (event.isCancelled()) ci.cancel();
        if(ModuleManager.INSTANCE.getModule(Criticals.class).isEnabled()) {Criticals.instance.sendPacket(event);}
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void receive(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        EventReceivePacket event = new EventReceivePacket(packet);
        if (ModuleManager.INSTANCE.getModule(PacketLogger.class).isEnabled()) {
    		Client.logger.info(event);
    	}
        event.call();
        if(event.isCancelled()) ci.cancel();
        if(ModuleManager.INSTANCE.getModule(Velocity.class).isEnabled()) {Velocity.get.onReceivePacket(event);}
    }
}
