package org.apache.http.io;

import org.apache.http.config.MessageConstraints;

public interface HttpMessageParserFactory {
   HttpMessageParser create(SessionInputBuffer var1, MessageConstraints var2);
}
