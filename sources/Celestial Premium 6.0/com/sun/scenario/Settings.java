/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.util.Callback
 */
package com.sun.scenario;

import com.sun.javafx.tk.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.util.Callback;

public class Settings {
    private final Map<String, String> settings = new HashMap<String, String>(5);
    private final CopyOnWriteArrayList<Callback<String, Void>> listeners = new CopyOnWriteArrayList();
    private static final Object SETTINGS_KEY = new StringBuilder("SettingsKey");

    private static synchronized Settings getInstance() {
        Map<Object, Object> map = Toolkit.getToolkit().getContextMap();
        Settings settings = (Settings)map.get(SETTINGS_KEY);
        if (settings == null) {
            settings = new Settings();
            map.put(SETTINGS_KEY, settings);
        }
        return settings;
    }

    public static void set(String string, String string2) {
        Settings.getInstance().setImpl(string, string2);
    }

    private void setImpl(String string, String string2) {
        this.checkKeyArg(string);
        this.settings.put(string, string2);
        for (Callback<String, Void> callback : this.listeners) {
            callback.call((Object)string);
        }
    }

    public static String get(String string) {
        return Settings.getInstance().getImpl(string);
    }

    private String getImpl(String string) {
        this.checkKeyArg(string);
        String string2 = this.settings.get(string);
        if (string2 == null) {
            try {
                string2 = System.getProperty(string);
            }
            catch (SecurityException securityException) {
                // empty catch block
            }
        }
        return string2;
    }

    public static boolean getBoolean(String string) {
        return Settings.getInstance().getBooleanImpl(string);
    }

    private boolean getBooleanImpl(String string) {
        String string2 = this.getImpl(string);
        return "true".equals(string2);
    }

    public static boolean getBoolean(String string, boolean bl) {
        return Settings.getInstance().getBooleanImpl(string, bl);
    }

    private boolean getBooleanImpl(String string, boolean bl) {
        String string2 = this.getImpl(string);
        boolean bl2 = bl;
        if (string2 != null) {
            if ("false".equals(string2)) {
                bl2 = false;
            } else if ("true".equals(string2)) {
                bl2 = true;
            }
        }
        return bl2;
    }

    public static int getInt(String string, int n) {
        return Settings.getInstance().getIntImpl(string, n);
    }

    private int getIntImpl(String string, int n) {
        String string2 = this.getImpl(string);
        int n2 = n;
        try {
            n2 = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        return n2;
    }

    public static void addPropertyChangeListener(Callback<String, Void> callback) {
        Settings.getInstance().addPropertyChangeListenerImpl(callback);
    }

    private void addPropertyChangeListenerImpl(Callback<String, Void> callback) {
        this.listeners.add(callback);
    }

    public static void removePropertyChangeListener(Callback<String, Void> callback) {
        Settings.getInstance().removePropertyChangeListenerImpl(callback);
    }

    private void removePropertyChangeListenerImpl(Callback<String, Void> callback) {
        this.listeners.remove(callback);
    }

    private void checkKeyArg(String string) {
        if (null == string || "".equals(string)) {
            throw new IllegalArgumentException("null key not allowed");
        }
    }

    private Settings() {
    }
}

