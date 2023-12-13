package org.apache.http.impl.io;

import org.apache.http.annotation.Immutable;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;

@Immutable
public class DefaultHttpResponseWriterFactory implements HttpMessageWriterFactory {
   public static final DefaultHttpResponseWriterFactory INSTANCE = new DefaultHttpResponseWriterFactory();
   private final LineFormatter lineFormatter;

   public DefaultHttpResponseWriterFactory(LineFormatter var1) {
      this.lineFormatter = (LineFormatter)(var1 != null ? var1 : BasicLineFormatter.INSTANCE);
   }

   public DefaultHttpResponseWriterFactory() {
      this((LineFormatter)null);
   }

   public HttpMessageWriter create(SessionOutputBuffer var1) {
      return new DefaultHttpResponseWriter(var1, this.lineFormatter);
   }
}
