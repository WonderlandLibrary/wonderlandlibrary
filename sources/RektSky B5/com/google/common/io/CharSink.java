/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.google.common.io.Closer;
import com.google.common.io.OutputSupplier;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class CharSink
implements OutputSupplier<Writer> {
    protected CharSink() {
    }

    public abstract Writer openStream() throws IOException;

    @Override
    @Deprecated
    public final Writer getOutput() throws IOException {
        return this.openStream();
    }

    public Writer openBufferedStream() throws IOException {
        Writer writer = this.openStream();
        return writer instanceof BufferedWriter ? (BufferedWriter)writer : new BufferedWriter(writer);
    }

    public void write(CharSequence charSequence) throws IOException {
        Preconditions.checkNotNull(charSequence);
        Closer closer = Closer.create();
        try {
            Writer out = closer.register(this.openStream());
            out.append(charSequence);
            out.flush();
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public void writeLines(Iterable<? extends CharSequence> lines) throws IOException {
        this.writeLines(lines, System.getProperty("line.separator"));
    }

    public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws IOException {
        Preconditions.checkNotNull(lines);
        Preconditions.checkNotNull(lineSeparator);
        Closer closer = Closer.create();
        try {
            Writer out = closer.register(this.openBufferedStream());
            for (CharSequence charSequence : lines) {
                out.append(charSequence).append(lineSeparator);
            }
            out.flush();
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public long writeFrom(Readable readable) throws IOException {
        Preconditions.checkNotNull(readable);
        Closer closer = Closer.create();
        try {
            Writer out = closer.register(this.openStream());
            long written = CharStreams.copy(readable, (Appendable)out);
            out.flush();
            long l2 = written;
            return l2;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }
}

