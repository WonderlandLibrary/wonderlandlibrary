package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public final class ConstructorConstructor {
   private final Map instanceCreators;

   public ConstructorConstructor(Map var1) {
      this.instanceCreators = var1;
   }

   public ObjectConstructor get(TypeToken var1) {
      Type var2 = var1.getType();
      Class var3 = var1.getRawType();
      InstanceCreator var4 = (InstanceCreator)this.instanceCreators.get(var2);
      if (var4 != null) {
         return new ObjectConstructor(this, var4, var2) {
            final InstanceCreator val$typeCreator;
            final Type val$type;
            final ConstructorConstructor this$0;

            {
               this.this$0 = var1;
               this.val$typeCreator = var2;
               this.val$type = var3;
            }

            public Object construct() {
               return this.val$typeCreator.createInstance(this.val$type);
            }
         };
      } else {
         InstanceCreator var5 = (InstanceCreator)this.instanceCreators.get(var3);
         if (var5 != null) {
            return new ObjectConstructor(this, var5, var2) {
               final InstanceCreator val$rawTypeCreator;
               final Type val$type;
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
                  this.val$rawTypeCreator = var2;
                  this.val$type = var3;
               }

               public Object construct() {
                  return this.val$rawTypeCreator.createInstance(this.val$type);
               }
            };
         } else {
            ObjectConstructor var6 = this.newDefaultConstructor(var3);
            if (var6 != null) {
               return var6;
            } else {
               ObjectConstructor var7 = this.newDefaultImplementationConstructor(var2, var3);
               return var7 != null ? var7 : this.newUnsafeAllocator(var2, var3);
            }
         }
      }
   }

   private ObjectConstructor newDefaultConstructor(Class var1) {
      try {
         Constructor var2 = var1.getDeclaredConstructor();
         if (!var2.isAccessible()) {
            var2.setAccessible(true);
         }

         return new ObjectConstructor(this, var2) {
            final Constructor val$constructor;
            final ConstructorConstructor this$0;

            {
               this.this$0 = var1;
               this.val$constructor = var2;
            }

            public Object construct() {
               try {
                  Object var1 = null;
                  return this.val$constructor.newInstance((Object[])var1);
               } catch (InstantiationException var2) {
                  throw new RuntimeException("Failed to invoke " + this.val$constructor + " with no args", var2);
               } catch (InvocationTargetException var3) {
                  throw new RuntimeException("Failed to invoke " + this.val$constructor + " with no args", var3.getTargetException());
               } catch (IllegalAccessException var4) {
                  throw new AssertionError(var4);
               }
            }
         };
      } catch (NoSuchMethodException var3) {
         return null;
      }
   }

   private ObjectConstructor newDefaultImplementationConstructor(Type var1, Class var2) {
      if (Collection.class.isAssignableFrom(var2)) {
         if (SortedSet.class.isAssignableFrom(var2)) {
            return new ObjectConstructor(this) {
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
               }

               public Object construct() {
                  return new TreeSet();
               }
            };
         } else if (EnumSet.class.isAssignableFrom(var2)) {
            return new ObjectConstructor(this, var1) {
               final Type val$type;
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
                  this.val$type = var2;
               }

               public Object construct() {
                  if (this.val$type instanceof ParameterizedType) {
                     Type var1 = ((ParameterizedType)this.val$type).getActualTypeArguments()[0];
                     if (var1 instanceof Class) {
                        return EnumSet.noneOf((Class)var1);
                     } else {
                        throw new JsonIOException("Invalid EnumSet type: " + this.val$type.toString());
                     }
                  } else {
                     throw new JsonIOException("Invalid EnumSet type: " + this.val$type.toString());
                  }
               }
            };
         } else if (Set.class.isAssignableFrom(var2)) {
            return new ObjectConstructor(this) {
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
               }

               public Object construct() {
                  return new LinkedHashSet();
               }
            };
         } else {
            return Queue.class.isAssignableFrom(var2) ? new ObjectConstructor(this) {
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
               }

               public Object construct() {
                  return new LinkedList();
               }
            } : new ObjectConstructor(this) {
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
               }

               public Object construct() {
                  return new ArrayList();
               }
            };
         }
      } else if (Map.class.isAssignableFrom(var2)) {
         if (SortedMap.class.isAssignableFrom(var2)) {
            return new ObjectConstructor(this) {
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
               }

               public Object construct() {
                  return new TreeMap();
               }
            };
         } else {
            return var1 instanceof ParameterizedType && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType)var1).getActualTypeArguments()[0]).getRawType()) ? new ObjectConstructor(this) {
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
               }

               public Object construct() {
                  return new LinkedHashMap();
               }
            } : new ObjectConstructor(this) {
               final ConstructorConstructor this$0;

               {
                  this.this$0 = var1;
               }

               public Object construct() {
                  return new LinkedTreeMap();
               }
            };
         }
      } else {
         return null;
      }
   }

   private ObjectConstructor newUnsafeAllocator(Type var1, Class var2) {
      return new ObjectConstructor(this, var2, var1) {
         private final UnsafeAllocator unsafeAllocator;
         final Class val$rawType;
         final Type val$type;
         final ConstructorConstructor this$0;

         {
            this.this$0 = var1;
            this.val$rawType = var2;
            this.val$type = var3;
            this.unsafeAllocator = UnsafeAllocator.create();
         }

         public Object construct() {
            try {
               Object var1 = this.unsafeAllocator.newInstance(this.val$rawType);
               return var1;
            } catch (Exception var2) {
               throw new RuntimeException("Unable to invoke no-args constructor for " + this.val$type + ". " + "Register an InstanceCreator with Gson for this type may fix this problem.", var2);
            }
         }
      };
   }

   public String toString() {
      return this.instanceCreators.toString();
   }
}
