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
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
  
  public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
    Preconditions.checkNotNull(file);
    Preconditions.checkNotNull(charset);
    return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
  }
  
  public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
    Preconditions.checkNotNull(file);
    Preconditions.checkNotNull(charset);
    return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
  }
  
  public static ByteSource asByteSource(File file) {
    return new FileByteSource(file);
  }
  
  private static final class FileByteSource extends ByteSource {
    private final File file;
    
    private FileByteSource(File file) {
      this.file = (File)Preconditions.checkNotNull(file);
    }
    
    public FileInputStream openStream() throws IOException {
      return new FileInputStream(this.file);
    }
    
    public long size() throws IOException {
      if (!this.file.isFile())
        throw new FileNotFoundException(this.file.toString()); 
      return this.file.length();
    }
    
    public byte[] read() throws IOException {
      Closer closer = Closer.create();
      try {
        FileInputStream in = closer.<FileInputStream>register(openStream());
        return Files.readFile(in, in.getChannel().size());
      } catch (Throwable e) {
        throw closer.rethrow(e);
      } finally {
        closer.close();
      } 
    }
    
    public String toString() {
      return "Files.asByteSource(" + this.file + ")";
    }
  }
  
  static byte[] readFile(InputStream in, long expectedSize) throws IOException {
    if (expectedSize > 2147483647L)
      throw new OutOfMemoryError("file is too large to fit in a byte array: " + expectedSize + " bytes"); 
    return (expectedSize == 0L) ? ByteStreams.toByteArray(in) : ByteStreams.toByteArray(in, (int)expectedSize);
  }
  
  public static ByteSink asByteSink(File file, FileWriteMode... modes) {
    return new FileByteSink(file, modes);
  }
  
  private static final class FileByteSink extends ByteSink {
    private final File file;
    
    private final ImmutableSet<FileWriteMode> modes;
    
    private FileByteSink(File file, FileWriteMode... modes) {
      this.file = (File)Preconditions.checkNotNull(file);
      this.modes = ImmutableSet.copyOf((Object[])modes);
    }
    
    public FileOutputStream openStream() throws IOException {
      return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
    }
    
    public String toString() {
      return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
    }
  }
  
  public static CharSource asCharSource(File file, Charset charset) {
    return asByteSource(file).asCharSource(charset);
  }
  
  public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
    return asByteSink(file, modes).asCharSink(charset);
  }
  
  @Deprecated
  public static InputSupplier<FileInputStream> newInputStreamSupplier(File file) {
    return ByteStreams.asInputSupplier(asByteSource(file));
  }
  
  @Deprecated
  public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file) {
    return newOutputStreamSupplier(file, false);
  }
  
  @Deprecated
  public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file, boolean append) {
    return ByteStreams.asOutputSupplier(asByteSink(file, modes(append)));
  }
  
  private static FileWriteMode[] modes(boolean append) {
    (new FileWriteMode[1])[0] = FileWriteMode.APPEND;
    return append ? new FileWriteMode[1] : new FileWriteMode[0];
  }
  
  @Deprecated
  public static InputSupplier<InputStreamReader> newReaderSupplier(File file, Charset charset) {
    return CharStreams.asInputSupplier(asCharSource(file, charset));
  }
  
  @Deprecated
  public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset) {
    return newWriterSupplier(file, charset, false);
  }
  
  @Deprecated
  public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset, boolean append) {
    return CharStreams.asOutputSupplier(asCharSink(file, charset, modes(append)));
  }
  
  public static byte[] toByteArray(File file) throws IOException {
    return asByteSource(file).read();
  }
  
  public static String toString(File file, Charset charset) throws IOException {
    return asCharSource(file, charset).read();
  }
  
  @Deprecated
  public static void copy(InputSupplier<? extends InputStream> from, File to) throws IOException {
    ByteStreams.asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
  }
  
  public static void write(byte[] from, File to) throws IOException {
    asByteSink(to, new FileWriteMode[0]).write(from);
  }
  
  @Deprecated
  public static void copy(File from, OutputSupplier<? extends OutputStream> to) throws IOException {
    asByteSource(from).copyTo(ByteStreams.asByteSink(to));
  }
  
  public static void copy(File from, OutputStream to) throws IOException {
    asByteSource(from).copyTo(to);
  }
  
  public static void copy(File from, File to) throws IOException {
    Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", new Object[] { from, to });
    asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
  }
  
  @Deprecated
  public static <R extends Readable & java.io.Closeable> void copy(InputSupplier<R> from, File to, Charset charset) throws IOException {
    CharStreams.asCharSource(from).copyTo(asCharSink(to, charset, new FileWriteMode[0]));
  }
  
  public static void write(CharSequence from, File to, Charset charset) throws IOException {
    asCharSink(to, charset, new FileWriteMode[0]).write(from);
  }
  
  public static void append(CharSequence from, File to, Charset charset) throws IOException {
    write(from, to, charset, true);
  }
  
  private static void write(CharSequence from, File to, Charset charset, boolean append) throws IOException {
    asCharSink(to, charset, modes(append)).write(from);
  }
  
  @Deprecated
  public static <W extends Appendable & java.io.Closeable> void copy(File from, Charset charset, OutputSupplier<W> to) throws IOException {
    asCharSource(from, charset).copyTo(CharStreams.asCharSink(to));
  }
  
  public static void copy(File from, Charset charset, Appendable to) throws IOException {
    asCharSource(from, charset).copyTo(to);
  }
  
  public static boolean equal(File file1, File file2) throws IOException {
    Preconditions.checkNotNull(file1);
    Preconditions.checkNotNull(file2);
    if (file1 == file2 || file1.equals(file2))
      return true; 
    long len1 = file1.length();
    long len2 = file2.length();
    if (len1 != 0L && len2 != 0L && len1 != len2)
      return false; 
    return asByteSource(file1).contentEquals(asByteSource(file2));
  }
  
  public static File createTempDir() {
    File baseDir = new File(System.getProperty("java.io.tmpdir"));
    String baseName = System.currentTimeMillis() + "-";
    for (int counter = 0; counter < 10000; counter++) {
      File tempDir = new File(baseDir, baseName + counter);
      if (tempDir.mkdir())
        return tempDir; 
    } 
    throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + '✏' + ')');
  }
  
  public static void touch(File file) throws IOException {
    Preconditions.checkNotNull(file);
    if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis()))
      throw new IOException("Unable to update modification time of " + file); 
  }
  
  public static void createParentDirs(File file) throws IOException {
    Preconditions.checkNotNull(file);
    File parent = file.getCanonicalFile().getParentFile();
    if (parent == null)
      return; 
    parent.mkdirs();
    if (!parent.isDirectory())
      throw new IOException("Unable to create parent directories of " + file); 
  }
  
  public static void move(File from, File to) throws IOException {
    Preconditions.checkNotNull(from);
    Preconditions.checkNotNull(to);
    Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", new Object[] { from, to });
    if (!from.renameTo(to)) {
      copy(from, to);
      if (!from.delete()) {
        if (!to.delete())
          throw new IOException("Unable to delete " + to); 
        throw new IOException("Unable to delete " + from);
      } 
    } 
  }
  
  public static String readFirstLine(File file, Charset charset) throws IOException {
    return asCharSource(file, charset).readFirstLine();
  }
  
  public static List<String> readLines(File file, Charset charset) throws IOException {
    return readLines(file, charset, new LineProcessor<List<String>>() {
          final List<String> result = Lists.newArrayList();
          
          public boolean processLine(String line) {
            this.result.add(line);
            return true;
          }
          
          public List<String> getResult() {
            return this.result;
          }
        });
  }
  
  public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
    return CharStreams.readLines(newReaderSupplier(file, charset), callback);
  }
  
  public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
    return ByteStreams.readBytes((InputSupplier)newInputStreamSupplier(file), processor);
  }
  
  public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
    return asByteSource(file).hash(hashFunction);
  }
  
  public static MappedByteBuffer map(File file) throws IOException {
    Preconditions.checkNotNull(file);
    return map(file, FileChannel.MapMode.READ_ONLY);
  }
  
  public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
    Preconditions.checkNotNull(file);
    Preconditions.checkNotNull(mode);
    if (!file.exists())
      throw new FileNotFoundException(file.toString()); 
    return map(file, mode, file.length());
  }
  
  public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws FileNotFoundException, IOException {
    Preconditions.checkNotNull(file);
    Preconditions.checkNotNull(mode);
    Closer closer = Closer.create();
    try {
      RandomAccessFile raf = closer.<RandomAccessFile>register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
      return map(raf, mode, size);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  private static MappedByteBuffer map(RandomAccessFile raf, FileChannel.MapMode mode, long size) throws IOException {
    Closer closer = Closer.create();
    try {
      FileChannel channel = closer.<FileChannel>register(raf.getChannel());
      return channel.map(mode, 0L, size);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public static String simplifyPath(String pathname) {
    Preconditions.checkNotNull(pathname);
    if (pathname.length() == 0)
      return "."; 
    Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
    List<String> path = new ArrayList<String>();
    for (String component : components) {
      if (component.equals("."))
        continue; 
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
    if (pathname.charAt(0) == '/')
      result = "/" + result; 
    while (result.startsWith("/../"))
      result = result.substring(3); 
    if (result.equals("/..")) {
      result = "/";
    } else if ("".equals(result)) {
      result = ".";
    } 
    return result;
  }
  
  public static String getFileExtension(String fullName) {
    Preconditions.checkNotNull(fullName);
    String fileName = (new File(fullName)).getName();
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
  }
  
  public static String getNameWithoutExtension(String file) {
    Preconditions.checkNotNull(file);
    String fileName = (new File(file)).getName();
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
  }
  
  public static TreeTraverser<File> fileTreeTraverser() {
    return FILE_TREE_TRAVERSER;
  }
  
  private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser<File>() {
      public Iterable<File> children(File file) {
        if (file.isDirectory()) {
          File[] files = file.listFiles();
          if (files != null)
            return Collections.unmodifiableList(Arrays.asList(files)); 
        } 
        return Collections.emptyList();
      }
      
      public String toString() {
        return "Files.fileTreeTraverser()";
      }
    };
  
  public static Predicate<File> isDirectory() {
    return FilePredicate.IS_DIRECTORY;
  }
  
  public static Predicate<File> isFile() {
    return FilePredicate.IS_FILE;
  }
  
  private enum FilePredicate implements Predicate<File> {
    IS_DIRECTORY {
      public boolean apply(File file) {
        return file.isDirectory();
      }
      
      public String toString() {
        return "Files.isDirectory()";
      }
    },
    IS_FILE {
      public boolean apply(File file) {
        return file.isFile();
      }
      
      public String toString() {
        return "Files.isFile()";
      }
    };
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\Files.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */