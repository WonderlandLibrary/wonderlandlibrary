/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFuture
 */
package me.wintware.client.viamcp.viafabric.platform;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import us.myles.ViaVersion.api.data.UserConnection;

public class VRClientSideUserConnection
extends UserConnection {
    public VRClientSideUserConnection(Channel socketChannel) {
        super(socketChannel);
    }

    @Override
    public void sendRawPacket(ByteBuf packet, boolean currentThread) {
        Runnable act = () -> this.getChannel().pipeline().context("via-decoder").fireChannelRead(packet);
        if (currentThread) {
            act.run();
        } else {
            this.getChannel().eventLoop().execute(act);
        }
    }

    @Override
    public ChannelFuture sendRawPacketFuture(ByteBuf packet) {
        this.getChannel().pipeline().context("via-decoder").fireChannelRead(packet);
        return this.getChannel().newSucceededFuture();
    }

    @Override
    public void sendRawPacketToServer(ByteBuf packet, boolean currentThread) {
        if (currentThread) {
            this.getChannel().pipeline().context("via-encoder").writeAndFlush(packet);
        } else {
            this.getChannel().eventLoop().submit(() -> this.getChannel().pipeline().context("via-encoder").writeAndFlush(packet));
        }
    }
}

