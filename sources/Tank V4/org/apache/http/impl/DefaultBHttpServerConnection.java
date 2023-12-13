package org.apache.http.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import org.apache.http.impl.io.DefaultHttpRequestParserFactory;
import org.apache.http.impl.io.DefaultHttpResponseWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.util.Args;

@NotThreadSafe
public class DefaultBHttpServerConnection extends BHttpConnectionBase implements HttpServerConnection {
   private final HttpMessageParser requestParser;
   private final HttpMessageWriter responseWriter;

   public DefaultBHttpServerConnection(int var1, int var2, CharsetDecoder var3, CharsetEncoder var4, MessageConstraints var5, ContentLengthStrategy var6, ContentLengthStrategy var7, HttpMessageParserFactory var8, HttpMessageWriterFactory var9) {
      super(var1, var2, var3, var4, var5, (ContentLengthStrategy)(var6 != null ? var6 : DisallowIdentityContentLengthStrategy.INSTANCE), var7);
      this.requestParser = ((HttpMessageParserFactory)(var8 != null ? var8 : DefaultHttpRequestParserFactory.INSTANCE)).create(this.getSessionInputBuffer(), var5);
      this.responseWriter = ((HttpMessageWriterFactory)(var9 != null ? var9 : DefaultHttpResponseWriterFactory.INSTANCE)).create(this.getSessionOutputBuffer());
   }

   public DefaultBHttpServerConnection(int var1, CharsetDecoder var2, CharsetEncoder var3, MessageConstraints var4) {
      this(var1, var1, var2, var3, var4, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory)null, (HttpMessageWriterFactory)null);
   }

   public DefaultBHttpServerConnection(int var1) {
      this(var1, var1, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory)null, (HttpMessageWriterFactory)null);
   }

   protected void onRequestReceived(HttpRequest var1) {
   }

   protected void onResponseSubmitted(HttpResponse var1) {
   }

   public void bind(Socket var1) throws IOException {
      super.bind(var1);
   }

   public HttpRequest receiveRequestHeader() throws HttpException, IOException {
      this.ensureOpen();
      HttpRequest var1 = (HttpRequest)this.requestParser.parse();
      this.onRequestReceived(var1);
      this.incrementRequestCount();
      return var1;
   }

   public void receiveRequestEntity(HttpEntityEnclosingRequest var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      this.ensureOpen();
      HttpEntity var2 = this.prepareInput(var1);
      var1.setEntity(var2);
   }

   public void sendResponseHeader(HttpResponse var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP response");
      this.ensureOpen();
      this.responseWriter.write(var1);
      this.onResponseSubmitted(var1);
      if (var1.getStatusLine().getStatusCode() >= 200) {
         this.incrementResponseCount();
      }

   }

   public void sendResponseEntity(HttpResponse var1) throws HttpException, IOException {
      Args.notNull(var1, "HTTP response");
      this.ensureOpen();
      HttpEntity var2 = var1.getEntity();
      if (var2 != null) {
         OutputStream var3 = this.prepareOutput(var1);
         var2.writeTo(var3);
         var3.close();
      }
   }

   public void flush() throws IOException {
      this.ensureOpen();
      this.doFlush();
   }
}
