package com.sun.javafx.binding;

import java.util.ArrayList;
import java.util.Locale;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class StringFormatter extends StringBinding {
   private static Object extractValue(Object var0) {
      return var0 instanceof ObservableValue ? ((ObservableValue)var0).getValue() : var0;
   }

   private static Object[] extractValues(Object[] var0) {
      int var1 = var0.length;
      Object[] var2 = new Object[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = extractValue(var0[var3]);
      }

      return var2;
   }

   private static ObservableValue[] extractDependencies(Object... var0) {
      ArrayList var1 = new ArrayList();
      Object[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         if (var5 instanceof ObservableValue) {
            var1.add((ObservableValue)var5);
         }
      }

      return (ObservableValue[])var1.toArray(new ObservableValue[var1.size()]);
   }

   public static StringExpression convert(final ObservableValue var0) {
      if (var0 == null) {
         throw new NullPointerException("ObservableValue must be specified");
      } else {
         return (StringExpression)(var0 instanceof StringExpression ? (StringExpression)var0 : new StringBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected String computeValue() {
               Object var1 = var0.getValue();
               return var1 == null ? "null" : var1.toString();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public static StringExpression concat(final Object... var0) {
      if (var0 != null && var0.length != 0) {
         if (var0.length == 1) {
            Object var6 = var0[0];
            return (StringExpression)(var6 instanceof ObservableValue ? convert((ObservableValue)var6) : StringConstant.valueOf(var6.toString()));
         } else if (extractDependencies(var0).length != 0) {
            return new StringFormatter() {
               {
                  super.bind(StringFormatter.extractDependencies(var0));
               }

               public void dispose() {
                  super.unbind(StringFormatter.extractDependencies(var0));
               }

               protected String computeValue() {
                  StringBuilder var1 = new StringBuilder();
                  Object[] var2 = var0;
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     Object var5 = var2[var4];
                     var1.append(StringFormatter.extractValue(var5));
                  }

                  return var1.toString();
               }

               public ObservableList getDependencies() {
                  return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Object[])StringFormatter.extractDependencies(var0)));
               }
            };
         } else {
            StringBuilder var1 = new StringBuilder();
            Object[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Object var5 = var2[var4];
               var1.append(var5);
            }

            return StringConstant.valueOf(var1.toString());
         }
      } else {
         return StringConstant.valueOf("");
      }
   }

   public static StringExpression format(final Locale var0, final String var1, final Object... var2) {
      if (var1 == null) {
         throw new NullPointerException("Format cannot be null.");
      } else if (extractDependencies(var2).length == 0) {
         return StringConstant.valueOf(String.format(var0, var1, var2));
      } else {
         StringFormatter var3 = new StringFormatter() {
            {
               super.bind(StringFormatter.extractDependencies(var2));
            }

            public void dispose() {
               super.unbind(StringFormatter.extractDependencies(var2));
            }

            protected String computeValue() {
               Object[] var1x = StringFormatter.extractValues(var2);
               return String.format(var0, var1, var1x);
            }

            public ObservableList getDependencies() {
               return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Object[])StringFormatter.extractDependencies(var2)));
            }
         };
         var3.get();
         return var3;
      }
   }

   public static StringExpression format(final String var0, final Object... var1) {
      if (var0 == null) {
         throw new NullPointerException("Format cannot be null.");
      } else if (extractDependencies(var1).length == 0) {
         return StringConstant.valueOf(String.format(var0, var1));
      } else {
         StringFormatter var2 = new StringFormatter() {
            {
               super.bind(StringFormatter.extractDependencies(var1));
            }

            public void dispose() {
               super.unbind(StringFormatter.extractDependencies(var1));
            }

            protected String computeValue() {
               Object[] var1x = StringFormatter.extractValues(var1);
               return String.format(var0, var1x);
            }

            public ObservableList getDependencies() {
               return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Object[])StringFormatter.extractDependencies(var1)));
            }
         };
         var2.get();
         return var2;
      }
   }
}
