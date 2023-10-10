// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import java.nio.Buffer;
import java.util.Arrays;
import java.nio.charset.CoderResult;
import me.gong.mcleaks.util.google.common.primitives.UnsignedBytes;
import java.io.IOException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.io.Reader;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.io.InputStream;

@GwtIncompatible
final class ReaderInputStream extends InputStream
{
    private final Reader reader;
    private final CharsetEncoder encoder;
    private final byte[] singleByte;
    private CharBuffer charBuffer;
    private ByteBuffer byteBuffer;
    private boolean endOfInput;
    private boolean draining;
    private boolean doneFlushing;
    
    ReaderInputStream(final Reader reader, final Charset charset, final int bufferSize) {
        this(reader, charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
    }
    
    ReaderInputStream(final Reader reader, final CharsetEncoder encoder, final int bufferSize) {
        this.singleByte = new byte[1];
        this.reader = Preconditions.checkNotNull(reader);
        this.encoder = Preconditions.checkNotNull(encoder);
        Preconditions.checkArgument(bufferSize > 0, "bufferSize must be positive: %s", bufferSize);
        encoder.reset();
        (this.charBuffer = CharBuffer.allocate(bufferSize)).flip();
        this.byteBuffer = ByteBuffer.allocate(bufferSize);
    }
    
    @Override
    public void close() throws IOException {
        this.reader.close();
    }
    
    @Override
    public int read() throws IOException {
        return (this.read(this.singleByte) == 1) ? UnsignedBytes.toInt(this.singleByte[0]) : -1;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        Preconditions.checkPositionIndexes(off, off + len, b.length);
        if (len == 0) {
            return 0;
        }
        int totalBytesRead = 0;
        boolean doneEncoding = this.endOfInput;
        while (true) {
            if (this.draining) {
                totalBytesRead += this.drain(b, off + totalBytesRead, len - totalBytesRead);
                if (totalBytesRead == len || this.doneFlushing) {
                    return (totalBytesRead > 0) ? totalBytesRead : -1;
                }
                this.draining = false;
                this.byteBuffer.clear();
            }
            while (true) {
                CoderResult result;
                if (this.doneFlushing) {
                    result = CoderResult.UNDERFLOW;
                }
                else if (doneEncoding) {
                    result = this.encoder.flush(this.byteBuffer);
                }
                else {
                    result = this.encoder.encode(this.charBuffer, this.byteBuffer, this.endOfInput);
                }
                if (result.isOverflow()) {
                    this.startDraining(true);
                    break;
                }
                if (result.isUnderflow()) {
                    if (doneEncoding) {
                        this.doneFlushing = true;
                        this.startDraining(false);
                        break;
                    }
                    if (this.endOfInput) {
                        doneEncoding = true;
                    }
                    else {
                        this.readMoreChars();
                    }
                }
                else {
                    if (result.isError()) {
                        result.throwException();
                        return 0;
                    }
                    continue;
                }
            }
        }
    }
    
    private static CharBuffer grow(final CharBuffer buf) {
        final char[] copy = Arrays.copyOf(buf.array(), buf.capacity() * 2);
        final CharBuffer bigger = CharBuffer.wrap(copy);
        bigger.position(buf.position());
        bigger.limit(buf.limit());
        return bigger;
    }
    
    private void readMoreChars() throws IOException {
        if (availableCapacity(this.charBuffer) == 0) {
            if (this.charBuffer.position() > 0) {
                this.charBuffer.compact().flip();
            }
            else {
                this.charBuffer = grow(this.charBuffer);
            }
        }
        final int limit = this.charBuffer.limit();
        final int numChars = this.reader.read(this.charBuffer.array(), limit, availableCapacity(this.charBuffer));
        if (numChars == -1) {
            this.endOfInput = true;
        }
        else {
            this.charBuffer.limit(limit + numChars);
        }
    }
    
    private static int availableCapacity(final Buffer buffer) {
        return buffer.capacity() - buffer.limit();
    }
    
    private void startDraining(final boolean overflow) {
        this.byteBuffer.flip();
        if (overflow && this.byteBuffer.remaining() == 0) {
            this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() * 2);
        }
        else {
            this.draining = true;
        }
    }
    
    private int drain(final byte[] b, final int off, final int len) {
        final int remaining = Math.min(len, this.byteBuffer.remaining());
        this.byteBuffer.get(b, off, remaining);
        return remaining;
    }
}
