package org.apache.commons.compress.archivers.dump;

import java.io.IOException;
import java.util.Date;
import org.apache.commons.compress.archivers.zip.ZipEncoding;

public class DumpArchiveSummary {
   private long dumpDate;
   private long previousDumpDate;
   private int volume;
   private String label;
   private int level;
   private String filesys;
   private String devname;
   private String hostname;
   private int flags;
   private int firstrec;
   private int ntrec;

   DumpArchiveSummary(byte[] var1, ZipEncoding var2) throws IOException {
      this.dumpDate = 1000L * (long)DumpArchiveUtil.convert32(var1, 4);
      this.previousDumpDate = 1000L * (long)DumpArchiveUtil.convert32(var1, 8);
      this.volume = DumpArchiveUtil.convert32(var1, 12);
      this.label = DumpArchiveUtil.decode(var2, var1, 676, 16).trim();
      this.level = DumpArchiveUtil.convert32(var1, 692);
      this.filesys = DumpArchiveUtil.decode(var2, var1, 696, 64).trim();
      this.devname = DumpArchiveUtil.decode(var2, var1, 760, 64).trim();
      this.hostname = DumpArchiveUtil.decode(var2, var1, 824, 64).trim();
      this.flags = DumpArchiveUtil.convert32(var1, 888);
      this.firstrec = DumpArchiveUtil.convert32(var1, 892);
      this.ntrec = DumpArchiveUtil.convert32(var1, 896);
   }

   public Date getDumpDate() {
      return new Date(this.dumpDate);
   }

   public void setDumpDate(Date var1) {
      this.dumpDate = var1.getTime();
   }

   public Date getPreviousDumpDate() {
      return new Date(this.previousDumpDate);
   }

   public void setPreviousDumpDate(Date var1) {
      this.previousDumpDate = var1.getTime();
   }

   public int getVolume() {
      return this.volume;
   }

   public void setVolume(int var1) {
      this.volume = var1;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int var1) {
      this.level = var1;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public String getFilesystem() {
      return this.filesys;
   }

   public void setFilesystem(String var1) {
      this.filesys = var1;
   }

   public String getDevname() {
      return this.devname;
   }

   public void setDevname(String var1) {
      this.devname = var1;
   }

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(String var1) {
      this.hostname = var1;
   }

   public int getFlags() {
      return this.flags;
   }

   public void setFlags(int var1) {
      this.flags = var1;
   }

   public int getFirstRecord() {
      return this.firstrec;
   }

   public void setFirstRecord(int var1) {
      this.firstrec = var1;
   }

   public int getNTRec() {
      return this.ntrec;
   }

   public void setNTRec(int var1) {
      this.ntrec = var1;
   }

   public boolean isNewHeader() {
      return (this.flags & 1) == 1;
   }

   public boolean isNewInode() {
      return (this.flags & 2) == 2;
   }

   public boolean isCompressed() {
      return (this.flags & 128) == 128;
   }

   public boolean isMetaDataOnly() {
      return (this.flags & 256) == 256;
   }

   public boolean isExtendedAttributes() {
      return (this.flags & '耀') == 32768;
   }

   public int hashCode() {
      int var1 = 17;
      if (this.label != null) {
         var1 = this.label.hashCode();
      }

      var1 = (int)((long)var1 + 31L * this.dumpDate);
      if (this.hostname != null) {
         var1 = 31 * this.hostname.hashCode() + 17;
      }

      if (this.devname != null) {
         var1 = 31 * this.devname.hashCode() + 17;
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && var1.getClass().equals(this.getClass())) {
         DumpArchiveSummary var2 = (DumpArchiveSummary)var1;
         if (this.dumpDate != var2.dumpDate) {
            return false;
         } else if (this.getHostname() != null && this.getHostname().equals(var2.getHostname())) {
            return this.getDevname() != null && this.getDevname().equals(var2.getDevname());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
