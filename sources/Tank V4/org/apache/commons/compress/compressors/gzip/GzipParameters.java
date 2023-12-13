package org.apache.commons.compress.compressors.gzip;

public class GzipParameters {
   private int compressionLevel = -1;
   private long modificationTime;
   private String filename;
   private String comment;
   private int operatingSystem = 255;

   public int getCompressionLevel() {
      return this.compressionLevel;
   }

   public void setCompressionLevel(int var1) {
      if (var1 >= -1 && var1 <= 9) {
         this.compressionLevel = var1;
      } else {
         throw new IllegalArgumentException("Invalid gzip compression level: " + var1);
      }
   }

   public long getModificationTime() {
      return this.modificationTime;
   }

   public void setModificationTime(long var1) {
      this.modificationTime = var1;
   }

   public String getFilename() {
      return this.filename;
   }

   public void setFilename(String var1) {
      this.filename = var1;
   }

   public String getComment() {
      return this.comment;
   }

   public void setComment(String var1) {
      this.comment = var1;
   }

   public int getOperatingSystem() {
      return this.operatingSystem;
   }

   public void setOperatingSystem(int var1) {
      this.operatingSystem = var1;
   }
}
