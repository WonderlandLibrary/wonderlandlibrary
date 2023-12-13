package com.sun.glass.ui;

import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import java.util.Locale;

public abstract class PlatformFactory {
   private static PlatformFactory instance;

   public static synchronized PlatformFactory getPlatformFactory() {
      if (instance == null) {
         try {
            String var0 = Platform.determinePlatform();
            String var1 = "com.sun.glass.ui." + var0.toLowerCase(Locale.ROOT) + "." + var0 + "PlatformFactory";
            Class var2 = Class.forName(var1);
            instance = (PlatformFactory)var2.newInstance();
         } catch (Exception var3) {
            var3.printStackTrace();
            System.out.println("Failed to load Glass factory class");
         }
      }

      return instance;
   }

   public abstract Application createApplication();

   public abstract MenuBarDelegate createMenuBarDelegate(MenuBar var1);

   public abstract MenuDelegate createMenuDelegate(Menu var1);

   public abstract MenuItemDelegate createMenuItemDelegate(MenuItem var1);

   public abstract ClipboardDelegate createClipboardDelegate();
}
