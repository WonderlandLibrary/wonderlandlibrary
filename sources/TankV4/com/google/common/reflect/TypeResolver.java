package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@Beta
public final class TypeResolver {
   private final TypeResolver.TypeTable typeTable;

   public TypeResolver() {
      this.typeTable = new TypeResolver.TypeTable();
   }

   private TypeResolver(TypeResolver.TypeTable var1) {
      this.typeTable = var1;
   }

   static TypeResolver accordingTo(Type var0) {
      return (new TypeResolver()).where(TypeResolver.TypeMappingIntrospector.getTypeMappings(var0));
   }

   public TypeResolver where(Type var1, Type var2) {
      HashMap var3 = Maps.newHashMap();
      populateTypeMappings(var3, (Type)Preconditions.checkNotNull(var1), (Type)Preconditions.checkNotNull(var2));
      return this.where(var3);
   }

   TypeResolver where(Map var1) {
      return new TypeResolver(this.typeTable.where(var1));
   }

   private static void populateTypeMappings(Map var0, Type var1, Type var2) {
      if (!var1.equals(var2)) {
         (new TypeVisitor(var0, var2) {
            final Map val$mappings;
            final Type val$to;

            {
               this.val$mappings = var1;
               this.val$to = var2;
            }

            void visitTypeVariable(TypeVariable var1) {
               this.val$mappings.put(new TypeResolver.TypeVariableKey(var1), this.val$to);
            }

            void visitWildcardType(WildcardType var1) {
               WildcardType var2 = (WildcardType)TypeResolver.access$000(WildcardType.class, this.val$to);
               Type[] var3 = var1.getUpperBounds();
               Type[] var4 = var2.getUpperBounds();
               Type[] var5 = var1.getLowerBounds();
               Type[] var6 = var2.getLowerBounds();
               Preconditions.checkArgument(var3.length == var4.length && var5.length == var6.length, "Incompatible type: %s vs. %s", var1, this.val$to);

               int var7;
               for(var7 = 0; var7 < var3.length; ++var7) {
                  TypeResolver.access$100(this.val$mappings, var3[var7], var4[var7]);
               }

               for(var7 = 0; var7 < var5.length; ++var7) {
                  TypeResolver.access$100(this.val$mappings, var5[var7], var6[var7]);
               }

            }

            void visitParameterizedType(ParameterizedType var1) {
               ParameterizedType var2 = (ParameterizedType)TypeResolver.access$000(ParameterizedType.class, this.val$to);
               Preconditions.checkArgument(var1.getRawType().equals(var2.getRawType()), "Inconsistent raw type: %s vs. %s", var1, this.val$to);
               Type[] var3 = var1.getActualTypeArguments();
               Type[] var4 = var2.getActualTypeArguments();
               Preconditions.checkArgument(var3.length == var4.length, "%s not compatible with %s", var1, var2);

               for(int var5 = 0; var5 < var3.length; ++var5) {
                  TypeResolver.access$100(this.val$mappings, var3[var5], var4[var5]);
               }

            }

            void visitGenericArrayType(GenericArrayType var1) {
               Type var2 = Types.getComponentType(this.val$to);
               Preconditions.checkArgument(var2 != null, "%s is not an array type.", this.val$to);
               TypeResolver.access$100(this.val$mappings, var1.getGenericComponentType(), var2);
            }

            void visitClass(Class var1) {
               throw new IllegalArgumentException("No type mapping from " + var1);
            }
         }).visit(new Type[]{var1});
      }
   }

   public Type resolveType(Type var1) {
      Preconditions.checkNotNull(var1);
      if (var1 instanceof TypeVariable) {
         return this.typeTable.resolve((TypeVariable)var1);
      } else if (var1 instanceof ParameterizedType) {
         return this.resolveParameterizedType((ParameterizedType)var1);
      } else if (var1 instanceof GenericArrayType) {
         return this.resolveGenericArrayType((GenericArrayType)var1);
      } else {
         return (Type)(var1 instanceof WildcardType ? this.resolveWildcardType((WildcardType)var1) : var1);
      }
   }

   private Type[] resolveTypes(Type[] var1) {
      Type[] var2 = new Type[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.resolveType(var1[var3]);
      }

      return var2;
   }

   private WildcardType resolveWildcardType(WildcardType var1) {
      Type[] var2 = var1.getLowerBounds();
      Type[] var3 = var1.getUpperBounds();
      return new Types.WildcardTypeImpl(this.resolveTypes(var2), this.resolveTypes(var3));
   }

   private Type resolveGenericArrayType(GenericArrayType var1) {
      Type var2 = var1.getGenericComponentType();
      Type var3 = this.resolveType(var2);
      return Types.newArrayType(var3);
   }

   private ParameterizedType resolveParameterizedType(ParameterizedType var1) {
      Type var2 = var1.getOwnerType();
      Type var3 = var2 == null ? null : this.resolveType(var2);
      Type var4 = this.resolveType(var1.getRawType());
      Type[] var5 = var1.getActualTypeArguments();
      Type[] var6 = this.resolveTypes(var5);
      return Types.newParameterizedTypeWithOwner(var3, (Class)var4, var6);
   }

   private static Object expectArgument(Class var0, Object var1) {
      try {
         return var0.cast(var1);
      } catch (ClassCastException var3) {
         throw new IllegalArgumentException(var1 + " is not a " + var0.getSimpleName());
      }
   }

   static Object access$000(Class var0, Object var1) {
      return expectArgument(var0, var1);
   }

   static void access$100(Map var0, Type var1, Type var2) {
      populateTypeMappings(var0, var1, var2);
   }

   TypeResolver(TypeResolver.TypeTable var1, Object var2) {
      this(var1);
   }

   static Type[] access$300(TypeResolver var0, Type[] var1) {
      return var0.resolveTypes(var1);
   }

   static final class TypeVariableKey {
      private final TypeVariable var;

      TypeVariableKey(TypeVariable var1) {
         this.var = (TypeVariable)Preconditions.checkNotNull(var1);
      }

      public int hashCode() {
         return Objects.hashCode(this.var.getGenericDeclaration(), this.var.getName());
      }

      public boolean equals(Object var1) {
         if (var1 instanceof TypeResolver.TypeVariableKey) {
            TypeResolver.TypeVariableKey var2 = (TypeResolver.TypeVariableKey)var1;
            return this.equalsTypeVariable(var2.var);
         } else {
            return false;
         }
      }

      public String toString() {
         return this.var.toString();
      }

      static Object forLookup(Type var0) {
         return var0 instanceof TypeVariable ? new TypeResolver.TypeVariableKey((TypeVariable)var0) : null;
      }

      boolean equalsType(Type var1) {
         return var1 instanceof TypeVariable ? this.equalsTypeVariable((TypeVariable)var1) : false;
      }

      private boolean equalsTypeVariable(TypeVariable var1) {
         return this.var.getGenericDeclaration().equals(var1.getGenericDeclaration()) && this.var.getName().equals(var1.getName());
      }
   }

   private static final class WildcardCapturer {
      private final AtomicInteger id;

      private WildcardCapturer() {
         this.id = new AtomicInteger();
      }

      Type capture(Type var1) {
         Preconditions.checkNotNull(var1);
         if (var1 instanceof Class) {
            return var1;
         } else if (var1 instanceof TypeVariable) {
            return var1;
         } else if (var1 instanceof GenericArrayType) {
            GenericArrayType var7 = (GenericArrayType)var1;
            return Types.newArrayType(this.capture(var7.getGenericComponentType()));
         } else if (var1 instanceof ParameterizedType) {
            ParameterizedType var6 = (ParameterizedType)var1;
            return Types.newParameterizedTypeWithOwner(this.captureNullable(var6.getOwnerType()), (Class)var6.getRawType(), this.capture(var6.getActualTypeArguments()));
         } else if (var1 instanceof WildcardType) {
            WildcardType var2 = (WildcardType)var1;
            Type[] var3 = var2.getLowerBounds();
            if (var3.length == 0) {
               Type[] var4 = var2.getUpperBounds();
               String var5 = "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join((Object[])var4);
               return Types.newArtificialTypeVariable(TypeResolver.WildcardCapturer.class, var5, var2.getUpperBounds());
            } else {
               return var1;
            }
         } else {
            throw new AssertionError("must have been one of the known types");
         }
      }

      private Type captureNullable(@Nullable Type var1) {
         return var1 == null ? null : this.capture(var1);
      }

      private Type[] capture(Type[] var1) {
         Type[] var2 = new Type[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = this.capture(var1[var3]);
         }

         return var2;
      }

