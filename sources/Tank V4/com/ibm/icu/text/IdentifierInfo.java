package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/** @deprecated */
public class IdentifierInfo {
   private static final UnicodeSet ASCII = (new UnicodeSet(0, 127)).freeze();
   private String identifier;
   private final BitSet requiredScripts = new BitSet();
   private final Set scriptSetSet = new HashSet();
   private final BitSet commonAmongAlternates = new BitSet();
   private final UnicodeSet numerics = new UnicodeSet();
   private final UnicodeSet identifierProfile = new UnicodeSet(0, 1114111);
   private static final BitSet JAPANESE = set(new BitSet(), 25, 17, 20, 22);
   private static final BitSet CHINESE = set(new BitSet(), 25, 17, 5);
   private static final BitSet KOREAN = set(new BitSet(), 25, 17, 18);
   private static final BitSet CONFUSABLE_WITH_LATIN = set(new BitSet(), 8, 14, 6);
   /** @deprecated */
   public static final Comparator BITSET_COMPARATOR = new Comparator() {
      public int compare(BitSet var1, BitSet var2) {
         int var3 = var1.cardinality() - var2.cardinality();
         if (var3 != 0) {
            return var3;
         } else {
            int var4 = var1.nextSetBit(0);

            for(int var5 = var2.nextSetBit(0); (var3 = var4 - var5) == 0 && var4 > 0; var5 = var2.nextSetBit(var5 + 1)) {
               var4 = var1.nextSetBit(var4 + 1);
            }

            return var3;
         }
      }

      public int compare(Object var1, Object var2) {
         return this.compare((BitSet)var1, (BitSet)var2);
      }
   };

   private IdentifierInfo clear() {
      this.requiredScripts.clear();
      this.scriptSetSet.clear();
      this.numerics.clear();
      this.commonAmongAlternates.clear();
      return this;
   }

   /** @deprecated */
   public IdentifierInfo setIdentifierProfile(UnicodeSet var1) {
      this.identifierProfile.set(var1);
      return this;
   }

   /** @deprecated */
   public UnicodeSet getIdentifierProfile() {
      return new UnicodeSet(this.identifierProfile);
   }

   /** @deprecated */
   public IdentifierInfo setIdentifier(String var1) {
      this.identifier = var1;
      this.clear();
      BitSet var2 = new BitSet();

      for(int var4 = 0; var4 < var1.length(); var4 += Character.charCount(var4)) {
         int var3 = Character.codePointAt(var1, var4);
         if (UCharacter.getType(var3) == 9) {
            this.numerics.add(var3 - UCharacter.getNumericValue(var3));
         }

         UScript.getScriptExtensions(var3, var2);
         var2.clear(0);
         var2.clear(1);
         switch(var2.cardinality()) {
         case 0:
            break;
         case 1:
            this.requiredScripts.or(var2);
            break;
         default:
            if (!this.requiredScripts.intersects(var2) && this.scriptSetSet.add(var2)) {
               var2 = new BitSet();
            }
         }
      }

      if (this.scriptSetSet.size() > 0) {
         this.commonAmongAlternates.set(0, 159);
         Iterator var8 = this.scriptSetSet.iterator();

         label54:
         while(true) {
            while(true) {
               if (!var8.hasNext()) {
                  break label54;
               }

               BitSet var5 = (BitSet)var8.next();
               if (this.requiredScripts.intersects(var5)) {
                  var8.remove();
               } else {
                  this.commonAmongAlternates.and(var5);
                  Iterator var6 = this.scriptSetSet.iterator();

                  while(var6.hasNext()) {
                     BitSet var7 = (BitSet)var6.next();
                     if (var5 != var7 && var7 >= 0) {
                        var8.remove();
                        break;
                     }
                  }
               }
            }
         }
      }

      if (this.scriptSetSet.size() == 0) {
         this.commonAmongAlternates.clear();
      }

      return this;
   }

   /** @deprecated */
   public String getIdentifier() {
      return this.identifier;
   }

   /** @deprecated */
   public BitSet getScripts() {
      return (BitSet)this.requiredScripts.clone();
   }

