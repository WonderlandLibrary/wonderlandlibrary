package org.apache.http.impl.client;

import java.net.URI;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/** @deprecated */
@Deprecated
@Immutable
class DefaultRedirectStrategyAdaptor implements RedirectStrategy {
   private final RedirectHandler handler;

   public DefaultRedirectStrategyAdaptor(RedirectHandler var1) {
      this.handler = var1;
   }

   public boolean isRedirected(HttpRequest var1, HttpResponse var2, HttpContext var3) throws ProtocolException {
      return this.handler.isRedirectRequested(var2, var3);
   }

   public HttpUriRequest getRedirect(HttpRequest var1, HttpResponse var2, HttpContext var3) throws ProtocolException {
      URI var4 = this.handler.getLocationURI(var2, var3);
      String var5 = var1.getRequestLine().getMethod();
      return (HttpUriRequest)(var5.equalsIgnoreCase("HEAD") ? new HttpHead(var4) : new HttpGet(var4));
   }

   public RedirectHandler getHandler() {
      return this.handler;
   }
}