      WildcardCapturer(Object var1) {
         this();
      }
   }

   private static final class TypeMappingIntrospector extends TypeVisitor {
      private static final TypeResolver.WildcardCapturer wildcardCapturer = new TypeResolver.WildcardCapturer();
      private final Map mappings = Maps.newHashMap();

      static ImmutableMap getTypeMappings(Type var0) {
         TypeResolver.TypeMappingIntrospector var1 = new TypeResolver.TypeMappingIntrospector();
         var1.visit(new Type[]{wildcardCapturer.capture(var0)});
         return ImmutableMap.copyOf(var1.mappings);
      }

      void visitClass(Class var1) {
         this.visit(new Type[]{var1.getGenericSuperclass()});
         this.visit(var1.getGenericInterfaces());
      }

      void visitParameterizedType(ParameterizedType var1) {
         Class var2 = (Class)var1.getRawType();
         TypeVariable[] var3 = var2.getTypeParameters();
         Type[] var4 = var1.getActualTypeArguments();
         Preconditions.checkState(var3.length == var4.length);

         for(int var5 = 0; var5 < var3.length; ++var5) {
            this.map(new TypeResolver.TypeVariableKey(var3[var5]), var4[var5]);
         }

         this.visit(new Type[]{var2});
         this.visit(new Type[]{var1.getOwnerType()});
      }

      void visitTypeVariable(TypeVariable var1) {
         this.visit(var1.getBounds());
      }

      void visitWildcardType(WildcardType var1) {
         this.visit(var1.getUpperBounds());
      }

      private void map(TypeResolver.TypeVariableKey var1, Type var2) {
         if (!this.mappings.containsKey(var1)) {
            for(Type var3 = var2; var3 != null; var3 = (Type)this.mappings.get(TypeResolver.TypeVariableKey.forLookup(var3))) {
               if (var1.equalsType(var3)) {
                  for(Type var4 = var2; var4 != null; var4 = (Type)this.mappings.remove(TypeResolver.TypeVariableKey.forLookup(var4))) {
                  }

                  return;
               }
            }

            this.mappings.put(var1, var2);
         }
      }
   }

   private static class TypeTable {
      private final ImmutableMap map;

      TypeTable() {
         this.map = ImmutableMap.of();
      }

      private TypeTable(ImmutableMap var1) {
         this.map = var1;
      }

      final TypeResolver.TypeTable where(Map var1) {
         ImmutableMap.Builder var2 = ImmutableMap.builder();
         var2.putAll(this.map);
         Iterator var3 = var1.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            TypeResolver.TypeVariableKey var5 = (TypeResolver.TypeVariableKey)var4.getKey();
            Type var6 = (Type)var4.getValue();
            Preconditions.checkArgument(!var5.equalsType(var6), "Type variable %s bound to itself", var5);
            var2.put(var5, var6);
         }

         return new TypeResolver.TypeTable(var2.build());
      }

      final Type resolve(TypeVariable var1) {
         TypeResolver.TypeTable var3 = new TypeResolver.TypeTable(this, var1, this) {
            final TypeVariable val$var;
            final TypeResolver.TypeTable val$unguarded;
            final TypeResolver.TypeTable this$0;

            {
               this.this$0 = var1;
               this.val$var = var2;
               this.val$unguarded = var3;
            }

            public Type resolveInternal(TypeVariable var1, TypeResolver.TypeTable var2) {
               return (Type)(var1.getGenericDeclaration().equals(this.val$var.getGenericDeclaration()) ? var1 : this.val$unguarded.resolveInternal(var1, var2));
            }
         };
         return this.resolveInternal(var1, var3);
      }

      Type resolveInternal(TypeVariable var1, TypeResolver.TypeTable var2) {
         Type var3 = (Type)this.map.get(new TypeResolver.TypeVariableKey(var1));
         if (var3 == null) {
            Type[] var4 = var1.getBounds();
            if (var4.length == 0) {
               return var1;
            } else {
               Type[] var5 = TypeResolver.access$300(new TypeResolver(var2), var4);
               return Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && Arrays.equals(var4, var5) ? var1 : Types.newArtificialTypeVariable(var1.getGenericDeclaration(), var1.getName(), var5);
            }
         } else {
            return (new TypeResolver(var2)).resolveType(var3);
         }
      }
   }
}
