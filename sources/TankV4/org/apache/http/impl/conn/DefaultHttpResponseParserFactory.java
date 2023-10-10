package org.apache.http.impl.conn;

import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;

@Immutable
public class DefaultHttpResponseParserFactory implements HttpMessageParserFactory {
   public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();
   private final LineParser lineParser;
   private final HttpResponseFactory responseFactory;

   public DefaultHttpResponseParserFactory(LineParser var1, HttpResponseFactory var2) {
      this.lineParser = (LineParser)(var1 != null ? var1 : BasicLineParser.INSTANCE);
      this.responseFactory = (HttpResponseFactory)(var2 != null ? var2 : DefaultHttpResponseFactory.INSTANCE);
   }

   public DefaultHttpResponseParserFactory(HttpResponseFactory var1) {
      this((LineParser)null, var1);
   }

   public DefaultHttpResponseParserFactory() {
      this((LineParser)null, (HttpResponseFactory)null);
   }

   public HttpMessageParser create(SessionInputBuffer var1, MessageConstraints var2) {
      return new DefaultHttpResponseParser(var1, this.lineParser, this.responseFactory, var2);
   }
}
