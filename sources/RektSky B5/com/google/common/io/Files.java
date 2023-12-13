/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.TreeTraverser;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Closer;
import com.google.common.io.FileWriteMode;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;
import com.google.common.io.OutputSupplier;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Beta
public final class Files {
    private static final int TEMP_DIR_ATTEMPTS = 10000;
    private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser<File>(){

        @Override
        public Iterable<File> children(File file) {
            File[] files;
            if (file.isDirectory() && (files = file.listFiles()) != null) {
                return Collections.unmodifiableList(Arrays.asList(files));
            }
            return Collections.emptyList();
        }

        public String toString() {
            return "Files.fileTreeTraverser()";
        }
    };

    private Files() {
    }

    public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(file), charset));
    }

    public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(file), charset));
    }

    public static ByteSource asByteSource(File file) {
        return new FileByteSource(file);
    }

    static byte[] readFile(InputStream in, long expectedSize) throws IOException {
        if (expectedSize > Integer.MAX_VALUE) {
            throw new OutOfMemoryError("file is too large to fit in a byte array: " + expectedSize + " bytes");
        }
        return expectedSize == 0L ? ByteStreams.toByteArray(in) : ByteStreams.toByteArray(in, (int)expectedSize);
    }

    public static ByteSink asByteSink(File file, FileWriteMode ... modes) {
        return new FileByteSink(file, modes);
    }

    public static CharSource asCharSource(File file, Charset charset) {
        return Files.asByteSource(file).asCharSource(charset);
    }

    public static CharSink asCharSink(File file, Charset charset, FileWriteMode ... modes) {
        return Files.asByteSink(file, modes).asCharSink(charset);
    }

    @Deprecated
    public static InputSupplier<FileInputStream> newInputStreamSupplier(File file) {
        return ByteStreams.asInputSupplier(Files.asByteSource(file));
    }

    @Deprecated
    public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file) {
        return Files.newOutputStreamSupplier(file, false);
    }

    @Deprecated
    public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file, boolean append) {
        return ByteStreams.asOutputSupplier(Files.asByteSink(file, Files.modes(append)));
    }

    private static FileWriteMode[] modes(boolean append) {
        FileWriteMode[] fileWriteModeArray;
        if (append) {
            FileWriteMode[] fileWriteModeArray2 = new FileWriteMode[1];
            fileWriteModeArray = fileWriteModeArray2;
            fileWriteModeArray2[0] = FileWriteMode.APPEND;
        } else {
            fileWriteModeArray = new FileWriteMode[]{};
        }
        return fileWriteModeArray;
    }

    @Deprecated
    public static InputSupplier<InputStreamReader> newReaderSupplier(File file, Charset charset) {
        return CharStreams.asInputSupplier(Files.asCharSource(file, charset));
    }

    @Deprecated
    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset) {
        return Files.newWriterSupplier(file, charset, false);
    }

    @Deprecated
    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset, boolean append) {
        return CharStreams.asOutputSupplier(Files.asCharSink(file, charset, Files.modes(append)));
    }

    public static byte[] toByteArray(File file) throws IOException {
        return Files.asByteSource(file).read();
    }

    public static String toString(File file, Charset charset) throws IOException {
        return Files.asCharSource(file, charset).read();
    }

    @Deprecated
    public static void copy(InputSupplier<? extends InputStream> from, File to) throws IOException {
        ByteStreams.asByteSource(from).copyTo(Files.asByteSink(to, new FileWriteMode[0]));
    }

    public static void write(byte[] from, File to) throws IOException {
        Files.asByteSink(to, new FileWriteMode[0]).write(from);
    }

    @Deprecated
    public static void copy(File from, OutputSupplier<? extends OutputStream> to) throws IOException {
        Files.asByteSource(from).copyTo(ByteStreams.asByteSink(to));
    }

    public static void copy(File from, OutputStream to) throws IOException {
        Files.asByteSource(from).copyTo(to);
    }

    public static void copy(File from, File to) throws IOException {
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        Files.asByteSource(from).copyTo(Files.asByteSink(to, new FileWriteMode[0]));
    }

    @Deprecated
    public static <R extends Readable & Closeable> void copy(InputSupplier<R> from, File to, Charset charset) throws IOException {
        CharStreams.asCharSource(from).copyTo(Files.asCharSink(to, charset, new FileWriteMode[0]));
    }

    public static void write(CharSequence from, File to, Charset charset) throws IOException {
        Files.asCharSink(to, charset, new FileWriteMode[0]).write(from);
    }

    public static void append(CharSequence from, File to, Charset charset) throws IOException {
        Files.write(from, to, charset, true);
    }

    private static void write(CharSequence from, File to, Charset charset, boolean append) throws IOException {
        Files.asCharSink(to, charset, Files.modes(append)).write(from);
    }

    @Deprecated
    public static <W extends Appendable & Closeable> void copy(File from, Charset charset, OutputSupplier<W> to) throws IOException {
        Files.asCharSource(from, charset).copyTo(CharStreams.asCharSink(to));
    }

    public static void copy(File from, Charset charset, Appendable to) throws IOException {
        Files.asCharSource(from, charset).copyTo(to);
    }

    public static boolean equal(File file1, File file2) throws IOException {
        Preconditions.checkNotNull(file1);
        Preconditions.checkNotNull(file2);
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }
        long len1 = file1.length();
        long len2 = file2.length();
        if (len1 != 0L && len2 != 0L && len1 != len2) {
            return false;
        }
        return Files.asByteSource(file1).contentEquals(Files.asByteSource(file2));
    }

    public static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";
        for (int counter = 0; counter < 10000; ++counter) {
            File tempDir = new File(baseDir, baseName + counter);
            if (!tempDir.mkdir()) continue;
            return tempDir;
        }
        throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + 9999 + ')');
    }

    public static void touch(File file) throws IOException {
        Preconditions.checkNotNull(file);
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            throw new IOException("Unable to update modification time of " + file);
        }
    }

    public static void createParentDirs(File file) throws IOException {
        Preconditions.checkNotNull(file);
        File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return;
        }
        parent.mkdirs();
        if (!parent.isDirectory()) {
            throw new IOException("Unable to create parent directories of " + file);
        }
    }

    public static void move(File from, File to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        if (!from.renameTo(to)) {
            Files.copy(from, to);
            if (!from.delete()) {
                if (!to.delete()) {
                    throw new IOException("Unable to delete " + to);
                }
                throw new IOException("Unable to delete " + from);
            }
        }
    }

    public static String readFirstLine(File file, Charset charset) throws IOException {
        return Files.asCharSource(file, charset).readFirstLine();
    }

    public static List<String> readLines(File file, Charset charset) throws IOException {
        return Files.readLines(file, charset, new LineProcessor<List<String>>(){
            final List<String> result = Lists.newArrayList();

            @Override
            public boolean processLine(String line) {
                this.result.add(line);
                return true;
            }

            @Override
            public List<String> getResult() {
                return this.result;
            }
        });
    }

    public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
        return CharStreams.readLines(Files.newReaderSupplier(file, charset), callback);
    }

    public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
        return ByteStreams.readBytes(Files.newInputStreamSupplier(file), processor);
    }

    public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
        return Files.asByteSource(file).hash(hashFunction);
    }

    public static MappedByteBuffer map(File file) throws IOException {
        Preconditions.checkNotNull(file);
        return Files.map(file, FileChannel.MapMode.READ_ONLY);
    }

    public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mode);
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
        return Files.map(file, mode, file.length());
    }

    public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws FileNotFoundException, IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mode);
        Closer closer = Closer.create();
        try {
            RandomAccessFile raf = closer.register(new RandomAccessFile(file, mode == FileChannel.MapMode.READ_ONLY ? "r" : "rw"));
            MappedByteBuffer mappedByteBuffer = Files.map(raf, mode, size);
            return mappedByteBuffer;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    private static MappedByteBuffer map(RandomAccessFile raf, FileChannel.MapMode mode, long size) throws IOException {
        Closer closer = Closer.create();
        try {
            FileChannel channel = closer.register(raf.getChannel());
            MappedByteBuffer mappedByteBuffer = channel.map(mode, 0L, size);
            return mappedByteBuffer;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public static String simplifyPath(String pathname) {
        Preconditions.checkNotNull(pathname);
        if (pathname.length() == 0) {
            return ".";
        }
        Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
        ArrayList<String> path = new ArrayList<String>();
        for (String component : components) {
            if (component.equals(".")) continue;
            if (component.equals("..")) {
                if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
                    path.remove(path.size() - 1);
                    continue;
                }
                path.add("..");
                continue;
            }
            path.add(component);
        }
        String result = Joiner.on('/').join(path);
        if (pathname.charAt(0) == '/') {
            result = "/" + result;
        }
        while (result.startsWith("/../")) {
            result = result.substring(3);
        }
        if (result.equals("/..")) {
            result = "/";
        } else if ("".equals(result)) {
            result = ".";
        }
        return result;
    }

    public static String getFileExtension(String fullName) {
        Preconditions.checkNotNull(fullName);
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getNameWithoutExtension(String file) {
        Preconditions.checkNotNull(file);
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }

    public static TreeTraverser<File> fileTreeTraverser() {
        return FILE_TREE_TRAVERSER;
    }

    public static Predicate<File> isDirectory() {
        return FilePredicate.IS_DIRECTORY;
    }

    public static Predicate<File> isFile() {
        return FilePredicate.IS_FILE;
    }

    private static enum FilePredicate implements Predicate<File>
    {
        IS_DIRECTORY{

            @Override
            public boolean apply(File file) {
                return file.isDirectory();
            }

            public String toString() {
                return "Files.isDirectory()";
            }
        }
        ,
        IS_FILE{

            @Override
            public boolean apply(File file) {
                return file.isFile();
            }

            public String toString() {
                return "Files.isFile()";
            }
        };

    }

    private static final class FileByteSink
    extends ByteSink {
        private final File file;
        private final ImmutableSet<FileWriteMode> modes;

        private FileByteSink(File file, FileWriteMode ... modes) {
            this.file = Preconditions.checkNotNull(file);
            this.modes = ImmutableSet.copyOf(modes);
        }

        @Override
        public FileOutputStream openStream() throws IOException {
            return new FileOutputStream(this.file, this.modes.contains((Object)FileWriteMode.APPEND));
        }

        public String toString() {
            return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
        }
    }

    private static final class FileByteSource
    extends ByteSource {
        private final File file;

        private FileByteSource(File file) {
            this.file = Preconditions.checkNotNull(file);
        }

        @Override
        public FileInputStream openStream() throws IOException {
            return new FileInputStream(this.file);
        }

        @Override
        public long size() throws IOException {
            if (!this.file.isFile()) {
                throw new FileNotFoundException(this.file.toString());
            }
            return this.file.length();
        }

        @Override
        public byte[] read() throws IOException {
            Closer closer = Closer.create();
            try {
                FileInputStream in = closer.register(this.openStream());
                byte[] byArray = Files.readFile(in, in.getChannel().size());
                return byArray;
            }
            catch (Throwable e2) {
                throw closer.rethrow(e2);
            }
            finally {
                closer.close();
            }
        }

        public String toString() {
            return "Files.asByteSource(" + this.file + ")";
        }
    }
}

