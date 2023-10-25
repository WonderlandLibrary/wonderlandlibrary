package org.apache.http.io;

import java.io.IOException;
import org.apache.http.HttpException;

public interface HttpMessageWriter<T extends org.apache.http.HttpMessage> {
  void write(T paramT) throws IOException, HttpException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\io\HttpMessageWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */