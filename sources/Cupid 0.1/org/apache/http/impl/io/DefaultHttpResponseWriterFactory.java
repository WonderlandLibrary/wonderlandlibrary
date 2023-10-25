package org.apache.http.impl.io;

import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;

@Immutable
public class DefaultHttpResponseWriterFactory implements HttpMessageWriterFactory<HttpResponse> {
  public static final DefaultHttpResponseWriterFactory INSTANCE = new DefaultHttpResponseWriterFactory();
  
  private final LineFormatter lineFormatter;
  
  public DefaultHttpResponseWriterFactory(LineFormatter lineFormatter) {
    this.lineFormatter = (lineFormatter != null) ? lineFormatter : (LineFormatter)BasicLineFormatter.INSTANCE;
  }
  
  public DefaultHttpResponseWriterFactory() {
    this(null);
  }
  
  public HttpMessageWriter<HttpResponse> create(SessionOutputBuffer buffer) {
    return new DefaultHttpResponseWriter(buffer, this.lineFormatter);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\io\DefaultHttpResponseWriterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */