package com.google.gson.reflect;

import com.google.gson.internal.;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TypeToken<T> {
  final Class<? super T> rawType;
  
  final Type type;
  
  final int hashCode;
  
  protected TypeToken() {
    this.type = getSuperclassTypeParameter(getClass());
    this.rawType = .Gson.Types.getRawType(this.type);
    this.hashCode = this.type.hashCode();
  }
  
  TypeToken(Type type) {
    this.type = .Gson.Types.canonicalize((Type).Gson.Preconditions.checkNotNull(type));
    this.rawType = .Gson.Types.getRawType(this.type);
    this.hashCode = this.type.hashCode();
  }
  
  static Type getSuperclassTypeParameter(Class<?> subclass) {
    Type superclass = subclass.getGenericSuperclass();
    if (superclass instanceof Class)
      throw new RuntimeException("Missing type parameter."); 
    ParameterizedType parameterized = (ParameterizedType)superclass;
    return .Gson.Types.canonicalize(parameterized.getActualTypeArguments()[0]);
  }
  
  public final Class<? super T> getRawType() {
    return this.rawType;
  }
  
  public final Type getType() {
    return this.type;
  }
  
  @Deprecated
  public boolean isAssignableFrom(Class<?> cls) {
    return isAssignableFrom(cls);
  }
  
  @Deprecated
  public boolean isAssignableFrom(Type from) {
    if (from == null)
      return false; 
    if (this.type.equals(from))
      return true; 
    if (this.type instanceof Class)
      return this.rawType.isAssignableFrom(.Gson.Types.getRawType(from)); 
    if (this.type instanceof ParameterizedType)
      return isAssignableFrom(from, (ParameterizedType)this.type, new HashMap<String, Type>()); 
    if (this.type instanceof GenericArrayType)
      return (this.rawType.isAssignableFrom(.Gson.Types.getRawType(from)) && isAssignableFrom(from, (GenericArrayType)this.type)); 
    throw buildUnexpectedTypeError(this.type, new Class[] { Class.class, ParameterizedType.class, GenericArrayType.class });
  }
  
  @Deprecated
  public boolean isAssignableFrom(TypeToken<?> token) {
    return isAssignableFrom(token.getType());
  }
  
  private static boolean isAssignableFrom(Type<?> from, GenericArrayType to) {
    Type toGenericComponentType = to.getGenericComponentType();
    if (toGenericComponentType instanceof ParameterizedType) {
      Type<?> t = from;
      if (from instanceof GenericArrayType) {
        t = ((GenericArrayType)from).getGenericComponentType();
      } else if (from instanceof Class) {
        Class<?> classType = (Class)from;
        while (classType.isArray())
          classType = classType.getComponentType(); 
        t = classType;
      } 
      return isAssignableFrom(t, (ParameterizedType)toGenericComponentType, new HashMap<String, Type>());
    } 
    return true;
  }
  
  private static boolean isAssignableFrom(Type from, ParameterizedType to, Map<String, Type> typeVarMap) {
    if (from == null)
      return false; 
    if (to.equals(from))
      return true; 
    Class<?> clazz = .Gson.Types.getRawType(from);
    ParameterizedType ptype = null;
    if (from instanceof ParameterizedType)
      ptype = (ParameterizedType)from; 
    if (ptype != null) {
      Type[] tArgs = ptype.getActualTypeArguments();
      TypeVariable[] arrayOfTypeVariable = (TypeVariable[])clazz.getTypeParameters();
      for (int i = 0; i < tArgs.length; i++) {
        Type arg = tArgs[i];
        TypeVariable<?> var = arrayOfTypeVariable[i];
        while (arg instanceof TypeVariable) {
          TypeVariable<?> v = (TypeVariable)arg;
          arg = typeVarMap.get(v.getName());
        } 
        typeVarMap.put(var.getName(), arg);
      } 
      if (typeEquals(ptype, to, typeVarMap))
        return true; 
    } 
    for (Type itype : clazz.getGenericInterfaces()) {
      if (isAssignableFrom(itype, to, new HashMap<String, Type>(typeVarMap)))
        return true; 
    } 
    Type sType = clazz.getGenericSuperclass();
    return isAssignableFrom(sType, to, new HashMap<String, Type>(typeVarMap));
  }
  
  private static boolean typeEquals(ParameterizedType from, ParameterizedType to, Map<String, Type> typeVarMap) {
    if (from.getRawType().equals(to.getRawType())) {
      Type[] fromArgs = from.getActualTypeArguments();
      Type[] toArgs = to.getActualTypeArguments();
      for (int i = 0; i < fromArgs.length; i++) {
        if (!matches(fromArgs[i], toArgs[i], typeVarMap))
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  private static AssertionError buildUnexpectedTypeError(Type token, Class<?>... expected) {
    StringBuilder exceptionMessage = new StringBuilder("Unexpected type. Expected one of: ");
    for (Class<?> clazz : expected)
      exceptionMessage.append(clazz.getName()).append(", "); 
    exceptionMessage.append("but got: ").append(token.getClass().getName()).append(", for type token: ").append(token.toString()).append('.');
    return new AssertionError(exceptionMessage.toString());
  }
  
  private static boolean matches(Type from, Type to, Map<String, Type> typeMap) {
    return (to.equals(from) || (from instanceof TypeVariable && to.equals(typeMap.get(((TypeVariable)from).getName()))));
  }
  
  public final int hashCode() {
    return this.hashCode;
  }
  
  public final boolean equals(Object o) {
    return (o instanceof TypeToken && .Gson.Types.equals(this.type, ((TypeToken)o).type));
  }
  
  public final String toString() {
    return .Gson.Types.typeToString(this.type);
  }
  
  public static TypeToken<?> get(Type type) {
    return new TypeToken(type);
  }
  
  public static <T> TypeToken<T> get(Class<T> type) {
    return new TypeToken<T>(type);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\reflect\TypeToken.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */