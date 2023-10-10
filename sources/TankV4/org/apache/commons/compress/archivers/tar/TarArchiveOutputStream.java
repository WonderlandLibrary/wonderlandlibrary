package org.apache.commons.compress.archivers.tar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.CountingOutputStream;

public class TarArchiveOutputStream extends ArchiveOutputStream {
   public static final int LONGFILE_ERROR = 0;
   public static final int LONGFILE_TRUNCATE = 1;
   public static final int LONGFILE_GNU = 2;
   public static final int LONGFILE_POSIX = 3;
   public static final int BIGNUMBER_ERROR = 0;
   public static final int BIGNUMBER_STAR = 1;
   public static final int BIGNUMBER_POSIX = 2;
   private long currSize;
   private String currName;
   private long currBytes;
   private final byte[] recordBuf;
   private int assemLen;
   private final byte[] assemBuf;
   private int longFileMode;
   private int bigNumberMode;
   private int recordsWritten;
   private final int recordsPerBlock;
   private final int recordSize;
   private boolean closed;
   private boolean haveUnclosedEntry;
   private boolean finished;
   private final OutputStream out;
   private final ZipEncoding encoding;
   private boolean addPaxHeadersForNonAsciiNames;
   private static final ZipEncoding ASCII = ZipEncodingHelper.getZipEncoding("ASCII");

   public TarArchiveOutputStream(OutputStream var1) {
      this(var1, 10240, 512);
   }

   public TarArchiveOutputStream(OutputStream var1, String var2) {
      this(var1, 10240, 512, var2);
   }

   public TarArchiveOutputStream(OutputStream var1, int var2) {
      this(var1, var2, 512);
   }

   public TarArchiveOutputStream(OutputStream var1, int var2, String var3) {
      this(var1, var2, 512, var3);
   }

   public TarArchiveOutputStream(OutputStream var1, int var2, int var3) {
      this(var1, var2, var3, (String)null);
   }

   public TarArchiveOutputStream(OutputStream var1, int var2, int var3, String var4) {
      this.longFileMode = 0;
      this.bigNumberMode = 0;
      this.closed = false;
      this.haveUnclosedEntry = false;
      this.finished = false;
      this.addPaxHeadersForNonAsciiNames = false;
      this.out = new CountingOutputStream(var1);
      this.encoding = ZipEncodingHelper.getZipEncoding(var4);
      this.assemLen = 0;
      this.assemBuf = new byte[var3];
      this.recordBuf = new byte[var3];
      this.recordSize = var3;
      this.recordsPerBlock = var2 / var3;
   }

   public void setLongFileMode(int var1) {
      this.longFileMode = var1;
   }

   public void setBigNumberMode(int var1) {
      this.bigNumberMode = var1;
   }

   public void setAddPaxHeadersForNonAsciiNames(boolean var1) {
      this.addPaxHeadersForNonAsciiNames = var1;
   }

   /** @deprecated */
   @Deprecated
   public int getCount() {
      return (int)this.getBytesWritten();
   }

   public long getBytesWritten() {
      return ((CountingOutputStream)this.out).getBytesWritten();
   }

   public void finish() throws IOException {
      if (this.finished) {
         throw new IOException("This archive has already been finished");
      } else if (this.haveUnclosedEntry) {
         throw new IOException("This archives contains unclosed entries.");
      } else {
         this.writeEOFRecord();
         this.writeEOFRecord();
         this.padAsNeeded();
         this.out.flush();
         this.finished = true;
      }
   }

   public void close() throws IOException {
      if (!this.finished) {
         this.finish();
      }

      if (!this.closed) {
         this.out.close();
         this.closed = true;
      }

   }

   public int getRecordSize() {
      return this.recordSize;
   }

   public void putArchiveEntry(ArchiveEntry var1) throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         TarArchiveOutputStream var10000;
         TarArchiveEntry var2;
         HashMap var3;
         String var4;
         boolean var5;
         String var6;
         label60: {
            var2 = (TarArchiveEntry)var1;
            var3 = new HashMap();
            var4 = var2.getName();
            var5 = this.handleLongName(var4, var3, "path", (byte)76, "file name");
            var6 = var2.getLinkName();
            if (var6 != null && var6.length() > 0) {
               var10000 = this;
               String var10003 = "linkpath";
               if (75 >= "link name") {
                  boolean var10004 = true;
                  break label60;
               }
            }

            var10000 = false;
         }

