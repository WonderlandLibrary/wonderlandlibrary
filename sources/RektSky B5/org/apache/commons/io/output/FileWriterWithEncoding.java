/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.FileUtils;

public class FileWriterWithEncoding
extends Writer {
    private final Writer out;

    public FileWriterWithEncoding(String filename, String encoding) throws IOException {
        this(new File(filename), encoding, false);
    }

    public FileWriterWithEncoding(String filename, String encoding, boolean append) throws IOException {
        this(new File(filename), encoding, append);
    }

    public FileWriterWithEncoding(String filename, Charset encoding) throws IOException {
        this(new File(filename), encoding, false);
    }

    public FileWriterWithEncoding(String filename, Charset encoding, boolean append) throws IOException {
        this(new File(filename), encoding, append);
    }

    public FileWriterWithEncoding(String filename, CharsetEncoder encoding) throws IOException {
        this(new File(filename), encoding, false);
    }

    public FileWriterWithEncoding(String filename, CharsetEncoder encoding, boolean append) throws IOException {
        this(new File(filename), encoding, append);
    }

    public FileWriterWithEncoding(File file, String encoding) throws IOException {
        this(file, encoding, false);
    }

    public FileWriterWithEncoding(File file, String encoding, boolean append) throws IOException {
        this.out = FileWriterWithEncoding.initWriter(file, encoding, append);
    }

    public FileWriterWithEncoding(File file, Charset encoding) throws IOException {
        this(file, encoding, false);
    }

    public FileWriterWithEncoding(File file, Charset encoding, boolean append) throws IOException {
        this.out = FileWriterWithEncoding.initWriter(file, encoding, append);
    }

    public FileWriterWithEncoding(File file, CharsetEncoder encoding) throws IOException {
        this(file, encoding, false);
    }

    public FileWriterWithEncoding(File file, CharsetEncoder encoding, boolean append) throws IOException {
        this.out = FileWriterWithEncoding.initWriter(file, encoding, append);
    }

    private static Writer initWriter(File file, Object encoding, boolean append) throws IOException {
        if (file == null) {
            throw new NullPointerException("File is missing");
        }
        if (encoding == null) {
            throw new NullPointerException("Encoding is missing");
        }
        FileOutputStream stream = null;
        boolean fileExistedAlready = file.exists();
        try {
            stream = new FileOutputStream(file, append);
            if (encoding instanceof Charset) {
                return new OutputStreamWriter((OutputStream)stream, (Charset)encoding);
            }
            if (encoding instanceof CharsetEncoder) {
                return new OutputStreamWriter((OutputStream)stream, (CharsetEncoder)encoding);
            }
            return new OutputStreamWriter((OutputStream)stream, (String)encoding);
        }
        catch (IOException | RuntimeException ex) {
            try {
                if (stream != null) {
                    ((OutputStream)stream).close();
                }
            }
            catch (IOException e2) {
                ex.addSuppressed(e2);
            }
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
    }

    @Override
    public void write(int idx) throws IOException {
        this.out.write(idx);
    }

    @Override
    public void write(char[] chr) throws IOException {
        this.out.write(chr);
    }

    @Override
    public void write(char[] chr, int st, int end) throws IOException {
        this.out.write(chr, st, end);
    }

    @Override
    public void write(String str) throws IOException {
        this.out.write(str);
    }

    @Override
    public void write(String str, int st, int end) throws IOException {
        this.out.write(str, st, end);
    }

    @Override
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override
    public void close() throws IOException {
        this.out.close();
    }
}

