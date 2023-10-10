package org.apache.http.client.methods;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderGroup;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

@NotThreadSafe
public class RequestBuilder {
   private String method;
   private ProtocolVersion version;
   private URI uri;
   private HeaderGroup headergroup;
   private HttpEntity entity;
   private LinkedList parameters;
   private RequestConfig config;

   RequestBuilder(String var1) {
      this.method = var1;
   }

   RequestBuilder() {
      this((String)null);
   }

   public static RequestBuilder create(String var0) {
      Args.notBlank(var0, "HTTP method");
      return new RequestBuilder(var0);
   }

   public static RequestBuilder get() {
      return new RequestBuilder("GET");
   }

   public static RequestBuilder head() {
      return new RequestBuilder("HEAD");
   }

   public static RequestBuilder post() {
      return new RequestBuilder("POST");
   }

   public static RequestBuilder put() {
      return new RequestBuilder("PUT");
   }

   public static RequestBuilder delete() {
      return new RequestBuilder("DELETE");
   }

   public static RequestBuilder trace() {
      return new RequestBuilder("TRACE");
   }

   public static RequestBuilder options() {
      return new RequestBuilder("OPTIONS");
   }

   public static RequestBuilder copy(HttpRequest var0) {
      Args.notNull(var0, "HTTP request");
      return (new RequestBuilder()).doCopy(var0);
   }

   private RequestBuilder doCopy(HttpRequest var1) {
      if (var1 == null) {
         return this;
      } else {
         this.method = var1.getRequestLine().getMethod();
         this.version = var1.getRequestLine().getProtocolVersion();
         if (var1 instanceof HttpUriRequest) {
            this.uri = ((HttpUriRequest)var1).getURI();
         } else {
            this.uri = URI.create(var1.getRequestLine().getMethod());
         }

         if (this.headergroup == null) {
            this.headergroup = new HeaderGroup();
         }

         this.headergroup.clear();
         this.headergroup.setHeaders(var1.getAllHeaders());
         if (var1 instanceof HttpEntityEnclosingRequest) {
            this.entity = ((HttpEntityEnclosingRequest)var1).getEntity();
         } else {
            this.entity = null;
         }

         if (var1 instanceof Configurable) {
            this.config = ((Configurable)var1).getConfig();
         } else {
            this.config = null;
         }

         this.parameters = null;
         return this;
      }
   }

   public String getMethod() {
      return this.method;
   }

   public ProtocolVersion getVersion() {
      return this.version;
   }

   public RequestBuilder setVersion(ProtocolVersion var1) {
      this.version = var1;
      return this;
   }

   public URI getUri() {
      return this.uri;
   }

   public RequestBuilder setUri(URI var1) {
      this.uri = var1;
      return this;
   }

   public RequestBuilder setUri(String var1) {
      this.uri = var1 != null ? URI.create(var1) : null;
      return this;
   }

   public Header getFirstHeader(String var1) {
      return this.headergroup != null ? this.headergroup.getFirstHeader(var1) : null;
   }

   public Header getLastHeader(String var1) {
      return this.headergroup != null ? this.headergroup.getLastHeader(var1) : null;
   }

   public Header[] getHeaders(String var1) {
      return this.headergroup != null ? this.headergroup.getHeaders(var1) : null;
   }

   public RequestBuilder addHeader(Header var1) {
      if (this.headergroup == null) {
         this.headergroup = new HeaderGroup();
      }

      this.headergroup.addHeader(var1);
      return this;
   }

   public RequestBuilder addHeader(String var1, String var2) {
      if (this.headergroup == null) {
         this.headergroup = new HeaderGroup();
      }

      this.headergroup.addHeader(new BasicHeader(var1, var2));
      return this;
   }

   public RequestBuilder removeHeader(Header var1) {
      if (this.headergroup == null) {
         this.headergroup = new HeaderGroup();
      }

      this.headergroup.removeHeader(var1);
      return this;
   }

   public RequestBuilder removeHeaders(String var1) {
      if (var1 != null && this.headergroup != null) {
         HeaderIterator var2 = this.headergroup.iterator();

         while(var2.hasNext()) {
            Header var3 = var2.nextHeader();
            if (var1.equalsIgnoreCase(var3.getName())) {
               var2.remove();
            }
         }

         return this;
      } else {
         return this;
      }
   }

   public RequestBuilder setHeader(Header var1) {
      if (this.headergroup == null) {
         this.headergroup = new HeaderGroup();
      }

      this.headergroup.updateHeader(var1);
      return this;
   }

   public RequestBuilder setHeader(String var1, String var2) {
      if (this.headergroup == null) {
         this.headergroup = new HeaderGroup();
      }

      this.headergroup.updateHeader(new BasicHeader(var1, var2));
      return this;
   }

   public HttpEntity getEntity() {
      return this.entity;
   }

   public RequestBuilder setEntity(HttpEntity var1) {
      this.entity = var1;
      return this;
   }

   public List getParameters() {
      return this.parameters != null ? new ArrayList(this.parameters) : new ArrayList();
   }

   public RequestBuilder addParameter(NameValuePair var1) {
      Args.notNull(var1, "Name value pair");
      if (this.parameters == null) {
         this.parameters = new LinkedList();
      }

      this.parameters.add(var1);
      return this;
   }

   public RequestBuilder addParameter(String var1, String var2) {
      return this.addParameter(new BasicNameValuePair(var1, var2));
   }

   public RequestBuilder addParameters(NameValuePair... var1) {
      NameValuePair[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         NameValuePair var5 = var2[var4];
         this.addParameter(var5);
      }

      return this;
   }

   public RequestConfig getConfig() {
      return this.config;
   }

   public RequestBuilder setConfig(RequestConfig var1) {
      this.config = var1;
      return this;
   }

   public HttpUriRequest build() {
      URI var2 = this.uri != null ? this.uri : URI.create("/");
      Object var3 = this.entity;
      if (this.parameters != null && !this.parameters.isEmpty()) {
         if (var3 != null || !"POST".equalsIgnoreCase(this.method) && !"PUT".equalsIgnoreCase(this.method)) {
            try {
               var2 = (new URIBuilder(var2)).addParameters(this.parameters).build();
            } catch (URISyntaxException var5) {
            }
         } else {
            var3 = new UrlEncodedFormEntity(this.parameters, HTTP.DEF_CONTENT_CHARSET);
         }
      }

      Object var1;
      if (var3 == null) {
         var1 = new RequestBuilder.InternalRequest(this.method);
      } else {
         RequestBuilder.InternalEntityEclosingRequest var4 = new RequestBuilder.InternalEntityEclosingRequest(this.method);
         var4.setEntity((HttpEntity)var3);
         var1 = var4;
      }

      ((HttpRequestBase)var1).setProtocolVersion(this.version);
      ((HttpRequestBase)var1).setURI(var2);
      if (this.headergroup != null) {
         ((HttpRequestBase)var1).setHeaders(this.headergroup.getAllHeaders());
      }

      ((HttpRequestBase)var1).setConfig(this.config);
      return (HttpUriRequest)var1;
   }

   static class InternalEntityEclosingRequest extends HttpEntityEnclosingRequestBase {
      private final String method;

      InternalEntityEclosingRequest(String var1) {
         this.method = var1;
      }

      public String getMethod() {
         return this.method;
      }
   }

   static class InternalRequest extends HttpRequestBase {
      private final String method;

      InternalRequest(String var1) {
         this.method = var1;
      }

      public String getMethod() {
         return this.method;
      }
   }
}
