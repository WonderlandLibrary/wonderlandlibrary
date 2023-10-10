// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import org.lwjgl.BufferUtils;
import java.util.zip.DataFormatException;
import java.io.EOFException;
import java.util.zip.Inflater;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.io.InputStream;

public class PNGImageData implements LoadableImageData
{
    private static final byte[] SIGNATURE;
    private static final int IHDR = 1229472850;
    private static final int PLTE = 1347179589;
    private static final int tRNS = 1951551059;
    private static final int IDAT = 1229209940;
    private static final int IEND = 1229278788;
    private static final byte COLOR_GREYSCALE = 0;
    private static final byte COLOR_TRUECOLOR = 2;
    private static final byte COLOR_INDEXED = 3;
    private static final byte COLOR_GREYALPHA = 4;
    private static final byte COLOR_TRUEALPHA = 6;
    private InputStream input;
    private final CRC32 crc;
    private final byte[] buffer;
    private int chunkLength;
    private int chunkType;
    private int chunkRemaining;
    private int width;
    private int height;
    private int colorType;
    private int bytesPerPixel;
    private byte[] palette;
    private byte[] paletteA;
    private byte[] transPixel;
    private int bitDepth;
    private int texWidth;
    private int texHeight;
    private ByteBuffer scratch;
    
    public PNGImageData() {
        this.crc = new CRC32();
        this.buffer = new byte[4096];
    }
    
    private void init(final InputStream input) throws IOException {
        this.input = input;
        final int read = input.read(this.buffer, 0, PNGImageData.SIGNATURE.length);
        if (read != PNGImageData.SIGNATURE.length || !this.checkSignatur(this.buffer)) {
            throw new IOException("Not a valid PNG file");
        }
        this.openChunk(1229472850);
        this.readIHDR();
        this.closeChunk();
    Label_0104:
        while (true) {
            this.openChunk();
            switch (this.chunkType) {
                case 1229209940: {
                    break Label_0104;
                }
                case 1347179589: {
                    this.readPLTE();
                    break;
                }
                case 1951551059: {
                    this.readtRNS();
                    break;
                }
            }
            this.closeChunk();
        }
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public boolean hasAlpha() {
        return this.colorType == 6 || this.paletteA != null || this.transPixel != null;
    }
    
    public boolean isRGB() {
        return this.colorType == 6 || this.colorType == 2 || this.colorType == 3;
    }
    
    private void decode(final ByteBuffer buffer, final int stride, final boolean flip) throws IOException {
        final int offset = buffer.position();
        byte[] curLine = new byte[this.width * this.bytesPerPixel + 1];
        byte[] prevLine = new byte[this.width * this.bytesPerPixel + 1];
        final Inflater inflater = new Inflater();
        try {
            for (int yIndex = 0; yIndex < this.height; ++yIndex) {
                int y = yIndex;
                if (flip) {
                    y = this.height - 1 - yIndex;
                }
                this.readChunkUnzip(inflater, curLine, 0, curLine.length);
                this.unfilter(curLine, prevLine);
                buffer.position(offset + y * stride);
                switch (this.colorType) {
                    case 2:
                    case 6: {
                        this.copy(buffer, curLine);
                        break;
                    }
                    case 3: {
                        this.copyExpand(buffer, curLine);
                        break;
                    }
                    default: {
                        throw new UnsupportedOperationException("Not yet implemented");
                    }
                }
                final byte[] tmp = curLine;
                curLine = prevLine;
                prevLine = tmp;
            }
        }
        finally {
            inflater.end();
        }
        this.bitDepth = (this.hasAlpha() ? 32 : 24);
    }
    
    private void copyExpand(final ByteBuffer buffer, final byte[] curLine) {
        for (int i = 1; i < curLine.length; ++i) {
            final int v = curLine[i] & 0xFF;
            final int index = v * 3;
            for (int j = 0; j < 3; ++j) {
                buffer.put(this.palette[index + j]);
            }
            if (this.hasAlpha()) {
                if (this.paletteA != null) {
                    buffer.put(this.paletteA[v]);
                }
                else {
                    buffer.put((byte)(-1));
                }
            }
        }
    }
    
    private void copy(final ByteBuffer buffer, final byte[] curLine) {
        buffer.put(curLine, 1, curLine.length - 1);
    }
    
    private void unfilter(final byte[] curLine, final byte[] prevLine) throws IOException {
        switch (curLine[0]) {
            case 0: {
                break;
            }
            case 1: {
                this.unfilterSub(curLine);
                break;
            }
            case 2: {
                this.unfilterUp(curLine, prevLine);
                break;
            }
            case 3: {
                this.unfilterAverage(curLine, prevLine);
                break;
            }
            case 4: {
                this.unfilterPaeth(curLine, prevLine);
                break;
            }
            default: {
                throw new IOException("invalide filter type in scanline: " + curLine[0]);
            }
        }
    }
    
    private void unfilterSub(final byte[] curLine) {
        final int bpp = this.bytesPerPixel;
        for (int lineSize = this.width * bpp, i = bpp + 1; i <= lineSize; ++i) {
            final int n = i;
            curLine[n] += curLine[i - bpp];
        }
    }
    
    private void unfilterUp(final byte[] curLine, final byte[] prevLine) {
        final int bpp = this.bytesPerPixel;
        for (int lineSize = this.width * bpp, i = 1; i <= lineSize; ++i) {
            final int n = i;
            curLine[n] += prevLine[i];
        }
    }
    
    private void unfilterAverage(final byte[] curLine, final byte[] prevLine) {
        final int bpp = this.bytesPerPixel;
        final int lineSize = this.width * bpp;
        int i;
        for (i = 1; i <= bpp; ++i) {
            final int n = i;
            curLine[n] += (byte)((prevLine[i] & 0xFF) >>> 1);
        }
        while (i <= lineSize) {
            final int n2 = i;
            curLine[n2] += (byte)((prevLine[i] & 0xFF) + (curLine[i - bpp] & 0xFF) >>> 1);
            ++i;
        }
    }
    
    private void unfilterPaeth(final byte[] curLine, final byte[] prevLine) {
        final int bpp = this.bytesPerPixel;
        final int lineSize = this.width * bpp;
        int i;
        for (i = 1; i <= bpp; ++i) {
            final int n = i;
            curLine[n] += prevLine[i];
        }
        while (i <= lineSize) {
            final int a = curLine[i - bpp] & 0xFF;
            final int b = prevLine[i] & 0xFF;
            int c = prevLine[i - bpp] & 0xFF;
            final int p = a + b - c;
            int pa = p - a;
            if (pa < 0) {
                pa = -pa;
            }
            int pb = p - b;
            if (pb < 0) {
                pb = -pb;
            }
            int pc = p - c;
            if (pc < 0) {
                pc = -pc;
            }
            if (pa <= pb && pa <= pc) {
                c = a;
            }
            else if (pb <= pc) {
                c = b;
            }
            final int n2 = i;
            curLine[n2] += (byte)c;
            ++i;
        }
    }
    
    private void readIHDR() throws IOException {
        this.checkChunkLength(13);
        this.readChunk(this.buffer, 0, 13);
        this.width = this.readInt(this.buffer, 0);
        this.height = this.readInt(this.buffer, 4);
        if (this.buffer[8] != 8) {
            throw new IOException("Unsupported bit depth");
        }
        switch (this.colorType = (this.buffer[9] & 0xFF)) {
            case 0: {
                this.bytesPerPixel = 1;
                break;
            }
            case 2: {
                this.bytesPerPixel = 3;
                break;
            }
            case 6: {
                this.bytesPerPixel = 4;
                break;
            }
            case 3: {
                this.bytesPerPixel = 1;
                break;
            }
            default: {
                throw new IOException("unsupported color format");
            }
        }
        if (this.buffer[10] != 0) {
            throw new IOException("unsupported compression method");
        }
        if (this.buffer[11] != 0) {
            throw new IOException("unsupported filtering method");
        }
        if (this.buffer[12] != 0) {
            throw new IOException("unsupported interlace method");
        }
    }
    
    private void readPLTE() throws IOException {
        final int paletteEntries = this.chunkLength / 3;
        if (paletteEntries < 1 || paletteEntries > 256 || this.chunkLength % 3 != 0) {
            throw new IOException("PLTE chunk has wrong length");
        }
        this.readChunk(this.palette = new byte[paletteEntries * 3], 0, this.palette.length);
    }
    
    private void readtRNS() throws IOException {
        switch (this.colorType) {
            case 0: {
                this.checkChunkLength(2);
                this.readChunk(this.transPixel = new byte[2], 0, 2);
                break;
            }
            case 2: {
                this.checkChunkLength(6);
                this.readChunk(this.transPixel = new byte[6], 0, 6);
                break;
            }
            case 3: {
                if (this.palette == null) {
                    throw new IOException("tRNS chunk without PLTE chunk");
                }
                this.paletteA = new byte[this.palette.length / 3];
                for (int i = 0; i < this.paletteA.length; ++i) {
                    this.paletteA[i] = -1;
                }
                this.readChunk(this.paletteA, 0, this.paletteA.length);
                break;
            }
        }
    }
    
    private void closeChunk() throws IOException {
        if (this.chunkRemaining > 0) {
            this.input.skip(this.chunkRemaining + 4);
        }
        else {
            this.readFully(this.buffer, 0, 4);
            final int expectedCrc = this.readInt(this.buffer, 0);
            final int computedCrc = (int)this.crc.getValue();
            if (computedCrc != expectedCrc) {
                throw new IOException("Invalid CRC");
            }
        }
        this.chunkRemaining = 0;
        this.chunkLength = 0;
        this.chunkType = 0;
    }
    
    private void openChunk() throws IOException {
        this.readFully(this.buffer, 0, 8);
        this.chunkLength = this.readInt(this.buffer, 0);
        this.chunkType = this.readInt(this.buffer, 4);
        this.chunkRemaining = this.chunkLength;
        this.crc.reset();
        this.crc.update(this.buffer, 4, 4);
    }
    
    private void openChunk(final int expected) throws IOException {
        this.openChunk();
        if (this.chunkType != expected) {
            throw new IOException("Expected chunk: " + Integer.toHexString(expected));
        }
    }
    
    private void checkChunkLength(final int expected) throws IOException {
        if (this.chunkLength != expected) {
            throw new IOException("Chunk has wrong size");
        }
    }
    
    private int readChunk(final byte[] buffer, final int offset, int length) throws IOException {
        if (length > this.chunkRemaining) {
            length = this.chunkRemaining;
        }
        this.readFully(buffer, offset, length);
        this.crc.update(buffer, offset, length);
        this.chunkRemaining -= length;
        return length;
    }
    
    private void refillInflater(final Inflater inflater) throws IOException {
        while (this.chunkRemaining == 0) {
            this.closeChunk();
            this.openChunk(1229209940);
        }
        final int read = this.readChunk(this.buffer, 0, this.buffer.length);
        inflater.setInput(this.buffer, 0, read);
    }
    
    private void readChunkUnzip(final Inflater inflater, final byte[] buffer, int offset, int length) throws IOException {
        try {
            do {
                final int read = inflater.inflate(buffer, offset, length);
                if (read <= 0) {
                    if (inflater.finished()) {
                        throw new EOFException();
                    }
                    if (!inflater.needsInput()) {
                        throw new IOException("Can't inflate " + length + " bytes");
                    }
                    this.refillInflater(inflater);
                }
                else {
                    offset += read;
                    length -= read;
                }
            } while (length > 0);
        }
        catch (DataFormatException ex) {
            final IOException io = new IOException("inflate error");
            io.initCause(ex);
            throw io;
        }
    }
    
    private void readFully(final byte[] buffer, int offset, int length) throws IOException {
        do {
            final int read = this.input.read(buffer, offset, length);
            if (read < 0) {
                throw new EOFException();
            }
            offset += read;
            length -= read;
        } while (length > 0);
    }
    
    private int readInt(final byte[] buffer, final int offset) {
        return buffer[offset] << 24 | (buffer[offset + 1] & 0xFF) << 16 | (buffer[offset + 2] & 0xFF) << 8 | (buffer[offset + 3] & 0xFF);
    }
    
    private boolean checkSignatur(final byte[] buffer) {
        for (int i = 0; i < PNGImageData.SIGNATURE.length; ++i) {
            if (buffer[i] != PNGImageData.SIGNATURE[i]) {
                return false;
            }
        }
        return true;
    }
    
    public int getDepth() {
        return this.bitDepth;
    }
    
    public ByteBuffer getImageBufferData() {
        return this.scratch;
    }
    
    public int getTexHeight() {
        return this.texHeight;
    }
    
    public int getTexWidth() {
        return this.texWidth;
    }
    
    public ByteBuffer loadImage(final InputStream fis) throws IOException {
        return this.loadImage(fis, false, null);
    }
    
    public ByteBuffer loadImage(final InputStream fis, final boolean flipped, final int[] transparent) throws IOException {
        return this.loadImage(fis, flipped, false, transparent);
    }
    
    public ByteBuffer loadImage(final InputStream fis, final boolean flipped, boolean forceAlpha, final int[] transparent) throws IOException {
        if (transparent != null) {
            forceAlpha = true;
        }
        this.init(fis);
        if (!this.isRGB()) {
            throw new IOException("Only RGB formatted images are supported by the PNGLoader");
        }
        this.texWidth = this.get2Fold(this.width);
        this.texHeight = this.get2Fold(this.height);
        final int perPixel = this.hasAlpha() ? 4 : 3;
        this.decode(this.scratch = BufferUtils.createByteBuffer(this.texWidth * this.texHeight * perPixel), this.texWidth * perPixel, flipped);
        if (this.height < this.texHeight - 1) {
            final int topOffset = (this.texHeight - 1) * (this.texWidth * perPixel);
            final int bottomOffset = (this.height - 1) * (this.texWidth * perPixel);
            for (int x = 0; x < this.texWidth; ++x) {
                for (int i = 0; i < perPixel; ++i) {
                    this.scratch.put(topOffset + x + i, this.scratch.get(x + i));
                    this.scratch.put(bottomOffset + this.texWidth * perPixel + x + i, this.scratch.get(bottomOffset + x + i));
                }
            }
        }
        if (this.width < this.texWidth - 1) {
            for (int y = 0; y < this.texHeight; ++y) {
                for (int j = 0; j < perPixel; ++j) {
                    this.scratch.put((y + 1) * (this.texWidth * perPixel) - perPixel + j, this.scratch.get(y * (this.texWidth * perPixel) + j));
                    this.scratch.put(y * (this.texWidth * perPixel) + this.width * perPixel + j, this.scratch.get(y * (this.texWidth * perPixel) + (this.width - 1) * perPixel + j));
                }
            }
        }
        if (!this.hasAlpha() && forceAlpha) {
            final ByteBuffer temp = BufferUtils.createByteBuffer(this.texWidth * this.texHeight * 4);
            for (int x2 = 0; x2 < this.texWidth; ++x2) {
                for (int y2 = 0; y2 < this.texHeight; ++y2) {
                    final int srcOffset = y2 * 3 + x2 * this.texHeight * 3;
                    final int dstOffset = y2 * 4 + x2 * this.texHeight * 4;
                    temp.put(dstOffset, this.scratch.get(srcOffset));
                    temp.put(dstOffset + 1, this.scratch.get(srcOffset + 1));
                    temp.put(dstOffset + 2, this.scratch.get(srcOffset + 2));
                    temp.put(dstOffset + 3, (byte)(-1));
                }
            }
            this.colorType = 6;
            this.bitDepth = 32;
            this.scratch = temp;
        }
        if (transparent != null) {
            for (int k = 0; k < this.texWidth * this.texHeight * 4; k += 4) {
                boolean match = true;
                for (int c = 0; c < 3; ++c) {
                    if (this.toInt(this.scratch.get(k + c)) != transparent[c]) {
                        match = false;
                    }
                }
                if (match) {
                    this.scratch.put(k + 3, (byte)0);
                }
            }
        }
        this.scratch.position(0);
        return this.scratch;
    }
    
    private int toInt(final byte b) {
        if (b < 0) {
            return 256 + b;
        }
        return b;
    }
    
    private int get2Fold(final int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {}
        return ret;
    }
    
    public void configureEdging(final boolean edging) {
    }
    
    static {
        SIGNATURE = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };
    }
}
