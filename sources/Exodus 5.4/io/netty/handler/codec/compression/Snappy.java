/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.compression.Crc32c;
import io.netty.handler.codec.compression.DecompressionException;

class Snappy {
    private static final int NOT_ENOUGH_INPUT = -1;
    private static final int COPY_1_BYTE_OFFSET = 1;
    private State state = State.READY;
    private static final int PREAMBLE_NOT_FULL = -1;
    private byte tag;
    private static final int MIN_COMPRESSIBLE_BYTES = 15;
    private int written;
    private static final int MAX_HT_SIZE = 16384;
    private static final int COPY_2_BYTE_OFFSET = 2;
    private static final int LITERAL = 0;
    private static final int COPY_4_BYTE_OFFSET = 3;

    public void reset() {
        this.state = State.READY;
        this.tag = 0;
        this.written = 0;
    }

    Snappy() {
    }

    static void validateChecksum(int n, ByteBuf byteBuf) {
        Snappy.validateChecksum(n, byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes());
    }

    private static int decodeLiteral(byte by, ByteBuf byteBuf, ByteBuf byteBuf2) {
        int n;
        byteBuf.markReaderIndex();
        switch (by >> 2 & 0x3F) {
            case 60: {
                if (!byteBuf.isReadable()) {
                    return -1;
                }
                n = byteBuf.readUnsignedByte();
                break;
            }
            case 61: {
                if (byteBuf.readableBytes() < 2) {
                    return -1;
                }
                n = ByteBufUtil.swapShort(byteBuf.readShort());
                break;
            }
            case 62: {
                if (byteBuf.readableBytes() < 3) {
                    return -1;
                }
                n = ByteBufUtil.swapMedium(byteBuf.readUnsignedMedium());
                break;
            }
            case 64: {
                if (byteBuf.readableBytes() < 4) {
                    return -1;
                }
                n = ByteBufUtil.swapInt(byteBuf.readInt());
                break;
            }
            default: {
                n = by >> 2 & 0x3F;
            }
        }
        if (byteBuf.readableBytes() < ++n) {
            byteBuf.resetReaderIndex();
            return -1;
        }
        byteBuf2.writeBytes(byteBuf, n);
        return n;
    }

    private static void encodeLiteral(ByteBuf byteBuf, ByteBuf byteBuf2, int n) {
        if (n < 61) {
            byteBuf2.writeByte(n - 1 << 2);
        } else {
            int n2 = Snappy.bitsToEncode(n - 1);
            int n3 = 1 + n2 / 8;
            byteBuf2.writeByte(59 + n3 << 2);
            for (int i = 0; i < n3; ++i) {
                byteBuf2.writeByte(n - 1 >> i * 8 & 0xFF);
            }
        }
        byteBuf2.writeBytes(byteBuf, n);
    }

    private static int decodeCopyWith4ByteOffset(byte by, ByteBuf byteBuf, ByteBuf byteBuf2, int n) {
        if (byteBuf.readableBytes() < 4) {
            return -1;
        }
        int n2 = byteBuf2.writerIndex();
        int n3 = 1 + (by >> 2 & 0x3F);
        int n4 = ByteBufUtil.swapInt(byteBuf.readInt());
        Snappy.validateOffset(n4, n);
        byteBuf2.markReaderIndex();
        if (n4 < n3) {
            for (int i = n3 / n4; i > 0; --i) {
                byteBuf2.readerIndex(n2 - n4);
                byteBuf2.readBytes(byteBuf2, n4);
            }
            if (n3 % n4 != 0) {
                byteBuf2.readerIndex(n2 - n4);
                byteBuf2.readBytes(byteBuf2, n3 % n4);
            }
        } else {
            byteBuf2.readerIndex(n2 - n4);
            byteBuf2.readBytes(byteBuf2, n3);
        }
        byteBuf2.resetReaderIndex();
        return n3;
    }

    private static short[] getHashTable(int n) {
        int n2;
        for (n2 = 256; n2 < 16384 && n2 < n; n2 <<= 1) {
        }
        short[] sArray = n2 <= 256 ? new short[256] : new short[16384];
        return sArray;
    }

    public static int calculateChecksum(ByteBuf byteBuf, int n, int n2) {
        Crc32c crc32c = new Crc32c();
        if (byteBuf.hasArray()) {
            crc32c.update(byteBuf.array(), byteBuf.arrayOffset() + n, n2);
        } else {
            byte[] byArray = new byte[n2];
            byteBuf.getBytes(n, byArray);
            crc32c.update(byArray, 0, n2);
        }
        int n3 = Snappy.maskChecksum((int)crc32c.getValue());
        crc32c.reset();
        return n3;
    }

    private static int readPreamble(ByteBuf byteBuf) {
        int n = 0;
        int n2 = 0;
        while (byteBuf.isReadable()) {
            short s = byteBuf.readUnsignedByte();
            n |= (s & 0x7F) << n2++ * 7;
            if ((s & 0x80) == 0) {
                return n;
            }
            if (n2 < 4) continue;
            throw new DecompressionException("Preamble is greater than 4 bytes");
        }
        return 0;
    }

