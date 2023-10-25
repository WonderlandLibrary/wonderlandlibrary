package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpParams;

@Deprecated
@NotThreadSafe
public class HttpRequestWriter extends AbstractMessageWriter<HttpRequest> {
  public HttpRequestWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
    super(buffer, formatter, params);
  }
  
  protected void writeHeadLine(HttpRequest message) throws IOException {
    this.lineFormatter.formatRequestLine(this.lineBuf, message.getRequestLine());
    this.sessionBuffer.writeLine(this.lineBuf);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\io\HttpRequestWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */