/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.TreeTraverser;
import com.google.common.io.AndroidIncompatible;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.io.InsecureRecursiveDeleteException;
import com.google.common.io.RecursiveDeleteOption;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SecureDirectoryStream;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nullable;

@Beta
@AndroidIncompatible
@GwtIncompatible
public final class MoreFiles {
    private MoreFiles() {
    }

    public static ByteSource asByteSource(Path path, OpenOption ... options) {
        return new PathByteSource(path, options);
    }

    public static ByteSink asByteSink(Path path, OpenOption ... options) {
        return new PathByteSink(path, options);
    }

    public static CharSource asCharSource(Path path, Charset charset, OpenOption ... options) {
        return MoreFiles.asByteSource(path, options).asCharSource(charset);
    }

    public static CharSink asCharSink(Path path, Charset charset, OpenOption ... options) {
        return MoreFiles.asByteSink(path, options).asCharSink(charset);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ImmutableList<Path> listFiles(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(dir);){
            ImmutableList<Path> immutableList = ImmutableList.copyOf(stream);
            return immutableList;
        }
        catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
    }

    public static TreeTraverser<Path> directoryTreeTraverser() {
        return DirectoryTreeTraverser.INSTANCE;
    }

    public static Predicate<Path> isDirectory(LinkOption ... options) {
        final LinkOption[] optionsCopy = (LinkOption[])options.clone();
        return new Predicate<Path>(){

            @Override
            public boolean apply(Path input) {
                return java.nio.file.Files.isDirectory(input, optionsCopy);
            }

            public String toString() {
                return "MoreFiles.isDirectory(" + Arrays.toString(optionsCopy) + ")";
            }
        };
    }

    public static Predicate<Path> isRegularFile(LinkOption ... options) {
        final LinkOption[] optionsCopy = (LinkOption[])options.clone();
        return new Predicate<Path>(){

            @Override
            public boolean apply(Path input) {
                return java.nio.file.Files.isRegularFile(input, optionsCopy);
            }

            public String toString() {
                return "MoreFiles.isRegularFile(" + Arrays.toString(optionsCopy) + ")";
            }
        };
    }

    public static void touch(Path path) throws IOException {
        Preconditions.checkNotNull(path);
        try {
            java.nio.file.Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
        }
        catch (NoSuchFileException e) {
            try {
                java.nio.file.Files.createFile(path, new FileAttribute[0]);
            }
            catch (FileAlreadyExistsException fileAlreadyExistsException) {
                // empty catch block
            }
        }
    }

    public static void createParentDirectories(Path path, FileAttribute<?> ... attrs) throws IOException {
        Path normalizedAbsolutePath = path.toAbsolutePath().normalize();
        Path parent = normalizedAbsolutePath.getParent();
        if (parent == null) {
            return;
        }
        if (!java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
            java.nio.file.Files.createDirectories(parent, attrs);
            if (!java.nio.file.Files.isDirectory(parent, new LinkOption[0])) {
                throw new IOException("Unable to create parent directories of " + path);
            }
        }
    }

