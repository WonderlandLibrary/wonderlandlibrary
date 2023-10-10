package org.apache.commons.compress.archivers.ar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;

public class ArArchiveOutputStream extends ArchiveOutputStream {
   public static final int LONGFILE_ERROR = 0;
   public static final int LONGFILE_BSD = 1;
   private final OutputStream out;
   private long entryOffset = 0L;
   private ArArchiveEntry prevEntry;
   private boolean haveUnclosedEntry = false;
   private int longFileMode = 0;
   private boolean finished = false;

   public ArArchiveOutputStream(OutputStream var1) {
      this.out = var1;
   }

   public void setLongFileMode(int var1) {
      this.longFileMode = var1;
   }

   private long writeArchiveHeader() throws IOException {
      byte[] var1 = ArchiveUtils.toAsciiBytes("!<arch>\n");
      this.out.write(var1);
      return (long)var1.length;
   }

   public void closeArchiveEntry() throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else if (this.prevEntry != null && this.haveUnclosedEntry) {
         if (this.entryOffset % 2L != 0L) {
            this.out.write(10);
         }

         this.haveUnclosedEntry = false;
      } else {
         throw new IOException("No current entry to close");
      }
   }

   public void putArchiveEntry(ArchiveEntry var1) throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         ArArchiveEntry var2 = (ArArchiveEntry)var1;
         if (this.prevEntry == null) {
            this.writeArchiveHeader();
         } else {
            if (this.prevEntry.getLength() != this.entryOffset) {
               throw new IOException("length does not match entry (" + this.prevEntry.getLength() + " != " + this.entryOffset);
            }

            if (this.haveUnclosedEntry) {
               this.closeArchiveEntry();
            }
         }

         this.prevEntry = var2;
         this.writeEntryHeader(var2);
         this.entryOffset = 0L;
         this.haveUnclosedEntry = true;
      }
   }

   private long fill(long var1, long var3, char var5) throws IOException {
      long var6 = var3 - var1;
      if (var6 > 0L) {
         for(int var8 = 0; (long)var8 < var6; ++var8) {
            this.write(var5);
         }
      }

      return var3;
   }

   private long write(String var1) throws IOException {
      byte[] var2 = var1.getBytes("ascii");
      this.write(var2);
      return (long)var2.length;
   }

   private long writeEntryHeader(ArArchiveEntry var1) throws IOException {
      long var2 = 0L;
      boolean var4 = false;
      String var5 = var1.getName();
      if (0 == this.longFileMode && var5.length() > 16) {
         throw new IOException("filename too long, > 16 chars: " + var5);
      } else {
         if (1 != this.longFileMode || var5.length() <= 16 && var5.indexOf(" ") <= -1) {
            var2 += this.write(var5);
         } else {
            var4 = true;
            var2 += this.write("#1/" + String.valueOf(var5.length()));
         }

         var2 = this.fill(var2, 16L, ' ');
         String var6 = "" + var1.getLastModified();
         if (var6.length() > 12) {
            throw new IOException("modified too long");
         } else {
            var2 += this.write(var6);
            var2 = this.fill(var2, 28L, ' ');
            String var7 = "" + var1.getUserId();
            if (var7.length() > 6) {
               throw new IOException("userid too long");
            } else {
               var2 += this.write(var7);
               var2 = this.fill(var2, 34L, ' ');
               String var8 = "" + var1.getGroupId();
               if (var8.length() > 6) {
                  throw new IOException("groupid too long");
               } else {
                  var2 += this.write(var8);
                  var2 = this.fill(var2, 40L, ' ');
                  String var9 = "" + Integer.toString(var1.getMode(), 8);
                  if (var9.length() > 8) {
                     throw new IOException("filemode too long");
                  } else {
                     var2 += this.write(var9);
                     var2 = this.fill(var2, 48L, ' ');
                     String var10 = String.valueOf(var1.getLength() + (long)(var4 ? var5.length() : 0));
                     if (var10.length() > 10) {
                        throw new IOException("size too long");
                     } else {
                        var2 += this.write(var10);
                        var2 = this.fill(var2, 58L, ' ');
                        var2 += this.write("`\n");
                        if (var4) {
                           var2 += this.write(var5);
                        }

                        return var2;
                     }
                  }
               }
            }
         }
      }
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      this.out.write(var1, var2, var3);
      this.count(var3);
      this.entryOffset += (long)var3;
   }

   public void close() throws IOException {
      if (!this.finished) {
         this.finish();
      }

      this.out.close();
      this.prevEntry = null;
   }

   public ArchiveEntry createArchiveEntry(File var1, String var2) throws IOException {
      if (this.finished) {
         throw new IOException("Stream has already been finished");
      } else {
         return new ArArchiveEntry(var1, var2);
      }
   }

   public void finish() throws IOException {
      if (this.haveUnclosedEntry) {
         throw new IOException("This archive contains unclosed entries.");
      } else if (this.finished) {
         throw new IOException("This archive has already been finished");
      } else {
         this.finished = true;
      }
   }
}
