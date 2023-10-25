/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Application
 *  javafx.application.Preloader
 *  javafx.application.Preloader$ErrorNotification
 *  javafx.application.Preloader$PreloaderNotification
 *  javafx.application.Preloader$ProgressNotification
 *  javafx.application.Preloader$StateChangeNotification
 *  javafx.application.Preloader$StateChangeNotification$Type
 *  javafx.stage.Stage
 */
package com.sun.javafx.application;

import com.sun.javafx.application.ModuleAccess;
import com.sun.javafx.application.ParametersImpl;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.stage.StageHelper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.stage.Stage;

public class LauncherImpl {
    public static final String LAUNCH_MODE_CLASS = "LM_CLASS";
    public static final String LAUNCH_MODE_JAR = "LM_JAR";
    public static final String LAUNCH_MODE_MODULE = "LM_MODULE";
    private static final boolean trace = false;
    private static final boolean verbose = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.verbose"));
    private static final String MF_MAIN_CLASS = "Main-Class";
    private static final String MF_JAVAFX_MAIN = "JavaFX-Application-Class";
    private static final String MF_JAVAFX_PRELOADER = "JavaFX-Preloader-Class";
    private static final String MF_JAVAFX_CLASS_PATH = "JavaFX-Class-Path";
    private static final String MF_JAVAFX_ARGUMENT_PREFIX = "JavaFX-Argument-";
    private static final String MF_JAVAFX_PARAMETER_NAME_PREFIX = "JavaFX-Parameter-Name-";
    private static final String MF_JAVAFX_PARAMETER_VALUE_PREFIX = "JavaFX-Parameter-Value-";
    private static final boolean simulateSlowProgress = false;
    private static AtomicBoolean launchCalled = new AtomicBoolean(false);
    private static final AtomicBoolean toolkitStarted = new AtomicBoolean(false);
    private static volatile RuntimeException launchException = null;
    private static Preloader currentPreloader = null;
    private static Class<? extends Preloader> savedPreloaderClass = null;
    private static ClassLoader savedMainCcl = null;
    private static volatile boolean error = false;
    private static volatile Throwable pConstructorError = null;
    private static volatile Throwable pInitError = null;
    private static volatile Throwable pStartError = null;
    private static volatile Throwable pStopError = null;
    private static volatile Throwable constructorError = null;
    private static volatile Throwable initError = null;
    private static volatile Throwable startError = null;
    private static volatile Throwable stopError = null;

    public static void launchApplication(Class<? extends Application> class_, String[] arrstring) {
        String string;
        Class<Object> class_2 = savedPreloaderClass;
        if (class_2 == null && (string = AccessController.doPrivileged(() -> System.getProperty("javafx.preloader"))) != null) {
            try {
                class_2 = Class.forName(string, false, class_.getClassLoader());
            }
            catch (Exception exception) {
                System.err.printf("Could not load preloader class '" + string + "', continuing without preloader.", new Object[0]);
                exception.printStackTrace();
            }
        }
        LauncherImpl.launchApplication(class_, class_2, arrstring);
    }

    public static void launchApplication(Class<? extends Application> class_, Class<? extends Preloader> class_2, String[] arrstring) {
        if (com.sun.glass.ui.Application.isEventThread()) {
            throw new IllegalStateException("Application launch must not be called on the JavaFX Application Thread");
        }
        if (launchCalled.getAndSet(true)) {
            throw new IllegalStateException("Application launch must not be called more than once");
        }
        if (!Application.class.isAssignableFrom(class_)) {
            throw new IllegalArgumentException("Error: " + class_.getName() + " is not a subclass of javafx.application.Application");
        }
        if (class_2 != null && !Preloader.class.isAssignableFrom(class_2)) {
            throw new IllegalArgumentException("Error: " + class_2.getName() + " is not a subclass of javafx.application.Preloader");
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                LauncherImpl.launchApplication1(class_, class_2, arrstring);
            }
            catch (RuntimeException runtimeException) {
                launchException = runtimeException;
            }
            catch (Exception exception) {
                launchException = new RuntimeException("Application launch exception", exception);
            }
            catch (Error error) {
                launchException = new RuntimeException("Application launch error", error);
            }
            finally {
                countDownLatch.countDown();
            }
        });
        thread.setName("JavaFX-Launcher");
        thread.start();
        try {
            countDownLatch.await();
        }
        catch (InterruptedException interruptedException) {
            throw new RuntimeException("Unexpected exception: ", interruptedException);
        }
        if (launchException != null) {
            throw launchException;
        }
    }

    public static void launchApplication(String string, String string2, String[] arrstring) {
        Object object;
        Object object2;
        if (verbose) {
            System.err.println("JavaFX launchApplication method: launchMode=" + string2);
        }
        String string3 = null;
        String string4 = null;
        String[] arrstring2 = arrstring;
        ClassLoader classLoader = null;
        ModuleAccess moduleAccess = null;
        if (string2.equals(LAUNCH_MODE_JAR)) {
            object2 = LauncherImpl.getJarAttributes(string);
            if (object2 == null) {
                LauncherImpl.abort(null, "Can't get manifest attributes from jar", new Object[0]);
            }
            if ((object = ((Attributes)object2).getValue(MF_JAVAFX_CLASS_PATH)) != null) {
                if (((String)object).trim().length() == 0) {
                    object = null;
                } else {
                    if (verbose) {
                        System.err.println("WARNING: Application jar uses deprecated JavaFX-Class-Path attribute. Please use Class-Path instead.");
                    }
                    classLoader = LauncherImpl.setupJavaFXClassLoader(new File(string), (String)object);
                }
            }
            if (arrstring.length == 0) {
                arrstring2 = LauncherImpl.getAppArguments((Attributes)object2);
            }
            if ((string3 = ((Attributes)object2).getValue(MF_JAVAFX_MAIN)) == null && (string3 = ((Attributes)object2).getValue(MF_MAIN_CLASS)) == null) {
                LauncherImpl.abort(null, "JavaFX jar manifest requires a valid JavaFX-Appliation-Class or Main-Class entry", new Object[0]);
            }
            string3 = string3.trim();
            string4 = ((Attributes)object2).getValue(MF_JAVAFX_PRELOADER);
            if (string4 != null) {
                string4 = string4.trim();
            }
        } else if (string2.equals(LAUNCH_MODE_CLASS)) {
            string3 = string;
        } else if (string2.equals(LAUNCH_MODE_MODULE)) {
            int n = string.indexOf(47);
            if (n == -1) {
                object = string;
                string3 = null;
            } else {
                object = string.substring(0, n);
                string3 = string.substring(n + 1);
            }
            moduleAccess = ModuleAccess.load((String)object);
            if (string3 == null) {
                Optional<String> optional = moduleAccess.getDescriptor().mainClass();
                if (!optional.isPresent()) {
                    LauncherImpl.abort(null, "Module %1$s does not have a MainClass attribute, use -m <module>/<main-class>", object);
                }
                string3 = optional.get();
            }
        } else {
            LauncherImpl.abort(new IllegalArgumentException("The launchMode argument must be one of LM_CLASS, LM_JAR or LM_MODULE"), "Invalid launch mode: %1$s", string2);
        }
        if (string4 == null) {
            string4 = System.getProperty("javafx.preloader");
        }
        if (string3 == null) {
            LauncherImpl.abort(null, "No main JavaFX class to launch", new Object[0]);
        }
        if (classLoader != null) {
            try {
                object2 = classLoader.loadClass(LauncherImpl.class.getName());
                object = ((Class)object2).getMethod("launchApplicationWithArgs", ModuleAccess.class, String.class, String.class, new String[0].getClass());
                Thread.currentThread().setContextClassLoader(classLoader);
                ((Method)object).invoke(null, null, string3, string4, arrstring2);
            }
            catch (Exception exception) {
                LauncherImpl.abort(exception, "Exception while launching application", new Object[0]);
            }
        } else {
            LauncherImpl.launchApplicationWithArgs(moduleAccess, string3, string4, arrstring2);
        }
    }

    private static Class<?> loadClass(ModuleAccess moduleAccess, String string) {
        Class<?> class_ = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (moduleAccess != null) {
            class_ = moduleAccess.classForName(string);
        } else {
            try {
                class_ = Class.forName(string, true, classLoader);
            }
            catch (ClassNotFoundException | NoClassDefFoundError throwable) {
                // empty catch block
            }
        }
        if (class_ == null && System.getProperty("os.name", "").contains("OS X") && Normalizer.isNormalized(string, Normalizer.Form.NFD)) {
            String string2 = Normalizer.normalize(string, Normalizer.Form.NFC);
            if (moduleAccess != null) {
                class_ = moduleAccess.classForName(string2);
            } else {
                try {
                    class_ = Class.forName(string2, true, classLoader);
                }
                catch (ClassNotFoundException | NoClassDefFoundError throwable) {
                    // empty catch block
                }
            }
        }
        return class_;
    }

    public static void launchApplicationWithArgs(ModuleAccess moduleAccess, String string, String string2, String[] arrstring) {
        try {
            LauncherImpl.startToolkit();
        }
        catch (InterruptedException interruptedException) {
            LauncherImpl.abort(interruptedException, "Toolkit initialization error", string);
        }
        Class class_ = null;
        Class class_2 = null;
        AtomicReference atomicReference = new AtomicReference();
        AtomicReference atomicReference2 = new AtomicReference();
        PlatformImpl.runAndWait(() -> {
            Class<?> class_ = LauncherImpl.loadClass(moduleAccess, string);
            if (class_ == null) {
                if (moduleAccess != null) {
                    LauncherImpl.abort(null, "Missing JavaFX application class %1$s in module %2$s", string, moduleAccess.getName());
                } else {
                    LauncherImpl.abort(null, "Missing JavaFX application class %1$s", string);
                }
            }
            atomicReference.set(class_);
            if (string2 != null) {
                class_ = LauncherImpl.loadClass(null, string2);
                if (class_ == null) {
                    LauncherImpl.abort(null, "Missing JavaFX preloader class %1$s", string2);
                }
                if (!Preloader.class.isAssignableFrom(class_)) {
                    LauncherImpl.abort(null, "JavaFX preloader class %1$s does not extend javafx.application.Preloader", class_.getName());
                }
                atomicReference2.set(class_.asSubclass(Preloader.class));
            }
        });
        class_ = (Class)atomicReference2.get();
        class_2 = (Class)atomicReference.get();
        savedPreloaderClass = class_;
        ReflectiveOperationException reflectiveOperationException = null;
        try {
            Method method = class_2.getMethod("main", new String[0].getClass());
            if (verbose) {
                System.err.println("Calling main(String[]) method");
            }
            savedMainCcl = Thread.currentThread().getContextClassLoader();
            method.invoke(null, new Object[]{arrstring});
            return;
        }
        catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException2) {
            reflectiveOperationException = reflectiveOperationException2;
            savedPreloaderClass = null;
            if (verbose) {
                System.err.println("WARNING: Cannot access application main method: " + reflectiveOperationException2);
            }
        }
        catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
            LauncherImpl.abort(null, "Exception running application %1$s", class_2.getName());
            return;
        }
        if (!Application.class.isAssignableFrom(class_2)) {
            LauncherImpl.abort(reflectiveOperationException, "JavaFX application class %1$s does not extend javafx.application.Application", class_2.getName());
        }
        Class<Application> class_3 = class_2.asSubclass(Application.class);
        if (verbose) {
            System.err.println("Launching application directly");
        }
        LauncherImpl.launchApplication(class_3, class_, arrstring);
    }

    private static URL fileToURL(File file) throws IOException {
        return file.getCanonicalFile().toURI().toURL();
    }

    private static ClassLoader setupJavaFXClassLoader(File file, String string) {
        try {
            Object object;
            File file2 = file.getParentFile();
            ArrayList<URL> arrayList = new ArrayList<URL>();
            String string2 = string;
            if (string2 != null) {
                while (string2.length() > 0) {
                    String string3;
                    int n = string2.indexOf(" ");
                    if (n < 0) {
                        string3 = string2;
                        Object object2 = object = file2 == null ? new File(string3) : new File(file2, string3);
                        if (((File)object).exists()) {
                            arrayList.add(LauncherImpl.fileToURL((File)object));
                            break;
                        }
                        if (!verbose) break;
                        System.err.println("Class Path entry \"" + string3 + "\" does not exist, ignoring");
                        break;
                    }
                    if (n > 0) {
                        string3 = string2.substring(0, n);
                        Object object3 = object = file2 == null ? new File(string3) : new File(file2, string3);
                        if (((File)object).exists()) {
                            arrayList.add(LauncherImpl.fileToURL((File)object));
                        } else if (verbose) {
                            System.err.println("Class Path entry \"" + string3 + "\" does not exist, ignoring");
                        }
                    }
                    string2 = string2.substring(n + 1);
                }
            }
            if (!arrayList.isEmpty()) {
                ArrayList<URL> arrayList2 = new ArrayList<URL>();
                string2 = System.getProperty("java.class.path");
                if (string2 != null) {
                    while (string2.length() > 0) {
                        int n = string2.indexOf(File.pathSeparatorChar);
                        if (n < 0) {
                            object = string2;
                            arrayList2.add(LauncherImpl.fileToURL(new File((String)object)));
                            break;
                        }
                        if (n > 0) {
                            object = string2.substring(0, n);
                            arrayList2.add(LauncherImpl.fileToURL(new File((String)object)));
                        }
                        string2 = string2.substring(n + 1);
                    }
                }
                arrayList2.addAll(arrayList);
                URL[] arruRL = arrayList2.toArray(new URL[0]);
                if (verbose) {
                    System.err.println("===== URL list");
                    for (int i = 0; i < arruRL.length; ++i) {
                        System.err.println("" + arruRL[i]);
                    }
                    System.err.println("=====");
                }
                return new URLClassLoader(arruRL, ClassLoader.getPlatformClassLoader());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    private static String decodeBase64(String string) throws IOException {
        return new String(Base64.getDecoder().decode(string));
    }

    private static String[] getAppArguments(Attributes attributes) {
        LinkedList<Object> linkedList = new LinkedList<Object>();
        try {
            int n = 1;
            String string = MF_JAVAFX_ARGUMENT_PREFIX;
            while (attributes.getValue(string + n) != null) {
                linkedList.add(LauncherImpl.decodeBase64(attributes.getValue(string + n)));
                ++n;
            }
            String string2 = MF_JAVAFX_PARAMETER_NAME_PREFIX;
            String string3 = MF_JAVAFX_PARAMETER_VALUE_PREFIX;
            n = 1;
            while (attributes.getValue(string2 + n) != null) {
                String string4 = LauncherImpl.decodeBase64(attributes.getValue(string2 + n));
                String string5 = null;
                if (attributes.getValue(string3 + n) != null) {
                    string5 = LauncherImpl.decodeBase64(attributes.getValue(string3 + n));
                }
                linkedList.add("--" + string4 + "=" + (string5 != null ? string5 : ""));
                ++n;
            }
        }
        catch (IOException iOException) {
            if (verbose) {
                System.err.println("Failed to extract application parameters");
            }
            iOException.printStackTrace();
        }
        return linkedList.toArray(new String[0]);
    }

    private static void abort(Throwable throwable, String string, Object ... arrobject) {
        String string2 = String.format(string, arrobject);
        if (string2 != null) {
            System.err.println(string2);
        }
        System.exit(1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Attributes getJarAttributes(String string) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(string);
            Manifest manifest = jarFile.getManifest();
            if (manifest == null) {
                LauncherImpl.abort(null, "No manifest in jar file %1$s", string);
            }
            Attributes attributes = manifest.getMainAttributes();
            return attributes;
        }
        catch (IOException iOException) {
            LauncherImpl.abort(iOException, "Error launching jar file %1%s", string);
        }
        finally {
            try {
                jarFile.close();
            }
            catch (IOException iOException) {}
        }
        return null;
    }

    private static void startToolkit() throws InterruptedException {
        if (toolkitStarted.getAndSet(true)) {
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        PlatformImpl.startup(() -> countDownLatch.countDown());
        countDownLatch.await();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void launchApplication1(Class<? extends Application> class_, Class<? extends Preloader> class_2, String[] arrstring) throws Exception {
        Object object;
        LauncherImpl.startToolkit();
        if (savedMainCcl != null && (object = Thread.currentThread().getContextClassLoader()) != null && object != savedMainCcl) {
            PlatformImpl.runLater(() -> LauncherImpl.lambda$launchApplication1$5((ClassLoader)object));
        }
        object = new AtomicBoolean(false);
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicBoolean atomicBoolean2 = new AtomicBoolean(false);
        AtomicBoolean atomicBoolean3 = new AtomicBoolean(false);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        PlatformImpl.FinishListener finishListener = new PlatformImpl.FinishListener(){
            final /* synthetic */ AtomicBoolean val$pStartCalled;
            final /* synthetic */ CountDownLatch val$pShutdownLatch;
            final /* synthetic */ AtomicBoolean val$exitCalled;
            {
                this.val$pStartCalled = atomicBoolean2;
                this.val$pShutdownLatch = countDownLatch2;
                this.val$exitCalled = atomicBoolean3;
            }

            @Override
            public void idle(boolean bl) {
                if (!bl) {
                    return;
                }
                if (atomicBoolean.get()) {
                    countDownLatch.countDown();
                } else if (this.val$pStartCalled.get()) {
                    this.val$pShutdownLatch.countDown();
                }
            }

            @Override
            public void exitCalled() {
                this.val$exitCalled.set(true);
                countDownLatch.countDown();
            }
        };
        PlatformImpl.addListener(finishListener);
        try {
            AtomicReference atomicReference = new AtomicReference();
            if (class_2 != null) {
                PlatformImpl.runAndWait(() -> {
                    try {
                        Constructor constructor = class_2.getConstructor(new Class[0]);
                        atomicReference.set((Preloader)constructor.newInstance(new Object[0]));
                        ParametersImpl.registerParameters((Application)atomicReference.get(), new ParametersImpl(arrstring));
                    }
                    catch (Throwable throwable) {
                        System.err.println("Exception in Preloader constructor");
                        pConstructorError = throwable;
                        error = true;
                    }
                });
            }
            if ((currentPreloader = (Preloader)atomicReference.get()) != null && !error && !atomicBoolean2.get()) {
                try {
                    currentPreloader.init();
                }
                catch (Throwable throwable) {
                    System.err.println("Exception in Preloader init method");
                    pInitError = throwable;
                    error = true;
                }
            }
            if (currentPreloader != null && !error && !atomicBoolean2.get()) {
                PlatformImpl.runAndWait(() -> LauncherImpl.lambda$launchApplication1$7((AtomicBoolean)object));
                if (!error && !atomicBoolean2.get()) {
                    LauncherImpl.notifyProgress(currentPreloader, 0.0);
                }
            }
            AtomicReference atomicReference2 = new AtomicReference();
            if (!error && !atomicBoolean2.get()) {
                if (currentPreloader != null) {
                    LauncherImpl.notifyProgress(currentPreloader, 1.0);
                    LauncherImpl.notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_LOAD, null);
                }
                PlatformImpl.runAndWait(() -> {
                    try {
                        Constructor constructor = class_.getConstructor(new Class[0]);
                        atomicReference2.set((Application)constructor.newInstance(new Object[0]));
                        ParametersImpl.registerParameters((Application)atomicReference2.get(), new ParametersImpl(arrstring));
                        PlatformImpl.setApplicationName(class_);
                    }
                    catch (Throwable throwable) {
                        System.err.println("Exception in Application constructor");
                        constructorError = throwable;
                        error = true;
                    }
                });
            }
            Application application = (Application)atomicReference2.get();
            if (!error && !atomicBoolean2.get()) {
                if (currentPreloader != null) {
                    LauncherImpl.notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_INIT, application);
                }
                try {
                    application.init();
                }
                catch (Throwable throwable) {
                    System.err.println("Exception in Application init method");
                    initError = throwable;
                    error = true;
                }
            }
            if (!error && !atomicBoolean2.get()) {
                if (currentPreloader != null) {
                    LauncherImpl.notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_START, application);
                }
                PlatformImpl.runAndWait(() -> {
                    try {
                        atomicBoolean.set(true);
                        Stage stage = new Stage();
                        StageHelper.setPrimary(stage, true);
                        application.start(stage);
                    }
                    catch (Throwable throwable) {
                        System.err.println("Exception in Application start method");
                        startError = throwable;
                        error = true;
                    }
                });
            }
            if (!error) {
                countDownLatch.await();
            }
            if (atomicBoolean.get()) {
                PlatformImpl.runAndWait(() -> {
                    try {
                        application.stop();
                    }
                    catch (Throwable throwable) {
                        System.err.println("Exception in Application stop method");
                        stopError = throwable;
                        error = true;
                    }
                });
            }
            if (error) {
                String string;
                if (pConstructorError != null) {
                    throw new RuntimeException("Unable to construct Preloader instance: " + class_, pConstructorError);
                }
                if (pInitError != null) {
                    throw new RuntimeException("Exception in Preloader init method", pInitError);
                }
                if (pStartError != null) {
                    throw new RuntimeException("Exception in Preloader start method", pStartError);
                }
                if (pStopError != null) {
                    throw new RuntimeException("Exception in Preloader stop method", pStopError);
                }
                if (constructorError != null) {
                    String string2 = "Unable to construct Application instance: " + class_;
                    if (!LauncherImpl.notifyError(string2, constructorError)) {
                        throw new RuntimeException(string2, constructorError);
                    }
                } else if (initError != null) {
                    String string3 = "Exception in Application init method";
                    if (!LauncherImpl.notifyError(string3, initError)) {
                        throw new RuntimeException(string3, initError);
                    }
                } else if (startError != null) {
                    String string4 = "Exception in Application start method";
                    if (!LauncherImpl.notifyError(string4, startError)) {
                        throw new RuntimeException(string4, startError);
                    }
                } else if (stopError != null && !LauncherImpl.notifyError(string = "Exception in Application stop method", stopError)) {
                    throw new RuntimeException(string, stopError);
                }
            }
        }
        finally {
            PlatformImpl.removeListener(finishListener);
            PlatformImpl.tkExit();
        }
    }

    private static void notifyStateChange(Preloader preloader, Preloader.StateChangeNotification.Type type, Application application) {
        PlatformImpl.runAndWait(() -> preloader.handleStateChangeNotification(new Preloader.StateChangeNotification(type, application)));
    }

    private static void notifyProgress(Preloader preloader, double d) {
        PlatformImpl.runAndWait(() -> preloader.handleProgressNotification(new Preloader.ProgressNotification(d)));
    }

    private static boolean notifyError(String string, Throwable throwable) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        PlatformImpl.runAndWait(() -> {
            if (currentPreloader != null) {
                try {
                    Preloader.ErrorNotification errorNotification = new Preloader.ErrorNotification(null, string, throwable);
                    boolean bl = currentPreloader.handleErrorNotification(errorNotification);
                    atomicBoolean.set(bl);
                }
                catch (Throwable throwable2) {
                    throwable2.printStackTrace();
                }
            }
        });
        return atomicBoolean.get();
    }

    private static void notifyCurrentPreloader(Preloader.PreloaderNotification preloaderNotification) {
        PlatformImpl.runAndWait(() -> {
            if (currentPreloader != null) {
                currentPreloader.handleApplicationNotification(preloaderNotification);
            }
        });
    }

    public static void notifyPreloader(Application application, Preloader.PreloaderNotification preloaderNotification) {
        if (launchCalled.get()) {
            LauncherImpl.notifyCurrentPreloader(preloaderNotification);
            return;
        }
    }

    private LauncherImpl() {
        throw new InternalError();
    }

    private static /* synthetic */ void lambda$launchApplication1$7(AtomicBoolean atomicBoolean) {
        try {
            atomicBoolean.set(true);
            Stage stage = new Stage();
            StageHelper.setPrimary(stage, true);
            currentPreloader.start(stage);
        }
        catch (Throwable throwable) {
            System.err.println("Exception in Preloader start method");
            pStartError = throwable;
            error = true;
        }
    }

    private static /* synthetic */ void lambda$launchApplication1$5(ClassLoader classLoader) {
        Thread.currentThread().setContextClassLoader(classLoader);
    }
}

