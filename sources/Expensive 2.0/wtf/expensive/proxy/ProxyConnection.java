package wtf.expensive.proxy;

import lombok.Getter;

import java.net.InetSocketAddress;

public class ProxyConnection {

    @Getter private ProxyType proxyType = ProxyType.DIRECT;
    @Getter private InetSocketAddress proxyAddr = null;

    public void setup(ProxyType proxyType, InetSocketAddress proxyAddr) {
        this.proxyType = proxyType;
        this.proxyAddr = proxyAddr;
    }

    public void reset() {
        proxyType = ProxyType.DIRECT;
        proxyAddr = null;
    }
}
