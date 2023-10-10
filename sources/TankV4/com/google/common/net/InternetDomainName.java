package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class InternetDomainName {
   private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
   private static final Splitter DOT_SPLITTER = Splitter.on('.');
   private static final Joiner DOT_JOINER = Joiner.on('.');
   private static final int NO_PUBLIC_SUFFIX_FOUND = -1;
   private static final String DOT_REGEX = "\\.";
   private static final int MAX_PARTS = 127;
   private static final int MAX_LENGTH = 253;
   private static final int MAX_DOMAIN_PART_LENGTH = 63;
   private final String name;
   private final ImmutableList parts;
   private final int publicSuffixIndex;
   private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
   private static final CharMatcher PART_CHAR_MATCHER;

   InternetDomainName(String var1) {
      var1 = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(var1, '.'));
      if (var1.endsWith(".")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      Preconditions.checkArgument(var1.length() <= 253, "Domain name too long: '%s':", var1);
      this.name = var1;
      this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(var1));
      Preconditions.checkArgument(this.parts.size() <= 127, "Domain has too many parts: '%s'", var1);
      Preconditions.checkArgument(validateSyntax(this.parts), "Not a valid domain name: '%s'", var1);
      this.publicSuffixIndex = this.findPublicSuffix();
   }

   private int findPublicSuffix() {
      // $FF: Couldn't be decompiled
   }

   public static InternetDomainName from(String var0) {
      return new InternetDomainName((String)Preconditions.checkNotNull(var0));
   }

   private static boolean validateSyntax(List var0) {
      int var1 = var0.size() - 1;
      if ((String)var0.get(var1) >= 1) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1; ++var2) {
            String var3 = (String)var0.get(var2);
            if (var3 >= 0) {
               return false;
            }
         }

         return true;
      }
   }

   public ImmutableList parts() {
      return this.parts;
   }

   public boolean isPublicSuffix() {
      return this.publicSuffixIndex == 0;
   }

   public InternetDomainName publicSuffix() {
      // $FF: Couldn't be decompiled
   }

   public boolean isUnderPublicSuffix() {
      return this.publicSuffixIndex > 0;
   }

   public InternetDomainName topPrivateDomain() {
      // $FF: Couldn't be decompiled
   }

   public boolean hasParent() {
      return this.parts.size() > 1;
   }

   public InternetDomainName parent() {
      Preconditions.checkState(this.hasParent(), "Domain '%s' has no parent", this.name);
      return this.ancestor(1);
   }

   private InternetDomainName ancestor(int var1) {
      return from(DOT_JOINER.join((Iterable)this.parts.subList(var1, this.parts.size())));
   }

   public InternetDomainName child(String var1) {
      return from((String)Preconditions.checkNotNull(var1) + "." + this.name);
   }

   public static boolean isValid(String var0) {
      try {
         from(var0);
         return true;
      } catch (IllegalArgumentException var2) {
         return false;
      }
   }

   public String toString() {
      return this.name;
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof InternetDomainName) {
         InternetDomainName var2 = (InternetDomainName)var1;
         return this.name.equals(var2.name);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   static {
      PART_CHAR_MATCHER = CharMatcher.JAVA_LETTER_OR_DIGIT.or(DASH_MATCHER);
   }
}
