package com.sun.webkit;

import com.sun.webkit.event.WCChangeEvent;
import com.sun.webkit.event.WCChangeListener;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.network.URLs;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class BackForwardList {
   private final WebPage page;
   private final List listenerList = new LinkedList();

   BackForwardList(WebPage var1) {
      this.page = var1;
      var1.addLoadListenerClient(new LoadListenerClient() {
         public void dispatchLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
            if (var3 == 14) {
               Entry var9 = BackForwardList.this.getCurrentEntry();
               if (var9 != null) {
                  var9.updateLastVisitedDate();
               }
            }

         }

         public void dispatchResourceLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
         }
      });
   }

   public int size() {
      return bflSize(this.page.getPage());
   }

   public int getMaximumSize() {
      return bflGetMaximumSize(this.page.getPage());
   }

   public void setMaximumSize(int var1) {
      bflSetMaximumSize(this.page.getPage(), var1);
   }

   public int getCurrentIndex() {
      return bflGetCurrentIndex(this.page.getPage());
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public void setEnabled(boolean var1) {
      bflSetEnabled(this.page.getPage(), var1);
   }

   public boolean isEnabled() {
      return bflIsEnabled(this.page.getPage());
   }

   public Entry get(int var1) {
      Entry var2 = (Entry)bflGet(this.page.getPage(), var1);
      return var2;
   }

   public Entry getCurrentEntry() {
      return this.get(this.getCurrentIndex());
   }

   public void clearBackForwardListForDRT() {
      bflClearBackForwardListForDRT(this.page.getPage());
   }

   public int indexOf(Entry var1) {
      return bflIndexOf(this.page.getPage(), var1.pitem, false);
   }

   public boolean contains(Entry var1) {
      return this.indexOf(var1) >= 0;
   }

   public Entry[] toArray() {
      int var1 = this.size();
      Entry[] var2 = new Entry[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = this.get(var3);
      }

      return var2;
   }

   public void setCurrentIndex(int var1) {
      if (bflSetCurrentIndex(this.page.getPage(), var1) < 0) {
         throw new IllegalArgumentException("invalid index: " + var1);
      }
   }

   private boolean canGoBack(int var1) {
      return var1 > 0;
   }

   public boolean canGoBack() {
      return this.canGoBack(this.getCurrentIndex());
   }

   public boolean goBack() {
      int var1 = this.getCurrentIndex();
      if (this.canGoBack(var1)) {
         this.setCurrentIndex(var1 - 1);
         return true;
      } else {
         return false;
      }
   }

   private boolean canGoForward(int var1) {
      return var1 < this.size() - 1;
   }

   public boolean canGoForward() {
      return this.canGoForward(this.getCurrentIndex());
   }

   public boolean goForward() {
      int var1 = this.getCurrentIndex();
      if (this.canGoForward(var1)) {
         this.setCurrentIndex(var1 + 1);
         return true;
      } else {
         return false;
      }
   }

   public void addChangeListener(WCChangeListener var1) {
      if (var1 != null) {
         if (this.listenerList.isEmpty()) {
            bflSetHostObject(this.page.getPage(), this);
         }

         this.listenerList.add(var1);
      }
   }

   public void removeChangeListener(WCChangeListener var1) {
      if (var1 != null) {
         this.listenerList.remove(var1);
         if (this.listenerList.isEmpty()) {
            bflSetHostObject(this.page.getPage(), (Object)null);
         }

      }
   }

   public WCChangeListener[] getChangeListeners() {
      return (WCChangeListener[])this.listenerList.toArray(new WCChangeListener[0]);
   }

   private void notifyChanged() {
      Iterator var1 = this.listenerList.iterator();

      while(var1.hasNext()) {
         WCChangeListener var2 = (WCChangeListener)var1.next();
         var2.stateChanged(new WCChangeEvent(this));
      }

   }

   private static native String bflItemGetURL(long var0);

   private static native String bflItemGetTitle(long var0);

   private static native WCImage bflItemGetIcon(long var0);

   private static native long bflItemGetLastVisitedDate(long var0);

   private static native boolean bflItemIsTargetItem(long var0);

   private static native Entry[] bflItemGetChildren(long var0, long var2);

   private static native String bflItemGetTarget(long var0);

   private static native void bflClearBackForwardListForDRT(long var0);

   private static native int bflSize(long var0);

   private static native int bflGetMaximumSize(long var0);

   private static native void bflSetMaximumSize(long var0, int var2);

   private static native int bflGetCurrentIndex(long var0);

   private static native int bflIndexOf(long var0, long var2, boolean var4);

   private static native void bflSetEnabled(long var0, boolean var2);

   private static native boolean bflIsEnabled(long var0);

   private static native Object bflGet(long var0, int var2);

   private static native int bflSetCurrentIndex(long var0, int var2);

   private static native void bflSetHostObject(long var0, Object var2);

   public static final class Entry {
      private long pitem = 0L;
      private long ppage = 0L;
      private Entry[] children;
      private URL url;
      private String title;
      private Date lastVisitedDate;
      private WCImage icon;
      private String target;
      private boolean isTargetItem;
      private final List listenerList = new LinkedList();

      private Entry(long var1, long var3) {
         this.pitem = var1;
         this.ppage = var3;
         this.getURL();
         this.getTitle();
         this.getLastVisitedDate();
         this.getIcon();
         this.getTarget();
         this.isTargetItem();
         this.getChildren();
      }

      private void notifyItemDestroyed() {
         this.pitem = 0L;
      }

      private void notifyItemChanged() {
         Iterator var1 = this.listenerList.iterator();

         while(var1.hasNext()) {
            WCChangeListener var2 = (WCChangeListener)var1.next();
            var2.stateChanged(new WCChangeEvent(this));
         }

      }

      public URL getURL() {
         try {
            return this.pitem == 0L ? this.url : (this.url = URLs.newURL(BackForwardList.bflItemGetURL(this.pitem)));
         } catch (MalformedURLException var2) {
            return this.url = null;
         }
      }

      public String getTitle() {
         return this.pitem == 0L ? this.title : (this.title = BackForwardList.bflItemGetTitle(this.pitem));
      }

      public WCImage getIcon() {
         return this.pitem == 0L ? this.icon : (this.icon = BackForwardList.bflItemGetIcon(this.pitem));
      }

      public String getTarget() {
         return this.pitem == 0L ? this.target : (this.target = BackForwardList.bflItemGetTarget(this.pitem));
      }

      public Date getLastVisitedDate() {
         return this.lastVisitedDate == null ? null : (Date)this.lastVisitedDate.clone();
      }

      private void updateLastVisitedDate() {
         this.lastVisitedDate = new Date(System.currentTimeMillis());
         this.notifyItemChanged();
      }

      public boolean isTargetItem() {
         return this.pitem == 0L ? this.isTargetItem : (this.isTargetItem = BackForwardList.bflItemIsTargetItem(this.pitem));
      }

      public Entry[] getChildren() {
         return this.pitem == 0L ? this.children : (this.children = BackForwardList.bflItemGetChildren(this.pitem, this.ppage));
      }

      public String toString() {
         return "url=" + this.getURL() + ",title=" + this.getTitle() + ",date=" + this.getLastVisitedDate();
      }

      public void addChangeListener(WCChangeListener var1) {
         if (var1 != null) {
            this.listenerList.add(var1);
         }
      }

      public void removeChangeListener(WCChangeListener var1) {
         if (var1 != null) {
            this.listenerList.remove(var1);
         }
      }
   }
}
