/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.spdy.SpdyHeaderBlockEncoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import java.util.Set;

public class SpdyHeaderBlockRawEncoder
extends SpdyHeaderBlockEncoder {
    private final int version;

    public SpdyHeaderBlockRawEncoder(SpdyVersion spdyVersion) {
        if (spdyVersion == null) {
            throw new NullPointerException("version");
        }
        this.version = spdyVersion.getVersion();
    }

    @Override
    public ByteBuf encode(SpdyHeadersFrame spdyHeadersFrame) throws Exception {
        Set<String> set = spdyHeadersFrame.headers().names();
        int n = set.size();
        if (n == 0) {
            return Unpooled.EMPTY_BUFFER;
        }
        if (n > 65535) {
            throw new IllegalArgumentException("header block contains too many headers");
        }
        ByteBuf byteBuf = Unpooled.buffer();
        SpdyHeaderBlockRawEncoder.writeLengthField(byteBuf, n);
        for (String string : set) {
            byte[] byArray = string.getBytes("UTF-8");
            SpdyHeaderBlockRawEncoder.writeLengthField(byteBuf, byArray.length);
            byteBuf.writeBytes(byArray);
            int n2 = byteBuf.writerIndex();
            int n3 = 0;
            SpdyHeaderBlockRawEncoder.writeLengthField(byteBuf, n3);
            for (String string2 : spdyHeadersFrame.headers().getAll(string)) {
                byte[] byArray2 = string2.getBytes("UTF-8");
                if (byArray2.length <= 0) continue;
                byteBuf.writeBytes(byArray2);
                byteBuf.writeByte(0);
                n3 += byArray2.length + 1;
            }
            if (n3 != 0) {
                --n3;
            }
            if (n3 > 65535) {
                throw new IllegalArgumentException("header exceeds allowable length: " + string);
            }
            if (n3 <= 0) continue;
            SpdyHeaderBlockRawEncoder.setLengthField(byteBuf, n2, n3);
            byteBuf.writerIndex(byteBuf.writerIndex() - 1);
        }
        return byteBuf;
    }

    private static void writeLengthField(ByteBuf byteBuf, int n) {
        byteBuf.writeInt(n);
    }

    private static void setLengthField(ByteBuf byteBuf, int n, int n2) {
        byteBuf.setInt(n, n2);
    }

    @Override
    void end() {
    }
}

