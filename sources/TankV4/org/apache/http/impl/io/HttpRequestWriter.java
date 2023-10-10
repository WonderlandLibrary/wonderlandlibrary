package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class HttpRequestWriter extends AbstractMessageWriter {
   public HttpRequestWriter(SessionOutputBuffer var1, LineFormatter var2, HttpParams var3) {
      super(var1, var2, var3);
   }

   protected void writeHeadLine(HttpRequest var1) throws IOException {
      this.lineFormatter.formatRequestLine(this.lineBuf, var1.getRequestLine());
      this.sessionBuffer.writeLine(this.lineBuf);
   }

   protected void writeHeadLine(HttpMessage var1) throws IOException {
      this.writeHeadLine((HttpRequest)var1);
   }
}
