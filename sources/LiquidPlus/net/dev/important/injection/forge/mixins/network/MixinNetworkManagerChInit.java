/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.socket.SocketChannel
 */
package net.dev.important.injection.forge.mixins.network;

import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import de.enzaxd.viaforge.ViaForge;
import de.enzaxd.viaforge.handler.DecodeHandler;
import de.enzaxd.viaforge.handler.EncodeHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.SocketChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net.minecraft.network.NetworkManager$5"})
public abstract class MixinNetworkManagerChInit {
    @Inject(method={"initChannel"}, at={@At(value="TAIL")}, remap=false)
    private void onInitChannel(Channel channel, CallbackInfo ci) {
        if (channel instanceof SocketChannel && ViaForge.getInstance().getVersion() != 47) {
            UserConnectionImpl user = new UserConnectionImpl(channel, true);
            new ProtocolPipelineImpl(user);
            channel.pipeline().addBefore("encoder", "via-encoder", (ChannelHandler)new EncodeHandler(user)).addBefore("decoder", "via-decoder", (ChannelHandler)new DecodeHandler(user));
        }
    }
}

