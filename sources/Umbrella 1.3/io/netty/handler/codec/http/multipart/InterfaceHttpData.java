/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.util.ReferenceCounted;

public interface InterfaceHttpData
extends Comparable<InterfaceHttpData>,
ReferenceCounted {
    public String getName();

    public HttpDataType getHttpDataType();

    public static enum HttpDataType {
        Attribute,
        FileUpload,
        InternalAttribute;

    }
}

