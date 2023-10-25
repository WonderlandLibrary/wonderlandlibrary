package org.apache.http.io;

public interface HttpMessageWriterFactory<T extends org.apache.http.HttpMessage> {
  HttpMessageWriter<T> create(SessionOutputBuffer paramSessionOutputBuffer);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\io\HttpMessageWriterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */