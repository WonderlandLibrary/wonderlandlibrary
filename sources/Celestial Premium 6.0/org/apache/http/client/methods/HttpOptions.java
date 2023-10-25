/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client.methods;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.Args;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@NotThreadSafe
public class HttpOptions
extends HttpRequestBase {
    public static final String METHOD_NAME = "OPTIONS";

    public HttpOptions() {
    }

    public HttpOptions(URI uri) {
        this.setURI(uri);
    }

    public HttpOptions(String uri) {
        this.setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public Set<String> getAllowedMethods(HttpResponse response) {
        Args.notNull(response, "HTTP response");
        HeaderIterator it = response.headerIterator("Allow");
        HashSet<String> methods = new HashSet<String>();
        while (it.hasNext()) {
            HeaderElement[] elements;
            Header header = it.nextHeader();
            for (HeaderElement element : elements = header.getElements()) {
                methods.add(element.getName());
            }
        }
        return methods;
    }
}

