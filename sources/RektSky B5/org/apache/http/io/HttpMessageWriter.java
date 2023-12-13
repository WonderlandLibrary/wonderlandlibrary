/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

public interface HttpMessageWriter<T extends HttpMessage> {
    public void write(T var1) throws IOException, HttpException;
}

