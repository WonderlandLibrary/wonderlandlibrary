// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;
import me.gong.mcleaks.util.google.common.base.Optional;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.InputStream;
import java.util.ArrayList;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Collection;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Arrays;
import me.gong.mcleaks.util.google.common.base.Predicate;
import java.nio.file.LinkOption;
import me.gong.mcleaks.util.google.common.collect.TreeTraverser;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.Files;
import me.gong.mcleaks.util.google.common.collect.ImmutableList;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@AndroidIncompatible
@GwtIncompatible
public final class MoreFiles
{
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
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            return ImmutableList.copyOf((Iterable<? extends Path>)stream);
        }
        catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
    }
    
    public static TreeTraverser<Path> directoryTreeTraverser() {
        return DirectoryTreeTraverser.INSTANCE;
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
                return "MoreFiles.isDirectory(" + Arrays.toString(optionsCopy) + ")";
            }
        };
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
                return "MoreFiles.isRegularFile(" + Arrays.toString(optionsCopy) + ")";
            }
        };
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
                throw new IOException("Unable to create parent directories of " + path);
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
            try (final DirectoryStream<Path> parent = Files.newDirectoryStream(parentPath)) {
                if (parent instanceof SecureDirectoryStream) {
                    sdsSupported = true;
                    exceptions = deleteRecursivelySecure((SecureDirectoryStream)parent, path.getFileName());
                }
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
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            if (stream instanceof SecureDirectoryStream) {
                final SecureDirectoryStream<Path> sds = (SecureDirectoryStream<Path>)(SecureDirectoryStream)stream;
                exceptions = deleteDirectoryContentsSecure(sds);
            }
            else {
                checkAllowsInsecure(path, options);
                exceptions = deleteDirectoryContentsInsecure(stream);
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
    
    @Nullable
    private static Collection<IOException> deleteRecursivelySecure(final SecureDirectoryStream<Path> dir, final Path path) {
        Collection<IOException> exceptions = null;
        try {
            if (isDirectory(dir, path, LinkOption.NOFOLLOW_LINKS)) {
                try (final SecureDirectoryStream<Path> childDir = dir.newDirectoryStream(path, LinkOption.NOFOLLOW_LINKS)) {
                    exceptions = deleteDirectoryContentsSecure(childDir);
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
    
    @Nullable
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
    
    @Nullable
    private static Collection<IOException> deleteRecursivelyInsecure(final Path path) {
        Collection<IOException> exceptions = null;
        try {
            if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                try (final DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                    exceptions = deleteDirectoryContentsInsecure(stream);
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
    
    @Nullable
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
    
    @Nullable
    private static Path getParentPath(final Path path) throws IOException {
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
    
    private static boolean isDirectory(final SecureDirectoryStream<Path> dir, final Path name, final LinkOption... options) throws IOException {
        return dir.getFileAttributeView(name, BasicFileAttributeView.class, options).readAttributes().isDirectory();
    }
    
    private static Collection<IOException> addException(@Nullable Collection<IOException> exceptions, final IOException e) {
        if (exceptions == null) {
            exceptions = new ArrayList<IOException>();
        }
        exceptions.add(e);
        return exceptions;
    }
    
    @Nullable
    private static Collection<IOException> concat(@Nullable final Collection<IOException> exceptions, @Nullable final Collection<IOException> other) {
        if (exceptions == null) {
            return other;
        }
        if (other != null) {
            exceptions.addAll(other);
        }
        return exceptions;
    }
    
    private static void throwDeleteFailed(final Path path, final Collection<IOException> exceptions) throws FileSystemException {
        final FileSystemException deleteFailed = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
        for (final IOException e : exceptions) {
            deleteFailed.addSuppressed(e);
        }
        throw deleteFailed;
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
            try (final SeekableByteChannel channel = Files.newByteChannel(this.path, this.options)) {
                return me.gong.mcleaks.util.google.common.io.Files.readFile(Channels.newInputStream(channel), channel.size());
            }
        }
        
        @Override
        public String toString() {
            return "MoreFiles.asByteSource(" + this.path + ", " + Arrays.toString(this.options) + ")";
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
            return "MoreFiles.asByteSink(" + this.path + ", " + Arrays.toString(this.options) + ")";
        }
    }
    
    private static final class DirectoryTreeTraverser extends TreeTraverser<Path>
    {
        private static final DirectoryTreeTraverser INSTANCE;
        
        @Override
        public Iterable<Path> children(final Path dir) {
            if (Files.isDirectory(dir, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    return MoreFiles.listFiles(dir);
                }
                catch (IOException e) {
                    throw new DirectoryIteratorException(e);
                }
            }
            return (Iterable<Path>)ImmutableList.of();
        }
        
        static {
            INSTANCE = new DirectoryTreeTraverser();
        }
    }
}
