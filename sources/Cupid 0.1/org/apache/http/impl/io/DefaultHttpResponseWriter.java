package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;

@NotThreadSafe
public class DefaultHttpResponseWriter extends AbstractMessageWriter<HttpResponse> {
  public DefaultHttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter) {
    super(buffer, formatter);
  }
  
  public DefaultHttpResponseWriter(SessionOutputBuffer buffer) {
    super(buffer, null);
  }
  
  protected void writeHeadLine(HttpResponse message) throws IOException {
    this.lineFormatter.formatStatusLine(this.lineBuf, message.getStatusLine());
    this.sessionBuffer.writeLine(this.lineBuf);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\io\DefaultHttpResponseWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */