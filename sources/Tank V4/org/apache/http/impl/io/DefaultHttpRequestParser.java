package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.ParseException;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class DefaultHttpRequestParser extends AbstractMessageParser {
   private final HttpRequestFactory requestFactory;
   private final CharArrayBuffer lineBuf;

   /** @deprecated */
   @Deprecated
   public DefaultHttpRequestParser(SessionInputBuffer var1, LineParser var2, HttpRequestFactory var3, HttpParams var4) {
      super(var1, var2, var4);
      this.requestFactory = (HttpRequestFactory)Args.notNull(var3, "Request factory");
      this.lineBuf = new CharArrayBuffer(128);
   }

   public DefaultHttpRequestParser(SessionInputBuffer var1, LineParser var2, HttpRequestFactory var3, MessageConstraints var4) {
      super(var1, var2, var4);
      this.requestFactory = (HttpRequestFactory)(var3 != null ? var3 : DefaultHttpRequestFactory.INSTANCE);
      this.lineBuf = new CharArrayBuffer(128);
   }

   public DefaultHttpRequestParser(SessionInputBuffer var1, MessageConstraints var2) {
      this(var1, (LineParser)null, (HttpRequestFactory)null, (MessageConstraints)var2);
   }

   public DefaultHttpRequestParser(SessionInputBuffer var1) {
      this(var1, (LineParser)null, (HttpRequestFactory)null, (MessageConstraints)MessageConstraints.DEFAULT);
   }

   protected HttpRequest parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException {
      this.lineBuf.clear();
      int var2 = var1.readLine(this.lineBuf);
      if (var2 == -1) {
         throw new ConnectionClosedException("Client closed connection");
      } else {
         ParserCursor var3 = new ParserCursor(0, this.lineBuf.length());
         RequestLine var4 = this.lineParser.parseRequestLine(this.lineBuf, var3);
         return this.requestFactory.newHttpRequest(var4);
      }
   }

   protected HttpMessage parseHead(SessionInputBuffer var1) throws IOException, HttpException, ParseException {
      return this.parseHead(var1);
   }
}
