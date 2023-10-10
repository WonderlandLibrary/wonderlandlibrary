package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;

@NotThreadSafe
public class DefaultHttpRequestWriter extends AbstractMessageWriter {
   public DefaultHttpRequestWriter(SessionOutputBuffer var1, LineFormatter var2) {
      super(var1, var2);
   }

   public DefaultHttpRequestWriter(SessionOutputBuffer var1) {
      this(var1, (LineFormatter)null);
   }

   protected void writeHeadLine(HttpRequest var1) throws IOException {
      this.lineFormatter.formatRequestLine(this.lineBuf, var1.getRequestLine());
      this.sessionBuffer.writeLine(this.lineBuf);
   }

   protected void writeHeadLine(HttpMessage var1) throws IOException {
      this.writeHeadLine((HttpRequest)var1);
   }
}
