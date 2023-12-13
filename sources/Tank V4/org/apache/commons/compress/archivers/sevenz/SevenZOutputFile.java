package org.apache.commons.compress.archivers.sevenz;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.CountingOutputStream;

public class SevenZOutputFile implements Closeable {
   private final RandomAccessFile file;
   private final List files = new ArrayList();
   private int numNonEmptyStreams = 0;
   private final CRC32 crc32 = new CRC32();
   private final CRC32 compressedCrc32 = new CRC32();
   private long fileBytesWritten = 0L;
   private boolean finished = false;
   private CountingOutputStream currentOutputStream;
   private CountingOutputStream[] additionalCountingStreams;
   private Iterable contentMethods;
   private final Map additionalSizes;

   public SevenZOutputFile(File var1) throws IOException {
      this.contentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.LZMA2));
      this.additionalSizes = new HashMap();
      this.file = new RandomAccessFile(var1, "rw");
      this.file.seek(32L);
   }

   public void setContentCompression(SevenZMethod var1) {
      this.setContentMethods(Collections.singletonList(new SevenZMethodConfiguration(var1)));
   }

   public void setContentMethods(Iterable var1) {
      this.contentMethods = reverse(var1);
   }

   public void close() throws IOException {
      if (!this.finished) {
         this.finish();
      }

      this.file.close();
   }

   public SevenZArchiveEntry createArchiveEntry(File var1, String var2) throws IOException {
      SevenZArchiveEntry var3 = new SevenZArchiveEntry();
      var3.setDirectory(var1.isDirectory());
      var3.setName(var2);
      var3.setLastModifiedDate(new Date(var1.lastModified()));
      return var3;
   }

   public void putArchiveEntry(ArchiveEntry var1) throws IOException {
      SevenZArchiveEntry var2 = (SevenZArchiveEntry)var1;
      this.files.add(var2);
   }

   public void closeArchiveEntry() throws IOException {
      if (this.currentOutputStream != null) {
         this.currentOutputStream.flush();
         this.currentOutputStream.close();
      }

      SevenZArchiveEntry var1 = (SevenZArchiveEntry)this.files.get(this.files.size() - 1);
      if (this.fileBytesWritten > 0L) {
         var1.setHasStream(true);
         ++this.numNonEmptyStreams;
         var1.setSize(this.currentOutputStream.getBytesWritten());
         var1.setCompressedSize(this.fileBytesWritten);
         var1.setCrcValue(this.crc32.getValue());
         var1.setCompressedCrcValue(this.compressedCrc32.getValue());
         var1.setHasCrc(true);
         if (this.additionalCountingStreams != null) {
            long[] var2 = new long[this.additionalCountingStreams.length];

            for(int var3 = 0; var3 < this.additionalCountingStreams.length; ++var3) {
               var2[var3] = this.additionalCountingStreams[var3].getBytesWritten();
            }

            this.additionalSizes.put(var1, var2);
         }
      } else {
         var1.setHasStream(false);
         var1.setSize(0L);
         var1.setCompressedSize(0L);
         var1.setHasCrc(false);
      }

      this.currentOutputStream = null;
      this.additionalCountingStreams = null;
      this.crc32.reset();
      this.compressedCrc32.reset();
      this.fileBytesWritten = 0L;
   }

   public void write(int var1) throws IOException {
      this.getCurrentOutputStream().write(var1);
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var3 > 0) {
         this.getCurrentOutputStream().write(var1, var2, var3);
      }

   }

   public void finish() throws IOException {
      if (this.finished) {
         throw new IOException("This archive has already been finished");
      } else {
         this.finished = true;
         long var1 = this.file.getFilePointer();
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         DataOutputStream var4 = new DataOutputStream(var3);
         this.writeHeader(var4);
         var4.flush();
         byte[] var5 = var3.toByteArray();
         this.file.write(var5);
         CRC32 var6 = new CRC32();
         this.file.seek(0L);
         this.file.write(SevenZFile.sevenZSignature);
         this.file.write(0);
         this.file.write(2);
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         DataOutputStream var8 = new DataOutputStream(var7);
         var8.writeLong(Long.reverseBytes(var1 - 32L));
         var8.writeLong(Long.reverseBytes(4294967295L & (long)var5.length));
         var6.reset();
         var6.update(var5);
         var8.writeInt(Integer.reverseBytes((int)var6.getValue()));
         var8.flush();
         byte[] var9 = var7.toByteArray();
         var6.reset();
         var6.update(var9);
         this.file.writeInt(Integer.reverseBytes((int)var6.getValue()));
         this.file.write(var9);
      }
   }

   private OutputStream getCurrentOutputStream() throws IOException {
      if (this.currentOutputStream == null) {
         this.currentOutputStream = this.setupFileOutputStream();
      }

      return this.currentOutputStream;
   }

   private CountingOutputStream setupFileOutputStream() throws IOException {
      if (this.files.isEmpty()) {
         throw new IllegalStateException("No current 7z entry");
      } else {
         Object var1 = new SevenZOutputFile.OutputStreamWrapper(this);
         ArrayList var2 = new ArrayList();
         boolean var3 = true;

         for(Iterator var4 = this.getContentMethods((SevenZArchiveEntry)this.files.get(this.files.size() - 1)).iterator(); var4.hasNext(); var3 = false) {
            SevenZMethodConfiguration var5 = (SevenZMethodConfiguration)var4.next();
            if (!var3) {
               CountingOutputStream var6 = new CountingOutputStream((OutputStream)var1);
               var2.add(var6);
               var1 = var6;
            }

            var1 = Coders.addEncoder((OutputStream)var1, var5.getMethod(), var5.getOptions());
         }

         if (!var2.isEmpty()) {
            this.additionalCountingStreams = (CountingOutputStream[])var2.toArray(new CountingOutputStream[var2.size()]);
         }

         return new CountingOutputStream(this, (OutputStream)var1) {
            final SevenZOutputFile this$0;

            {
               this.this$0 = var1;
            }

            public void write(int var1) throws IOException {
               super.write(var1);
               SevenZOutputFile.access$100(this.this$0).update(var1);
            }

            public void write(byte[] var1) throws IOException {
               super.write(var1);
               SevenZOutputFile.access$100(this.this$0).update(var1);
            }

            public void write(byte[] var1, int var2, int var3) throws IOException {
               super.write(var1, var2, var3);
               SevenZOutputFile.access$100(this.this$0).update(var1, var2, var3);
            }
         };
      }
   }

   private Iterable getContentMethods(SevenZArchiveEntry var1) {
      Iterable var2 = var1.getContentMethods();
      return var2 == null ? this.contentMethods : var2;
   }

   private void writeHeader(DataOutput var1) throws IOException {
      var1.write(1);
      var1.write(4);
      this.writeStreamsInfo(var1);
      this.writeFilesInfo(var1);
      var1.write(0);
   }

   private void writeStreamsInfo(DataOutput var1) throws IOException {
      if (this.numNonEmptyStreams > 0) {
         this.writePackInfo(var1);
         this.writeUnpackInfo(var1);
      }

      this.writeSubStreamsInfo(var1);
      var1.write(0);
   }

   private void writePackInfo(DataOutput var1) throws IOException {
      var1.write(6);
      this.writeUint64(var1, 0L);
      this.writeUint64(var1, 4294967295L & (long)this.numNonEmptyStreams);
      var1.write(9);
      Iterator var2 = this.files.iterator();

      SevenZArchiveEntry var3;
      while(var2.hasNext()) {
         var3 = (SevenZArchiveEntry)var2.next();
         if (var3.hasStream()) {
            this.writeUint64(var1, var3.getCompressedSize());
         }
      }

      var1.write(10);
      var1.write(1);
      var2 = this.files.iterator();

      while(var2.hasNext()) {
         var3 = (SevenZArchiveEntry)var2.next();
         if (var3.hasStream()) {
            var1.writeInt(Integer.reverseBytes((int)var3.getCompressedCrcValue()));
         }
      }

      var1.write(0);
   }

   private void writeUnpackInfo(DataOutput var1) throws IOException {
      var1.write(7);
      var1.write(11);
      this.writeUint64(var1, (long)this.numNonEmptyStreams);
      var1.write(0);
      Iterator var2 = this.files.iterator();

      SevenZArchiveEntry var3;
      while(var2.hasNext()) {
         var3 = (SevenZArchiveEntry)var2.next();
         if (var3.hasStream()) {
            this.writeFolder(var1, var3);
         }
      }

      var1.write(12);
      var2 = this.files.iterator();

      while(true) {
         do {
            if (!var2.hasNext()) {
               var1.write(10);
               var1.write(1);
               var2 = this.files.iterator();

               while(var2.hasNext()) {
                  var3 = (SevenZArchiveEntry)var2.next();
                  if (var3.hasStream()) {
                     var1.writeInt(Integer.reverseBytes((int)var3.getCrcValue()));
                  }
               }

               var1.write(0);
               return;
            }

            var3 = (SevenZArchiveEntry)var2.next();
         } while(!var3.hasStream());

         long[] var4 = (long[])this.additionalSizes.get(var3);
         if (var4 != null) {
            long[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               long var8 = var5[var7];
               this.writeUint64(var1, var8);
            }
         }

         this.writeUint64(var1, var3.getSize());
      }
   }

   private void writeFolder(DataOutput var1, SevenZArchiveEntry var2) throws IOException {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      int var4 = 0;
      Iterator var5 = this.getContentMethods(var2).iterator();

      while(var5.hasNext()) {
         SevenZMethodConfiguration var6 = (SevenZMethodConfiguration)var5.next();
         ++var4;
         this.writeSingleCodec(var6, var3);
      }

      this.writeUint64(var1, (long)var4);
      var1.write(var3.toByteArray());

      for(int var7 = 0; var7 < var4 - 1; ++var7) {
         this.writeUint64(var1, (long)(var7 + 1));
         this.writeUint64(var1, (long)var7);
      }

   }

   private void writeSingleCodec(SevenZMethodConfiguration var1, OutputStream var2) throws IOException {
      byte[] var3 = var1.getMethod().getId();
      byte[] var4 = Coders.findByMethod(var1.getMethod()).getOptionsAsProperties(var1.getOptions());
      int var5 = var3.length;
      if (var4.length > 0) {
         var5 |= 32;
      }

      var2.write(var5);
      var2.write(var3);
      if (var4.length > 0) {
         var2.write(var4.length);
         var2.write(var4);
      }

   }

   private void writeSubStreamsInfo(DataOutput var1) throws IOException {
      var1.write(8);
      var1.write(0);
   }

   private void writeFilesInfo(DataOutput var1) throws IOException {
      var1.write(5);
      this.writeUint64(var1, (long)this.files.size());
      this.writeFileEmptyStreams(var1);
      this.writeFileEmptyFiles(var1);
      this.writeFileAntiItems(var1);
      this.writeFileNames(var1);
      this.writeFileCTimes(var1);
      this.writeFileATimes(var1);
      this.writeFileMTimes(var1);
      this.writeFileWindowsAttributes(var1);
      var1.write(0);
   }

   private void writeFileEmptyStreams(DataOutput var1) throws IOException {
      boolean var2 = false;
      Iterator var3 = this.files.iterator();

      while(var3.hasNext()) {
         SevenZArchiveEntry var4 = (SevenZArchiveEntry)var3.next();
         if (!var4.hasStream()) {
            var2 = true;
            break;
         }
      }

      if (var2) {
         var1.write(14);
         BitSet var7 = new BitSet(this.files.size());

         for(int var8 = 0; var8 < this.files.size(); ++var8) {
            var7.set(var8, !((SevenZArchiveEntry)this.files.get(var8)).hasStream());
         }

         ByteArrayOutputStream var9 = new ByteArrayOutputStream();
         DataOutputStream var5 = new DataOutputStream(var9);
         this.writeBits(var5, var7, this.files.size());
         var5.flush();
         byte[] var6 = var9.toByteArray();
         this.writeUint64(var1, (long)var6.length);
         var1.write(var6);
      }

   }

   private void writeFileEmptyFiles(DataOutput var1) throws IOException {
      boolean var2 = false;
      int var3 = 0;
      BitSet var4 = new BitSet(0);

      for(int var5 = 0; var5 < this.files.size(); ++var5) {
         if (!((SevenZArchiveEntry)this.files.get(var5)).hasStream()) {
            boolean var6 = ((SevenZArchiveEntry)this.files.get(var5)).isDirectory();
            var4.set(var3++, !var6);
            var2 |= !var6;
         }
      }

      if (var2) {
         var1.write(15);
         ByteArrayOutputStream var8 = new ByteArrayOutputStream();
         DataOutputStream var9 = new DataOutputStream(var8);
         this.writeBits(var9, var4, var3);
         var9.flush();
         byte[] var7 = var8.toByteArray();
         this.writeUint64(var1, (long)var7.length);
         var1.write(var7);
      }

   }

   private void writeFileAntiItems(DataOutput var1) throws IOException {
      boolean var2 = false;
      BitSet var3 = new BitSet(0);
      int var4 = 0;

      for(int var5 = 0; var5 < this.files.size(); ++var5) {
         if (!((SevenZArchiveEntry)this.files.get(var5)).hasStream()) {
            boolean var6 = ((SevenZArchiveEntry)this.files.get(var5)).isAntiItem();
            var3.set(var4++, var6);
            var2 |= var6;
         }
      }

      if (var2) {
         var1.write(16);
         ByteArrayOutputStream var8 = new ByteArrayOutputStream();
         DataOutputStream var9 = new DataOutputStream(var8);
         this.writeBits(var9, var3, var4);
         var9.flush();
         byte[] var7 = var8.toByteArray();
         this.writeUint64(var1, (long)var7.length);
         var1.write(var7);
      }

   }

   private void writeFileNames(DataOutput var1) throws IOException {
      var1.write(17);
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      DataOutputStream var3 = new DataOutputStream(var2);
      var3.write(0);
      Iterator var4 = this.files.iterator();

      while(var4.hasNext()) {
         SevenZArchiveEntry var5 = (SevenZArchiveEntry)var4.next();
         var3.write(var5.getName().getBytes("UTF-16LE"));
         var3.writeShort(0);
      }

      var3.flush();
      byte[] var6 = var2.toByteArray();
      this.writeUint64(var1, (long)var6.length);
      var1.write(var6);
   }

   private void writeFileCTimes(DataOutput var1) throws IOException {
      int var2 = 0;
      Iterator var3 = this.files.iterator();

      while(var3.hasNext()) {
         SevenZArchiveEntry var4 = (SevenZArchiveEntry)var3.next();
         if (var4.getHasCreationDate()) {
            ++var2;
         }
      }

      if (var2 > 0) {
         var1.write(18);
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         DataOutputStream var8 = new DataOutputStream(var7);
         if (var2 == this.files.size()) {
            var8.write(1);
         } else {
            var8.write(0);
            BitSet var5 = new BitSet(this.files.size());

            for(int var6 = 0; var6 < this.files.size(); ++var6) {
               var5.set(var6, ((SevenZArchiveEntry)this.files.get(var6)).getHasCreationDate());
            }

            this.writeBits(var8, var5, this.files.size());
         }

         var8.write(0);
         Iterator var9 = this.files.iterator();

         while(var9.hasNext()) {
            SevenZArchiveEntry var11 = (SevenZArchiveEntry)var9.next();
            if (var11.getHasCreationDate()) {
               var8.writeLong(Long.reverseBytes(SevenZArchiveEntry.javaTimeToNtfsTime(var11.getCreationDate())));
            }
         }

         var8.flush();
         byte[] var10 = var7.toByteArray();
         this.writeUint64(var1, (long)var10.length);
         var1.write(var10);
      }

   }

   private void writeFileATimes(DataOutput var1) throws IOException {
      int var2 = 0;
      Iterator var3 = this.files.iterator();

      while(var3.hasNext()) {
         SevenZArchiveEntry var4 = (SevenZArchiveEntry)var3.next();
         if (var4.getHasAccessDate()) {
            ++var2;
         }
      }

      if (var2 > 0) {
         var1.write(19);
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         DataOutputStream var8 = new DataOutputStream(var7);
         if (var2 == this.files.size()) {
            var8.write(1);
         } else {
            var8.write(0);
            BitSet var5 = new BitSet(this.files.size());

            for(int var6 = 0; var6 < this.files.size(); ++var6) {
               var5.set(var6, ((SevenZArchiveEntry)this.files.get(var6)).getHasAccessDate());
            }

            this.writeBits(var8, var5, this.files.size());
         }

         var8.write(0);
         Iterator var9 = this.files.iterator();

         while(var9.hasNext()) {
            SevenZArchiveEntry var11 = (SevenZArchiveEntry)var9.next();
            if (var11.getHasAccessDate()) {
               var8.writeLong(Long.reverseBytes(SevenZArchiveEntry.javaTimeToNtfsTime(var11.getAccessDate())));
            }
         }

         var8.flush();
         byte[] var10 = var7.toByteArray();
         this.writeUint64(var1, (long)var10.length);
         var1.write(var10);
      }

   }

   private void writeFileMTimes(DataOutput var1) throws IOException {
      int var2 = 0;
      Iterator var3 = this.files.iterator();

      while(var3.hasNext()) {
         SevenZArchiveEntry var4 = (SevenZArchiveEntry)var3.next();
         if (var4.getHasLastModifiedDate()) {
            ++var2;
         }
      }

      if (var2 > 0) {
         var1.write(20);
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         DataOutputStream var8 = new DataOutputStream(var7);
         if (var2 == this.files.size()) {
            var8.write(1);
         } else {
            var8.write(0);
            BitSet var5 = new BitSet(this.files.size());

            for(int var6 = 0; var6 < this.files.size(); ++var6) {
               var5.set(var6, ((SevenZArchiveEntry)this.files.get(var6)).getHasLastModifiedDate());
            }

            this.writeBits(var8, var5, this.files.size());
         }

         var8.write(0);
         Iterator var9 = this.files.iterator();

         while(var9.hasNext()) {
            SevenZArchiveEntry var11 = (SevenZArchiveEntry)var9.next();
            if (var11.getHasLastModifiedDate()) {
               var8.writeLong(Long.reverseBytes(SevenZArchiveEntry.javaTimeToNtfsTime(var11.getLastModifiedDate())));
            }
         }

         var8.flush();
         byte[] var10 = var7.toByteArray();
         this.writeUint64(var1, (long)var10.length);
         var1.write(var10);
      }

   }

   private void writeFileWindowsAttributes(DataOutput var1) throws IOException {
      int var2 = 0;
      Iterator var3 = this.files.iterator();

      while(var3.hasNext()) {
         SevenZArchiveEntry var4 = (SevenZArchiveEntry)var3.next();
         if (var4.getHasWindowsAttributes()) {
            ++var2;
         }
      }

      if (var2 > 0) {
         var1.write(21);
         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         DataOutputStream var8 = new DataOutputStream(var7);
         if (var2 == this.files.size()) {
            var8.write(1);
         } else {
            var8.write(0);
            BitSet var5 = new BitSet(this.files.size());

            for(int var6 = 0; var6 < this.files.size(); ++var6) {
               var5.set(var6, ((SevenZArchiveEntry)this.files.get(var6)).getHasWindowsAttributes());
            }

            this.writeBits(var8, var5, this.files.size());
         }

         var8.write(0);
         Iterator var9 = this.files.iterator();

         while(var9.hasNext()) {
            SevenZArchiveEntry var11 = (SevenZArchiveEntry)var9.next();
            if (var11.getHasWindowsAttributes()) {
               var8.writeInt(Integer.reverseBytes(var11.getWindowsAttributes()));
            }
         }

         var8.flush();
         byte[] var10 = var7.toByteArray();
         this.writeUint64(var1, (long)var10.length);
         var1.write(var10);
      }

   }

   private void writeUint64(DataOutput var1, long var2) throws IOException {
      int var4 = 0;
      int var5 = 128;

      int var6;
      for(var6 = 0; var6 < 8; ++var6) {
         if (var2 < 1L << 7 * (var6 + 1)) {
            var4 = (int)((long)var4 | var2 >>> 8 * var6);
            break;
         }

         var4 |= var5;
         var5 >>>= 1;
      }

      var1.write(var4);

      while(var6 > 0) {
         var1.write((int)(255L & var2));
         var2 >>>= 8;
         --var6;
      }

   }

   private void writeBits(DataOutput var1, BitSet var2, int var3) throws IOException {
      int var4 = 0;
      int var5 = 7;

      for(int var6 = 0; var6 < var3; ++var6) {
         var4 |= (var2.get(var6) ? 1 : 0) << var5;
         --var5;
         if (var5 < 0) {
            var1.write(var4);
            var5 = 7;
            var4 = 0;
         }
      }

      if (var5 != 7) {
         var1.write(var4);
      }

   }

   private static Iterable reverse(Iterable var0) {
      LinkedList var1 = new LinkedList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.addFirst(var3);
      }

      return var1;
   }

   static CRC32 access$100(SevenZOutputFile var0) {
      return var0.crc32;
   }

   static RandomAccessFile access$200(SevenZOutputFile var0) {
      return var0.file;
   }

   static CRC32 access$300(SevenZOutputFile var0) {
      return var0.compressedCrc32;
   }

   static long access$408(SevenZOutputFile var0) {
      return (long)(var0.fileBytesWritten++);
   }

   static long access$414(SevenZOutputFile var0, long var1) {
      return var0.fileBytesWritten += var1;
   }

   private class OutputStreamWrapper extends OutputStream {
      final SevenZOutputFile this$0;

      private OutputStreamWrapper(SevenZOutputFile var1) {
         this.this$0 = var1;
      }

      public void write(int var1) throws IOException {
         SevenZOutputFile.access$200(this.this$0).write(var1);
         SevenZOutputFile.access$300(this.this$0).update(var1);
         SevenZOutputFile.access$408(this.this$0);
      }

      public void write(byte[] var1) throws IOException {
         this.write(var1, 0, var1.length);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         SevenZOutputFile.access$200(this.this$0).write(var1, var2, var3);
         SevenZOutputFile.access$300(this.this$0).update(var1, var2, var3);
         SevenZOutputFile.access$414(this.this$0, (long)var3);
      }

      public void flush() throws IOException {
      }

      public void close() throws IOException {
      }

      OutputStreamWrapper(SevenZOutputFile var1, Object var2) {
         this(var1);
      }
   }
}
