package com.google.gson.internal;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Excluder implements TypeAdapterFactory, Cloneable {
   private static final double IGNORE_VERSIONS = -1.0D;
   public static final Excluder DEFAULT = new Excluder();
   private double version = -1.0D;
   private int modifiers = 136;
   private boolean serializeInnerClasses = true;
   private boolean requireExpose;
   private List serializationStrategies = Collections.emptyList();
   private List deserializationStrategies = Collections.emptyList();

   protected Excluder clone() {
      try {
         return (Excluder)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError();
      }
   }

   public Excluder withVersion(double var1) {
      Excluder var3 = this.clone();
      var3.version = var1;
      return var3;
   }

   public Excluder withModifiers(int... var1) {
      Excluder var2 = this.clone();
      var2.modifiers = 0;
      int[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var3[var5];
         var2.modifiers |= var6;
      }

      return var2;
   }

   public Excluder disableInnerClassSerialization() {
      Excluder var1 = this.clone();
      var1.serializeInnerClasses = false;
      return var1;
   }

   public Excluder excludeFieldsWithoutExposeAnnotation() {
      Excluder var1 = this.clone();
      var1.requireExpose = true;
      return var1;
   }

   public Excluder withExclusionStrategy(ExclusionStrategy var1, boolean var2, boolean var3) {
      Excluder var4 = this.clone();
      if (var2) {
         var4.serializationStrategies = new ArrayList(this.serializationStrategies);
         var4.serializationStrategies.add(var1);
      }

      if (var3) {
         var4.deserializationStrategies = new ArrayList(this.deserializationStrategies);
         var4.deserializationStrategies.add(var1);
      }

      return var4;
   }

   public TypeAdapter create(Gson var1, TypeToken var2) {
      Class var3 = var2.getRawType();
      boolean var4 = this.excludeClass(var3, true);
      boolean var5 = this.excludeClass(var3, false);
      return !var4 && !var5 ? null : new TypeAdapter(this, var5, var4, var1, var2) {
         private TypeAdapter delegate;
         final boolean val$skipDeserialize;
         final boolean val$skipSerialize;
         final Gson val$gson;
         final TypeToken val$type;
         final Excluder this$0;

         {
            this.this$0 = var1;
            this.val$skipDeserialize = var2;
            this.val$skipSerialize = var3;
            this.val$gson = var4;
            this.val$type = var5;
         }

         public Object read(JsonReader var1) throws IOException {
            if (this.val$skipDeserialize) {
               var1.skipValue();
               return null;
            } else {
               return this.delegate().read(var1);
            }
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            if (this.val$skipSerialize) {
               var1.nullValue();
            } else {
               this.delegate().write(var1, var2);
            }
         }

         private TypeAdapter delegate() {
            TypeAdapter var1 = this.delegate;
            return var1 != null ? var1 : (this.delegate = this.val$gson.getDelegateAdapter(this.this$0, this.val$type));
         }
      };
   }

   public boolean excludeField(Field var1, boolean var2) {
      if ((this.modifiers & var1.getModifiers()) != 0) {
         return true;
      } else {
         if (this.version != -1.0D) {
            Since var10001 = (Since)var1.getAnnotation(Since.class);
            if ((Until)var1.getAnnotation(Until.class) != false) {
               return true;
            }
         }

         if (var1.isSynthetic()) {
            return true;
         } else {
            if (this.requireExpose) {
               label69: {
                  Expose var3 = (Expose)var1.getAnnotation(Expose.class);
                  if (var3 != null) {
                     if (var2) {
                        if (var3.serialize()) {
                           break label69;
                        }
                     } else if (var3.deserialize()) {
                        break label69;
                     }
                  }

                  return true;
               }
            }

            if (!this.serializeInnerClasses && var1.getType() != false) {
               return true;
            } else if (var1.getType() == false) {
               return true;
            } else {
               List var7 = var2 ? this.serializationStrategies : this.deserializationStrategies;
               if (!var7.isEmpty()) {
                  FieldAttributes var4 = new FieldAttributes(var1);
                  Iterator var5 = var7.iterator();

                  while(var5.hasNext()) {
                     ExclusionStrategy var6 = (ExclusionStrategy)var5.next();
                     if (var6.shouldSkipField(var4)) {
                        return true;
                     }
                  }
               }

               return false;
            }
         }
      }
   }

   public boolean excludeClass(Class var1, boolean var2) {
      if (this.version != -1.0D) {
         Since var10001 = (Since)var1.getAnnotation(Since.class);
         if ((Until)var1.getAnnotation(Until.class) != false) {
            return true;
         }
      }

      if (!this.serializeInnerClasses && var1 != false) {
         return true;
      } else if (var1 == false) {
         return true;
      } else {
         List var3 = var2 ? this.serializationStrategies : this.deserializationStrategies;
         Iterator var4 = var3.iterator();

         ExclusionStrategy var5;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            var5 = (ExclusionStrategy)var4.next();
         } while(!var5.shouldSkipClass(var1));

         return true;
      }
   }

   protected Object clone() throws CloneNotSupportedException {
      return this.clone();
   }
}
