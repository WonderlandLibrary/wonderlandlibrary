package org.apache.http.impl.io;

import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;

@Immutable
public class DefaultHttpRequestWriterFactory implements HttpMessageWriterFactory<HttpRequest> {
  public static final DefaultHttpRequestWriterFactory INSTANCE = new DefaultHttpRequestWriterFactory();
  
  private final LineFormatter lineFormatter;
  
  public DefaultHttpRequestWriterFactory(LineFormatter lineFormatter) {
    this.lineFormatter = (lineFormatter != null) ? lineFormatter : (LineFormatter)BasicLineFormatter.INSTANCE;
  }
  
  public DefaultHttpRequestWriterFactory() {
    this(null);
  }
  
  public HttpMessageWriter<HttpRequest> create(SessionOutputBuffer buffer) {
    return new DefaultHttpRequestWriter(buffer, this.lineFormatter);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\io\DefaultHttpRequestWriterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */