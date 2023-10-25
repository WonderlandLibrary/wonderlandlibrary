package org.apache.http.io;

import org.apache.http.config.MessageConstraints;

public interface HttpMessageParserFactory<T extends org.apache.http.HttpMessage> {
  HttpMessageParser<T> create(SessionInputBuffer paramSessionInputBuffer, MessageConstraints paramMessageConstraints);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\io\HttpMessageParserFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */