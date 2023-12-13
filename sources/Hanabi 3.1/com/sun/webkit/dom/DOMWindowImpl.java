package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

public class DOMWindowImpl extends JSObject implements AbstractView, EventTarget {
   private static SelfDisposer[] hashTable = new SelfDisposer[64];
   private static int hashCount;

   private static int hashPeer(long var0) {
      return (int)(~var0 ^ var0 >> 7) & hashTable.length - 1;
   }

   private static AbstractView getCachedImpl(long var0) {
      if (var0 == 0L) {
         return null;
      } else {
         int var2 = hashPeer(var0);
         SelfDisposer var3 = hashTable[var2];
         SelfDisposer var4 = null;

         SelfDisposer var6;
         for(SelfDisposer var5 = var3; var5 != null; var5 = var6) {
            var6 = var5.next;
            if (var5.peer == var0) {
               DOMWindowImpl var7 = (DOMWindowImpl)var5.get();
               if (var7 != null) {
                  dispose(var0);
                  return var7;
               }

               if (var4 != null) {
                  var4.next = var6;
               } else {
                  hashTable[var2] = var6;
               }
               break;
            }

            var4 = var5;
         }

         DOMWindowImpl var8 = (DOMWindowImpl)createInterface(var0);
         var6 = new SelfDisposer(var8, var0);
         Disposer.addRecord(var6);
         var6.next = var3;
         hashTable[var2] = var6;
         if (3 * hashCount >= 2 * hashTable.length) {
            rehash();
         }

         ++hashCount;
         return var8;
      }
   }

   static int test_getHashCount() {
      return hashCount;
   }

   private static void rehash() {
      SelfDisposer[] var0 = hashTable;
      int var1 = var0.length;
      SelfDisposer[] var2 = new SelfDisposer[2 * var1];
      hashTable = var2;
      int var3 = var1;

      while(true) {
         --var3;
         if (var3 < 0) {
            return;
         }

         SelfDisposer var5;
         for(SelfDisposer var4 = var0[var3]; var4 != null; var4 = var5) {
            var5 = var4.next;
            int var6 = hashPeer(var4.peer);
            var4.next = var2[var6];
            var2[var6] = var4;
         }
      }
   }

   DOMWindowImpl(long var1) {
      super(var1, 2);
   }

   static AbstractView createInterface(long var0) {
      return var0 == 0L ? null : new DOMWindowImpl(var0);
   }

   static AbstractView create(long var0) {
      return getCachedImpl(var0);
   }

