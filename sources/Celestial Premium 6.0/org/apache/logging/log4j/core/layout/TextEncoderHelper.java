/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import org.apache.logging.log4j.core.layout.ByteBufferDestination;
import org.apache.logging.log4j.core.layout.ByteBufferDestinationHelper;

public class TextEncoderHelper {
    private TextEncoderHelper() {
    }

    static void encodeTextFallBack(Charset charset, StringBuilder text, ByteBufferDestination destination) {
        byte[] bytes = text.toString().getBytes(charset);
        destination.writeBytes(bytes, 0, bytes.length);
    }

    static void encodeText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBuffer byteBuf, StringBuilder text, ByteBufferDestination destination) throws CharacterCodingException {
        charsetEncoder.reset();
        if (text.length() > charBuf.capacity()) {
            TextEncoderHelper.encodeChunkedText(charsetEncoder, charBuf, byteBuf, text, destination);
            return;
        }
        charBuf.clear();
        text.getChars(0, text.length(), charBuf.array(), charBuf.arrayOffset());
        charBuf.limit(text.length());
        CoderResult result = charsetEncoder.encode(charBuf, byteBuf, true);
        TextEncoderHelper.writeEncodedText(charsetEncoder, charBuf, byteBuf, destination, result);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void writeEncodedText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBuffer byteBuf, ByteBufferDestination destination, CoderResult result) {
        if (!result.isUnderflow()) {
            TextEncoderHelper.writeChunkedEncodedText(charsetEncoder, charBuf, destination, byteBuf, result);
            return;
        }
        result = charsetEncoder.flush(byteBuf);
        if (!result.isUnderflow()) {
            ByteBufferDestination byteBufferDestination = destination;
            synchronized (byteBufferDestination) {
                TextEncoderHelper.flushRemainingBytes(charsetEncoder, destination, byteBuf);
            }
            return;
        }
        if (byteBuf != destination.getByteBuffer()) {
            byteBuf.flip();
            destination.writeBytes(byteBuf);
            byteBuf.clear();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void writeChunkedEncodedText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBufferDestination destination, ByteBuffer byteBuf, CoderResult result) {
        ByteBufferDestination byteBufferDestination = destination;
        synchronized (byteBufferDestination) {
            byteBuf = TextEncoderHelper.writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, true, destination, byteBuf, result);
            TextEncoderHelper.flushRemainingBytes(charsetEncoder, destination, byteBuf);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void encodeChunkedText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBuffer byteBuf, StringBuilder text, ByteBufferDestination destination) {
        int start = 0;
        CoderResult result = CoderResult.UNDERFLOW;
        boolean endOfInput = false;
        while (!endOfInput && result.isUnderflow()) {
            charBuf.clear();
            int copied = TextEncoderHelper.copy(text, start, charBuf);
            endOfInput = (start += copied) >= text.length();
            charBuf.flip();
            result = charsetEncoder.encode(charBuf, byteBuf, endOfInput);
        }
        if (endOfInput) {
            TextEncoderHelper.writeEncodedText(charsetEncoder, charBuf, byteBuf, destination, result);
            return;
        }
        ByteBufferDestination byteBufferDestination = destination;
        synchronized (byteBufferDestination) {
            byteBuf = TextEncoderHelper.writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, endOfInput, destination, byteBuf, result);
            while (!endOfInput) {
                result = CoderResult.UNDERFLOW;
                while (!endOfInput && result.isUnderflow()) {
                    charBuf.clear();
                    int copied = TextEncoderHelper.copy(text, start, charBuf);
                    endOfInput = (start += copied) >= text.length();
                    charBuf.flip();
                    result = charsetEncoder.encode(charBuf, byteBuf, endOfInput);
                }
                byteBuf = TextEncoderHelper.writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, endOfInput, destination, byteBuf, result);
            }
            TextEncoderHelper.flushRemainingBytes(charsetEncoder, destination, byteBuf);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public static void encodeText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBufferDestination destination) {
        charsetEncoder.reset();
        ByteBufferDestination byteBufferDestination = destination;
        synchronized (byteBufferDestination) {
            ByteBuffer byteBuf = destination.getByteBuffer();
            byteBuf = TextEncoderHelper.encodeAsMuchAsPossible(charsetEncoder, charBuf, true, destination, byteBuf);
            TextEncoderHelper.flushRemainingBytes(charsetEncoder, destination, byteBuf);
        }
    }

    private static ByteBuffer writeAndEncodeAsMuchAsPossible(CharsetEncoder charsetEncoder, CharBuffer charBuf, boolean endOfInput, ByteBufferDestination destination, ByteBuffer temp, CoderResult result) {
        while (true) {
            temp = TextEncoderHelper.drainIfByteBufferFull(destination, temp, result);
            if (!result.isOverflow()) break;
            result = charsetEncoder.encode(charBuf, temp, endOfInput);
        }
        if (!result.isUnderflow()) {
            TextEncoderHelper.throwException(result);
        }
        return temp;
    }

    private static void throwException(CoderResult result) {
        try {
            result.throwException();
        }
        catch (CharacterCodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static ByteBuffer encodeAsMuchAsPossible(CharsetEncoder charsetEncoder, CharBuffer charBuf, boolean endOfInput, ByteBufferDestination destination, ByteBuffer temp) {
        CoderResult result;
        do {
            result = charsetEncoder.encode(charBuf, temp, endOfInput);
            temp = TextEncoderHelper.drainIfByteBufferFull(destination, temp, result);
        } while (result.isOverflow());
        if (!result.isUnderflow()) {
            TextEncoderHelper.throwException(result);
        }
        return temp;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static ByteBuffer drainIfByteBufferFull(ByteBufferDestination destination, ByteBuffer temp, CoderResult result) {
        if (result.isOverflow()) {
            ByteBufferDestination byteBufferDestination = destination;
            synchronized (byteBufferDestination) {
                ByteBuffer destinationBuffer = destination.getByteBuffer();
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

    private static void flushRemainingBytes(CharsetEncoder charsetEncoder, ByteBufferDestination destination, ByteBuffer temp) {
        CoderResult result;
        do {
            result = charsetEncoder.flush(temp);
            temp = TextEncoderHelper.drainIfByteBufferFull(destination, temp, result);
        } while (result.isOverflow());
        if (!result.isUnderflow()) {
            TextEncoderHelper.throwException(result);
        }
        if (temp.remaining() > 0 && temp != destination.getByteBuffer()) {
            temp.flip();
            ByteBufferDestinationHelper.writeToUnsynchronized(temp, destination);
            temp.clear();
        }
    }

    static int copy(StringBuilder source, int offset, CharBuffer destination) {
        int length = Math.min(source.length() - offset, destination.remaining());
        char[] array = destination.array();
        int start = destination.position();
        source.getChars(offset, offset + length, array, destination.arrayOffset() + start);
        destination.position(start + length);
        return length;
    }
}

