package javafx.fxml;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.util.Builder;
import sun.reflect.misc.MethodUtil;

final class JavaFXBuilder {
   private static final Object[] NO_ARGS = new Object[0];
   private static final Class[] NO_SIG = new Class[0];
   private final Class builderClass;
   private final Method createMethod;
   private final Method buildMethod;
   private final Map methods = new HashMap();
   private final Map getters = new HashMap();
   private final Map setters = new HashMap();

   JavaFXBuilder() {
      this.builderClass = null;
      this.createMethod = null;
      this.buildMethod = null;
   }

   JavaFXBuilder(Class var1) throws NoSuchMethodException, InstantiationException, IllegalAccessException {
      this.builderClass = var1;
      this.createMethod = MethodUtil.getMethod(var1, "create", NO_SIG);
      this.buildMethod = MethodUtil.getMethod(var1, "build", NO_SIG);

      assert Modifier.isStatic(this.createMethod.getModifiers());

      assert !Modifier.isStatic(this.buildMethod.getModifiers());

   }

   Builder createBuilder() {
      return new ObjectBuilder();
   }

   private Method findMethod(String var1) {
      if (var1.length() > 1 && Character.isUpperCase(var1.charAt(1))) {
         var1 = Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
      }

      Method[] var2 = MethodUtil.getMethods(this.builderClass);
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method var5 = var2[var4];
         if (var5.getName().equals(var1)) {
            return var5;
         }
      }

      throw new IllegalArgumentException("Method " + var1 + " could not be found at class " + this.builderClass.getName());
   }

   public Class getTargetClass() {
      return this.buildMethod.getReturnType();
   }

   final class ObjectBuilder extends AbstractMap implements Builder {
      private final Map containers;
      private Object builder;
      private Map properties;

      private ObjectBuilder() {
         this.containers = new HashMap();
         this.builder = null;

         try {
            this.builder = MethodUtil.invoke(JavaFXBuilder.this.createMethod, (Object)null, JavaFXBuilder.NO_ARGS);
         } catch (Exception var3) {
            throw new RuntimeException("Creation of the builder " + JavaFXBuilder.this.builderClass.getName() + " failed.", var3);
         }
      }

      public Object build() {
         Iterator var1 = this.containers.entrySet().iterator();

         while(var1.hasNext()) {
            Map.Entry var2 = (Map.Entry)var1.next();
            this.put((String)var2.getKey(), var2.getValue());
         }

         Object var10;
         try {
            var10 = MethodUtil.invoke(JavaFXBuilder.this.buildMethod, this.builder, JavaFXBuilder.NO_ARGS);
            if (this.properties != null && var10 instanceof Node) {
               ((Node)var10).getProperties().putAll(this.properties);
            }
         } catch (InvocationTargetException var7) {
            throw new RuntimeException(var7);
         } catch (IllegalAccessException var8) {
            throw new RuntimeException(var8);
         } finally {
            this.builder = null;
         }

         return var10;
      }

      public int size() {
         throw new UnsupportedOperationException();
      }

      public boolean isEmpty() {
         throw new UnsupportedOperationException();
      }

      public boolean containsKey(Object var1) {
         return this.getTemporaryContainer(var1.toString()) != null;
      }

      public boolean containsValue(Object var1) {
         throw new UnsupportedOperationException();
      }

      public Object get(Object var1) {
         return this.getTemporaryContainer(var1.toString());
      }

      public Object put(String var1, Object var2) {
         if (Node.class.isAssignableFrom(JavaFXBuilder.this.getTargetClass()) && "properties".equals(var1)) {
            this.properties = (Map)var2;
            return null;
         } else {
            try {
               Method var3 = (Method)JavaFXBuilder.this.methods.get(var1);
               if (var3 == null) {
                  var3 = JavaFXBuilder.this.findMethod(var1);
                  JavaFXBuilder.this.methods.put(var1, var3);
               }

               try {
                  Class var4 = var3.getParameterTypes()[0];
                  if (var4.isArray()) {
                     List var5;
                     if (var2 instanceof List) {
                        var5 = (List)var2;
                     } else {
                        var5 = Arrays.asList(var2.toString().split(","));
                     }

                     Class var6 = var4.getComponentType();
                     Object var7 = Array.newInstance(var6, var5.size());

                     for(int var8 = 0; var8 < var5.size(); ++var8) {
                        Array.set(var7, var8, BeanAdapter.coerce(var5.get(var8), var6));
                     }

                     var2 = var7;
                  }

                  MethodUtil.invoke(var3, this.builder, new Object[]{BeanAdapter.coerce(var2, var4)});
               } catch (Exception var9) {
                  Logger.getLogger(JavaFXBuilder.class.getName()).log(Level.WARNING, "Method " + var3.getName() + " failed", var9);
               }

               return null;
            } catch (Exception var10) {
               Logger.getLogger(JavaFXBuilder.class.getName()).log(Level.WARNING, "Failed to set " + JavaFXBuilder.this.getTargetClass() + "." + var1 + " using " + JavaFXBuilder.this.builderClass, var10);
               return null;
            }
         }
      }

      Object getReadOnlyProperty(String var1) {
         if (JavaFXBuilder.this.setters.get(var1) != null) {
            return null;
         } else {
            Method var2 = (Method)JavaFXBuilder.this.getters.get(var1);
            if (var2 == null) {
               Method var3 = null;
               Class var4 = JavaFXBuilder.this.getTargetClass();
               String var5 = Character.toUpperCase(var1.charAt(0)) + var1.substring(1);

               try {
                  var2 = MethodUtil.getMethod(var4, "get" + var5, JavaFXBuilder.NO_SIG);
                  var3 = MethodUtil.getMethod(var4, "set" + var5, new Class[]{var2.getReturnType()});
               } catch (Exception var7) {
               }

               if (var2 != null) {
                  JavaFXBuilder.this.getters.put(var1, var2);
                  JavaFXBuilder.this.setters.put(var1, var3);
               }

               if (var3 != null) {
                  return null;
               }
            }

            Class var8;
            if (var2 == null) {
               Method var9 = JavaFXBuilder.this.findMethod(var1);
               if (var9 == null) {
                  return null;
               }

               var8 = var9.getParameterTypes()[0];
               if (var8.isArray()) {
                  var8 = List.class;
               }
            } else {
               var8 = var2.getReturnType();
            }

            if (ObservableMap.class.isAssignableFrom(var8)) {
               return FXCollections.observableMap(new HashMap());
            } else if (Map.class.isAssignableFrom(var8)) {
               return new HashMap();
            } else if (ObservableList.class.isAssignableFrom(var8)) {
               return FXCollections.observableArrayList();
            } else if (List.class.isAssignableFrom(var8)) {
               return new ArrayList();
            } else {
               return Set.class.isAssignableFrom(var8) ? new HashSet() : null;
            }
         }
      }

      public Object getTemporaryContainer(String var1) {
         Object var2 = this.containers.get(var1);
         if (var2 == null) {
            var2 = this.getReadOnlyProperty(var1);
            if (var2 != null) {
               this.containers.put(var1, var2);
            }
         }

         return var2;
      }

      public Object remove(Object var1) {
         throw new UnsupportedOperationException();
      }

      public void putAll(Map var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         throw new UnsupportedOperationException();
      }

      public Set keySet() {
         throw new UnsupportedOperationException();
      }

      public Collection values() {
         throw new UnsupportedOperationException();
      }

      public Set entrySet() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      ObjectBuilder(Object var2) {
         this();
      }
   }
}
