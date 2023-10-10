// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.common.collect.ImmutableSet;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.Arrays;
import com.google.common.base.Predicate;
import com.google.common.graph.Traverser;
import java.util.Iterator;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import com.google.common.base.Splitter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.CheckForNull;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import com.google.common.annotations.Beta;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import com.google.common.base.Preconditions;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.io.File;
import com.google.common.graph.SuccessorsFunction;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class Files
{
    private static final int TEMP_DIR_ATTEMPTS = 10000;
    private static final SuccessorsFunction<File> FILE_TREE;
    
    private Files() {
    }
    
    @Beta
    public static BufferedReader newReader(final File file, final Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }
    
    @Beta
    public static BufferedWriter newWriter(final File file, final Charset charset) throws FileNotFoundException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(charset);
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }
    
    public static ByteSource asByteSource(final File file) {
        return new FileByteSource(file);
    }
    
    public static ByteSink asByteSink(final File file, final FileWriteMode... modes) {
        return new FileByteSink(file, modes);
    }
    
    public static CharSource asCharSource(final File file, final Charset charset) {
        return asByteSource(file).asCharSource(charset);
    }
    
    public static CharSink asCharSink(final File file, final Charset charset, final FileWriteMode... modes) {
        return asByteSink(file, modes).asCharSink(charset);
    }
    
    @Beta
    public static byte[] toByteArray(final File file) throws IOException {
        return asByteSource(file).read();
    }
    
    @Deprecated
    @Beta
    public static String toString(final File file, final Charset charset) throws IOException {
        return asCharSource(file, charset).read();
    }
    
    @Beta
    public static void write(final byte[] from, final File to) throws IOException {
        asByteSink(to, new FileWriteMode[0]).write(from);
    }
    
    @Deprecated
    @Beta
    public static void write(final CharSequence from, final File to, final Charset charset) throws IOException {
        asCharSink(to, charset, new FileWriteMode[0]).write(from);
    }
    
    @Beta
    public static void copy(final File from, final OutputStream to) throws IOException {
        asByteSource(from).copyTo(to);
    }
    
    @Beta
    public static void copy(final File from, final File to) throws IOException {
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
    }
    
    @Deprecated
    @Beta
    public static void copy(final File from, final Charset charset, final Appendable to) throws IOException {
        asCharSource(from, charset).copyTo(to);
    }
    
    @Deprecated
    @Beta
    public static void append(final CharSequence from, final File to, final Charset charset) throws IOException {
        asCharSink(to, charset, FileWriteMode.APPEND).write(from);
    }
    
    @Beta
    public static boolean equal(final File file1, final File file2) throws IOException {
        Preconditions.checkNotNull(file1);
        Preconditions.checkNotNull(file2);
        if (file1 == file2 || file1.equals(file2)) {
            return true;
        }
        final long len1 = file1.length();
        final long len2 = file2.length();
        return (len1 == 0L || len2 == 0L || len1 == len2) && asByteSource(file1).contentEquals(asByteSource(file2));
    }
    
    @Deprecated
    @Beta
    public static File createTempDir() {
        final File baseDir = new File(System.getProperty("java.io.tmpdir"));
        final String baseName = new StringBuilder(21).append(System.currentTimeMillis()).append("-").toString();
        for (int counter = 0; counter < 10000; ++counter) {
            final File tempDir = new File(baseDir, new StringBuilder(11 + String.valueOf(baseName).length()).append(baseName).append(counter).toString());
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException(new StringBuilder(66 + String.valueOf(baseName).length() + String.valueOf(baseName).length()).append("Failed to create directory within 10000 attempts (tried ").append(baseName).append("0 to ").append(baseName).append(9999).append(')').toString());
    }
    
    @Beta
    public static void touch(final File file) throws IOException {
        Preconditions.checkNotNull(file);
        if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
            final String value = String.valueOf(file);
            throw new IOException(new StringBuilder(38 + String.valueOf(value).length()).append("Unable to update modification time of ").append(value).toString());
        }
    }
    
    @Beta
    public static void createParentDirs(final File file) throws IOException {
        Preconditions.checkNotNull(file);
        final File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return;
        }
        parent.mkdirs();
        if (!parent.isDirectory()) {
            final String value = String.valueOf(file);
            throw new IOException(new StringBuilder(39 + String.valueOf(value).length()).append("Unable to create parent directories of ").append(value).toString());
        }
    }
    
    @Beta
    public static void move(final File from, final File to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
        if (!from.renameTo(to)) {
            copy(from, to);
            if (!from.delete()) {
                if (!to.delete()) {
                    final String value = String.valueOf(to);
                    throw new IOException(new StringBuilder(17 + String.valueOf(value).length()).append("Unable to delete ").append(value).toString());
                }
                final String value2 = String.valueOf(from);
                throw new IOException(new StringBuilder(17 + String.valueOf(value2).length()).append("Unable to delete ").append(value2).toString());
            }
        }
    }
    
    @Deprecated
    @CheckForNull
    @Beta
    public static String readFirstLine(final File file, final Charset charset) throws IOException {
        return asCharSource(file, charset).readFirstLine();
    }
    
    @Beta
    public static List<String> readLines(final File file, final Charset charset) throws IOException {
        return asCharSource(file, charset).readLines((LineProcessor<List<String>>)new LineProcessor<List<String>>() {
            final List<String> result = Lists.newArrayList();
            
            @Override
            public boolean processLine(final String line) {
                this.result.add(line);
                return true;
            }
            
            @Override
            public List<String> getResult() {
                return this.result;
            }
        });
    }
    
    @Deprecated
    @ParametricNullness
    @Beta
    @CanIgnoreReturnValue
    public static <T> T readLines(final File file, final Charset charset, final LineProcessor<T> callback) throws IOException {
        return asCharSource(file, charset).readLines(callback);
    }
    
    @Deprecated
    @ParametricNullness
    @Beta
    @CanIgnoreReturnValue
    public static <T> T readBytes(final File file, final ByteProcessor<T> processor) throws IOException {
        return asByteSource(file).read(processor);
    }
    
    @Deprecated
    @Beta
    public static HashCode hash(final File file, final HashFunction hashFunction) throws IOException {
        return asByteSource(file).hash(hashFunction);
    }
    
    @Beta
    public static MappedByteBuffer map(final File file) throws IOException {
        Preconditions.checkNotNull(file);
        return map(file, FileChannel.MapMode.READ_ONLY);
    }
    
    @Beta
    public static MappedByteBuffer map(final File file, final FileChannel.MapMode mode) throws IOException {
        return mapInternal(file, mode, -1L);
    }
    
    @Beta
    public static MappedByteBuffer map(final File file, final FileChannel.MapMode mode, final long size) throws IOException {
        Preconditions.checkArgument(size >= 0L, "size (%s) may not be negative", size);
        return mapInternal(file, mode, size);
    }
    
    private static MappedByteBuffer mapInternal(final File file, final FileChannel.MapMode mode, final long size) throws IOException {
        Preconditions.checkNotNull(file);
        Preconditions.checkNotNull(mode);
        final Closer closer = Closer.create();
        try {
            final RandomAccessFile raf = closer.register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
            final FileChannel channel = closer.register(raf.getChannel());
            return channel.map(mode, 0L, (size == -1L) ? channel.size() : size);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Beta
    public static String simplifyPath(final String pathname) {
        Preconditions.checkNotNull(pathname);
        if (pathname.length() == 0) {
            return ".";
        }
        final Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
        final List<String> path = new ArrayList<String>();
        for (final String s : components) {
            final String component = s;
            switch (s) {
                case ".": {
                    continue;
                }
                case "..": {
                    if (path.size() > 0 && !path.get(path.size() - 1).equals("..")) {
                        path.remove(path.size() - 1);
                        continue;
                    }
                    path.add("..");
                    continue;
                }
                default: {
                    path.add(component);
                    continue;
                }
            }
        }
        String result = Joiner.on('/').join(path);
        if (pathname.charAt(0) == '/') {
            final String original = "/";
            final String value = String.valueOf(result);
            result = ((value.length() != 0) ? original.concat(value) : new String(original));
        }
        while (result.startsWith("/../")) {
            result = result.substring(3);
        }
        if (result.equals("/..")) {
            result = "/";
        }
        else if ("".equals(result)) {
            result = ".";
        }
        return result;
    }
    
    @Beta
    public static String getFileExtension(final String fullName) {
        Preconditions.checkNotNull(fullName);
        final String fileName = new File(fullName).getName();
        final int dotIndex = fileName.lastIndexOf(46);
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
    
    @Beta
    public static String getNameWithoutExtension(final String file) {
        Preconditions.checkNotNull(file);
        final String fileName = new File(file).getName();
        final int dotIndex = fileName.lastIndexOf(46);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
    
    @Beta
    public static Traverser<File> fileTraverser() {
        return Traverser.forTree(Files.FILE_TREE);
    }
    
    @Beta
    public static Predicate<File> isDirectory() {
        return FilePredicate.IS_DIRECTORY;
    }
    
    @Beta
    public static Predicate<File> isFile() {
        return FilePredicate.IS_FILE;
    }
    
    static {
        FILE_TREE = new SuccessorsFunction<File>() {
            @Override
            public Iterable<File> successors(final File file) {
                if (file.isDirectory()) {
                    final File[] files = file.listFiles();
                    if (files != null) {
                        return (Iterable<File>)Collections.unmodifiableList((List<?>)Arrays.asList((T[])files));
                    }
                }
                return (Iterable<File>)ImmutableList.of();
            }
        };
    }
    
    private static final class FileByteSource extends ByteSource
    {
        private final File file;
        
        private FileByteSource(final File file) {
            this.file = Preconditions.checkNotNull(file);
        }
        
        @Override
        public FileInputStream openStream() throws IOException {
            return new FileInputStream(this.file);
        }
        
        @Override
        public Optional<Long> sizeIfKnown() {
            if (this.file.isFile()) {
                return Optional.of(this.file.length());
            }
            return Optional.absent();
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
            final Closer closer = Closer.create();
            try {
                final FileInputStream in = closer.register(this.openStream());
                return ByteStreams.toByteArray(in, in.getChannel().size());
            }
            catch (Throwable e) {
                throw closer.rethrow(e);
            }
            finally {
                closer.close();
            }
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.file);
            return new StringBuilder(20 + String.valueOf(value).length()).append("Files.asByteSource(").append(value).append(")").toString();
        }
    }
    
    private static final class FileByteSink extends ByteSink
    {
        private final File file;
        private final ImmutableSet<FileWriteMode> modes;
        
        private FileByteSink(final File file, final FileWriteMode... modes) {
            this.file = Preconditions.checkNotNull(file);
            this.modes = ImmutableSet.copyOf(modes);
        }
        
        @Override
        public FileOutputStream openStream() throws IOException {
            return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.file);
            final String value2 = String.valueOf(this.modes);
            return new StringBuilder(20 + String.valueOf(value).length() + String.valueOf(value2).length()).append("Files.asByteSink(").append(value).append(", ").append(value2).append(")").toString();
        }
    }
    
    private enum FilePredicate implements Predicate<File>
    {
        IS_DIRECTORY(0) {
            @Override
            public boolean apply(final File file) {
                return file.isDirectory();
            }
            
            @Override
            public String toString() {
                return "Files.isDirectory()";
            }
        }, 
        IS_FILE(1) {
            @Override
            public boolean apply(final File file) {
                return file.isFile();
            }
            
            @Override
            public String toString() {
                return "Files.isFile()";
            }
        };
        
        private static /* synthetic */ FilePredicate[] $values() {
            return new FilePredicate[] { FilePredicate.IS_DIRECTORY, FilePredicate.IS_FILE };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
