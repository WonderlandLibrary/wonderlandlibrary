// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.nio.Buffer;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import java.nio.CharBuffer;
import javax.annotation.CheckForNull;
import java.io.Reader;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class LineReader
{
    private final Readable readable;
    @CheckForNull
    private final Reader reader;
    private final CharBuffer cbuf;
    private final char[] buf;
    private final Queue<String> lines;
    private final LineBuffer lineBuf;
    
    public LineReader(final Readable readable) {
        this.cbuf = CharStreams.createBuffer();
        this.buf = this.cbuf.array();
        this.lines = new ArrayDeque<String>();
        this.lineBuf = new LineBuffer() {
            @Override
            protected void handleLine(final String line, final String end) {
                LineReader.this.lines.add(line);
            }
        };
        this.readable = Preconditions.checkNotNull(readable);
        this.reader = ((readable instanceof Reader) ? ((Reader)readable) : null);
    }
    
    @CheckForNull
    @CanIgnoreReturnValue
    public String readLine() throws IOException {
        while (this.lines.peek() == null) {
            Java8Compatibility.clear(this.cbuf);
            final int read = (this.reader != null) ? this.reader.read(this.buf, 0, this.buf.length) : this.readable.read(this.cbuf);
            if (read == -1) {
                this.lineBuf.finish();
                break;
            }
            this.lineBuf.add(this.buf, 0, read);
        }
        return this.lines.poll();
    }
}
