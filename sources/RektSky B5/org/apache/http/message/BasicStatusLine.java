/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class BasicStatusLine
implements StatusLine,
Cloneable,
Serializable {
    private static final long serialVersionUID = -2443303766890459269L;
    private final ProtocolVersion protoVersion;
    private final int statusCode;
    private final String reasonPhrase;

    public BasicStatusLine(ProtocolVersion version, int statusCode, String reasonPhrase) {
        this.protoVersion = Args.notNull(version, "Version");
        this.statusCode = Args.notNegative(statusCode, "Status code");
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return this.protoVersion;
    }

    @Override
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String toString() {
        return BasicLineFormatter.INSTANCE.formatStatusLine(null, this).toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