    public static String getFileExtension(Path path) {
        Path name = path.getFileName();
        if (name == null) {
            return "";
        }
        String fileName = name.toString();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getNameWithoutExtension(Path path) {
        Path name = path.getFileName();
        if (name == null) {
            return "";
        }
        String fileName = name.toString();
        int dotIndex = fileName.lastIndexOf(46);
        return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
    }

    public static void deleteRecursively(Path path, RecursiveDeleteOption ... options) throws IOException {
        Path parentPath = MoreFiles.getParentPath(path);
        if (parentPath == null) {
            throw new FileSystemException(path.toString(), null, "can't delete recursively");
        }
        Collection<IOException> exceptions = null;
        try {
            boolean sdsSupported = false;
            try (DirectoryStream<Path> parent = java.nio.file.Files.newDirectoryStream(parentPath);){
                if (parent instanceof SecureDirectoryStream) {
                    sdsSupported = true;
                    exceptions = MoreFiles.deleteRecursivelySecure((SecureDirectoryStream)parent, path.getFileName());
                }
            }
            if (!sdsSupported) {
                MoreFiles.checkAllowsInsecure(path, options);
                exceptions = MoreFiles.deleteRecursivelyInsecure(path);
            }
        }
        catch (IOException e) {
            if (exceptions == null) {
                throw e;
            }
            exceptions.add(e);
        }
        if (exceptions != null) {
            MoreFiles.throwDeleteFailed(path, exceptions);
        }
    }

    public static void deleteDirectoryContents(Path path, RecursiveDeleteOption ... options) throws IOException {
        Collection<IOException> exceptions = null;
        try (DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(path);){
            if (stream instanceof SecureDirectoryStream) {
                SecureDirectoryStream sds = (SecureDirectoryStream)stream;
                exceptions = MoreFiles.deleteDirectoryContentsSecure(sds);
            } else {
                MoreFiles.checkAllowsInsecure(path, options);
                exceptions = MoreFiles.deleteDirectoryContentsInsecure(stream);
            }
        }
        catch (IOException e) {
            if (exceptions == null) {
                throw e;
            }
            exceptions.add(e);
        }
        if (exceptions != null) {
            MoreFiles.throwDeleteFailed(path, exceptions);
        }
    }

    @Nullable
    private static Collection<IOException> deleteRecursivelySecure(SecureDirectoryStream<Path> dir, Path path) {
        Collection<IOException> exceptions = null;
        try {
            if (MoreFiles.isDirectory(dir, path, LinkOption.NOFOLLOW_LINKS)) {
                try (SecureDirectoryStream<Path> childDir = dir.newDirectoryStream(path, LinkOption.NOFOLLOW_LINKS);){
                    exceptions = MoreFiles.deleteDirectoryContentsSecure(childDir);
                }
                if (exceptions == null) {
                    dir.deleteDirectory(path);
                }
            } else {
                dir.deleteFile(path);
            }
            return exceptions;
        }
        catch (IOException e) {
            return MoreFiles.addException(exceptions, e);
        }
    }

    @Nullable
    private static Collection<IOException> deleteDirectoryContentsSecure(SecureDirectoryStream<Path> dir) {
        Collection<IOException> exceptions = null;
        try {
            for (Path path : dir) {
                exceptions = MoreFiles.concat(exceptions, MoreFiles.deleteRecursivelySecure(dir, path.getFileName()));
            }
            return exceptions;
        }
        catch (DirectoryIteratorException e) {
            return MoreFiles.addException(exceptions, e.getCause());
        }
    }

    @Nullable
    private static Collection<IOException> deleteRecursivelyInsecure(Path path) {
        Collection<IOException> exceptions = null;
        try {
            if (java.nio.file.Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                try (DirectoryStream<Path> stream = java.nio.file.Files.newDirectoryStream(path);){
                    exceptions = MoreFiles.deleteDirectoryContentsInsecure(stream);
                }
            }
            if (exceptions == null) {
                java.nio.file.Files.delete(path);
            }
            return exceptions;
        }
        catch (IOException e) {
            return MoreFiles.addException(exceptions, e);
        }
    }

    @Nullable
    private static Collection<IOException> deleteDirectoryContentsInsecure(DirectoryStream<Path> dir) {
        Collection<IOException> exceptions = null;
        try {
            for (Path entry : dir) {
                exceptions = MoreFiles.concat(exceptions, MoreFiles.deleteRecursivelyInsecure(entry));
            }
            return exceptions;
        }
        catch (DirectoryIteratorException e) {
            return MoreFiles.addException(exceptions, e.getCause());
        }
    }

    @Nullable
    private static Path getParentPath(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            return parent;
        }
        if (path.getNameCount() == 0) {
            return null;
        }
        return path.getFileSystem().getPath(".", new String[0]);
    }

    private static void checkAllowsInsecure(Path path, RecursiveDeleteOption[] options) throws InsecureRecursiveDeleteException {
        if (!Arrays.asList(options).contains((Object)RecursiveDeleteOption.ALLOW_INSECURE)) {
            throw new InsecureRecursiveDeleteException(path.toString());
        }
    }

    private static boolean isDirectory(SecureDirectoryStream<Path> dir, Path name, LinkOption ... options) throws IOException {
        return dir.getFileAttributeView(name, BasicFileAttributeView.class, options).readAttributes().isDirectory();
    }

    private static Collection<IOException> addException(@Nullable Collection<IOException> exceptions, IOException e) {
        if (exceptions == null) {
            exceptions = new ArrayList<IOException>();
        }
        exceptions.add(e);
        return exceptions;
    }

    @Nullable
    private static Collection<IOException> concat(@Nullable Collection<IOException> exceptions, @Nullable Collection<IOException> other) {
        if (exceptions == null) {
            return other;
        }
        if (other != null) {
            exceptions.addAll(other);
        }
        return exceptions;
    }

    private static void throwDeleteFailed(Path path, Collection<IOException> exceptions) throws FileSystemException {
        FileSystemException deleteFailed = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
        for (IOException e : exceptions) {
            deleteFailed.addSuppressed(e);
        }
        throw deleteFailed;
    }

    private static final class DirectoryTreeTraverser
    extends TreeTraverser<Path> {
        private static final DirectoryTreeTraverser INSTANCE = new DirectoryTreeTraverser();

        private DirectoryTreeTraverser() {
        }

        @Override
        public Iterable<Path> children(Path dir) {
            if (java.nio.file.Files.isDirectory(dir, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    return MoreFiles.listFiles(dir);
                }
                catch (IOException e) {
                    throw new DirectoryIteratorException(e);
                }
            }
            return ImmutableList.of();
        }
    }

    private static final class PathByteSink
    extends ByteSink {
        private final Path path;
        private final OpenOption[] options;

        private PathByteSink(Path path, OpenOption ... options) {
            this.path = Preconditions.checkNotNull(path);
            this.options = (OpenOption[])options.clone();
        }

        @Override
        public OutputStream openStream() throws IOException {
            return java.nio.file.Files.newOutputStream(this.path, this.options);
        }

        public String toString() {
            return "MoreFiles.asByteSink(" + this.path + ", " + Arrays.toString(this.options) + ")";
        }
    }

    private static final class PathByteSource
    extends ByteSource {
        private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
        private final Path path;
        private final OpenOption[] options;
        private final boolean followLinks;

        private PathByteSource(Path path, OpenOption ... options) {
            this.path = Preconditions.checkNotNull(path);
            this.options = (OpenOption[])options.clone();
            this.followLinks = PathByteSource.followLinks(this.options);
        }

        private static boolean followLinks(OpenOption[] options) {
            for (OpenOption option : options) {
                if (option != LinkOption.NOFOLLOW_LINKS) continue;
                return false;
            }
            return true;
        }

        @Override
        public InputStream openStream() throws IOException {
            return java.nio.file.Files.newInputStream(this.path, this.options);
        }

        private BasicFileAttributes readAttributes() throws IOException {
            LinkOption[] arrlinkOption;
            if (this.followLinks) {
                arrlinkOption = FOLLOW_LINKS;
            } else {
                LinkOption[] arrlinkOption2 = new LinkOption[1];
                arrlinkOption = arrlinkOption2;
                arrlinkOption2[0] = LinkOption.NOFOLLOW_LINKS;
            }
            return java.nio.file.Files.readAttributes(this.path, BasicFileAttributes.class, arrlinkOption);
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
            BasicFileAttributes attrs = this.readAttributes();
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
            try (SeekableByteChannel channel = java.nio.file.Files.newByteChannel(this.path, this.options);){
                byte[] arrby = Files.readFile(Channels.newInputStream(channel), channel.size());
                return arrby;
            }
        }

        public String toString() {
            return "MoreFiles.asByteSource(" + this.path + ", " + Arrays.toString(this.options) + ")";
        }
    }
}

