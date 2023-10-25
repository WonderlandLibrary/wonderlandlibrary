/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface ResponseHandler<T> {
    public T handleResponse(HttpResponse var1) throws ClientProtocolException, IOException;
}

