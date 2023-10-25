/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.AbstractReferenceCounted;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

final class InternalAttribute
extends AbstractReferenceCounted
implements InterfaceHttpData {
    private final List<ByteBuf> value = new ArrayList<ByteBuf>();
    private final Charset charset;
    private int size;

    public ByteBuf toByteBuf() {
        return ((CompositeByteBuf)Unpooled.compositeBuffer().addComponents(this.value).writerIndex(this.size())).readerIndex(0);
    }

    @Override
    protected void deallocate() {
    }

    public int size() {
        return this.size;
    }

    public void setValue(String string, int n) {
        if (string == null) {
            throw new NullPointerException("value");
        }
        ByteBuf byteBuf = Unpooled.copiedBuffer(string, this.charset);
        ByteBuf byteBuf2 = this.value.set(n, byteBuf);
        if (byteBuf2 != null) {
            this.size -= byteBuf2.readableBytes();
            byteBuf2.release();
        }
        this.size += byteBuf.readableBytes();
    }

    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.InternalAttribute;
    }

    public void addValue(String string) {
        if (string == null) {
            throw new NullPointerException("value");
        }
        ByteBuf byteBuf = Unpooled.copiedBuffer(string, this.charset);
        this.value.add(byteBuf);
        this.size += byteBuf.readableBytes();
    }

    @Override
    public int compareTo(InternalAttribute internalAttribute) {
        return this.getName().compareToIgnoreCase(internalAttribute.getName());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute)object;
        return this.getName().equalsIgnoreCase(attribute.getName());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ByteBuf byteBuf : this.value) {
            stringBuilder.append(byteBuf.toString(this.charset));
        }
        return stringBuilder.toString();
    }

    public void addValue(String string, int n) {
        if (string == null) {
            throw new NullPointerException("value");
        }
        ByteBuf byteBuf = Unpooled.copiedBuffer(string, this.charset);
        this.value.add(n, byteBuf);
        this.size += byteBuf.readableBytes();
    }

    @Override
    public int compareTo(InterfaceHttpData interfaceHttpData) {
        if (!(interfaceHttpData instanceof InternalAttribute)) {
            throw new ClassCastException("Cannot compare " + (Object)((Object)this.getHttpDataType()) + " with " + (Object)((Object)interfaceHttpData.getHttpDataType()));
        }
        return this.compareTo((InternalAttribute)interfaceHttpData);
    }

    @Override
    public String getName() {
        return "InternalAttribute";
    }

    InternalAttribute(Charset charset) {
        this.charset = charset;
    }

    public int hashCode() {
        return this.getName().hashCode();
    }
}

