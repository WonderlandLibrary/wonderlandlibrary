/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.FXPermissions
 *  com.sun.javafx.PlatformUtil
 *  javafx.application.ConditionalFeature
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.SimpleBooleanProperty
 *  javafx.scene.Scene
 *  javafx.util.FXPermission
 */
package com.sun.javafx.application;

import com.sun.glass.ui.Application;
import com.sun.javafx.FXPermissions;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.tk.TKListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.ModuleHelper;
import com.sun.javafx.util.Utils;
import java.lang.module.ModuleDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.application.ConditionalFeature;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.util.FXPermission;

public class PlatformImpl {
    private static AtomicBoolean initialized = new AtomicBoolean(false);
    private static AtomicBoolean platformExit = new AtomicBoolean(false);
    private static AtomicBoolean toolkitExit = new AtomicBoolean(false);
    private static CountDownLatch startupLatch = new CountDownLatch(1);
    private static AtomicBoolean listenersRegistered = new AtomicBoolean(false);
    private static TKListener toolkitListener = null;
    private static volatile boolean implicitExit = true;
    private static boolean taskbarApplication = true;
    private static boolean contextual2DNavigation;
    private static AtomicInteger pendingRunnables;
    private static AtomicInteger numWindows;
    private static volatile boolean firstWindowShown;
    private static volatile boolean lastWindowClosed;
    private static AtomicBoolean reallyIdle;
    private static Set<FinishListener> finishListeners;
    private static final Object runLaterLock;
    private static Boolean isGraphicsSupported;
    private static Boolean isControlsSupported;
    private static Boolean isMediaSupported;
    private static Boolean isWebSupported;
    private static Boolean isSWTSupported;
    private static Boolean isSwingSupported;
    private static Boolean isFXMLSupported;
    private static Boolean hasTwoLevelFocus;
    private static Boolean hasVirtualKeyboard;
    private static Boolean hasTouch;
    private static Boolean hasMultiTouch;
    private static Boolean hasPointer;
    private static boolean isThreadMerged;
    private static String applicationType;
    private static BooleanProperty accessibilityActive;
    private static CountDownLatch allNestedLoopsExitedLatch;
    private static final boolean verbose;
    private static final boolean DEBUG;
    private static final FXPermission FXCANVAS_PERMISSION;
    private static final CountDownLatch platformExitLatch;
    private static boolean isModena;
    private static boolean isCaspian;
    private static String accessibilityTheme;

    public static void setTaskbarApplication(boolean bl) {
        taskbarApplication = bl;
    }

    public static boolean isTaskbarApplication() {
        return taskbarApplication;
    }

    public static void setApplicationName(Class class_) {
        PlatformImpl.runLater(() -> Application.GetApplication().setName(class_.getName()));
    }

    public static boolean isContextual2DNavigation() {
        return contextual2DNavigation;
    }

    public static void startup(Runnable runnable) {
        PlatformImpl.startup(runnable, false);
    }

    public static void startup(Runnable runnable, boolean bl) {
        Object object;
        if (platformExit.get()) {
            throw new IllegalStateException("Platform.exit has been called");
        }
        if (initialized.getAndSet(true)) {
            if (bl) {
                throw new IllegalStateException("Toolkit already initialized");
            }
            PlatformImpl.runLater(runnable);
            return;
        }
        Module module = PlatformImpl.class.getModule();
        ModuleDescriptor moduleDescriptor = module.getDescriptor();
        if (!module.isNamed() || !"javafx.graphics".equals(module.getName()) || moduleDescriptor == null || moduleDescriptor.isAutomatic() || moduleDescriptor.isOpen()) {
            object = "Unsupported JavaFX configuration: classes were loaded from '" + module + "'";
            if (moduleDescriptor != null) {
                object = (String)object + ", isAutomatic: " + moduleDescriptor.isAutomatic();
                object = (String)object + ", isOpen: " + moduleDescriptor.isOpen();
            }
            Logging.getJavaFXLogger().warning((String)object);
        }
        object = AccessController.doPrivileged(() -> {
            applicationType = System.getProperty("com.sun.javafx.application.type");
            if (applicationType == null) {
                applicationType = "";
            }
            contextual2DNavigation = Boolean.getBoolean("com.sun.javafx.isContextual2DNavigation");
            String string = System.getProperty("com.sun.javafx.twoLevelFocus");
            if (string != null) {
                hasTwoLevelFocus = Boolean.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.virtualKeyboard")) != null) {
                if (string.equalsIgnoreCase("none")) {
                    hasVirtualKeyboard = false;
                } else if (string.equalsIgnoreCase("javafx")) {
                    hasVirtualKeyboard = true;
                } else if (string.equalsIgnoreCase("native")) {
                    hasVirtualKeyboard = true;
                }
            }
            if ((string = System.getProperty("com.sun.javafx.touch")) != null) {
                hasTouch = Boolean.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.multiTouch")) != null) {
                hasMultiTouch = Boolean.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.pointer")) != null) {
                hasPointer = Boolean.valueOf(string);
            }
            if ((string = System.getProperty("javafx.embed.singleThread")) != null && (isThreadMerged = Boolean.valueOf(string).booleanValue()) && !PlatformImpl.isSupported(ConditionalFeature.SWING)) {
                isThreadMerged = false;
                if (verbose) {
                    System.err.println("WARNING: javafx.embed.singleThread ignored (javafx.swing module not found)");
                }
            }
            return null;
        });
        if (DEBUG) {
            System.err.println("PlatformImpl::startup : applicationType = " + applicationType);
        }
        if ("FXCanvas".equals(applicationType)) {
            PlatformImpl.initFXCanvas();
        }
        if (!taskbarApplication) {
            Void void_ = AccessController.doPrivileged(() -> {
                System.setProperty("glass.taskbarApplication", "false");
                return null;
            });
        }
        toolkitListener = new TKListener(){

            @Override
            public void changedTopLevelWindows(List<TKStage> list) {
                numWindows.set(list.size());
                PlatformImpl.checkIdle();
            }

            @Override
            public void exitedLastNestedLoop() {
                if (platformExit.get()) {
                    allNestedLoopsExitedLatch.countDown();
                }
                PlatformImpl.checkIdle();
            }
        };
        Toolkit.getToolkit().addTkListener(toolkitListener);
        Toolkit.getToolkit().startup(() -> {
            startupLatch.countDown();
            runnable.run();
        });
        if (isThreadMerged) {
            PlatformImpl.installFwEventQueue();
        }
    }

    private static void initDeviceDetailsFXCanvas() {
        long l = AccessController.doPrivileged(() -> Long.getLong("javafx.embed.eventProc", 0L));
        if (l != 0L) {
            HashMap<String, Long> hashMap = Application.getDeviceDetails();
            if (hashMap == null) {
                hashMap = new HashMap<String, Long>();
                Application.setDeviceDetails(hashMap);
            }
            if (hashMap.get("javafx.embed.eventProc") == null) {
                hashMap.put("javafx.embed.eventProc", l);
            }
        }
    }

    private static void addExportsToFXCanvas(Class<?> class_) {
        String[] arrstring = new String[]{"com.sun.glass.ui", "com.sun.javafx.cursor", "com.sun.javafx.embed", "com.sun.javafx.stage", "com.sun.javafx.tk"};
        if (DEBUG) {
            System.err.println("addExportsToFXCanvas: class = " + class_);
        }
        Object object = ModuleHelper.getModule(PlatformImpl.class);
        Object object2 = ModuleHelper.getModule(class_);
        for (String string : arrstring) {
            if (DEBUG) {
                System.err.println("add export of " + string + " from " + object + " to " + object2);
            }
            ModuleHelper.addExports(object, string, object2);
        }
    }

