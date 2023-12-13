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
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Beta
public final class Files {
   private static final int TEMP_DIR_ATTEMPTS = 10000;
   private static final TreeTraverser FILE_TREE_TRAVERSER = new TreeTraverser() {
      public Iterable children(File var1) {
         if (var1.isDirectory()) {
            File[] var2 = var1.listFiles();
            if (var2 != null) {
               return Collections.unmodifiableList(Arrays.asList(var2));
            }
         }

         return Collections.emptyList();
      }

      public String toString() {
         return "Files.fileTreeTraverser()";
      }

      public Iterable children(Object var1) {
         return this.children((File)var1);
      }
   };

   private Files() {
   }

   public static BufferedReader newReader(File var0, Charset var1) throws FileNotFoundException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new BufferedReader(new InputStreamReader(new FileInputStream(var0), var1));
   }

   public static BufferedWriter newWriter(File var0, Charset var1) throws FileNotFoundException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var0), var1));
   }

   public static ByteSource asByteSource(File var0) {
      return new Files.FileByteSource(var0);
   }

   static byte[] readFile(InputStream var0, long var1) throws IOException {
      if (var1 > 2147483647L) {
         throw new OutOfMemoryError("file is too large to fit in a byte array: " + var1 + " bytes");
      } else {
         return var1 == 0L ? ByteStreams.toByteArray(var0) : ByteStreams.toByteArray(var0, (int)var1);
      }
   }

   public static ByteSink asByteSink(File var0, FileWriteMode... var1) {
      return new Files.FileByteSink(var0, var1);
   }

   public static CharSource asCharSource(File var0, Charset var1) {
      return asByteSource(var0).asCharSource(var1);
   }

   public static CharSink asCharSink(File var0, Charset var1, FileWriteMode... var2) {
      return asByteSink(var0, var2).asCharSink(var1);
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier newInputStreamSupplier(File var0) {
      return ByteStreams.asInputSupplier(asByteSource(var0));
   }

   /** @deprecated */
   @Deprecated
   public static OutputSupplier newOutputStreamSupplier(File var0) {
      return newOutputStreamSupplier(var0, false);
   }

   /** @deprecated */
   @Deprecated
   public static OutputSupplier newOutputStreamSupplier(File var0, boolean var1) {
      return ByteStreams.asOutputSupplier(asByteSink(var0, modes(var1)));
   }

   private static FileWriteMode[] modes(boolean var0) {
      return var0 ? new FileWriteMode[]{FileWriteMode.APPEND} : new FileWriteMode[0];
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier newReaderSupplier(File var0, Charset var1) {
      return CharStreams.asInputSupplier(asCharSource(var0, var1));
   }

   /** @deprecated */
   @Deprecated
   public static OutputSupplier newWriterSupplier(File var0, Charset var1) {
      return newWriterSupplier(var0, var1, false);
   }

   /** @deprecated */
   @Deprecated
   public static OutputSupplier newWriterSupplier(File var0, Charset var1, boolean var2) {
      return CharStreams.asOutputSupplier(asCharSink(var0, var1, modes(var2)));
   }

   public static byte[] toByteArray(File var0) throws IOException {
      return asByteSource(var0).read();
   }

   public static String toString(File var0, Charset var1) throws IOException {
      return asCharSource(var0, var1).read();
   }

   /** @deprecated */
   @Deprecated
   public static void copy(InputSupplier var0, File var1) throws IOException {
      ByteStreams.asByteSource(var0).copyTo(asByteSink(var1));
   }

   public static void write(byte[] var0, File var1) throws IOException {
      asByteSink(var1).write(var0);
   }

   /** @deprecated */
   @Deprecated
   public static void copy(File var0, OutputSupplier var1) throws IOException {
      asByteSource(var0).copyTo(ByteStreams.asByteSink(var1));
   }

   public static void copy(File var0, OutputStream var1) throws IOException {
      asByteSource(var0).copyTo(var1);
   }

   public static void copy(File var0, File var1) throws IOException {
      Preconditions.checkArgument(!var0.equals(var1), "Source %s and destination %s must be different", var0, var1);
      asByteSource(var0).copyTo(asByteSink(var1));
   }

   /** @deprecated */
   @Deprecated
   public static void copy(InputSupplier var0, File var1, Charset var2) throws IOException {
      CharStreams.asCharSource(var0).copyTo(asCharSink(var1, var2));
   }

   public static void write(CharSequence var0, File var1, Charset var2) throws IOException {
      asCharSink(var1, var2).write(var0);
   }

   public static void append(CharSequence var0, File var1, Charset var2) throws IOException {
      write(var0, var1, var2, true);
   }

   private static void write(CharSequence var0, File var1, Charset var2, boolean var3) throws IOException {
      asCharSink(var1, var2, modes(var3)).write(var0);
   }

   /** @deprecated */
   @Deprecated
   public static void copy(File var0, Charset var1, OutputSupplier var2) throws IOException {
      asCharSource(var0, var1).copyTo(CharStreams.asCharSink(var2));
   }

   public static void copy(File var0, Charset var1, Appendable var2) throws IOException {
      asCharSource(var0, var1).copyTo(var2);
   }

   public static boolean equal(File var0, File var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      if (var0 != var1 && !var0.equals(var1)) {
         long var2 = var0.length();
         long var4 = var1.length();
         return var2 != 0L && var4 != 0L && var2 != var4 ? false : asByteSource(var0).contentEquals(asByteSource(var1));
      } else {
         return true;
      }
   }

   public static File createTempDir() {
      File var0 = new File(System.getProperty("java.io.tmpdir"));
      String var1 = System.currentTimeMillis() + "-";

      for(int var2 = 0; var2 < 10000; ++var2) {
         File var3 = new File(var0, var1 + var2);
         if (var3.mkdir()) {
            return var3;
         }
      }

      throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + var1 + "0 to " + var1 + 9999 + ')');
   }

   public static void touch(File var0) throws IOException {
      Preconditions.checkNotNull(var0);
      if (!var0.createNewFile() && !var0.setLastModified(System.currentTimeMillis())) {
         throw new IOException("Unable to update modification time of " + var0);
      }
   }

   public static void createParentDirs(File var0) throws IOException {
      Preconditions.checkNotNull(var0);
      File var1 = var0.getCanonicalFile().getParentFile();
      if (var1 != null) {
         var1.mkdirs();
         if (!var1.isDirectory()) {
            throw new IOException("Unable to create parent directories of " + var0);
         }
      }
   }

   public static void move(File var0, File var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(!var0.equals(var1), "Source %s and destination %s must be different", var0, var1);
      if (!var0.renameTo(var1)) {
         copy(var0, var1);
         if (!var0.delete()) {
            if (!var1.delete()) {
               throw new IOException("Unable to delete " + var1);
            }

            throw new IOException("Unable to delete " + var0);
         }
      }

   }

   public static String readFirstLine(File var0, Charset var1) throws IOException {
      return asCharSource(var0, var1).readFirstLine();
   }

   public static List readLines(File var0, Charset var1) throws IOException {
      return (List)readLines(var0, var1, new LineProcessor() {
         final List result = Lists.newArrayList();

         public boolean processLine(String var1) {
            this.result.add(var1);
            return true;
         }

         public List getResult() {
            return this.result;
         }

         public Object getResult() {
            return this.getResult();
         }
      });
   }

   public static Object readLines(File var0, Charset var1, LineProcessor var2) throws IOException {
      return CharStreams.readLines(newReaderSupplier(var0, var1), var2);
   }

   public static Object readBytes(File var0, ByteProcessor var1) throws IOException {
      return ByteStreams.readBytes(newInputStreamSupplier(var0), var1);
   }

   public static HashCode hash(File var0, HashFunction var1) throws IOException {
      return asByteSource(var0).hash(var1);
   }

   public static MappedByteBuffer map(File var0) throws IOException {
      Preconditions.checkNotNull(var0);
      return map(var0, MapMode.READ_ONLY);
   }

   public static MappedByteBuffer map(File var0, MapMode var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      if (!var0.exists()) {
         throw new FileNotFoundException(var0.toString());
      } else {
         return map(var0, var1, var0.length());
      }
   }

   public static MappedByteBuffer map(File var0, MapMode var1, long var2) throws FileNotFoundException, IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Closer var4 = Closer.create();

      MappedByteBuffer var6;
      try {
         RandomAccessFile var5 = (RandomAccessFile)var4.register(new RandomAccessFile(var0, var1 == MapMode.READ_ONLY ? "r" : "rw"));
         var6 = map(var5, var1, var2);
      } catch (Throwable var8) {
         throw var4.rethrow(var8);
      }

      var4.close();
      return var6;
   }

   private static MappedByteBuffer map(RandomAccessFile var0, MapMode var1, long var2) throws IOException {
      Closer var4 = Closer.create();

      MappedByteBuffer var6;
      try {
         FileChannel var5 = (FileChannel)var4.register(var0.getChannel());
         var6 = var5.map(var1, 0L, var2);
      } catch (Throwable var8) {
         throw var4.rethrow(var8);
      }

      var4.close();
      return var6;
   }

   public static String simplifyPath(String var0) {
      Preconditions.checkNotNull(var0);
      if (var0.length() == 0) {
         return ".";
      } else {
         Iterable var1 = Splitter.on('/').omitEmptyStrings().split(var0);
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.iterator();

         while(true) {
            while(true) {
               while(true) {
                  String var4;
                  do {
                     if (!var3.hasNext()) {
                        String var5 = Joiner.on('/').join((Iterable)var2);
                        if (var0.charAt(0) == '/') {
                           var5 = "/" + var5;
                        }

                        while(var5.startsWith("/../")) {
                           var5 = var5.substring(3);
                        }

                        if (var5.equals("/..")) {
                           var5 = "/";
                        } else if ("".equals(var5)) {
                           var5 = ".";
                        }

                        return var5;
                     }

                     var4 = (String)var3.next();
                  } while(var4.equals("."));

                  if (var4.equals("..")) {
                     if (var2.size() > 0 && !((String)var2.get(var2.size() - 1)).equals("..")) {
                        var2.remove(var2.size() - 1);
                     } else {
                        var2.add("..");
                     }
                  } else {
                     var2.add(var4);
                  }
               }
            }
         }
      }
   }

   public static String getFileExtension(String var0) {
      Preconditions.checkNotNull(var0);
      String var1 = (new File(var0)).getName();
      int var2 = var1.lastIndexOf(46);
      return var2 == -1 ? "" : var1.substring(var2 + 1);
   }

   public static String getNameWithoutExtension(String var0) {
      Preconditions.checkNotNull(var0);
      String var1 = (new File(var0)).getName();
      int var2 = var1.lastIndexOf(46);
      return var2 == -1 ? var1 : var1.substring(0, var2);
   }

   public static TreeTraverser fileTreeTraverser() {
      return FILE_TREE_TRAVERSER;
   }

   public static Predicate isDirectory() {
      return Files.FilePredicate.IS_DIRECTORY;
   }

   public static Predicate isFile() {
      return Files.FilePredicate.IS_FILE;
   }

   private static enum FilePredicate implements Predicate {
      IS_DIRECTORY {
         public boolean apply(File var1) {
            return var1.isDirectory();
         }

         public String toString() {
            return "Files.isDirectory()";
         }

         public boolean apply(Object var1) {
            return this.apply((File)var1);
         }
      },
      IS_FILE {
         public boolean apply(File var1) {
            return var1.isFile();
         }

         public String toString() {
            return "Files.isFile()";
         }

         public boolean apply(Object var1) {
            return this.apply((File)var1);
         }
      };

      private static final Files.FilePredicate[] $VALUES = new Files.FilePredicate[]{IS_DIRECTORY, IS_FILE};

      private FilePredicate() {
      }

      FilePredicate(Object var3) {
         this();
      }
   }

   private static final class FileByteSink extends ByteSink {
      private final File file;
      private final ImmutableSet modes;

      private FileByteSink(File var1, FileWriteMode... var2) {
         this.file = (File)Preconditions.checkNotNull(var1);
         this.modes = ImmutableSet.copyOf((Object[])var2);
      }

      public FileOutputStream openStream() throws IOException {
         return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
      }

      public String toString() {
         return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
      }

      public OutputStream openStream() throws IOException {
         return this.openStream();
      }

      FileByteSink(File var1, FileWriteMode[] var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class FileByteSource extends ByteSource {
      private final File file;

      private FileByteSource(File var1) {
         this.file = (File)Preconditions.checkNotNull(var1);
      }

      public FileInputStream openStream() throws IOException {
         return new FileInputStream(this.file);
      }

      public long size() throws IOException {
         if (!this.file.isFile()) {
            throw new FileNotFoundException(this.file.toString());
         } else {
            return this.file.length();
         }
      }

      public byte[] read() throws IOException {
         Closer var1 = Closer.create();

         byte[] var3;
         try {
            FileInputStream var2 = (FileInputStream)var1.register(this.openStream());
            var3 = Files.readFile(var2, var2.getChannel().size());
         } catch (Throwable var5) {
            throw var1.rethrow(var5);
         }

         var1.close();
         return var3;
      }

      public String toString() {
         return "Files.asByteSource(" + this.file + ")";
      }

      public InputStream openStream() throws IOException {
         return this.openStream();
      }

      FileByteSource(File var1, Object var2) {
         this(var1);
      }
   }
}
