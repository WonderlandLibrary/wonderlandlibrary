package joptsimple;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import joptsimple.internal.Classes;
import joptsimple.internal.Rows;
import joptsimple.internal.Strings;

public class BuiltinHelpFormatter implements HelpFormatter {
   private final Rows nonOptionRows;
   private final Rows optionRows;

   BuiltinHelpFormatter() {
      this(80, 2);
   }

   public BuiltinHelpFormatter(int var1, int var2) {
      this.nonOptionRows = new Rows(var1 * 2, 0);
      this.optionRows = new Rows(var1, var2);
   }

   public String format(Map var1) {
      Comparator var2 = new Comparator(this) {
         final BuiltinHelpFormatter this$0;

         {
            this.this$0 = var1;
         }

         public int compare(OptionDescriptor var1, OptionDescriptor var2) {
            return ((String)var1.options().iterator().next()).compareTo((String)var2.options().iterator().next());
         }

         public int compare(Object var1, Object var2) {
            return this.compare((OptionDescriptor)var1, (OptionDescriptor)var2);
         }
      };
      TreeSet var3 = new TreeSet(var2);
      var3.addAll(var1.values());
      this.addRows(var3);
      return this.formattedHelpOutput();
   }

   private String formattedHelpOutput() {
      StringBuilder var1 = new StringBuilder();
      String var2 = this.nonOptionRows.render();
      if (!Strings.isNullOrEmpty(var2)) {
         var1.append(var2).append(Strings.LINE_SEPARATOR);
      }

      var1.append(this.optionRows.render());
      return var1.toString();
   }

   private void addRows(Collection var1) {
      this.addNonOptionsDescription(var1);
      if (var1.isEmpty()) {
         this.optionRows.add("No options specified", "");
      } else {
         this.addHeaders(var1);
         this.addOptions(var1);
      }

      this.fitRowsToWidth();
   }

   private void addNonOptionsDescription(Collection var1) {
      OptionDescriptor var2 = this.findAndRemoveNonOptionsSpec(var1);
      if (var2 != false) {
         this.nonOptionRows.add("Non-option arguments:", "");
         this.nonOptionRows.add(this.createNonOptionArgumentsDisplay(var2), "");
      }

   }

   private String createNonOptionArgumentsDisplay(OptionDescriptor var1) {
      StringBuilder var2 = new StringBuilder();
      this.maybeAppendOptionInfo(var2, var1);
      this.maybeAppendNonOptionsDescription(var2, var1);
      return var2.toString();
   }

   private void maybeAppendNonOptionsDescription(StringBuilder var1, OptionDescriptor var2) {
      var1.append(var1.length() > 0 && !Strings.isNullOrEmpty(var2.description()) ? " -- " : "").append(var2.description());
   }

   private OptionDescriptor findAndRemoveNonOptionsSpec(Collection var1) {
      Iterator var2 = var1.iterator();

      OptionDescriptor var3;
      do {
         if (!var2.hasNext()) {
            throw new AssertionError("no non-options argument spec");
         }

         var3 = (OptionDescriptor)var2.next();
      } while(!var3.representsNonOptions());

      var2.remove();
      return var3;
   }

   private void addHeaders(Collection var1) {
      if (var1 != false) {
         this.optionRows.add("Option (* = required)", "Description");
         this.optionRows.add("---------------------", "-----------");
      } else {
         this.optionRows.add("Option", "Description");
         this.optionRows.add("------", "-----------");
      }

   }

   private void addOptions(Collection var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         OptionDescriptor var3 = (OptionDescriptor)var2.next();
         if (!var3.representsNonOptions()) {
            this.optionRows.add(this.createOptionDisplay(var3), this.createDescriptionDisplay(var3));
         }
      }

   }

   private String createOptionDisplay(OptionDescriptor var1) {
      StringBuilder var2 = new StringBuilder(var1.isRequired() ? "* " : "");
      Iterator var3 = var1.options().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.append(var4.length() > 1 ? "--" : ParserRules.HYPHEN);
         var2.append(var4);
         if (var3.hasNext()) {
            var2.append(", ");
         }
      }

      this.maybeAppendOptionInfo(var2, var1);
      return var2.toString();
   }

   private void maybeAppendOptionInfo(StringBuilder var1, OptionDescriptor var2) {
      String var3 = this.extractTypeIndicator(var2);
      String var4 = var2.argumentDescription();
      if (var3 != null || !Strings.isNullOrEmpty(var4)) {
         this.appendOptionHelp(var1, var3, var4, var2.requiresArgument());
      }

   }

   private String extractTypeIndicator(OptionDescriptor var1) {
      String var2 = var1.argumentTypeIndicator();
      return !Strings.isNullOrEmpty(var2) && !String.class.getName().equals(var2) ? Classes.shortNameOf(var2) : null;
   }

   private void appendOptionHelp(StringBuilder var1, String var2, String var3, boolean var4) {
      if (var4) {
         this.appendTypeIndicator(var1, var2, var3, '<', '>');
      } else {
         this.appendTypeIndicator(var1, var2, var3, '[', ']');
      }

   }

   private void appendTypeIndicator(StringBuilder var1, String var2, String var3, char var4, char var5) {
      var1.append(' ').append(var4);
      if (var2 != null) {
         var1.append(var2);
      }

      if (!Strings.isNullOrEmpty(var3)) {
         if (var2 != null) {
            var1.append(": ");
         }

         var1.append(var3);
      }

      var1.append(var5);
   }

   private String createDescriptionDisplay(OptionDescriptor var1) {
      List var2 = var1.defaultValues();
      if (var2.isEmpty()) {
         return var1.description();
      } else {
         String var3 = this.createDefaultValuesDisplay(var2);
         return (var1.description() + ' ' + Strings.surround("default: " + var3, '(', ')')).trim();
      }
   }

   private String createDefaultValuesDisplay(List var1) {
      return var1.size() == 1 ? var1.get(0).toString() : var1.toString();
   }

   private void fitRowsToWidth() {
      this.nonOptionRows.fitToWidth();
      this.optionRows.fitToWidth();
   }
}
