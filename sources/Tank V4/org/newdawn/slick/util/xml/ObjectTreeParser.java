package org.newdawn.slick.util.xml;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class ObjectTreeParser {
   private HashMap nameToClass = new HashMap();
   private String defaultPackage;
   private ArrayList ignored = new ArrayList();
   private String addMethod = "add";
   static Class class$java$lang$String;
   static Class class$java$lang$Integer;
   static Class class$java$lang$Double;
   static Class class$java$lang$Float;
   static Class class$java$lang$Boolean;
   static Class class$java$lang$Long;

   public ObjectTreeParser() {
   }

   public ObjectTreeParser(String var1) {
      this.defaultPackage = var1;
   }

   public void addElementMapping(String var1, Class var2) {
      this.nameToClass.put(var1, var2);
   }

   public void addIgnoredElement(String var1) {
      this.ignored.add(var1);
   }

   public void setAddMethodName(String var1) {
      this.addMethod = var1;
   }

   public void setDefaultPackage(String var1) {
      this.defaultPackage = var1;
   }

   public Object parse(String var1) throws SlickXMLException {
      return this.parse(var1, ResourceLoader.getResourceAsStream(var1));
   }

   public Object parse(String var1, InputStream var2) throws SlickXMLException {
      XMLParser var3 = new XMLParser();
      XMLElement var4 = var3.parse(var1, var2);
      return this.traverse(var4);
   }

   public Object parseOnto(String var1, Object var2) throws SlickXMLException {
      return this.parseOnto(var1, ResourceLoader.getResourceAsStream(var1), var2);
   }

   public Object parseOnto(String var1, InputStream var2, Object var3) throws SlickXMLException {
      XMLParser var4 = new XMLParser();
      XMLElement var5 = var4.parse(var1, var2);
      return this.traverse(var5, var3);
   }

   private Class getClassForElementName(String var1) {
      Class var2 = (Class)this.nameToClass.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         if (this.defaultPackage != null) {
            try {
               return Class.forName(this.defaultPackage + "." + var1);
            } catch (ClassNotFoundException var4) {
            }
         }

         return null;
      }
   }

   private Object traverse(XMLElement var1) throws SlickXMLException {
      return this.traverse(var1, (Object)null);
   }

   private Object traverse(XMLElement var1, Object var2) throws SlickXMLException {
      String var3 = var1.getName();
      if (this.ignored.contains(var3)) {
         return null;
      } else {
         Class var4;
         if (var2 == null) {
            var4 = this.getClassForElementName(var3);
         } else {
            var4 = var2.getClass();
         }

         if (var4 == null) {
            throw new SlickXMLException("Unable to map element " + var3 + " to a class, define the mapping");
         } else {
            try {
               if (var2 == null) {
                  var2 = var4.newInstance();
                  Method var5 = this.getMethod(var4, "setXMLElementName", new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String});
                  if (var5 != null) {
                     this.invoke(var5, var2, new Object[]{var3});
                  }

                  Method var6 = this.getMethod(var4, "setXMLElementContent", new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String});
                  if (var6 != null) {
                     this.invoke(var6, var2, new Object[]{var1.getContent()});
                  }
               }

               String[] var14 = var1.getAttributeNames();

               String var10;
               for(int var15 = 0; var15 < var14.length; ++var15) {
                  String var7 = "set" + var14[var15];
                  Method var8 = this.findMethod(var4, var7);
                  if (var8 == null) {
                     Field var9 = this.findField(var4, var14[var15]);
                     if (var9 != null) {
                        var10 = var1.getAttribute(var14[var15]);
                        Object var11 = this.typeValue(var10, var9.getType());
                        this.setField(var9, var2, var11);
                     } else {
                        Log.info("Unable to find property on: " + var4 + " for attribute: " + var14[var15]);
                     }
                  } else {
                     String var19 = var1.getAttribute(var14[var15]);
                     Object var21 = this.typeValue(var19, var8.getParameterTypes()[0]);
                     this.invoke(var8, var2, new Object[]{var21});
                  }
               }

               XMLElementList var16 = var1.getChildren();

               for(int var17 = 0; var17 < var16.size(); ++var17) {
                  XMLElement var18 = var16.get(var17);
                  Object var20 = this.traverse(var18);
                  if (var20 != null) {
                     var10 = this.addMethod;
                     Method var22 = this.findMethod(var4, var10, var20.getClass());
                     if (var22 == null) {
                        Log.info("Unable to find method to add: " + var20 + " to " + var4);
                     } else {
                        this.invoke(var22, var2, new Object[]{var20});
                     }
                  }
               }

               return var2;
            } catch (InstantiationException var12) {
               throw new SlickXMLException("Unable to instance " + var4 + " for element " + var3 + ", no zero parameter constructor?", var12);
            } catch (IllegalAccessException var13) {
               throw new SlickXMLException("Unable to instance " + var4 + " for element " + var3 + ", no zero parameter constructor?", var13);
            }
         }
      }
   }

   private Object typeValue(String var1, Class var2) throws SlickXMLException {
      if (var2 == (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
         return var1;
      } else {
         try {
            var2 = this.mapPrimitive(var2);
            return var2.getConstructor(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).newInstance(var1);
         } catch (Exception var4) {
            throw new SlickXMLException("Failed to convert: " + var1 + " to the expected primitive type: " + var2, var4);
         }
      }
   }

   private Class mapPrimitive(Class var1) {
      if (var1 == Integer.TYPE) {
         return class$java$lang$Integer == null ? (class$java$lang$Integer = class$("java.lang.Integer")) : class$java$lang$Integer;
      } else if (var1 == Double.TYPE) {
         return class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double;
      } else if (var1 == Float.TYPE) {
         return class$java$lang$Float == null ? (class$java$lang$Float = class$("java.lang.Float")) : class$java$lang$Float;
      } else if (var1 == Boolean.TYPE) {
         return class$java$lang$Boolean == null ? (class$java$lang$Boolean = class$("java.lang.Boolean")) : class$java$lang$Boolean;
      } else if (var1 == Long.TYPE) {
         return class$java$lang$Long == null ? (class$java$lang$Long = class$("java.lang.Long")) : class$java$lang$Long;
      } else {
         throw new RuntimeException("Unsupported primitive: " + var1);
      }
   }

   private Field findField(Class var1, String var2) {
      Field[] var3 = var1.getDeclaredFields();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getName().equalsIgnoreCase(var2)) {
            if (var3[var4].getType().isPrimitive()) {
               return var3[var4];
            }

            if (var3[var4].getType() == (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String)) {
               return var3[var4];
            }
         }
      }

      return null;
   }

   private Method findMethod(Class var1, String var2) {
      Method[] var3 = var1.getDeclaredMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].getName().equalsIgnoreCase(var2)) {
            Method var5 = var3[var4];
            Class[] var6 = var5.getParameterTypes();
            if (var6.length == 1) {
               return var5;
            }
         }
      }

      return null;
   }

   private Method findMethod(Class var1, String var2, Class var3) {
      Method[] var4 = var1.getDeclaredMethods();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].getName().equalsIgnoreCase(var2)) {
            Method var6 = var4[var5];
            Class[] var7 = var6.getParameterTypes();
            if (var7.length == 1 && var6.getParameterTypes()[0].isAssignableFrom(var3)) {
               return var6;
            }
         }
      }

      return null;
   }

   private void setField(Field var1, Object var2, Object var3) throws SlickXMLException {
      try {
         var1.setAccessible(true);
         var1.set(var2, var3);
      } catch (IllegalArgumentException var6) {
         throw new SlickXMLException("Failed to set: " + var1 + " for an XML attribute, is it valid?", var6);
      } catch (IllegalAccessException var7) {
         throw new SlickXMLException("Failed to set: " + var1 + " for an XML attribute, is it valid?", var7);
      }

      var1.setAccessible(false);
   }

   private void invoke(Method var1, Object var2, Object[] var3) throws SlickXMLException {
      try {
         var1.setAccessible(true);
         var1.invoke(var2, var3);
      } catch (IllegalArgumentException var6) {
         throw new SlickXMLException("Failed to invoke: " + var1 + " for an XML attribute, is it valid?", var6);
      } catch (IllegalAccessException var7) {
         throw new SlickXMLException("Failed to invoke: " + var1 + " for an XML attribute, is it valid?", var7);
      } catch (InvocationTargetException var8) {
         throw new SlickXMLException("Failed to invoke: " + var1 + " for an XML attribute, is it valid?", var8);
      }

      var1.setAccessible(false);
   }

   private Method getMethod(Class var1, String var2, Class[] var3) {
      try {
         return var1.getMethod(var2, var3);
      } catch (SecurityException var5) {
         return null;
      } catch (NoSuchMethodException var6) {
         return null;
      }
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw (new NoClassDefFoundError()).initCause(var2);
      }
   }
}
