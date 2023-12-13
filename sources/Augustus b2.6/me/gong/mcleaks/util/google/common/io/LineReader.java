// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.LinkedList;
import java.util.Queue;
import java.nio.CharBuffer;
import java.io.Reader;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class LineReader
{
    private final Readable readable;
    private final Reader reader;
    private final CharBuffer cbuf;
    private final char[] buf;
    private final Queue<String> lines;
    private final LineBuffer lineBuf;
    
    public LineReader(final Readable readable) {
        this.cbuf = CharStreams.createBuffer();
        this.buf = this.cbuf.array();
        this.lines = new LinkedList<String>();
        this.lineBuf = new LineBuffer() {
            @Override
            protected void handleLine(final String line, final String end) {
                LineReader.this.lines.add(line);
            }
        };
        this.readable = Preconditions.checkNotNull(readable);
        this.reader = ((readable instanceof Reader) ? ((Reader)readable) : null);
    }
    
    @CanIgnoreReturnValue
    public String readLine() throws IOException {
        while (this.lines.peek() == null) {
            this.cbuf.clear();
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
