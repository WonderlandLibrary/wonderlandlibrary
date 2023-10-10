package org.apache.commons.compress.archivers.sevenz;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimeZone;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class SevenZArchiveEntry implements ArchiveEntry {
   private String name;
   private boolean hasStream;
   private boolean isDirectory;
   private boolean isAntiItem;
   private boolean hasCreationDate;
   private boolean hasLastModifiedDate;
   private boolean hasAccessDate;
   private long creationDate;
   private long lastModifiedDate;
   private long accessDate;
   private boolean hasWindowsAttributes;
   private int windowsAttributes;
   private boolean hasCrc;
   private long crc;
   private long compressedCrc;
   private long size;
   private long compressedSize;
   private Iterable contentMethods;

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public boolean hasStream() {
      return this.hasStream;
   }

   public void setHasStream(boolean var1) {
      this.hasStream = var1;
   }

   public boolean isDirectory() {
      return this.isDirectory;
   }

   public void setDirectory(boolean var1) {
      this.isDirectory = var1;
   }

   public boolean isAntiItem() {
      return this.isAntiItem;
   }

   public void setAntiItem(boolean var1) {
      this.isAntiItem = var1;
   }

   public boolean getHasCreationDate() {
      return this.hasCreationDate;
   }

   public void setHasCreationDate(boolean var1) {
      this.hasCreationDate = var1;
   }

   public Date getCreationDate() {
      if (this.hasCreationDate) {
         return ntfsTimeToJavaTime(this.creationDate);
      } else {
         throw new UnsupportedOperationException("The entry doesn't have this timestamp");
      }
   }

   public void setCreationDate(long var1) {
      this.creationDate = var1;
   }

   public void setCreationDate(Date var1) {
      this.hasCreationDate = var1 != null;
      if (this.hasCreationDate) {
         this.creationDate = javaTimeToNtfsTime(var1);
      }

   }

   public boolean getHasLastModifiedDate() {
      return this.hasLastModifiedDate;
   }

   public void setHasLastModifiedDate(boolean var1) {
      this.hasLastModifiedDate = var1;
   }

   public Date getLastModifiedDate() {
      if (this.hasLastModifiedDate) {
         return ntfsTimeToJavaTime(this.lastModifiedDate);
      } else {
         throw new UnsupportedOperationException("The entry doesn't have this timestamp");
      }
   }

   public void setLastModifiedDate(long var1) {
      this.lastModifiedDate = var1;
   }

   public void setLastModifiedDate(Date var1) {
      this.hasLastModifiedDate = var1 != null;
      if (this.hasLastModifiedDate) {
         this.lastModifiedDate = javaTimeToNtfsTime(var1);
      }

   }

   public boolean getHasAccessDate() {
      return this.hasAccessDate;
   }

   public void setHasAccessDate(boolean var1) {
      this.hasAccessDate = var1;
   }

   public Date getAccessDate() {
      if (this.hasAccessDate) {
         return ntfsTimeToJavaTime(this.accessDate);
      } else {
         throw new UnsupportedOperationException("The entry doesn't have this timestamp");
      }
   }

   public void setAccessDate(long var1) {
      this.accessDate = var1;
   }

   public void setAccessDate(Date var1) {
      this.hasAccessDate = var1 != null;
      if (this.hasAccessDate) {
         this.accessDate = javaTimeToNtfsTime(var1);
      }

   }

   public boolean getHasWindowsAttributes() {
      return this.hasWindowsAttributes;
   }

   public void setHasWindowsAttributes(boolean var1) {
      this.hasWindowsAttributes = var1;
   }

   public int getWindowsAttributes() {
      return this.windowsAttributes;
   }

   public void setWindowsAttributes(int var1) {
      this.windowsAttributes = var1;
   }

   public boolean getHasCrc() {
      return this.hasCrc;
   }

   public void setHasCrc(boolean var1) {
      this.hasCrc = var1;
   }

   /** @deprecated */
   @Deprecated
   public int getCrc() {
      return (int)this.crc;
   }

   /** @deprecated */
   @Deprecated
   public void setCrc(int var1) {
      this.crc = (long)var1;
   }

   public long getCrcValue() {
      return this.crc;
   }

   public void setCrcValue(long var1) {
      this.crc = var1;
   }

   /** @deprecated */
   @Deprecated
   int getCompressedCrc() {
      return (int)this.compressedCrc;
   }

   /** @deprecated */
   @Deprecated
   void setCompressedCrc(int var1) {
      this.compressedCrc = (long)var1;
   }

   long getCompressedCrcValue() {
      return this.compressedCrc;
   }

   void setCompressedCrcValue(long var1) {
      this.compressedCrc = var1;
   }

   public long getSize() {
      return this.size;
   }

   public void setSize(long var1) {
      this.size = var1;
   }

   long getCompressedSize() {
      return this.compressedSize;
   }

   void setCompressedSize(long var1) {
      this.compressedSize = var1;
   }

   public void setContentMethods(Iterable var1) {
      if (var1 != null) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            SevenZMethodConfiguration var4 = (SevenZMethodConfiguration)var3.next();
            var2.addLast(var4);
         }

         this.contentMethods = Collections.unmodifiableList(var2);
      } else {
         this.contentMethods = null;
      }

   }

   public Iterable getContentMethods() {
      return this.contentMethods;
   }

   public static Date ntfsTimeToJavaTime(long var0) {
      Calendar var2 = Calendar.getInstance();
      var2.setTimeZone(TimeZone.getTimeZone("GMT+0"));
      var2.set(1601, 0, 1, 0, 0, 0);
      var2.set(14, 0);
      long var3 = var2.getTimeInMillis() + var0 / 10000L;
      return new Date(var3);
   }

   public static long javaTimeToNtfsTime(Date var0) {
      Calendar var1 = Calendar.getInstance();
      var1.setTimeZone(TimeZone.getTimeZone("GMT+0"));
      var1.set(1601, 0, 1, 0, 0, 0);
      var1.set(14, 0);
      return (var0.getTime() - var1.getTimeInMillis()) * 1000L * 10L;
   }
}
