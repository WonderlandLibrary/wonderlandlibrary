package org.apache.commons.compress.archivers.dump;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Map.Entry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;

public class DumpArchiveInputStream extends ArchiveInputStream {
   private DumpArchiveSummary summary;
   private DumpArchiveEntry active;
   private boolean isClosed;
   private boolean hasHitEOF;
   private long entrySize;
   private long entryOffset;
   private int readIdx;
   private final byte[] readBuf;
   private byte[] blockBuffer;
   private int recordOffset;
   private long filepos;
   protected TapeInputStream raw;
   private final Map names;
   private final Map pending;
   private Queue queue;
   private final ZipEncoding encoding;

   public DumpArchiveInputStream(InputStream var1) throws ArchiveException {
      this(var1, (String)null);
   }

   public DumpArchiveInputStream(InputStream var1, String var2) throws ArchiveException {
      this.readBuf = new byte[1024];
      this.names = new HashMap();
      this.pending = new HashMap();
      this.raw = new TapeInputStream(var1);
      this.hasHitEOF = false;
      this.encoding = ZipEncodingHelper.getZipEncoding(var2);

      try {
         byte[] var3 = this.raw.readRecord();
         if (!DumpArchiveUtil.verify(var3)) {
            throw new UnrecognizedFormatException();
         }

         this.summary = new DumpArchiveSummary(var3, this.encoding);
         this.raw.resetBlockSize(this.summary.getNTRec(), this.summary.isCompressed());
         this.blockBuffer = new byte[4096];
         this.readCLRI();
         this.readBITS();
      } catch (IOException var4) {
         throw new ArchiveException(var4.getMessage(), var4);
      }

      Dirent var5 = new Dirent(2, 2, 4, ".");
      this.names.put(2, var5);
      this.queue = new PriorityQueue(10, new Comparator(this) {
         final DumpArchiveInputStream this$0;

         {
            this.this$0 = var1;
         }

         public int compare(DumpArchiveEntry var1, DumpArchiveEntry var2) {
            return var1.getOriginalName() != null && var2.getOriginalName() != null ? var1.getOriginalName().compareTo(var2.getOriginalName()) : Integer.MAX_VALUE;
         }

         public int compare(Object var1, Object var2) {
            return this.compare((DumpArchiveEntry)var1, (DumpArchiveEntry)var2);
         }
      });
   }

   /** @deprecated */
   @Deprecated
   public int getCount() {
      return (int)this.getBytesRead();
   }

   public long getBytesRead() {
      return this.raw.getBytesRead();
   }

   public DumpArchiveSummary getSummary() {
      return this.summary;
   }

   private void readCLRI() throws IOException {
      byte[] var1 = this.raw.readRecord();
      if (!DumpArchiveUtil.verify(var1)) {
         throw new InvalidFormatException();
      } else {
         this.active = DumpArchiveEntry.parse(var1);
         if (DumpArchiveConstants.SEGMENT_TYPE.CLRI != this.active.getHeaderType()) {
            throw new InvalidFormatException();
         } else if (this.raw.skip((long)(1024 * this.active.getHeaderCount())) == -1L) {
            throw new EOFException();
         } else {
            this.readIdx = this.active.getHeaderCount();
         }
      }
   }

   private void readBITS() throws IOException {
      byte[] var1 = this.raw.readRecord();
      if (!DumpArchiveUtil.verify(var1)) {
         throw new InvalidFormatException();
      } else {
         this.active = DumpArchiveEntry.parse(var1);
         if (DumpArchiveConstants.SEGMENT_TYPE.BITS != this.active.getHeaderType()) {
            throw new InvalidFormatException();
         } else if (this.raw.skip((long)(1024 * this.active.getHeaderCount())) == -1L) {
            throw new EOFException();
         } else {
            this.readIdx = this.active.getHeaderCount();
         }
      }
   }

