package org.apache.http.params;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;

/** @deprecated */
@Deprecated
@ThreadSafe
public class BasicHttpParams extends AbstractHttpParams implements Serializable, Cloneable {
   private static final long serialVersionUID = -7086398485908701455L;
   private final Map parameters = new ConcurrentHashMap();

   public Object getParameter(String var1) {
      return this.parameters.get(var1);
   }

   public HttpParams setParameter(String var1, Object var2) {
      if (var1 == null) {
         return this;
      } else {
         if (var2 != null) {
            this.parameters.put(var1, var2);
         } else {
            this.parameters.remove(var1);
         }

         return this;
      }
   }

   public boolean removeParameter(String var1) {
      if (this.parameters.containsKey(var1)) {
         this.parameters.remove(var1);
         return true;
      } else {
         return false;
      }
   }

   public void setParameters(String[] var1, Object var2) {
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         this.setParameter(var6, var2);
      }

   }

   public boolean isParameterSet(String var1) {
      return this.getParameter(var1) != null;
   }

   public boolean isParameterSetLocally(String var1) {
      return this.parameters.get(var1) != null;
   }

   public void clear() {
      this.parameters.clear();
   }

   public HttpParams copy() {
      try {
         return (HttpParams)this.clone();
      } catch (CloneNotSupportedException var2) {
         throw new UnsupportedOperationException("Cloning not supported");
      }
   }

   public Object clone() throws CloneNotSupportedException {
      BasicHttpParams var1 = (BasicHttpParams)super.clone();
      this.copyParams(var1);
      return var1;
   }

   public void copyParams(HttpParams var1) {
      Iterator var2 = this.parameters.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.setParameter((String)var3.getKey(), var3.getValue());
      }

   }

   public Set getNames() {
      return new HashSet(this.parameters.keySet());
   }
}
