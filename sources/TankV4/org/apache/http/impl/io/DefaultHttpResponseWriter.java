package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;

@NotThreadSafe
public class DefaultHttpResponseWriter extends AbstractMessageWriter {
   public DefaultHttpResponseWriter(SessionOutputBuffer var1, LineFormatter var2) {
      super(var1, var2);
   }

   public DefaultHttpResponseWriter(SessionOutputBuffer var1) {
      super(var1, (LineFormatter)null);
   }

   protected void writeHeadLine(HttpResponse var1) throws IOException {
      this.lineFormatter.formatStatusLine(this.lineBuf, var1.getStatusLine());
      this.sessionBuffer.writeLine(this.lineBuf);
   }

   protected void writeHeadLine(HttpMessage var1) throws IOException {
      this.writeHeadLine((HttpResponse)var1);
   }
}
