package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ICUService extends ICUNotifier {
   protected final String name;
   private static final boolean DEBUG = ICUDebug.enabled("service");
   private final ICURWLock factoryLock = new ICURWLock();
   private final List factories = new ArrayList();
   private int defaultSize = 0;
   private SoftReference cacheref;
   private SoftReference idref;
   private ICUService.LocaleRef dnref;

   public ICUService() {
      this.name = "";
   }

   public ICUService(String var1) {
      this.name = var1;
   }

   public Object get(String var1) {
      return this.getKey(this.createKey(var1), (String[])null);
   }

   public Object get(String var1, String[] var2) {
      if (var1 == null) {
         throw new NullPointerException("descriptor must not be null");
      } else {
         return this.getKey(this.createKey(var1), var2);
      }
   }

   public Object getKey(ICUService.Key var1) {
      return this.getKey(var1, (String[])null);
   }

   public Object getKey(ICUService.Key var1, String[] var2) {
      return this.getKey(var1, var2, (ICUService.Factory)null);
   }

   public Object getKey(ICUService.Key var1, String[] var2, ICUService.Factory var3) {
      if (this.factories.size() == 0) {
         return this.handleDefault(var1, var2);
      } else {
         if (DEBUG) {
            System.out.println("Service: " + this.name + " key: " + var1.canonicalID());
         }

         ICUService.CacheEntry var4 = null;
         if (var1 != null) {
            this.factoryLock.acquireRead();
            Map var5 = null;
            SoftReference var6 = this.cacheref;
            if (var6 != null) {
               if (DEBUG) {
                  System.out.println("Service " + this.name + " ref exists");
               }

               var5 = (Map)var6.get();
            }

            if (var5 == null) {
               if (DEBUG) {
                  System.out.println("Service " + this.name + " cache was empty");
               }

               var5 = Collections.synchronizedMap(new HashMap());
               var6 = new SoftReference(var5);
            }

            String var7 = null;
            ArrayList var8 = null;
            boolean var9 = false;
            int var10 = 0;
            int var11 = 0;
            int var12 = this.factories.size();
            boolean var13 = true;
            int var14;
            if (var3 != null) {
               for(var14 = 0; var14 < var12; ++var14) {
                  if (var3 == this.factories.get(var14)) {
                     var11 = var14 + 1;
                     break;
                  }
               }

               if (var11 == 0) {
                  throw new IllegalStateException("Factory " + var3 + "not registered with service: " + this);
               }

               var13 = false;
            }

            label137:
            do {
               var7 = var1.currentDescriptor();
               if (DEBUG) {
                  System.out.println(this.name + "[" + var10++ + "] looking for: " + var7);
               }

               var4 = (ICUService.CacheEntry)var5.get(var7);
               if (var4 != null) {
                  if (DEBUG) {
                     System.out.println(this.name + " found with descriptor: " + var7);
                  }
                  break;
               }

               if (DEBUG) {
                  System.out.println("did not find: " + var7 + " in cache");
               }

               var9 = var13;
               var14 = var11;

               while(var14 < var12) {
                  ICUService.Factory var15 = (ICUService.Factory)this.factories.get(var14++);
                  if (DEBUG) {
                     System.out.println("trying factory[" + (var14 - 1) + "] " + var15.toString());
                  }

                  Object var16 = var15.create(var1, this);
                  if (var16 != null) {
                     var4 = new ICUService.CacheEntry(var7, var16);
                     if (DEBUG) {
                        System.out.println(this.name + " factory supported: " + var7 + ", caching");
                     }
                     break label137;
                  }

                  if (DEBUG) {
                     System.out.println("factory did not support: " + var7);
                  }
               }

               if (var8 == null) {
                  var8 = new ArrayList(5);
               }

               var8.add(var7);
            } while(var1.fallback());

            if (var4 != null) {
               if (var9) {
                  if (DEBUG) {
                     System.out.println("caching '" + var4.actualDescriptor + "'");
                  }

                  var5.put(var4.actualDescriptor, var4);
                  String var19;
                  if (var8 != null) {
                     for(Iterator var18 = var8.iterator(); var18.hasNext(); var5.put(var19, var4)) {
                        var19 = (String)var18.next();
                        if (DEBUG) {
                           System.out.println(this.name + " adding descriptor: '" + var19 + "' for actual: '" + var4.actualDescriptor + "'");
                        }
                     }
                  }

                  this.cacheref = var6;
               }

               if (var2 != null) {
                  if (var4.actualDescriptor.indexOf("/") == 0) {
                     var2[0] = var4.actualDescriptor.substring(1);
                  } else {
                     var2[0] = var4.actualDescriptor;
                  }
               }

               if (DEBUG) {
                  System.out.println("found in service: " + this.name);
               }

               Object var20 = var4.service;
               this.factoryLock.releaseRead();
               return var20;
            }

            this.factoryLock.releaseRead();
         }

         if (DEBUG) {
            System.out.println("not found in service: " + this.name);
         }

         return this.handleDefault(var1, var2);
      }
   }

   protected Object handleDefault(ICUService.Key var1, String[] var2) {
      return null;
   }

   public Set getVisibleIDs() {
      return this.getVisibleIDs((String)null);
   }

   public Set getVisibleIDs(String var1) {
      Object var2 = this.getVisibleIDMap().keySet();
      ICUService.Key var3 = this.createKey(var1);
      if (var3 != null) {
         HashSet var4 = new HashSet(((Set)var2).size());
         Iterator var5 = ((Set)var2).iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            if (var3.isFallbackOf(var6)) {
               var4.add(var6);
            }
         }

         var2 = var4;
      }

      return (Set)var2;
   }

   private Map getVisibleIDMap() {
      Map var1 = null;
      SoftReference var2 = this.idref;
      if (var2 != null) {
         var1 = (Map)var2.get();
      }

      while(var1 == null) {
         synchronized(this){}
         if (var2 != this.idref && this.idref != null) {
            var2 = this.idref;
            var1 = (Map)var2.get();
         } else {
            this.factoryLock.acquireRead();
            HashMap var8 = new HashMap();
            ListIterator var4 = this.factories.listIterator(this.factories.size());

            while(var4.hasPrevious()) {
               ICUService.Factory var5 = (ICUService.Factory)var4.previous();
               var5.updateVisibleIDs(var8);
            }

            var1 = Collections.unmodifiableMap(var8);
            this.idref = new SoftReference(var1);
            this.factoryLock.releaseRead();
         }
      }

      return var1;
   }

   public String getDisplayName(String var1) {
      return this.getDisplayName(var1, ULocale.getDefault(ULocale.Category.DISPLAY));
   }

   public String getDisplayName(String var1, ULocale var2) {
      Map var3 = this.getVisibleIDMap();
      ICUService.Factory var4 = (ICUService.Factory)var3.get(var1);
      if (var4 != null) {
         return var4.getDisplayName(var1, var2);
      } else {
         ICUService.Key var5 = this.createKey(var1);

         do {
            if (!var5.fallback()) {
               return null;
            }

            var4 = (ICUService.Factory)var3.get(var5.currentID());
         } while(var4 == null);

         return var4.getDisplayName(var1, var2);
      }
   }

   public SortedMap getDisplayNames() {
      ULocale var1 = ULocale.getDefault(ULocale.Category.DISPLAY);
      return this.getDisplayNames(var1, (Comparator)null, (String)null);
   }

   public SortedMap getDisplayNames(ULocale var1) {
      return this.getDisplayNames(var1, (Comparator)null, (String)null);
   }

   public SortedMap getDisplayNames(ULocale var1, Comparator var2) {
      return this.getDisplayNames(var1, var2, (String)null);
   }

   public SortedMap getDisplayNames(ULocale var1, String var2) {
      return this.getDisplayNames(var1, (Comparator)null, var2);
   }

   public SortedMap getDisplayNames(ULocale var1, Comparator var2, String var3) {
      SortedMap var4 = null;
      ICUService.LocaleRef var5 = this.dnref;
      if (var5 != null) {
         var4 = var5.get(var1, var2);
      }

      Iterator var8;
      Entry var9;
      while(var4 == null) {
         synchronized(this){}
         if (var5 != this.dnref && this.dnref != null) {
            var5 = this.dnref;
            var4 = var5.get(var1, var2);
         } else {
            TreeMap var13 = new TreeMap(var2);
            Map var7 = this.getVisibleIDMap();
            var8 = var7.entrySet().iterator();

            while(var8.hasNext()) {
               var9 = (Entry)var8.next();
               String var10 = (String)var9.getKey();
               ICUService.Factory var11 = (ICUService.Factory)var9.getValue();
               var13.put(var11.getDisplayName(var10, var1), var10);
            }

            var4 = Collections.unmodifiableSortedMap(var13);
            this.dnref = new ICUService.LocaleRef(var4, var1, var2);
         }
      }

      ICUService.Key var6 = this.createKey(var3);
      if (var6 == null) {
         return var4;
      } else {
         TreeMap var14 = new TreeMap(var4);
         var8 = var14.entrySet().iterator();

         while(var8.hasNext()) {
            var9 = (Entry)var8.next();
            if (!var6.isFallbackOf((String)var9.getValue())) {
               var8.remove();
            }
         }

         return var14;
      }
   }

   public final List factories() {
      this.factoryLock.acquireRead();
      ArrayList var1 = new ArrayList(this.factories);
      this.factoryLock.releaseRead();
      return var1;
   }

   public ICUService.Factory registerObject(Object var1, String var2) {
      return this.registerObject(var1, var2, true);
   }

   public ICUService.Factory registerObject(Object var1, String var2, boolean var3) {
      String var4 = this.createKey(var2).canonicalID();
      return this.registerFactory(new ICUService.SimpleFactory(var1, var4, var3));
   }

   public final ICUService.Factory registerFactory(ICUService.Factory var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.factoryLock.acquireWrite();
         this.factories.add(0, var1);
         this.clearCaches();
         this.factoryLock.releaseWrite();
         this.notifyChanged();
         return var1;
      }
   }

   public final boolean unregisterFactory(ICUService.Factory var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         boolean var2 = false;
         this.factoryLock.acquireWrite();
         if (this.factories.remove(var1)) {
            var2 = true;
            this.clearCaches();
         }

         this.factoryLock.releaseWrite();
         if (var2) {
            this.notifyChanged();
         }

         return var2;
      }
   }

   public final void reset() {
      this.factoryLock.acquireWrite();
      this.reInitializeFactories();
      this.clearCaches();
      this.factoryLock.releaseWrite();
      this.notifyChanged();
   }

   protected void reInitializeFactories() {
      this.factories.clear();
   }

   public boolean isDefault() {
      return this.factories.size() == this.defaultSize;
   }

   protected void markDefault() {
      this.defaultSize = this.factories.size();
   }

   public ICUService.Key createKey(String var1) {
      return var1 == null ? null : new ICUService.Key(var1);
   }

   protected void clearCaches() {
      this.cacheref = null;
      this.idref = null;
      this.dnref = null;
   }

   protected void clearServiceCache() {
      this.cacheref = null;
   }

   protected boolean acceptsListener(EventListener var1) {
      return var1 instanceof ICUService.ServiceListener;
   }

   protected void notifyListener(EventListener var1) {
      ((ICUService.ServiceListener)var1).serviceChanged(this);
   }

   public String stats() {
      ICURWLock.Stats var1 = this.factoryLock.resetStats();
      return var1 != null ? var1.toString() : "no stats";
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return super.toString() + "{" + this.name + "}";
   }

   public interface ServiceListener extends EventListener {
      void serviceChanged(ICUService var1);
   }

   private static class LocaleRef {
      private final ULocale locale;
      private SoftReference ref;
      private Comparator com;

      LocaleRef(SortedMap var1, ULocale var2, Comparator var3) {
         this.locale = var2;
         this.com = var3;
         this.ref = new SoftReference(var1);
      }

      SortedMap get(ULocale var1, Comparator var2) {
         SortedMap var3 = (SortedMap)this.ref.get();
         return var3 == null || !this.locale.equals(var1) || this.com != var2 && (this.com == null || !this.com.equals(var2)) ? null : var3;
      }
   }

   private static final class CacheEntry {
      final String actualDescriptor;
      final Object service;

      CacheEntry(String var1, Object var2) {
         this.actualDescriptor = var1;
         this.service = var2;
      }
   }

   public static class SimpleFactory implements ICUService.Factory {
      protected Object instance;
      protected String id;
      protected boolean visible;

      public SimpleFactory(Object var1, String var2) {
         this(var1, var2, true);
      }

      public SimpleFactory(Object var1, String var2, boolean var3) {
         if (var1 != null && var2 != null) {
            this.instance = var1;
            this.id = var2;
            this.visible = var3;
         } else {
            throw new IllegalArgumentException("Instance or id is null");
         }
      }

      public Object create(ICUService.Key var1, ICUService var2) {
         return this.id.equals(var1.currentID()) ? this.instance : null;
      }

      public void updateVisibleIDs(Map var1) {
         if (this.visible) {
            var1.put(this.id, this);
         } else {
            var1.remove(this.id);
         }

      }

      public String getDisplayName(String var1, ULocale var2) {
         return this.visible && this.id.equals(var1) ? var1 : null;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(super.toString());
         var1.append(", id: ");
         var1.append(this.id);
         var1.append(", visible: ");
         var1.append(this.visible);
         return var1.toString();
      }
   }

   public interface Factory {
      Object create(ICUService.Key var1, ICUService var2);

      void updateVisibleIDs(Map var1);

      String getDisplayName(String var1, ULocale var2);
   }

   public static class Key {
      private final String id;

      public Key(String var1) {
         this.id = var1;
      }

      public final String id() {
         return this.id;
      }

      public String canonicalID() {
         return this.id;
      }

      public String currentID() {
         return this.canonicalID();
      }

      public String currentDescriptor() {
         return "/" + this.currentID();
      }

      public boolean fallback() {
         return false;
      }

      public boolean isFallbackOf(String var1) {
         return this.canonicalID().equals(var1);
      }
   }
}
