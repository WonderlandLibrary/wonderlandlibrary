package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.SetCookie2;

@NotThreadSafe
public class BasicClientCookie2 extends BasicClientCookie implements SetCookie2 {
   private static final long serialVersionUID = -7744598295706617057L;
   private String commentURL;
   private int[] ports;
   private boolean discard;

   public BasicClientCookie2(String var1, String var2) {
      super(var1, var2);
   }

   public int[] getPorts() {
      return this.ports;
   }

   public void setPorts(int[] var1) {
      this.ports = var1;
   }

   public String getCommentURL() {
      return this.commentURL;
   }

   public void setCommentURL(String var1) {
      this.commentURL = var1;
   }

   public void setDiscard(boolean var1) {
      this.discard = var1;
   }

   public boolean isPersistent() {
      return !this.discard && super.isPersistent();
   }

   public boolean isExpired(Date var1) {
      return this.discard || super.isExpired(var1);
   }

   public Object clone() throws CloneNotSupportedException {
      BasicClientCookie2 var1 = (BasicClientCookie2)super.clone();
      if (this.ports != null) {
         var1.ports = (int[])this.ports.clone();
      }

      return var1;
   }
}
