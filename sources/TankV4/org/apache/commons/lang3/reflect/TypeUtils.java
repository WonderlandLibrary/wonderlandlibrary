package org.apache.commons.lang3.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;

public class TypeUtils {
   public static final WildcardType WILDCARD_ALL = wildcardType().withUpperBounds(Object.class).build();

   public static boolean isAssignable(Type var0, Type var1) {
      return isAssignable(var0, (Type)var1, (Map)null);
   }

   private static boolean isAssignable(Type var0, ParameterizedType var1, Map var2) {
      if (var0 == null) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1.equals(var0)) {
         return true;
      } else {
         Class var3 = getRawType(var1);
         Map var4 = getTypeArguments((Type)var0, var3, (Map)null);
         if (var4 == null) {
            return false;
         } else if (var4.isEmpty()) {
            return true;
         } else {
            Map var5 = getTypeArguments(var1, var3, var2);
            Iterator var6 = var5.keySet().iterator();

            Type var8;
            Type var9;
            do {
               do {
                  do {
                     if (!var6.hasNext()) {
                        return true;
                     }

                     TypeVariable var7 = (TypeVariable)var6.next();
                     var8 = unrollVariableAssignments(var7, var5);
                     var9 = unrollVariableAssignments(var7, var4);
                  } while(var9 == null);
               } while(var8.equals(var9));
            } while(var8 instanceof WildcardType && var2 == null);

            return false;
         }
      }
   }

   private static Type unrollVariableAssignments(TypeVariable var0, Map var1) {
      while(true) {
         Type var2 = (Type)var1.get(var0);
         if (!(var2 instanceof TypeVariable) || var2.equals(var0)) {
            return var2;
         }

         var0 = (TypeVariable)var2;
      }
   }

   private static boolean isAssignable(Type var0, GenericArrayType var1, Map var2) {
      if (var0 == null) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1.equals(var0)) {
         return true;
      } else {
         Type var3 = var1.getGenericComponentType();
         if (!(var0 instanceof Class)) {
            if (var0 instanceof GenericArrayType) {
               return isAssignable(((GenericArrayType)var0).getGenericComponentType(), (Type)var3, var2);
            } else {
               int var5;
               int var6;
               Type var7;
               Type[] var8;
               if (var0 instanceof WildcardType) {
                  var8 = getImplicitUpperBounds((WildcardType)var0);
                  var5 = var8.length;

                  for(var6 = 0; var6 < var5; ++var6) {
                     var7 = var8[var6];
                     if (isAssignable(var7, var1)) {
                        return true;
                     }
                  }

                  return false;
               } else if (var0 instanceof TypeVariable) {
                  var8 = getImplicitBounds((TypeVariable)var0);
                  var5 = var8.length;

                  for(var6 = 0; var6 < var5; ++var6) {
                     var7 = var8[var6];
                     if (isAssignable(var7, var1)) {
                        return true;
                     }
                  }

                  return false;
               } else if (var0 instanceof ParameterizedType) {
                  return false;
               } else {
                  throw new IllegalStateException("found an unhandled type: " + var0);
               }
            }
         } else {
            Class var4 = (Class)var0;
            Class var10000;
            if (var4.isArray()) {
               var10000 = var4.getComponentType();
               if (var2 != null) {
                  boolean var10002 = true;
                  return (boolean)var10000;
               }
            }

            var10000 = false;
            return (boolean)var10000;
         }
      }
   }

   private static boolean isAssignable(Type var0, WildcardType var1, Map var2) {
      if (var0 == null) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (var1.equals(var0)) {
         return true;
      } else {
         Type[] var3 = getImplicitUpperBounds(var1);
         Type[] var4 = getImplicitLowerBounds(var1);
         if (!(var0 instanceof WildcardType)) {
            Type[] var16 = var3;
            int var17 = var3.length;

            int var18;
            Type var19;
            for(var18 = 0; var18 < var17; ++var18) {
               var19 = var16[var18];
               substituteTypeVariables(var19, var2);
               if (var2 != null) {
                  return false;
               }
            }

            var16 = var4;
            var17 = var4.length;

            for(var18 = 0; var18 < var17; ++var18) {
               var19 = var16[var18];
               substituteTypeVariables(var19, var2);
               if (var2 != null) {
                  return false;
               }
            }

            return true;
         } else {
            WildcardType var5 = (WildcardType)var0;
            Type[] var6 = getImplicitUpperBounds(var5);
            Type[] var7 = getImplicitLowerBounds(var5);
            Type[] var8 = var3;
            int var9 = var3.length;

            int var10;
            Type var11;
            Type[] var12;
            int var13;
            int var14;
            Type var15;
            for(var10 = 0; var10 < var9; ++var10) {
               var11 = var8[var10];
               var11 = substituteTypeVariables(var11, var2);
               var12 = var6;
               var13 = var6.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  var15 = var12[var14];
                  if (var2 != null) {
                     return false;
                  }
               }
            }

            var8 = var4;
            var9 = var4.length;

            for(var10 = 0; var10 < var9; ++var10) {
               var11 = var8[var10];
               var11 = substituteTypeVariables(var11, var2);
               var12 = var7;
               var13 = var7.length;

               for(var14 = 0; var14 < var13; ++var14) {
                  var15 = var12[var14];
                  if (var2 != null) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   private static Type substituteTypeVariables(Type var0, Map var1) {
      if (var0 instanceof TypeVariable && var1 != null) {
         Type var2 = (Type)var1.get(var0);
         if (var2 == null) {
            throw new IllegalArgumentException("missing assignment type for type variable " + var0);
         } else {
            return var2;
         }
      } else {
         return var0;
      }
   }

   public static Map getTypeArguments(ParameterizedType var0) {
      return getTypeArguments((ParameterizedType)var0, getRawType(var0), (Map)null);
   }

   public static Map getTypeArguments(Type var0, Class var1) {
      return getTypeArguments((Type)var0, var1, (Map)null);
   }

   private static Map getTypeArguments(Type var0, Class var1, Map var2) {
      if (var0 instanceof Class) {
         return getTypeArguments((Class)var0, var1, var2);
      } else if (var0 instanceof ParameterizedType) {
         return getTypeArguments((ParameterizedType)var0, var1, var2);
      } else if (var0 instanceof GenericArrayType) {
         return getTypeArguments(((GenericArrayType)var0).getGenericComponentType(), var1.isArray() ? var1.getComponentType() : var1, var2);
      } else {
         Type[] var3;
         int var4;
         int var5;
         Type var6;
         if (var0 instanceof WildcardType) {
            var3 = getImplicitUpperBounds((WildcardType)var0);
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
               var6 = var3[var5];
               if (var1 == null) {
                  return getTypeArguments(var6, var1, var2);
               }
            }

            return null;
         } else if (var0 instanceof TypeVariable) {
            var3 = getImplicitBounds((TypeVariable)var0);
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
               var6 = var3[var5];
               if (var1 == null) {
                  return getTypeArguments(var6, var1, var2);
               }
            }

            return null;
         } else {
            throw new IllegalStateException("found an unhandled type: " + var0);
         }
      }
   }

   private static Map getTypeArguments(ParameterizedType var0, Class var1, Map var2) {
      Class var3 = getRawType(var0);
      if (var1 == null) {
         return null;
      } else {
         Type var4 = var0.getOwnerType();
         Object var5;
         if (var4 instanceof ParameterizedType) {
            ParameterizedType var6 = (ParameterizedType)var4;
            var5 = getTypeArguments(var6, getRawType(var6), var2);
         } else {
            var5 = var2 == null ? new HashMap() : new HashMap(var2);
         }

         Type[] var10 = var0.getActualTypeArguments();
         TypeVariable[] var7 = var3.getTypeParameters();

         for(int var8 = 0; var8 < var7.length; ++var8) {
            Type var9 = var10[var8];
            ((Map)var5).put(var7[var8], ((Map)var5).containsKey(var9) ? (Type)((Map)var5).get(var9) : var9);
         }

         if (var1.equals(var3)) {
            return (Map)var5;
         } else {
            return getTypeArguments((Type)getClosestParentType(var3, var1), var1, (Map)var5);
         }
      }
   }

   private static Map getTypeArguments(Class var0, Class var1, Map var2) {
      if (var1 == null) {
         return null;
      } else {
         if (var0.isPrimitive()) {
            if (var1.isPrimitive()) {
               return new HashMap();
            }

            var0 = ClassUtils.primitiveToWrapper(var0);
         }

         HashMap var3 = var2 == null ? new HashMap() : new HashMap(var2);
         return (Map)(var1.equals(var0) ? var3 : getTypeArguments((Type)getClosestParentType(var0, var1), var1, var3));
      }
   }

   public static Map determineTypeArguments(Class var0, ParameterizedType var1) {
      Validate.notNull(var0, "cls is null");
      Validate.notNull(var1, "superType is null");
      Class var2 = getRawType(var1);
      if (var2 == null) {
         return null;
      } else if (var0.equals(var2)) {
         return getTypeArguments((ParameterizedType)var1, var2, (Map)null);
      } else {
         Type var3 = getClosestParentType(var0, var2);
         if (var3 instanceof Class) {
            return determineTypeArguments((Class)var3, var1);
         } else {
            ParameterizedType var4 = (ParameterizedType)var3;
            Class var5 = getRawType(var4);
            Map var6 = determineTypeArguments(var5, var1);
            mapTypeVariablesToArguments(var0, var4, var6);
            return var6;
         }
      }
   }

   private static void mapTypeVariablesToArguments(Class var0, ParameterizedType var1, Map var2) {
      Type var3 = var1.getOwnerType();
      if (var3 instanceof ParameterizedType) {
         mapTypeVariablesToArguments(var0, (ParameterizedType)var3, var2);
      }

      Type[] var4 = var1.getActualTypeArguments();
      TypeVariable[] var5 = getRawType(var1).getTypeParameters();
      List var6 = Arrays.asList(var0.getTypeParameters());

      for(int var7 = 0; var7 < var4.length; ++var7) {
         TypeVariable var8 = var5[var7];
         Type var9 = var4[var7];
         if (var6.contains(var9) && var2.containsKey(var8)) {
            var2.put((TypeVariable)var9, var2.get(var8));
         }
      }

   }

   private static Type getClosestParentType(Class var0, Class var1) {
      if (var1.isInterface()) {
         Type[] var2 = var0.getGenericInterfaces();
         Type var3 = null;
         Type[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Type var7 = var4[var6];
            Class var8 = null;
            if (var7 instanceof ParameterizedType) {
               var8 = getRawType((ParameterizedType)var7);
            } else {
               if (!(var7 instanceof Class)) {
                  throw new IllegalStateException("Unexpected generic interface type found: " + var7);
               }

               var8 = (Class)var7;
            }

            if (var1 == null && isAssignable(var3, var8)) {
               var3 = var7;
            }
         }

         if (var3 != null) {
            return var3;
         }
      }

      return var0.getGenericSuperclass();
   }

   public static boolean isInstance(Object var0, Type var1) {
      if (var1 == null) {
         return false;
      } else {
         return var0 == null ? !(var1 instanceof Class) || !((Class)var1).isPrimitive() : isAssignable(var0.getClass(), (Type)var1, (Map)null);
      }
   }

   public static Type[] normalizeUpperBounds(Type[] var0) {
      Validate.notNull(var0, "null value specified for bounds array");
      if (var0.length < 2) {
         return var0;
      } else {
         HashSet var1 = new HashSet(var0.length);
         Type[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Type var5 = var2[var4];
            boolean var6 = false;
            Type[] var7 = var0;
            int var8 = var0.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               Type var10 = var7[var9];
               if (var5 != var10) {
                  ;
               }
            }

            if (!var6) {
               var1.add(var5);
            }
         }

         return (Type[])var1.toArray(new Type[var1.size()]);
      }
   }

   public static Type[] getImplicitBounds(TypeVariable var0) {
      Validate.notNull(var0, "typeVariable is null");
      Type[] var1 = var0.getBounds();
      return var1.length == 0 ? new Type[]{Object.class} : normalizeUpperBounds(var1);
   }

   public static Type[] getImplicitUpperBounds(WildcardType var0) {
      Validate.notNull(var0, "wildcardType is null");
      Type[] var1 = var0.getUpperBounds();
      return var1.length == 0 ? new Type[]{Object.class} : normalizeUpperBounds(var1);
   }

   public static Type[] getImplicitLowerBounds(WildcardType var0) {
      Validate.notNull(var0, "wildcardType is null");
      Type[] var1 = var0.getLowerBounds();
      return var1.length == 0 ? new Type[]{null} : var1;
   }

   public static boolean typesSatisfyVariables(Map var0) {
      Validate.notNull(var0, "typeVarAssigns is null");
      Iterator var1 = var0.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         TypeVariable var3 = (TypeVariable)var2.getKey();
         Type var4 = (Type)var2.getValue();
         Type[] var5 = getImplicitBounds(var3);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Type var8 = var5[var7];
            substituteTypeVariables(var8, var0);
            if (var0 != null) {
               return false;
            }
         }
      }

      return true;
   }

   private static Class getRawType(ParameterizedType var0) {
      Type var1 = var0.getRawType();
      if (!(var1 instanceof Class)) {
         throw new IllegalStateException("Wait... What!? Type of rawType: " + var1);
      } else {
         return (Class)var1;
      }
   }

   public static Class getRawType(Type var0, Type var1) {
      if (var0 instanceof Class) {
         return (Class)var0;
      } else if (var0 instanceof ParameterizedType) {
         return getRawType((ParameterizedType)var0);
      } else if (var0 instanceof TypeVariable) {
         if (var1 == null) {
            return null;
         } else {
            GenericDeclaration var5 = ((TypeVariable)var0).getGenericDeclaration();
            if (!(var5 instanceof Class)) {
               return null;
            } else {
               Map var3 = getTypeArguments(var1, (Class)var5);
               if (var3 == null) {
                  return null;
               } else {
                  Type var4 = (Type)var3.get(var0);
                  return var4 == null ? null : getRawType(var4, var1);
               }
            }
         }
      } else if (var0 instanceof GenericArrayType) {
         Class var2 = getRawType(((GenericArrayType)var0).getGenericComponentType(), var1);
         return Array.newInstance(var2, 0).getClass();
      } else if (var0 instanceof WildcardType) {
         return null;
      } else {
         throw new IllegalArgumentException("unknown type: " + var0);
      }
   }

   public static boolean isArrayType(Type var0) {
      return var0 instanceof GenericArrayType || var0 instanceof Class && ((Class)var0).isArray();
   }

   public static Type getArrayComponentType(Type var0) {
      if (var0 instanceof Class) {
         Class var1 = (Class)var0;
         return var1.isArray() ? var1.getComponentType() : null;
      } else {
         return var0 instanceof GenericArrayType ? ((GenericArrayType)var0).getGenericComponentType() : null;
      }
   }

   public static Type unrollVariables(Map var0, Type var1) {
      if (var0 == null) {
         var0 = Collections.emptyMap();
      }

      if (var1 != false) {
         if (var1 instanceof TypeVariable) {
            return unrollVariables(var0, (Type)var0.get(var1));
         }

         if (var1 instanceof ParameterizedType) {
            ParameterizedType var7 = (ParameterizedType)var1;
            Object var3;
            if (var7.getOwnerType() == null) {
               var3 = var0;
            } else {
               var3 = new HashMap(var0);
               ((Map)var3).putAll(getTypeArguments(var7));
            }

            Type[] var4 = var7.getActualTypeArguments();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               Type var6 = unrollVariables((Map)var3, var4[var5]);
               if (var6 != null) {
                  var4[var5] = var6;
               }
            }

            return parameterizeWithOwner(var7.getOwnerType(), (Class)var7.getRawType(), var4);
         }

         if (var1 instanceof WildcardType) {
            WildcardType var2 = (WildcardType)var1;
            return wildcardType().withUpperBounds(unrollBounds(var0, var2.getUpperBounds())).withLowerBounds(unrollBounds(var0, var2.getLowerBounds())).build();
         }
      }

      return var1;
   }

   private static Type[] unrollBounds(Map var0, Type[] var1) {
      Type[] var2 = var1;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Type var4 = unrollVariables(var0, var2[var3]);
         if (var4 == null) {
            var2 = (Type[])ArrayUtils.remove((Object[])var2, var3--);
         } else {
            var2[var3] = var4;
         }
      }

      return var2;
   }

   public static final ParameterizedType parameterize(Class var0, Type... var1) {
      return parameterizeWithOwner((Type)null, var0, (Type[])var1);
   }

   public static final ParameterizedType parameterize(Class var0, Map var1) {
      Validate.notNull(var0, "raw class is null");
      Validate.notNull(var1, "typeArgMappings is null");
      return parameterizeWithOwner((Type)null, var0, (Type[])extractTypeArgumentsFrom(var1, var0.getTypeParameters()));
   }

   public static final ParameterizedType parameterizeWithOwner(Type var0, Class var1, Type... var2) {
      Validate.notNull(var1, "raw class is null");
      Object var3;
      if (var1.getEnclosingClass() == null) {
         Validate.isTrue(var0 == null, "no owner allowed for top-level %s", var1);
         var3 = null;
      } else if (var0 == null) {
         var3 = var1.getEnclosingClass();
      } else {
         Validate.isTrue(isAssignable(var0, var1.getEnclosingClass()), "%s is invalid owner type for parameterized %s", var0, var1);
         var3 = var0;
      }

      Validate.noNullElements((Object[])var2, "null type argument at index %s");
      Validate.isTrue(var1.getTypeParameters().length == var2.length, "invalid number of type parameters specified: expected %s, got %s", var1.getTypeParameters().length, var2.length);
      return new TypeUtils.ParameterizedTypeImpl(var1, (Type)var3, var2);
   }

   public static final ParameterizedType parameterizeWithOwner(Type var0, Class var1, Map var2) {
      Validate.notNull(var1, "raw class is null");
      Validate.notNull(var2, "typeArgMappings is null");
      return parameterizeWithOwner(var0, var1, extractTypeArgumentsFrom(var2, var1.getTypeParameters()));
   }

   private static Type[] extractTypeArgumentsFrom(Map var0, TypeVariable[] var1) {
      Type[] var2 = new Type[var1.length];
      int var3 = 0;
      TypeVariable[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         TypeVariable var7 = var4[var6];
         Validate.isTrue(var0.containsKey(var7), "missing argument mapping for %s", toString(var7));
         var2[var3++] = (Type)var0.get(var7);
      }

      return var2;
   }

   public static TypeUtils.WildcardTypeBuilder wildcardType() {
      return new TypeUtils.WildcardTypeBuilder();
   }

   public static GenericArrayType genericArrayType(Type var0) {
      return new TypeUtils.GenericArrayTypeImpl((Type)Validate.notNull(var0, "componentType is null"));
   }

   private static boolean equals(ParameterizedType var0, Type var1) {
      if (var1 instanceof ParameterizedType) {
         ParameterizedType var2 = (ParameterizedType)var1;
         var0.getRawType();
         if (var2.getRawType() != false) {
            var0.getOwnerType();
            if (var2.getOwnerType() != false) {
               return equals((Type[])var0.getActualTypeArguments(), (Type[])var2.getActualTypeArguments());
            }
         }
      }

      return false;
   }

   private static boolean equals(GenericArrayType var0, Type var1) {
      Type var10000;
      if (var1 instanceof GenericArrayType) {
         var10000 = var0.getGenericComponentType();
         if (((GenericArrayType)var1).getGenericComponentType() != false) {
            boolean var10001 = true;
            return (boolean)var10000;
         }
      }

      var10000 = false;
      return (boolean)var10000;
   }

   private static boolean equals(WildcardType var0, Type var1) {
      if (!(var1 instanceof WildcardType)) {
         return true;
      } else {
         WildcardType var2 = (WildcardType)var1;
         return var0.getLowerBounds() == var2.getLowerBounds() && getImplicitUpperBounds(var0) == getImplicitUpperBounds(var2);
      }
   }

   public static String toString(Type var0) {
      Validate.notNull(var0);
      if (var0 instanceof Class) {
         return classToString((Class)var0);
      } else if (var0 instanceof ParameterizedType) {
         return parameterizedTypeToString((ParameterizedType)var0);
      } else if (var0 instanceof WildcardType) {
         return wildcardTypeToString((WildcardType)var0);
      } else if (var0 instanceof TypeVariable) {
         return typeVariableToString((TypeVariable)var0);
      } else if (var0 instanceof GenericArrayType) {
         return genericArrayTypeToString((GenericArrayType)var0);
      } else {
         throw new IllegalArgumentException(ObjectUtils.identityToString(var0));
      }
   }

   public static String toLongString(TypeVariable var0) {
      Validate.notNull(var0, "var is null");
      StringBuilder var1 = new StringBuilder();
      GenericDeclaration var2 = var0.getGenericDeclaration();
      if (var2 instanceof Class) {
         Class var3;
         for(var3 = (Class)var2; var3.getEnclosingClass() != null; var3 = var3.getEnclosingClass()) {
            var1.insert(0, var3.getSimpleName()).insert(0, '.');
         }

         var1.insert(0, var3.getName());
      } else if (var2 instanceof Type) {
         var1.append(toString((Type)var2));
      } else {
         var1.append(var2);
      }

      return var1.append(':').append(typeVariableToString(var0)).toString();
   }

   public static Typed wrap(Type var0) {
      return new Typed(var0) {
         final Type val$type;

         {
            this.val$type = var1;
         }

         public Type getType() {
            return this.val$type;
         }
      };
   }

   public static Typed wrap(Class var0) {
      return wrap((Type)var0);
   }

   private static String classToString(Class var0) {
      StringBuilder var1 = new StringBuilder();
      if (var0.getEnclosingClass() != null) {
         var1.append(classToString(var0.getEnclosingClass())).append('.').append(var0.getSimpleName());
      } else {
         var1.append(var0.getName());
      }

      if (var0.getTypeParameters().length > 0) {
         var1.append('<');
         appendAllTo(var1, ", ", var0.getTypeParameters());
         var1.append('>');
      }

      return var1.toString();
   }

   private static String typeVariableToString(TypeVariable var0) {
      StringBuilder var1 = new StringBuilder(var0.getName());
      Type[] var2 = var0.getBounds();
      if (var2.length > 0 && (var2.length != 1 || !Object.class.equals(var2[0]))) {
         var1.append(" extends ");
         appendAllTo(var1, " & ", var0.getBounds());
      }

      return var1.toString();
   }

   private static String parameterizedTypeToString(ParameterizedType var0) {
      StringBuilder var1 = new StringBuilder();
      Type var2 = var0.getOwnerType();
      Class var3 = (Class)var0.getRawType();
      Type[] var4 = var0.getActualTypeArguments();
      if (var2 == null) {
         var1.append(var3.getName());
      } else {
         if (var2 instanceof Class) {
            var1.append(((Class)var2).getName());
         } else {
            var1.append(var2.toString());
         }

         var1.append('.').append(var3.getSimpleName());
      }

      appendAllTo(var1.append('<'), ", ", var4).append('>');
      return var1.toString();
   }

   private static String wildcardTypeToString(WildcardType var0) {
      StringBuilder var1 = (new StringBuilder()).append('?');
      Type[] var2 = var0.getLowerBounds();
      Type[] var3 = var0.getUpperBounds();
      if (var2.length > 0) {
         appendAllTo(var1.append(" super "), " & ", var2);
      } else if (var3.length != 1 || !Object.class.equals(var3[0])) {
         appendAllTo(var1.append(" extends "), " & ", var3);
      }

      return var1.toString();
   }

   private static String genericArrayTypeToString(GenericArrayType var0) {
      return String.format("%s[]", toString(var0.getGenericComponentType()));
   }

   private static StringBuilder appendAllTo(StringBuilder var0, String var1, Type... var2) {
      Validate.notEmpty(Validate.noNullElements((Object[])var2));
      if (var2.length > 0) {
         var0.append(toString(var2[0]));

         for(int var3 = 1; var3 < var2.length; ++var3) {
            var0.append(var1).append(toString(var2[var3]));
         }
      }

      return var0;
   }

   static boolean access$100(GenericArrayType var0, Type var1) {
      return equals(var0, var1);
   }

   static boolean access$200(ParameterizedType var0, Type var1) {
      return equals(var0, var1);
   }

   static boolean access$300(WildcardType var0, Type var1) {
      return equals(var0, var1);
   }

   private static final class WildcardTypeImpl implements WildcardType {
      private static final Type[] EMPTY_BOUNDS = new Type[0];
      private final Type[] upperBounds;
      private final Type[] lowerBounds;

      private WildcardTypeImpl(Type[] var1, Type[] var2) {
         this.upperBounds = (Type[])ObjectUtils.defaultIfNull(var1, EMPTY_BOUNDS);
         this.lowerBounds = (Type[])ObjectUtils.defaultIfNull(var2, EMPTY_BOUNDS);
      }

      public Type[] getUpperBounds() {
         return (Type[])this.upperBounds.clone();
      }

      public Type[] getLowerBounds() {
         return (Type[])this.lowerBounds.clone();
      }

      public String toString() {
         return TypeUtils.toString(this);
      }

      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof WildcardType && TypeUtils.access$300(this, (WildcardType)var1);
      }

      public int hashCode() {
         short var1 = 18688;
         int var2 = var1 | Arrays.hashCode(this.upperBounds);
         var2 <<= 8;
         var2 |= Arrays.hashCode(this.lowerBounds);
         return var2;
      }

      WildcardTypeImpl(Type[] var1, Type[] var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class ParameterizedTypeImpl implements ParameterizedType {
      private final Class raw;
      private final Type useOwner;
      private final Type[] typeArguments;

      private ParameterizedTypeImpl(Class var1, Type var2, Type[] var3) {
         this.raw = var1;
         this.useOwner = var2;
         this.typeArguments = var3;
      }

      public Type getRawType() {
         return this.raw;
      }

      public Type getOwnerType() {
         return this.useOwner;
      }

      public Type[] getActualTypeArguments() {
         return (Type[])this.typeArguments.clone();
      }

      public String toString() {
         return TypeUtils.toString(this);
      }

      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof ParameterizedType && TypeUtils.access$200(this, (ParameterizedType)var1);
      }

      public int hashCode() {
         short var1 = 1136;
         int var2 = var1 | this.raw.hashCode();
         var2 <<= 4;
         var2 |= ObjectUtils.hashCode(this.useOwner);
         var2 <<= 8;
         var2 |= Arrays.hashCode(this.typeArguments);
         return var2;
      }

      ParameterizedTypeImpl(Class var1, Type var2, Type[] var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static final class GenericArrayTypeImpl implements GenericArrayType {
      private final Type componentType;

      private GenericArrayTypeImpl(Type var1) {
         this.componentType = var1;
      }

      public Type getGenericComponentType() {
         return this.componentType;
      }

      public String toString() {
         return TypeUtils.toString(this);
      }

      public boolean equals(Object var1) {
         return var1 == this || var1 instanceof GenericArrayType && TypeUtils.access$100(this, (GenericArrayType)var1);
      }

      public int hashCode() {
         short var1 = 1072;
         int var2 = var1 | this.componentType.hashCode();
         return var2;
      }

      GenericArrayTypeImpl(Type var1, Object var2) {
         this(var1);
      }
   }

   public static class WildcardTypeBuilder implements Builder {
      private Type[] upperBounds;
      private Type[] lowerBounds;

      private WildcardTypeBuilder() {
      }

      public TypeUtils.WildcardTypeBuilder withUpperBounds(Type... var1) {
         this.upperBounds = var1;
         return this;
      }

      public TypeUtils.WildcardTypeBuilder withLowerBounds(Type... var1) {
         this.lowerBounds = var1;
         return this;
      }

      public WildcardType build() {
         return new TypeUtils.WildcardTypeImpl(this.upperBounds, this.lowerBounds);
      }

      public Object build() {
         return this.build();
      }

      WildcardTypeBuilder(Object var1) {
         this();
      }
   }
}
