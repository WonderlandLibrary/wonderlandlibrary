package com.sun.webkit.network;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

abstract class FormDataElement {
   private InputStream inputStream;

   void open() throws IOException {
      this.inputStream = this.createInputStream();
   }

   long getSize() {
      if (this.inputStream == null) {
         throw new IllegalStateException();
      } else {
         return this.doGetSize();
      }
   }

   InputStream getInputStream() {
      if (this.inputStream == null) {
         throw new IllegalStateException();
      } else {
         return this.inputStream;
      }
   }

   void close() throws IOException {
      if (this.inputStream != null) {
         this.inputStream.close();
         this.inputStream = null;
      }

   }

   protected abstract InputStream createInputStream() throws IOException;

   protected abstract long doGetSize();

   private static FormDataElement fwkCreateFromByteArray(byte[] var0) {
      return new ByteArrayElement(var0);
   }

   private static FormDataElement fwkCreateFromFile(String var0) {
      return new FileElement(var0);
   }

   private static final class FileElement extends FormDataElement {
      private final File file;

      private FileElement(String var1) {
         this.file = new File(var1);
      }

      protected InputStream createInputStream() throws IOException {
         return new BufferedInputStream(new FileInputStream(this.file));
      }

      protected long doGetSize() {
         return this.file.length();
      }

      // $FF: synthetic method
      FileElement(String var1, Object var2) {
         this(var1);
      }
   }

   private static final class ByteArrayElement extends FormDataElement {
      private final byte[] byteArray;

      private ByteArrayElement(byte[] var1) {
         this.byteArray = var1;
      }

      protected InputStream createInputStream() {
         return new ByteArrayInputStream(this.byteArray);
      }

      protected long doGetSize() {
         return (long)this.byteArray.length;
      }

      // $FF: synthetic method
      ByteArrayElement(byte[] var1, Object var2) {
         this(var1);
      }
   }
}
