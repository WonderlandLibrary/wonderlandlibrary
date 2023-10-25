/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.util.CharArrayBuffer;

public interface HeaderValueFormatter {
    public CharArrayBuffer formatElements(CharArrayBuffer var1, HeaderElement[] var2, boolean var3);

    public CharArrayBuffer formatHeaderElement(CharArrayBuffer var1, HeaderElement var2, boolean var3);

    public CharArrayBuffer formatParameters(CharArrayBuffer var1, NameValuePair[] var2, boolean var3);

    public CharArrayBuffer formatNameValuePair(CharArrayBuffer var1, NameValuePair var2, boolean var3);
}