    static int maskChecksum(int n) {
        return (n >> 15 | n << 17) + -1568478504;
    }

    private static int findMatchingLength(ByteBuf byteBuf, int n, int n2, int n3) {
        int n4 = 0;
        while (n2 <= n3 - 4 && byteBuf.getInt(n2) == byteBuf.getInt(n + n4)) {
            n2 += 4;
            n4 += 4;
        }
        while (n2 < n3 && byteBuf.getByte(n + n4) == byteBuf.getByte(n2)) {
            ++n2;
            ++n4;
        }
        return n4;
    }

    static void validateChecksum(int n, ByteBuf byteBuf, int n2, int n3) {
        int n4 = Snappy.calculateChecksum(byteBuf, n2, n3);
        if (n4 != n) {
            throw new DecompressionException("mismatching checksum: " + Integer.toHexString(n4) + " (expected: " + Integer.toHexString(n) + ')');
        }
    }

    private static int bitsToEncode(int n) {
        int n2 = Integer.highestOneBit(n);
        int n3 = 0;
        while ((n2 >>= 1) != 0) {
            ++n3;
        }
        return n3;
    }

    public void decode(ByteBuf byteBuf, ByteBuf byteBuf2) {
        while (byteBuf.isReadable()) {
            block0 : switch (this.state) {
                case READY: {
                    this.state = State.READING_PREAMBLE;
                }
                case READING_PREAMBLE: {
                    int n = Snappy.readPreamble(byteBuf);
                    if (n == -1) {
                        return;
                    }
                    if (n == 0) {
                        this.state = State.READY;
                        return;
                    }
                    byteBuf2.ensureWritable(n);
                    this.state = State.READING_TAG;
                }
                case READING_TAG: {
                    if (!byteBuf.isReadable()) {
                        return;
                    }
                    this.tag = byteBuf.readByte();
                    switch (this.tag & 3) {
                        case 0: {
                            this.state = State.READING_LITERAL;
                            break block0;
                        }
                        case 1: 
                        case 2: 
                        case 3: {
                            this.state = State.READING_COPY;
                        }
                    }
                    break;
                }
                case READING_LITERAL: {
                    int n = Snappy.decodeLiteral(this.tag, byteBuf, byteBuf2);
                    if (n != -1) {
                        this.state = State.READING_TAG;
                        this.written += n;
                        break;
                    }
                    return;
                }
                case READING_COPY: {
                    switch (this.tag & 3) {
                        case 1: {
                            int n = Snappy.decodeCopyWith1ByteOffset(this.tag, byteBuf, byteBuf2, this.written);
                            if (n != -1) {
                                this.state = State.READING_TAG;
                                this.written += n;
                                break block0;
                            }
                            return;
                        }
                        case 2: {
                            int n = Snappy.decodeCopyWith2ByteOffset(this.tag, byteBuf, byteBuf2, this.written);
                            if (n != -1) {
                                this.state = State.READING_TAG;
                                this.written += n;
                                break block0;
                            }
                            return;
                        }
                        case 3: {
                            int n = Snappy.decodeCopyWith4ByteOffset(this.tag, byteBuf, byteBuf2, this.written);
                            if (n != -1) {
                                this.state = State.READING_TAG;
                                this.written += n;
                                break block0;
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    private static void validateOffset(int n, int n2) {
        if (n > Short.MAX_VALUE) {
            throw new DecompressionException("Offset exceeds maximum permissible value");
        }
        if (n <= 0) {
            throw new DecompressionException("Offset is less than minimum permissible value");
        }
        if (n > n2) {
            throw new DecompressionException("Offset exceeds size of chunk");
        }
    }

    private static int decodeCopyWith1ByteOffset(byte by, ByteBuf byteBuf, ByteBuf byteBuf2, int n) {
        if (!byteBuf.isReadable()) {
            return -1;
        }
        int n2 = byteBuf2.writerIndex();
        int n3 = 4 + ((by & 0x1C) >> 2);
        int n4 = (by & 0xE0) << 8 >> 5 | byteBuf.readUnsignedByte();
        Snappy.validateOffset(n4, n);
        byteBuf2.markReaderIndex();
        if (n4 < n3) {
            for (int i = n3 / n4; i > 0; --i) {
                byteBuf2.readerIndex(n2 - n4);
                byteBuf2.readBytes(byteBuf2, n4);
            }
            if (n3 % n4 != 0) {
                byteBuf2.readerIndex(n2 - n4);
                byteBuf2.readBytes(byteBuf2, n3 % n4);
            }
        } else {
            byteBuf2.readerIndex(n2 - n4);
            byteBuf2.readBytes(byteBuf2, n3);
        }
        byteBuf2.resetReaderIndex();
        return n3;
    }

    private static int hash(ByteBuf byteBuf, int n, int n2) {
        return byteBuf.getInt(n) + 506832829 >>> n2;
    }

    public static int calculateChecksum(ByteBuf byteBuf) {
        return Snappy.calculateChecksum(byteBuf, byteBuf.readerIndex(), byteBuf.readableBytes());
    }

    private static void encodeCopyWithOffset(ByteBuf byteBuf, int n, int n2) {
        if (n2 < 12 && n < 2048) {
            byteBuf.writeByte(1 | n2 - 4 << 2 | n >> 8 << 5);
            byteBuf.writeByte(n & 0xFF);
        } else {
            byteBuf.writeByte(2 | n2 - 1 << 2);
            byteBuf.writeByte(n & 0xFF);
            byteBuf.writeByte(n >> 8 & 0xFF);
        }
    }

    private static int decodeCopyWith2ByteOffset(byte by, ByteBuf byteBuf, ByteBuf byteBuf2, int n) {
        if (byteBuf.readableBytes() < 2) {
            return -1;
        }
        int n2 = byteBuf2.writerIndex();
        int n3 = 1 + (by >> 2 & 0x3F);
        short s = ByteBufUtil.swapShort(byteBuf.readShort());
        Snappy.validateOffset(s, n);
        byteBuf2.markReaderIndex();
        if (s < n3) {
            for (int i = n3 / s; i > 0; --i) {
                byteBuf2.readerIndex(n2 - s);
                byteBuf2.readBytes(byteBuf2, (int)s);
            }
            if (n3 % s != 0) {
                byteBuf2.readerIndex(n2 - s);
                byteBuf2.readBytes(byteBuf2, n3 % s);
            }
        } else {
            byteBuf2.readerIndex(n2 - s);
            byteBuf2.readBytes(byteBuf2, n3);
        }
        byteBuf2.resetReaderIndex();
        return n3;
    }

    public void encode(ByteBuf byteBuf, ByteBuf byteBuf2, int n) {
        int n2;
        int n3 = 0;
        while (true) {
            if (((n2 = n >>> n3 * 7) & 0xFFFFFF80) == 0) break;
            byteBuf2.writeByte(n2 & 0x7F | 0x80);
            ++n3;
        }
        byteBuf2.writeByte(n2);
        n2 = n3 = byteBuf.readerIndex();
        short[] sArray = Snappy.getHashTable(n);
        int n4 = 32 - (int)Math.floor(Math.log(sArray.length) / Math.log(2.0));
        int n5 = n3;
        if (n - n3 >= 15) {
            int n6 = Snappy.hash(byteBuf, ++n3, n4);
            block1: while (true) {
                int n7;
                int n8;
                int n9;
                int n10 = 32;
                int n11 = n3;
                do {
                    n3 = n11;
                    n8 = n6;
                    if ((n11 = n3 + (n7 = n10++ >> 5)) > n - 4) break block1;
                    n6 = Snappy.hash(byteBuf, n11, n4);
                    n9 = n2 + sArray[n8];
                    sArray[n8] = (short)(n3 - n2);
                } while (byteBuf.getInt(n3) != byteBuf.getInt(n9));
                Snappy.encodeLiteral(byteBuf, byteBuf2, n3 - n5);
                do {
                    n7 = n3;
                    int n12 = 4 + Snappy.findMatchingLength(byteBuf, n9 + 4, n3 + 4, n);
                    int n13 = n7 - n9;
                    Snappy.encodeCopy(byteBuf2, n13, n12);
                    byteBuf.readerIndex(byteBuf.readerIndex() + n12);
                    n8 = (n3 += n12) - 1;
                    n5 = n3;
                    if (n3 >= n - 4) break block1;
                    int n14 = Snappy.hash(byteBuf, n8, n4);
                    sArray[n14] = (short)(n3 - n2 - 1);
                    int n15 = Snappy.hash(byteBuf, n8 + 1, n4);
                    n9 = n2 + sArray[n15];
                    sArray[n15] = (short)(n3 - n2);
                } while (byteBuf.getInt(n8 + 1) == byteBuf.getInt(n9));
                n6 = Snappy.hash(byteBuf, n8 + 2, n4);
                ++n3;
            }
        }
        if (n5 < n) {
            Snappy.encodeLiteral(byteBuf, byteBuf2, n - n5);
        }
    }

    private static void encodeCopy(ByteBuf byteBuf, int n, int n2) {
        while (n2 >= 68) {
            Snappy.encodeCopyWithOffset(byteBuf, n, 64);
            n2 -= 64;
        }
        if (n2 > 64) {
            Snappy.encodeCopyWithOffset(byteBuf, n, 60);
            n2 -= 60;
        }
        Snappy.encodeCopyWithOffset(byteBuf, n, n2);
    }

    private static enum State {
        READY,
        READING_PREAMBLE,
        READING_TAG,
        READING_LITERAL,
        READING_COPY;

    }
}

