package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Beta
public final class FileBackedOutputStream extends OutputStream {
   private final int fileThreshold;
   private final boolean resetOnFinalize;
   private final ByteSource source;
   private OutputStream out;
   private FileBackedOutputStream.MemoryOutput memory;
   private File file;

   @VisibleForTesting
   synchronized File getFile() {
      return this.file;
   }

   public FileBackedOutputStream(int var1) {
      this(var1, false);
   }

   public FileBackedOutputStream(int var1, boolean var2) {
      this.fileThreshold = var1;
      this.resetOnFinalize = var2;
      this.memory = new FileBackedOutputStream.MemoryOutput();
      this.out = this.memory;
      if (var2) {
         this.source = new ByteSource(this) {
            final FileBackedOutputStream this$0;

            {
               this.this$0 = var1;
            }

            public InputStream openStream() throws IOException {
               return FileBackedOutputStream.access$100(this.this$0);
            }

            protected void finalize() {
               try {
                  this.this$0.reset();
               } catch (Throwable var2) {
                  var2.printStackTrace(System.err);
               }

            }
         };
      } else {
         this.source = new ByteSource(this) {
            final FileBackedOutputStream this$0;

            {
               this.this$0 = var1;
            }

            public InputStream openStream() throws IOException {
               return FileBackedOutputStream.access$100(this.this$0);
            }
         };
      }

   }

   public ByteSource asByteSource() {
      return this.source;
   }

   private synchronized InputStream openInputStream() throws IOException {
      return (InputStream)(this.file != null ? new FileInputStream(this.file) : new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount()));
   }

   public synchronized void reset() throws IOException {
      this.close();
      if (this.memory == null) {
         this.memory = new FileBackedOutputStream.MemoryOutput();
      } else {
         this.memory.reset();
      }

      this.out = this.memory;
      if (this.file != null) {
         File var1 = this.file;
         this.file = null;
         if (!var1.delete()) {
            throw new IOException("Could not delete: " + var1);
         }
      }

   }

   public synchronized void write(int var1) throws IOException {
      this.update(1);
      this.out.write(var1);
   }

   public synchronized void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public synchronized void write(byte[] var1, int var2, int var3) throws IOException {
      this.update(var3);
      this.out.write(var1, var2, var3);
   }

   public synchronized void close() throws IOException {
      this.out.close();
   }

   public synchronized void flush() throws IOException {
      this.out.flush();
   }

   private void update(int var1) throws IOException {
      if (this.file == null && this.memory.getCount() + var1 > this.fileThreshold) {
         File var2 = File.createTempFile("FileBackedOutputStream", (String)null);
         if (this.resetOnFinalize) {
            var2.deleteOnExit();
         }

         FileOutputStream var3 = new FileOutputStream(var2);
         var3.write(this.memory.getBuffer(), 0, this.memory.getCount());
         var3.flush();
         this.out = var3;
         this.file = var2;
         this.memory = null;
      }

   }

   static InputStream access$100(FileBackedOutputStream var0) throws IOException {
      return var0.openInputStream();
   }

   private static class MemoryOutput extends ByteArrayOutputStream {
      private MemoryOutput() {
      }

      byte[] getBuffer() {
         return this.buf;
      }

      int getCount() {
         return this.count;
      }

      MemoryOutput(Object var1) {
         this();
      }
   }
}
