// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.status.StatusLogger;
import java.nio.charset.CodingErrorAction;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;
import org.apache.logging.log4j.core.util.Constants;
import java.nio.charset.Charset;

public class StringBuilderEncoder implements Encoder<StringBuilder>
{
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
    private final ThreadLocal<Object[]> threadLocal;
    private final Charset charset;
    private final int charBufferSize;
    private final int byteBufferSize;
    
    public StringBuilderEncoder(final Charset charset) {
        this(charset, Constants.ENCODER_CHAR_BUFFER_SIZE, 8192);
    }
    
    public StringBuilderEncoder(final Charset charset, final int charBufferSize, final int byteBufferSize) {
        this.threadLocal = new ThreadLocal<Object[]>();
        this.charBufferSize = charBufferSize;
        this.byteBufferSize = byteBufferSize;
        this.charset = Objects.requireNonNull(charset, "charset");
    }
    
    @Override
    public void encode(final StringBuilder source, final ByteBufferDestination destination) {
        try {
            final Object[] threadLocalState = this.getThreadLocalState();
            final CharsetEncoder charsetEncoder = (CharsetEncoder)threadLocalState[0];
            final CharBuffer charBuffer = (CharBuffer)threadLocalState[1];
            final ByteBuffer byteBuffer = (ByteBuffer)threadLocalState[2];
            TextEncoderHelper.encodeText(charsetEncoder, charBuffer, byteBuffer, source, destination);
        }
        catch (Exception ex) {
            this.logEncodeTextException(ex, source, destination);
            TextEncoderHelper.encodeTextFallBack(this.charset, source, destination);
        }
    }
    
    private Object[] getThreadLocalState() {
        Object[] threadLocalState = this.threadLocal.get();
        if (threadLocalState == null) {
            threadLocalState = new Object[] { this.charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), CharBuffer.allocate(this.charBufferSize), ByteBuffer.allocate(this.byteBufferSize) };
            this.threadLocal.set(threadLocalState);
        }
        else {
            ((CharsetEncoder)threadLocalState[0]).reset();
            ((CharBuffer)threadLocalState[1]).clear();
            ((ByteBuffer)threadLocalState[2]).clear();
        }
        return threadLocalState;
    }
    
    private void logEncodeTextException(final Exception ex, final StringBuilder text, final ByteBufferDestination destination) {
        StatusLogger.getLogger().error("Recovering from StringBuilderEncoder.encode('{}') error: {}", text, ex, ex);
    }
}
