/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PlatformLogger
 */
package com.sun.javafx.util;

import com.sun.javafx.logging.PlatformLogger;

public class Logging {
    private static PlatformLogger layoutLogger = null;
    private static PlatformLogger focusLogger = null;
    private static PlatformLogger inputLogger = null;
    private static PlatformLogger cssLogger = null;
    private static PlatformLogger javafxLogger = null;
    private static PlatformLogger accessibilityLogger = null;

    public static final PlatformLogger getLayoutLogger() {
        if (layoutLogger == null) {
            layoutLogger = PlatformLogger.getLogger((String)"javafx.scene.layout");
        }
        return layoutLogger;
    }

    public static final PlatformLogger getFocusLogger() {
        if (focusLogger == null) {
            focusLogger = PlatformLogger.getLogger((String)"javafx.scene.focus");
        }
        return focusLogger;
    }

    public static final PlatformLogger getInputLogger() {
        if (inputLogger == null) {
            inputLogger = PlatformLogger.getLogger((String)"javafx.scene.input");
        }
        return inputLogger;
    }

    public static final PlatformLogger getCSSLogger() {
        if (cssLogger == null) {
            cssLogger = PlatformLogger.getLogger((String)"javafx.css");
        }
        return cssLogger;
    }

    public static final PlatformLogger getJavaFXLogger() {
        if (javafxLogger == null) {
            javafxLogger = PlatformLogger.getLogger((String)"javafx");
        }
        return javafxLogger;
    }

    public static final PlatformLogger getAccessibilityLogger() {
        if (accessibilityLogger == null) {
            accessibilityLogger = PlatformLogger.getLogger((String)"javafx.accessibility");
        }
        return accessibilityLogger;
    }
}

