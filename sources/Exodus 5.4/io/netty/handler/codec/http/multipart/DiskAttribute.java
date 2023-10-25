/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.AbstractDiskHttpData;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.IOException;

public class DiskAttribute
extends AbstractDiskHttpData
implements Attribute {
    public static final String postfix = ".att";
    public static boolean deleteOnExitTemporaryFile = true;
    public static String baseDirectory;
    public static final String prefix = "Attr_";

    @Override
    public Attribute duplicate() {
        DiskAttribute diskAttribute = new DiskAttribute(this.getName());
        diskAttribute.setCharset(this.getCharset());
        ByteBuf byteBuf = this.content();
        if (byteBuf != null) {
            try {
                diskAttribute.setContent(byteBuf.duplicate());
            }
            catch (IOException iOException) {
                throw new ChannelException(iOException);
            }
        }
        return diskAttribute;
    }

    @Override
    protected String getBaseDirectory() {
        return baseDirectory;
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

    @Override
    public Attribute retain() {
        super.retain();
        return this;
    }

    @Override
    public String getValue() throws IOException {
        byte[] byArray = this.get();
        return new String(byArray, this.charset.name());
    }

    public DiskAttribute(String string) {
        super(string, HttpConstants.DEFAULT_CHARSET, 0L);
    }

    @Override
    protected String getPrefix() {
        return prefix;
    }

    @Override
    public Attribute retain(int n) {
        super.retain(n);
        return this;
    }

    @Override
    public int compareTo(Attribute attribute) {
        return this.getName().compareToIgnoreCase(attribute.getName());
    }

    @Override
    protected String getDiskFilename() {
        return this.getName() + postfix;
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.Attribute;
    }

    @Override
    protected String getPostfix() {
        return postfix;
    }

    @Override
    public void addContent(ByteBuf byteBuf, boolean bl) throws IOException {
        int n = byteBuf.readableBytes();
        if (this.definedSize > 0L && this.definedSize < this.size + (long)n) {
            this.definedSize = this.size + (long)n;
        }
        super.addContent(byteBuf, bl);
    }

    public boolean equals(Object object) {
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute)object;
        return this.getName().equalsIgnoreCase(attribute.getName());
    }

    @Override
    public Attribute copy() {
        DiskAttribute diskAttribute = new DiskAttribute(this.getName());
        diskAttribute.setCharset(this.getCharset());
        ByteBuf byteBuf = this.content();
        if (byteBuf != null) {
            try {
                diskAttribute.setContent(byteBuf.copy());
            }
            catch (IOException iOException) {
                throw new ChannelException(iOException);
            }
        }
        return diskAttribute;
    }

    @Override
    public int compareTo(InterfaceHttpData interfaceHttpData) {
        if (!(interfaceHttpData instanceof Attribute)) {
            throw new ClassCastException("Cannot compare " + (Object)((Object)this.getHttpDataType()) + " with " + (Object)((Object)interfaceHttpData.getHttpDataType()));
        }
        return this.compareTo((Attribute)interfaceHttpData);
    }

    public DiskAttribute(String string, String string2) throws IOException {
        super(string, HttpConstants.DEFAULT_CHARSET, 0L);
        this.setValue(string2);
    }

    @Override
    protected boolean deleteOnExit() {
        return deleteOnExitTemporaryFile;
    }

    public String toString() {
        try {
            return this.getName() + '=' + this.getValue();
        }
        catch (IOException iOException) {
            return this.getName() + "=IoException";
        }
    }
}

