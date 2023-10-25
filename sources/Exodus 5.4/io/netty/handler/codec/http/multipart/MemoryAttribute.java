/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.AbstractMemoryHttpData;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.IOException;

public class MemoryAttribute
extends AbstractMemoryHttpData
implements Attribute {
    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.Attribute;
    }

    @Override
    public String getValue() {
        return this.getByteBuf().toString(this.charset);
    }

    public String toString() {
        return this.getName() + '=' + this.getValue();
    }

    @Override
    public int compareTo(Attribute attribute) {
        return this.getName().compareToIgnoreCase(attribute.getName());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute)object;
        return this.getName().equalsIgnoreCase(attribute.getName());
    }

    public MemoryAttribute(String string, String string2) throws IOException {
        super(string, HttpConstants.DEFAULT_CHARSET, 0L);
        this.setValue(string2);
    }

    @Override
    public Attribute retain(int n) {
        super.retain(n);
        return this;
    }

    @Override
    public Attribute duplicate() {
        MemoryAttribute memoryAttribute = new MemoryAttribute(this.getName());
        memoryAttribute.setCharset(this.getCharset());
        ByteBuf byteBuf = this.content();
        if (byteBuf != null) {
            try {
                memoryAttribute.setContent(byteBuf.duplicate());
            }
            catch (IOException iOException) {
                throw new ChannelException(iOException);
            }
        }
        return memoryAttribute;
    }

    @Override
    public Attribute copy() {
        MemoryAttribute memoryAttribute = new MemoryAttribute(this.getName());
        memoryAttribute.setCharset(this.getCharset());
        ByteBuf byteBuf = this.content();
        if (byteBuf != null) {
            try {
                memoryAttribute.setContent(byteBuf.copy());
            }
            catch (IOException iOException) {
                throw new ChannelException(iOException);
            }
        }
        return memoryAttribute;
    }

    public MemoryAttribute(String string) {
        super(string, HttpConstants.DEFAULT_CHARSET, 0L);
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public Attribute retain() {
        super.retain();
        return this;
    }

    @Override
    public void addContent(ByteBuf byteBuf, boolean bl) throws IOException {
        int n = byteBuf.readableBytes();
        if (this.definedSize > 0L && this.definedSize < this.size + (long)n) {
            this.definedSize = this.size + (long)n;
        }
        super.addContent(byteBuf, bl);
    }

    @Override
    public int compareTo(InterfaceHttpData interfaceHttpData) {
        if (!(interfaceHttpData instanceof Attribute)) {
            throw new ClassCastException("Cannot compare " + (Object)((Object)this.getHttpDataType()) + " with " + (Object)((Object)interfaceHttpData.getHttpDataType()));
        }
        return this.compareTo((Attribute)interfaceHttpData);
    }

    @Override
    public void setValue(String string) throws IOException {
        if (string == null) {
            throw new NullPointerException("value");
        }
        byte[] byArray = string.getBytes(this.charset.name());
        ByteBuf byteBuf = Unpooled.wrappedBuffer(byArray);
        if (this.definedSize > 0L) {
            this.definedSize = byteBuf.readableBytes();
        }
        this.setContent(byteBuf);
    }
}

