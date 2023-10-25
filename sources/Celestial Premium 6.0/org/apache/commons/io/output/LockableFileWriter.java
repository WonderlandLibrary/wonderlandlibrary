/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class LockableFileWriter
extends Writer {
    private static final String LCK = ".lck";
    private final Writer out;
    private final File lockFile;

    public LockableFileWriter(String fileName) throws IOException {
        this(fileName, false, null);
    }

    public LockableFileWriter(String fileName, boolean append) throws IOException {
        this(fileName, append, null);
    }

    public LockableFileWriter(String fileName, boolean append, String lockDir) throws IOException {
        this(new File(fileName), append, lockDir);
    }

    public LockableFileWriter(File file) throws IOException {
        this(file, false, null);
    }

    public LockableFileWriter(File file, boolean append) throws IOException {
        this(file, append, null);
    }

    @Deprecated
    public LockableFileWriter(File file, boolean append, String lockDir) throws IOException {
        this(file, Charset.defaultCharset(), append, lockDir);
    }

    public LockableFileWriter(File file, Charset encoding) throws IOException {
        this(file, encoding, false, null);
    }

    public LockableFileWriter(File file, String encoding) throws IOException {
        this(file, encoding, false, null);
    }

    public LockableFileWriter(File file, Charset encoding, boolean append, String lockDir) throws IOException {
        file = file.getAbsoluteFile();
        if (file.getParentFile() != null) {
            FileUtils.forceMkdir(file.getParentFile());
        }
        if (file.isDirectory()) {
            throw new IOException("File specified is a directory");
        }
        if (lockDir == null) {
            lockDir = System.getProperty("java.io.tmpdir");
        }
        File lockDirFile = new File(lockDir);
        FileUtils.forceMkdir(lockDirFile);
        this.testLockDir(lockDirFile);
        this.lockFile = new File(lockDirFile, file.getName() + LCK);
        this.createLock();
        this.out = this.initWriter(file, encoding, append);
    }

    public LockableFileWriter(File file, String encoding, boolean append, String lockDir) throws IOException {
        this(file, Charsets.toCharset(encoding), append, lockDir);
    }

    private void testLockDir(File lockDir) throws IOException {
        if (!lockDir.exists()) {
            throw new IOException("Could not find lockDir: " + lockDir.getAbsolutePath());
        }
        if (!lockDir.canWrite()) {
            throw new IOException("Could not write to lockDir: " + lockDir.getAbsolutePath());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void createLock() throws IOException {
        Class<LockableFileWriter> class_ = LockableFileWriter.class;
        synchronized (LockableFileWriter.class) {
            if (!this.lockFile.createNewFile()) {
                throw new IOException("Can't write file, lock " + this.lockFile.getAbsolutePath() + " exists");
            }
            this.lockFile.deleteOnExit();
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return;
        }
    }

    private Writer initWriter(File file, Charset encoding, boolean append) throws IOException {
        boolean fileExistedAlready = file.exists();
        FileOutputStream stream = null;
        OutputStreamWriter writer = null;
        try {
            stream = new FileOutputStream(file.getAbsolutePath(), append);
            writer = new OutputStreamWriter((OutputStream)stream, Charsets.toCharset(encoding));
        }
        catch (IOException ex) {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(stream);
            FileUtils.deleteQuietly(this.lockFile);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
        catch (RuntimeException ex) {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(stream);
            FileUtils.deleteQuietly(this.lockFile);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
        return writer;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() throws IOException {
        try {
            this.out.close();
        }
        finally {
            this.lockFile.delete();
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
}

