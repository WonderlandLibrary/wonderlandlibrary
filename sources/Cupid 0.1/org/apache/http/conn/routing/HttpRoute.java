package org.apache.http.conn.routing;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public final class HttpRoute implements RouteInfo, Cloneable {
  private final HttpHost targetHost;
  
  private final InetAddress localAddress;
  
  private final List<HttpHost> proxyChain;
  
  private final RouteInfo.TunnelType tunnelled;
  
  private final RouteInfo.LayerType layered;
  
  private final boolean secure;
  
  private HttpRoute(HttpHost target, InetAddress local, List<HttpHost> proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
    Args.notNull(target, "Target host");
    this.targetHost = target;
    this.localAddress = local;
    if (proxies != null && !proxies.isEmpty()) {
      this.proxyChain = new ArrayList<HttpHost>(proxies);
    } else {
      this.proxyChain = null;
    } 
    if (tunnelled == RouteInfo.TunnelType.TUNNELLED)
      Args.check((this.proxyChain != null), "Proxy required if tunnelled"); 
    this.secure = secure;
    this.tunnelled = (tunnelled != null) ? tunnelled : RouteInfo.TunnelType.PLAIN;
    this.layered = (layered != null) ? layered : RouteInfo.LayerType.PLAIN;
  }
  
  public HttpRoute(HttpHost target, InetAddress local, HttpHost[] proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
    this(target, local, (proxies != null) ? Arrays.<HttpHost>asList(proxies) : null, secure, tunnelled, layered);
  }
  
  public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
    this(target, local, (proxy != null) ? Collections.<HttpHost>singletonList(proxy) : null, secure, tunnelled, layered);
  }
  
  public HttpRoute(HttpHost target, InetAddress local, boolean secure) {
    this(target, local, Collections.emptyList(), secure, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
  }
  
  public HttpRoute(HttpHost target) {
    this(target, (InetAddress)null, Collections.emptyList(), false, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
  }
  
  public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure) {
    this(target, local, Collections.singletonList(Args.notNull(proxy, "Proxy host")), secure, secure ? RouteInfo.TunnelType.TUNNELLED : RouteInfo.TunnelType.PLAIN, secure ? RouteInfo.LayerType.LAYERED : RouteInfo.LayerType.PLAIN);
  }
  
  public HttpRoute(HttpHost target, HttpHost proxy) {
    this(target, null, proxy, false);
  }
  
  public final HttpHost getTargetHost() {
    return this.targetHost;
  }
  
  public final InetAddress getLocalAddress() {
    return this.localAddress;
  }
  
  public final InetSocketAddress getLocalSocketAddress() {
    return (this.localAddress != null) ? new InetSocketAddress(this.localAddress, 0) : null;
  }
  
  public final int getHopCount() {
    return (this.proxyChain != null) ? (this.proxyChain.size() + 1) : 1;
  }
  
  public final HttpHost getHopTarget(int hop) {
    Args.notNegative(hop, "Hop index");
    int hopcount = getHopCount();
    Args.check((hop < hopcount), "Hop index exceeds tracked route length");
    if (hop < hopcount - 1)
      return this.proxyChain.get(hop); 
    return this.targetHost;
  }
  
  public final HttpHost getProxyHost() {
    return (this.proxyChain != null && !this.proxyChain.isEmpty()) ? this.proxyChain.get(0) : null;
  }
  
  public final RouteInfo.TunnelType getTunnelType() {
    return this.tunnelled;
  }
  
  public final boolean isTunnelled() {
    return (this.tunnelled == RouteInfo.TunnelType.TUNNELLED);
  }
  
  public final RouteInfo.LayerType getLayerType() {
    return this.layered;
  }
  
  public final boolean isLayered() {
    return (this.layered == RouteInfo.LayerType.LAYERED);
  }
  
  public final boolean isSecure() {
    return this.secure;
  }
  
  public final boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj instanceof HttpRoute) {
      HttpRoute that = (HttpRoute)obj;
      return (this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered && LangUtils.equals(this.targetHost, that.targetHost) && LangUtils.equals(this.localAddress, that.localAddress) && LangUtils.equals(this.proxyChain, that.proxyChain));
    } 
    return false;
  }
  
  public final int hashCode() {
    int hash = 17;
    hash = LangUtils.hashCode(hash, this.targetHost);
    hash = LangUtils.hashCode(hash, this.localAddress);
    if (this.proxyChain != null)
      for (HttpHost element : this.proxyChain)
        hash = LangUtils.hashCode(hash, element);  
    hash = LangUtils.hashCode(hash, this.secure);
    hash = LangUtils.hashCode(hash, this.tunnelled);
    hash = LangUtils.hashCode(hash, this.layered);
    return hash;
  }
  
  public final String toString() {
    StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
    if (this.localAddress != null) {
      cab.append(this.localAddress);
      cab.append("->");
    } 
    cab.append('{');
    if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED)
      cab.append('t'); 
    if (this.layered == RouteInfo.LayerType.LAYERED)
      cab.append('l'); 
    if (this.secure)
      cab.append('s'); 
    cab.append("}->");
    if (this.proxyChain != null)
      for (HttpHost aProxyChain : this.proxyChain) {
        cab.append(aProxyChain);
        cab.append("->");
      }  
    cab.append(this.targetHost);
    return cab.toString();
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\conn\routing\HttpRoute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */