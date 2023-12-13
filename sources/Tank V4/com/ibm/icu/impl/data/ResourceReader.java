package com.ibm.icu.impl.data;

import com.ibm.icu.impl.ICUData;
import com.ibm.icu.impl.PatternProps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ResourceReader {
   private BufferedReader reader;
   private String resourceName;
   private String encoding;
   private Class root;
   private int lineNo;

   public ResourceReader(String var1, String var2) throws UnsupportedEncodingException {
      this(ICUData.class, "data/" + var1, var2);
   }

   public ResourceReader(String var1) {
      this(ICUData.class, "data/" + var1);
   }

   public ResourceReader(Class var1, String var2, String var3) throws UnsupportedEncodingException {
      this.root = var1;
      this.resourceName = var2;
      this.encoding = var3;
      this.lineNo = -1;
      this._reset();
   }

   public ResourceReader(InputStream var1, String var2, String var3) {
      this.root = null;
      this.resourceName = var2;
      this.encoding = var3;
      this.lineNo = -1;

      try {
         InputStreamReader var4 = var3 == null ? new InputStreamReader(var1) : new InputStreamReader(var1, var3);
         this.reader = new BufferedReader(var4);
         this.lineNo = 0;
      } catch (UnsupportedEncodingException var5) {
      }

   }

   public ResourceReader(InputStream var1, String var2) {
      this((InputStream)var1, var2, (String)null);
   }

   public ResourceReader(Class var1, String var2) {
      this.root = var1;
      this.resourceName = var2;
      this.encoding = null;
      this.lineNo = -1;

      try {
         this._reset();
      } catch (UnsupportedEncodingException var4) {
      }

   }

   public String readLine() throws IOException {
      if (this.lineNo != 0) {
         ++this.lineNo;
         return this.reader.readLine();
      } else {
         ++this.lineNo;
         String var1 = this.reader.readLine();
         if (var1.charAt(0) == '\uffef' || var1.charAt(0) == '\ufeff') {
            var1 = var1.substring(1);
         }

         return var1;
      }
   }

   public String readLineSkippingComments(boolean var1) throws IOException {
      String var2;
      int var3;
      do {
         var2 = this.readLine();
         if (var2 == null) {
            return var2;
         }

         var3 = PatternProps.skipWhiteSpace(var2, 0);
      } while(var3 == var2.length() || var2.charAt(var3) == '#');

      if (var1) {
         var2 = var2.substring(var3);
      }

      return var2;
   }

   public String readLineSkippingComments() throws IOException {
      return this.readLineSkippingComments(false);
   }

   public int getLineNumber() {
      return this.lineNo;
   }

   public String describePosition() {
      return this.resourceName + ':' + this.lineNo;
   }

   public void reset() {
      try {
         this._reset();
      } catch (UnsupportedEncodingException var2) {
      }

   }

   private void _reset() throws UnsupportedEncodingException {
      if (this.lineNo != 0) {
         InputStream var1 = ICUData.getStream(this.root, this.resourceName);
         if (var1 == null) {
            throw new IllegalArgumentException("Can't open " + this.resourceName);
         } else {
            InputStreamReader var2 = this.encoding == null ? new InputStreamReader(var1) : new InputStreamReader(var1, this.encoding);
            this.reader = new BufferedReader(var2);
            this.lineNo = 0;
         }
      }
   }
}
