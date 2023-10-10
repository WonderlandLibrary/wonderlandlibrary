package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Primitives;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class TypeToken extends TypeCapture implements Serializable {
   private final Type runtimeType;
   private transient TypeResolver typeResolver;

   protected TypeToken() {
      this.runtimeType = this.capture();
      Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
   }

   protected TypeToken(Class var1) {
      Type var2 = super.capture();
      if (var2 instanceof Class) {
         this.runtimeType = var2;
      } else {
         this.runtimeType = of(var1).resolveType(var2).runtimeType;
      }

   }

   private TypeToken(Type var1) {
      this.runtimeType = (Type)Preconditions.checkNotNull(var1);
   }

   public static TypeToken of(Class var0) {
      return new TypeToken.SimpleTypeToken(var0);
   }

   public static TypeToken of(Type var0) {
      return new TypeToken.SimpleTypeToken(var0);
   }

   public final Class getRawType() {
      Class var1 = getRawType(this.runtimeType);
      return var1;
   }

   private ImmutableSet getImmediateRawTypes() {
      ImmutableSet var1 = getRawTypes(this.runtimeType);
      return var1;
   }

   public final Type getType() {
      return this.runtimeType;
   }

   public final TypeToken where(TypeParameter var1, TypeToken var2) {
      TypeResolver var3 = (new TypeResolver()).where(ImmutableMap.of(new TypeResolver.TypeVariableKey(var1.typeVariable), var2.runtimeType));
      return new TypeToken.SimpleTypeToken(var3.resolveType(this.runtimeType));
   }

   public final TypeToken where(TypeParameter var1, Class var2) {
      return this.where(var1, of(var2));
   }

   public final TypeToken resolveType(Type var1) {
      Preconditions.checkNotNull(var1);
      TypeResolver var2 = this.typeResolver;
      if (var2 == null) {
         var2 = this.typeResolver = TypeResolver.accordingTo(this.runtimeType);
      }

      return of(var2.resolveType(var1));
   }

   private Type[] resolveInPlace(Type[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = this.resolveType(var1[var2]).getType();
      }

      return var1;
   }

   private TypeToken resolveSupertype(Type var1) {
      TypeToken var2 = this.resolveType(var1);
      var2.typeResolver = this.typeResolver;
      return var2;
   }

   @Nullable
   final TypeToken getGenericSuperclass() {
      if (this.runtimeType instanceof TypeVariable) {
         return this.boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
      } else if (this.runtimeType instanceof WildcardType) {
         return this.boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
      } else {
         Type var1 = this.getRawType().getGenericSuperclass();
         if (var1 == null) {
            return null;
         } else {
            TypeToken var2 = this.resolveSupertype(var1);
            return var2;
         }
      }
   }

   @Nullable
   private TypeToken boundAsSuperclass(Type var1) {
      TypeToken var2 = of(var1);
      return var2.getRawType().isInterface() ? null : var2;
   }

   final ImmutableList getGenericInterfaces() {
      if (this.runtimeType instanceof TypeVariable) {
         return this.boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
      } else if (this.runtimeType instanceof WildcardType) {
         return this.boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
      } else {
         ImmutableList.Builder var1 = ImmutableList.builder();
         Type[] var2 = this.getRawType().getGenericInterfaces();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Type var5 = var2[var4];
            TypeToken var6 = this.resolveSupertype(var5);
            var1.add((Object)var6);
         }

         return var1.build();
      }
   }

   private ImmutableList boundsAsInterfaces(Type[] var1) {
      ImmutableList.Builder var2 = ImmutableList.builder();
      Type[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Type var6 = var3[var5];
         TypeToken var7 = of(var6);
         if (var7.getRawType().isInterface()) {
            var2.add((Object)var7);
         }
      }

      return var2.build();
   }

   public final TypeToken.TypeSet getTypes() {
      return new TypeToken.TypeSet(this);
   }

   public final TypeToken getSupertype(Class var1) {
      Preconditions.checkArgument(var1.isAssignableFrom(this.getRawType()), "%s is not a super class of %s", var1, this);
      if (this.runtimeType instanceof TypeVariable) {
         return this.getSupertypeFromUpperBounds(var1, ((TypeVariable)this.runtimeType).getBounds());
      } else if (this.runtimeType instanceof WildcardType) {
         return this.getSupertypeFromUpperBounds(var1, ((WildcardType)this.runtimeType).getUpperBounds());
      } else if (var1.isArray()) {
         return this.getArraySupertype(var1);
      } else {
         TypeToken var2 = this.resolveSupertype(toGenericType(var1).runtimeType);
         return var2;
      }
   }

   public final TypeToken getSubtype(Class var1) {
      Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
      if (this.runtimeType instanceof WildcardType) {
         return this.getSubtypeFromLowerBounds(var1, ((WildcardType)this.runtimeType).getLowerBounds());
      } else {
         Preconditions.checkArgument(this.getRawType().isAssignableFrom(var1), "%s isn't a subclass of %s", var1, this);
         if (this != null) {
            return this.getArraySubtype(var1);
         } else {
            TypeToken var2 = of(this.resolveTypeArgsForSubclass(var1));
            return var2;
         }
      }
   }

   public final boolean isAssignableFrom(TypeToken var1) {
      return this.isAssignableFrom(var1.runtimeType);
   }

   public final boolean isAssignableFrom(Type var1) {
      return isAssignable((Type)Preconditions.checkNotNull(var1), this.runtimeType);
   }

   public final TypeToken wrap() {
      if (this != false) {
         Class var1 = (Class)this.runtimeType;
         return of(Primitives.wrap(var1));
      } else {
         return this;
      }
   }

   private boolean isWrapper() {
      return Primitives.allWrapperTypes().contains(this.runtimeType);
   }

   public final TypeToken unwrap() {
      if (this.isWrapper()) {
         Class var1 = (Class)this.runtimeType;
         return of(Primitives.unwrap(var1));
      } else {
         return this;
      }
   }

   @Nullable
   public final TypeToken getComponentType() {
      Type var1 = Types.getComponentType(this.runtimeType);
      return var1 == null ? null : of(var1);
   }

   public final Invokable method(Method var1) {
      Preconditions.checkArgument(of(var1.getDeclaringClass()).isAssignableFrom(this), "%s not declared by %s", var1, this);
      return new Invokable.MethodInvokable(this, var1) {
         final TypeToken this$0;

         {
            this.this$0 = var1;
         }

         Type getGenericReturnType() {
            return this.this$0.resolveType(super.getGenericReturnType()).getType();
         }

         Type[] getGenericParameterTypes() {
            return TypeToken.access$000(this.this$0, super.getGenericParameterTypes());
         }

         Type[] getGenericExceptionTypes() {
            return TypeToken.access$000(this.this$0, super.getGenericExceptionTypes());
         }

         public TypeToken getOwnerType() {
            return this.this$0;
         }

         public String toString() {
            return this.getOwnerType() + "." + super.toString();
         }
      };
   }

   public final Invokable constructor(Constructor var1) {
      Preconditions.checkArgument(var1.getDeclaringClass() == this.getRawType(), "%s not declared by %s", var1, this.getRawType());
      return new Invokable.ConstructorInvokable(this, var1) {
         final TypeToken this$0;

         {
            this.this$0 = var1;
         }

         Type getGenericReturnType() {
            return this.this$0.resolveType(super.getGenericReturnType()).getType();
         }

         Type[] getGenericParameterTypes() {
            return TypeToken.access$000(this.this$0, super.getGenericParameterTypes());
         }

         Type[] getGenericExceptionTypes() {
            return TypeToken.access$000(this.this$0, super.getGenericExceptionTypes());
         }

         public TypeToken getOwnerType() {
            return this.this$0;
         }

         public String toString() {
            return this.getOwnerType() + "(" + Joiner.on(", ").join((Object[])this.getGenericParameterTypes()) + ")";
         }
      };
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof TypeToken) {
         TypeToken var2 = (TypeToken)var1;
         return this.runtimeType.equals(var2.runtimeType);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.runtimeType.hashCode();
   }

   public String toString() {
      return Types.toString(this.runtimeType);
   }

   protected Object writeReplace() {
      return of((new TypeResolver()).resolveType(this.runtimeType));
   }

   final TypeToken rejectTypeVariables() {
      (new TypeVisitor(this) {
         final TypeToken this$0;

         {
            this.this$0 = var1;
         }

         void visitTypeVariable(TypeVariable var1) {
            throw new IllegalArgumentException(TypeToken.access$400(this.this$0) + "contains a type variable and is not safe for the operation");
         }

         void visitWildcardType(WildcardType var1) {
            this.visit(var1.getLowerBounds());
            this.visit(var1.getUpperBounds());
         }

         void visitParameterizedType(ParameterizedType var1) {
            this.visit(var1.getActualTypeArguments());
            this.visit(new Type[]{var1.getOwnerType()});
         }

         void visitGenericArrayType(GenericArrayType var1) {
            this.visit(new Type[]{var1.getGenericComponentType()});
         }
      }).visit(new Type[]{this.runtimeType});
      return this;
   }

   private static boolean isAssignableFromAny(Type[] var0, Type var1) {
      Type[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type var5 = var2[var4];
         if (var1 != false) {
            return true;
         }
      }

      return false;
   }

   private static boolean isAssignableToClass(Type var0, Class var1) {
      return var1.isAssignableFrom(getRawType(var0));
   }

   private static boolean isAssignableToWildcardType(Type var0, WildcardType var1) {
      Type var10001;
      if (supertypeBound(var1) != false) {
         var10001 = var0;
         if (var1 == null) {
            boolean var10002 = true;
            return (boolean)var10001;
         }
      }

      var10001 = false;
      return (boolean)var10001;
   }

   private static boolean isAssignableToParameterizedType(Type var0, ParameterizedType var1) {
      Class var2 = getRawType(var1);
      if (!var2.isAssignableFrom(getRawType(var0))) {
         return false;
      } else {
         TypeVariable[] var3 = var2.getTypeParameters();
         Type[] var4 = var1.getActualTypeArguments();
         TypeToken var5 = of(var0);

         for(int var6 = 0; var6 < var3.length; ++var6) {
            Type var7 = var5.resolveType(var3[var6]).runtimeType;
            if (var4[var6] != false) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean isAssignableToGenericArrayType(Type var0, GenericArrayType var1) {
      if (var0 instanceof Class) {
         Class var3 = (Class)var0;
         return !var3.isArray() ? false : isAssignable(var3.getComponentType(), var1.getGenericComponentType());
      } else if (var0 instanceof GenericArrayType) {
         GenericArrayType var2 = (GenericArrayType)var0;
         return isAssignable(var2.getGenericComponentType(), var1.getGenericComponentType());
      } else {
         return false;
      }
   }

   private static boolean isAssignableFromGenericArrayType(GenericArrayType var0, Type var1) {
      if (var1 instanceof Class) {
         Class var3 = (Class)var1;
         if (!var3.isArray()) {
            return var3 == Object.class;
         } else {
            return isAssignable(var0.getGenericComponentType(), var3.getComponentType());
         }
      } else if (var1 instanceof GenericArrayType) {
         GenericArrayType var2 = (GenericArrayType)var1;
         return isAssignable(var0.getGenericComponentType(), var2.getGenericComponentType());
      } else {
         return false;
      }
   }

   private static Type supertypeBound(Type var0) {
      return var0 instanceof WildcardType ? supertypeBound((WildcardType)var0) : var0;
   }

   private static Type supertypeBound(WildcardType var0) {
      Type[] var1 = var0.getUpperBounds();
      if (var1.length == 1) {
         return supertypeBound(var1[0]);
      } else if (var1.length == 0) {
         return Object.class;
      } else {
         throw new AssertionError("There should be at most one upper bound for wildcard type: " + var0);
      }
   }

   @Nullable
   private static Type subtypeBound(Type var0) {
      return var0 instanceof WildcardType ? subtypeBound((WildcardType)var0) : var0;
   }

   @Nullable
   private static Type subtypeBound(WildcardType var0) {
      Type[] var1 = var0.getLowerBounds();
      if (var1.length == 1) {
         return subtypeBound(var1[0]);
      } else if (var1.length == 0) {
         return null;
      } else {
         throw new AssertionError("Wildcard should have at most one lower bound: " + var0);
      }
   }

   @VisibleForTesting
   static Class getRawType(Type var0) {
      return (Class)getRawTypes(var0).iterator().next();
   }

   @VisibleForTesting
   static ImmutableSet getRawTypes(Type var0) {
      Preconditions.checkNotNull(var0);
      ImmutableSet.Builder var1 = ImmutableSet.builder();
      (new TypeVisitor(var1) {
         final ImmutableSet.Builder val$builder;

         {
            this.val$builder = var1;
         }

         void visitTypeVariable(TypeVariable var1) {
            this.visit(var1.getBounds());
         }

         void visitWildcardType(WildcardType var1) {
            this.visit(var1.getUpperBounds());
         }

         void visitParameterizedType(ParameterizedType var1) {
            this.val$builder.add((Object)((Class)var1.getRawType()));
         }

         void visitClass(Class var1) {
            this.val$builder.add((Object)var1);
         }

         void visitGenericArrayType(GenericArrayType var1) {
            this.val$builder.add((Object)Types.getArrayClass(TypeToken.getRawType(var1.getGenericComponentType())));
         }
      }).visit(new Type[]{var0});
      return var1.build();
   }

   @VisibleForTesting
   static TypeToken toGenericType(Class var0) {
      TypeToken var2;
      if (var0.isArray()) {
         Type var3 = Types.newArrayType(toGenericType(var0.getComponentType()).runtimeType);
         var2 = of(var3);
         return var2;
      } else {
         TypeVariable[] var1 = var0.getTypeParameters();
         if (var1.length > 0) {
            var2 = of((Type)Types.newParameterizedType(var0, var1));
            return var2;
         } else {
            return of(var0);
         }
      }
   }

   private TypeToken getSupertypeFromUpperBounds(Class var1, Type[] var2) {
      Type[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Type var6 = var3[var5];
         TypeToken var7 = of(var6);
         if (of(var1).isAssignableFrom(var7)) {
            TypeToken var8 = var7.getSupertype(var1);
            return var8;
         }
      }

      throw new IllegalArgumentException(var1 + " isn't a super type of " + this);
   }

   private TypeToken getSubtypeFromLowerBounds(Class var1, Type[] var2) {
      int var4 = var2.length;
      byte var5 = 0;
      if (var5 < var4) {
         Type var6 = var2[var5];
         TypeToken var7 = of(var6);
         return var7.getSubtype(var1);
      } else {
         throw new IllegalArgumentException(var1 + " isn't a subclass of " + this);
      }
   }

   private TypeToken getArraySupertype(Class var1) {
      TypeToken var2 = (TypeToken)Preconditions.checkNotNull(this.getComponentType(), "%s isn't a super type of %s", var1, this);
      TypeToken var3 = var2.getSupertype(var1.getComponentType());
      TypeToken var4 = of(newArrayClassOrGenericArrayType(var3.runtimeType));
      return var4;
   }

   private TypeToken getArraySubtype(Class var1) {
      TypeToken var2 = this.getComponentType().getSubtype(var1.getComponentType());
      TypeToken var3 = of(newArrayClassOrGenericArrayType(var2.runtimeType));
      return var3;
   }

   private Type resolveTypeArgsForSubclass(Class var1) {
      if (this.runtimeType instanceof Class) {
         return var1;
      } else {
         TypeToken var2 = toGenericType(var1);
         Type var3 = var2.getSupertype(this.getRawType()).runtimeType;
         return (new TypeResolver()).where(var3, this.runtimeType).resolveType(var2.runtimeType);
      }
   }

   private static Type newArrayClassOrGenericArrayType(Type var0) {
      return Types.JavaVersion.JAVA7.newArrayType(var0);
   }

   static Type[] access$000(TypeToken var0, Type[] var1) {
      return var0.resolveInPlace(var1);
   }

   static ImmutableSet access$200(TypeToken var0) {
      return var0.getImmediateRawTypes();
   }

   static Type access$400(TypeToken var0) {
      return var0.runtimeType;
   }

   TypeToken(Type var1, Object var2) {
      this(var1);
   }

   private abstract static class TypeCollector {
      static final TypeToken.TypeCollector FOR_GENERIC_TYPE = new TypeToken.TypeCollector() {
         Class getRawType(TypeToken var1) {
            return var1.getRawType();
         }

         Iterable getInterfaces(TypeToken var1) {
            return var1.getGenericInterfaces();
         }

         @Nullable
         TypeToken getSuperclass(TypeToken var1) {
            return var1.getGenericSuperclass();
         }

         Object getSuperclass(Object var1) {
            return this.getSuperclass((TypeToken)var1);
         }

         Iterable getInterfaces(Object var1) {
            return this.getInterfaces((TypeToken)var1);
         }

         Class getRawType(Object var1) {
            return this.getRawType((TypeToken)var1);
         }
      };
      static final TypeToken.TypeCollector FOR_RAW_TYPE = new TypeToken.TypeCollector() {
         Class getRawType(Class var1) {
            return var1;
         }

         Iterable getInterfaces(Class var1) {
            return Arrays.asList(var1.getInterfaces());
         }

         @Nullable
         Class getSuperclass(Class var1) {
            return var1.getSuperclass();
         }

         Object getSuperclass(Object var1) {
            return this.getSuperclass((Class)var1);
         }

         Iterable getInterfaces(Object var1) {
            return this.getInterfaces((Class)var1);
         }

         Class getRawType(Object var1) {
            return this.getRawType((Class)var1);
         }
      };

      private TypeCollector() {
      }

      final TypeToken.TypeCollector classesOnly() {
         return new TypeToken.TypeCollector.ForwardingTypeCollector(this, this) {
            final TypeToken.TypeCollector this$0;

            {
               this.this$0 = var1;
            }

            Iterable getInterfaces(Object var1) {
               return ImmutableSet.of();
            }

            ImmutableList collectTypes(Iterable var1) {
               ImmutableList.Builder var2 = ImmutableList.builder();
               Iterator var3 = var1.iterator();

               while(var3.hasNext()) {
                  Object var4 = var3.next();
                  if (!this.getRawType(var4).isInterface()) {
                     var2.add(var4);
                  }
               }

               return super.collectTypes(var2.build());
            }
         };
      }

      final ImmutableList collectTypes(Object var1) {
         return this.collectTypes((Iterable)ImmutableList.of(var1));
      }

      ImmutableList collectTypes(Iterable var1) {
         HashMap var2 = Maps.newHashMap();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            this.collectTypes(var4, var2);
         }

         return sortKeysByValue(var2, Ordering.natural().reverse());
      }

      private int collectTypes(Object var1, Map var2) {
         Integer var3 = (Integer)var2.get(this);
         if (var3 != null) {
            return var3;
         } else {
            int var4 = this.getRawType(var1).isInterface() ? 1 : 0;

            Object var6;
            for(Iterator var5 = this.getInterfaces(var1).iterator(); var5.hasNext(); var4 = Math.max(var4, this.collectTypes(var6, var2))) {
               var6 = var5.next();
            }

            Object var7 = this.getSuperclass(var1);
            if (var7 != null) {
               var4 = Math.max(var4, this.collectTypes(var7, var2));
            }

            var2.put(var1, var4 + 1);
            return var4 + 1;
         }
      }

      private static ImmutableList sortKeysByValue(Map var0, Comparator var1) {
         Ordering var2 = new Ordering(var1, var0) {
            final Comparator val$valueComparator;
            final Map val$map;

            {
               this.val$valueComparator = var1;
               this.val$map = var2;
            }

            public int compare(Object var1, Object var2) {
               return this.val$valueComparator.compare(this.val$map.get(var1), this.val$map.get(var2));
            }
         };
         return var2.immutableSortedCopy(var0.keySet());
      }

      abstract Class getRawType(Object var1);

      abstract Iterable getInterfaces(Object var1);

      @Nullable
      abstract Object getSuperclass(Object var1);

      TypeCollector(Object var1) {
         this();
      }

      private static class ForwardingTypeCollector extends TypeToken.TypeCollector {
         private final TypeToken.TypeCollector delegate;

         ForwardingTypeCollector(TypeToken.TypeCollector var1) {
            super(null);
            this.delegate = var1;
         }

         Class getRawType(Object var1) {
            return this.delegate.getRawType(var1);
         }

         Iterable getInterfaces(Object var1) {
            return this.delegate.getInterfaces(var1);
         }

         Object getSuperclass(Object var1) {
            return this.delegate.getSuperclass(var1);
         }
      }
   }

   private static final class SimpleTypeToken extends TypeToken {
      private static final long serialVersionUID = 0L;

      SimpleTypeToken(Type var1) {
         super(var1, null);
      }
   }

   private static enum TypeFilter implements Predicate {
      IGNORE_TYPE_VARIABLE_OR_WILDCARD {
         public boolean apply(TypeToken var1) {
            return !(TypeToken.access$400(var1) instanceof TypeVariable) && !(TypeToken.access$400(var1) instanceof WildcardType);
         }

         public boolean apply(Object var1) {
            return this.apply((TypeToken)var1);
         }
      },
      INTERFACE_ONLY {
         public boolean apply(TypeToken var1) {
            return var1.getRawType().isInterface();
         }

         public boolean apply(Object var1) {
            return this.apply((TypeToken)var1);
         }
      };

      private static final TypeToken.TypeFilter[] $VALUES = new TypeToken.TypeFilter[]{IGNORE_TYPE_VARIABLE_OR_WILDCARD, INTERFACE_ONLY};

      private TypeFilter() {
      }

      TypeFilter(Object var3) {
         this();
      }
   }

   private final class ClassSet extends TypeToken.TypeSet {
      private transient ImmutableSet classes;
      private static final long serialVersionUID = 0L;
      final TypeToken this$0;

      private ClassSet(TypeToken var1) {
         super(var1);
         this.this$0 = var1;
      }

      protected Set delegate() {
         ImmutableSet var1 = this.classes;
         if (var1 == null) {
            ImmutableList var2 = TypeToken.TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes((Object)this.this$0);
            return this.classes = FluentIterable.from((Iterable)var2).filter((Predicate)TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
         } else {
            return var1;
         }
      }

      public TypeToken.TypeSet classes() {
         return this;
      }

      public Set rawTypes() {
         ImmutableList var1 = TypeToken.TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes((Iterable)TypeToken.access$200(this.this$0));
         return ImmutableSet.copyOf((Collection)var1);
      }

      public TypeToken.TypeSet interfaces() {
         throw new UnsupportedOperationException("classes().interfaces() not supported.");
      }

      private Object readResolve() {
         return this.this$0.getTypes().classes();
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }

      ClassSet(TypeToken var1, Object var2) {
         this(var1);
      }
   }

   private final class InterfaceSet extends TypeToken.TypeSet {
      private final transient TypeToken.TypeSet allTypes;
      private transient ImmutableSet interfaces;
      private static final long serialVersionUID = 0L;
      final TypeToken this$0;

      InterfaceSet(TypeToken var1, TypeToken.TypeSet var2) {
         super(var1);
         this.this$0 = var1;
         this.allTypes = var2;
      }

      protected Set delegate() {
         ImmutableSet var1 = this.interfaces;
         return var1 == null ? (this.interfaces = FluentIterable.from((Iterable)this.allTypes).filter((Predicate)TypeToken.TypeFilter.INTERFACE_ONLY).toSet()) : var1;
      }

      public TypeToken.TypeSet interfaces() {
         return this;
      }

      public Set rawTypes() {
         ImmutableList var1 = TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable)TypeToken.access$200(this.this$0));
         return FluentIterable.from((Iterable)var1).filter(new Predicate(this) {
            final TypeToken.InterfaceSet this$1;

            {
               this.this$1 = var1;
            }

            public boolean apply(Class var1) {
               return var1.isInterface();
            }

            public boolean apply(Object var1) {
               return this.apply((Class)var1);
            }
         }).toSet();
      }

      public TypeToken.TypeSet classes() {
         throw new UnsupportedOperationException("interfaces().classes() not supported.");
      }

      private Object readResolve() {
         return this.this$0.getTypes().interfaces();
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   public class TypeSet extends ForwardingSet implements Serializable {
      private transient ImmutableSet types;
      private static final long serialVersionUID = 0L;
      final TypeToken this$0;

      TypeSet(TypeToken var1) {
         this.this$0 = var1;
      }

      public TypeToken.TypeSet interfaces() {
         return this.this$0.new InterfaceSet(this.this$0, this);
      }

      public TypeToken.TypeSet classes() {
         return this.this$0.new ClassSet(this.this$0);
      }

      protected Set delegate() {
         ImmutableSet var1 = this.types;
         if (var1 == null) {
            ImmutableList var2 = TypeToken.TypeCollector.FOR_GENERIC_TYPE.collectTypes((Object)this.this$0);
            return this.types = FluentIterable.from((Iterable)var2).filter((Predicate)TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
         } else {
            return var1;
         }
      }

      public Set rawTypes() {
         ImmutableList var1 = TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable)TypeToken.access$200(this.this$0));
         return ImmutableSet.copyOf((Collection)var1);
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
