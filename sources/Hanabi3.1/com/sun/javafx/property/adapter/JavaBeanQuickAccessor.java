package com.sun.javafx.property.adapter;

import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;

public final class JavaBeanQuickAccessor {
   private JavaBeanQuickAccessor() {
   }

   public static ReadOnlyJavaBeanObjectProperty createReadOnlyJavaBeanObjectProperty(Object var0, String var1) throws NoSuchMethodException {
      return ReadOnlyJavaBeanObjectPropertyBuilder.create().bean(var0).name(var1).build();
   }
}
