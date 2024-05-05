package me.earth.earthhack.impl.core.mixins.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.core.ducks.INetworkManager;
import me.earth.earthhack.impl.event.events.network.DisconnectEvent;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager implements INetworkManager
{
    @Shadow
    public abstract boolean isChannelOpen();

    @Shadow
    protected abstract void flushOutboundQueue();

    @Shadow
    protected abstract void dispatchPacket(final Packet<?> inPacket, @Nullable final GenericFutureListener <? extends Future <? super Void >> [] futureListeners);

    @Override
    public Packet<?> sendPacketNoEvent(Packet<?> packetIn)
    {
        if (this.isChannelOpen())
        {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, null);
            return packetIn;
        }

        return null;
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacketPre(Packet<?> packet, CallbackInfo info)
    {
        PacketEvent.Send<?> event = new PacketEvent.Send<>(packet);
        Bus.EVENT_BUS.post(event, packet.getClass());

        if (event.isCancelled())
        {
            info.cancel();
        }
    }

    @Inject(method = "dispatchPacket", at = @At("RETURN"))
    private void onSendPacketPost(final Packet<?> packetIn, @Nullable final GenericFutureListener <? extends Future <? super Void >> [] futureListeners, CallbackInfo info)
    {
        PacketEvent.Post<?> event = new PacketEvent.Post<>(packetIn);
        Bus.EVENT_BUS.post(event, packetIn.getClass());
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelReadPre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info)
    {
        PacketEvent.Receive<?> event = new PacketEvent.Receive<>(packet);
        Bus.EVENT_BUS.post(event, packet.getClass());

        if (event.isCancelled())
        {
            info.cancel();
        }
    }

    @Inject(method = "closeChannel", at = @At(value = "INVOKE", target = "Lio/netty/channel/Channel;isOpen()Z", remap = false))
    private void onDisconnectHook(ITextComponent component, CallbackInfo callbackInfo)
    {
        if (this.isChannelOpen())
        {
            Bus.EVENT_BUS.post(new DisconnectEvent(component));
        }
    }

}
