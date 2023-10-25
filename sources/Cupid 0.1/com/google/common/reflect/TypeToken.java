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
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable {
  private final Type runtimeType;
  
  private transient TypeResolver typeResolver;
  
  protected TypeToken() {
    this.runtimeType = capture();
    Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", new Object[] { this.runtimeType });
  }
  
  protected TypeToken(Class<?> declaringClass) {
    Type captured = capture();
    if (captured instanceof Class) {
      this.runtimeType = captured;
    } else {
      this.runtimeType = (of((Class)declaringClass).resolveType(captured)).runtimeType;
    } 
  }
  
  private TypeToken(Type type) {
    this.runtimeType = (Type)Preconditions.checkNotNull(type);
  }
  
  public static <T> TypeToken<T> of(Class<T> type) {
    return new SimpleTypeToken<T>(type);
  }
  
  public static TypeToken<?> of(Type type) {
    return new SimpleTypeToken(type);
  }
  
  public final Class<? super T> getRawType() {
    Class<?> rawType = getRawType(this.runtimeType);
    Class<? super T> result = (Class)rawType;
    return result;
  }
  
  private ImmutableSet<Class<? super T>> getImmediateRawTypes() {
    ImmutableSet<Class<? super T>> result = (ImmutableSet)getRawTypes(this.runtimeType);
    return result;
  }
  
  public final Type getType() {
    return this.runtimeType;
  }
  
  public final <X> TypeToken<T> where(TypeParameter<X> typeParam, TypeToken<X> typeArg) {
    TypeResolver resolver = (new TypeResolver()).where((Map<TypeResolver.TypeVariableKey, ? extends Type>)ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
    return new SimpleTypeToken<T>(resolver.resolveType(this.runtimeType));
  }
  
  public final <X> TypeToken<T> where(TypeParameter<X> typeParam, Class<X> typeArg) {
    return where(typeParam, of(typeArg));
  }
  
  public final TypeToken<?> resolveType(Type type) {
    Preconditions.checkNotNull(type);
    TypeResolver resolver = this.typeResolver;
    if (resolver == null)
      resolver = this.typeResolver = TypeResolver.accordingTo(this.runtimeType); 
    return of(resolver.resolveType(type));
  }
  
  private Type[] resolveInPlace(Type[] types) {
    for (int i = 0; i < types.length; i++)
      types[i] = resolveType(types[i]).getType(); 
    return types;
  }
  
  private TypeToken<?> resolveSupertype(Type type) {
    TypeToken<?> supertype = resolveType(type);
    supertype.typeResolver = this.typeResolver;
    return supertype;
  }
  
  @Nullable
  final TypeToken<? super T> getGenericSuperclass() {
    if (this.runtimeType instanceof TypeVariable)
      return boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]); 
    if (this.runtimeType instanceof WildcardType)
      return boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]); 
    Type superclass = getRawType().getGenericSuperclass();
    if (superclass == null)
      return null; 
    TypeToken<? super T> superToken = (TypeToken)resolveSupertype(superclass);
    return superToken;
  }
  
  @Nullable
  private TypeToken<? super T> boundAsSuperclass(Type bound) {
    TypeToken<?> token = of(bound);
    if (token.getRawType().isInterface())
      return null; 
    TypeToken<? super T> superclass = (TypeToken)token;
    return superclass;
  }
  
  final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
    if (this.runtimeType instanceof TypeVariable)
      return boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds()); 
    if (this.runtimeType instanceof WildcardType)
      return boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds()); 
    ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
    for (Type interfaceType : getRawType().getGenericInterfaces()) {
      TypeToken<? super T> resolvedInterface = (TypeToken)resolveSupertype(interfaceType);
      builder.add(resolvedInterface);
    } 
    return builder.build();
  }
  
  private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] bounds) {
    ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
    for (Type bound : bounds) {
      TypeToken<? super T> boundType = (TypeToken)of(bound);
      if (boundType.getRawType().isInterface())
        builder.add(boundType); 
    } 
    return builder.build();
  }
  
  public final TypeSet getTypes() {
    return new TypeSet();
  }
  
  public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
    Preconditions.checkArgument(superclass.isAssignableFrom(getRawType()), "%s is not a super class of %s", new Object[] { superclass, this });
    if (this.runtimeType instanceof TypeVariable)
      return getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds()); 
    if (this.runtimeType instanceof WildcardType)
      return getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds()); 
    if (superclass.isArray())
      return getArraySupertype(superclass); 
    TypeToken<? super T> supertype = (TypeToken)resolveSupertype((toGenericType((Class)superclass)).runtimeType);
    return supertype;
  }
  
  public final TypeToken<? extends T> getSubtype(Class<?> subclass) {
    Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", new Object[] { this });
    if (this.runtimeType instanceof WildcardType)
      return getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds()); 
    Preconditions.checkArgument(getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", new Object[] { subclass, this });
    if (isArray())
      return getArraySubtype(subclass); 
    TypeToken<? extends T> subtype = (TypeToken)of(resolveTypeArgsForSubclass(subclass));
    return subtype;
  }
  
  public final boolean isAssignableFrom(TypeToken<?> type) {
    return isAssignableFrom(type.runtimeType);
  }
  
  public final boolean isAssignableFrom(Type type) {
    return isAssignable((Type)Preconditions.checkNotNull(type), this.runtimeType);
  }
  
  public final boolean isArray() {
    return (getComponentType() != null);
  }
  
  public final boolean isPrimitive() {
    return (this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive());
  }
  
  public final TypeToken<T> wrap() {
    if (isPrimitive()) {
      Class<T> type = (Class<T>)this.runtimeType;
      return of(Primitives.wrap(type));
    } 
    return this;
  }
  
  private boolean isWrapper() {
    return Primitives.allWrapperTypes().contains(this.runtimeType);
  }
  
  public final TypeToken<T> unwrap() {
    if (isWrapper()) {
      Class<T> type = (Class<T>)this.runtimeType;
      return of(Primitives.unwrap(type));
    } 
    return this;
  }
  
  @Nullable
  public final TypeToken<?> getComponentType() {
    Type componentType = Types.getComponentType(this.runtimeType);
    if (componentType == null)
      return null; 
    return of(componentType);
  }
  
  public final Invokable<T, Object> method(Method method) {
    Preconditions.checkArgument(of(method.getDeclaringClass()).isAssignableFrom(this), "%s not declared by %s", new Object[] { method, this });
    return new Invokable.MethodInvokable<T>(method) {
        Type getGenericReturnType() {
          return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
        }
        
        Type[] getGenericParameterTypes() {
          return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
        }
        
        Type[] getGenericExceptionTypes() {
          return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
        }
        
        public TypeToken<T> getOwnerType() {
          return TypeToken.this;
        }
        
        public String toString() {
          return getOwnerType() + "." + super.toString();
        }
      };
  }
  
  public final Invokable<T, T> constructor(Constructor<?> constructor) {
    Preconditions.checkArgument((constructor.getDeclaringClass() == getRawType()), "%s not declared by %s", new Object[] { constructor, getRawType() });
    return new Invokable.ConstructorInvokable<T>(constructor) {
        Type getGenericReturnType() {
          return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
        }
        
        Type[] getGenericParameterTypes() {
          return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
        }
        
        Type[] getGenericExceptionTypes() {
          return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
        }
        
        public TypeToken<T> getOwnerType() {
          return TypeToken.this;
        }
        
        public String toString() {
          return getOwnerType() + "(" + Joiner.on(", ").join((Object[])getGenericParameterTypes()) + ")";
        }
      };
  }
  
  public class TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable {
    private transient ImmutableSet<TypeToken<? super T>> types;
    
    private static final long serialVersionUID = 0L;
    
    public TypeSet interfaces() {
      return new TypeToken.InterfaceSet(this);
    }
    
    public TypeSet classes() {
      return new TypeToken.ClassSet();
    }
    
    protected Set<TypeToken<? super T>> delegate() {
      ImmutableSet<TypeToken<? super T>> filteredTypes = this.types;
      if (filteredTypes == null) {
        ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
        return (Set<TypeToken<? super T>>)(this.types = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
      } 
      return (Set<TypeToken<? super T>>)filteredTypes;
    }
    
    public Set<Class<? super T>> rawTypes() {
      ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getImmediateRawTypes());
      return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
    }
  }
  
  private final class InterfaceSet extends TypeSet {
    private final transient TypeToken<T>.TypeSet allTypes;
    
    private transient ImmutableSet<TypeToken<? super T>> interfaces;
    
    private static final long serialVersionUID = 0L;
    
    InterfaceSet(TypeToken<T>.TypeSet allTypes) {
      this.allTypes = allTypes;
    }
    
    protected Set<TypeToken<? super T>> delegate() {
      ImmutableSet<TypeToken<? super T>> result = this.interfaces;
      if (result == null)
        return (Set<TypeToken<? super T>>)(this.interfaces = FluentIterable.from((Iterable)this.allTypes).filter(TypeToken.TypeFilter.INTERFACE_ONLY).toSet()); 
      return (Set<TypeToken<? super T>>)result;
    }
    
    public TypeToken<T>.TypeSet interfaces() {
      return this;
    }
    
    public Set<Class<? super T>> rawTypes() {
      ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getImmediateRawTypes());
      return (Set<Class<? super T>>)FluentIterable.from((Iterable)collectedTypes).filter(new Predicate<Class<?>>() {
            public boolean apply(Class<?> type) {
              return type.isInterface();
            }
          }).toSet();
    }
    
    public TypeToken<T>.TypeSet classes() {
      throw new UnsupportedOperationException("interfaces().classes() not supported.");
    }
    
    private Object readResolve() {
      return TypeToken.this.getTypes().interfaces();
    }
  }
  
  private final class ClassSet extends TypeSet {
    private transient ImmutableSet<TypeToken<? super T>> classes;
    
    private static final long serialVersionUID = 0L;
    
    private ClassSet() {}
    
    protected Set<TypeToken<? super T>> delegate() {
      ImmutableSet<TypeToken<? super T>> result = this.classes;
      if (result == null) {
        ImmutableList<TypeToken<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
        return (Set<TypeToken<? super T>>)(this.classes = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
      } 
      return (Set<TypeToken<? super T>>)result;
    }
    
    public TypeToken<T>.TypeSet classes() {
      return this;
    }
    
    public Set<Class<? super T>> rawTypes() {
      ImmutableList<Class<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes((Iterable<? extends Class<? super T>>)TypeToken.this.getImmediateRawTypes());
      return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
    }
    
    public TypeToken<T>.TypeSet interfaces() {
      throw new UnsupportedOperationException("classes().interfaces() not supported.");
    }
    
    private Object readResolve() {
      return TypeToken.this.getTypes().classes();
    }
  }
  
  private enum TypeFilter implements Predicate<TypeToken<?>> {
    IGNORE_TYPE_VARIABLE_OR_WILDCARD {
      public boolean apply(TypeToken<?> type) {
        return (!(type.runtimeType instanceof TypeVariable) && !(type.runtimeType instanceof WildcardType));
      }
    },
    INTERFACE_ONLY {
      public boolean apply(TypeToken<?> type) {
        return type.getRawType().isInterface();
      }
    };
  }
  
  public boolean equals(@Nullable Object o) {
    if (o instanceof TypeToken) {
      TypeToken<?> that = (TypeToken)o;
      return this.runtimeType.equals(that.runtimeType);
    } 
    return false;
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
  
  final TypeToken<T> rejectTypeVariables() {
    (new TypeVisitor() {
        void visitTypeVariable(TypeVariable<?> type) {
          throw new IllegalArgumentException(TypeToken.this.runtimeType + "contains a type variable and is not safe for the operation");
        }
        
        void visitWildcardType(WildcardType type) {
          visit(type.getLowerBounds());
          visit(type.getUpperBounds());
        }
        
        void visitParameterizedType(ParameterizedType type) {
          visit(type.getActualTypeArguments());
          visit(new Type[] { type.getOwnerType() });
        }
        
        void visitGenericArrayType(GenericArrayType type) {
          visit(new Type[] { type.getGenericComponentType() });
        }
      }).visit(new Type[] { this.runtimeType });
    return this;
  }
  
  private static boolean isAssignable(Type from, Type to) {
    if (to.equals(from))
      return true; 
    if (to instanceof WildcardType)
      return isAssignableToWildcardType(from, (WildcardType)to); 
    if (from instanceof TypeVariable)
      return isAssignableFromAny(((TypeVariable)from).getBounds(), to); 
    if (from instanceof WildcardType)
      return isAssignableFromAny(((WildcardType)from).getUpperBounds(), to); 
    if (from instanceof GenericArrayType)
      return isAssignableFromGenericArrayType((GenericArrayType)from, to); 
    if (to instanceof Class)
      return isAssignableToClass(from, (Class)to); 
    if (to instanceof ParameterizedType)
      return isAssignableToParameterizedType(from, (ParameterizedType)to); 
    if (to instanceof GenericArrayType)
      return isAssignableToGenericArrayType(from, (GenericArrayType)to); 
    return false;
  }
  
  private static boolean isAssignableFromAny(Type[] fromTypes, Type to) {
    for (Type from : fromTypes) {
      if (isAssignable(from, to))
        return true; 
    } 
    return false;
  }
  
  private static boolean isAssignableToClass(Type from, Class<?> to) {
    return to.isAssignableFrom(getRawType(from));
  }
  
  private static boolean isAssignableToWildcardType(Type from, WildcardType to) {
    return (isAssignable(from, supertypeBound(to)) && isAssignableBySubtypeBound(from, to));
  }
  
  private static boolean isAssignableBySubtypeBound(Type from, WildcardType to) {
    Type toSubtypeBound = subtypeBound(to);
    if (toSubtypeBound == null)
      return true; 
    Type fromSubtypeBound = subtypeBound(from);
    if (fromSubtypeBound == null)
      return false; 
    return isAssignable(toSubtypeBound, fromSubtypeBound);
  }
  
  private static boolean isAssignableToParameterizedType(Type from, ParameterizedType to) {
    Class<?> matchedClass = getRawType(to);
    if (!matchedClass.isAssignableFrom(getRawType(from)))
      return false; 
    TypeVariable[] arrayOfTypeVariable = (TypeVariable[])matchedClass.getTypeParameters();
    Type[] toTypeArgs = to.getActualTypeArguments();
    TypeToken<?> fromTypeToken = of(from);
    for (int i = 0; i < arrayOfTypeVariable.length; i++) {
      Type fromTypeArg = (fromTypeToken.resolveType(arrayOfTypeVariable[i])).runtimeType;
      if (!matchTypeArgument(fromTypeArg, toTypeArgs[i]))
        return false; 
    } 
    return true;
  }
  
  private static boolean isAssignableToGenericArrayType(Type from, GenericArrayType to) {
    if (from instanceof Class) {
      Class<?> fromClass = (Class)from;
      if (!fromClass.isArray())
        return false; 
      return isAssignable(fromClass.getComponentType(), to.getGenericComponentType());
    } 
    if (from instanceof GenericArrayType) {
      GenericArrayType fromArrayType = (GenericArrayType)from;
      return isAssignable(fromArrayType.getGenericComponentType(), to.getGenericComponentType());
    } 
    return false;
  }
  
  private static boolean isAssignableFromGenericArrayType(GenericArrayType from, Type to) {
    if (to instanceof Class) {
      Class<?> toClass = (Class)to;
      if (!toClass.isArray())
        return (toClass == Object.class); 
      return isAssignable(from.getGenericComponentType(), toClass.getComponentType());
    } 
    if (to instanceof GenericArrayType) {
      GenericArrayType toArrayType = (GenericArrayType)to;
      return isAssignable(from.getGenericComponentType(), toArrayType.getGenericComponentType());
    } 
    return false;
  }
  
  private static boolean matchTypeArgument(Type from, Type to) {
    if (from.equals(to))
      return true; 
    if (to instanceof WildcardType)
      return isAssignableToWildcardType(from, (WildcardType)to); 
    return false;
  }
  
  private static Type supertypeBound(Type type) {
    if (type instanceof WildcardType)
      return supertypeBound((WildcardType)type); 
    return type;
  }
  
  private static Type supertypeBound(WildcardType type) {
    Type[] upperBounds = type.getUpperBounds();
    if (upperBounds.length == 1)
      return supertypeBound(upperBounds[0]); 
    if (upperBounds.length == 0)
      return Object.class; 
    throw new AssertionError("There should be at most one upper bound for wildcard type: " + type);
  }
  
  @Nullable
  private static Type subtypeBound(Type type) {
    if (type instanceof WildcardType)
      return subtypeBound((WildcardType)type); 
    return type;
  }
  
  @Nullable
  private static Type subtypeBound(WildcardType type) {
    Type[] lowerBounds = type.getLowerBounds();
    if (lowerBounds.length == 1)
      return subtypeBound(lowerBounds[0]); 
    if (lowerBounds.length == 0)
      return null; 
    throw new AssertionError("Wildcard should have at most one lower bound: " + type);
  }
  
  @VisibleForTesting
  static Class<?> getRawType(Type type) {
    return (Class)getRawTypes(type).iterator().next();
  }
  
  @VisibleForTesting
  static ImmutableSet<Class<?>> getRawTypes(Type type) {
    Preconditions.checkNotNull(type);
    final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
    (new TypeVisitor() {
        void visitTypeVariable(TypeVariable<?> t) {
          visit(t.getBounds());
        }
        
        void visitWildcardType(WildcardType t) {
          visit(t.getUpperBounds());
        }
        
        void visitParameterizedType(ParameterizedType t) {
          builder.add(t.getRawType());
        }
        
        void visitClass(Class<?> t) {
          builder.add(t);
        }
        
        void visitGenericArrayType(GenericArrayType t) {
          builder.add(Types.getArrayClass(TypeToken.getRawType(t.getGenericComponentType())));
        }
      }).visit(new Type[] { type });
    return builder.build();
  }
  
  @VisibleForTesting
  static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
    if (cls.isArray()) {
      Type arrayOfGenericType = Types.newArrayType((toGenericType((Class)cls.getComponentType())).runtimeType);
      TypeToken<? extends T> result = (TypeToken)of(arrayOfGenericType);
      return result;
    } 
    TypeVariable[] arrayOfTypeVariable = (TypeVariable[])cls.getTypeParameters();
    if (arrayOfTypeVariable.length > 0) {
      TypeToken<? extends T> type = (TypeToken)of(Types.newParameterizedType(cls, (Type[])arrayOfTypeVariable));
      return type;
    } 
    return of(cls);
  }
  
  private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> supertype, Type[] upperBounds) {
    for (Type upperBound : upperBounds) {
      TypeToken<? super T> bound = (TypeToken)of(upperBound);
      if (of(supertype).isAssignableFrom(bound)) {
        TypeToken<? super T> result = bound.getSupertype(supertype);
        return result;
      } 
    } 
    throw new IllegalArgumentException(supertype + " isn't a super type of " + this);
  }
  
  private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> subclass, Type[] lowerBounds) {
    Type[] arr$ = lowerBounds;
    int len$ = arr$.length, i$ = 0;
    if (i$ < len$) {
      Type lowerBound = arr$[i$];
      TypeToken<? extends T> bound = (TypeToken)of(lowerBound);
      return bound.getSubtype(subclass);
    } 
    throw new IllegalArgumentException(subclass + " isn't a subclass of " + this);
  }
  
  private TypeToken<? super T> getArraySupertype(Class<? super T> supertype) {
    TypeToken<?> componentType = (TypeToken)Preconditions.checkNotNull(getComponentType(), "%s isn't a super type of %s", new Object[] { supertype, this });
    TypeToken<?> componentSupertype = componentType.getSupertype(supertype.getComponentType());
    TypeToken<? super T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
    return result;
  }
  
  private TypeToken<? extends T> getArraySubtype(Class<?> subclass) {
    TypeToken<?> componentSubtype = getComponentType().getSubtype(subclass.getComponentType());
    TypeToken<? extends T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
    return result;
  }
  
  private Type resolveTypeArgsForSubclass(Class<?> subclass) {
    if (this.runtimeType instanceof Class)
      return subclass; 
    TypeToken<?> genericSubtype = toGenericType(subclass);
    Type supertypeWithArgsFromSubtype = (genericSubtype.getSupertype(getRawType())).runtimeType;
    return (new TypeResolver()).where(supertypeWithArgsFromSubtype, this.runtimeType).resolveType(genericSubtype.runtimeType);
  }
  
  private static Type newArrayClassOrGenericArrayType(Type componentType) {
    return Types.JavaVersion.JAVA7.newArrayType(componentType);
  }
  
  private static final class SimpleTypeToken<T> extends TypeToken<T> {
    private static final long serialVersionUID = 0L;
    
    SimpleTypeToken(Type type) {
      super(type);
    }
  }
  
  private static abstract class TypeCollector<K> {
    private TypeCollector() {}
    
    static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>() {
        Class<?> getRawType(TypeToken<?> type) {
          return type.getRawType();
        }
        
        Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> type) {
          return (Iterable<? extends TypeToken<?>>)type.getGenericInterfaces();
        }
        
        @Nullable
        TypeToken<?> getSuperclass(TypeToken<?> type) {
          return type.getGenericSuperclass();
        }
      };
    
    static final TypeCollector<Class<?>> FOR_RAW_TYPE = new TypeCollector<Class<?>>() {
        Class<?> getRawType(Class<?> type) {
          return type;
        }
        
        Iterable<? extends Class<?>> getInterfaces(Class<?> type) {
          return Arrays.asList(type.getInterfaces());
        }
        
        @Nullable
        Class<?> getSuperclass(Class<?> type) {
          return type.getSuperclass();
        }
      };
    
    final TypeCollector<K> classesOnly() {
      return new ForwardingTypeCollector<K>(this) {
          Iterable<? extends K> getInterfaces(K type) {
            return (Iterable<? extends K>)ImmutableSet.of();
          }
          
          ImmutableList<K> collectTypes(Iterable<? extends K> types) {
            ImmutableList.Builder<K> builder = ImmutableList.builder();
            for (K type : types) {
              if (!getRawType(type).isInterface())
                builder.add(type); 
            } 
            return super.collectTypes((Iterable<? extends K>)builder.build());
          }
        };
    }
    
    final ImmutableList<K> collectTypes(K type) {
      return collectTypes((Iterable<? extends K>)ImmutableList.of(type));
    }
    
    ImmutableList<K> collectTypes(Iterable<? extends K> types) {
      Map<K, Integer> map = Maps.newHashMap();
      for (K type : types)
        collectTypes(type, map); 
      return sortKeysByValue(map, (Comparator<? super Integer>)Ordering.natural().reverse());
    }
    
    private int collectTypes(K type, Map<? super K, Integer> map) {
      Integer existing = map.get(this);
      if (existing != null)
        return existing.intValue(); 
      int aboveMe = getRawType(type).isInterface() ? 1 : 0;
      for (K interfaceType : getInterfaces(type))
        aboveMe = Math.max(aboveMe, collectTypes(interfaceType, map)); 
      K superclass = getSuperclass(type);
      if (superclass != null)
        aboveMe = Math.max(aboveMe, collectTypes(superclass, map)); 
      map.put(type, Integer.valueOf(aboveMe + 1));
      return aboveMe + 1;
    }
    
    private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
      Ordering<K> keyOrdering = new Ordering<K>() {
          public int compare(K left, K right) {
            return valueComparator.compare(map.get(left), map.get(right));
          }
        };
      return keyOrdering.immutableSortedCopy(map.keySet());
    }
    
    abstract Class<?> getRawType(K param1K);
    
    abstract Iterable<? extends K> getInterfaces(K param1K);
    
    @Nullable
    abstract K getSuperclass(K param1K);
    
    private static class ForwardingTypeCollector<K> extends TypeCollector<K> {
      private final TypeToken.TypeCollector<K> delegate;
      
      ForwardingTypeCollector(TypeToken.TypeCollector<K> delegate) {
        this.delegate = delegate;
      }
      
      Class<?> getRawType(K type) {
        return this.delegate.getRawType(type);
      }
      
      Iterable<? extends K> getInterfaces(K type) {
        return this.delegate.getInterfaces(type);
      }
      
      K getSuperclass(K type) {
        return this.delegate.getSuperclass(type);
      }
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\reflect\TypeToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */