package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class CanonicalIterator {
   private static boolean PROGRESS = false;
   private static boolean SKIP_ZEROS = true;
   private final Normalizer2 nfd;
   private final Normalizer2Impl nfcImpl;
   private String source;
   private boolean done;
   private String[][] pieces;
   private int[] current;
   private transient StringBuilder buffer = new StringBuilder();
   private static final Set SET_WITH_NULL_STRING = new HashSet();

   public CanonicalIterator(String var1) {
      Norm2AllModes var2 = Norm2AllModes.getNFCInstance();
      this.nfd = var2.decomp;
      this.nfcImpl = var2.impl.ensureCanonIterData();
      this.setSource(var1);
   }

   public String getSource() {
      return this.source;
   }

   public void reset() {
      this.done = false;

      for(int var1 = 0; var1 < this.current.length; ++var1) {
         this.current[var1] = 0;
      }

   }

   public String next() {
      if (this.done) {
         return null;
      } else {
         this.buffer.setLength(0);

         for(int var1 = 0; var1 < this.pieces.length; ++var1) {
            this.buffer.append(this.pieces[var1][this.current[var1]]);
         }

         String var3 = this.buffer.toString();
         int var2 = this.current.length - 1;

         while(true) {
            if (var2 < 0) {
               this.done = true;
               break;
            }

            int var10002 = this.current[var2]++;
            if (this.current[var2] < this.pieces[var2].length) {
               break;
            }

            this.current[var2] = 0;
            --var2;
         }

         return var3;
      }
   }

   public void setSource(String var1) {
      this.source = this.nfd.normalize(var1);
      this.done = false;
      if (var1.length() == 0) {
         this.pieces = new String[1][];
         this.current = new int[1];
         this.pieces[0] = new String[]{""};
      } else {
         ArrayList var2 = new ArrayList();
         int var4 = 0;

         int var3;
         int var5;
         for(var5 = UTF16.findOffsetFromCodePoint((String)this.source, 1); var5 < this.source.length(); var5 += Character.charCount(var3)) {
            var3 = this.source.codePointAt(var5);
            if (this.nfcImpl.isCanonSegmentStarter(var3)) {
               var2.add(this.source.substring(var4, var5));
               var4 = var5;
            }
         }

         var2.add(this.source.substring(var4, var5));
         this.pieces = new String[var2.size()][];
         this.current = new int[var2.size()];

         for(var5 = 0; var5 < this.pieces.length; ++var5) {
            if (PROGRESS) {
               System.out.println("SEGMENT");
            }

            this.pieces[var5] = this.getEquivalents((String)var2.get(var5));
         }

      }
   }

   /** @deprecated */
   public static void permute(String var0, boolean var1, Set var2) {
      if (var0.length() <= 2 && UTF16.countCodePoint(var0) <= 1) {
         var2.add(var0);
      } else {
         HashSet var3 = new HashSet();

         int var4;
         for(int var5 = 0; var5 < var0.length(); var5 += UTF16.getCharCount(var4)) {
            var4 = UTF16.charAt(var0, var5);
            if (!var1 || var5 == 0 || UCharacter.getCombiningClass(var4) != 0) {
               var3.clear();
               permute(var0.substring(0, var5) + var0.substring(var5 + UTF16.getCharCount(var4)), var1, var3);
               String var6 = UTF16.valueOf(var0, var5);
               Iterator var7 = var3.iterator();

               while(var7.hasNext()) {
                  String var8 = (String)var7.next();
                  String var9 = var6 + var8;
                  var2.add(var9);
               }
            }
         }

      }
   }

   private String[] getEquivalents(String var1) {
      HashSet var2 = new HashSet();
      Set var3 = this.getEquivalents2(var1);
      HashSet var4 = new HashSet();
      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         var4.clear();
         permute(var6, SKIP_ZEROS, var4);
         Iterator var7 = var4.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            if (Normalizer.compare((String)var8, (String)var1, 0) == 0) {
               if (PROGRESS) {
                  System.out.println("Adding Permutation: " + Utility.hex(var8));
               }

               var2.add(var8);
            } else if (PROGRESS) {
               System.out.println("-Skipping Permutation: " + Utility.hex(var8));
            }
         }
      }

      String[] var9 = new String[var2.size()];
      var2.toArray(var9);
      return var9;
   }

   private Set getEquivalents2(String var1) {
      HashSet var2 = new HashSet();
      if (PROGRESS) {
         System.out.println("Adding: " + Utility.hex(var1));
      }

      var2.add(var1);
      StringBuffer var3 = new StringBuffer();
      UnicodeSet var4 = new UnicodeSet();

      int var5;
      label41:
      for(int var6 = 0; var6 < var1.length(); var6 += Character.charCount(var5)) {
         var5 = var1.codePointAt(var6);
         if (this.nfcImpl.getCanonStartSet(var5, var4)) {
            UnicodeSetIterator var7 = new UnicodeSetIterator(var4);

            while(true) {
               int var8;
               Set var9;
               do {
                  if (!var7.next()) {
                     continue label41;
                  }

                  var8 = var7.codepoint;
                  var9 = this.extract(var8, var1, var6, var3);
               } while(var9 == null);

               String var10 = var1.substring(0, var6);
               var10 = var10 + UTF16.valueOf(var8);
               Iterator var11 = var9.iterator();

               while(var11.hasNext()) {
                  String var12 = (String)var11.next();
                  var2.add(var10 + var12);
               }
            }
         }
      }

      return var2;
   }

   private Set extract(int var1, String var2, int var3, StringBuffer var4) {
      if (PROGRESS) {
         System.out.println(" extract: " + Utility.hex(UTF16.valueOf(var1)) + ", " + Utility.hex(var2.substring(var3)));
      }

      String var5 = this.nfcImpl.getDecomposition(var1);
      if (var5 == null) {
         var5 = UTF16.valueOf(var1);
      }

      boolean var6 = false;
      byte var8 = 0;
      int var9 = UTF16.charAt((String)var5, 0);
      int var11 = var8 + UTF16.getCharCount(var9);
      var4.setLength(0);

      int var7;
      for(int var10 = var3; var10 < var2.length(); var10 += UTF16.getCharCount(var7)) {
         var7 = UTF16.charAt(var2, var10);
         if (var7 == var9) {
            if (PROGRESS) {
               System.out.println("  matches: " + Utility.hex(UTF16.valueOf(var7)));
            }

            if (var11 == var5.length()) {
               var4.append(var2.substring(var10 + UTF16.getCharCount(var7)));
               var6 = true;
               break;
            }

            var9 = UTF16.charAt(var5, var11);
            var11 += UTF16.getCharCount(var9);
         } else {
            if (PROGRESS) {
               System.out.println("  buffer: " + Utility.hex(UTF16.valueOf(var7)));
            }

            UTF16.append(var4, var7);
         }
      }

      if (!var6) {
         return null;
      } else {
         if (PROGRESS) {
            System.out.println("Matches");
         }

         if (var4.length() == 0) {
            return SET_WITH_NULL_STRING;
         } else {
            String var12 = var4.toString();
            return 0 != Normalizer.compare((String)(UTF16.valueOf(var1) + var12), (String)var2.substring(var3), 0) ? null : this.getEquivalents2(var12);
         }
      }
   }

   static {
      SET_WITH_NULL_STRING.add("");
   }
}
