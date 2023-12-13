package com.sun.javafx.webkit;

import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.webkit.InputMethodClient;
import com.sun.webkit.Invoker;
import com.sun.webkit.WebPage;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.graphics.WCPoint;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.web.WebView;

public final class InputMethodClientImpl implements InputMethodClient, ExtendedInputMethodRequests {
   private static final Logger log = Logger.getLogger(InputMethodClientImpl.class.getName());
   private final WeakReference wvRef;
   private final WebPage webPage;
   private boolean state;

   public InputMethodClientImpl(WebView var1, WebPage var2) {
      this.wvRef = new WeakReference(var1);
      this.webPage = var2;
      if (var2 != null) {
         var2.setInputMethodClient(this);
      }

   }

   public void activateInputMethods(boolean var1) {
      WebView var2 = (WebView)this.wvRef.get();
      if (var2 != null && var2.getScene() != null) {
         var2.getScene().impl_enableInputMethodEvents(var1);
      }

      this.state = var1;
   }

   public boolean getInputMethodState() {
      return this.state;
   }

   public static WCInputMethodEvent convertToWCInputMethodEvent(InputMethodEvent var0) {
      ArrayList var1 = new ArrayList();
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;
      Iterator var4 = var0.getComposed().iterator();

      while(var4.hasNext()) {
         InputMethodTextRun var5 = (InputMethodTextRun)var4.next();
         String var6 = var5.getText();
         InputMethodHighlight var7 = var5.getHighlight();
         var1.add(var3);
         var1.add(var3 + var6.length());
         var1.add(var7 != InputMethodHighlight.SELECTED_CONVERTED && var7 != InputMethodHighlight.SELECTED_RAW ? 0 : 1);
         var3 += var6.length();
         var2.append(var6);
      }

      int var8 = var1.size();
      if (var8 == 0) {
         var1.add(0);
         var1.add(var3);
         var1.add(0);
         var8 = var1.size();
      }

      int[] var9 = new int[var8];

      for(int var10 = 0; var10 < var8; ++var10) {
         var9[var10] = (Integer)var1.get(var10);
      }

      return new WCInputMethodEvent(var0.getCommitted(), var2.toString(), var9, var0.getCaretPosition());
   }

   public Point2D getTextLocation(int var1) {
      FutureTask var2 = new FutureTask(() -> {
         int[] var2 = this.webPage.getClientTextLocation(var1);
         WCPoint var3 = this.webPage.getPageClient().windowToScreen(new WCPoint((float)var2[0], (float)(var2[1] + var2[3])));
         return new Point2D((double)var3.getIntX(), (double)var3.getIntY());
      });
      Invoker.getInvoker().invokeOnEventThread(var2);
      Point2D var3 = null;

      try {
         var3 = (Point2D)var2.get();
      } catch (ExecutionException var5) {
         log.log(Level.SEVERE, "InputMethodClientImpl.getTextLocation " + var5);
      } catch (InterruptedException var6) {
         log.log(Level.SEVERE, "InputMethodClientImpl.getTextLocation InterruptedException" + var6);
      }

      return var3;
   }

   public int getLocationOffset(int var1, int var2) {
      FutureTask var3 = new FutureTask(() -> {
         WCPoint var3 = this.webPage.getPageClient().windowToScreen(new WCPoint(0.0F, 0.0F));
         return this.webPage.getClientLocationOffset(var1 - var3.getIntX(), var2 - var3.getIntY());
      });
      Invoker.getInvoker().invokeOnEventThread(var3);
      int var4 = 0;

      try {
         var4 = (Integer)var3.get();
      } catch (ExecutionException var6) {
         log.log(Level.SEVERE, "InputMethodClientImpl.getLocationOffset " + var6);
      } catch (InterruptedException var7) {
         log.log(Level.SEVERE, "InputMethodClientImpl.getTextLocation InterruptedException" + var7);
      }

      return var4;
   }

   public void cancelLatestCommittedText() {
   }

   public String getSelectedText() {
      return this.webPage.getClientSelectedText();
   }

   public int getInsertPositionOffset() {
      return this.webPage.getClientInsertPositionOffset();
   }

   public String getCommittedText(int var1, int var2) {
      try {
         return this.webPage.getClientCommittedText().substring(var1, var2);
      } catch (StringIndexOutOfBoundsException var4) {
         throw new IllegalArgumentException(var4);
      }
   }

   public int getCommittedTextLength() {
      return this.webPage.getClientCommittedTextLength();
   }
}
