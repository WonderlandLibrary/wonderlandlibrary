/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface HttpMessageParser<T extends HttpMessage> {
    public T parse() throws IOException, HttpException;
}

