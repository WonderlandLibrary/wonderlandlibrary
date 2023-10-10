package org.apache.logging.log4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MarkerManager {
   private static ConcurrentMap markerMap = new ConcurrentHashMap();

   private MarkerManager() {
   }

   public static Marker getMarker(String var0) {
      markerMap.putIfAbsent(var0, new MarkerManager.Log4jMarker(var0));
      return (Marker)markerMap.get(var0);
   }

   public static Marker getMarker(String var0, String var1) {
      Marker var2 = (Marker)markerMap.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Parent Marker " + var1 + " has not been defined");
      } else {
         return getMarker(var0, var2);
      }
   }

   public static Marker getMarker(String var0, Marker var1) {
      markerMap.putIfAbsent(var0, new MarkerManager.Log4jMarker(var0, var1));
      return (Marker)markerMap.get(var0);
   }

   private static class Log4jMarker implements Marker {
      private static final long serialVersionUID = 100L;
      private final String name;
      private final Marker parent;

      public Log4jMarker(String var1) {
         this.name = var1;
         this.parent = null;
      }

      public Log4jMarker(String var1, Marker var2) {
         this.name = var1;
         this.parent = var2;
      }

      public String getName() {
         return this.name;
      }

      public Marker getParent() {
         return this.parent;
      }

      public boolean isInstanceOf(Marker var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("A marker parameter is required");
         } else {
            Object var2 = this;

            while(var2 != var1) {
               var2 = ((Marker)var2).getParent();
               if (var2 == null) {
                  return false;
               }
            }

            return true;
         }
      }

      public boolean isInstanceOf(String var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("A marker name is required");
         } else {
            Object var2 = this;

            while(!var1.equals(((Marker)var2).getName())) {
               var2 = ((Marker)var2).getParent();
               if (var2 == null) {
                  return false;
               }
            }

            return true;
         }
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && var1 instanceof Marker) {
            Marker var2 = (Marker)var1;
            if (this.name != null) {
               if (!this.name.equals(var2.getName())) {
                  return false;
               }
            } else if (var2.getName() != null) {
               return false;
            }

            return true;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.name != null ? this.name.hashCode() : 0;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(this.name);
         if (this.parent != null) {
            Marker var2 = this.parent;
            var1.append("[ ");

            for(boolean var3 = true; var2 != null; var2 = var2.getParent()) {
               if (!var3) {
                  var1.append(", ");
               }

               var1.append(var2.getName());
               var3 = false;
            }

            var1.append(" ]");
         }

         return var1.toString();
      }
   }
}
