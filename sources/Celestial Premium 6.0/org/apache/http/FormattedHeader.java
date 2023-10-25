/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http;

import org.apache.http.Header;
import org.apache.http.util.CharArrayBuffer;

public interface FormattedHeader
extends Header {
    public CharArrayBuffer getBuffer();

    public int getValuePos();
}

