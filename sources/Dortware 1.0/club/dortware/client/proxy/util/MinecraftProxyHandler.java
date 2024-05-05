package club.dortware.client.proxy.util;

import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.socket.oio.OioSocketChannel;
import org.lwjgl.Sys;

import java.net.*;

public class MinecraftProxyHandler implements ChannelFactory<OioSocketChannel> {

    private final Proxy proxy;

    public MinecraftProxyHandler(Proxy proxy) {
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return proxy;
    }

    @Override
    public OioSocketChannel newChannel() {
//        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 57441));
//        Authenticator.setDefault(new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("peYQee", "rmpqZ8".toCharArray());
//            }
//        });
        Socket socket = new Socket(proxy);
        return new OioSocketChannel(socket);
    }
}
