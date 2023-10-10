package org.apache.http.impl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.impl.entity.EntityDeserializer;
import org.apache.http.impl.entity.EntitySerializer;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpRequestWriter;
import org.apache.http.io.EofSensor;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;
import org.apache.http.message.LineParser;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@NotThreadSafe
public abstract class AbstractHttpClientConnection implements HttpClientConnection {
   private final EntitySerializer entityserializer = this.createEntitySerializer();
   private final EntityDeserializer entitydeserializer = this.createEntityDeserializer();
   private SessionInputBuffer inbuffer = null;
   private SessionOutputBuffer outbuffer = null;
   private EofSensor eofSensor = null;
   private HttpMessageParser responseParser = null;
   private HttpMessageWriter requestWriter = null;
   private HttpConnectionMetricsImpl metrics = null;

   protected abstract void assertOpen() throws IllegalStateException;

   protected EntityDeserializer createEntityDeserializer() {
      return new EntityDeserializer(new LaxContentLengthStrategy());
   }

   protected EntitySerializer createEntitySerializer() {
      return new EntitySerializer(new StrictContentLengthStrategy());
   }

   protected HttpResponseFactory createHttpResponseFactory() {
      return DefaultHttpResponseFactory.INSTANCE;
   }

   protected HttpMessageParser createResponseParser(SessionInputBuffer var1, HttpResponseFactory var2, HttpParams var3) {
      return new DefaultHttpResponseParser(var1, (LineParser)null, var2, var3);
   }

   protected HttpMessageWriter createRequestWriter(SessionOutputBuffer var1, HttpParams var2) {
      return new HttpRequestWriter(var1, (LineFormatter)null, var2);
   }

   protected HttpConnectionMetricsImpl createConnectionMetrics(HttpTransportMetrics var1, HttpTransportMetrics var2) {
      return new HttpConnectionMetricsImpl(var1, var2);
   }

   protected void init(SessionInputBuffer var1, SessionOutputBuffer var2, HttpParams var3) {
      this.inbuffer = (SessionInputBuffer)Args.notNull(var1, "Input session buffer");
      this.outbuffer = (SessionOutputBuffer)Args.notNull(var2, "Output session buffer");
      if (var1 instanceof EofSensor) {
         this.eofSensor = (EofSensor)var1;
      }

      this.responseParser = this.createResponseParser(var1, this.createHttpResponseFactory(), var3);
      this.requestWriter = this.createRequestWriter(var2, var3);
      this.metrics = this.createConnectionMetrics(var1.getMetrics(), var2.getMetrics());
   }

   public boolean isResponseAvailable(int var1) throws IOException {
      this.assertOpen();

      try {
         return this.inbuffer.isDataAvailable(var1);
      } catch (SocketTimeoutException var3) {
         return false;
      }
   }

   public void sendRequestHeader(HttpRequest var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      this.assertOpen();
      this.requestWriter.write(var1);
      this.metrics.incrementRequestCount();
   }

   public void sendRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      this.assertOpen();
      if (var1.getEntity() != null) {
         this.entityserializer.serialize(this.outbuffer, var1, var1.getEntity());
      }
   }

   protected void doFlush() throws IOException {
      this.outbuffer.flush();
   }

   public void flush() throws IOException {
      this.assertOpen();
      this.doFlush();
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      this.assertOpen();
      HttpResponse var1 = (HttpResponse)this.responseParser.parse();
      if (var1.getStatusLine().getStatusCode() >= 200) {
         this.metrics.incrementResponseCount();
      }

      return var1;
   }

   public void receiveResponseEntity(HttpResponse var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP response");
      this.assertOpen();
      HttpEntity var2 = this.entitydeserializer.deserialize(this.inbuffer, var1);
      var1.setEntity(var2);
   }

   public boolean isStale() {
      if (!this.isOpen()) {
         return true;
      } else if (this != null) {
         return true;
      } else {
         try {
            this.inbuffer.isDataAvailable(1);
            return this.isEof();
         } catch (SocketTimeoutException var2) {
            return false;
         } catch (IOException var3) {
            return true;
         }
      }
   }

   public HttpConnectionMetrics getMetrics() {
      return this.metrics;
   }
}
