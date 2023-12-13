package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import joptsimple.internal.Reflection;
import joptsimple.internal.ReflectionException;

abstract class AbstractOptionSpec implements OptionSpec, OptionDescriptor {
   private final List options;
   private final String description;
   private boolean forHelp;

   protected AbstractOptionSpec(String var1) {
      this(Collections.singletonList(var1), "");
   }

   protected AbstractOptionSpec(Collection var1, String var2) {
      this.options = new ArrayList();
      this.arrangeOptions(var1);
      this.description = var2;
   }

   public final Collection options() {
      return Collections.unmodifiableList(this.options);
   }

   public final List values(OptionSet var1) {
      return var1.valuesOf((OptionSpec)this);
   }

   public final Object value(OptionSet var1) {
      return var1.valueOf((OptionSpec)this);
   }

   public String description() {
      return this.description;
   }

   public final AbstractOptionSpec forHelp() {
      this.forHelp = true;
      return this;
   }

   public final boolean isForHelp() {
      return this.forHelp;
   }

   public boolean representsNonOptions() {
      return false;
   }

   protected abstract Object convert(String var1);

   protected Object convertWith(ValueConverter var1, String var2) {
      try {
         return Reflection.convertWith(var1, var2);
      } catch (ReflectionException var4) {
         throw new OptionArgumentConversionException(this.options(), var2, var4);
      } catch (ValueConversionException var5) {
         throw new OptionArgumentConversionException(this.options(), var2, var5);
      }
   }

   protected String argumentTypeIndicatorFrom(ValueConverter var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = var1.valuePattern();
         return var2 == null ? var1.valueType().getName() : var2;
      }
   }

   abstract void handleOption(OptionParser var1, ArgumentList var2, OptionSet var3, String var4);

   private void arrangeOptions(Collection var1) {
      if (var1.size() == 1) {
         this.options.addAll(var1);
      } else {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (var5.length() == 1) {
               var2.add(var5);
            } else {
               var3.add(var5);
            }
         }

         Collections.sort(var2);
         Collections.sort(var3);
         this.options.addAll(var2);
         this.options.addAll(var3);
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof AbstractOptionSpec)) {
         return false;
      } else {
         AbstractOptionSpec var2 = (AbstractOptionSpec)var1;
         return this.options.equals(var2.options);
      }
   }

   public int hashCode() {
      return this.options.hashCode();
   }

   public String toString() {
      return this.options.toString();
   }
}