   public DumpArchiveEntry getNextDumpEntry() throws IOException {
      return this.getNextEntry();
   }

   public DumpArchiveEntry getNextEntry() throws IOException {
      DumpArchiveEntry var1 = null;
      String var2 = null;
      if (!this.queue.isEmpty()) {
         return (DumpArchiveEntry)this.queue.remove();
      } else {
         while(var1 == null) {
            if (this.hasHitEOF) {
               return null;
            }

            while(this.readIdx < this.active.getHeaderCount()) {
               if (!this.active.isSparseRecord(this.readIdx++) && this.raw.skip(1024L) == -1L) {
                  throw new EOFException();
               }
            }

            this.readIdx = 0;
            this.filepos = this.raw.getBytesRead();
            byte[] var3 = this.raw.readRecord();
            if (!DumpArchiveUtil.verify(var3)) {
               throw new InvalidFormatException();
            }

            for(this.active = DumpArchiveEntry.parse(var3); DumpArchiveConstants.SEGMENT_TYPE.ADDR == this.active.getHeaderType(); this.active = DumpArchiveEntry.parse(var3)) {
               if (this.raw.skip((long)(1024 * (this.active.getHeaderCount() - this.active.getHeaderHoles()))) == -1L) {
                  throw new EOFException();
               }

               this.filepos = this.raw.getBytesRead();
               var3 = this.raw.readRecord();
               if (!DumpArchiveUtil.verify(var3)) {
                  throw new InvalidFormatException();
               }
            }

            if (DumpArchiveConstants.SEGMENT_TYPE.END == this.active.getHeaderType()) {
               this.hasHitEOF = true;
               return null;
            }

            var1 = this.active;
            if (var1.isDirectory()) {
               this.readDirectoryEntry(this.active);
               this.entryOffset = 0L;
               this.entrySize = 0L;
               this.readIdx = this.active.getHeaderCount();
            } else {
               this.entryOffset = 0L;
               this.entrySize = this.active.getEntrySize();
               this.readIdx = 0;
            }

            this.recordOffset = this.readBuf.length;
            var2 = this.getPath(var1);
            if (var2 == null) {
               var1 = null;
            }
         }

         var1.setName(var2);
         var1.setSimpleName(((Dirent)this.names.get(var1.getIno())).getName());
         var1.setOffset(this.filepos);
         return var1;
      }
   }

   private void readDirectoryEntry(DumpArchiveEntry var1) throws IOException {
      long var2 = var1.getEntrySize();

      for(boolean var4 = true; var4 || DumpArchiveConstants.SEGMENT_TYPE.ADDR == var1.getHeaderType(); var2 -= 1024L) {
         if (!var4) {
            this.raw.readRecord();
         }

         if (!this.names.containsKey(var1.getIno()) && DumpArchiveConstants.SEGMENT_TYPE.INODE == var1.getHeaderType()) {
            this.pending.put(var1.getIno(), var1);
         }

         int var5 = 1024 * var1.getHeaderCount();
         if (this.blockBuffer.length < var5) {
            this.blockBuffer = new byte[var5];
         }

         if (this.raw.read(this.blockBuffer, 0, var5) != var5) {
            throw new EOFException();
         }

         boolean var6 = false;

         int var15;
         for(int var7 = 0; var7 < var5 - 8 && (long)var7 < var2 - 8L; var7 += var15) {
            int var8 = DumpArchiveUtil.convert32(this.blockBuffer, var7);
            var15 = DumpArchiveUtil.convert16(this.blockBuffer, var7 + 4);
            byte var9 = this.blockBuffer[var7 + 6];
            String var10 = DumpArchiveUtil.decode(this.encoding, this.blockBuffer, var7 + 8, this.blockBuffer[var7 + 7]);
            if (!".".equals(var10) && !"..".equals(var10)) {
               Dirent var11 = new Dirent(var8, var1.getIno(), var9, var10);
               this.names.put(var8, var11);
               Iterator var12 = this.pending.entrySet().iterator();

               while(var12.hasNext()) {
                  Entry var13 = (Entry)var12.next();
                  String var14 = this.getPath((DumpArchiveEntry)var13.getValue());
                  if (var14 != null) {
                     ((DumpArchiveEntry)var13.getValue()).setName(var14);
                     ((DumpArchiveEntry)var13.getValue()).setSimpleName(((Dirent)this.names.get(var13.getKey())).getName());
                     this.queue.add(var13.getValue());
                  }
               }

               var12 = this.queue.iterator();

               while(var12.hasNext()) {
                  DumpArchiveEntry var17 = (DumpArchiveEntry)var12.next();
                  this.pending.remove(var17.getIno());
               }
            }
         }

         byte[] var16 = this.raw.peek();
         if (!DumpArchiveUtil.verify(var16)) {
            throw new InvalidFormatException();
         }

         var1 = DumpArchiveEntry.parse(var16);
         var4 = false;
      }

   }

