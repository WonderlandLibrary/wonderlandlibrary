/*     */ package org.neverhook.client.event;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.neverhook.client.event.events.Event;
/*     */ import org.neverhook.client.event.events.EventStoppable;
/*     */ import org.neverhook.client.event.types.Priority;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventManager
/*     */ {
/*  17 */   private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY_MAP = new HashMap<>();
/*     */   
/*     */   public static void register(Object object) {
/*  20 */     for (Method method : object.getClass().getDeclaredMethods()) {
/*  21 */       if (!isMethodBad(method))
/*     */       {
/*     */ 
/*     */         
/*  25 */         register(method, object); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void unregister(Object object) {
/*  30 */     for (Iterator<List<MethodData>> iterator = REGISTRY_MAP.values().iterator(); iterator.hasNext(); ) { List<MethodData> dataList = iterator.next();
/*  31 */       dataList.removeIf(data -> data.getSource().equals(object)); }
/*     */     
/*  33 */     cleanMap(true);
/*     */   }
/*     */   
/*     */   private static void register(Method method, Object object) {
/*  37 */     Class<? extends Event> indexClass = (Class)method.getParameterTypes()[0];
/*  38 */     final MethodData data = new MethodData(object, method, ((EventTarget)method.<EventTarget>getAnnotation(EventTarget.class)).value());
/*     */     
/*  40 */     if (!data.getTarget().isAccessible()) {
/*  41 */       data.getTarget().setAccessible(true);
/*     */     }
/*     */     
/*  44 */     if (REGISTRY_MAP.containsKey(indexClass)) {
/*  45 */       if (!((List)REGISTRY_MAP.get(indexClass)).contains(data)) {
/*  46 */         ((List<MethodData>)REGISTRY_MAP.get(indexClass)).add(data);
/*  47 */         sortListValue(indexClass);
/*     */       } 
/*     */     } else {
/*  50 */       REGISTRY_MAP.put(indexClass, new CopyOnWriteArrayList<MethodData>()
/*     */           {
/*     */             private static final long serialVersionUID = 666L;
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void cleanMap(boolean onlyEmptyEntries) {
/*  62 */     Iterator<Map.Entry<Class<? extends Event>, List<MethodData>>> mapIterator = REGISTRY_MAP.entrySet().iterator();
/*     */     
/*  64 */     while (mapIterator.hasNext()) {
/*  65 */       if (!onlyEmptyEntries || ((List)((Map.Entry)mapIterator.next()).getValue()).isEmpty()) {
/*  66 */         mapIterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void sortListValue(Class<? extends Event> indexClass) {
/*  72 */     List<MethodData> sortedList = new CopyOnWriteArrayList<>();
/*     */     
/*  74 */     for (byte priority : Priority.VALUE_ARRAY) {
/*  75 */       for (MethodData data : REGISTRY_MAP.get(indexClass)) {
/*  76 */         if (data.getPriority() == priority) {
/*  77 */           sortedList.add(data);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     REGISTRY_MAP.put(indexClass, sortedList);
/*     */   }
/*     */   
/*     */   private static boolean isMethodBad(Method method) {
/*  86 */     return ((method.getParameterTypes()).length != 1 || !method.isAnnotationPresent((Class)EventTarget.class));
/*     */   }
/*     */   
/*     */   private static boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
/*  90 */     return (isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass));
/*     */   }
/*     */   
/*     */   public static Event call(Event event) {
/*  94 */     List<MethodData> dataList = REGISTRY_MAP.get(event.getClass());
/*     */     
/*  96 */     if (dataList != null) {
/*  97 */       if (event instanceof EventStoppable) {
/*  98 */         EventStoppable stoppable = (EventStoppable)event;
/*     */         
/* 100 */         for (MethodData data : dataList) {
/* 101 */           invoke(data, event);
/*     */           
/* 103 */           if (stoppable.isStopped()) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } else {
/* 108 */         for (MethodData data : dataList) {
/* 109 */           invoke(data, event);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 114 */     return event;
/*     */   }
/*     */   
/*     */   private static void invoke(MethodData data, Event argument) {
/*     */     try {
/* 119 */       data.getTarget().invoke(data.getSource(), new Object[] { argument });
/* 120 */     } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException illegalAccessException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MethodData
/*     */   {
/*     */     private final Object source;
/*     */     
/*     */     private final Method target;
/*     */     
/*     */     private final byte priority;
/*     */     
/*     */     public MethodData(Object source, Method target, byte priority) {
/* 133 */       this.source = source;
/* 134 */       this.target = target;
/* 135 */       this.priority = priority;
/*     */     }
/*     */     
/*     */     public Object getSource() {
/* 139 */       return this.source;
/*     */     }
/*     */     
/*     */     public Method getTarget() {
/* 143 */       return this.target;
/*     */     }
/*     */     
/*     */     public byte getPriority() {
/* 147 */       return this.priority;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\event\EventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */