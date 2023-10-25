/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.compressors.gzip;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;

public class GzipCompressorInputStream
extends CompressorInputStream {
    private static final int FHCRC = 2;
    private static final int FEXTRA = 4;
    private static final int FNAME = 8;
    private static final int FCOMMENT = 16;
    private static final int FRESERVED = 224;
    private final InputStream in;
    private final boolean decompressConcatenated;
    private final byte[] buf = new byte[8192];
    private int bufUsed = 0;
    private Inflater inf = new Inflater(true);
    private final CRC32 crc = new CRC32();
    private int memberSize;
    private boolean endReached = false;
    private final byte[] oneByte = new byte[1];
    private final GzipParameters parameters = new GzipParameters();

    public GzipCompressorInputStream(InputStream inputStream) throws IOException {
        this(inputStream, false);
    }

    public GzipCompressorInputStream(InputStream inputStream, boolean decompressConcatenated) throws IOException {
        this.in = inputStream.markSupported() ? inputStream : new BufferedInputStream(inputStream);
        this.decompressConcatenated = decompressConcatenated;
        this.init(true);
    }

    public GzipParameters getMetaData() {
        return this.parameters;
    }

    private boolean init(boolean isFirstMember) throws IOException {
        assert (isFirstMember || this.decompressConcatenated);
        int magic0 = this.in.read();
        int magic1 = this.in.read();
        if (magic0 == -1 && !isFirstMember) {
            return false;
        }
        if (magic0 != 31 || magic1 != 139) {
            throw new IOException(isFirstMember ? "Input is not in the .gz format" : "Garbage after a valid .gz stream");
        }
        DataInputStream inData = new DataInputStream(this.in);
        int method = inData.readUnsignedByte();
        if (method != 8) {
            throw new IOException("Unsupported compression method " + method + " in the .gz header");
        }
        int flg = inData.readUnsignedByte();
        if ((flg & 0xE0) != 0) {
            throw new IOException("Reserved flags are set in the .gz header");
        }
        this.parameters.setModificationTime(this.readLittleEndianInt(inData) * 1000);
        switch (inData.readUnsignedByte()) {
            case 2: {
                this.parameters.setCompressionLevel(9);
                break;
            }
            case 4: {
                this.parameters.setCompressionLevel(1);
                break;
            }
        }
        this.parameters.setOperatingSystem(inData.readUnsignedByte());
        if ((flg & 4) != 0) {
            int xlen = inData.readUnsignedByte();
            xlen |= inData.readUnsignedByte() << 8;
            while (xlen-- > 0) {
                inData.readUnsignedByte();
            }
        }
        if ((flg & 8) != 0) {
            this.parameters.setFilename(new String(this.readToNull(inData), "ISO-8859-1"));
        }
        if ((flg & 0x10) != 0) {
            this.parameters.setComment(new String(this.readToNull(inData), "ISO-8859-1"));
        }
        if ((flg & 2) != 0) {
            inData.readShort();
        }
        this.inf.reset();
        this.crc.reset();
        this.memberSize = 0;
        return true;
    }

    private byte[] readToNull(DataInputStream inData) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int b = 0;
        while ((b = inData.readUnsignedByte()) != 0) {
            bos.write(b);
        }
        return bos.toByteArray();
    }

    private int readLittleEndianInt(DataInputStream inData) throws IOException {
        return inData.readUnsignedByte() | inData.readUnsignedByte() << 8 | inData.readUnsignedByte() << 16 | inData.readUnsignedByte() << 24;
    }

    public int read() throws IOException {
        return this.read(this.oneByte, 0, 1) == -1 ? -1 : this.oneByte[0] & 0xFF;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (this.endReached) {
            return -1;
        }
        int size = 0;
        while (len > 0) {
            int ret;
            if (this.inf.needsInput()) {
                this.in.mark(this.buf.length);
                this.bufUsed = this.in.read(this.buf);
                if (this.bufUsed == -1) {
                    throw new EOFException();
                }
                this.inf.setInput(this.buf, 0, this.bufUsed);
            }
            try {
                ret = this.inf.inflate(b, off, len);
            }
            catch (DataFormatException e) {
                throw new IOException("Gzip-compressed data is corrupt");
            }
            this.crc.update(b, off, ret);
            this.memberSize += ret;
            off += ret;
            len -= ret;
            size += ret;
            this.count(ret);
            if (!this.inf.finished()) continue;
            this.in.reset();
            int skipAmount = this.bufUsed - this.inf.getRemaining();
            if (this.in.skip(skipAmount) != (long)skipAmount) {
                throw new IOException();
            }
            this.bufUsed = 0;
            DataInputStream inData = new DataInputStream(this.in);
            long crcStored = 0L;
            for (int i = 0; i < 4; ++i) {
                crcStored |= (long)inData.readUnsignedByte() << i * 8;
            }
            if (crcStored != this.crc.getValue()) {
                throw new IOException("Gzip-compressed data is corrupt (CRC32 error)");
            }
            int isize = 0;
            for (int i = 0; i < 4; ++i) {
                isize |= inData.readUnsignedByte() << i * 8;
            }
            if (isize != this.memberSize) {
                throw new IOException("Gzip-compressed data is corrupt(uncompressed size mismatch)");
            }
            if (this.decompressConcatenated && this.init(false)) continue;
            this.inf.end();
            this.inf = null;
            this.endReached = true;
            return size == 0 ? -1 : size;
        }
        return size;
    }

    public static boolean matches(byte[] signature, int length) {
        if (length < 2) {
            return false;
        }
        if (signature[0] != 31) {
            return false;
        }
        return signature[1] == -117;
    }

    public void close() throws IOException {
        if (this.inf != null) {
            this.inf.end();
            this.inf = null;
        }
        if (this.in != System.in) {
            this.in.close();
        }
    }
}

