/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.compression.DecompressionException;
import io.netty.handler.codec.compression.ZlibDecoder;
import io.netty.handler.codec.compression.ZlibWrapper;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class JdkZlibDecoder
extends ZlibDecoder {
    private Inflater inflater;
    private volatile boolean finished;
    private final byte[] dictionary;
    private static final int FCOMMENT = 16;
    private GzipState gzipState = GzipState.HEADER_START;
    private final CRC32 crc;
    private static final int FRESERVED = 224;
    private static final int FNAME = 8;
    private int flags = -1;
    private int xlen = -1;
    private boolean decideZlibOrNone;
    private static final int FHCRC = 2;
    private static final int FEXTRA = 4;

    private static boolean looksLikeZlib(short s) {
        return (s & 0x7800) == 30720 && s % 31 == 0;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        block25: {
            ByteBuf byteBuf2;
            block24: {
                int n;
                if (this.finished) {
                    byteBuf.skipBytes(byteBuf.readableBytes());
                    return;
                }
                if (!byteBuf.isReadable()) {
                    return;
                }
                if (this.decideZlibOrNone) {
                    if (byteBuf.readableBytes() < 2) {
                        return;
                    }
                    n = !JdkZlibDecoder.looksLikeZlib(byteBuf.getShort(0)) ? 1 : 0;
                    this.inflater = new Inflater(n != 0);
                    this.decideZlibOrNone = false;
                }
                if (this.crc != null) {
                    switch (this.gzipState) {
                        case FOOTER_START: {
                            if (this.readGZIPFooter(byteBuf)) {
                                this.finished = true;
                            }
                            return;
                        }
                    }
                    if (this.gzipState != GzipState.HEADER_END && !this.readGZIPHeader(byteBuf)) {
                        return;
                    }
                }
                n = byteBuf.readableBytes();
                if (byteBuf.hasArray()) {
                    this.inflater.setInput(byteBuf.array(), byteBuf.arrayOffset() + byteBuf.readerIndex(), byteBuf.readableBytes());
                } else {
                    byte[] byArray = new byte[byteBuf.readableBytes()];
                    byteBuf.getBytes(byteBuf.readerIndex(), byArray);
                    this.inflater.setInput(byArray);
                }
                int n2 = this.inflater.getRemaining() << 1;
                byteBuf2 = channelHandlerContext.alloc().heapBuffer(n2);
                try {
                    boolean bl = false;
                    byte[] byArray = byteBuf2.array();
                    while (!this.inflater.needsInput()) {
                        int n3 = byteBuf2.writerIndex();
                        int n4 = byteBuf2.arrayOffset() + n3;
                        int n5 = byteBuf2.writableBytes();
                        if (n5 == 0) {
                            list.add(byteBuf2);
                            byteBuf2 = channelHandlerContext.alloc().heapBuffer(n2);
                            byArray = byteBuf2.array();
                            continue;
                        }
                        int n6 = this.inflater.inflate(byArray, n4, n5);
                        if (n6 > 0) {
                            byteBuf2.writerIndex(n3 + n6);
                            if (this.crc != null) {
                                this.crc.update(byArray, n4, n6);
                            }
                        } else if (this.inflater.needsDictionary()) {
                            if (this.dictionary == null) {
                                throw new DecompressionException("decompression failure, unable to set dictionary as non was specified");
                            }
                            this.inflater.setDictionary(this.dictionary);
                        }
                        if (!this.inflater.finished()) continue;
                        if (this.crc == null) {
                            this.finished = true;
                            break;
                        }
                        bl = true;
                        break;
                    }
                    byteBuf.skipBytes(n - this.inflater.getRemaining());
                    if (bl) {
                        this.gzipState = GzipState.FOOTER_START;
                        if (this.readGZIPFooter(byteBuf)) {
                            this.finished = true;
                        }
                    }
                    if (!byteBuf2.isReadable()) break block24;
                    list.add(byteBuf2);
                }
                catch (DataFormatException dataFormatException) {
                    throw new DecompressionException("decompression failure", dataFormatException);
                }
                break block25;
            }
            byteBuf2.release();
        }
    }

    private void verifyCrc(ByteBuf byteBuf) {
        long l = 0L;
        for (int i = 0; i < 4; ++i) {
            l |= (long)byteBuf.readUnsignedByte() << i * 8;
        }
        long l2 = this.crc.getValue();
        if (l != l2) {
            throw new DecompressionException("CRC value missmatch. Expected: " + l + ", Got: " + l2);
        }
    }

    public JdkZlibDecoder(ZlibWrapper zlibWrapper) {
        this(zlibWrapper, null);
    }

    public JdkZlibDecoder(byte[] byArray) {
        this(ZlibWrapper.ZLIB, byArray);
    }

    private boolean readGZIPHeader(ByteBuf byteBuf) {
        switch (this.gzipState) {
            case HEADER_START: {
                if (byteBuf.readableBytes() < 10) {
                    return false;
                }
                byte by = byteBuf.readByte();
                byte by2 = byteBuf.readByte();
                if (by != 31) {
                    throw new DecompressionException("Input is not in the GZIP format");
                }
                this.crc.update(by);
                this.crc.update(by2);
                short s = byteBuf.readUnsignedByte();
                if (s != 8) {
                    throw new DecompressionException("Unsupported compression method " + s + " in the GZIP header");
                }
                this.crc.update(s);
                this.flags = byteBuf.readUnsignedByte();
                this.crc.update(this.flags);
                if ((this.flags & 0xE0) != 0) {
                    throw new DecompressionException("Reserved flags are set in the GZIP header");
                }
                this.crc.update(byteBuf.readByte());
                this.crc.update(byteBuf.readByte());
                this.crc.update(byteBuf.readByte());
                this.crc.update(byteBuf.readByte());
                this.crc.update(byteBuf.readUnsignedByte());
                this.crc.update(byteBuf.readUnsignedByte());
                this.gzipState = GzipState.FLG_READ;
            }
            case FLG_READ: {
                short s;
                if ((this.flags & 4) != 0) {
                    if (byteBuf.readableBytes() < 2) {
                        return false;
                    }
                    s = byteBuf.readUnsignedByte();
                    short s2 = byteBuf.readUnsignedByte();
                    this.crc.update(s);
                    this.crc.update(s2);
                    this.xlen |= s << 8 | s2;
                }
                this.gzipState = GzipState.XLEN_READ;
            }
            case XLEN_READ: {
                if (this.xlen != -1) {
                    if (byteBuf.readableBytes() < this.xlen) {
                        return false;
                    }
                    byte[] byArray = new byte[this.xlen];
                    byteBuf.readBytes(byArray);
                    this.crc.update(byArray);
                }
                this.gzipState = GzipState.SKIP_FNAME;
            }
            case SKIP_FNAME: {
                short s;
                if ((this.flags & 8) != 0) {
                    if (!byteBuf.isReadable()) {
                        return false;
                    }
                    do {
                        s = byteBuf.readUnsignedByte();
                        this.crc.update(s);
                    } while (s != 0 && byteBuf.isReadable());
                }
                this.gzipState = GzipState.SKIP_COMMENT;
            }
            case SKIP_COMMENT: {
                short s;
                if ((this.flags & 0x10) != 0) {
                    if (!byteBuf.isReadable()) {
                        return false;
                    }
                    do {
                        s = byteBuf.readUnsignedByte();
                        this.crc.update(s);
                    } while (s != 0 && byteBuf.isReadable());
                }
                this.gzipState = GzipState.PROCESS_FHCRC;
            }
            case PROCESS_FHCRC: {
                if ((this.flags & 2) != 0) {
                    if (byteBuf.readableBytes() < 4) {
                        return false;
                    }
                    this.verifyCrc(byteBuf);
                }
                this.crc.reset();
                this.gzipState = GzipState.HEADER_END;
            }
            case HEADER_END: {
                return true;
            }
        }
        throw new IllegalStateException();
    }

    public JdkZlibDecoder() {
        this(ZlibWrapper.ZLIB, null);
    }

    private JdkZlibDecoder(ZlibWrapper zlibWrapper, byte[] byArray) {
        if (zlibWrapper == null) {
            throw new NullPointerException("wrapper");
        }
        switch (zlibWrapper) {
            case GZIP: {
                this.inflater = new Inflater(true);
                this.crc = new CRC32();
                break;
            }
            case NONE: {
                this.inflater = new Inflater(true);
                this.crc = null;
                break;
            }
            case ZLIB: {
                this.inflater = new Inflater();
                this.crc = null;
                break;
            }
            case ZLIB_OR_NONE: {
                this.decideZlibOrNone = true;
                this.crc = null;
                break;
            }
            default: {
                throw new IllegalArgumentException("Only GZIP or ZLIB is supported, but you used " + (Object)((Object)zlibWrapper));
            }
        }
        this.dictionary = byArray;
    }

    @Override
    protected void handlerRemoved0(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.handlerRemoved0(channelHandlerContext);
        if (this.inflater != null) {
            this.inflater.end();
        }
    }

    private boolean readGZIPFooter(ByteBuf byteBuf) {
        int n;
        if (byteBuf.readableBytes() < 8) {
            return false;
        }
        this.verifyCrc(byteBuf);
        int n2 = 0;
        for (n = 0; n < 4; ++n) {
            n2 |= byteBuf.readUnsignedByte() << n * 8;
        }
        n = this.inflater.getTotalOut();
        if (n2 != n) {
            throw new DecompressionException("Number of bytes mismatch. Expected: " + n2 + ", Got: " + n);
        }
        return true;
    }

    @Override
    public boolean isClosed() {
        return this.finished;
    }

    private static enum GzipState {
        HEADER_START,
        HEADER_END,
        FLG_READ,
        XLEN_READ,
        SKIP_FNAME,
        SKIP_COMMENT,
        PROCESS_FHCRC,
        FOOTER_START;

    }
}

