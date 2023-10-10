package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

public class UnsupportedZipFeatureException extends ZipException {
   private final UnsupportedZipFeatureException.Feature reason;
   private final ZipArchiveEntry entry;
   private static final long serialVersionUID = 20130101L;

   public UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature var1, ZipArchiveEntry var2) {
      super("unsupported feature " + var1 + " used in entry " + var2.getName());
      this.reason = var1;
      this.entry = var2;
   }

   public UnsupportedZipFeatureException(ZipMethod var1, ZipArchiveEntry var2) {
      super("unsupported feature method '" + var1.name() + "' used in entry " + var2.getName());
      this.reason = UnsupportedZipFeatureException.Feature.METHOD;
      this.entry = var2;
   }

   public UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature var1) {
      super("unsupported feature " + var1 + " used in archive.");
      this.reason = var1;
      this.entry = null;
   }

   public UnsupportedZipFeatureException.Feature getFeature() {
      return this.reason;
   }

   public ZipArchiveEntry getEntry() {
      return this.entry;
   }

   public static class Feature {
      public static final UnsupportedZipFeatureException.Feature ENCRYPTION = new UnsupportedZipFeatureException.Feature("encryption");
      public static final UnsupportedZipFeatureException.Feature METHOD = new UnsupportedZipFeatureException.Feature("compression method");
      public static final UnsupportedZipFeatureException.Feature DATA_DESCRIPTOR = new UnsupportedZipFeatureException.Feature("data descriptor");
      public static final UnsupportedZipFeatureException.Feature SPLITTING = new UnsupportedZipFeatureException.Feature("splitting");
      private final String name;

      private Feature(String var1) {
         this.name = var1;
      }

      public String toString() {
         return this.name;
      }
   }
}