   private String getPath(DumpArchiveEntry var1) {
      Stack var2 = new Stack();
      Dirent var3 = null;
      int var4 = var1.getIno();

      while(true) {
         if (!this.names.containsKey(var4)) {
            var2.clear();
            break;
         }

         var3 = (Dirent)this.names.get(var4);
         var2.push(var3.getName());
         if (var3.getIno() == var3.getParentIno()) {
            break;
         }

         var4 = var3.getParentIno();
      }

      if (var2.isEmpty()) {
         this.pending.put(var1.getIno(), var1);
         return null;
      } else {
         StringBuilder var5 = new StringBuilder((String)var2.pop());

         while(!var2.isEmpty()) {
            var5.append('/');
            var5.append((String)var2.pop());
         }

         return var5.toString();
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = 0;
      if (!this.hasHitEOF && !this.isClosed && this.entryOffset < this.entrySize) {
         if (this.active == null) {
            throw new IllegalStateException("No current dump entry");
         } else {
            if ((long)var3 + this.entryOffset > this.entrySize) {
               var3 = (int)(this.entrySize - this.entryOffset);
            }

            while(var3 > 0) {
               int var5 = var3 > this.readBuf.length - this.recordOffset ? this.readBuf.length - this.recordOffset : var3;
               if (this.recordOffset + var5 <= this.readBuf.length) {
                  System.arraycopy(this.readBuf, this.recordOffset, var1, var2, var5);
                  var4 += var5;
                  this.recordOffset += var5;
                  var3 -= var5;
                  var2 += var5;
               }

               if (var3 > 0) {
                  if (this.readIdx >= 512) {
                     byte[] var6 = this.raw.readRecord();
                     if (!DumpArchiveUtil.verify(var6)) {
                        throw new InvalidFormatException();
                     }

                     this.active = DumpArchiveEntry.parse(var6);
                     this.readIdx = 0;
                  }

                  if (!this.active.isSparseRecord(this.readIdx++)) {
                     int var7 = this.raw.read(this.readBuf, 0, this.readBuf.length);
                     if (var7 != this.readBuf.length) {
                        throw new EOFException();
                     }
                  } else {
                     Arrays.fill(this.readBuf, (byte)0);
                  }

                  this.recordOffset = 0;
               }
            }

            this.entryOffset += (long)var4;
            return var4;
         }
      } else {
         return -1;
      }
   }

   public void close() throws IOException {
      if (!this.isClosed) {
         this.isClosed = true;
         this.raw.close();
      }

   }

   public static boolean matches(byte[] var0, int var1) {
      if (var1 < 32) {
         return false;
      } else if (var1 >= 1024) {
         return DumpArchiveUtil.verify(var0);
      } else {
         return 60012 == DumpArchiveUtil.convert32(var0, 24);
      }
   }

   public ArchiveEntry getNextEntry() throws IOException {
      return this.getNextEntry();
   }
}
