/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.input;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

public class Tailer
implements Runnable {
    private static final int DEFAULT_DELAY_MILLIS = 1000;
    private static final String RAF_MODE = "r";
    private static final int DEFAULT_BUFSIZE = 4096;
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
    private final byte[] inbuf;
    private final File file;
    private final Charset cset;
    private final long delayMillis;
    private final boolean end;
    private final TailerListener listener;
    private final boolean reOpen;
    private volatile boolean run = true;

    public Tailer(File file, TailerListener listener) {
        this(file, listener, 1000L);
    }

    public Tailer(File file, TailerListener listener, long delayMillis) {
        this(file, listener, delayMillis, false);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end) {
        this(file, listener, delayMillis, end, 4096);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
        this(file, listener, delayMillis, end, reOpen, 4096);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
        this(file, listener, delayMillis, end, false, bufSize);
    }

    public Tailer(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        this(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufSize);
    }

    public Tailer(File file, Charset cset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        this.file = file;
        this.delayMillis = delayMillis;
        this.end = end;
        this.inbuf = new byte[bufSize];
        this.listener = listener;
        listener.init(this);
        this.reOpen = reOpen;
        this.cset = cset;
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, int bufSize) {
        return Tailer.create(file, listener, delayMillis, end, false, bufSize);
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        return Tailer.create(file, DEFAULT_CHARSET, listener, delayMillis, end, reOpen, bufSize);
    }

    public static Tailer create(File file, Charset charset, TailerListener listener, long delayMillis, boolean end, boolean reOpen, int bufSize) {
        Tailer tailer = new Tailer(file, charset, listener, delayMillis, end, reOpen, bufSize);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        return tailer;
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end) {
        return Tailer.create(file, listener, delayMillis, end, 4096);
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis, boolean end, boolean reOpen) {
        return Tailer.create(file, listener, delayMillis, end, reOpen, 4096);
    }

    public static Tailer create(File file, TailerListener listener, long delayMillis) {
        return Tailer.create(file, listener, delayMillis, false);
    }

    public static Tailer create(File file, TailerListener listener) {
        return Tailer.create(file, listener, 1000L, false);
    }

    public File getFile() {
        return this.file;
    }

    protected boolean getRun() {
        return this.run;
    }

    public long getDelay() {
        return this.delayMillis;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        RandomAccessFile reader = null;
        try {
            long last = 0L;
            long position = 0L;
            while (this.getRun() && reader == null) {
                try {
                    reader = new RandomAccessFile(this.file, RAF_MODE);
                }
                catch (FileNotFoundException e) {
                    this.listener.fileNotFound();
                }
                if (reader == null) {
                    Thread.sleep(this.delayMillis);
                    continue;
                }
                position = this.end ? this.file.length() : 0L;
                last = this.file.lastModified();
                reader.seek(position);
            }
            while (this.getRun()) {
                boolean newer = FileUtils.isFileNewer(this.file, last);
                long length = this.file.length();
                if (length < position) {
                    this.listener.fileRotated();
                    try {
                        RandomAccessFile save = reader;
                        reader = new RandomAccessFile(this.file, RAF_MODE);
                        try {
                            this.readLines(save);
                        }
                        catch (IOException ioe) {
                            this.listener.handle(ioe);
                        }
                        position = 0L;
                        IOUtils.closeQuietly((Closeable)save);
                    }
                    catch (FileNotFoundException e) {
                        this.listener.fileNotFound();
                    }
                    continue;
                }
                if (length > position) {
                    position = this.readLines(reader);
                    last = this.file.lastModified();
                } else if (newer) {
                    position = 0L;
                    reader.seek(position);
                    position = this.readLines(reader);
                    last = this.file.lastModified();
                }
                if (this.reOpen) {
                    IOUtils.closeQuietly((Closeable)reader);
                }
                Thread.sleep(this.delayMillis);
                if (!this.getRun() || !this.reOpen) continue;
                reader = new RandomAccessFile(this.file, RAF_MODE);
                reader.seek(position);
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.stop(e);
        }
        catch (Exception e) {
            this.stop(e);
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private void stop(Exception e) {
        this.listener.handle(e);
        this.stop();
    }

    public void stop() {
        this.run = false;
    }

    private long readLines(RandomAccessFile reader) throws IOException {
        int num;
        long pos;
        ByteArrayOutputStream lineBuf = new ByteArrayOutputStream(64);
        long rePos = pos = reader.getFilePointer();
        boolean seenCR = false;
        while (this.getRun() && (num = reader.read(this.inbuf)) != -1) {
            block5: for (int i = 0; i < num; ++i) {
                byte ch = this.inbuf[i];
                switch (ch) {
                    case 10: {
                        seenCR = false;
                        this.listener.handle(new String(lineBuf.toByteArray(), this.cset));
                        lineBuf.reset();
                        rePos = pos + (long)i + 1L;
                        continue block5;
                    }
                    case 13: {
                        if (seenCR) {
                            lineBuf.write(13);
                        }
                        seenCR = true;
                        continue block5;
                    }
                    default: {
                        if (seenCR) {
                            seenCR = false;
                            this.listener.handle(new String(lineBuf.toByteArray(), this.cset));
                            lineBuf.reset();
                            rePos = pos + (long)i + 1L;
                        }
                        lineBuf.write(ch);
                    }
                }
            }
            pos = reader.getFilePointer();
        }
        IOUtils.closeQuietly(lineBuf);
        reader.seek(rePos);
        if (this.listener instanceof TailerListenerAdapter) {
            ((TailerListenerAdapter)this.listener).endOfFileReached();
        }
        return rePos;
    }
}

