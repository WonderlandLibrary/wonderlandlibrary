package best.azura.client.impl.ui.gui.proxy;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.socket.oio.OioSocketChannel;
import me.errordev.http.request.HTTPMethod;
import me.errordev.http.request.HTTPRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class ProxyManager {
    private static Proxy proxy;
    private static long connectTime, ping = 0;
    private static Thread pingCheckingThread;

    public static Proxy getProxy() {
        return proxy;
    }

    public static void setProxy(Proxy proxyIn) {
        if (proxy == proxyIn) return;
        proxy = proxyIn;
        connectTime = System.currentTimeMillis();
    }

    public static long getConnectTime() {
        return connectTime;
    }

    public static long getPing() {
        return ping;
    }

    public static void setProxy(String ip, String port) {
        setProxy(ip, port, "SOCKS");
    }
    public static void setProxy(String ip, String port, String typeStr) {
        try {
            Proxy.Type type = Proxy.Type.SOCKS;
            try {
                type = Proxy.Type.valueOf(typeStr);
            } catch (Exception ignored) {}
            setProxy(new Proxy(type, new InetSocketAddress(ip, Integer.parseInt(port))));
        } catch (Exception exception) {
            setProxy(null);
        }
        if (proxy != null) {
            ping = -1;
            if (pingCheckingThread != null) pingCheckingThread.interrupt();
            pingCheckingThread = new Thread(() -> {
                try {
                    final long start = System.currentTimeMillis();
                    final Socket s = new Socket();
                    s.connect(proxy.address());
                    ping = System.currentTimeMillis() - start;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            pingCheckingThread.start();
        }
    }

    public static String generateProxy() {
        try {
            AtomicReference<String> proxy = new AtomicReference<>("");
            final HTTPRequest request = new HTTPRequest(false, HTTPMethod.GET, null, ((req, urlConnection) -> {
                try {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                    for (String read; (read = bf.readLine()) != null;) {
                        JsonObject jsonObject = new Gson().fromJson(read, JsonObject.class);
                        if (jsonObject.has("proxy")) {
                            proxy.set(jsonObject.get("proxy").getAsString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }), 5000, 5000);
            request.connect(new URL("https://proxygenerator.azura.best"));
            return proxy.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "none:none";
    }

    public static ChannelFactory<OioSocketChannel> createProxyChannel() {
        return new SocketFactory();
    }

    private static class SocketFactory implements ChannelFactory<OioSocketChannel> {

        @Override
        public OioSocketChannel newChannel() {
            if (getProxy() == null || getProxy() == Proxy.NO_PROXY) {
                return new OioSocketChannel(new Socket(Proxy.NO_PROXY));
            }
            final Socket sock = new Socket(getProxy());
            try {
                Method m = sock.getClass().getDeclaredMethod("getImpl");
                m.setAccessible(true);
                Object sd = m.invoke(sock);
                m = sd.getClass().getDeclaredMethod("setV4");
                m.setAccessible(true);
                m.invoke(sd);
                return new OioSocketChannel(sock);
            }
            catch (Exception ex2) {
                throw new RuntimeException("Failed to create socks 4 proxy!", new Exception());
            }
        }
    }
}