    private static void initFXCanvas() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkPermission((Permission)FXCANVAS_PERMISSION);
            }
            catch (SecurityException securityException) {
                System.err.println("FXCanvas: no permission to access JavaFX internals");
                securityException.printStackTrace();
                return;
            }
        }
        Predicate<StackWalker.StackFrame> predicate = stackFrame -> !stackFrame.getClassName().startsWith("javafx.application.") && !stackFrame.getClassName().startsWith("com.sun.javafx.application.");
        StackWalker stackWalker = AccessController.doPrivileged(() -> StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE));
        Optional optional = stackWalker.walk(stream -> stream.filter(predicate).findFirst());
        if (optional.isPresent()) {
            Class<?> class_ = ((StackWalker.StackFrame)optional.get()).getDeclaringClass();
            if (DEBUG) {
                System.err.println("callerClassName = " + class_);
            }
            if ("javafx.embed.swt.FXCanvas".equals(class_.getName())) {
                PlatformImpl.initDeviceDetailsFXCanvas();
                PlatformImpl.addExportsToFXCanvas(class_);
            }
        }
    }

    private static void installFwEventQueue() {
        PlatformImpl.invokeSwingFXUtilsMethod("installFwEventQueue");
    }

    private static void removeFwEventQueue() {
        PlatformImpl.invokeSwingFXUtilsMethod("removeFwEventQueue");
    }

    private static void invokeSwingFXUtilsMethod(String string) {
        try {
            Class<?> class_ = Class.forName("com.sun.javafx.embed.swing.SwingFXUtilsImpl");
            Method method = class_.getDeclaredMethod(string, new Class[0]);
            PlatformImpl.waitForStart();
            method.invoke(null, new Object[0]);
        }
        catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
            throw new RuntimeException("Property javafx.embed.singleThread is not supported");
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new RuntimeException(invocationTargetException);
        }
    }

    private static void waitForStart() {
        if (startupLatch.getCount() > 0L) {
            try {
                startupLatch.await();
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public static boolean isFxApplicationThread() {
        return Toolkit.getToolkit().isFxUserThread();
    }

    public static void runLater(Runnable runnable) {
        PlatformImpl.runLater(runnable, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void runLater(Runnable runnable, boolean bl) {
        if (!initialized.get()) {
            throw new IllegalStateException("Toolkit not initialized");
        }
        pendingRunnables.incrementAndGet();
        PlatformImpl.waitForStart();
        Object object = runLaterLock;
        synchronized (object) {
            if (!bl && toolkitExit.get()) {
                pendingRunnables.decrementAndGet();
                return;
            }
            AccessControlContext accessControlContext = AccessController.getContext();
            Toolkit.getToolkit().defer(() -> {
                try {
                    Void void_ = AccessController.doPrivileged(() -> {
                        runnable.run();
                        return null;
                    }, accessControlContext);
                }
                finally {
                    pendingRunnables.decrementAndGet();
                    PlatformImpl.checkIdle();
                }
            });
        }
    }

    public static void runAndWait(Runnable runnable) {
        PlatformImpl.runAndWait(runnable, false);
    }

    private static void runAndWait(Runnable runnable, boolean bl) {
        if (PlatformImpl.isFxApplicationThread()) {
            try {
                runnable.run();
            }
            catch (Throwable throwable) {
                System.err.println("Exception in runnable");
                throwable.printStackTrace();
            }
        } else {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            PlatformImpl.runLater(() -> {
                try {
                    runnable.run();
                }
                finally {
                    countDownLatch.countDown();
                }
            }, bl);
            if (!bl && toolkitExit.get()) {
                throw new IllegalStateException("Toolkit has exited");
            }
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public static void setImplicitExit(boolean bl) {
        implicitExit = bl;
        PlatformImpl.checkIdle();
    }

    public static boolean isImplicitExit() {
        return implicitExit;
    }

    public static void addListener(FinishListener finishListener) {
        listenersRegistered.set(true);
        finishListeners.add(finishListener);
    }

    public static void removeListener(FinishListener finishListener) {
        finishListeners.remove(finishListener);
        listenersRegistered.set(!finishListeners.isEmpty());
        if (!listenersRegistered.get()) {
            PlatformImpl.checkIdle();
        }
    }

    private static void notifyFinishListeners(boolean bl) {
        if (listenersRegistered.get()) {
            for (FinishListener finishListener : finishListeners) {
                if (bl) {
                    finishListener.exitCalled();
                    continue;
                }
                finishListener.idle(implicitExit);
            }
        } else if (implicitExit || platformExit.get()) {
            PlatformImpl.tkExit();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void checkIdle() {
        if (!initialized.get()) {
            return;
        }
        if (!PlatformImpl.isFxApplicationThread()) {
            PlatformImpl.runLater(() -> {});
            return;
        }
        boolean bl = false;
        Class<PlatformImpl> class_ = PlatformImpl.class;
        synchronized (PlatformImpl.class) {
            int n = numWindows.get();
            if (n > 0) {
                firstWindowShown = true;
                lastWindowClosed = false;
                reallyIdle.set(false);
            } else if (n == 0 && firstWindowShown) {
                lastWindowClosed = true;
            }
            if (lastWindowClosed && pendingRunnables.get() == 0 && (toolkitExit.get() || !Toolkit.getToolkit().isNestedLoopRunning())) {
                if (reallyIdle.getAndSet(true)) {
                    bl = true;
                    lastWindowClosed = false;
                } else {
                    PlatformImpl.runLater(() -> {});
                }
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            if (bl) {
                PlatformImpl.notifyFinishListeners(false);
            }
            return;
        }
    }

    static CountDownLatch test_getPlatformExitLatch() {
        return platformExitLatch;
    }

    public static void tkExit() {
        if (toolkitExit.getAndSet(true)) {
            return;
        }
        if (initialized.get()) {
            if (platformExit.get()) {
                PlatformImpl.runAndWait(() -> {
                    if (Toolkit.getToolkit().isNestedLoopRunning()) {
                        Toolkit.getToolkit().exitAllNestedEventLoops();
                    } else {
                        allNestedLoopsExitedLatch.countDown();
                    }
                }, true);
                try {
                    allNestedLoopsExitedLatch.await();
                }
                catch (InterruptedException interruptedException) {
                    throw new RuntimeException("Could not exit all nested event loops");
                }
            }
            PlatformImpl.runAndWait(() -> Toolkit.getToolkit().exit(), true);
            if (isThreadMerged) {
                PlatformImpl.removeFwEventQueue();
            }
            Toolkit.getToolkit().removeTkListener(toolkitListener);
            toolkitListener = null;
            platformExitLatch.countDown();
        }
    }

    public static BooleanProperty accessibilityActiveProperty() {
        return accessibilityActive;
    }

    public static void exit() {
        platformExit.set(true);
        PlatformImpl.notifyFinishListeners(true);
    }

    private static Boolean checkForClass(String string) {
        try {
            Class.forName(string, false, PlatformImpl.class.getClassLoader());
            return Boolean.TRUE;
        }
        catch (ClassNotFoundException classNotFoundException) {
            return Boolean.FALSE;
        }
    }

    public static boolean isSupported(ConditionalFeature conditionalFeature) {
        boolean bl = PlatformImpl.isSupportedImpl(conditionalFeature);
        if (bl && conditionalFeature == ConditionalFeature.TRANSPARENT_WINDOW) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                try {
                    securityManager.checkPermission((Permission)FXPermissions.CREATE_TRANSPARENT_WINDOW_PERMISSION);
                }
                catch (SecurityException securityException) {
                    return false;
                }
            }
            return true;
        }
        return bl;
    }

    public static void setDefaultPlatformUserAgentStylesheet() {
        PlatformImpl.setPlatformUserAgentStylesheet("MODENA");
    }

    public static boolean isModena() {
        return isModena;
    }

    public static boolean isCaspian() {
        return isCaspian;
    }

    public static void setPlatformUserAgentStylesheet(String string) {
        if (PlatformImpl.isFxApplicationThread()) {
            PlatformImpl._setPlatformUserAgentStylesheet(string);
        } else {
            PlatformImpl.runLater(() -> PlatformImpl._setPlatformUserAgentStylesheet(string));
        }
    }

    public static boolean setAccessibilityTheme(String string) {
        if (accessibilityTheme != null) {
            StyleManager.getInstance().removeUserAgentStylesheet(accessibilityTheme);
            accessibilityTheme = null;
        }
        PlatformImpl._setAccessibilityTheme(string);
        if (accessibilityTheme != null) {
            StyleManager.getInstance().addUserAgentStylesheet(accessibilityTheme);
            return true;
        }
        return false;
    }

    private static void _setAccessibilityTheme(String string) {
        String string2 = AccessController.doPrivileged(() -> System.getProperty("com.sun.javafx.highContrastTheme"));
        if (PlatformImpl.isCaspian()) {
            if (string != null || string2 != null) {
                accessibilityTheme = "com/sun/javafx/scene/control/skin/caspian/highcontrast.css";
            }
        } else if (PlatformImpl.isModena()) {
            if (string2 != null) {
                switch (string2.toUpperCase()) {
                    case "BLACKONWHITE": {
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/blackOnWhite.css";
                        break;
                    }
                    case "WHITEONBLACK": {
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/whiteOnBlack.css";
                        break;
                    }
                    case "YELLOWONBLACK": {
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/yellowOnBlack.css";
                        break;
                    }
                }
            } else if (string != null) {
                switch (HighContrastScheme.valueOf(string)) {
                    case HIGH_CONTRAST_WHITE: {
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/blackOnWhite.css";
                        break;
                    }
                    case HIGH_CONTRAST_BLACK: {
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/whiteOnBlack.css";
                        break;
                    }
                    case HIGH_CONTRAST_1: 
                    case HIGH_CONTRAST_2: {
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/yellowOnBlack.css";
                        break;
                    }
                }
            }
        }
    }

    private static void _setPlatformUserAgentStylesheet(String string) {
        isCaspian = false;
        isModena = false;
        String string2 = AccessController.doPrivileged(() -> System.getProperty("javafx.userAgentStylesheetUrl"));
        if (string2 != null) {
            string = string2;
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        if ("CASPIAN".equalsIgnoreCase(string)) {
            isCaspian = true;
            arrayList.add("com/sun/javafx/scene/control/skin/caspian/caspian.css");
            if (PlatformImpl.isSupported(ConditionalFeature.INPUT_TOUCH)) {
                arrayList.add("com/sun/javafx/scene/control/skin/caspian/embedded.css");
                if (Utils.isQVGAScreen()) {
                    arrayList.add("com/sun/javafx/scene/control/skin/caspian/embedded-qvga.css");
                }
                if (PlatformUtil.isAndroid()) {
                    arrayList.add("com/sun/javafx/scene/control/skin/caspian/android.css");
                }
                if (PlatformUtil.isIOS()) {
                    arrayList.add("com/sun/javafx/scene/control/skin/caspian/ios.css");
                }
            }
            if (PlatformImpl.isSupported(ConditionalFeature.TWO_LEVEL_FOCUS)) {
                arrayList.add("com/sun/javafx/scene/control/skin/caspian/two-level-focus.css");
            }
            if (PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                arrayList.add("com/sun/javafx/scene/control/skin/caspian/fxvk.css");
            }
            if (!PlatformImpl.isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
                arrayList.add("com/sun/javafx/scene/control/skin/caspian/caspian-no-transparency.css");
            }
        } else if ("MODENA".equalsIgnoreCase(string)) {
            isModena = true;
            arrayList.add("com/sun/javafx/scene/control/skin/modena/modena.css");
            if (PlatformImpl.isSupported(ConditionalFeature.INPUT_TOUCH)) {
                arrayList.add("com/sun/javafx/scene/control/skin/modena/touch.css");
            }
            if (PlatformUtil.isEmbedded()) {
                arrayList.add("com/sun/javafx/scene/control/skin/modena/modena-embedded-performance.css");
            }
            if (PlatformUtil.isAndroid()) {
                arrayList.add("com/sun/javafx/scene/control/skin/modena/android.css");
            }
            if (PlatformUtil.isIOS()) {
                arrayList.add("com/sun/javafx/scene/control/skin/modena/ios.css");
            }
            if (PlatformImpl.isSupported(ConditionalFeature.TWO_LEVEL_FOCUS)) {
                arrayList.add("com/sun/javafx/scene/control/skin/modena/two-level-focus.css");
            }
            if (PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                arrayList.add("com/sun/javafx/scene/control/skin/caspian/fxvk.css");
            }
            if (!PlatformImpl.isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
                arrayList.add("com/sun/javafx/scene/control/skin/modena/modena-no-transparency.css");
            }
        } else {
            arrayList.add(string);
        }
        PlatformImpl._setAccessibilityTheme(Toolkit.getToolkit().getThemeName());
        if (accessibilityTheme != null) {
            arrayList.add(accessibilityTheme);
        }
        Object object = AccessController.doPrivileged(() -> {
            StyleManager.getInstance().setUserAgentStylesheets(arrayList);
            return null;
        });
    }

    public static void addNoTransparencyStylesheetToScene(Scene scene) {
        if (PlatformImpl.isCaspian()) {
            AccessController.doPrivileged(() -> {
                StyleManager.getInstance().addUserAgentStylesheet(scene, "com/sun/javafx/scene/control/skin/caspian/caspian-no-transparency.css");
                return null;
            });
        } else if (PlatformImpl.isModena()) {
            AccessController.doPrivileged(() -> {
                StyleManager.getInstance().addUserAgentStylesheet(scene, "com/sun/javafx/scene/control/skin/modena/modena-no-transparency.css");
                return null;
            });
        }
    }

    private static boolean isSupportedImpl(ConditionalFeature conditionalFeature) {
        switch (conditionalFeature) {
            case GRAPHICS: {
                if (isGraphicsSupported == null) {
                    isGraphicsSupported = PlatformImpl.checkForClass("javafx.stage.Stage");
                }
                return isGraphicsSupported;
            }
            case CONTROLS: {
                if (isControlsSupported == null) {
                    isControlsSupported = PlatformImpl.checkForClass("javafx.scene.control.Control");
                }
                return isControlsSupported;
            }
            case MEDIA: {
                if (isMediaSupported == null && (isMediaSupported = PlatformImpl.checkForClass("javafx.scene.media.MediaView")).booleanValue() && PlatformUtil.isEmbedded()) {
                    Void void_ = AccessController.doPrivileged(() -> {
                        String string = System.getProperty("com.sun.javafx.experimental.embedded.media", "false");
                        isMediaSupported = Boolean.valueOf(string);
                        return null;
                    });
                }
                return isMediaSupported;
            }
            case WEB: {
                if (isWebSupported == null && (isWebSupported = PlatformImpl.checkForClass("javafx.scene.web.WebView")).booleanValue() && PlatformUtil.isEmbedded()) {
                    Void void_ = AccessController.doPrivileged(() -> {
                        String string = System.getProperty("com.sun.javafx.experimental.embedded.web", "false");
                        isWebSupported = Boolean.valueOf(string);
                        return null;
                    });
                }
                return isWebSupported;
            }
            case SWT: {
                if (isSWTSupported == null) {
                    isSWTSupported = PlatformImpl.checkForClass("javafx.embed.swt.FXCanvas");
                }
                return isSWTSupported;
            }
            case SWING: {
                if (isSwingSupported == null) {
                    isSwingSupported = PlatformImpl.checkForClass("javax.swing.JComponent") != false && PlatformImpl.checkForClass("javafx.embed.swing.JFXPanel") != false;
                }
                return isSwingSupported;
            }
            case FXML: {
                if (isFXMLSupported == null) {
                    isFXMLSupported = PlatformImpl.checkForClass("javafx.fxml.FXMLLoader") != false && PlatformImpl.checkForClass("javax.xml.stream.XMLInputFactory") != false;
                }
                return isFXMLSupported;
            }
            case TWO_LEVEL_FOCUS: {
                if (hasTwoLevelFocus == null) {
                    return Toolkit.getToolkit().isSupported(conditionalFeature);
                }
                return hasTwoLevelFocus;
            }
            case VIRTUAL_KEYBOARD: {
                if (hasVirtualKeyboard == null) {
                    return Toolkit.getToolkit().isSupported(conditionalFeature);
                }
                return hasVirtualKeyboard;
            }
            case INPUT_TOUCH: {
                if (hasTouch == null) {
                    return Toolkit.getToolkit().isSupported(conditionalFeature);
                }
                return hasTouch;
            }
            case INPUT_MULTITOUCH: {
                if (hasMultiTouch == null) {
                    return Toolkit.getToolkit().isSupported(conditionalFeature);
                }
                return hasMultiTouch;
            }
            case INPUT_POINTER: {
                if (hasPointer == null) {
                    return Toolkit.getToolkit().isSupported(conditionalFeature);
                }
                return hasPointer;
            }
        }
        return Toolkit.getToolkit().isSupported(conditionalFeature);
    }

    static {
        pendingRunnables = new AtomicInteger(0);
        numWindows = new AtomicInteger(0);
        firstWindowShown = false;
        lastWindowClosed = false;
        reallyIdle = new AtomicBoolean(false);
        finishListeners = new CopyOnWriteArraySet<FinishListener>();
        runLaterLock = new Object();
        isThreadMerged = false;
        applicationType = "";
        accessibilityActive = new SimpleBooleanProperty();
        allNestedLoopsExitedLatch = new CountDownLatch(1);
        verbose = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.verbose"));
        DEBUG = AccessController.doPrivileged(() -> Boolean.getBoolean("com.sun.javafx.application.debug"));
        FXCANVAS_PERMISSION = new FXPermission("accessFXCanvasInternals");
        platformExitLatch = new CountDownLatch(1);
        isModena = false;
        isCaspian = false;
    }

    public static interface FinishListener {
        public void idle(boolean var1);

        public void exitCalled();
    }

    public static final class HighContrastScheme
    extends Enum<HighContrastScheme> {
        public static final /* enum */ HighContrastScheme HIGH_CONTRAST_BLACK = new HighContrastScheme("high.contrast.black.theme");
        public static final /* enum */ HighContrastScheme HIGH_CONTRAST_WHITE = new HighContrastScheme("high.contrast.white.theme");
        public static final /* enum */ HighContrastScheme HIGH_CONTRAST_1 = new HighContrastScheme("high.contrast.1.theme");
        public static final /* enum */ HighContrastScheme HIGH_CONTRAST_2 = new HighContrastScheme("high.contrast.2.theme");
        private final String themeKey;
        private static final /* synthetic */ HighContrastScheme[] $VALUES;

        public static HighContrastScheme[] values() {
            return (HighContrastScheme[])$VALUES.clone();
        }

        public static HighContrastScheme valueOf(String string) {
            return Enum.valueOf(HighContrastScheme.class, string);
        }

        private HighContrastScheme(String string2) {
            this.themeKey = string2;
        }

        public String getThemeKey() {
            return this.themeKey;
        }

        public static String fromThemeName(Function<String, String> function, String string) {
            if (function == null || string == null) {
                return null;
            }
            for (HighContrastScheme highContrastScheme : HighContrastScheme.values()) {
                if (!string.equalsIgnoreCase(function.apply(highContrastScheme.getThemeKey()))) continue;
                return highContrastScheme.toString();
            }
            return null;
        }

        private static /* synthetic */ HighContrastScheme[] $values() {
            return new HighContrastScheme[]{HIGH_CONTRAST_BLACK, HIGH_CONTRAST_WHITE, HIGH_CONTRAST_1, HIGH_CONTRAST_2};
        }

        static {
            $VALUES = HighContrastScheme.$values();
        }
    }
}

