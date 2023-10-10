// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;

public class TextEncoderHelper
{
    private TextEncoderHelper() {
    }
    
    static void encodeTextFallBack(final Charset charset, final StringBuilder text, final ByteBufferDestination destination) {
        final byte[] bytes = text.toString().getBytes(charset);
        destination.writeBytes(bytes, 0, bytes.length);
    }
    
    static void encodeText(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final ByteBuffer byteBuf, final StringBuilder text, final ByteBufferDestination destination) throws CharacterCodingException {
        charsetEncoder.reset();
        if (text.length() > charBuf.capacity()) {
            encodeChunkedText(charsetEncoder, charBuf, byteBuf, text, destination);
            return;
        }
        charBuf.clear();
        text.getChars(0, text.length(), charBuf.array(), charBuf.arrayOffset());
        charBuf.limit(text.length());
        final CoderResult result = charsetEncoder.encode(charBuf, byteBuf, true);
        writeEncodedText(charsetEncoder, charBuf, byteBuf, destination, result);
    }
    
    private static void writeEncodedText(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final ByteBuffer byteBuf, final ByteBufferDestination destination, CoderResult result) {
        if (!result.isUnderflow()) {
            writeChunkedEncodedText(charsetEncoder, charBuf, destination, byteBuf, result);
            return;
        }
        result = charsetEncoder.flush(byteBuf);
        if (!result.isUnderflow()) {
            synchronized (destination) {
                flushRemainingBytes(charsetEncoder, destination, byteBuf);
            }
            return;
        }
        if (byteBuf != destination.getByteBuffer()) {
            byteBuf.flip();
            destination.writeBytes(byteBuf);
            byteBuf.clear();
        }
    }
    
    private static void writeChunkedEncodedText(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final ByteBufferDestination destination, ByteBuffer byteBuf, final CoderResult result) {
        synchronized (destination) {
            byteBuf = writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, true, destination, byteBuf, result);
            flushRemainingBytes(charsetEncoder, destination, byteBuf);
        }
    }
    
    private static void encodeChunkedText(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, ByteBuffer byteBuf, final StringBuilder text, final ByteBufferDestination destination) {
        int start = 0;
        CoderResult result;
        boolean endOfInput;
        int copied;
        for (result = CoderResult.UNDERFLOW, endOfInput = false; !endOfInput && result.isUnderflow(); result = charsetEncoder.encode(charBuf, byteBuf, endOfInput)) {
            charBuf.clear();
            copied = copy(text, start, charBuf);
            start += copied;
            endOfInput = (start >= text.length());
            charBuf.flip();
        }
        if (endOfInput) {
            writeEncodedText(charsetEncoder, charBuf, byteBuf, destination, result);
            return;
        }
        synchronized (destination) {
            byteBuf = writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, endOfInput, destination, byteBuf, result);
            while (!endOfInput) {
                for (result = CoderResult.UNDERFLOW; !endOfInput && result.isUnderflow(); result = charsetEncoder.encode(charBuf, byteBuf, endOfInput)) {
                    charBuf.clear();
                    final int copied2 = copy(text, start, charBuf);
                    start += copied2;
                    endOfInput = (start >= text.length());
                    charBuf.flip();
                }
                byteBuf = writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, endOfInput, destination, byteBuf, result);
            }
            flushRemainingBytes(charsetEncoder, destination, byteBuf);
        }
    }
    
    @Deprecated
    public static void encodeText(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final ByteBufferDestination destination) {
        charsetEncoder.reset();
        synchronized (destination) {
            ByteBuffer byteBuf = destination.getByteBuffer();
            byteBuf = encodeAsMuchAsPossible(charsetEncoder, charBuf, true, destination, byteBuf);
            flushRemainingBytes(charsetEncoder, destination, byteBuf);
        }
    }
    
    private static ByteBuffer writeAndEncodeAsMuchAsPossible(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final boolean endOfInput, final ByteBufferDestination destination, ByteBuffer temp, CoderResult result) {
        while (true) {
            temp = drainIfByteBufferFull(destination, temp, result);
            if (!result.isOverflow()) {
                break;
            }
            result = charsetEncoder.encode(charBuf, temp, endOfInput);
        }
        if (!result.isUnderflow()) {
            throwException(result);
        }
        return temp;
    }
    
    private static void throwException(final CoderResult result) {
        try {
            result.throwException();
        }
        catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private static ByteBuffer encodeAsMuchAsPossible(final CharsetEncoder charsetEncoder, final CharBuffer charBuf, final boolean endOfInput, final ByteBufferDestination destination, ByteBuffer temp) {
        CoderResult result;
        do {
            result = charsetEncoder.encode(charBuf, temp, endOfInput);
            temp = drainIfByteBufferFull(destination, temp, result);
        } while (result.isOverflow());
        if (!result.isUnderflow()) {
            throwException(result);
        }
        return temp;
    }
    
    private static ByteBuffer drainIfByteBufferFull(final ByteBufferDestination destination, final ByteBuffer temp, final CoderResult result) {
        if (result.isOverflow()) {
            synchronized (destination) {
                final ByteBuffer destinationBuffer = destination.getByteBuffer();
                if (destinationBuffer != temp) {
                    temp.flip();
                    ByteBufferDestinationHelper.writeToUnsynchronized(temp, destination);
                    temp.clear();
                    return destination.getByteBuffer();
                }
                return destination.drain(destinationBuffer);
            }
        }
        return temp;
    }
    
    private static void flushRemainingBytes(final CharsetEncoder charsetEncoder, final ByteBufferDestination destination, ByteBuffer temp) {
        CoderResult result;
        do {
            result = charsetEncoder.flush(temp);
            temp = drainIfByteBufferFull(destination, temp, result);
        } while (result.isOverflow());
        if (!result.isUnderflow()) {
            throwException(result);
        }
        if (temp.remaining() > 0 && temp != destination.getByteBuffer()) {
            temp.flip();
            ByteBufferDestinationHelper.writeToUnsynchronized(temp, destination);
            temp.clear();
        }
    }
    
    static int copy(final StringBuilder source, final int offset, final CharBuffer destination) {
        final int length = Math.min(source.length() - offset, destination.remaining());
        final char[] array = destination.array();
        final int start = destination.position();
        source.getChars(offset, offset + length, array, destination.arrayOffset() + start);
        destination.position(start + length);
        return length;
    }
}
