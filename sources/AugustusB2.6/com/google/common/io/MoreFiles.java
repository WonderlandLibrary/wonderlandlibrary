// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.OutputStream;
import java.util.stream.Stream;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;
import com.google.common.base.Optional;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.InputStream;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.CheckForNull;
import java.util.Collection;
import java.util.Objects;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import com.google.common.base.Preconditions;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.SecureDirectoryStream;
import java.util.Arrays;
import com.google.common.base.Predicate;
import java.nio.file.LinkOption;
import com.google.common.graph.Traverser;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.Files;
import com.google.common.collect.ImmutableList;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import com.google.common.graph.SuccessorsFunction;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public final class MoreFiles
{
    private static final SuccessorsFunction<Path> FILE_TREE;
    
    private MoreFiles() {
    }
    
    public static ByteSource asByteSource(final Path path, final OpenOption... options) {
        return new PathByteSource(path, options);
    }
    
    public static ByteSink asByteSink(final Path path, final OpenOption... options) {
        return new PathByteSink(path, options);
    }
    
    public static CharSource asCharSource(final Path path, final Charset charset, final OpenOption... options) {
        return asByteSource(path, options).asCharSource(charset);
    }
    
    public static CharSink asCharSink(final Path path, final Charset charset, final OpenOption... options) {
        return asByteSink(path, options).asCharSink(charset);
    }
    
    public static ImmutableList<Path> listFiles(final Path dir) throws IOException {
        try {
            final DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
            try {
                final ImmutableList<Path> copy = ImmutableList.copyOf((Iterable<? extends Path>)stream);
                if (stream != null) {
                    stream.close();
                }
                return copy;
            }
            catch (Throwable t) {
                if (stream != null) {
                    try {
                        stream.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
    }
    
    public static Traverser<Path> fileTraverser() {
        return Traverser.forTree(MoreFiles.FILE_TREE);
    }
    
    private static Iterable<Path> fileTreeChildren(final Path dir) {
        if (Files.isDirectory(dir, LinkOption.NOFOLLOW_LINKS)) {
            try {
                return listFiles(dir);
            }
            catch (IOException e) {
                throw new DirectoryIteratorException(e);
            }
        }
        return (Iterable<Path>)ImmutableList.of();
    }
    
    public static Predicate<Path> isDirectory(final LinkOption... options) {
        final LinkOption[] optionsCopy = options.clone();
        return new Predicate<Path>() {
            @Override
            public boolean apply(final Path input) {
                return Files.isDirectory(input, optionsCopy);
            }
            
            @Override
            public String toString() {
                final String string = Arrays.toString(optionsCopy);
                return new StringBuilder(23 + String.valueOf(string).length()).append("MoreFiles.isDirectory(").append(string).append(")").toString();
            }
        };
    }
    
    private static boolean isDirectory(final SecureDirectoryStream<Path> dir, final Path name, final LinkOption... options) throws IOException {
        return dir.getFileAttributeView(name, BasicFileAttributeView.class, options).readAttributes().isDirectory();
    }
    
    public static Predicate<Path> isRegularFile(final LinkOption... options) {
        final LinkOption[] optionsCopy = options.clone();
        return new Predicate<Path>() {
            @Override
            public boolean apply(final Path input) {
                return Files.isRegularFile(input, optionsCopy);
            }
            
            @Override
            public String toString() {
                final String string = Arrays.toString(optionsCopy);
                return new StringBuilder(25 + String.valueOf(string).length()).append("MoreFiles.isRegularFile(").append(string).append(")").toString();
            }
        };
    }
    
    public static boolean equal(final Path path1, final Path path2) throws IOException {
        Preconditions.checkNotNull(path1);
        Preconditions.checkNotNull(path2);
        if (Files.isSameFile(path1, path2)) {
            return true;
        }
        final ByteSource source1 = asByteSource(path1, new OpenOption[0]);
        final ByteSource source2 = asByteSource(path2, new OpenOption[0]);
        final long len1 = source1.sizeIfKnown().or(0L);
        final long len2 = source2.sizeIfKnown().or(0L);
        return (len1 == 0L || len2 == 0L || len1 == len2) && source1.contentEquals(source2);
    }
    
    public static void touch(final Path path) throws IOException {
        Preconditions.checkNotNull(path);
        try {
            Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
        }
        catch (NoSuchFileException e) {
            try {
                Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
            }
            catch (FileAlreadyExistsException ex) {}
        }
    }
    
    public static void createParentDirectories(final Path path, final FileAttribute<?>... attrs) throws IOException {
        final Path normalizedAbsolutePath = path.toAbsolutePath().normalize();
        final Path parent = normalizedAbsolutePath.getParent();
        if (parent == null) {
            return;
        }
        if (!Files.isDirectory(parent, new LinkOption[0])) {
            Files.createDirectories(parent, attrs);
            if (!Files.isDirectory(parent, new LinkOption[0])) {
                final String value = String.valueOf(path);
                throw new IOException(new StringBuilder(39 + String.valueOf(value).length()).append("Unable to create parent directories of ").append(value).toString());
            }
        }
    }
    
    public static String getFileExtension(final Path path) {
        final Path name = path.getFileName();
        if (name == null) {
            return "";
        }
        final String fileName = name.toString();
        final int dotIndex = fileName.lastIndexOf(46);
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
    
    public static String getNameWithoutExtension(final Path path) {
        final Path name = path.getFileName();
        if (name == null) {
            return "";
        }
        final String fileName = name.toString();
        final int dotIndex = fileName.lastIndexOf(46);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
    
    public static void deleteRecursively(final Path path, final RecursiveDeleteOption... options) throws IOException {
        final Path parentPath = getParentPath(path);
        if (parentPath == null) {
            throw new FileSystemException(path.toString(), null, "can't delete recursively");
        }
        Collection<IOException> exceptions = null;
        try {
            boolean sdsSupported = false;
            final DirectoryStream<Path> parent = Files.newDirectoryStream(parentPath);
            try {
                if (parent instanceof SecureDirectoryStream) {
                    sdsSupported = true;
                    exceptions = deleteRecursivelySecure((SecureDirectoryStream)parent, Objects.requireNonNull(path.getFileName()));
                }
                if (parent != null) {
                    parent.close();
                }
            }
            catch (Throwable t) {
                if (parent != null) {
                    try {
                        parent.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
            if (!sdsSupported) {
                checkAllowsInsecure(path, options);
                exceptions = deleteRecursivelyInsecure(path);
            }
        }
        catch (IOException e) {
            if (exceptions == null) {
                throw e;
            }
            exceptions.add(e);
        }
        if (exceptions != null) {
            throwDeleteFailed(path, exceptions);
        }
    }
    
    public static void deleteDirectoryContents(final Path path, final RecursiveDeleteOption... options) throws IOException {
        Collection<IOException> exceptions = null;
        try {
            final DirectoryStream<Path> stream = Files.newDirectoryStream(path);
            try {
                if (stream instanceof SecureDirectoryStream) {
                    final SecureDirectoryStream<Path> sds = (SecureDirectoryStream<Path>)(SecureDirectoryStream)stream;
                    exceptions = deleteDirectoryContentsSecure(sds);
                }
                else {
                    checkAllowsInsecure(path, options);
                    exceptions = deleteDirectoryContentsInsecure(stream);
                }
                if (stream != null) {
                    stream.close();
                }
            }
            catch (Throwable t) {
                if (stream != null) {
                    try {
                        stream.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (IOException e) {
            if (exceptions == null) {
                throw e;
            }
            exceptions.add(e);
        }
        if (exceptions != null) {
            throwDeleteFailed(path, exceptions);
        }
    }
    
    @CheckForNull
    private static Collection<IOException> deleteRecursivelySecure(final SecureDirectoryStream<Path> dir, final Path path) {
        Collection<IOException> exceptions = null;
        try {
            if (isDirectory(dir, path, LinkOption.NOFOLLOW_LINKS)) {
                final SecureDirectoryStream<Path> childDir = dir.newDirectoryStream(path, LinkOption.NOFOLLOW_LINKS);
                try {
                    exceptions = deleteDirectoryContentsSecure(childDir);
                    if (childDir != null) {
                        childDir.close();
                    }
                }
                catch (Throwable t) {
                    if (childDir != null) {
                        try {
                            childDir.close();
                        }
                        catch (Throwable exception) {
                            t.addSuppressed(exception);
                        }
                    }
                    throw t;
                }
                if (exceptions == null) {
                    dir.deleteDirectory(path);
                }
            }
            else {
                dir.deleteFile(path);
            }
            return exceptions;
        }
        catch (IOException e) {
            return addException(exceptions, e);
        }
    }
    
    @CheckForNull
    private static Collection<IOException> deleteDirectoryContentsSecure(final SecureDirectoryStream<Path> dir) {
        Collection<IOException> exceptions = null;
        try {
            for (final Path path : dir) {
                exceptions = concat(exceptions, deleteRecursivelySecure(dir, path.getFileName()));
            }
            return exceptions;
        }
        catch (DirectoryIteratorException e) {
            return addException(exceptions, e.getCause());
        }
    }
    
    @CheckForNull
    private static Collection<IOException> deleteRecursivelyInsecure(final Path path) {
        Collection<IOException> exceptions = null;
        try {
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                final DirectoryStream<Path> stream = Files.newDirectoryStream(path);
                try {
                    exceptions = deleteDirectoryContentsInsecure(stream);
                    if (stream != null) {
                        stream.close();
                    }
                }
                catch (Throwable t) {
                    if (stream != null) {
                        try {
                            stream.close();
                        }
                        catch (Throwable exception) {
                            t.addSuppressed(exception);
                        }
                    }
                    throw t;
                }
            }
            if (exceptions == null) {
                Files.delete(path);
            }
            return exceptions;
        }
        catch (IOException e) {
            return addException(exceptions, e);
        }
    }
    
    @CheckForNull
    private static Collection<IOException> deleteDirectoryContentsInsecure(final DirectoryStream<Path> dir) {
        Collection<IOException> exceptions = null;
        try {
            for (final Path entry : dir) {
                exceptions = concat(exceptions, deleteRecursivelyInsecure(entry));
            }
            return exceptions;
        }
        catch (DirectoryIteratorException e) {
            return addException(exceptions, e.getCause());
        }
    }
    
    @CheckForNull
    private static Path getParentPath(final Path path) {
        final Path parent = path.getParent();
        if (parent != null) {
            return parent;
        }
        if (path.getNameCount() == 0) {
            return null;
        }
        return path.getFileSystem().getPath(".", new String[0]);
    }
    
    private static void checkAllowsInsecure(final Path path, final RecursiveDeleteOption[] options) throws InsecureRecursiveDeleteException {
        if (!Arrays.asList(options).contains(RecursiveDeleteOption.ALLOW_INSECURE)) {
            throw new InsecureRecursiveDeleteException(path.toString());
        }
    }
    
    private static Collection<IOException> addException(@CheckForNull Collection<IOException> exceptions, final IOException e) {
        if (exceptions == null) {
            exceptions = new ArrayList<IOException>();
        }
        exceptions.add(e);
        return exceptions;
    }
    
    @CheckForNull
    private static Collection<IOException> concat(@CheckForNull final Collection<IOException> exceptions, @CheckForNull final Collection<IOException> other) {
        if (exceptions == null) {
            return other;
        }
        if (other != null) {
            exceptions.addAll(other);
        }
        return exceptions;
    }
    
    private static void throwDeleteFailed(final Path path, final Collection<IOException> exceptions) throws FileSystemException {
        final NoSuchFileException pathNotFound = pathNotFound(path, exceptions);
        if (pathNotFound != null) {
            throw pathNotFound;
        }
        final FileSystemException deleteFailed = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
        for (final IOException e : exceptions) {
            deleteFailed.addSuppressed(e);
        }
        throw deleteFailed;
    }
    
    @CheckForNull
    private static NoSuchFileException pathNotFound(final Path path, final Collection<IOException> exceptions) {
        if (exceptions.size() != 1) {
            return null;
        }
        final IOException exception = Iterables.getOnlyElement(exceptions);
        if (!(exception instanceof NoSuchFileException)) {
            return null;
        }
        final NoSuchFileException noSuchFileException = (NoSuchFileException)exception;
        final String exceptionFile = noSuchFileException.getFile();
        if (exceptionFile == null) {
            return null;
        }
        final Path parentPath = getParentPath(path);
        if (parentPath == null) {
            return null;
        }
        final Path pathResolvedFromParent = parentPath.resolve(Objects.requireNonNull(path.getFileName()));
        if (exceptionFile.equals(pathResolvedFromParent.toString())) {
            return noSuchFileException;
        }
        return null;
    }
    
    static {
        FILE_TREE = new SuccessorsFunction<Path>() {
            @Override
            public Iterable<Path> successors(final Path path) {
                return fileTreeChildren(path);
            }
        };
    }
    
    private static final class PathByteSource extends ByteSource
    {
        private static final LinkOption[] FOLLOW_LINKS;
        private final Path path;
        private final OpenOption[] options;
        private final boolean followLinks;
        
        private PathByteSource(final Path path, final OpenOption... options) {
            this.path = Preconditions.checkNotNull(path);
            this.options = options.clone();
            this.followLinks = followLinks(this.options);
        }
        
        private static boolean followLinks(final OpenOption[] options) {
            for (final OpenOption option : options) {
                if (option == LinkOption.NOFOLLOW_LINKS) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public InputStream openStream() throws IOException {
            return Files.newInputStream(this.path, this.options);
        }
        
        private BasicFileAttributes readAttributes() throws IOException {
            return Files.readAttributes(this.path, BasicFileAttributes.class, this.followLinks ? PathByteSource.FOLLOW_LINKS : new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
        }
        
        @Override
        public Optional<Long> sizeIfKnown() {
            BasicFileAttributes attrs;
            try {
                attrs = this.readAttributes();
            }
            catch (IOException e) {
                return Optional.absent();
            }
            if (attrs.isDirectory() || attrs.isSymbolicLink()) {
                return Optional.absent();
            }
            return Optional.of(attrs.size());
        }
        
        @Override
        public long size() throws IOException {
            final BasicFileAttributes attrs = this.readAttributes();
            if (attrs.isDirectory()) {
                throw new IOException("can't read: is a directory");
            }
            if (attrs.isSymbolicLink()) {
                throw new IOException("can't read: is a symbolic link");
            }
            return attrs.size();
        }
        
        @Override
        public byte[] read() throws IOException {
            final SeekableByteChannel channel = Files.newByteChannel(this.path, this.options);
            try {
                final byte[] byteArray = ByteStreams.toByteArray(Channels.newInputStream(channel), channel.size());
                if (channel != null) {
                    channel.close();
                }
                return byteArray;
            }
            catch (Throwable t) {
                if (channel != null) {
                    try {
                        channel.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        
        @Override
        public CharSource asCharSource(final Charset charset) {
            if (this.options.length == 0) {
                return new AsCharSource(charset) {
                    @Override
                    public Stream<String> lines() throws IOException {
                        return Files.lines(PathByteSource.this.path, this.charset);
                    }
                };
            }
            return super.asCharSource(charset);
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.path);
            final String string = Arrays.toString(this.options);
            return new StringBuilder(26 + String.valueOf(value).length() + String.valueOf(string).length()).append("MoreFiles.asByteSource(").append(value).append(", ").append(string).append(")").toString();
        }
        
        static {
            FOLLOW_LINKS = new LinkOption[0];
        }
    }
    
    private static final class PathByteSink extends ByteSink
    {
        private final Path path;
        private final OpenOption[] options;
        
        private PathByteSink(final Path path, final OpenOption... options) {
            this.path = Preconditions.checkNotNull(path);
            this.options = options.clone();
        }
        
        @Override
        public OutputStream openStream() throws IOException {
            return Files.newOutputStream(this.path, this.options);
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.path);
            final String string = Arrays.toString(this.options);
            return new StringBuilder(24 + String.valueOf(value).length() + String.valueOf(string).length()).append("MoreFiles.asByteSink(").append(value).append(", ").append(string).append(")").toString();
        }
    }
}
