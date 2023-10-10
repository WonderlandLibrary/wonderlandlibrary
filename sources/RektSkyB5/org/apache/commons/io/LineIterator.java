/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator
implements Iterator<String>,
Closeable {
    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished = false;

    public LineIterator(Reader reader) throws IllegalArgumentException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }
        this.bufferedReader = reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
    }

    @Override
    public boolean hasNext() {
        if (this.cachedLine != null) {
            return true;
        }
        if (this.finished) {
            return false;
        }
        try {
            String line;
            do {
                if ((line = this.bufferedReader.readLine()) != null) continue;
                this.finished = true;
                return false;
            } while (!this.isValidLine(line));
            this.cachedLine = line;
            return true;
        }
        catch (IOException ioe) {
            try {
                this.close();
            }
            catch (IOException e2) {
                ioe.addSuppressed(e2);
            }
            throw new IllegalStateException(ioe);
        }
    }

    protected boolean isValidLine(String line) {
        return true;
    }

    @Override
    public String next() {
        return this.nextLine();
    }

    public String nextLine() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        String currentLine = this.cachedLine;
        this.cachedLine = null;
        return currentLine;
    }

    @Override
    public void close() throws IOException {
        this.finished = true;
        this.cachedLine = null;
        if (this.bufferedReader != null) {
            this.bufferedReader.close();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on LineIterator");
    }

    @Deprecated
    public static void closeQuietly(LineIterator iterator) {
        try {
            if (iterator != null) {
                iterator.close();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}

