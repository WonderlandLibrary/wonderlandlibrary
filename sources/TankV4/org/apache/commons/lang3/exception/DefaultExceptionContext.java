package org.apache.commons.lang3.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultExceptionContext implements ExceptionContext, Serializable {
   private static final long serialVersionUID = 20110706L;
   private final List contextValues = new ArrayList();

   public DefaultExceptionContext addContextValue(String var1, Object var2) {
      this.contextValues.add(new ImmutablePair(var1, var2));
      return this;
   }

   public DefaultExceptionContext setContextValue(String var1, Object var2) {
      Iterator var3 = this.contextValues.iterator();

      while(var3.hasNext()) {
         Pair var4 = (Pair)var3.next();
         if (StringUtils.equals(var1, (CharSequence)var4.getKey())) {
            var3.remove();
         }
      }

      this.addContextValue(var1, var2);
      return this;
   }

   public List getContextValues(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.contextValues.iterator();

      while(var3.hasNext()) {
         Pair var4 = (Pair)var3.next();
         if (StringUtils.equals(var1, (CharSequence)var4.getKey())) {
            var2.add(var4.getValue());
         }
      }

      return var2;
   }

   public Object getFirstContextValue(String var1) {
      Iterator var2 = this.contextValues.iterator();

      Pair var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Pair)var2.next();
      } while(!StringUtils.equals(var1, (CharSequence)var3.getKey()));

      return var3.getValue();
   }

   public Set getContextLabels() {
      HashSet var1 = new HashSet();
      Iterator var2 = this.contextValues.iterator();

      while(var2.hasNext()) {
         Pair var3 = (Pair)var2.next();
         var1.add(var3.getKey());
      }

      return var1;
   }

   public List getContextEntries() {
      return this.contextValues;
   }

   public String getFormattedExceptionMessage(String var1) {
      StringBuilder var2 = new StringBuilder(256);
      if (var1 != null) {
         var2.append(var1);
      }

      if (this.contextValues.size() > 0) {
         if (var2.length() > 0) {
            var2.append('\n');
         }

         var2.append("Exception Context:\n");
         int var3 = 0;

         for(Iterator var4 = this.contextValues.iterator(); var4.hasNext(); var2.append("]\n")) {
            Pair var5 = (Pair)var4.next();
            var2.append("\t[");
            ++var3;
            var2.append(var3);
            var2.append(':');
            var2.append((String)var5.getKey());
            var2.append("=");
            Object var6 = var5.getValue();
            if (var6 == null) {
               var2.append("null");
            } else {
               String var7;
               try {
                  var7 = var6.toString();
               } catch (Exception var9) {
                  var7 = "Exception thrown on toString(): " + ExceptionUtils.getStackTrace(var9);
               }

               var2.append(var7);
            }
         }

         var2.append("---------------------------------");
      }

      return var2.toString();
   }

   public ExceptionContext setContextValue(String var1, Object var2) {
      return this.setContextValue(var1, var2);
   }

   public ExceptionContext addContextValue(String var1, Object var2) {
      return this.addContextValue(var1, var2);
   }
}
