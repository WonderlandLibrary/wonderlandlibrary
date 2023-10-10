package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@NotThreadSafe
public class BasicHeaderElement implements HeaderElement, Cloneable {
   private final String name;
   private final String value;
   private final NameValuePair[] parameters;

   public BasicHeaderElement(String var1, String var2, NameValuePair[] var3) {
      this.name = (String)Args.notNull(var1, "Name");
      this.value = var2;
      if (var3 != null) {
         this.parameters = var3;
      } else {
         this.parameters = new NameValuePair[0];
      }

   }

   public BasicHeaderElement(String var1, String var2) {
      this(var1, var2, (NameValuePair[])null);
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public NameValuePair[] getParameters() {
      return (NameValuePair[])this.parameters.clone();
   }

   public int getParameterCount() {
      return this.parameters.length;
   }

   public NameValuePair getParameter(int var1) {
      return this.parameters[var1];
   }

   public NameValuePair getParameterByName(String var1) {
      Args.notNull(var1, "Name");
      NameValuePair var2 = null;
      NameValuePair[] var3 = this.parameters;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         NameValuePair var6 = var3[var5];
         if (var6.getName().equalsIgnoreCase(var1)) {
            var2 = var6;
            break;
         }
      }

      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof HeaderElement)) {
         return false;
      } else {
         BasicHeaderElement var2 = (BasicHeaderElement)var1;
         return this.name.equals(var2.name) && LangUtils.equals(this.value, var2.value) && LangUtils.equals(this.parameters, var2.parameters);
      }
   }

   public int hashCode() {
      byte var1 = 17;
      int var6 = LangUtils.hashCode(var1, this.name);
      var6 = LangUtils.hashCode(var6, this.value);
      NameValuePair[] var2 = this.parameters;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         NameValuePair var5 = var2[var4];
         var6 = LangUtils.hashCode(var6, var5);
      }

      return var6;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.name);
      if (this.value != null) {
         var1.append("=");
         var1.append(this.value);
      }

      NameValuePair[] var2 = this.parameters;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         NameValuePair var5 = var2[var4];
         var1.append("; ");
         var1.append(var5);
      }

      return var1.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
