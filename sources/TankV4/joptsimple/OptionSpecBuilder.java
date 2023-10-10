package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OptionSpecBuilder extends NoArgumentOptionSpec {
   private final OptionParser parser;

   OptionSpecBuilder(OptionParser var1, Collection var2, String var3) {
      super(var2, var3);
      this.parser = var1;
      this.attachToParser();
   }

   private void attachToParser() {
      this.parser.recognize(this);
   }

   public ArgumentAcceptingOptionSpec withRequiredArg() {
      RequiredArgumentOptionSpec var1 = new RequiredArgumentOptionSpec(this.options(), this.description());
      this.parser.recognize(var1);
      return var1;
   }

   public ArgumentAcceptingOptionSpec withOptionalArg() {
      OptionalArgumentOptionSpec var1 = new OptionalArgumentOptionSpec(this.options(), this.description());
      this.parser.recognize(var1);
      return var1;
   }

   public OptionSpecBuilder requiredIf(String var1, String... var2) {
      List var3 = this.validatedDependents(var1, var2);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         this.parser.requiredIf(this.options(), var5);
      }

      return this;
   }

   public OptionSpecBuilder requiredIf(OptionSpec var1, OptionSpec... var2) {
      this.parser.requiredIf(this.options(), var1);
      OptionSpec[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         OptionSpec var6 = var3[var5];
         this.parser.requiredIf(this.options(), var6);
      }

      return this;
   }

   public OptionSpecBuilder requiredUnless(String var1, String... var2) {
      List var3 = this.validatedDependents(var1, var2);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         this.parser.requiredUnless(this.options(), var5);
      }

      return this;
   }

   public OptionSpecBuilder requiredUnless(OptionSpec var1, OptionSpec... var2) {
      this.parser.requiredUnless(this.options(), var1);
      OptionSpec[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         OptionSpec var6 = var3[var5];
         this.parser.requiredUnless(this.options(), var6);
      }

      return this;
   }

   private List validatedDependents(String var1, String... var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1);
      Collections.addAll(var3, var2);
      Iterator var4 = var3.iterator();

      String var5;
      do {
         if (!var4.hasNext()) {
            return var3;
         }

         var5 = (String)var4.next();
      } while(this.parser.isRecognized(var5));

      throw new UnconfiguredOptionException(var5);
   }

   public List defaultValues() {
      return super.defaultValues();
   }

   public String argumentTypeIndicator() {
      return super.argumentTypeIndicator();
   }

   public String argumentDescription() {
      return super.argumentDescription();
   }

   public boolean isRequired() {
      return super.isRequired();
   }

   public boolean requiresArgument() {
      return super.requiresArgument();
   }

   public boolean acceptsArguments() {
      return super.acceptsArguments();
   }

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public boolean representsNonOptions() {
      return super.representsNonOptions();
   }

   public String description() {
      return super.description();
   }
}