         TarArchiveOutputStream var7 = var10000;
         if (this.bigNumberMode == 2) {
            this.addPaxHeadersForBigNumbers(var3, var2);
         } else if (this.bigNumberMode != 1) {
            this.failForBigNumbers(var2);
         }

         if (this.addPaxHeadersForNonAsciiNames && !var5 && !ASCII.canEncode(var4)) {
            var3.put("path", var4);
         }

         if (this.addPaxHeadersForNonAsciiNames && var7 == false && (var2.isLink() || var2.isSymbolicLink()) && !ASCII.canEncode(var6)) {
            var3.put("linkpath", var6);
         }

         if (var3.size() > 0) {
            this.writePaxHeaders(var4, var3);
         }

         var2.writeEntryHeader(this.recordBuf, this.encoding, this.bigNumberMode == 1);
         this.writeRecord(this.recordBuf);
         this.currBytes = 0L;
         if (var2.isDirectory()) {
            this.currSize = 0L;
         } else {
            this.currSize = var2.getSize();
         }

         this.currName = var4;
         this.haveUnclosedEntry = true;
      }
   }

   public void closeArchiveEntry() throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else if (!this.haveUnclosedEntry) {
         throw new IOException("No current entry to close");
      } else {
         if (this.assemLen > 0) {
            for(int var1 = this.assemLen; var1 < this.assemBuf.length; ++var1) {
               this.assemBuf[var1] = 0;
            }

            this.writeRecord(this.assemBuf);
            this.currBytes += (long)this.assemLen;
            this.assemLen = 0;
         }

         if (this.currBytes < this.currSize) {
            throw new IOException("entry '" + this.currName + "' closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
         } else {
            this.haveUnclosedEntry = false;
         }
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (!this.haveUnclosedEntry) {
         throw new IllegalStateException("No current tar entry");
      } else if (this.currBytes + (long)var3 > this.currSize) {
         throw new IOException("request to write '" + var3 + "' bytes exceeds size in header of '" + this.currSize + "' bytes for entry '" + this.currName + "'");
      } else {
         int var4;
         if (this.assemLen > 0) {
            if (this.assemLen + var3 >= this.recordBuf.length) {
               var4 = this.recordBuf.length - this.assemLen;
               System.arraycopy(this.assemBuf, 0, this.recordBuf, 0, this.assemLen);
               System.arraycopy(var1, var2, this.recordBuf, this.assemLen, var4);
               this.writeRecord(this.recordBuf);
               this.currBytes += (long)this.recordBuf.length;
               var2 += var4;
               var3 -= var4;
               this.assemLen = 0;
            } else {
               System.arraycopy(var1, var2, this.assemBuf, this.assemLen, var3);
               var2 += var3;
               this.assemLen += var3;
               var3 = 0;
            }
         }

         while(var3 > 0) {
            if (var3 < this.recordBuf.length) {
               System.arraycopy(var1, var2, this.assemBuf, this.assemLen, var3);
               this.assemLen += var3;
               break;
            }

            this.writeRecord(var1, var2);
            var4 = this.recordBuf.length;
            this.currBytes += (long)var4;
            var3 -= var4;
            var2 += var4;
         }

      }
   }

   void writePaxHeaders(String var1, Map var2) throws IOException {
      String var3 = "./PaxHeaders.X/" + this.stripTo7Bits(var1);
      if (var3.length() >= 100) {
         var3 = var3.substring(0, 99);
      }

      TarArchiveEntry var4 = new TarArchiveEntry(var3, (byte)120);
      StringWriter var5 = new StringWriter();
      Iterator var6 = var2.entrySet().iterator();

      while(var6.hasNext()) {
         Entry var7 = (Entry)var6.next();
         String var8 = (String)var7.getKey();
         String var9 = (String)var7.getValue();
         int var10 = var8.length() + var9.length() + 3 + 2;
         String var11 = var10 + " " + var8 + "=" + var9 + "\n";

         for(int var12 = var11.getBytes("UTF-8").length; var10 != var12; var12 = var11.getBytes("UTF-8").length) {
            var10 = var12;
            var11 = var12 + " " + var8 + "=" + var9 + "\n";
         }

         var5.write(var11);
      }

      byte[] var13 = var5.toString().getBytes("UTF-8");
      var4.setSize((long)var13.length);
      this.putArchiveEntry(var4);
      this.write(var13);
      this.closeArchiveEntry();
   }

   private String stripTo7Bits(String var1) {
      int var2 = var1.length();
      StringBuilder var3 = new StringBuilder(var2);

      for(int var4 = 0; var4 < var2; ++var4) {
         char var5 = (char)(var1.charAt(var4) & 127);
         if (var5 != 0) {
            var3.append("_");
         } else {
            var3.append(var5);
         }
      }

      return var3.toString();
   }

   private void writeEOFRecord() throws IOException {
      Arrays.fill(this.recordBuf, (byte)0);
      this.writeRecord(this.recordBuf);
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public ArchiveEntry createArchiveEntry(File var1, String var2) throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         return new TarArchiveEntry(var1, var2);
      }
   }

   private void writeRecord(byte[] var1) throws IOException {
      if (var1.length != this.recordSize) {
         throw new IOException("record to write has length '" + var1.length + "' which is not the record size of '" + this.recordSize + "'");
      } else {
         this.out.write(var1);
         ++this.recordsWritten;
      }
   }

   private void writeRecord(byte[] var1, int var2) throws IOException {
      if (var2 + this.recordSize > var1.length) {
         throw new IOException("record has length '" + var1.length + "' with offset '" + var2 + "' which is less than the record size of '" + this.recordSize + "'");
      } else {
         this.out.write(var1, var2, this.recordSize);
         ++this.recordsWritten;
      }
   }

   private void padAsNeeded() throws IOException {
      int var1 = this.recordsWritten % this.recordsPerBlock;
      if (var1 != 0) {
         for(int var2 = var1; var2 < this.recordsPerBlock; ++var2) {
            this.writeEOFRecord();
         }
      }

   }

   private void addPaxHeadersForBigNumbers(Map var1, TarArchiveEntry var2) {
      this.addPaxHeaderForBigNumber(var1, "size", var2.getSize(), 8589934591L);
      this.addPaxHeaderForBigNumber(var1, "gid", (long)var2.getGroupId(), 2097151L);
      this.addPaxHeaderForBigNumber(var1, "mtime", var2.getModTime().getTime() / 1000L, 8589934591L);
      this.addPaxHeaderForBigNumber(var1, "uid", (long)var2.getUserId(), 2097151L);
      this.addPaxHeaderForBigNumber(var1, "SCHILY.devmajor", (long)var2.getDevMajor(), 2097151L);
      this.addPaxHeaderForBigNumber(var1, "SCHILY.devminor", (long)var2.getDevMinor(), 2097151L);
      this.failForBigNumber("mode", (long)var2.getMode(), 2097151L);
   }

   private void addPaxHeaderForBigNumber(Map var1, String var2, long var3, long var5) {
      if (var3 < 0L || var3 > var5) {
         var1.put(var2, String.valueOf(var3));
      }

   }

   private void failForBigNumbers(TarArchiveEntry var1) {
      this.failForBigNumber("entry size", var1.getSize(), 8589934591L);
      this.failForBigNumber("group id", (long)var1.getGroupId(), 2097151L);
      this.failForBigNumber("last modification time", var1.getModTime().getTime() / 1000L, 8589934591L);
      this.failForBigNumber("user id", (long)var1.getUserId(), 2097151L);
      this.failForBigNumber("mode", (long)var1.getMode(), 2097151L);
      this.failForBigNumber("major device number", (long)var1.getDevMajor(), 2097151L);
      this.failForBigNumber("minor device number", (long)var1.getDevMinor(), 2097151L);
   }

   private void failForBigNumber(String var1, long var2, long var4) {
      if (var2 < 0L || var2 > var4) {
         throw new RuntimeException(var1 + " '" + var2 + "' is too big ( > " + var4 + " )");
      }
   }
}