   static long getPeer(AbstractView var0) {
      return var0 == null ? 0L : ((DOMWindowImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static AbstractView getImpl(long var0) {
      return create(var0);
   }

   public Element getFrameElement() {
      return ElementImpl.getImpl(getFrameElementImpl(this.getPeer()));
   }

   static native long getFrameElementImpl(long var0);

   public boolean getOffscreenBuffering() {
      return getOffscreenBufferingImpl(this.getPeer());
   }

   static native boolean getOffscreenBufferingImpl(long var0);

   public int getOuterHeight() {
      return getOuterHeightImpl(this.getPeer());
   }

   static native int getOuterHeightImpl(long var0);

   public int getOuterWidth() {
      return getOuterWidthImpl(this.getPeer());
   }

   static native int getOuterWidthImpl(long var0);

   public int getInnerHeight() {
      return getInnerHeightImpl(this.getPeer());
   }

   static native int getInnerHeightImpl(long var0);

   public int getInnerWidth() {
      return getInnerWidthImpl(this.getPeer());
   }

   static native int getInnerWidthImpl(long var0);

   public int getScreenX() {
      return getScreenXImpl(this.getPeer());
   }

   static native int getScreenXImpl(long var0);

   public int getScreenY() {
      return getScreenYImpl(this.getPeer());
   }

   static native int getScreenYImpl(long var0);

   public int getScreenLeft() {
      return getScreenLeftImpl(this.getPeer());
   }

   static native int getScreenLeftImpl(long var0);

   public int getScreenTop() {
      return getScreenTopImpl(this.getPeer());
   }

   static native int getScreenTopImpl(long var0);

   public int getScrollX() {
      return getScrollXImpl(this.getPeer());
   }

   static native int getScrollXImpl(long var0);

   public int getScrollY() {
      return getScrollYImpl(this.getPeer());
   }

   static native int getScrollYImpl(long var0);

   public int getPageXOffset() {
      return getPageXOffsetImpl(this.getPeer());
   }

   static native int getPageXOffsetImpl(long var0);

   public int getPageYOffset() {
      return getPageYOffsetImpl(this.getPeer());
   }

   static native int getPageYOffsetImpl(long var0);

   public boolean getClosed() {
      return getClosedImpl(this.getPeer());
   }

   static native boolean getClosedImpl(long var0);

   public int getLength() {
      return getLengthImpl(this.getPeer());
   }

   static native int getLengthImpl(long var0);

   public String getName() {
      return getNameImpl(this.getPeer());
   }

   static native String getNameImpl(long var0);

   public void setName(String var1) {
      setNameImpl(this.getPeer(), var1);
   }

   static native void setNameImpl(long var0, String var2);

   public String getStatus() {
      return getStatusImpl(this.getPeer());
   }

   static native String getStatusImpl(long var0);

   public void setStatus(String var1) {
      setStatusImpl(this.getPeer(), var1);
   }

   static native void setStatusImpl(long var0, String var2);

   public String getDefaultStatus() {
      return getDefaultStatusImpl(this.getPeer());
   }

   static native String getDefaultStatusImpl(long var0);

   public void setDefaultStatus(String var1) {
      setDefaultStatusImpl(this.getPeer(), var1);
   }

   static native void setDefaultStatusImpl(long var0, String var2);

   public AbstractView getSelf() {
      return getImpl(getSelfImpl(this.getPeer()));
   }

   static native long getSelfImpl(long var0);

   public AbstractView getWindow() {
      return getImpl(getWindowImpl(this.getPeer()));
   }

   static native long getWindowImpl(long var0);

   public AbstractView getFrames() {
      return getImpl(getFramesImpl(this.getPeer()));
   }

   static native long getFramesImpl(long var0);

   public AbstractView getOpener() {
      return getImpl(getOpenerImpl(this.getPeer()));
   }

   static native long getOpenerImpl(long var0);

   public AbstractView getParent() {
      return getImpl(getParentImpl(this.getPeer()));
   }

   static native long getParentImpl(long var0);

   public AbstractView getTop() {
      return getImpl(getTopImpl(this.getPeer()));
   }

   static native long getTopImpl(long var0);

   public Document getDocumentEx() {
      return DocumentImpl.getImpl(getDocumentExImpl(this.getPeer()));
   }

   static native long getDocumentExImpl(long var0);

   public double getDevicePixelRatio() {
      return getDevicePixelRatioImpl(this.getPeer());
   }

   static native double getDevicePixelRatioImpl(long var0);

   public EventListener getOnanimationend() {
      return EventListenerImpl.getImpl(getOnanimationendImpl(this.getPeer()));
   }

   static native long getOnanimationendImpl(long var0);

   public void setOnanimationend(EventListener var1) {
      setOnanimationendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnanimationendImpl(long var0, long var2);

   public EventListener getOnanimationiteration() {
      return EventListenerImpl.getImpl(getOnanimationiterationImpl(this.getPeer()));
   }

   static native long getOnanimationiterationImpl(long var0);

   public void setOnanimationiteration(EventListener var1) {
      setOnanimationiterationImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnanimationiterationImpl(long var0, long var2);

   public EventListener getOnanimationstart() {
      return EventListenerImpl.getImpl(getOnanimationstartImpl(this.getPeer()));
   }

   static native long getOnanimationstartImpl(long var0);

   public void setOnanimationstart(EventListener var1) {
      setOnanimationstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnanimationstartImpl(long var0, long var2);

   public EventListener getOntransitionend() {
      return EventListenerImpl.getImpl(getOntransitionendImpl(this.getPeer()));
   }

   static native long getOntransitionendImpl(long var0);

   public void setOntransitionend(EventListener var1) {
      setOntransitionendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOntransitionendImpl(long var0, long var2);

   public EventListener getOnwebkitanimationend() {
      return EventListenerImpl.getImpl(getOnwebkitanimationendImpl(this.getPeer()));
   }

   static native long getOnwebkitanimationendImpl(long var0);

   public void setOnwebkitanimationend(EventListener var1) {
      setOnwebkitanimationendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwebkitanimationendImpl(long var0, long var2);

   public EventListener getOnwebkitanimationiteration() {
      return EventListenerImpl.getImpl(getOnwebkitanimationiterationImpl(this.getPeer()));
   }

   static native long getOnwebkitanimationiterationImpl(long var0);

   public void setOnwebkitanimationiteration(EventListener var1) {
      setOnwebkitanimationiterationImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwebkitanimationiterationImpl(long var0, long var2);

   public EventListener getOnwebkitanimationstart() {
      return EventListenerImpl.getImpl(getOnwebkitanimationstartImpl(this.getPeer()));
   }

   static native long getOnwebkitanimationstartImpl(long var0);

   public void setOnwebkitanimationstart(EventListener var1) {
      setOnwebkitanimationstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwebkitanimationstartImpl(long var0, long var2);

   public EventListener getOnwebkittransitionend() {
      return EventListenerImpl.getImpl(getOnwebkittransitionendImpl(this.getPeer()));
   }

   static native long getOnwebkittransitionendImpl(long var0);

   public void setOnwebkittransitionend(EventListener var1) {
      setOnwebkittransitionendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwebkittransitionendImpl(long var0, long var2);

   public EventListener getOnabort() {
      return EventListenerImpl.getImpl(getOnabortImpl(this.getPeer()));
   }

   static native long getOnabortImpl(long var0);

   public void setOnabort(EventListener var1) {
      setOnabortImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnabortImpl(long var0, long var2);

   public EventListener getOnblur() {
      return EventListenerImpl.getImpl(getOnblurImpl(this.getPeer()));
   }

   static native long getOnblurImpl(long var0);

   public void setOnblur(EventListener var1) {
      setOnblurImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnblurImpl(long var0, long var2);

   public EventListener getOncanplay() {
      return EventListenerImpl.getImpl(getOncanplayImpl(this.getPeer()));
   }

   static native long getOncanplayImpl(long var0);

   public void setOncanplay(EventListener var1) {
      setOncanplayImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOncanplayImpl(long var0, long var2);

   public EventListener getOncanplaythrough() {
      return EventListenerImpl.getImpl(getOncanplaythroughImpl(this.getPeer()));
   }

   static native long getOncanplaythroughImpl(long var0);

   public void setOncanplaythrough(EventListener var1) {
      setOncanplaythroughImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOncanplaythroughImpl(long var0, long var2);

   public EventListener getOnchange() {
      return EventListenerImpl.getImpl(getOnchangeImpl(this.getPeer()));
   }

   static native long getOnchangeImpl(long var0);

   public void setOnchange(EventListener var1) {
      setOnchangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnchangeImpl(long var0, long var2);

   public EventListener getOnclick() {
      return EventListenerImpl.getImpl(getOnclickImpl(this.getPeer()));
   }

   static native long getOnclickImpl(long var0);

   public void setOnclick(EventListener var1) {
      setOnclickImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnclickImpl(long var0, long var2);

   public EventListener getOncontextmenu() {
      return EventListenerImpl.getImpl(getOncontextmenuImpl(this.getPeer()));
   }

   static native long getOncontextmenuImpl(long var0);

   public void setOncontextmenu(EventListener var1) {
      setOncontextmenuImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOncontextmenuImpl(long var0, long var2);

   public EventListener getOndblclick() {
      return EventListenerImpl.getImpl(getOndblclickImpl(this.getPeer()));
   }

   static native long getOndblclickImpl(long var0);

   public void setOndblclick(EventListener var1) {
      setOndblclickImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndblclickImpl(long var0, long var2);

   public EventListener getOndrag() {
      return EventListenerImpl.getImpl(getOndragImpl(this.getPeer()));
   }

   static native long getOndragImpl(long var0);

   public void setOndrag(EventListener var1) {
      setOndragImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragImpl(long var0, long var2);

   public EventListener getOndragend() {
      return EventListenerImpl.getImpl(getOndragendImpl(this.getPeer()));
   }

   static native long getOndragendImpl(long var0);

   public void setOndragend(EventListener var1) {
      setOndragendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragendImpl(long var0, long var2);

   public EventListener getOndragenter() {
      return EventListenerImpl.getImpl(getOndragenterImpl(this.getPeer()));
   }

   static native long getOndragenterImpl(long var0);

   public void setOndragenter(EventListener var1) {
      setOndragenterImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragenterImpl(long var0, long var2);

   public EventListener getOndragleave() {
      return EventListenerImpl.getImpl(getOndragleaveImpl(this.getPeer()));
   }

   static native long getOndragleaveImpl(long var0);

   public void setOndragleave(EventListener var1) {
      setOndragleaveImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragleaveImpl(long var0, long var2);

   public EventListener getOndragover() {
      return EventListenerImpl.getImpl(getOndragoverImpl(this.getPeer()));
   }

   static native long getOndragoverImpl(long var0);

   public void setOndragover(EventListener var1) {
      setOndragoverImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragoverImpl(long var0, long var2);

   public EventListener getOndragstart() {
      return EventListenerImpl.getImpl(getOndragstartImpl(this.getPeer()));
   }

   static native long getOndragstartImpl(long var0);

   public void setOndragstart(EventListener var1) {
      setOndragstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragstartImpl(long var0, long var2);

   public EventListener getOndrop() {
      return EventListenerImpl.getImpl(getOndropImpl(this.getPeer()));
   }

   static native long getOndropImpl(long var0);

   public void setOndrop(EventListener var1) {
      setOndropImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndropImpl(long var0, long var2);

   public EventListener getOndurationchange() {
      return EventListenerImpl.getImpl(getOndurationchangeImpl(this.getPeer()));
   }

   static native long getOndurationchangeImpl(long var0);

   public void setOndurationchange(EventListener var1) {
      setOndurationchangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndurationchangeImpl(long var0, long var2);

   public EventListener getOnemptied() {
      return EventListenerImpl.getImpl(getOnemptiedImpl(this.getPeer()));
   }

   static native long getOnemptiedImpl(long var0);

   public void setOnemptied(EventListener var1) {
      setOnemptiedImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnemptiedImpl(long var0, long var2);

   public EventListener getOnended() {
      return EventListenerImpl.getImpl(getOnendedImpl(this.getPeer()));
   }

   static native long getOnendedImpl(long var0);

   public void setOnended(EventListener var1) {
      setOnendedImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnendedImpl(long var0, long var2);

   public EventListener getOnerror() {
      return EventListenerImpl.getImpl(getOnerrorImpl(this.getPeer()));
   }

   static native long getOnerrorImpl(long var0);

   public void setOnerror(EventListener var1) {
      setOnerrorImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnerrorImpl(long var0, long var2);

   public EventListener getOnfocus() {
      return EventListenerImpl.getImpl(getOnfocusImpl(this.getPeer()));
   }

   static native long getOnfocusImpl(long var0);

   public void setOnfocus(EventListener var1) {
      setOnfocusImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnfocusImpl(long var0, long var2);

   public EventListener getOninput() {
      return EventListenerImpl.getImpl(getOninputImpl(this.getPeer()));
   }

   static native long getOninputImpl(long var0);

   public void setOninput(EventListener var1) {
      setOninputImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOninputImpl(long var0, long var2);

   public EventListener getOninvalid() {
      return EventListenerImpl.getImpl(getOninvalidImpl(this.getPeer()));
   }

   static native long getOninvalidImpl(long var0);

   public void setOninvalid(EventListener var1) {
      setOninvalidImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOninvalidImpl(long var0, long var2);

   public EventListener getOnkeydown() {
      return EventListenerImpl.getImpl(getOnkeydownImpl(this.getPeer()));
   }

   static native long getOnkeydownImpl(long var0);

   public void setOnkeydown(EventListener var1) {
      setOnkeydownImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnkeydownImpl(long var0, long var2);

   public EventListener getOnkeypress() {
      return EventListenerImpl.getImpl(getOnkeypressImpl(this.getPeer()));
   }

   static native long getOnkeypressImpl(long var0);

   public void setOnkeypress(EventListener var1) {
      setOnkeypressImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnkeypressImpl(long var0, long var2);

   public EventListener getOnkeyup() {
      return EventListenerImpl.getImpl(getOnkeyupImpl(this.getPeer()));
   }

   static native long getOnkeyupImpl(long var0);

   public void setOnkeyup(EventListener var1) {
      setOnkeyupImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnkeyupImpl(long var0, long var2);

   public EventListener getOnload() {
      return EventListenerImpl.getImpl(getOnloadImpl(this.getPeer()));
   }

   static native long getOnloadImpl(long var0);

   public void setOnload(EventListener var1) {
      setOnloadImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadImpl(long var0, long var2);

   public EventListener getOnloadeddata() {
      return EventListenerImpl.getImpl(getOnloadeddataImpl(this.getPeer()));
   }

   static native long getOnloadeddataImpl(long var0);

   public void setOnloadeddata(EventListener var1) {
      setOnloadeddataImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadeddataImpl(long var0, long var2);

   public EventListener getOnloadedmetadata() {
      return EventListenerImpl.getImpl(getOnloadedmetadataImpl(this.getPeer()));
   }

   static native long getOnloadedmetadataImpl(long var0);

   public void setOnloadedmetadata(EventListener var1) {
      setOnloadedmetadataImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadedmetadataImpl(long var0, long var2);

   public EventListener getOnloadstart() {
      return EventListenerImpl.getImpl(getOnloadstartImpl(this.getPeer()));
   }

   static native long getOnloadstartImpl(long var0);

   public void setOnloadstart(EventListener var1) {
      setOnloadstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadstartImpl(long var0, long var2);

   public EventListener getOnmousedown() {
      return EventListenerImpl.getImpl(getOnmousedownImpl(this.getPeer()));
   }

   static native long getOnmousedownImpl(long var0);

   public void setOnmousedown(EventListener var1) {
      setOnmousedownImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmousedownImpl(long var0, long var2);

   public EventListener getOnmouseenter() {
      return EventListenerImpl.getImpl(getOnmouseenterImpl(this.getPeer()));
   }

   static native long getOnmouseenterImpl(long var0);

   public void setOnmouseenter(EventListener var1) {
      setOnmouseenterImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseenterImpl(long var0, long var2);

   public EventListener getOnmouseleave() {
      return EventListenerImpl.getImpl(getOnmouseleaveImpl(this.getPeer()));
   }

   static native long getOnmouseleaveImpl(long var0);

   public void setOnmouseleave(EventListener var1) {
      setOnmouseleaveImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseleaveImpl(long var0, long var2);

   public EventListener getOnmousemove() {
      return EventListenerImpl.getImpl(getOnmousemoveImpl(this.getPeer()));
   }

   static native long getOnmousemoveImpl(long var0);

   public void setOnmousemove(EventListener var1) {
      setOnmousemoveImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmousemoveImpl(long var0, long var2);

   public EventListener getOnmouseout() {
      return EventListenerImpl.getImpl(getOnmouseoutImpl(this.getPeer()));
   }

   static native long getOnmouseoutImpl(long var0);

   public void setOnmouseout(EventListener var1) {
      setOnmouseoutImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseoutImpl(long var0, long var2);

   public EventListener getOnmouseover() {
      return EventListenerImpl.getImpl(getOnmouseoverImpl(this.getPeer()));
   }

   static native long getOnmouseoverImpl(long var0);

   public void setOnmouseover(EventListener var1) {
      setOnmouseoverImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseoverImpl(long var0, long var2);

   public EventListener getOnmouseup() {
      return EventListenerImpl.getImpl(getOnmouseupImpl(this.getPeer()));
   }

   static native long getOnmouseupImpl(long var0);

   public void setOnmouseup(EventListener var1) {
      setOnmouseupImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseupImpl(long var0, long var2);

   public EventListener getOnmousewheel() {
      return EventListenerImpl.getImpl(getOnmousewheelImpl(this.getPeer()));
   }

   static native long getOnmousewheelImpl(long var0);

   public void setOnmousewheel(EventListener var1) {
      setOnmousewheelImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmousewheelImpl(long var0, long var2);

   public EventListener getOnpause() {
      return EventListenerImpl.getImpl(getOnpauseImpl(this.getPeer()));
   }

   static native long getOnpauseImpl(long var0);

   public void setOnpause(EventListener var1) {
      setOnpauseImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpauseImpl(long var0, long var2);

   public EventListener getOnplay() {
      return EventListenerImpl.getImpl(getOnplayImpl(this.getPeer()));
   }

   static native long getOnplayImpl(long var0);

   public void setOnplay(EventListener var1) {
      setOnplayImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnplayImpl(long var0, long var2);

   public EventListener getOnplaying() {
      return EventListenerImpl.getImpl(getOnplayingImpl(this.getPeer()));
   }

   static native long getOnplayingImpl(long var0);

   public void setOnplaying(EventListener var1) {
      setOnplayingImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnplayingImpl(long var0, long var2);

   public EventListener getOnprogress() {
      return EventListenerImpl.getImpl(getOnprogressImpl(this.getPeer()));
   }

   static native long getOnprogressImpl(long var0);

   public void setOnprogress(EventListener var1) {
      setOnprogressImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnprogressImpl(long var0, long var2);

   public EventListener getOnratechange() {
      return EventListenerImpl.getImpl(getOnratechangeImpl(this.getPeer()));
   }

   static native long getOnratechangeImpl(long var0);

   public void setOnratechange(EventListener var1) {
      setOnratechangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnratechangeImpl(long var0, long var2);

   public EventListener getOnreset() {
      return EventListenerImpl.getImpl(getOnresetImpl(this.getPeer()));
   }

   static native long getOnresetImpl(long var0);

   public void setOnreset(EventListener var1) {
      setOnresetImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnresetImpl(long var0, long var2);

   public EventListener getOnresize() {
      return EventListenerImpl.getImpl(getOnresizeImpl(this.getPeer()));
   }

   static native long getOnresizeImpl(long var0);

   public void setOnresize(EventListener var1) {
      setOnresizeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnresizeImpl(long var0, long var2);

   public EventListener getOnscroll() {
      return EventListenerImpl.getImpl(getOnscrollImpl(this.getPeer()));
   }

   static native long getOnscrollImpl(long var0);

   public void setOnscroll(EventListener var1) {
      setOnscrollImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnscrollImpl(long var0, long var2);

   public EventListener getOnseeked() {
      return EventListenerImpl.getImpl(getOnseekedImpl(this.getPeer()));
   }

   static native long getOnseekedImpl(long var0);

   public void setOnseeked(EventListener var1) {
      setOnseekedImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnseekedImpl(long var0, long var2);

   public EventListener getOnseeking() {
      return EventListenerImpl.getImpl(getOnseekingImpl(this.getPeer()));
   }

   static native long getOnseekingImpl(long var0);

   public void setOnseeking(EventListener var1) {
      setOnseekingImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnseekingImpl(long var0, long var2);

   public EventListener getOnselect() {
      return EventListenerImpl.getImpl(getOnselectImpl(this.getPeer()));
   }

   static native long getOnselectImpl(long var0);

   public void setOnselect(EventListener var1) {
      setOnselectImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnselectImpl(long var0, long var2);

   public EventListener getOnstalled() {
      return EventListenerImpl.getImpl(getOnstalledImpl(this.getPeer()));
   }

   static native long getOnstalledImpl(long var0);

   public void setOnstalled(EventListener var1) {
      setOnstalledImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnstalledImpl(long var0, long var2);

   public EventListener getOnsubmit() {
      return EventListenerImpl.getImpl(getOnsubmitImpl(this.getPeer()));
   }

   static native long getOnsubmitImpl(long var0);

   public void setOnsubmit(EventListener var1) {
      setOnsubmitImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnsubmitImpl(long var0, long var2);

   public EventListener getOnsuspend() {
      return EventListenerImpl.getImpl(getOnsuspendImpl(this.getPeer()));
   }

   static native long getOnsuspendImpl(long var0);

   public void setOnsuspend(EventListener var1) {
      setOnsuspendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnsuspendImpl(long var0, long var2);

   public EventListener getOntimeupdate() {
      return EventListenerImpl.getImpl(getOntimeupdateImpl(this.getPeer()));
   }

   static native long getOntimeupdateImpl(long var0);

   public void setOntimeupdate(EventListener var1) {
      setOntimeupdateImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOntimeupdateImpl(long var0, long var2);

   public EventListener getOnvolumechange() {
      return EventListenerImpl.getImpl(getOnvolumechangeImpl(this.getPeer()));
   }

   static native long getOnvolumechangeImpl(long var0);

   public void setOnvolumechange(EventListener var1) {
      setOnvolumechangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnvolumechangeImpl(long var0, long var2);

   public EventListener getOnwaiting() {
      return EventListenerImpl.getImpl(getOnwaitingImpl(this.getPeer()));
   }

   static native long getOnwaitingImpl(long var0);

   public void setOnwaiting(EventListener var1) {
      setOnwaitingImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwaitingImpl(long var0, long var2);

   public EventListener getOnsearch() {
      return EventListenerImpl.getImpl(getOnsearchImpl(this.getPeer()));
   }

   static native long getOnsearchImpl(long var0);

   public void setOnsearch(EventListener var1) {
      setOnsearchImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnsearchImpl(long var0, long var2);

   public EventListener getOnwheel() {
      return EventListenerImpl.getImpl(getOnwheelImpl(this.getPeer()));
   }

   static native long getOnwheelImpl(long var0);

   public void setOnwheel(EventListener var1) {
      setOnwheelImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwheelImpl(long var0, long var2);

   public EventListener getOnbeforeunload() {
      return EventListenerImpl.getImpl(getOnbeforeunloadImpl(this.getPeer()));
   }

   static native long getOnbeforeunloadImpl(long var0);

   public void setOnbeforeunload(EventListener var1) {
      setOnbeforeunloadImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnbeforeunloadImpl(long var0, long var2);

   public EventListener getOnhashchange() {
      return EventListenerImpl.getImpl(getOnhashchangeImpl(this.getPeer()));
   }

   static native long getOnhashchangeImpl(long var0);

   public void setOnhashchange(EventListener var1) {
      setOnhashchangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnhashchangeImpl(long var0, long var2);

   public EventListener getOnmessage() {
      return EventListenerImpl.getImpl(getOnmessageImpl(this.getPeer()));
   }

   static native long getOnmessageImpl(long var0);

   public void setOnmessage(EventListener var1) {
      setOnmessageImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmessageImpl(long var0, long var2);

   public EventListener getOnoffline() {
      return EventListenerImpl.getImpl(getOnofflineImpl(this.getPeer()));
   }

   static native long getOnofflineImpl(long var0);

   public void setOnoffline(EventListener var1) {
      setOnofflineImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnofflineImpl(long var0, long var2);

   public EventListener getOnonline() {
      return EventListenerImpl.getImpl(getOnonlineImpl(this.getPeer()));
   }

   static native long getOnonlineImpl(long var0);

   public void setOnonline(EventListener var1) {
      setOnonlineImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnonlineImpl(long var0, long var2);

   public EventListener getOnpagehide() {
      return EventListenerImpl.getImpl(getOnpagehideImpl(this.getPeer()));
   }

   static native long getOnpagehideImpl(long var0);

   public void setOnpagehide(EventListener var1) {
      setOnpagehideImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpagehideImpl(long var0, long var2);

   public EventListener getOnpageshow() {
      return EventListenerImpl.getImpl(getOnpageshowImpl(this.getPeer()));
   }

   static native long getOnpageshowImpl(long var0);

   public void setOnpageshow(EventListener var1) {
      setOnpageshowImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpageshowImpl(long var0, long var2);

   public EventListener getOnpopstate() {
      return EventListenerImpl.getImpl(getOnpopstateImpl(this.getPeer()));
   }

   static native long getOnpopstateImpl(long var0);

   public void setOnpopstate(EventListener var1) {
      setOnpopstateImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpopstateImpl(long var0, long var2);

   public EventListener getOnstorage() {
      return EventListenerImpl.getImpl(getOnstorageImpl(this.getPeer()));
   }

   static native long getOnstorageImpl(long var0);

   public void setOnstorage(EventListener var1) {
      setOnstorageImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnstorageImpl(long var0, long var2);

   public EventListener getOnunload() {
      return EventListenerImpl.getImpl(getOnunloadImpl(this.getPeer()));
   }

   static native long getOnunloadImpl(long var0);

   public void setOnunload(EventListener var1) {
      setOnunloadImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnunloadImpl(long var0, long var2);

   public DOMSelectionImpl getSelection() {
      return DOMSelectionImpl.getImpl(getSelectionImpl(this.getPeer()));
   }

   static native long getSelectionImpl(long var0);

   public void focus() {
      focusImpl(this.getPeer());
   }

   static native void focusImpl(long var0);

   public void blur() {
      blurImpl(this.getPeer());
   }

   static native void blurImpl(long var0);

   public void close() {
      closeImpl(this.getPeer());
   }

   static native void closeImpl(long var0);

   public void print() {
      printImpl(this.getPeer());
   }

   static native void printImpl(long var0);

   public void stop() {
      stopImpl(this.getPeer());
   }

   static native void stopImpl(long var0);

   public void alert(String var1) {
      alertImpl(this.getPeer(), var1);
   }

   static native void alertImpl(long var0, String var2);

   public boolean confirm(String var1) {
      return confirmImpl(this.getPeer(), var1);
   }

   static native boolean confirmImpl(long var0, String var2);

   public String prompt(String var1, String var2) {
      return promptImpl(this.getPeer(), var1, var2);
   }

   static native String promptImpl(long var0, String var2, String var3);

   public boolean find(String var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      return findImpl(this.getPeer(), var1, var2, var3, var4, var5, var6, var7);
   }

   static native boolean findImpl(long var0, String var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8);

   public void scrollBy(int var1, int var2) {
      scrollByImpl(this.getPeer(), var1, var2);
   }

   static native void scrollByImpl(long var0, int var2, int var3);

   public void scrollTo(int var1, int var2) {
      scrollToImpl(this.getPeer(), var1, var2);
   }

   static native void scrollToImpl(long var0, int var2, int var3);

   public void scroll(int var1, int var2) {
      scrollImpl(this.getPeer(), var1, var2);
   }

   static native void scrollImpl(long var0, int var2, int var3);

   public void moveBy(float var1, float var2) {
      moveByImpl(this.getPeer(), var1, var2);
   }

   static native void moveByImpl(long var0, float var2, float var3);

   public void moveTo(float var1, float var2) {
      moveToImpl(this.getPeer(), var1, var2);
   }

   static native void moveToImpl(long var0, float var2, float var3);

   public void resizeBy(float var1, float var2) {
      resizeByImpl(this.getPeer(), var1, var2);
   }

   static native void resizeByImpl(long var0, float var2, float var3);

   public void resizeTo(float var1, float var2) {
      resizeToImpl(this.getPeer(), var1, var2);
   }

   static native void resizeToImpl(long var0, float var2, float var3);

   public CSSStyleDeclaration getComputedStyle(Element var1, String var2) {
      return CSSStyleDeclarationImpl.getImpl(getComputedStyleImpl(this.getPeer(), ElementImpl.getPeer(var1), var2));
   }

   static native long getComputedStyleImpl(long var0, long var2, String var4);

   public void captureEvents() {
      captureEventsImpl(this.getPeer());
   }

   static native void captureEventsImpl(long var0);

   public void releaseEvents() {
      releaseEventsImpl(this.getPeer());
   }

   static native void releaseEventsImpl(long var0);

   public void addEventListener(String var1, EventListener var2, boolean var3) {
      addEventListenerImpl(this.getPeer(), var1, EventListenerImpl.getPeer(var2), var3);
   }

   static native void addEventListenerImpl(long var0, String var2, long var3, boolean var5);

   public void removeEventListener(String var1, EventListener var2, boolean var3) {
      removeEventListenerImpl(this.getPeer(), var1, EventListenerImpl.getPeer(var2), var3);
   }

   static native void removeEventListenerImpl(long var0, String var2, long var3, boolean var5);

   public boolean dispatchEvent(Event var1) throws DOMException {
      return dispatchEventImpl(this.getPeer(), EventImpl.getPeer(var1));
   }

   static native boolean dispatchEventImpl(long var0, long var2);

   public String atob(String var1) throws DOMException {
      return atobImpl(this.getPeer(), var1);
   }

   static native String atobImpl(long var0, String var2);

   public String btoa(String var1) throws DOMException {
      return btoaImpl(this.getPeer(), var1);
   }

   static native String btoaImpl(long var0, String var2);

   public void clearTimeout(int var1) {
      clearTimeoutImpl(this.getPeer(), var1);
   }

   static native void clearTimeoutImpl(long var0, int var2);

   public void clearInterval(int var1) {
      clearIntervalImpl(this.getPeer(), var1);
   }

   static native void clearIntervalImpl(long var0, int var2);

   public DocumentView getDocument() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   private static final class SelfDisposer extends Disposer.WeakDisposerRecord {
      private final long peer;
      SelfDisposer next;

      SelfDisposer(Object var1, long var2) {
         super(var1);
         this.peer = var2;
      }

      public void dispose() {
         int var1 = DOMWindowImpl.hashPeer(this.peer);
         SelfDisposer var2 = DOMWindowImpl.hashTable[var1];
         SelfDisposer var3 = null;

         SelfDisposer var5;
         for(SelfDisposer var4 = var2; var4 != null; var4 = var5) {
            var5 = var4.next;
            if (var4.peer == this.peer) {
               var4.clear();
               if (var3 != null) {
                  var3.next = var5;
               } else {
                  DOMWindowImpl.hashTable[var1] = var5;
               }

               DOMWindowImpl.hashCount--;
               break;
            }

            var3 = var4;
         }

         DOMWindowImpl.dispose(this.peer);
      }
   }
}
