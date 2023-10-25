/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Platform;
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
                String string = Platform.determinePlatform();
                String string2 = "com.sun.glass.ui." + string.toLowerCase(Locale.ROOT) + "." + string + "PlatformFactory";
                Class<?> class_ = Class.forName(string2);
                instance = (PlatformFactory)class_.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (Exception exception) {
                exception.printStackTrace();
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