   /** @deprecated */
   public Set getAlternates() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.scriptSetSet.iterator();

      while(var2.hasNext()) {
         BitSet var3 = (BitSet)var2.next();
         var1.add((BitSet)var3.clone());
      }

      return var1;
   }

   /** @deprecated */
   public UnicodeSet getNumerics() {
      return new UnicodeSet(this.numerics);
   }

   /** @deprecated */
   public BitSet getCommonAmongAlternates() {
      return (BitSet)this.commonAmongAlternates.clone();
   }

   /** @deprecated */
   public SpoofChecker.RestrictionLevel getRestrictionLevel() {
      if (this.identifierProfile.containsAll(this.identifier) && this.getNumerics().size() <= 1) {
         if (ASCII.containsAll(this.identifier)) {
            return SpoofChecker.RestrictionLevel.ASCII;
         } else {
            int var1 = this.requiredScripts.cardinality() + (this.commonAmongAlternates.cardinality() == 0 ? this.scriptSetSet.size() : 1);
            if (var1 < 2) {
               return SpoofChecker.RestrictionLevel.HIGHLY_RESTRICTIVE;
            } else {
               BitSet var10001 = JAPANESE;
               if (this.requiredScripts == false) {
                  BitSet var10003 = CHINESE;
                  if (this.requiredScripts == false) {
                     BitSet var10005 = KOREAN;
                     if (this.requiredScripts != false) {
                        if (var1 == 2 && this.requiredScripts.get(25) && !this.requiredScripts.intersects(CONFUSABLE_WITH_LATIN)) {
                           return SpoofChecker.RestrictionLevel.MODERATELY_RESTRICTIVE;
                        }

                        return SpoofChecker.RestrictionLevel.MINIMALLY_RESTRICTIVE;
                     }
                  }
               }

               return SpoofChecker.RestrictionLevel.HIGHLY_RESTRICTIVE;
            }
         }
      } else {
         return SpoofChecker.RestrictionLevel.UNRESTRICTIVE;
      }
   }

   /** @deprecated */
   public int getScriptCount() {
      int var1 = this.requiredScripts.cardinality() + (this.commonAmongAlternates.cardinality() == 0 ? this.scriptSetSet.size() : 1);
      return var1;
   }

   /** @deprecated */
   public String toString() {
      return this.identifier + ", " + this.identifierProfile.toPattern(false) + ", " + this.getRestrictionLevel() + ", " + displayScripts(this.requiredScripts) + ", " + displayAlternates(this.scriptSetSet) + ", " + this.numerics.toPattern(false);
   }

   /** @deprecated */
   public static String displayAlternates(Set var0) {
      if (var0.size() == 0) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder();
         TreeSet var2 = new TreeSet(BITSET_COMPARATOR);
         var2.addAll(var0);

         BitSet var4;
         for(Iterator var3 = var2.iterator(); var3.hasNext(); var1.append(displayScripts(var4))) {
            var4 = (BitSet)var3.next();
            if (var1.length() != 0) {
               var1.append("; ");
            }
         }

         return var1.toString();
      }
   }

   /** @deprecated */
   public static String displayScripts(BitSet var0) {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = var0.nextSetBit(0); var2 >= 0; var2 = var0.nextSetBit(var2 + 1)) {
         if (var1.length() != 0) {
            var1.append(' ');
         }

         var1.append(UScript.getShortName(var2));
      }

      return var1.toString();
   }

   /** @deprecated */
   public static BitSet parseScripts(String var0) {
      BitSet var1 = new BitSet();
      String[] var2 = var0.trim().split(",?\\s+");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var5.length() != 0) {
            var1.set(UScript.getCodeFromName(var5));
         }
      }

      return var1;
   }

   /** @deprecated */
   public static Set parseAlternates(String var0) {
      HashSet var1 = new HashSet();
      String[] var2 = var0.trim().split("\\s*;\\s*");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         if (var5.length() != 0) {
            var1.add(parseScripts(var5));
         }
      }

      return var1;
   }

   /** @deprecated */
   public static final BitSet set(BitSet var0, int... var1) {
      int[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         var0.set(var5);
      }

      return var0;
   }
}
