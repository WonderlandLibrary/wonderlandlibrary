/*     */ package org.yaml.snakeyaml.introspector;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySubstitute
/*     */   extends Property
/*     */ {
/*  36 */   private static final Logger log = Logger.getLogger(PropertySubstitute.class.getPackage().getName());
/*     */   
/*     */   protected Class<?> targetType;
/*     */   
/*     */   private final String readMethod;
/*     */   
/*     */   private final String writeMethod;
/*     */   private transient Method read;
/*     */   private transient Method write;
/*     */   private Field field;
/*     */   protected Class<?>[] parameters;
/*     */   private Property delegate;
/*     */   private boolean filler;
/*     */   
/*     */   public PropertySubstitute(String name, Class<?> type, String readMethod, String writeMethod, Class<?>... params) {
/*  51 */     super(name, type);
/*  52 */     this.readMethod = readMethod;
/*  53 */     this.writeMethod = writeMethod;
/*  54 */     setActualTypeArguments(params);
/*  55 */     this.filler = false;
/*     */   }
/*     */   
/*     */   public PropertySubstitute(String name, Class<?> type, Class<?>... params) {
/*  59 */     this(name, type, null, null, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getActualTypeArguments() {
/*  64 */     if (this.parameters == null && this.delegate != null) {
/*  65 */       return this.delegate.getActualTypeArguments();
/*     */     }
/*  67 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void setActualTypeArguments(Class<?>... args) {
/*  71 */     if (args != null && args.length > 0) {
/*  72 */       this.parameters = args;
/*     */     } else {
/*  74 */       this.parameters = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(Object object, Object value) throws Exception {
/*  80 */     if (this.write != null) {
/*  81 */       if (!this.filler) {
/*  82 */         this.write.invoke(object, new Object[] { value });
/*  83 */       } else if (value != null) {
/*  84 */         if (value instanceof Collection) {
/*  85 */           Collection<?> collection = (Collection)value;
/*  86 */           for (Object val : collection) {
/*  87 */             this.write.invoke(object, new Object[] { val });
/*     */           } 
/*  89 */         } else if (value instanceof Map) {
/*  90 */           Map<?, ?> map = (Map<?, ?>)value;
/*  91 */           for (Map.Entry<?, ?> entry : map.entrySet()) {
/*  92 */             this.write.invoke(object, new Object[] { entry.getKey(), entry.getValue() });
/*     */           } 
/*  94 */         } else if (value.getClass().isArray()) {
/*     */ 
/*     */ 
/*     */           
/*  98 */           int len = Array.getLength(value);
/*  99 */           for (int i = 0; i < len; i++) {
/* 100 */             this.write.invoke(object, new Object[] { Array.get(value, i) });
/*     */           } 
/*     */         } 
/*     */       } 
/* 104 */     } else if (this.field != null) {
/* 105 */       this.field.set(object, value);
/* 106 */     } else if (this.delegate != null) {
/* 107 */       this.delegate.set(object, value);
/*     */     } else {
/* 109 */       log.warning("No setter/delegate for '" + getName() + "' on object " + object);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(Object object) {
/*     */     try {
/* 117 */       if (this.read != null)
/* 118 */         return this.read.invoke(object, new Object[0]); 
/* 119 */       if (this.field != null) {
/* 120 */         return this.field.get(object);
/*     */       }
/* 122 */     } catch (Exception e) {
/* 123 */       throw new YAMLException("Unable to find getter for property '" + getName() + "' on object " + object + ":" + e);
/*     */     } 
/*     */ 
/*     */     
/* 127 */     if (this.delegate != null) {
/* 128 */       return this.delegate.get(object);
/*     */     }
/* 130 */     throw new YAMLException("No getter or delegate for property '" + getName() + "' on object " + object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Annotation> getAnnotations() {
/* 136 */     Annotation[] annotations = null;
/* 137 */     if (this.read != null) {
/* 138 */       annotations = this.read.getAnnotations();
/* 139 */     } else if (this.field != null) {
/* 140 */       annotations = this.field.getAnnotations();
/*     */     } 
/* 142 */     return (annotations != null) ? Arrays.<Annotation>asList(annotations) : this.delegate.getAnnotations();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/*     */     A annotation;
/* 148 */     if (this.read != null) {
/* 149 */       annotation = this.read.getAnnotation(annotationType);
/* 150 */     } else if (this.field != null) {
/* 151 */       annotation = this.field.getAnnotation(annotationType);
/*     */     } else {
/* 153 */       annotation = this.delegate.getAnnotation(annotationType);
/*     */     } 
/* 155 */     return annotation;
/*     */   }
/*     */   
/*     */   public void setTargetType(Class<?> targetType) {
/* 159 */     if (this.targetType != targetType) {
/* 160 */       this.targetType = targetType;
/*     */       
/* 162 */       String name = getName();
/* 163 */       for (Class<?> c = targetType; c != null; c = c.getSuperclass()) {
/* 164 */         for (Field f : c.getDeclaredFields()) {
/* 165 */           if (f.getName().equals(name)) {
/* 166 */             int modifiers = f.getModifiers();
/* 167 */             if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
/* 168 */               f.setAccessible(true);
/* 169 */               this.field = f;
/*     */             } 
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 175 */       if (this.field == null && log.isLoggable(Level.FINE)) {
/* 176 */         log.fine(String.format("Failed to find field for %s.%s", new Object[] { targetType.getName(), getName() }));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 181 */       if (this.readMethod != null) {
/* 182 */         this.read = discoverMethod(targetType, this.readMethod, new Class[0]);
/*     */       }
/* 184 */       if (this.writeMethod != null) {
/* 185 */         this.filler = false;
/* 186 */         this.write = discoverMethod(targetType, this.writeMethod, new Class[] { getType() });
/* 187 */         if (this.write == null && this.parameters != null) {
/* 188 */           this.filler = true;
/* 189 */           this.write = discoverMethod(targetType, this.writeMethod, this.parameters);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Method discoverMethod(Class<?> type, String name, Class<?>... params) {
/* 196 */     for (Class<?> c = type; c != null; c = c.getSuperclass()) {
/* 197 */       for (Method method : c.getDeclaredMethods()) {
/* 198 */         if (name.equals(method.getName())) {
/* 199 */           Class<?>[] parameterTypes = method.getParameterTypes();
/* 200 */           if (parameterTypes.length == params.length) {
/*     */ 
/*     */             
/* 203 */             boolean found = true;
/* 204 */             for (int i = 0; i < parameterTypes.length; i++) {
/* 205 */               if (!parameterTypes[i].isAssignableFrom(params[i])) {
/* 206 */                 found = false;
/*     */               }
/*     */             } 
/* 209 */             if (found) {
/* 210 */               method.setAccessible(true);
/* 211 */               return method;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 216 */     }  if (log.isLoggable(Level.FINE)) {
/* 217 */       log.fine(String.format("Failed to find [%s(%d args)] for %s.%s", new Object[] { name, Integer.valueOf(params.length), this.targetType.getName(), getName() }));
/*     */     }
/*     */     
/* 220 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 225 */     String n = super.getName();
/* 226 */     if (n != null) {
/* 227 */       return n;
/*     */     }
/* 229 */     return (this.delegate != null) ? this.delegate.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType() {
/* 234 */     Class<?> t = super.getType();
/* 235 */     if (t != null) {
/* 236 */       return t;
/*     */     }
/* 238 */     return (this.delegate != null) ? this.delegate.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/* 243 */     return (this.read != null || this.field != null || (this.delegate != null && this.delegate.isReadable()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/* 248 */     return (this.write != null || this.field != null || (this.delegate != null && this.delegate.isWritable()));
/*     */   }
/*     */   
/*     */   public void setDelegate(Property delegate) {
/* 252 */     this.delegate = delegate;
/* 253 */     if (this.writeMethod != null && this.write == null && !this.filler) {
/* 254 */       this.filler = true;
/* 255 */       this.write = discoverMethod(this.targetType, this.writeMethod, getActualTypeArguments());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\introspector\PropertySubstitute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */