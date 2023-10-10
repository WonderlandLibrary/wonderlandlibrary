/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class ProxyWriter
extends FilterWriter {
    public ProxyWriter(Writer proxy) {
        super(proxy);
    }

    @Override
    public Writer append(char c2) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.append(c2);
            this.afterWrite(1);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        try {
            this.beforeWrite(end - start);
            this.out.append(csq, start, end);
            this.afterWrite(end - start);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
        return this;
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        try {
            int len = 0;
            if (csq != null) {
                len = csq.length();
            }
            this.beforeWrite(len);
            this.out.append(csq);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
        return this;
    }

    @Override
    public void write(int idx) throws IOException {
        try {
            this.beforeWrite(1);
            this.out.write(idx);
            this.afterWrite(1);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(char[] chr) throws IOException {
        try {
            int len = 0;
            if (chr != null) {
                len = chr.length;
            }
            this.beforeWrite(len);
            this.out.write(chr);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(char[] chr, int st, int len) throws IOException {
        try {
            this.beforeWrite(len);
            this.out.write(chr, st, len);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(String str) throws IOException {
        try {
            int len = 0;
            if (str != null) {
                len = str.length();
            }
            this.beforeWrite(len);
            this.out.write(str);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void write(String str, int st, int len) throws IOException {
        try {
            this.beforeWrite(len);
            this.out.write(str, st, len);
            this.afterWrite(len);
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void flush() throws IOException {
        try {
            this.out.flush();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.out.close();
        }
        catch (IOException e2) {
            this.handleIOException(e2);
        }
    }

    protected void beforeWrite(int n2) throws IOException {
    }

    protected void afterWrite(int n2) throws IOException {
    }

    protected void handleIOException(IOException e2) throws IOException {
        throw e2;
    }
}

