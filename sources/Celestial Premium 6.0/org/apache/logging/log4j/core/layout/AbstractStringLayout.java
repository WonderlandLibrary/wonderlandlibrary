/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.nio.charset.Charset;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.AbstractLayout;

public abstract class AbstractStringLayout
extends AbstractLayout<String> {
    private final Charset charset;

    protected AbstractStringLayout(Charset charset) {
        this.charset = charset;
    }

    @Override
    public byte[] toByteArray(LogEvent event) {
        return ((String)this.toSerializable(event)).getBytes(this.charset);
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    protected Charset getCharset() {
        return this.charset;
    }
}

