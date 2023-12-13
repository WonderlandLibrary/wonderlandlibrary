/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;

public interface Header
extends NameValuePair {
    public HeaderElement[] getElements() throws ParseException;
}

