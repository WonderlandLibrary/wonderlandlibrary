/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.message;

import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.util.CharArrayBuffer;

public interface LineFormatter {
    public CharArrayBuffer appendProtocolVersion(CharArrayBuffer var1, ProtocolVersion var2);

    public CharArrayBuffer formatRequestLine(CharArrayBuffer var1, RequestLine var2);

    public CharArrayBuffer formatStatusLine(CharArrayBuffer var1, StatusLine var2);

    public CharArrayBuffer formatHeader(CharArrayBuffer var1, Header var2);
}

