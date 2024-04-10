package info.sigmaclient.sigma.gui.altmanager;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.Channel;

public class FakeNetworkManager extends NetworkManager
{
    public FakeNetworkManager(final PacketDirection packetDirection)
    {
        super(packetDirection);
    }
    
    public Channel channel() {
        return new Channel() {
            @Override
            protected void finalize() {
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public void close() {

            }
        };
    }
}