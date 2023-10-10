package com.google.gson;

import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GsonBuilder {
   private Excluder excluder;
   private LongSerializationPolicy longSerializationPolicy;
   private FieldNamingStrategy fieldNamingPolicy;
   private final Map instanceCreators;
   private final List factories;
   private final List hierarchyFactories;
   private boolean serializeNulls;
   private String datePattern;
   private int dateStyle;
   private int timeStyle;
   private boolean complexMapKeySerialization;
   private boolean serializeSpecialFloatingPointValues;
   private boolean escapeHtmlChars;
   private boolean prettyPrinting;
   private boolean generateNonExecutableJson;

   public GsonBuilder() {
      this.excluder = Excluder.DEFAULT;
      this.longSerializationPolicy = LongSerializationPolicy.DEFAULT;
      this.fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
      this.instanceCreators = new HashMap();
      this.factories = new ArrayList();
      this.hierarchyFactories = new ArrayList();
      this.dateStyle = 2;
      this.timeStyle = 2;
      this.escapeHtmlChars = true;
   }

   public GsonBuilder setVersion(double var1) {
      this.excluder = this.excluder.withVersion(var1);
      return this;
   }

   public GsonBuilder excludeFieldsWithModifiers(int... var1) {
      this.excluder = this.excluder.withModifiers(var1);
      return this;
   }

   public GsonBuilder generateNonExecutableJson() {
      this.generateNonExecutableJson = true;
      return this;
   }

   public GsonBuilder excludeFieldsWithoutExposeAnnotation() {
      this.excluder = this.excluder.excludeFieldsWithoutExposeAnnotation();
      return this;
   }

   public GsonBuilder serializeNulls() {
      this.serializeNulls = true;
      return this;
   }

   public GsonBuilder enableComplexMapKeySerialization() {
      this.complexMapKeySerialization = true;
      return this;
   }

   public GsonBuilder disableInnerClassSerialization() {
      this.excluder = this.excluder.disableInnerClassSerialization();
      return this;
   }

   public GsonBuilder setLongSerializationPolicy(LongSerializationPolicy var1) {
      this.longSerializationPolicy = var1;
      return this;
   }

   public GsonBuilder setFieldNamingPolicy(FieldNamingPolicy var1) {
      this.fieldNamingPolicy = var1;
      return this;
   }

   public GsonBuilder setFieldNamingStrategy(FieldNamingStrategy var1) {
      this.fieldNamingPolicy = var1;
      return this;
   }

   public GsonBuilder setExclusionStrategies(ExclusionStrategy... var1) {
      ExclusionStrategy[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ExclusionStrategy var5 = var2[var4];
         this.excluder = this.excluder.withExclusionStrategy(var5, true, true);
      }

      return this;
   }

   public GsonBuilder addSerializationExclusionStrategy(ExclusionStrategy var1) {
      this.excluder = this.excluder.withExclusionStrategy(var1, true, false);
      return this;
   }

   public GsonBuilder addDeserializationExclusionStrategy(ExclusionStrategy var1) {
      this.excluder = this.excluder.withExclusionStrategy(var1, false, true);
      return this;
   }

   public GsonBuilder setPrettyPrinting() {
      this.prettyPrinting = true;
      return this;
   }

   public GsonBuilder disableHtmlEscaping() {
      this.escapeHtmlChars = false;
      return this;
   }

   public GsonBuilder setDateFormat(String var1) {
      this.datePattern = var1;
      return this;
   }

   public GsonBuilder setDateFormat(int var1) {
      this.dateStyle = var1;
      this.datePattern = null;
      return this;
   }

   public GsonBuilder setDateFormat(int var1, int var2) {
      this.dateStyle = var1;
      this.timeStyle = var2;
      this.datePattern = null;
      return this;
   }

   public GsonBuilder registerTypeAdapter(Type var1, Object var2) {
      $Gson$Preconditions.checkArgument(var2 instanceof JsonSerializer || var2 instanceof JsonDeserializer || var2 instanceof InstanceCreator || var2 instanceof TypeAdapter);
      if (var2 instanceof InstanceCreator) {
         this.instanceCreators.put(var1, (InstanceCreator)var2);
      }

      if (var2 instanceof JsonSerializer || var2 instanceof JsonDeserializer) {
         TypeToken var3 = TypeToken.get(var1);
         this.factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(var3, var2));
      }

      if (var2 instanceof TypeAdapter) {
         this.factories.add(TypeAdapters.newFactory(TypeToken.get(var1), (TypeAdapter)var2));
      }

      return this;
   }

   public GsonBuilder registerTypeAdapterFactory(TypeAdapterFactory var1) {
      this.factories.add(var1);
      return this;
   }

   public GsonBuilder registerTypeHierarchyAdapter(Class var1, Object var2) {
      $Gson$Preconditions.checkArgument(var2 instanceof JsonSerializer || var2 instanceof JsonDeserializer || var2 instanceof TypeAdapter);
      if (var2 instanceof JsonDeserializer || var2 instanceof JsonSerializer) {
         this.hierarchyFactories.add(0, TreeTypeAdapter.newTypeHierarchyFactory(var1, var2));
      }

      if (var2 instanceof TypeAdapter) {
         this.factories.add(TypeAdapters.newTypeHierarchyFactory(var1, (TypeAdapter)var2));
      }

      return this;
   }

   public GsonBuilder serializeSpecialFloatingPointValues() {
      this.serializeSpecialFloatingPointValues = true;
      return this;
   }

   public Gson create() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.factories);
      Collections.reverse(var1);
      var1.addAll(this.hierarchyFactories);
      this.addTypeAdaptersForDate(this.datePattern, this.dateStyle, this.timeStyle, var1);
      return new Gson(this.excluder, this.fieldNamingPolicy, this.instanceCreators, this.serializeNulls, this.complexMapKeySerialization, this.generateNonExecutableJson, this.escapeHtmlChars, this.prettyPrinting, this.serializeSpecialFloatingPointValues, this.longSerializationPolicy, var1);
   }

   private void addTypeAdaptersForDate(String var1, int var2, int var3, List var4) {
      DefaultDateTypeAdapter var5;
      if (var1 != null && !"".equals(var1.trim())) {
         var5 = new DefaultDateTypeAdapter(var1);
      } else {
         if (var2 == 2 || var3 == 2) {
            return;
         }

         var5 = new DefaultDateTypeAdapter(var2, var3);
      }

      var4.add(TreeTypeAdapter.newFactory(TypeToken.get(Date.class), var5));
      var4.add(TreeTypeAdapter.newFactory(TypeToken.get(Timestamp.class), var5));
      var4.add(TreeTypeAdapter.newFactory(TypeToken.get(java.sql.Date.class), var5));
   }
}
