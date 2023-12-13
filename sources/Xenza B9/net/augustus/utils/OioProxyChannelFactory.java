// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import io.netty.channel.Channel;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import net.augustus.ui.augustusmanager.AugustusProxy;
import java.net.Socket;
import java.net.Proxy;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.bootstrap.ChannelFactory;

public class OioProxyChannelFactory implements ChannelFactory<OioSocketChannel>
{
    private Proxy proxy;
    
    public OioProxyChannelFactory(final Proxy proxy) {
        this.proxy = proxy;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
    
    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }
    
    @Override
    public OioSocketChannel newChannel() {
        if (this.proxy != null && this.proxy != Proxy.NO_PROXY) {
            final Socket sock = new Socket(this.proxy);
            try {
                if (AugustusProxy.type.equals("Socks4")) {
                    Method m = sock.getClass().getDeclaredMethod("getImpl", (Class<?>[])new Class[0]);
                    m.setAccessible(true);
                    final Object sd = m.invoke(sock, new Object[0]);
                    m = sd.getClass().getDeclaredMethod("setV4", (Class<?>[])new Class[0]);
                    m.setAccessible(true);
                    m.invoke(sd, new Object[0]);
                }
                return new OioSocketChannel(sock);
            }
            catch (final SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException var4) {
                throw new RuntimeException("Failed to create socks 4 proxy!", var4);
            }
        }
        return new OioSocketChannel(new Socket(Proxy.NO_PROXY));
    }
}
