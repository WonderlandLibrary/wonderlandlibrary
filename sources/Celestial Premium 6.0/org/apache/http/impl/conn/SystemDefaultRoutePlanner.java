/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.conn;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.protocol.HttpContext;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Immutable
public class SystemDefaultRoutePlanner
extends DefaultRoutePlanner {
    private final ProxySelector proxySelector;

    public SystemDefaultRoutePlanner(SchemePortResolver schemePortResolver, ProxySelector proxySelector) {
        super(schemePortResolver);
        this.proxySelector = proxySelector != null ? proxySelector : ProxySelector.getDefault();
    }

    public SystemDefaultRoutePlanner(ProxySelector proxySelector) {
        this(null, proxySelector);
    }

    @Override
    protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        URI targetURI;
        try {
            targetURI = new URI(target.toURI());
        }
        catch (URISyntaxException ex) {
            throw new HttpException("Cannot convert host to URI: " + target, ex);
        }
        List<Proxy> proxies = this.proxySelector.select(targetURI);
        Proxy p = this.chooseProxy(proxies);
        HttpHost result = null;
        if (p.type() == Proxy.Type.HTTP) {
            if (!(p.address() instanceof InetSocketAddress)) {
                throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
            }
            InetSocketAddress isa = (InetSocketAddress)p.address();
            result = new HttpHost(this.getHost(isa), isa.getPort());
        }
        return result;
    }

    private String getHost(InetSocketAddress isa) {
        return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
    }

    private Proxy chooseProxy(List<Proxy> proxies) {
        Proxy result = null;
        block3: for (int i = 0; result == null && i < proxies.size(); ++i) {
            Proxy p = proxies.get(i);
            switch (p.type()) {
                case DIRECT: 
                case HTTP: {
                    result = p;
                    continue block3;
                }
            }
        }
        if (result == null) {
            result = Proxy.NO_PROXY;
        }
        return result;
    }
}

