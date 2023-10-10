package com.ibm.icu.text;

import com.ibm.icu.impl.IDNA2003;
import com.ibm.icu.impl.UTS46;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public abstract class IDNA {
   public static final int DEFAULT = 0;
   public static final int ALLOW_UNASSIGNED = 1;
   public static final int USE_STD3_RULES = 2;
   public static final int CHECK_BIDI = 4;
   public static final int CHECK_CONTEXTJ = 8;
   public static final int NONTRANSITIONAL_TO_ASCII = 16;
   public static final int NONTRANSITIONAL_TO_UNICODE = 32;
   public static final int CHECK_CONTEXTO = 64;

   public static IDNA getUTS46Instance(int var0) {
      return new UTS46(var0);
   }

   public abstract StringBuilder labelToASCII(CharSequence var1, StringBuilder var2, IDNA.Info var3);

   public abstract StringBuilder labelToUnicode(CharSequence var1, StringBuilder var2, IDNA.Info var3);

   public abstract StringBuilder nameToASCII(CharSequence var1, StringBuilder var2, IDNA.Info var3);

   public abstract StringBuilder nameToUnicode(CharSequence var1, StringBuilder var2, IDNA.Info var3);

   /** @deprecated */
   protected static void resetInfo(IDNA.Info var0) {
      IDNA.Info.access$000(var0);
   }

   /** @deprecated */
   protected static boolean hasCertainErrors(IDNA.Info var0, EnumSet var1) {
      return !IDNA.Info.access$100(var0).isEmpty() && !Collections.disjoint(IDNA.Info.access$100(var0), var1);
   }

   /** @deprecated */
   protected static boolean hasCertainLabelErrors(IDNA.Info var0, EnumSet var1) {
      return !IDNA.Info.access$200(var0).isEmpty() && !Collections.disjoint(IDNA.Info.access$200(var0), var1);
   }

   /** @deprecated */
   protected static void addLabelError(IDNA.Info var0, IDNA.Error var1) {
      IDNA.Info.access$200(var0).add(var1);
   }

   /** @deprecated */
   protected static void promoteAndResetLabelErrors(IDNA.Info var0) {
      if (!IDNA.Info.access$200(var0).isEmpty()) {
         IDNA.Info.access$100(var0).addAll(IDNA.Info.access$200(var0));
         IDNA.Info.access$200(var0).clear();
      }

   }

   /** @deprecated */
   protected static void addError(IDNA.Info var0, IDNA.Error var1) {
      IDNA.Info.access$100(var0).add(var1);
   }

   /** @deprecated */
   protected static void setTransitionalDifferent(IDNA.Info var0) {
      IDNA.Info.access$302(var0, true);
   }

   /** @deprecated */
   protected static void setBiDi(IDNA.Info var0) {
      IDNA.Info.access$402(var0, true);
   }

   /** @deprecated */
   protected static boolean isBiDi(IDNA.Info var0) {
      return IDNA.Info.access$400(var0);
   }

   /** @deprecated */
   protected static void setNotOkBiDi(IDNA.Info var0) {
      IDNA.Info.access$502(var0, false);
   }

   /** @deprecated */
   protected static boolean isOkBiDi(IDNA.Info var0) {
      return IDNA.Info.access$500(var0);
   }

   /** @deprecated */
   protected IDNA() {
   }

   public static StringBuffer convertToASCII(String var0, int var1) throws StringPrepParseException {
      UCharacterIterator var2 = UCharacterIterator.getInstance(var0);
      return convertToASCII(var2, var1);
   }

   public static StringBuffer convertToASCII(StringBuffer var0, int var1) throws StringPrepParseException {
      UCharacterIterator var2 = UCharacterIterator.getInstance(var0);
      return convertToASCII(var2, var1);
   }

   public static StringBuffer convertToASCII(UCharacterIterator var0, int var1) throws StringPrepParseException {
      return IDNA2003.convertToASCII(var0, var1);
   }

   public static StringBuffer convertIDNToASCII(UCharacterIterator var0, int var1) throws StringPrepParseException {
      return convertIDNToASCII(var0.getText(), var1);
   }

   public static StringBuffer convertIDNToASCII(StringBuffer var0, int var1) throws StringPrepParseException {
      return convertIDNToASCII(var0.toString(), var1);
   }

   public static StringBuffer convertIDNToASCII(String var0, int var1) throws StringPrepParseException {
      return IDNA2003.convertIDNToASCII(var0, var1);
   }

   public static StringBuffer convertToUnicode(String var0, int var1) throws StringPrepParseException {
      UCharacterIterator var2 = UCharacterIterator.getInstance(var0);
      return convertToUnicode(var2, var1);
   }

   public static StringBuffer convertToUnicode(StringBuffer var0, int var1) throws StringPrepParseException {
      UCharacterIterator var2 = UCharacterIterator.getInstance(var0);
      return convertToUnicode(var2, var1);
   }

   public static StringBuffer convertToUnicode(UCharacterIterator var0, int var1) throws StringPrepParseException {
      return IDNA2003.convertToUnicode(var0, var1);
   }

   public static StringBuffer convertIDNToUnicode(UCharacterIterator var0, int var1) throws StringPrepParseException {
      return convertIDNToUnicode(var0.getText(), var1);
   }

   public static StringBuffer convertIDNToUnicode(StringBuffer var0, int var1) throws StringPrepParseException {
      return convertIDNToUnicode(var0.toString(), var1);
   }

   public static StringBuffer convertIDNToUnicode(String var0, int var1) throws StringPrepParseException {
      return IDNA2003.convertIDNToUnicode(var0, var1);
   }

   public static int compare(StringBuffer var0, StringBuffer var1, int var2) throws StringPrepParseException {
      if (var0 != null && var1 != null) {
         return IDNA2003.compare(var0.toString(), var1.toString(), var2);
      } else {
         throw new IllegalArgumentException("One of the source buffers is null");
      }
   }

   public static int compare(String var0, String var1, int var2) throws StringPrepParseException {
      if (var0 != null && var1 != null) {
         return IDNA2003.compare(var0, var1, var2);
      } else {
         throw new IllegalArgumentException("One of the source buffers is null");
      }
   }

   public static int compare(UCharacterIterator var0, UCharacterIterator var1, int var2) throws StringPrepParseException {
      if (var0 != null && var1 != null) {
         return IDNA2003.compare(var0.getText(), var1.getText(), var2);
      } else {
         throw new IllegalArgumentException("One of the source buffers is null");
      }
   }

   public static enum Error {
      EMPTY_LABEL,
      LABEL_TOO_LONG,
      DOMAIN_NAME_TOO_LONG,
      LEADING_HYPHEN,
      TRAILING_HYPHEN,
      HYPHEN_3_4,
      LEADING_COMBINING_MARK,
      DISALLOWED,
      PUNYCODE,
      LABEL_HAS_DOT,
      INVALID_ACE_LABEL,
      BIDI,
      CONTEXTJ,
      CONTEXTO_PUNCTUATION,
      CONTEXTO_DIGITS;

      private static final IDNA.Error[] $VALUES = new IDNA.Error[]{EMPTY_LABEL, LABEL_TOO_LONG, DOMAIN_NAME_TOO_LONG, LEADING_HYPHEN, TRAILING_HYPHEN, HYPHEN_3_4, LEADING_COMBINING_MARK, DISALLOWED, PUNYCODE, LABEL_HAS_DOT, INVALID_ACE_LABEL, BIDI, CONTEXTJ, CONTEXTO_PUNCTUATION, CONTEXTO_DIGITS};
   }

   public static final class Info {
      private EnumSet errors = EnumSet.noneOf(IDNA.Error.class);
      private EnumSet labelErrors = EnumSet.noneOf(IDNA.Error.class);
      private boolean isTransDiff = false;
      private boolean isBiDi = false;
      private boolean isOkBiDi = true;

      public boolean hasErrors() {
         return !this.errors.isEmpty();
      }

      public Set getErrors() {
         return this.errors;
      }

      public boolean isTransitionalDifferent() {
         return this.isTransDiff;
      }

      private void reset() {
         this.errors.clear();
         this.labelErrors.clear();
         this.isTransDiff = false;
         this.isBiDi = false;
         this.isOkBiDi = true;
      }

      static void access$000(IDNA.Info var0) {
         var0.reset();
      }

      static EnumSet access$100(IDNA.Info var0) {
         return var0.errors;
      }

      static EnumSet access$200(IDNA.Info var0) {
         return var0.labelErrors;
      }

      static boolean access$302(IDNA.Info var0, boolean var1) {
         return var0.isTransDiff = var1;
      }

      static boolean access$402(IDNA.Info var0, boolean var1) {
         return var0.isBiDi = var1;
      }

      static boolean access$400(IDNA.Info var0) {
         return var0.isBiDi;
      }

      static boolean access$502(IDNA.Info var0, boolean var1) {
         return var0.isOkBiDi = var1;
      }

      static boolean access$500(IDNA.Info var0) {
         return var0.isOkBiDi;
      }
   }
}
