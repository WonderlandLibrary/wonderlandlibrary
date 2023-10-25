/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.io;

import org.apache.http.HttpMessage;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface HttpMessageParserFactory<T extends HttpMessage> {
    public HttpMessageParser<T> create(SessionInputBuffer var1, MessageConstraints var2);
}

