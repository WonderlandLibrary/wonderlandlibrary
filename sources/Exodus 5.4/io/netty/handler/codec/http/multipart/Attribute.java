/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.multipart.HttpData;
import java.io.IOException;

public interface Attribute
extends HttpData {
    public String getValue() throws IOException;

    @Override
    public Attribute retain(int var1);

    @Override
    public Attribute duplicate();

    @Override
    public Attribute copy();

    public void setValue(String var1) throws IOException;

    @Override
    public Attribute retain();
}

