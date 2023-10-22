package com.sun.webkit;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.webkit.event.WCFocusEvent;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.event.WCKeyEvent;
import com.sun.webkit.event.WCMouseEvent;
import com.sun.webkit.event.WCMouseWheelEvent;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;
import com.sun.webkit.graphics.WCSize;
import com.sun.webkit.network.CookieManager;
import com.sun.webkit.network.URLs;
import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import netscape.javascript.JSException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class WebPage {
   private static final Logger log = Logger.getLogger(WebPage.class.getName());
   private static final Logger paintLog = Logger.getLogger(WebPage.class.getName() + ".paint");
   private static final int MAX_FRAME_QUEUE_SIZE = 10;
   private long pPage = 0L;
   private boolean isDisposed = false;
   private int width;
   private int height;
   private int fontSmoothingType;
   private final WCFrameView hostWindow;
   private final Set frames = new HashSet();
   private final AccessControlContext accessControlContext;
   private final Map requestURLs = new HashMap();
   private final Set requestStarted = new HashSet();
   private static final ReentrantLock PAGE_LOCK = new ReentrantLock();
   private final Queue frameQueue = new LinkedList();
   private RenderFrame currentFrame = new RenderFrame();
   private int updateContentCycleID;
   private static boolean firstWebPageCreated;
   private WCPageBackBuffer backbuffer;
   private List dirtyRects = new LinkedList();
   private final WebPageClient pageClient;
   private final UIClient uiClient;
   private final PolicyClient policyClient;
   private InputMethodClient imClient;
   private final List loadListenerClients = new LinkedList();
   private final InspectorClient inspectorClient;
   private final RenderTheme renderTheme;
   private final ScrollBarTheme scrollbarTheme;
   public static final int DND_DST_ENTER = 0;
   public static final int DND_DST_OVER = 1;
   public static final int DND_DST_CHANGE = 2;
   public static final int DND_DST_EXIT = 3;
   public static final int DND_DST_DROP = 4;
   public static final int DND_SRC_ENTER = 100;
   public static final int DND_SRC_OVER = 101;
   public static final int DND_SRC_CHANGE = 102;
   public static final int DND_SRC_EXIT = 103;
   public static final int DND_SRC_DROP = 104;

   private static void collectJSCGarbages() {
      Invoker.getInvoker().checkEventThread();
      Disposer.addRecord(new Object(), WebPage::collectJSCGarbages);
      twkDoJSCGarbageCollection();
   }

   public WebPage(WebPageClient var1, UIClient var2, PolicyClient var3, InspectorClient var4, ThemeClient var5, boolean var6) {
      Invoker.getInvoker().checkEventThread();
      this.pageClient = var1;
      this.uiClient = var2;
      this.policyClient = var3;
      this.inspectorClient = var4;
      if (var5 != null) {
         this.renderTheme = var5.createRenderTheme();
         this.scrollbarTheme = var5.createScrollBarTheme();
      } else {
         this.renderTheme = null;
         this.scrollbarTheme = null;
      }

      this.accessControlContext = AccessController.getContext();
      this.hostWindow = new WCFrameView(this);
      this.pPage = this.twkCreatePage(var6);
      this.twkInit(this.pPage, false, WCGraphicsManager.getGraphicsManager().getDevicePixelScale());
      if (var1 != null && var1.isBackBufferSupported()) {
         this.backbuffer = var1.createBackBuffer();
         this.backbuffer.ref();
      }

      if (!firstWebPageCreated) {
         Disposer.addRecord(new Object(), WebPage::collectJSCGarbages);
         firstWebPageCreated = true;
      }

   }

   long getPage() {
      return this.pPage;
   }

   private WCWidget getHostWindow() {
      return this.hostWindow;
   }

   public AccessControlContext getAccessControlContext() {
      return this.accessControlContext;
   }

   static boolean lockPage() {
      return Invoker.getInvoker().lock(PAGE_LOCK);
   }

   static boolean unlockPage() {
      return Invoker.getInvoker().unlock(PAGE_LOCK);
   }

   private void addDirtyRect(WCRectangle var1) {
      if (!(var1.getWidth() <= 0.0F) && !(var1.getHeight() <= 0.0F)) {
         Iterator var2 = this.dirtyRects.iterator();

         while(var2.hasNext()) {
            WCRectangle var3 = (WCRectangle)var2.next();
            if (var3.contains(var1)) {
               return;
            }

            if (var1.contains(var3)) {
               var2.remove();
            } else {
               WCRectangle var4 = var3.createUnion(var1);
               if (var4.getIntWidth() * var4.getIntHeight() < var3.getIntWidth() * var3.getIntHeight() + var1.getIntWidth() * var1.getIntHeight()) {
                  var2.remove();
                  var1 = var4;
               }
            }
         }

         this.dirtyRects.add(var1);
      }
   }

   public boolean isDirty() {
      lockPage();

      boolean var1;
      try {
         var1 = !this.dirtyRects.isEmpty();
      } finally {
         unlockPage();
      }

      return var1;
   }

   private void updateDirty(WCRectangle var1) {
      if (paintLog.isLoggable(Level.FINEST)) {
         paintLog.log(Level.FINEST, "Entering, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
      }

      if (!this.isDisposed && this.width > 0 && this.height > 0) {
         if (var1 == null) {
            var1 = new WCRectangle(0.0F, 0.0F, (float)this.width, (float)this.height);
         }

         List var2 = this.dirtyRects;
         this.dirtyRects = new LinkedList();
         this.twkPrePaint(this.getPage());

         while(!var2.isEmpty()) {
            WCRectangle var3 = ((WCRectangle)var2.remove(0)).intersection(var1);
            if (!(var3.getWidth() <= 0.0F) && !(var3.getHeight() <= 0.0F)) {
               paintLog.log(Level.FINEST, "Updating: {0}", var3);
               WCRenderQueue var4 = WCGraphicsManager.getGraphicsManager().createRenderQueue(var3, true);
               this.twkUpdateContent(this.getPage(), var4, var3.getIntX() - 1, var3.getIntY() - 1, var3.getIntWidth() + 2, var3.getIntHeight() + 2);
               this.currentFrame.addRenderQueue(var4);
            }
         }

         WCRenderQueue var11 = WCGraphicsManager.getGraphicsManager().createRenderQueue(var1, false);
         this.twkPostPaint(this.getPage(), var11, var1.getIntX(), var1.getIntY(), var1.getIntWidth(), var1.getIntHeight());
         this.currentFrame.addRenderQueue(var11);
         if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "Dirty rects processed, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
         }

         if (this.currentFrame.getRQList().size() > 0) {
            synchronized(this.frameQueue) {
               paintLog.log(Level.FINEST, "About to update frame queue, frameQueue: {0}", this.frameQueue);
               Iterator var12 = this.frameQueue.iterator();

               while(true) {
                  while(var12.hasNext()) {
                     RenderFrame var5 = (RenderFrame)var12.next();
                     Iterator var6 = this.currentFrame.getRQList().iterator();

                     while(var6.hasNext()) {
                        WCRenderQueue var7 = (WCRenderQueue)var6.next();
                        WCRectangle var8 = var7.getClip();
                        if (var7.isOpaque() && var8.contains(var5.getEnclosingRect())) {
                           paintLog.log(Level.FINEST, "Dropping: {0}", var5);
                           var5.drop();
                           var12.remove();
                           break;
                        }
                     }
                  }

                  this.frameQueue.add(this.currentFrame);
                  this.currentFrame = new RenderFrame();
                  if (this.frameQueue.size() > 10) {
                     paintLog.log(Level.FINEST, "Frame queue exceeded maximum size, clearing and requesting full repaint");
                     this.dropRenderFrames();
                     this.repaintAll();
                  }

                  paintLog.log(Level.FINEST, "Frame queue updated, frameQueue: {0}", this.frameQueue);
                  break;
               }
            }
         }

         if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "Exiting, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
         }

      } else {
         this.dirtyRects.clear();
      }
   }

   private void scroll(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (paintLog.isLoggable(Level.FINEST)) {
         paintLog.finest("rect=[" + var1 + ", " + var2 + " " + var3 + "x" + var4 + "] delta=[" + var5 + ", " + var6 + "]");
      }

      var5 += this.currentFrame.scrollDx;
      var6 += this.currentFrame.scrollDy;
      if (Math.abs(var5) < var3 && Math.abs(var6) < var4) {
         int var7 = var5 >= 0 ? var1 : var1 - var5;
         int var8 = var6 >= 0 ? var2 : var2 - var6;
         int var9 = var5 == 0 ? var3 : var3 - Math.abs(var5);
         int var10 = var6 == 0 ? var4 : var4 - Math.abs(var6);
         WCRenderQueue var11 = WCGraphicsManager.getGraphicsManager().createRenderQueue(new WCRectangle(0.0F, 0.0F, (float)this.width, (float)this.height), false);
         ByteBuffer var12 = ByteBuffer.allocate(32).order(ByteOrder.nativeOrder()).putInt(40).putInt(this.backbuffer.getID()).putInt(var7).putInt(var8).putInt(var9).putInt(var10).putInt(var5).putInt(var6);
         var12.flip();
         var11.addBuffer(var12);
         this.currentFrame.drop();
         this.currentFrame.addRenderQueue(var11);
         this.currentFrame.scrollDx = var5;
         this.currentFrame.scrollDy = var6;
         if (!this.dirtyRects.isEmpty()) {
            WCRectangle var13 = new WCRectangle((float)var1, (float)var2, (float)var3, (float)var4);
            Iterator var14 = this.dirtyRects.iterator();

            while(var14.hasNext()) {
               WCRectangle var15 = (WCRectangle)var14.next();
               if (var13.contains(var15)) {
                  if (paintLog.isLoggable(Level.FINEST)) {
                     paintLog.log(Level.FINEST, "translating old dirty rect by the delta: " + var15);
                  }

                  var15.translate((float)var5, (float)var6);
               }
            }
         }
      }

      this.addDirtyRect(new WCRectangle((float)var1, var6 >= 0 ? (float)var2 : (float)(var2 + var4 + var6), (float)var3, (float)Math.abs(var6)));
      this.addDirtyRect(new WCRectangle(var5 >= 0 ? (float)var1 : (float)(var1 + var3 + var5), (float)var2, (float)Math.abs(var5), (float)(var4 - Math.abs(var6))));
   }

   public WebPageClient getPageClient() {
      return this.pageClient;
   }

   public void setInputMethodClient(InputMethodClient var1) {
      this.imClient = var1;
   }

   public void setInputMethodState(boolean var1) {
      if (this.imClient != null) {
         this.imClient.activateInputMethods(var1);
      }

   }

   public void addLoadListenerClient(LoadListenerClient var1) {
      if (!this.loadListenerClients.contains(var1)) {
         this.loadListenerClients.add(var1);
      }

   }

   private RenderTheme getRenderTheme() {
      return this.renderTheme;
   }

   private static RenderTheme fwkGetDefaultRenderTheme() {
      return ThemeClient.getDefaultRenderTheme();
   }

   private ScrollBarTheme getScrollBarTheme() {
      return this.scrollbarTheme;
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      lockPage();

      try {
         log.log(Level.FINE, "setBounds: " + var1 + " " + var2 + " " + var3 + " " + var4);
         if (!this.isDisposed) {
            this.width = var3;
            this.height = var4;
            this.twkSetBounds(this.getPage(), 0, 0, var3, var4);
            this.repaintAll();
            return;
         }

         log.log(Level.FINE, "setBounds() request for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public void setOpaque(long var1, boolean var3) {
      lockPage();

      try {
         log.log(Level.FINE, "setOpaque: " + var3);
         if (this.isDisposed) {
            log.log(Level.FINE, "setOpaque() request for a disposed web page.");
            return;
         }

         if (!this.frames.contains(var1)) {
            return;
         }

         this.twkSetTransparent(var1, !var3);
      } finally {
         unlockPage();
      }

   }

   public void setBackgroundColor(long var1, int var3) {
      lockPage();

      try {
         log.log(Level.FINE, "setBackgroundColor: " + var3);
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               return;
            }

            this.twkSetBackgroundColor(var1, var3);
            return;
         }

         log.log(Level.FINE, "setBackgroundColor() request for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public void setBackgroundColor(int var1) {
      lockPage();

      try {
         log.log(Level.FINE, "setBackgroundColor: " + var1 + " for all frames");
         if (this.isDisposed) {
            log.log(Level.FINE, "setBackgroundColor() request for a disposed web page.");
            return;
         }

         Iterator var2 = this.frames.iterator();

         while(var2.hasNext()) {
            long var3 = (Long)var2.next();
            this.twkSetBackgroundColor(var3, var1);
         }
      } finally {
         unlockPage();
      }

   }

   public void updateContent(WCRectangle var1) {
      lockPage();

      try {
         ++this.updateContentCycleID;
         paintLog.log(Level.FINEST, "toPaint: {0}", var1);
         if (!this.isDisposed) {
            this.updateDirty(var1);
            this.updateRendering();
            return;
         }

         paintLog.fine("updateContent() request for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public void updateRendering() {
      this.twkUpdateRendering(this.getPage());
   }

   public int getUpdateContentCycleID() {
      return this.updateContentCycleID;
   }

   public boolean isRepaintPending() {
      lockPage();

      boolean var2;
      try {
         synchronized(this.frameQueue) {
            var2 = !this.frameQueue.isEmpty();
         }
      } finally {
         unlockPage();
      }

      return var2;
   }

   public void print(WCGraphicsContext var1, int var2, int var3, int var4, int var5) {
      lockPage();

      try {
         WCRenderQueue var6 = WCGraphicsManager.getGraphicsManager().createRenderQueue(new WCRectangle((float)var2, (float)var3, (float)var4, (float)var5), true);
         FutureTask var7 = new FutureTask(() -> {
            this.twkUpdateContent(this.getPage(), var6, var2, var3, var4, var5);
         }, (Object)null);
         Invoker.getInvoker().invokeOnEventThread(var7);

         try {
            var7.get();
         } catch (ExecutionException var13) {
            throw new AssertionError(var13);
         } catch (InterruptedException var14) {
         }

         var6.decode(var1);
      } finally {
         unlockPage();
      }

   }

   public void paint(WCGraphicsContext var1, int var2, int var3, int var4, int var5) {
      lockPage();

      try {
         if (this.pageClient != null && this.pageClient.isBackBufferSupported()) {
            if (!this.backbuffer.validate(this.width, this.height)) {
               Invoker.getInvoker().invokeOnEventThread(() -> {
                  this.repaintAll();
               });
            } else {
               WCGraphicsContext var6 = this.backbuffer.createGraphics();

               try {
                  this.paint2GC(var6);
                  var6.flush();
               } finally {
                  this.backbuffer.disposeGraphics(var6);
               }

               this.backbuffer.flush(var1, var2, var3, var4, var5);
            }
         } else {
            this.paint2GC(var1);
         }
      } finally {
         unlockPage();
      }
   }

   private void paint2GC(WCGraphicsContext var1) {
      paintLog.finest("Entering");
      var1.setFontSmoothingType(this.fontSmoothingType);
      ArrayList var2;
      synchronized(this.frameQueue) {
         var2 = new ArrayList(this.frameQueue);
         this.frameQueue.clear();
      }

      paintLog.log(Level.FINEST, "Frames to render: {0}", var2);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         RenderFrame var4 = (RenderFrame)var3.next();
         paintLog.log(Level.FINEST, "Rendering: {0}", var4);
         Iterator var5 = var4.getRQList().iterator();

         while(var5.hasNext()) {
            WCRenderQueue var6 = (WCRenderQueue)var5.next();
            var1.saveState();
            if (var6.getClip() != null) {
               var1.setClip(var6.getClip());
            }

            var6.decode(var1);
            var1.restoreState();
         }
      }

      paintLog.finest("Exiting");
   }

   public void dropRenderFrames() {
      lockPage();

      try {
         this.currentFrame.drop();
         synchronized(this.frameQueue) {
            for(RenderFrame var2 = (RenderFrame)this.frameQueue.poll(); var2 != null; var2 = (RenderFrame)this.frameQueue.poll()) {
               var2.drop();
            }

         }
      } finally {
         unlockPage();
      }
   }

   public void dispatchFocusEvent(WCFocusEvent var1) {
      lockPage();

      try {
         log.log(Level.FINEST, "dispatchFocusEvent: " + var1);
         if (!this.isDisposed) {
            this.twkProcessFocusEvent(this.getPage(), var1.getID(), var1.getDirection());
            return;
         }

         log.log(Level.FINE, "Focus event for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public boolean dispatchKeyEvent(WCKeyEvent var1) {
      lockPage();

      boolean var2;
      try {
         log.log(Level.FINEST, "dispatchKeyEvent: " + var1);
         if (this.isDisposed) {
            log.log(Level.FINE, "Key event for a disposed web page.");
            var2 = false;
            return var2;
         }

         if (!WCKeyEvent.filterEvent(var1)) {
            var2 = this.twkProcessKeyEvent(this.getPage(), var1.getType(), var1.getText(), var1.getKeyIdentifier(), var1.getWindowsVirtualKeyCode(), var1.isShiftDown(), var1.isCtrlDown(), var1.isAltDown(), var1.isMetaDown(), (double)var1.getWhen() / 1000.0);
            return var2;
         }

         log.log(Level.FINEST, "filtered");
         var2 = false;
      } finally {
         unlockPage();
      }

      return var2;
   }

   public boolean dispatchMouseEvent(WCMouseEvent var1) {
      lockPage();

      boolean var2;
      try {
         log.log(Level.FINEST, "dispatchMouseEvent: " + var1.getX() + "," + var1.getY());
         if (this.isDisposed) {
            log.log(Level.FINE, "Mouse event for a disposed web page.");
            var2 = false;
            return var2;
         }

         var2 = !this.isDragConfirmed() && this.twkProcessMouseEvent(this.getPage(), var1.getID(), var1.getButton(), var1.getClickCount(), var1.getX(), var1.getY(), var1.getScreenX(), var1.getScreenY(), var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown(), var1.isPopupTrigger(), (double)var1.getWhen() / 1000.0);
      } finally {
         unlockPage();
      }

      return var2;
   }

   public boolean dispatchMouseWheelEvent(WCMouseWheelEvent var1) {
      lockPage();

      boolean var2;
      try {
         log.log(Level.FINEST, "dispatchMouseWheelEvent: " + var1);
         if (!this.isDisposed) {
            var2 = this.twkProcessMouseWheelEvent(this.getPage(), var1.getX(), var1.getY(), var1.getScreenX(), var1.getScreenY(), var1.getDeltaX(), var1.getDeltaY(), var1.isShiftDown(), var1.isControlDown(), var1.isAltDown(), var1.isMetaDown(), (double)var1.getWhen() / 1000.0);
            return var2;
         }

         log.log(Level.FINE, "MouseWheel event for a disposed web page.");
         var2 = false;
      } finally {
         unlockPage();
      }

      return var2;
   }

   public boolean dispatchInputMethodEvent(WCInputMethodEvent var1) {
      lockPage();

      try {
         log.log(Level.FINEST, "dispatchInputMethodEvent: " + var1);
         boolean var2;
         if (this.isDisposed) {
            log.log(Level.FINE, "InputMethod event for a disposed web page.");
            var2 = false;
            return var2;
         } else {
            switch (var1.getID()) {
               case 0:
                  var2 = this.twkProcessInputTextChange(this.getPage(), var1.getComposed(), var1.getCommitted(), var1.getAttributes(), var1.getCaretPosition());
                  return var2;
               case 1:
                  var2 = this.twkProcessCaretPositionChange(this.getPage(), var1.getCaretPosition());
                  return var2;
               default:
                  var2 = false;
                  return var2;
            }
         }
      } finally {
         unlockPage();
      }
   }

   public int dispatchDragOperation(int var1, String[] var2, String[] var3, int var4, int var5, int var6, int var7, int var8) {
      lockPage();

      int var9;
      try {
         log.log(Level.FINEST, "dispatchDragOperation: " + var4 + "," + var5 + " dndCommand:" + var1 + " dndAction" + var8);
         if (this.isDisposed) {
            log.log(Level.FINE, "DnD event for a disposed web page.");
            byte var13 = 0;
            return var13;
         }

         var9 = this.twkProcessDrag(this.getPage(), var1, var2, var3, var4, var5, var6, var7, var8);
      } finally {
         unlockPage();
      }

      return var9;
   }

   public void confirmStartDrag() {
      if (this.uiClient != null) {
         this.uiClient.confirmStartDrag();
      }

   }

   public boolean isDragConfirmed() {
      return this.uiClient != null ? this.uiClient.isDragConfirmed() : false;
   }

   public int[] getClientTextLocation(int var1) {
      lockPage();

      int[] var2;
      try {
         if (!this.isDisposed) {
            Invoker.getInvoker().checkEventThread();
            var2 = this.twkGetTextLocation(this.getPage(), var1);
            return var2;
         }

         log.log(Level.FINE, "getClientTextLocation() request for a disposed web page.");
         var2 = new int[]{0, 0, 0, 0};
      } finally {
         unlockPage();
      }

      return var2;
   }

   public int getClientLocationOffset(int var1, int var2) {
      lockPage();

      int var3;
      try {
         if (!this.isDisposed) {
            Invoker.getInvoker().checkEventThread();
            var3 = this.twkGetInsertPositionOffset(this.getPage());
            return var3;
         }

         log.log(Level.FINE, "getClientLocationOffset() request for a disposed web page.");
         var3 = 0;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public int getClientInsertPositionOffset() {
      lockPage();

      int var1;
      try {
         if (!this.isDisposed) {
            var1 = this.twkGetInsertPositionOffset(this.getPage());
            return var1;
         }

         log.log(Level.FINE, "getClientInsertPositionOffset() request for a disposed web page.");
         var1 = 0;
      } finally {
         unlockPage();
      }

      return var1;
   }

   public int getClientCommittedTextLength() {
      lockPage();

      int var1;
      try {
         if (!this.isDisposed) {
            var1 = this.twkGetCommittedTextLength(this.getPage());
            return var1;
         }

         log.log(Level.FINE, "getClientCommittedTextOffset() request for a disposed web page.");
         var1 = 0;
      } finally {
         unlockPage();
      }

      return var1;
   }

   public String getClientCommittedText() {
      lockPage();

      String var1;
      try {
         if (!this.isDisposed) {
            var1 = this.twkGetCommittedText(this.getPage());
            return var1;
         }

         log.log(Level.FINE, "getClientCommittedText() request for a disposed web page.");
         var1 = "";
      } finally {
         unlockPage();
      }

      return var1;
   }

   public String getClientSelectedText() {
      lockPage();

      String var1;
      try {
         if (!this.isDisposed) {
            var1 = this.twkGetSelectedText(this.getPage());
            String var2 = var1 != null ? var1 : "";
            return var2;
         }

         log.log(Level.FINE, "getClientSelectedText() request for a disposed web page.");
         var1 = "";
      } finally {
         unlockPage();
      }

      return var1;
   }

   public void dispose() {
      lockPage();

      try {
         log.log(Level.FINER, "dispose");
         this.stop();
         this.dropRenderFrames();
         this.isDisposed = true;
         this.twkDestroyPage(this.pPage);
         this.pPage = 0L;
         Iterator var1 = this.frames.iterator();

         while(true) {
            if (!var1.hasNext()) {
               this.frames.clear();
               if (this.backbuffer != null) {
                  this.backbuffer.deref();
                  this.backbuffer = null;
               }
               break;
            }

            long var2 = (Long)var1.next();
            log.log(Level.FINE, "Undestroyed frame view: " + var2);
         }
      } finally {
         unlockPage();
      }

   }

   public String getName(long var1) {
      lockPage();

      String var3;
      try {
         log.log(Level.FINE, "Get Name: frame = " + var1);
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               var3 = null;
               return var3;
            }

            var3 = this.twkGetName(var1);
            return var3;
         }

         log.log(Level.FINE, "getName() request for a disposed web page.");
         var3 = null;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public String getURL(long var1) {
      lockPage();

      String var3;
      try {
         log.log(Level.FINE, "Get URL: frame = " + var1);
         if (this.isDisposed) {
            log.log(Level.FINE, "getURL() request for a disposed web page.");
            var3 = null;
            return var3;
         }

         if (this.frames.contains(var1)) {
            var3 = this.twkGetURL(var1);
            return var3;
         }

         var3 = null;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public String getEncoding() {
      lockPage();

      String var1;
      try {
         log.log(Level.FINE, "Get encoding");
         if (this.isDisposed) {
            log.log(Level.FINE, "getEncoding() request for a disposed web page.");
            var1 = null;
            return var1;
         }

         var1 = this.twkGetEncoding(this.getPage());
      } finally {
         unlockPage();
      }

      return var1;
   }

   public void setEncoding(String var1) {
      lockPage();

      try {
         log.log(Level.FINE, "Set encoding: encoding = " + var1);
         if (this.isDisposed) {
            log.log(Level.FINE, "setEncoding() request for a disposed web page.");
            return;
         }

         if (var1 != null && !var1.isEmpty()) {
            this.twkSetEncoding(this.getPage(), var1);
         }
      } finally {
         unlockPage();
      }

   }

   public String getInnerText(long var1) {
      lockPage();

      String var3;
      try {
         log.log(Level.FINE, "Get inner text: frame = " + var1);
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               var3 = null;
               return var3;
            }

            var3 = this.twkGetInnerText(var1);
            return var3;
         }

         log.log(Level.FINE, "getInnerText() request for a disposed web page.");
         var3 = null;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public String getRenderTree(long var1) {
      lockPage();

      String var3;
      try {
         log.log(Level.FINE, "Get render tree: frame = " + var1);
         if (this.isDisposed) {
            log.log(Level.FINE, "getRenderTree() request for a disposed web page.");
            var3 = null;
            return var3;
         }

         if (!this.frames.contains(var1)) {
            var3 = null;
            return var3;
         }

         var3 = this.twkGetRenderTree(var1);
      } finally {
         unlockPage();
      }

      return var3;
   }

   public int getUnloadEventListenersCount(long var1) {
      lockPage();

      int var3;
      try {
         log.log(Level.FINE, "frame: " + var1);
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               byte var7 = 0;
               return var7;
            }

            var3 = this.twkGetUnloadEventListenersCount(var1);
            return var3;
         }

         log.log(Level.FINE, "request for a disposed web page.");
         var3 = 0;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public void forceRepaint() {
      this.repaintAll();
      this.updateContent(new WCRectangle(0.0F, 0.0F, (float)this.width, (float)this.height));
   }

   public String getContentType(long var1) {
      lockPage();

      String var3;
      try {
         log.log(Level.FINE, "Get content type: frame = " + var1);
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               var3 = null;
               return var3;
            }

            var3 = this.twkGetContentType(var1);
            return var3;
         }

         log.log(Level.FINE, "getContentType() request for a disposed web page.");
         var3 = null;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public String getTitle(long var1) {
      lockPage();

      String var3;
      try {
         log.log(Level.FINE, "Get title: frame = " + var1);
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               var3 = null;
               return var3;
            }

            var3 = this.twkGetTitle(var1);
            return var3;
         }

         log.log(Level.FINE, "getTitle() request for a disposed web page.");
         var3 = null;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public WCImage getIcon(long var1) {
      lockPage();

      WCImage var4;
      try {
         log.log(Level.FINE, "Get icon: frame = " + var1);
         String var3;
         if (this.isDisposed) {
            log.log(Level.FINE, "getIcon() request for a disposed web page.");
            var3 = null;
            return var3;
         }

         if (!this.frames.contains(var1)) {
            var3 = null;
            return var3;
         }

         var3 = this.twkGetIconURL(var1);
         if (var3 != null && !var3.isEmpty()) {
            var4 = WCGraphicsManager.getGraphicsManager().getIconImage(var3);
            return var4;
         }

         var4 = null;
      } finally {
         unlockPage();
      }

      return var4;
   }

   public void open(long var1, String var3) {
      lockPage();

      try {
         log.log(Level.FINE, "Open URL: " + var3);
         if (this.isDisposed) {
            log.log(Level.FINE, "open() request for a disposed web page.");
            return;
         }

         if (this.frames.contains(var1)) {
            if (this.twkIsLoading(var1)) {
               Invoker.getInvoker().postOnEventThread(() -> {
                  this.twkOpen(var1, var3);
               });
            } else {
               this.twkOpen(var1, var3);
            }

            return;
         }
      } finally {
         unlockPage();
      }

   }

   public void load(long var1, String var3, String var4) {
      lockPage();

      try {
         log.log(Level.FINE, "Load text: " + var3);
         if (var3 != null) {
            if (this.isDisposed) {
               log.log(Level.FINE, "load() request for a disposed web page.");
               return;
            }

            if (!this.frames.contains(var1)) {
               return;
            }

            if (this.twkIsLoading(var1)) {
               Invoker.getInvoker().postOnEventThread(() -> {
                  this.twkLoad(var1, var3, var4);
               });
            } else {
               this.twkLoad(var1, var3, var4);
            }

            return;
         }
      } finally {
         unlockPage();
      }

   }

   public void stop(long var1) {
      lockPage();

      try {
         log.log(Level.FINE, "Stop loading: frame = " + var1);
         if (this.isDisposed) {
            log.log(Level.FINE, "cancel() request for a disposed web page.");
            return;
         }

         if (this.frames.contains(var1)) {
            String var3 = this.twkGetURL(var1);
            String var4 = this.twkGetContentType(var1);
            this.twkStop(var1);
            this.fireLoadEvent(var1, 6, var3, var4, 1.0, 0);
            return;
         }
      } finally {
         unlockPage();
      }

   }

   public void stop() {
      lockPage();

      try {
         log.log(Level.FINE, "Stop loading sync");
         if (!this.isDisposed) {
            this.twkStopAll(this.getPage());
            return;
         }

         log.log(Level.FINE, "stopAll() request for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public void refresh(long var1) {
      lockPage();

      try {
         log.log(Level.FINE, "Refresh: frame = " + var1);
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               return;
            }

            this.twkRefresh(var1);
            return;
         }

         log.log(Level.FINE, "refresh() request for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public BackForwardList createBackForwardList() {
      return new BackForwardList(this);
   }

   public boolean goBack() {
      lockPage();

      boolean var1;
      try {
         log.log(Level.FINE, "Go back");
         if (!this.isDisposed) {
            var1 = this.twkGoBackForward(this.getPage(), -1);
            return var1;
         }

         log.log(Level.FINE, "goBack() request for a disposed web page.");
         var1 = false;
      } finally {
         unlockPage();
      }

      return var1;
   }

   public boolean goForward() {
      lockPage();

      boolean var1;
      try {
         log.log(Level.FINE, "Go forward");
         if (!this.isDisposed) {
            var1 = this.twkGoBackForward(this.getPage(), 1);
            return var1;
         }

         log.log(Level.FINE, "goForward() request for a disposed web page.");
         var1 = false;
      } finally {
         unlockPage();
      }

      return var1;
   }

   public boolean copy() {
      lockPage();

      boolean var1;
      try {
         log.log(Level.FINE, "Copy");
         if (!this.isDisposed) {
            long var7 = this.getMainFrame();
            boolean var3;
            if (!this.frames.contains(var7)) {
               var3 = false;
               return var3;
            }

            var3 = this.twkCopy(var7);
            return var3;
         }

         log.log(Level.FINE, "copy() request for a disposed web page.");
         var1 = false;
      } finally {
         unlockPage();
      }

      return var1;
   }

   public boolean find(String var1, boolean var2, boolean var3, boolean var4) {
      lockPage();

      boolean var5;
      try {
         log.log(Level.FINE, "Find in page: stringToFind = " + var1 + ", " + (var2 ? "forward" : "backward") + (var3 ? ", wrap" : "") + (var4 ? ", matchCase" : ""));
         if (this.isDisposed) {
            log.log(Level.FINE, "find() request for a disposed web page.");
            var5 = false;
            return var5;
         }

         var5 = this.twkFindInPage(this.getPage(), var1, var2, var3, var4);
      } finally {
         unlockPage();
      }

      return var5;
   }

   public boolean find(long var1, String var3, boolean var4, boolean var5, boolean var6) {
      lockPage();

      boolean var7;
      try {
         log.log(Level.FINE, "Find in frame: stringToFind = " + var3 + ", " + (var4 ? "forward" : "backward") + (var5 ? ", wrap" : "") + (var6 ? ", matchCase" : ""));
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               var7 = false;
               return var7;
            }

            var7 = this.twkFindInFrame(var1, var3, var4, var5, var6);
            return var7;
         }

         log.log(Level.FINE, "find() request for a disposed web page.");
         var7 = false;
      } finally {
         unlockPage();
      }

      return var7;
   }

   public void overridePreference(String var1, String var2) {
      lockPage();

      try {
         this.twkOverridePreference(this.getPage(), var1, var2);
      } finally {
         unlockPage();
      }

   }

   public void resetToConsistentStateBeforeTesting() {
      lockPage();

      try {
         this.twkResetToConsistentStateBeforeTesting(this.getPage());
      } finally {
         unlockPage();
      }

   }

   public float getZoomFactor(boolean var1) {
      lockPage();

      float var4;
      try {
         log.log(Level.FINE, "Get zoom factor, textOnly=" + var1);
         if (this.isDisposed) {
            log.log(Level.FINE, "getZoomFactor() request for a disposed web page.");
            float var8 = 1.0F;
            return var8;
         }

         long var2 = this.getMainFrame();
         if (!this.frames.contains(var2)) {
            var4 = 1.0F;
            return var4;
         }

         var4 = this.twkGetZoomFactor(var2, var1);
      } finally {
         unlockPage();
      }

      return var4;
   }

   public void setZoomFactor(float var1, boolean var2) {
      lockPage();

      try {
         log.fine(String.format("Set zoom factor %.2f, textOnly=%b", var1, var2));
         if (this.isDisposed) {
            log.log(Level.FINE, "setZoomFactor() request for a disposed web page.");
            return;
         }

         long var3 = this.getMainFrame();
         if (var3 != 0L && this.frames.contains(var3)) {
            this.twkSetZoomFactor(var3, var1, var2);
            return;
         }
      } finally {
         unlockPage();
      }

   }

   public void setFontSmoothingType(int var1) {
      this.fontSmoothingType = var1;
      this.repaintAll();
   }

   public void reset(long var1) {
      lockPage();

      try {
         log.log(Level.FINE, "Reset: frame = " + var1);
         if (!this.isDisposed) {
            if (var1 == 0L || !this.frames.contains(var1)) {
               return;
            }

            this.twkReset(var1);
            return;
         }

         log.log(Level.FINE, "reset() request for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public Object executeScript(long var1, String var3) throws JSException {
      lockPage();

      Object var4;
      try {
         log.log(Level.FINE, "execute script: \"" + var3 + "\" in frame = " + var1);
         if (this.isDisposed) {
            log.log(Level.FINE, "executeScript() request for a disposed web page.");
            var4 = null;
            return var4;
         }

         if (var1 == 0L || !this.frames.contains(var1)) {
            var4 = null;
            return var4;
         }

         var4 = this.twkExecuteScript(var1, var3);
      } finally {
         unlockPage();
      }

      return var4;
   }

   public long getMainFrame() {
      lockPage();

      long var1;
      try {
         log.log(Level.FINER, "getMainFrame: page = " + this.pPage);
         if (!this.isDisposed) {
            var1 = this.twkGetMainFrame(this.getPage());
            log.log(Level.FINER, "Main frame = " + var1);
            this.frames.add(var1);
            long var3 = var1;
            return var3;
         }

         log.log(Level.FINE, "getMainFrame() request for a disposed web page.");
         var1 = 0L;
      } finally {
         unlockPage();
      }

      return var1;
   }

   public long getParentFrame(long var1) {
      lockPage();

      long var3;
      try {
         log.log(Level.FINE, "getParentFrame: child = " + var1);
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               var3 = 0L;
               return var3;
            }

            var3 = this.twkGetParentFrame(var1);
            return var3;
         }

         log.log(Level.FINE, "getParentFrame() request for a disposed web page.");
         var3 = 0L;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public List getChildFrames(long var1) {
      lockPage();

      try {
         log.log(Level.FINE, "getChildFrames: parent = " + var1);
         Object var13;
         if (this.isDisposed) {
            log.log(Level.FINE, "getChildFrames() request for a disposed web page.");
            var13 = null;
            return (List)var13;
         } else if (!this.frames.contains(var1)) {
            var13 = null;
            return (List)var13;
         } else {
            long[] var3 = this.twkGetChildFrames(var1);
            LinkedList var4 = new LinkedList();
            long[] var5 = var3;
            int var6 = var3.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               long var8 = var5[var7];
               var4.add(var8);
            }

            LinkedList var14 = var4;
            return var14;
         }
      } finally {
         unlockPage();
      }
   }

   public WCRectangle getVisibleRect(long var1) {
      lockPage();

      WCRectangle var4;
      try {
         if (!this.frames.contains(var1)) {
            Object var8 = null;
            return (WCRectangle)var8;
         }

         int[] var3 = this.twkGetVisibleRect(var1);
         if (var3 == null) {
            var4 = null;
            return var4;
         }

         var4 = new WCRectangle((float)var3[0], (float)var3[1], (float)var3[2], (float)var3[3]);
      } finally {
         unlockPage();
      }

      return var4;
   }

   public void scrollToPosition(long var1, WCPoint var3) {
      lockPage();

      try {
         if (this.frames.contains(var1)) {
            this.twkScrollToPosition(var1, var3.getIntX(), var3.getIntY());
            return;
         }
      } finally {
         unlockPage();
      }

   }

   public WCSize getContentSize(long var1) {
      lockPage();

      Object var3;
      try {
         if (this.frames.contains(var1)) {
            int[] var8 = this.twkGetContentSize(var1);
            WCSize var4;
            if (var8 != null) {
               var4 = new WCSize((float)var8[0], (float)var8[1]);
               return var4;
            }

            var4 = null;
            return var4;
         }

         var3 = null;
      } finally {
         unlockPage();
      }

      return (WCSize)var3;
   }

   public Document getDocument(long var1) {
      lockPage();

      Document var3;
      try {
         log.log(Level.FINE, "getDocument");
         if (this.isDisposed) {
            log.log(Level.FINE, "getDocument() request for a disposed web page.");
            var3 = null;
            return var3;
         }

         if (this.frames.contains(var1)) {
            var3 = twkGetDocument(var1);
            return var3;
         }

         var3 = null;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public Element getOwnerElement(long var1) {
      lockPage();

      Element var3;
      try {
         log.log(Level.FINE, "getOwnerElement");
         if (this.isDisposed) {
            log.log(Level.FINE, "getOwnerElement() request for a disposed web page.");
            var3 = null;
            return var3;
         }

         if (!this.frames.contains(var1)) {
            var3 = null;
            return var3;
         }

         var3 = twkGetOwnerElement(var1);
      } finally {
         unlockPage();
      }

      return var3;
   }

   public boolean executeCommand(String var1, String var2) {
      lockPage();

      boolean var3;
      try {
         if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "command: [{0}], value: [{1}]", new Object[]{var1, var2});
         }

         if (!this.isDisposed) {
            var3 = this.twkExecuteCommand(this.getPage(), var1, var2);
            log.log(Level.FINE, "result: [{0}]", var3);
            boolean var4 = var3;
            return var4;
         }

         log.log(Level.FINE, "Web page is already disposed");
         var3 = false;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public boolean queryCommandEnabled(String var1) {
      lockPage();

      boolean var3;
      try {
         log.log(Level.FINE, "command: [{0}]", var1);
         boolean var2;
         if (this.isDisposed) {
            log.log(Level.FINE, "Web page is already disposed");
            var2 = false;
            return var2;
         }

         var2 = this.twkQueryCommandEnabled(this.getPage(), var1);
         log.log(Level.FINE, "result: [{0}]", var2);
         var3 = var2;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public boolean queryCommandState(String var1) {
      lockPage();

      boolean var3;
      try {
         log.log(Level.FINE, "command: [{0}]", var1);
         boolean var2;
         if (this.isDisposed) {
            log.log(Level.FINE, "Web page is already disposed");
            var2 = false;
            return var2;
         }

         var2 = this.twkQueryCommandState(this.getPage(), var1);
         log.log(Level.FINE, "result: [{0}]", var2);
         var3 = var2;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public String queryCommandValue(String var1) {
      lockPage();

      String var3;
      try {
         log.log(Level.FINE, "command: [{0}]", var1);
         String var2;
         if (this.isDisposed) {
            log.log(Level.FINE, "Web page is already disposed");
            var2 = null;
            return var2;
         }

         var2 = this.twkQueryCommandValue(this.getPage(), var1);
         log.log(Level.FINE, "result: [{0}]", var2);
         var3 = var2;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public boolean isEditable() {
      lockPage();

      boolean var1;
      try {
         log.log(Level.FINE, "isEditable");
         if (!this.isDisposed) {
            var1 = this.twkIsEditable(this.getPage());
            return var1;
         }

         log.log(Level.FINE, "isEditable() request for a disposed web page.");
         var1 = false;
      } finally {
         unlockPage();
      }

      return var1;
   }

   public void setEditable(boolean var1) {
      lockPage();

      try {
         log.log(Level.FINE, "setEditable");
         if (!this.isDisposed) {
            this.twkSetEditable(this.getPage(), var1);
            return;
         }

         log.log(Level.FINE, "setEditable() request for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public String getHtml(long var1) {
      lockPage();

      String var3;
      try {
         log.log(Level.FINE, "getHtml");
         if (!this.isDisposed) {
            if (!this.frames.contains(var1)) {
               var3 = null;
               return var3;
            }

            var3 = this.twkGetHtml(var1);
            return var3;
         }

         log.log(Level.FINE, "getHtml() request for a disposed web page.");
         var3 = null;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public int beginPrinting(float var1, float var2) {
      lockPage();

      byte var3;
      try {
         if (!this.isDisposed) {
            AtomicReference var11 = new AtomicReference(0);
            CountDownLatch var4 = new CountDownLatch(1);
            Invoker.getInvoker().invokeOnEventThread(() -> {
               try {
                  int var5 = this.twkBeginPrinting(this.getPage(), var1, var2);
                  var11.set(var5);
               } finally {
                  var4.countDown();
               }

            });

            try {
               var4.await();
            } catch (InterruptedException var9) {
               throw new RuntimeException(var9);
            }

            int var5 = (Integer)var11.get();
            return var5;
         }

         log.warning("beginPrinting() called for a disposed web page.");
         var3 = 0;
      } finally {
         unlockPage();
      }

      return var3;
   }

   public void endPrinting() {
      lockPage();

      try {
         if (this.isDisposed) {
            log.warning("endPrinting() called for a disposed web page.");
            return;
         }

         CountDownLatch var1 = new CountDownLatch(1);
         Invoker.getInvoker().invokeOnEventThread(() -> {
            try {
               this.twkEndPrinting(this.getPage());
            } finally {
               var1.countDown();
            }

         });

         try {
            var1.await();
         } catch (InterruptedException var6) {
            throw new RuntimeException(var6);
         }
      } finally {
         unlockPage();
      }

   }

   public void print(WCGraphicsContext var1, int var2, float var3) {
      lockPage();

      try {
         if (!this.isDisposed) {
            WCRenderQueue var4 = WCGraphicsManager.getGraphicsManager().createRenderQueue((WCRectangle)null, true);
            CountDownLatch var5 = new CountDownLatch(1);
            Invoker.getInvoker().invokeOnEventThread(() -> {
               try {
                  this.twkPrint(this.getPage(), var4, var2, var3);
               } finally {
                  var5.countDown();
               }

            });

            try {
               var5.await();
            } catch (InterruptedException var10) {
               var4.dispose();
               return;
            }

            var4.decode(var1);
            return;
         }

         log.warning("print() called for a disposed web page.");
      } finally {
         unlockPage();
      }

   }

   public int getPageHeight() {
      return this.getFrameHeight(this.getMainFrame());
   }

   public int getFrameHeight(long var1) {
      lockPage();

      int var4;
      try {
         log.log(Level.FINE, "Get page height");
         byte var8;
         if (this.isDisposed) {
            log.log(Level.FINE, "getFrameHeight() request for a disposed web page.");
            var8 = 0;
            return var8;
         }

         if (!this.frames.contains(var1)) {
            var8 = 0;
            return var8;
         }

         int var3 = this.twkGetFrameHeight(var1);
         log.log(Level.FINE, "Height = " + var3);
         var4 = var3;
      } finally {
         unlockPage();
      }

      return var4;
   }

   public float adjustFrameHeight(long var1, float var3, float var4, float var5) {
      lockPage();

      float var6;
      try {
         log.log(Level.FINE, "Adjust page height");
         if (this.isDisposed) {
            log.log(Level.FINE, "adjustFrameHeight() request for a disposed web page.");
            var6 = 0.0F;
            return var6;
         }

         if (this.frames.contains(var1)) {
            var6 = this.twkAdjustFrameHeight(var1, var3, var4, var5);
            return var6;
         }

         var6 = 0.0F;
      } finally {
         unlockPage();
      }

      return var6;
   }

   public boolean getUsePageCache() {
      lockPage();

      boolean var1;
      try {
         var1 = this.twkGetUsePageCache(this.getPage());
      } finally {
         unlockPage();
      }

      return var1;
   }

   public void setUsePageCache(boolean var1) {
      lockPage();

      try {
         this.twkSetUsePageCache(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   public boolean getDeveloperExtrasEnabled() {
      lockPage();

      boolean var2;
      try {
         boolean var1 = this.twkGetDeveloperExtrasEnabled(this.getPage());
         log.log(Level.FINE, "Getting developerExtrasEnabled, result: [{0}]", var1);
         var2 = var1;
      } finally {
         unlockPage();
      }

      return var2;
   }

   public void setDeveloperExtrasEnabled(boolean var1) {
      lockPage();

      try {
         log.log(Level.FINE, "Setting developerExtrasEnabled, value: [{0}]", var1);
         this.twkSetDeveloperExtrasEnabled(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   public boolean isJavaScriptEnabled() {
      lockPage();

      boolean var1;
      try {
         var1 = this.twkIsJavaScriptEnabled(this.getPage());
      } finally {
         unlockPage();
      }

      return var1;
   }

   public void setJavaScriptEnabled(boolean var1) {
      lockPage();

      try {
         this.twkSetJavaScriptEnabled(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   public boolean isContextMenuEnabled() {
      lockPage();

      boolean var1;
      try {
         var1 = this.twkIsContextMenuEnabled(this.getPage());
      } finally {
         unlockPage();
      }

      return var1;
   }

   public void setContextMenuEnabled(boolean var1) {
      lockPage();

      try {
         this.twkSetContextMenuEnabled(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   public void setUserStyleSheetLocation(String var1) {
      lockPage();

      try {
         this.twkSetUserStyleSheetLocation(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   public String getUserAgent() {
      lockPage();

      String var1;
      try {
         var1 = this.twkGetUserAgent(this.getPage());
      } finally {
         unlockPage();
      }

      return var1;
   }

   public void setUserAgent(String var1) {
      lockPage();

      try {
         this.twkSetUserAgent(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   public void setLocalStorageDatabasePath(String var1) {
      lockPage();

      try {
         this.twkSetLocalStorageDatabasePath(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   public void setLocalStorageEnabled(boolean var1) {
      lockPage();

      try {
         this.twkSetLocalStorageEnabled(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   public void connectInspectorFrontend() {
      lockPage();

      try {
         log.log(Level.FINE, "Connecting inspector frontend");
         this.twkConnectInspectorFrontend(this.getPage());
      } finally {
         unlockPage();
      }

   }

   public void disconnectInspectorFrontend() {
      lockPage();

      try {
         log.log(Level.FINE, "Disconnecting inspector frontend");
         this.twkDisconnectInspectorFrontend(this.getPage());
      } finally {
         unlockPage();
      }

   }

   public void dispatchInspectorMessageFromFrontend(String var1) {
      lockPage();

      try {
         if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Dispatching inspector message from frontend, message: [{0}]", var1);
         }

         this.twkDispatchInspectorMessageFromFrontend(this.getPage(), var1);
      } finally {
         unlockPage();
      }

   }

   private void fwkFrameCreated(long var1) {
      log.log(Level.FINE, "Frame created: frame = " + var1);
      if (this.frames.contains(var1)) {
         log.log(Level.FINE, "Error in fwkFrameCreated: frame is already in frames");
      } else {
         this.frames.add(var1);
      }
   }

   private void fwkFrameDestroyed(long var1) {
      log.log(Level.FINE, "Frame destroyed: frame = " + var1);
      if (!this.frames.contains(var1)) {
         log.log(Level.FINE, "Error in fwkFrameDestroyed: frame is not found in frames");
      } else {
         this.frames.remove(var1);
      }
   }

   private void fwkRepaint(int var1, int var2, int var3, int var4) {
      lockPage();

      try {
         if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "x: {0}, y: {1}, w: {2}, h: {3}", new Object[]{var1, var2, var3, var4});
         }

         this.addDirtyRect(new WCRectangle((float)var1, (float)var2, (float)var3, (float)var4));
      } finally {
         unlockPage();
      }

   }

   private void fwkScroll(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (paintLog.isLoggable(Level.FINEST)) {
         paintLog.finest("Scroll: " + var1 + " " + var2 + " " + var3 + " " + var4 + "  " + var5 + " " + var6);
      }

      if (this.pageClient != null && this.pageClient.isBackBufferSupported()) {
         this.scroll(var1, var2, var3, var4, var5, var6);
      } else {
         paintLog.finest("blit scrolling is switched off");
      }
   }

   private void fwkTransferFocus(boolean var1) {
      log.log(Level.FINER, "Transfer focus " + (var1 ? "forward" : "backward"));
      if (this.pageClient != null) {
         this.pageClient.transferFocus(var1);
      }

   }

   private void fwkSetCursor(long var1) {
      log.log(Level.FINER, "Set cursor: " + var1);
      if (this.pageClient != null) {
         this.pageClient.setCursor(var1);
      }

   }

   private void fwkSetFocus(boolean var1) {
      log.log(Level.FINER, "Set focus: " + (var1 ? "true" : "false"));
      if (this.pageClient != null) {
         this.pageClient.setFocus(var1);
      }

   }

   private void fwkSetTooltip(String var1) {
      log.log(Level.FINER, "Set tooltip: " + var1);
      if (this.pageClient != null) {
         this.pageClient.setTooltip(var1);
      }

   }

   private void fwkPrint() {
      log.log(Level.FINER, "Print");
      if (this.uiClient != null) {
         this.uiClient.print();
      }

   }

   private void fwkSetRequestURL(long var1, int var3, String var4) {
      log.log(Level.FINER, "Set request URL: id = " + var3 + ", url = " + var4);
      synchronized(this.requestURLs) {
         this.requestURLs.put(var3, var4);
      }
   }

   private void fwkRemoveRequestURL(long var1, int var3) {
      log.log(Level.FINER, "Set request URL: id = " + var3);
      synchronized(this.requestURLs) {
         this.requestURLs.remove(var3);
         this.requestStarted.remove(var3);
      }
   }

   private WebPage fwkCreateWindow(boolean var1, boolean var2, boolean var3, boolean var4) {
      log.log(Level.FINER, "Create window");
      return this.uiClient != null ? this.uiClient.createPage(var1, var2, var3, var4) : null;
   }

   private void fwkShowWindow() {
      log.log(Level.FINER, "Show window");
      if (this.uiClient != null) {
         this.uiClient.showView();
      }

   }

   private void fwkCloseWindow() {
      log.log(Level.FINER, "Close window");
      if (this.permitCloseWindowAction() && this.uiClient != null) {
         this.uiClient.closePage();
      }

   }

   private WCRectangle fwkGetWindowBounds() {
      log.log(Level.FINE, "Get window bounds");
      if (this.uiClient != null) {
         WCRectangle var1 = this.uiClient.getViewBounds();
         if (var1 != null) {
            return var1;
         }
      }

      return this.fwkGetPageBounds();
   }

   private void fwkSetWindowBounds(int var1, int var2, int var3, int var4) {
      log.log(Level.FINER, "Set window bounds: " + var1 + " " + var2 + " " + var3 + " " + var4);
      if (this.uiClient != null) {
         this.uiClient.setViewBounds(new WCRectangle((float)var1, (float)var2, (float)var3, (float)var4));
      }

   }

   private WCRectangle fwkGetPageBounds() {
      log.log(Level.FINER, "Get page bounds");
      return new WCRectangle(0.0F, 0.0F, (float)this.width, (float)this.height);
   }

   private void fwkSetScrollbarsVisible(boolean var1) {
   }

   private void fwkSetStatusbarText(String var1) {
      log.log(Level.FINER, "Set statusbar text: " + var1);
      if (this.uiClient != null) {
         this.uiClient.setStatusbarText(var1);
      }

   }

   private String[] fwkChooseFile(String var1, boolean var2, String var3) {
      log.log(Level.FINER, "Choose file, initial=" + var1);
      return this.uiClient != null ? this.uiClient.chooseFile(var1, var2, var3) : null;
   }

   private void fwkStartDrag(Object var1, int var2, int var3, int var4, int var5, String[] var6, Object[] var7, boolean var8) {
      log.log(Level.FINER, "Start drag: ");
      if (this.uiClient != null) {
         this.uiClient.startDrag(WCImage.getImage(var1), var2, var3, var4, var5, var6, var7, var8);
      }

   }

   private WCPoint fwkScreenToWindow(WCPoint var1) {
      log.log(Level.FINER, "fwkScreenToWindow");
      return this.pageClient != null ? this.pageClient.screenToWindow(var1) : var1;
   }

   private WCPoint fwkWindowToScreen(WCPoint var1) {
      log.log(Level.FINER, "fwkWindowToScreen");
      return this.pageClient != null ? this.pageClient.windowToScreen(var1) : var1;
   }

   private void fwkAlert(String var1) {
      log.log(Level.FINE, "JavaScript alert(): text = " + var1);
      if (this.uiClient != null) {
         this.uiClient.alert(var1);
      }

   }

   private boolean fwkConfirm(String var1) {
      log.log(Level.FINE, "JavaScript confirm(): text = " + var1);
      return this.uiClient != null ? this.uiClient.confirm(var1) : false;
   }

   private String fwkPrompt(String var1, String var2) {
      log.log(Level.FINE, "JavaScript prompt(): text = " + var1 + ", default = " + var2);
      return this.uiClient != null ? this.uiClient.prompt(var1, var2) : null;
   }

   private boolean fwkCanRunBeforeUnloadConfirmPanel() {
      log.log(Level.FINE, "JavaScript canRunBeforeUnloadConfirmPanel()");
      return this.uiClient != null ? this.uiClient.canRunBeforeUnloadConfirmPanel() : false;
   }

   private boolean fwkRunBeforeUnloadConfirmPanel(String var1) {
      log.log(Level.FINE, "JavaScript runBeforeUnloadConfirmPanel(): message = " + var1);
      return this.uiClient != null ? this.uiClient.runBeforeUnloadConfirmPanel(var1) : false;
   }

   private void fwkAddMessageToConsole(String var1, int var2, String var3) {
      log.log(Level.FINE, "fwkAddMessageToConsole(): message = " + var1 + ", lineNumber = " + var2 + ", sourceId = " + var3);
      if (this.pageClient != null) {
         this.pageClient.addMessageToConsole(var1, var2, var3);
      }

   }

   private void fwkFireLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
      log.log(Level.FINER, "Load event: pFrame = " + var1 + ", state = " + var3 + ", url = " + var4 + ", contenttype=" + var5 + ", progress = " + var6 + ", error = " + var8);
      this.fireLoadEvent(var1, var3, var4, var5, var6, var8);
   }

   private void fwkFireResourceLoadEvent(long var1, int var3, int var4, String var5, double var6, int var8) {
      log.log(Level.FINER, "Resource load event: pFrame = " + var1 + ", state = " + var3 + ", id = " + var4 + ", contenttype=" + var5 + ", progress = " + var6 + ", error = " + var8);
      String var9 = (String)this.requestURLs.get(var4);
      if (var9 == null) {
         log.log(Level.FINE, "Error in fwkFireResourceLoadEvent: unknown request id " + var4);
      } else {
         int var10 = var3;
         if (var3 == 20) {
            if (this.requestStarted.contains(var4)) {
               var10 = 21;
            } else {
               this.requestStarted.add(var4);
            }
         }

         this.fireResourceLoadEvent(var1, var10, var9, var5, var6, var8);
      }
   }

   private boolean fwkPermitNavigateAction(long var1, String var3) {
      log.log(Level.FINE, "Policy: permit NAVIGATE: pFrame = " + var1 + ", url = " + var3);
      return this.policyClient != null ? this.policyClient.permitNavigateAction(var1, this.str2url(var3)) : true;
   }

   private boolean fwkPermitRedirectAction(long var1, String var3) {
      log.log(Level.FINE, "Policy: permit REDIRECT: pFrame = " + var1 + ", url = " + var3);
      return this.policyClient != null ? this.policyClient.permitRedirectAction(var1, this.str2url(var3)) : true;
   }

   private boolean fwkPermitAcceptResourceAction(long var1, String var3) {
      log.log(Level.FINE, "Policy: permit ACCEPT_RESOURCE: pFrame + " + var1 + ", url = " + var3);
      return this.policyClient != null ? this.policyClient.permitAcceptResourceAction(var1, this.str2url(var3)) : true;
   }

   private boolean fwkPermitSubmitDataAction(long var1, String var3, String var4, boolean var5) {
      log.log(Level.FINE, "Policy: permit " + (var5 ? "" : "RE") + "SUBMIT_DATA: pFrame = " + var1 + ", url = " + var3 + ", httpMethod = " + var4);
      if (this.policyClient != null) {
         return var5 ? this.policyClient.permitSubmitDataAction(var1, this.str2url(var3), var4) : this.policyClient.permitResubmitDataAction(var1, this.str2url(var3), var4);
      } else {
         return true;
      }
   }

   private boolean fwkPermitEnableScriptsAction(long var1, String var3) {
      log.log(Level.FINE, "Policy: permit ENABLE_SCRIPTS: pFrame + " + var1 + ", url = " + var3);
      return this.policyClient != null ? this.policyClient.permitEnableScriptsAction(var1, this.str2url(var3)) : true;
   }

   private boolean fwkPermitNewWindowAction(long var1, String var3) {
      log.log(Level.FINE, "Policy: permit NEW_PAGE: pFrame = " + var1 + ", url = " + var3);
      return this.policyClient != null ? this.policyClient.permitNewPageAction(var1, this.str2url(var3)) : true;
   }

   private boolean permitCloseWindowAction() {
      log.log(Level.FINE, "Policy: permit CLOSE_PAGE");
      return this.policyClient != null ? this.policyClient.permitClosePageAction(this.getMainFrame()) : true;
   }

   private void fwkRepaintAll() {
      log.log(Level.FINE, "Repainting the entire page");
      this.repaintAll();
   }

   private boolean fwkSendInspectorMessageToFrontend(String var1) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "Sending inspector message to frontend, message: [{0}]", var1);
      }

      boolean var2 = false;
      if (this.inspectorClient != null) {
         log.log(Level.FINE, "Invoking inspector client");
         var2 = this.inspectorClient.sendMessageToFrontend(var1);
      }

      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "Result: [{0}]", var2);
      }

      return var2;
   }

   public static int getWorkerThreadCount() {
      return twkWorkerThreadCount();
   }

   private static native int twkWorkerThreadCount();

   private void fwkDidClearWindowObject(long var1, long var3) {
      if (this.pageClient != null) {
         this.pageClient.didClearWindowObject(var1, var3);
      }

   }

   private URL str2url(String var1) {
      try {
         return URLs.newURL(var1);
      } catch (MalformedURLException var3) {
         log.log(Level.FINE, "Exception while converting \"" + var1 + "\" to URL", var3);
         return null;
      }
   }

   private void fireLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
      Iterator var9 = this.loadListenerClients.iterator();

      while(var9.hasNext()) {
         LoadListenerClient var10 = (LoadListenerClient)var9.next();
         var10.dispatchLoadEvent(var1, var3, var4, var5, var6, var8);
      }

   }

   private void fireResourceLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8) {
      Iterator var9 = this.loadListenerClients.iterator();

      while(var9.hasNext()) {
         LoadListenerClient var10 = (LoadListenerClient)var9.next();
         var10.dispatchResourceLoadEvent(var1, var3, var4, var5, var6, var8);
      }

   }

   private void repaintAll() {
      this.dirtyRects.clear();
      this.addDirtyRect(new WCRectangle(0.0F, 0.0F, (float)this.width, (float)this.height));
   }

   int test_getFramesCount() {
      return this.frames.size();
   }

   private static native void twkInitWebCore(boolean var0, boolean var1, boolean var2);

   private native long twkCreatePage(boolean var1);

   private native void twkInit(long var1, boolean var3, float var4);

   private native void twkDestroyPage(long var1);

   private native long twkGetMainFrame(long var1);

   private native long twkGetParentFrame(long var1);

   private native long[] twkGetChildFrames(long var1);

   private native String twkGetName(long var1);

   private native String twkGetURL(long var1);

   private native String twkGetInnerText(long var1);

   private native String twkGetRenderTree(long var1);

   private native String twkGetContentType(long var1);

   private native String twkGetTitle(long var1);

   private native String twkGetIconURL(long var1);

   private static native Document twkGetDocument(long var0);

   private static native Element twkGetOwnerElement(long var0);

   private native void twkOpen(long var1, String var3);

   private native void twkOverridePreference(long var1, String var3, String var4);

   private native void twkResetToConsistentStateBeforeTesting(long var1);

   private native void twkLoad(long var1, String var3, String var4);

   private native boolean twkIsLoading(long var1);

   private native void twkStop(long var1);

   private native void twkStopAll(long var1);

   private native void twkRefresh(long var1);

   private native boolean twkGoBackForward(long var1, int var3);

   private native boolean twkCopy(long var1);

   private native boolean twkFindInPage(long var1, String var3, boolean var4, boolean var5, boolean var6);

   private native boolean twkFindInFrame(long var1, String var3, boolean var4, boolean var5, boolean var6);

   private native float twkGetZoomFactor(long var1, boolean var3);

   private native void twkSetZoomFactor(long var1, float var3, boolean var4);

   private native Object twkExecuteScript(long var1, String var3);

   private native void twkReset(long var1);

   private native int twkGetFrameHeight(long var1);

   private native int twkBeginPrinting(long var1, float var3, float var4);

   private native void twkEndPrinting(long var1);

   private native void twkPrint(long var1, WCRenderQueue var3, int var4, float var5);

   private native float twkAdjustFrameHeight(long var1, float var3, float var4, float var5);

   private native int[] twkGetVisibleRect(long var1);

   private native void twkScrollToPosition(long var1, int var3, int var4);

   private native int[] twkGetContentSize(long var1);

   private native void twkSetTransparent(long var1, boolean var3);

   private native void twkSetBackgroundColor(long var1, int var3);

   private native void twkSetBounds(long var1, int var3, int var4, int var5, int var6);

   private native void twkPrePaint(long var1);

   private native void twkUpdateContent(long var1, WCRenderQueue var3, int var4, int var5, int var6, int var7);

   private native void twkUpdateRendering(long var1);

   private native void twkPostPaint(long var1, WCRenderQueue var3, int var4, int var5, int var6, int var7);

   private native String twkGetEncoding(long var1);

   private native void twkSetEncoding(long var1, String var3);

   private native void twkProcessFocusEvent(long var1, int var3, int var4);

   private native boolean twkProcessKeyEvent(long var1, int var3, String var4, String var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10, double var11);

   private native boolean twkProcessMouseEvent(long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, double var15);

   private native boolean twkProcessMouseWheelEvent(long var1, int var3, int var4, int var5, int var6, float var7, float var8, boolean var9, boolean var10, boolean var11, boolean var12, double var13);

   private native boolean twkProcessInputTextChange(long var1, String var3, String var4, int[] var5, int var6);

   private native boolean twkProcessCaretPositionChange(long var1, int var3);

   private native int[] twkGetTextLocation(long var1, int var3);

   private native int twkGetInsertPositionOffset(long var1);

   private native int twkGetCommittedTextLength(long var1);

   private native String twkGetCommittedText(long var1);

   private native String twkGetSelectedText(long var1);

   private native int twkProcessDrag(long var1, int var3, String[] var4, String[] var5, int var6, int var7, int var8, int var9, int var10);

   private native boolean twkExecuteCommand(long var1, String var3, String var4);

   private native boolean twkQueryCommandEnabled(long var1, String var3);

   private native boolean twkQueryCommandState(long var1, String var3);

   private native String twkQueryCommandValue(long var1, String var3);

   private native boolean twkIsEditable(long var1);

   private native void twkSetEditable(long var1, boolean var3);

   private native String twkGetHtml(long var1);

   private native boolean twkGetUsePageCache(long var1);

   private native void twkSetUsePageCache(long var1, boolean var3);

   private native boolean twkGetDeveloperExtrasEnabled(long var1);

   private native void twkSetDeveloperExtrasEnabled(long var1, boolean var3);

   private native boolean twkIsJavaScriptEnabled(long var1);

   private native void twkSetJavaScriptEnabled(long var1, boolean var3);

   private native boolean twkIsContextMenuEnabled(long var1);

   private native void twkSetContextMenuEnabled(long var1, boolean var3);

   private native void twkSetUserStyleSheetLocation(long var1, String var3);

   private native String twkGetUserAgent(long var1);

   private native void twkSetUserAgent(long var1, String var3);

   private native void twkSetLocalStorageDatabasePath(long var1, String var3);

   private native void twkSetLocalStorageEnabled(long var1, boolean var3);

   private native int twkGetUnloadEventListenersCount(long var1);

   private native void twkConnectInspectorFrontend(long var1);

   private native void twkDisconnectInspectorFrontend(long var1);

   private native void twkDispatchInspectorMessageFromFrontend(long var1, String var3);

   private static native void twkDoJSCGarbageCollection();

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("jfxwebkit");
         log.finer("jfxwebkit loaded");
         boolean var0;
         if (CookieHandler.getDefault() == null) {
            var0 = Boolean.valueOf(System.getProperty("com.sun.webkit.setDefaultCookieHandler", "true"));
            if (var0) {
               CookieHandler.setDefault(new CookieManager());
            }
         }

         var0 = Boolean.valueOf(System.getProperty("com.sun.webkit.useJIT", "true"));
         boolean var1 = Boolean.valueOf(System.getProperty("com.sun.webkit.useDFGJIT", "true"));
         boolean var2 = Boolean.valueOf(System.getProperty("com.sun.webkit.useCSS3D", "false"));
         var2 = var2 && Platform.isSupported(ConditionalFeature.SCENE3D);
         twkInitWebCore(var0, var1, var2);
         return null;
      });
      firstWebPageCreated = false;
   }

   private static final class RenderFrame {
      private final List rqList;
      private int scrollDx;
      private int scrollDy;
      private final WCRectangle enclosingRect;

      private RenderFrame() {
         this.rqList = new LinkedList();
         this.enclosingRect = new WCRectangle();
      }

      private void addRenderQueue(WCRenderQueue var1) {
         if (!var1.isEmpty()) {
            this.rqList.add(var1);
            WCRectangle var2 = var1.getClip();
            if (this.enclosingRect.isEmpty()) {
               this.enclosingRect.setFrame(var2.getX(), var2.getY(), var2.getWidth(), var2.getHeight());
            } else if (!var2.isEmpty()) {
               WCRectangle.union(this.enclosingRect, var2, this.enclosingRect);
            }

         }
      }

      private List getRQList() {
         return this.rqList;
      }

      private WCRectangle getEnclosingRect() {
         return this.enclosingRect;
      }

      private void drop() {
         Iterator var1 = this.rqList.iterator();

         while(var1.hasNext()) {
            WCRenderQueue var2 = (WCRenderQueue)var1.next();
            var2.dispose();
         }

         this.rqList.clear();
         this.enclosingRect.setFrame(0.0F, 0.0F, 0.0F, 0.0F);
         this.scrollDx = 0;
         this.scrollDy = 0;
      }

      public String toString() {
         return "RenderFrame{rqList=" + this.rqList + ", enclosingRect=" + this.enclosingRect + "}";
      }

      // $FF: synthetic method
      RenderFrame(Object var1) {
         this();
      }
   }
}
