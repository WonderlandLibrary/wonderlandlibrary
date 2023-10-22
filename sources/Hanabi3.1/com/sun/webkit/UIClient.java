package com.sun.webkit;

import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCRectangle;

public interface UIClient {
   WebPage createPage(boolean var1, boolean var2, boolean var3, boolean var4);

   void closePage();

   void showView();

   WCRectangle getViewBounds();

   void setViewBounds(WCRectangle var1);

   void setStatusbarText(String var1);

   void alert(String var1);

   boolean confirm(String var1);

   String prompt(String var1, String var2);

   boolean canRunBeforeUnloadConfirmPanel();

   boolean runBeforeUnloadConfirmPanel(String var1);

   String[] chooseFile(String var1, boolean var2, String var3);

   void print();

   void startDrag(WCImage var1, int var2, int var3, int var4, int var5, String[] var6, Object[] var7, boolean var8);

   void confirmStartDrag();

   boolean isDragConfirmed();
}
