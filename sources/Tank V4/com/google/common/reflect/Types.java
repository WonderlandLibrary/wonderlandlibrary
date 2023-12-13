package com.google.common.reflect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;

final class Types {
   private static final Function TYPE_TO_STRING = new Function() {
      public String apply(Type var1) {
         return Types.toString(var1);
      }

      public Object apply(Object var1) {
         return this.apply((Type)var1);
      }
   };
   private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");

   static Type newArrayType(Type var0) {
      if (var0 instanceof WildcardType) {
         WildcardType var1 = (WildcardType)var0;
         Type[] var2 = var1.getLowerBounds();
         Preconditions.checkArgument(var2.length <= 1, "Wildcard cannot have more than one lower bounds.");
         if (var2.length == 1) {
            return supertypeOf(newArrayType(var2[0]));
         } else {
            Type[] var3 = var1.getUpperBounds();
            Preconditions.checkArgument(var3.length == 1, "Wildcard should have only one upper bound.");
            return subtypeOf(newArrayType(var3[0]));
         }
      } else {
         return Types.JavaVersion.CURRENT.newArrayType(var0);
      }
   }

   static ParameterizedType newParameterizedTypeWithOwner(@Nullable Type var0, Class var1, Type... var2) {
      if (var0 == null) {
         return newParameterizedType(var1, var2);
      } else {
         Preconditions.checkNotNull(var2);
         Preconditions.checkArgument(var1.getEnclosingClass() != null, "Owner type for unenclosed %s", var1);
         return new Types.ParameterizedTypeImpl(var0, var1, var2);
      }
   }

   static ParameterizedType newParameterizedType(Class var0, Type... var1) {
      return new Types.ParameterizedTypeImpl(Types.ClassOwnership.JVM_BEHAVIOR.getOwnerType(var0), var0, var1);
   }

   static TypeVariable newArtificialTypeVariable(GenericDeclaration var0, String var1, Type... var2) {
      return new Types.TypeVariableImpl(var0, var1, var2.length == 0 ? new Type[]{Object.class} : var2);
   }

   @VisibleForTesting
   static WildcardType subtypeOf(Type var0) {
      return new Types.WildcardTypeImpl(new Type[0], new Type[]{var0});
   }

   @VisibleForTesting
   static WildcardType supertypeOf(Type var0) {
      return new Types.WildcardTypeImpl(new Type[]{var0}, new Type[]{Object.class});
   }

   static String toString(Type var0) {
      return var0 instanceof Class ? ((Class)var0).getName() : var0.toString();
   }

   @Nullable
   static Type getComponentType(Type var0) {
      Preconditions.checkNotNull(var0);
      AtomicReference var1 = new AtomicReference();
      (new TypeVisitor(var1) {
         final AtomicReference val$result;

         {
            this.val$result = var1;
         }

         void visitTypeVariable(TypeVariable var1) {
            this.val$result.set(Types.access$100(var1.getBounds()));
         }

         void visitWildcardType(WildcardType var1) {
            this.val$result.set(Types.access$100(var1.getUpperBounds()));
         }

         void visitGenericArrayType(GenericArrayType var1) {
            this.val$result.set(var1.getGenericComponentType());
         }

         void visitClass(Class var1) {
            this.val$result.set(var1.getComponentType());
         }
      }).visit(new Type[]{var0});
      return (Type)var1.get();
   }

   @Nullable
   private static Type subtypeOfComponentType(Type[] var0) {
      Type[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Type var4 = var1[var3];
         Type var5 = getComponentType(var4);
         if (var5 != null) {
            if (var5 instanceof Class) {
               Class var6 = (Class)var5;
               if (var6.isPrimitive()) {
                  return var6;
               }
            }

            return subtypeOf(var5);
         }
      }

      return null;
   }

   private static Type[] toArray(Collection var0) {
      return (Type[])var0.toArray(new Type[var0.size()]);
   }

   private static Iterable filterUpperBounds(Iterable var0) {
      return Iterables.filter(var0, Predicates.not(Predicates.equalTo(Object.class)));
   }

   private static void disallowPrimitiveType(Type[] var0, String var1) {
      Type[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type var5 = var2[var4];
         if (var5 instanceof Class) {
            Class var6 = (Class)var5;
            Preconditions.checkArgument(!var6.isPrimitive(), "Primitive type '%s' used as %s", var6, var1);
         }
      }

   }

   static Class getArrayClass(Class var0) {
      return Array.newInstance(var0, 0).getClass();
   }

   private Types() {
   }

   static Type access$100(Type[] var0) {
      return subtypeOfComponentType(var0);
   }

   static void access$200(Type[] var0, String var1) {
      disallowPrimitiveType(var0, var1);
   }

   static Type[] access$300(Collection var0) {
      return toArray(var0);
   }

   static Function access$400() {
      return TYPE_TO_STRING;
   }

   static Joiner access$500() {
      return COMMA_JOINER;
   }

   static Iterable access$600(Iterable var0) {
      return filterUpperBounds(var0);
   }

   static final class NativeTypeVariableEquals {
      static final boolean NATIVE_TYPE_VARIABLE_ONLY = !Types.NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(Types.NativeTypeVariableEquals.class, "X"));
   }

   static enum JavaVersion {
      JAVA6 {
         GenericArrayType newArrayType(Type var1) {
            return new Types.GenericArrayTypeImpl(var1);
         }

         Type usedInGenericType(Type var1) {
            Preconditions.checkNotNull(var1);
            if (var1 instanceof Class) {
               Class var2 = (Class)var1;
               if (var2.isArray()) {
                  return new Types.GenericArrayTypeImpl(var2.getComponentType());
               }
            }

            return var1;
         }

         Type newArrayType(Type var1) {
            return this.newArrayType(var1);
         }
      },
      JAVA7 {
         Type newArrayType(Type var1) {
            return (Type)(var1 instanceof Class ? Types.getArrayClass((Class)var1) : new Types.GenericArrayTypeImpl(var1));
         }

         Type usedInGenericType(Type var1) {
            return (Type)Preconditions.checkNotNull(var1);
         }
      };

      static final Types.JavaVersion CURRENT = (new TypeCapture() {
      }).capture() instanceof Class ? JAVA7 : JAVA6;
      private static final Types.JavaVersion[] $VALUES = new Types.JavaVersion[]{JAVA6, JAVA7};

      private JavaVersion() {
      }

      abstract Type newArrayType(Type var1);

      abstract Type usedInGenericType(Type var1);

      final ImmutableList usedInGenericType(Type[] var1) {
         ImmutableList.Builder var2 = ImmutableList.builder();
         Type[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Type var6 = var3[var5];
            var2.add((Object)this.usedInGenericType(var6));
         }

         return var2.build();
      }

      JavaVersion(Object var3) {
         this();
      }
   }

   static final class WildcardTypeImpl implements WildcardType, Serializable {
      private final ImmutableList lowerBounds;
      private final ImmutableList upperBounds;
      private static final long serialVersionUID = 0L;

      WildcardTypeImpl(Type[] var1, Type[] var2) {
         Types.access$200(var1, "lower bound for wildcard");
         Types.access$200(var2, "upper bound for wildcard");
         this.lowerBounds = Types.JavaVersion.CURRENT.usedInGenericType(var1);
         this.upperBounds = Types.JavaVersion.CURRENT.usedInGenericType(var2);
      }

      public Type[] getLowerBounds() {
         return Types.access$300(this.lowerBounds);
      }

      public Type[] getUpperBounds() {
         return Types.access$300(this.upperBounds);
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof WildcardType)) {
            return false;
         } else {
            WildcardType var2 = (WildcardType)var1;
            return this.lowerBounds.equals(Arrays.asList(var2.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(var2.getUpperBounds()));
         }
      }

      public int hashCode() {
         return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("?");
         Iterator var2 = this.lowerBounds.iterator();

         Type var3;
         while(var2.hasNext()) {
            var3 = (Type)var2.next();
            var1.append(" super ").append(Types.toString(var3));
         }

         var2 = Types.access$600(this.upperBounds).iterator();

         while(var2.hasNext()) {
            var3 = (Type)var2.next();
            var1.append(" extends ").append(Types.toString(var3));
         }

         return var1.toString();
      }
   }

   private static final class TypeVariableImpl implements TypeVariable {
      private final GenericDeclaration genericDeclaration;
      private final String name;
      private final ImmutableList bounds;

      TypeVariableImpl(GenericDeclaration var1, String var2, Type[] var3) {
         Types.access$200(var3, "bound for type variable");
         this.genericDeclaration = (GenericDeclaration)Preconditions.checkNotNull(var1);
         this.name = (String)Preconditions.checkNotNull(var2);
         this.bounds = ImmutableList.copyOf((Object[])var3);
      }

      public Type[] getBounds() {
         return Types.access$300(this.bounds);
      }

      public GenericDeclaration getGenericDeclaration() {
         return this.genericDeclaration;
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.name;
      }

      public int hashCode() {
         return this.genericDeclaration.hashCode() ^ this.name.hashCode();
      }

      public boolean equals(Object var1) {
         if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
            if (!(var1 instanceof Types.TypeVariableImpl)) {
               return false;
            } else {
               Types.TypeVariableImpl var3 = (Types.TypeVariableImpl)var1;
               return this.name.equals(var3.getName()) && this.genericDeclaration.equals(var3.getGenericDeclaration()) && this.bounds.equals(var3.bounds);
            }
         } else if (!(var1 instanceof TypeVariable)) {
            return false;
         } else {
            TypeVariable var2 = (TypeVariable)var1;
            return this.name.equals(var2.getName()) && this.genericDeclaration.equals(var2.getGenericDeclaration());
         }
      }
   }

   private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
      private final Type ownerType;
      private final ImmutableList argumentsList;
      private final Class rawType;
      private static final long serialVersionUID = 0L;

      ParameterizedTypeImpl(@Nullable Type var1, Class var2, Type[] var3) {
         Preconditions.checkNotNull(var2);
         Preconditions.checkArgument(var3.length == var2.getTypeParameters().length);
         Types.access$200(var3, "type parameter");
         this.ownerType = var1;
         this.rawType = var2;
         this.argumentsList = Types.JavaVersion.CURRENT.usedInGenericType(var3);
      }

      public Type[] getActualTypeArguments() {
         return Types.access$300(this.argumentsList);
      }

      public Type getRawType() {
         return this.rawType;
      }

      public Type getOwnerType() {
         return this.ownerType;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         if (this.ownerType != null) {
            var1.append(Types.toString(this.ownerType)).append('.');
         }

         var1.append(this.rawType.getName()).append('<').append(Types.access$500().join(Iterables.transform(this.argumentsList, Types.access$400()))).append('>');
         return var1.toString();
      }

      public int hashCode() {
         return (this.ownerType == null ? 0 : this.ownerType.hashCode()) ^ this.argumentsList.hashCode() ^ this.rawType.hashCode();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof ParameterizedType)) {
            return false;
         } else {
            ParameterizedType var2 = (ParameterizedType)var1;
            return this.getRawType().equals(var2.getRawType()) && Objects.equal(this.getOwnerType(), var2.getOwnerType()) && Arrays.equals(this.getActualTypeArguments(), var2.getActualTypeArguments());
         }
      }
   }

   private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
      private final Type componentType;
      private static final long serialVersionUID = 0L;

      GenericArrayTypeImpl(Type var1) {
         this.componentType = Types.JavaVersion.CURRENT.usedInGenericType(var1);
      }

      public Type getGenericComponentType() {
         return this.componentType;
      }

      public String toString() {
         return Types.toString(this.componentType) + "[]";
      }

      public int hashCode() {
         return this.componentType.hashCode();
      }

      public boolean equals(Object var1) {
         if (var1 instanceof GenericArrayType) {
            GenericArrayType var2 = (GenericArrayType)var1;
            return Objects.equal(this.getGenericComponentType(), var2.getGenericComponentType());
         } else {
            return false;
         }
      }
   }

   private static enum ClassOwnership {
      OWNED_BY_ENCLOSING_CLASS {
         @Nullable
         Class getOwnerType(Class var1) {
            return var1.getEnclosingClass();
         }
      },
      LOCAL_CLASS_HAS_NO_OWNER {
         @Nullable
         Class getOwnerType(Class var1) {
            return var1.isLocalClass() ? null : var1.getEnclosingClass();
         }
      };

      static final Types.ClassOwnership JVM_BEHAVIOR = detectJvmBehavior();
      private static final Types.ClassOwnership[] $VALUES = new Types.ClassOwnership[]{OWNED_BY_ENCLOSING_CLASS, LOCAL_CLASS_HAS_NO_OWNER};

      private ClassOwnership() {
      }

      @Nullable
      abstract Class getOwnerType(Class var1);

      private static Types.ClassOwnership detectJvmBehavior() {
         Class var0 = (new LocalClass() {
         }).getClass();
         ParameterizedType var1 = (ParameterizedType)var0.getGenericSuperclass();
         Types.ClassOwnership[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Types.ClassOwnership var5 = var2[var4];
            if (var5.getOwnerType(LocalClass.class) == var1.getOwnerType()) {
               return var5;
            }
         }

         throw new AssertionError();
      }

      ClassOwnership(Object var3) {
         this();
      }
   }
}
