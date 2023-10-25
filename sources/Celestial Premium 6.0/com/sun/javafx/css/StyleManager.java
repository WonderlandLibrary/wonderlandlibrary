/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PlatformLogger
 *  com.sun.javafx.logging.PlatformLogger$Level
 *  javafx.application.Application
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.css.CssParser
 *  javafx.css.CssParser$ParseError
 *  javafx.css.FontFace
 *  javafx.css.PseudoClass
 *  javafx.css.Rule
 *  javafx.css.Selector
 *  javafx.css.StyleClass
 *  javafx.css.StyleConverter
 *  javafx.css.StyleOrigin
 *  javafx.css.Styleable
 *  javafx.css.Stylesheet
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.SubScene
 *  javafx.scene.image.Image
 *  javafx.scene.layout.Region
 *  javafx.scene.text.Font
 *  javafx.stage.Window
 */
package com.sun.javafx.css;

import com.sun.javafx.css.FontFaceImpl;
import com.sun.javafx.css.SelectorPartitioning;
import com.sun.javafx.css.StyleCache;
import com.sun.javafx.css.StyleClassSet;
import com.sun.javafx.css.StyleMap;
import com.sun.javafx.logging.PlatformLogger;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.ParentHelper;
import com.sun.javafx.util.DataURI;
import com.sun.javafx.util.Logging;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssParser;
import javafx.css.FontFace;
import javafx.css.PseudoClass;
import javafx.css.Rule;
import javafx.css.Selector;
import javafx.css.StyleClass;
import javafx.css.StyleConverter;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.Stylesheet;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Window;

public final class StyleManager {
    private static final Object styleLock = new Object();
    private static PlatformLogger LOGGER;
    public static final Map<Parent, CacheContainer> cacheContainerMap;
    public final List<StylesheetContainer> userAgentStylesheetContainers = new ArrayList<StylesheetContainer>();
    public final List<StylesheetContainer> platformUserAgentStylesheetContainers = new ArrayList<StylesheetContainer>();
    public boolean hasDefaultUserAgentStylesheet = false;
    public final Map<String, StylesheetContainer> stylesheetContainerMap = new HashMap<String, StylesheetContainer>();
    private final ImageCache imageCache = new ImageCache();
    private static final String skinPrefix = "com/sun/javafx/scene/control/skin/";
    private static final String skinUtilsClassName = "com.sun.javafx.scene.control.skin.Utils";
    private Key key = null;
    private final WeakHashMap<Region, String> weakRegionUserAgentStylesheetMap = new WeakHashMap();
    private static ObservableList<CssParser.ParseError> errors;
    private static List<String> cacheMapKey;

    private static PlatformLogger getLogger() {
        if (LOGGER == null) {
            LOGGER = Logging.getCSSLogger();
        }
        return LOGGER;
    }

    public static StyleManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private StyleManager() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    CacheContainer getCacheContainer(Styleable styleable, SubScene subScene) {
        Object object;
        Object object2;
        if (styleable == null && subScene == null) {
            return null;
        }
        Parent parent = null;
        if (subScene != null) {
            parent = subScene.getRoot();
        } else if (styleable instanceof Node) {
            object2 = (Node)styleable;
            object = object2.getScene();
            if (object != null) {
                parent = object.getRoot();
            }
        } else if (styleable instanceof Window && (object2 = ((Window)styleable).getScene()) != null) {
            parent = object2.getRoot();
        }
        if (parent == null) {
            return null;
        }
        object2 = styleLock;
        synchronized (object2) {
            object = cacheContainerMap.get((Object)parent);
            if (object == null) {
                object = new CacheContainer();
                cacheContainerMap.put(parent, (CacheContainer)object);
            }
            return object;
        }
    }

    public StyleCache getSharedCache(Styleable styleable, SubScene subScene, StyleCache.Key key) {
        CacheContainer cacheContainer = this.getCacheContainer(styleable, subScene);
        if (cacheContainer == null) {
            return null;
        }
        Map<StyleCache.Key, StyleCache> map = cacheContainer.getStyleCache();
        if (map == null) {
            return null;
        }
        StyleCache styleCache = map.get(key);
        if (styleCache == null) {
            styleCache = new StyleCache();
            map.put(new StyleCache.Key(key), styleCache);
        }
        return styleCache;
    }

    public StyleMap getStyleMap(Styleable styleable, SubScene subScene, int n) {
        if (n == -1) {
            return StyleMap.EMPTY_MAP;
        }
        CacheContainer cacheContainer = this.getCacheContainer(styleable, subScene);
        if (cacheContainer == null) {
            return StyleMap.EMPTY_MAP;
        }
        return cacheContainer.getStyleMap(n);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void forget(Scene scene) {
        if (scene == null) {
            return;
        }
        this.forget(scene.getRoot());
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            String string = null;
            if (scene.getUserAgentStylesheet() != null && !(string = scene.getUserAgentStylesheet().trim()).isEmpty()) {
                for (int i = this.userAgentStylesheetContainers.size() - 1; 0 <= i; --i) {
                    object2 = this.userAgentStylesheetContainers.get(i);
                    if (!string.equals(((StylesheetContainer)object2).fname)) continue;
                    ((StylesheetContainer)object2).parentUsers.remove(scene.getRoot());
                    if (object2.parentUsers.list.size() != 0) continue;
                    this.userAgentStylesheetContainers.remove(i);
                }
            }
            Set<Map.Entry<String, StylesheetContainer>> set = this.stylesheetContainerMap.entrySet();
            object2 = set.iterator();
            while (object2.hasNext()) {
                Map.Entry<String, StylesheetContainer> entry = object2.next();
                StylesheetContainer stylesheetContainer = entry.getValue();
                Iterator iterator = stylesheetContainer.parentUsers.list.iterator();
                while (iterator.hasNext()) {
                    Reference reference = iterator.next();
                    Parent parent = (Parent)reference.get();
                    if (parent != null && parent.getScene() != scene && parent.getScene() != null) continue;
                    reference.clear();
                    iterator.remove();
                }
                if (!stylesheetContainer.parentUsers.list.isEmpty()) continue;
                object2.remove();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stylesheetsChanged(Scene scene, ListChangeListener.Change<String> change) {
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            Set<Map.Entry<Parent, CacheContainer>> set = cacheContainerMap.entrySet();
            for (Map.Entry<Parent, CacheContainer> object3 : set) {
                object2 = object3.getKey();
                CacheContainer cacheContainer = object3.getValue();
                if (object2.getScene() != scene) continue;
                cacheContainer.clearCache();
            }
            change.reset();
            while (change.next()) {
                if (!change.wasRemoved()) continue;
                for (String string : change.getRemoved()) {
                    this.stylesheetRemoved(scene, string);
                    object2 = this.stylesheetContainerMap.get(string);
                    if (object2 == null) continue;
                    ((StylesheetContainer)object2).invalidateChecksum();
                }
            }
        }
    }

    private void stylesheetRemoved(Scene scene, String string) {
        this.stylesheetRemoved(scene.getRoot(), string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void forget(Parent parent) {
        if (parent == null) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            ObservableList observableList;
            CacheContainer cacheContainer = cacheContainerMap.remove((Object)parent);
            if (cacheContainer != null) {
                cacheContainer.clearCache();
            }
            if ((observableList = parent.getStylesheets()) != null && !observableList.isEmpty()) {
                for (Object object2 : observableList) {
                    this.stylesheetRemoved(parent, (String)object2);
                }
            }
            Iterator<Map.Entry<String, StylesheetContainer>> iterator = this.stylesheetContainerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Object object2;
                object2 = iterator.next();
                StylesheetContainer stylesheetContainer = (StylesheetContainer)object2.getValue();
                stylesheetContainer.parentUsers.remove(parent);
                if (!stylesheetContainer.parentUsers.list.isEmpty()) continue;
                iterator.remove();
                if (stylesheetContainer.selectorPartitioning != null) {
                    stylesheetContainer.selectorPartitioning.reset();
                }
                String string = stylesheetContainer.fname;
                this.imageCache.cleanUpImageCache(string);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stylesheetsChanged(Parent parent, ListChangeListener.Change<String> change) {
        Object object = styleLock;
        synchronized (object) {
            change.reset();
            while (change.next()) {
                if (!change.wasRemoved()) continue;
                for (String string : change.getRemoved()) {
                    this.stylesheetRemoved(parent, string);
                    StylesheetContainer stylesheetContainer = this.stylesheetContainerMap.get(string);
                    if (stylesheetContainer == null) continue;
                    stylesheetContainer.invalidateChecksum();
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void stylesheetRemoved(Parent parent, String string) {
        Object object = styleLock;
        synchronized (object) {
            StylesheetContainer stylesheetContainer = this.stylesheetContainerMap.get(string);
            if (stylesheetContainer == null) {
                return;
            }
            stylesheetContainer.parentUsers.remove(parent);
            if (stylesheetContainer.parentUsers.list.isEmpty()) {
                this.removeStylesheetContainer(stylesheetContainer);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void forget(SubScene subScene) {
        if (subScene == null) {
            return;
        }
        Parent parent = subScene.getRoot();
        if (parent == null) {
            return;
        }
        this.forget(parent);
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            Object object3;
            String string = null;
            if (subScene.getUserAgentStylesheet() != null && !(string = subScene.getUserAgentStylesheet().trim()).isEmpty()) {
                object3 = this.userAgentStylesheetContainers.iterator();
                while (object3.hasNext()) {
                    object2 = (StylesheetContainer)object3.next();
                    if (!string.equals(((StylesheetContainer)object2).fname)) continue;
                    ((StylesheetContainer)object2).parentUsers.remove(subScene.getRoot());
                    if (object2.parentUsers.list.size() != 0) continue;
                    object3.remove();
                }
            }
            object3 = new ArrayList<StylesheetContainer>(this.stylesheetContainerMap.values());
            object2 = object3.iterator();
            while (object2.hasNext()) {
                StylesheetContainer stylesheetContainer = (StylesheetContainer)object2.next();
                Iterator iterator = stylesheetContainer.parentUsers.list.iterator();
                block5: while (iterator.hasNext()) {
                    Reference reference = iterator.next();
                    Parent parent2 = (Parent)reference.get();
                    if (parent2 == null) continue;
                    for (Parent parent3 = parent2; parent3 != null; parent3 = parent3.getParent()) {
                        if (parent != parent3.getParent()) continue;
                        reference.clear();
                        iterator.remove();
                        this.forget(parent2);
                        continue block5;
                    }
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void removeStylesheetContainer(StylesheetContainer stylesheetContainer) {
        if (stylesheetContainer == null) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            Parent parent;
            Object object2;
            String string = stylesheetContainer.fname;
            this.stylesheetContainerMap.remove(string);
            if (stylesheetContainer.selectorPartitioning != null) {
                stylesheetContainer.selectorPartitioning.reset();
            }
            for (Map.Entry<Parent, CacheContainer> entry : cacheContainerMap.entrySet()) {
                Object object3;
                object2 = entry.getValue();
                if (object2 == null || ((CacheContainer)object2).cacheMap == null || ((CacheContainer)object2).cacheMap.isEmpty()) continue;
                parent = new ArrayList();
                for (Map.Entry<List<String>, Map<Key, Cache>> entry2 : ((CacheContainer)object2).cacheMap.entrySet()) {
                    object3 = entry2.getKey();
                    if (!(object3 != null ? object3.contains(string) : string == null)) continue;
                    parent.add(object3);
                }
                if (parent.isEmpty()) continue;
                for (List list : parent) {
                    object3 = ((CacheContainer)object2).cacheMap.remove(list);
                    if (object3 == null) continue;
                    object3.clear();
                }
            }
            this.imageCache.cleanUpImageCache(string);
            List list = stylesheetContainer.parentUsers.list;
            for (int i = list.size() - 1; 0 <= i; --i) {
                object2 = (Reference)list.remove(i);
                parent = (Parent)((Reference)object2).get();
                ((Reference)object2).clear();
                if (parent == null || parent.getScene() == null) continue;
                NodeHelper.reapplyCSS((Node)parent);
            }
        }
    }

    public Image getCachedImage(String string) {
        return this.imageCache.getCachedImage(string);
    }

    private static URL getURL(String string) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        try {
            URI uRI = new URI(string.trim());
            if (!uRI.isAbsolute()) {
                if (string.startsWith(skinPrefix) && (string.endsWith(".css") || string.endsWith(".bss"))) {
                    try {
                        ClassLoader classLoader = StyleManager.class.getClassLoader();
                        Class<?> class_ = Class.forName(skinUtilsClassName, true, classLoader);
                        Method method = class_.getMethod("getResource", String.class);
                        return (URL)method.invoke(null, string.substring(skinPrefix.length()));
                    }
                    catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                        reflectiveOperationException.printStackTrace();
                        return null;
                    }
                }
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                String string2 = uRI.getPath();
                URL uRL = null;
                uRL = string2.startsWith("/") ? classLoader.getResource(string2.substring(1)) : classLoader.getResource(string2);
                return uRL;
            }
            return uRI.toURL();
        }
        catch (MalformedURLException malformedURLException) {
            return null;
        }
        catch (URISyntaxException uRISyntaxException) {
            return null;
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    static byte[] calculateCheckSum(String string) {
        block15: {
            if (string == null || string.isEmpty()) {
                return new byte[0];
            }
            try {
                URL uRL = StyleManager.getURL(string);
                if (uRL == null || !"file".equals(uRL.getProtocol())) break block15;
                try (InputStream inputStream = uRL.openStream();){
                    byte[] arrby;
                    try (DigestInputStream digestInputStream = new DigestInputStream(inputStream, MessageDigest.getInstance("MD5"));){
                        digestInputStream.getMessageDigest().reset();
                        byte[] arrby2 = new byte[4096];
                        while (digestInputStream.read(arrby2) != -1) {
                        }
                        arrby = digestInputStream.getMessageDigest().digest();
                    }
                    return arrby;
                }
            }
            catch (IOException | IllegalArgumentException | SecurityException | NoSuchAlgorithmException exception) {
                // empty catch block
            }
        }
        return new byte[0];
    }

    public static Stylesheet loadStylesheet(String string) {
        try {
            return StyleManager.loadStylesheetUnPrivileged(string);
        }
        catch (AccessControlException accessControlException) {
            System.err.println("WARNING: security exception trying to load: " + string);
            if (string.length() < 7 && string.indexOf("!/") < string.length() - 7) {
                return null;
            }
            try {
                String string2;
                String string3;
                URI uRI;
                String string4;
                URI uRI2 = new URI(string);
                if ("jar".equals(uRI2.getScheme()) && (string4 = (uRI = AccessController.doPrivileged(() -> StyleManager.class.getProtectionDomain().getCodeSource().getLocation().toURI())).getSchemeSpecificPart()).equals(string3 = (string2 = uRI2.getSchemeSpecificPart()).substring(string2.indexOf(47), string2.indexOf("!/")))) {
                    String string5 = string.substring(string.indexOf("!/") + 2);
                    if (string.endsWith(".css") || string.endsWith(".bss")) {
                        JarEntry jarEntry;
                        FilePermission filePermission = new FilePermission(string4, "read");
                        PermissionCollection permissionCollection = filePermission.newPermissionCollection();
                        permissionCollection.add(filePermission);
                        AccessControlContext accessControlContext = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissionCollection)});
                        JarFile jarFile = null;
                        try {
                            jarFile = AccessController.doPrivileged(() -> new JarFile(string4), accessControlContext);
                        }
                        catch (PrivilegedActionException privilegedActionException) {
                            return null;
                        }
                        if (jarFile != null && (jarEntry = jarFile.getJarEntry(string5)) != null) {
                            return AccessController.doPrivileged(() -> StyleManager.loadStylesheetUnPrivileged(string), accessControlContext);
                        }
                    }
                }
                return null;
            }
            catch (URISyntaxException uRISyntaxException) {
                return null;
            }
            catch (PrivilegedActionException privilegedActionException) {
                return null;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Stylesheet loadStylesheetUnPrivileged(String string) {
        Object object = styleLock;
        synchronized (object) {
            block41: {
                Boolean bl = AccessController.doPrivileged(() -> {
                    String string2 = System.getProperty("binary.css");
                    return !string.endsWith(".bss") && string2 != null ? !Boolean.valueOf(string2).booleanValue() : Boolean.FALSE;
                });
                try {
                    Charset charset;
                    Object object2;
                    String string2 = bl != false ? ".css" : ".bss";
                    URL uRL = null;
                    Stylesheet stylesheet = null;
                    if (!string.endsWith(".css") && !string.endsWith(".bss")) {
                        uRL = StyleManager.getURL(string);
                    } else {
                        object2 = string.substring(0, string.length() - 4);
                        uRL = StyleManager.getURL((String)object2 + string2);
                        if (uRL == null && (bl = Boolean.valueOf(bl == false)).booleanValue()) {
                            uRL = StyleManager.getURL((String)object2 + ".css");
                        }
                        if (uRL != null && !bl.booleanValue()) {
                            try {
                                stylesheet = Stylesheet.loadBinary((URL)uRL);
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                            if (stylesheet == null) {
                                uRL = StyleManager.getURL(string);
                            }
                        }
                    }
                    if (stylesheet == null) {
                        object2 = null;
                        if (uRL != null) {
                            stylesheet = new CssParser().parse(uRL);
                        } else {
                            object2 = DataURI.tryParse(string);
                        }
                        if (object2 != null) {
                            boolean bl2;
                            boolean bl3 = "text".equalsIgnoreCase(object2.getMimeType()) && ("css".equalsIgnoreCase(object2.getMimeSubtype()) || "plain".equalsIgnoreCase(object2.getMimeSubtype()));
                            boolean bl4 = bl2 = "application".equalsIgnoreCase(object2.getMimeType()) && "octet-stream".equalsIgnoreCase(object2.getMimeSubtype());
                            if (bl3) {
                                String object3 = object2.getParameters().get("charset");
                                try {
                                    charset = object3 != null ? Charset.forName(object3) : Charset.defaultCharset();
                                }
                                catch (IllegalCharsetNameException | UnsupportedCharsetException illegalArgumentException) {
                                    String string3 = String.format("Unsupported charset \"%s\" in stylesheet URI \"%s\"", object3, object2);
                                    if (errors != null) {
                                        errors.add((Object)new CssParser.ParseError(string3));
                                    }
                                    if (StyleManager.getLogger().isLoggable(PlatformLogger.Level.WARNING)) {
                                        StyleManager.getLogger().warning(string3);
                                    }
                                    return null;
                                }
                                String string4 = new String(object2.getData(), charset);
                                stylesheet = new CssParser().parse(string4);
                            } else if (bl2) {
                                try (ByteArrayInputStream string5 = new ByteArrayInputStream(object2.getData());){
                                    stylesheet = Stylesheet.loadBinary((InputStream)string5);
                                }
                            } else {
                                String string5 = String.format("Unexpected MIME type \"%s/%s\" in stylesheet URI \"%s\"", object2.getMimeType(), object2.getMimeSubtype(), object2);
                                if (errors != null) {
                                    errors.add((Object)new CssParser.ParseError(string5));
                                }
                                if (StyleManager.getLogger().isLoggable(PlatformLogger.Level.WARNING)) {
                                    StyleManager.getLogger().warning(string5);
                                }
                                return null;
                            }
                        }
                    }
                    if (stylesheet == null) {
                        if (errors != null) {
                            object2 = new CssParser.ParseError("Resource \"" + string + "\" not found.");
                            errors.add(object2);
                        }
                        if (StyleManager.getLogger().isLoggable(PlatformLogger.Level.WARNING)) {
                            StyleManager.getLogger().warning(String.format("Resource \"%s\" not found.", string));
                        }
                    }
                    if (stylesheet != null) {
                        block15: for (FontFace fontFace : stylesheet.getFontFaces()) {
                            if (!(fontFace instanceof FontFaceImpl)) continue;
                            for (FontFaceImpl.FontFaceSrc fontFaceSrc : ((FontFaceImpl)fontFace).getSources()) {
                                if (fontFaceSrc.getType() != FontFaceImpl.FontFaceSrcType.URL) continue;
                                charset = Font.loadFont((String)fontFaceSrc.getSrc(), (double)10.0);
                                if (charset != null) continue block15;
                                StyleManager.getLogger().info("Could not load @font-face font [" + fontFaceSrc.getSrc() + "]");
                                continue block15;
                            }
                        }
                    }
                    return stylesheet;
                }
                catch (FileNotFoundException fileNotFoundException) {
                    if (errors != null) {
                        CssParser.ParseError parseError = new CssParser.ParseError("Stylesheet \"" + string + "\" not found.");
                        errors.add((Object)parseError);
                    }
                    if (StyleManager.getLogger().isLoggable(PlatformLogger.Level.INFO)) {
                        StyleManager.getLogger().info("Could not find stylesheet: " + string);
                    }
                }
                catch (IOException iOException) {
                    String string6;
                    DataURI dataURI = DataURI.tryParse(string);
                    String string7 = string6 = dataURI != null ? dataURI.toString() : string;
                    if (errors != null) {
                        errors.add((Object)new CssParser.ParseError("Could not load stylesheet: " + string6));
                    }
                    if (!StyleManager.getLogger().isLoggable(PlatformLogger.Level.INFO)) break block41;
                    StyleManager.getLogger().info("Could not load stylesheet: " + string6);
                }
            }
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setUserAgentStylesheets(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            String string;
            String string2;
            int n;
            int n2;
            boolean bl;
            if (list.size() == this.platformUserAgentStylesheetContainers.size()) {
                bl = true;
                n2 = list.size();
                for (n = 0; n < n2 && bl; ++n) {
                    string2 = list.get(n);
                    String string3 = string = string2 != null ? string2.trim() : null;
                    if (string == null || string.isEmpty()) break;
                    StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(n);
                    bl = string.equals(stylesheetContainer.fname);
                    if (!bl) continue;
                    String string4 = stylesheetContainer.stylesheet.getUrl();
                    byte[] arrby = StyleManager.calculateCheckSum(string4);
                    bl = Arrays.equals(arrby, stylesheetContainer.checksum);
                }
                if (bl) {
                    return;
                }
            }
            bl = false;
            n2 = list.size();
            for (n = 0; n < n2; ++n) {
                string2 = list.get(n);
                String string5 = string = string2 != null ? string2.trim() : null;
                if (string == null || string.isEmpty()) continue;
                if (!bl) {
                    this.platformUserAgentStylesheetContainers.clear();
                    bl = true;
                }
                if (n == 0) {
                    this._setDefaultUserAgentStylesheet(string);
                    continue;
                }
                this._addUserAgentStylesheet(string);
            }
            if (bl) {
                this.userAgentStylesheetsChanged();
            }
        }
    }

    public void addUserAgentStylesheet(String string) {
        this.addUserAgentStylesheet(null, string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addUserAgentStylesheet(Scene scene, String string) {
        String string2;
        String string3 = string2 = string != null ? string.trim() : null;
        if (string2 == null || string2.isEmpty()) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            if (this._addUserAgentStylesheet(string2)) {
                this.userAgentStylesheetsChanged();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean _addUserAgentStylesheet(String string) {
        Object object = styleLock;
        synchronized (object) {
            int n = this.platformUserAgentStylesheetContainers.size();
            for (int i = 0; i < n; ++i) {
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i);
                if (!string.equals(stylesheetContainer.fname)) continue;
                return false;
            }
            Stylesheet stylesheet = StyleManager.loadStylesheet(string);
            if (stylesheet == null) {
                return false;
            }
            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            this.platformUserAgentStylesheetContainers.add(new StylesheetContainer(string, stylesheet));
            return true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addUserAgentStylesheet(Scene scene, Stylesheet stylesheet) {
        if (stylesheet == null) {
            throw new IllegalArgumentException("null arg ua_stylesheet");
        }
        String string = stylesheet.getUrl();
        String string2 = string != null ? string.trim() : "";
        Object object = styleLock;
        synchronized (object) {
            int n = this.platformUserAgentStylesheetContainers.size();
            for (int i = 0; i < n; ++i) {
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i);
                if (!string2.equals(stylesheetContainer.fname)) continue;
                return;
            }
            this.platformUserAgentStylesheetContainers.add(new StylesheetContainer(string2, stylesheet));
            if (stylesheet != null) {
                stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            }
            this.userAgentStylesheetsChanged();
        }
    }

    public void setDefaultUserAgentStylesheet(String string) {
        this.setDefaultUserAgentStylesheet(null, string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDefaultUserAgentStylesheet(Scene scene, String string) {
        String string2;
        String string3 = string2 = string != null ? string.trim() : null;
        if (string2 == null || string2.isEmpty()) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            if (this._setDefaultUserAgentStylesheet(string2)) {
                this.userAgentStylesheetsChanged();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean _setDefaultUserAgentStylesheet(String string) {
        Object object = styleLock;
        synchronized (object) {
            int n = this.platformUserAgentStylesheetContainers.size();
            for (int i = 0; i < n; ++i) {
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i);
                if (!string.equals(stylesheetContainer.fname)) continue;
                if (i > 0) {
                    this.platformUserAgentStylesheetContainers.remove(i);
                    if (this.hasDefaultUserAgentStylesheet) {
                        this.platformUserAgentStylesheetContainers.set(0, stylesheetContainer);
                    } else {
                        this.platformUserAgentStylesheetContainers.add(0, stylesheetContainer);
                    }
                }
                return i > 0;
            }
            Stylesheet stylesheet = StyleManager.loadStylesheet(string);
            if (stylesheet == null) {
                return false;
            }
            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            StylesheetContainer stylesheetContainer = new StylesheetContainer(string, stylesheet);
            if (this.platformUserAgentStylesheetContainers.size() == 0) {
                this.platformUserAgentStylesheetContainers.add(stylesheetContainer);
            } else if (this.hasDefaultUserAgentStylesheet) {
                this.platformUserAgentStylesheetContainers.set(0, stylesheetContainer);
            } else {
                this.platformUserAgentStylesheetContainers.add(0, stylesheetContainer);
            }
            this.hasDefaultUserAgentStylesheet = true;
            return true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeUserAgentStylesheet(String string) {
        String string2;
        String string3 = string2 = string != null ? string.trim() : null;
        if (string2 == null || string2.isEmpty()) {
            return;
        }
        Object object = styleLock;
        synchronized (object) {
            boolean bl = false;
            for (int i = this.platformUserAgentStylesheetContainers.size() - 1; i >= 0; --i) {
                if (string2.equals(Application.getUserAgentStylesheet())) continue;
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i);
                if (!string2.equals(stylesheetContainer.fname)) continue;
                this.platformUserAgentStylesheetContainers.remove(i);
                bl = true;
            }
            if (bl) {
                this.userAgentStylesheetsChanged();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDefaultUserAgentStylesheet(Stylesheet stylesheet) {
        if (stylesheet == null) {
            return;
        }
        String string = stylesheet.getUrl();
        String string2 = string != null ? string.trim() : "";
        Object object = styleLock;
        synchronized (object) {
            int n = this.platformUserAgentStylesheetContainers.size();
            for (int i = 0; i < n; ++i) {
                StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i);
                if (!string2.equals(stylesheetContainer.fname)) continue;
                if (i > 0) {
                    this.platformUserAgentStylesheetContainers.remove(i);
                    if (this.hasDefaultUserAgentStylesheet) {
                        this.platformUserAgentStylesheetContainers.set(0, stylesheetContainer);
                    } else {
                        this.platformUserAgentStylesheetContainers.add(0, stylesheetContainer);
                    }
                }
                return;
            }
            StylesheetContainer stylesheetContainer = new StylesheetContainer(string2, stylesheet);
            if (this.platformUserAgentStylesheetContainers.size() == 0) {
                this.platformUserAgentStylesheetContainers.add(stylesheetContainer);
            } else if (this.hasDefaultUserAgentStylesheet) {
                this.platformUserAgentStylesheetContainers.set(0, stylesheetContainer);
            } else {
                this.platformUserAgentStylesheetContainers.add(0, stylesheetContainer);
            }
            this.hasDefaultUserAgentStylesheet = true;
            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
            this.userAgentStylesheetsChanged();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void userAgentStylesheetsChanged() {
        ArrayList<Parent> arrayList = new ArrayList<Parent>();
        Iterator iterator = styleLock;
        synchronized (iterator) {
            for (CacheContainer cacheContainer : cacheContainerMap.values()) {
                cacheContainer.clearCache();
            }
            StyleConverter.clearCache();
            for (Parent parent : cacheContainerMap.keySet()) {
                if (parent == null) continue;
                arrayList.add(parent);
            }
        }
        for (Parent parent : arrayList) {
            NodeHelper.reapplyCSS((Node)parent);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<StylesheetContainer> processStylesheets(List<String> list, Parent parent) {
        Object object = styleLock;
        synchronized (object) {
            ArrayList<StylesheetContainer> arrayList = new ArrayList<StylesheetContainer>();
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                Object object2;
                String string = list.get(i);
                StylesheetContainer stylesheetContainer = null;
                if (this.stylesheetContainerMap.containsKey(string)) {
                    stylesheetContainer = this.stylesheetContainerMap.get(string);
                    if (!arrayList.contains(stylesheetContainer)) {
                        if (stylesheetContainer.checksumInvalid) {
                            object2 = StyleManager.calculateCheckSum(string);
                            if (!Arrays.equals((byte[])object2, stylesheetContainer.checksum)) {
                                this.removeStylesheetContainer(stylesheetContainer);
                                Stylesheet stylesheet = StyleManager.loadStylesheet(string);
                                stylesheetContainer = new StylesheetContainer(string, stylesheet, (byte[])object2);
                                this.stylesheetContainerMap.put(string, stylesheetContainer);
                            } else {
                                stylesheetContainer.checksumInvalid = false;
                            }
                        }
                        arrayList.add(stylesheetContainer);
                    }
                    stylesheetContainer.parentUsers.add(parent);
                    continue;
                }
                object2 = StyleManager.loadStylesheet(string);
                stylesheetContainer = new StylesheetContainer(string, (Stylesheet)object2);
                stylesheetContainer.parentUsers.add(parent);
                this.stylesheetContainerMap.put(string, stylesheetContainer);
                arrayList.add(stylesheetContainer);
            }
            return arrayList;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<StylesheetContainer> gatherParentStylesheets(Parent parent) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<String> list = ParentHelper.getAllParentStylesheets(parent);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        Object object = styleLock;
        synchronized (object) {
            return this.processStylesheets(list, parent);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private List<StylesheetContainer> gatherSceneStylesheets(Scene scene) {
        if (scene == null) {
            return Collections.emptyList();
        }
        ObservableList observableList = scene.getStylesheets();
        if (observableList == null || observableList.isEmpty()) {
            return Collections.emptyList();
        }
        Object object = styleLock;
        synchronized (object) {
            return this.processStylesheets((List<String>)observableList, scene.getRoot());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public StyleMap findMatchingStyles(Node node, SubScene subScene, Set<PseudoClass>[] arrset) {
        Scene scene = node.getScene();
        if (scene == null) {
            return StyleMap.EMPTY_MAP;
        }
        CacheContainer cacheContainer = this.getCacheContainer((Styleable)node, subScene);
        if (cacheContainer == null) {
            assert (false) : node.toString();
            return StyleMap.EMPTY_MAP;
        }
        Object object = styleLock;
        synchronized (object) {
            Object object2;
            boolean bl;
            Node node2;
            Parent parent = node instanceof Parent ? (Parent)node : node.getParent();
            List<StylesheetContainer> list = this.gatherParentStylesheets(parent);
            boolean bl2 = !list.isEmpty();
            List<StylesheetContainer> list2 = this.gatherSceneStylesheets(scene);
            boolean bl3 = !list2.isEmpty();
            String string = node.getStyle();
            boolean bl4 = string != null && !string.trim().isEmpty();
            String string2 = scene.getUserAgentStylesheet();
            boolean bl5 = string2 != null && !string2.trim().isEmpty();
            String string3 = subScene != null ? subScene.getUserAgentStylesheet() : null;
            boolean bl6 = string3 != null && !string3.trim().isEmpty();
            String string4 = null;
            for (node2 = node; !(node2 == null || node2 instanceof Region && (string4 = this.weakRegionUserAgentStylesheetMap.computeIfAbsent((Region)node2, Region::getUserAgentStylesheet)) != null); node2 = node2.getParent()) {
            }
            boolean bl7 = bl = string4 != null && !string4.trim().isEmpty();
            if (!(bl4 || bl2 || bl3 || bl5 || bl6 || bl || !this.platformUserAgentStylesheetContainers.isEmpty())) {
                return StyleMap.EMPTY_MAP;
            }
            String string5 = node.getTypeSelector();
            String string6 = node.getId();
            ObservableList observableList = node.getStyleClass();
            if (this.key == null) {
                this.key = new Key();
            }
            this.key.className = string5;
            this.key.id = string6;
            int n = observableList.size();
            for (int i = 0; i < n; ++i) {
                object2 = (String)observableList.get(i);
                if (object2 == null || ((String)object2).isEmpty()) continue;
                this.key.styleClasses.add((Object)StyleClassSet.getStyleClass((String)object2));
            }
            Map<Key, Cache> map = cacheContainer.getCacheMap(list, string4);
            Cache cache = map.get(this.key);
            if (cache != null) {
                this.key.styleClasses.clear();
            } else {
                int n2;
                Object object3;
                object2 = new ArrayList();
                if (bl6 || bl5) {
                    object3 = bl6 ? subScene.getUserAgentStylesheet().trim() : scene.getUserAgentStylesheet().trim();
                    StylesheetContainer stylesheetContainer = null;
                    int n3 = this.userAgentStylesheetContainers.size();
                    for (n2 = 0; n2 < n3; ++n2) {
                        stylesheetContainer = this.userAgentStylesheetContainers.get(n2);
                        if (((String)object3).equals(stylesheetContainer.fname)) break;
                        stylesheetContainer = null;
                    }
                    if (stylesheetContainer == null) {
                        Stylesheet stylesheet = StyleManager.loadStylesheet((String)object3);
                        if (stylesheet != null) {
                            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
                        }
                        stylesheetContainer = new StylesheetContainer((String)object3, stylesheet);
                        this.userAgentStylesheetContainers.add(stylesheetContainer);
                    }
                    if (stylesheetContainer.selectorPartitioning != null) {
                        Parent parent2 = bl6 ? subScene.getRoot() : scene.getRoot();
                        stylesheetContainer.parentUsers.add(parent2);
                        List<Selector> list3 = stylesheetContainer.selectorPartitioning.match(string6, string5, (Set<StyleClass>)((Object)this.key.styleClasses));
                        object2.addAll(list3);
                    }
                } else if (!this.platformUserAgentStylesheetContainers.isEmpty()) {
                    int n4 = this.platformUserAgentStylesheetContainers.size();
                    for (int i = 0; i < n4; ++i) {
                        StylesheetContainer stylesheetContainer = this.platformUserAgentStylesheetContainers.get(i);
                        if (stylesheetContainer == null || stylesheetContainer.selectorPartitioning == null) continue;
                        List<Selector> list4 = stylesheetContainer.selectorPartitioning.match(string6, string5, (Set<StyleClass>)((Object)this.key.styleClasses));
                        object2.addAll(list4);
                    }
                }
                if (bl) {
                    object3 = null;
                    n2 = this.userAgentStylesheetContainers.size();
                    for (int i = 0; i < n2; ++i) {
                        object3 = this.userAgentStylesheetContainers.get(i);
                        if (string4.equals(((StylesheetContainer)object3).fname)) break;
                        object3 = null;
                    }
                    if (object3 == null) {
                        Stylesheet stylesheet = StyleManager.loadStylesheet(string4);
                        if (stylesheet != null) {
                            stylesheet.setOrigin(StyleOrigin.USER_AGENT);
                        }
                        object3 = new StylesheetContainer(string4, stylesheet);
                        this.userAgentStylesheetContainers.add((StylesheetContainer)object3);
                    }
                    if (((StylesheetContainer)object3).selectorPartitioning != null) {
                        ((StylesheetContainer)object3).parentUsers.add((Parent)node2);
                        List<Selector> list5 = ((StylesheetContainer)object3).selectorPartitioning.match(string6, string5, (Set<StyleClass>)((Object)this.key.styleClasses));
                        object2.addAll(list5);
                    }
                }
                if (!list2.isEmpty()) {
                    int n5 = list2.size();
                    for (int i = 0; i < n5; ++i) {
                        StylesheetContainer stylesheetContainer = list2.get(i);
                        if (stylesheetContainer == null || stylesheetContainer.selectorPartitioning == null) continue;
                        List<Selector> list6 = stylesheetContainer.selectorPartitioning.match(string6, string5, (Set<StyleClass>)((Object)this.key.styleClasses));
                        object2.addAll(list6);
                    }
                }
                if (bl2) {
                    int n6 = list == null ? 0 : list.size();
                    for (int i = 0; i < n6; ++i) {
                        StylesheetContainer stylesheetContainer = list.get(i);
                        if (stylesheetContainer.selectorPartitioning == null) continue;
                        List<Selector> list7 = stylesheetContainer.selectorPartitioning.match(string6, string5, (Set<StyleClass>)((Object)this.key.styleClasses));
                        object2.addAll(list7);
                    }
                }
                cache = new Cache((List<Selector>)object2);
                map.put(this.key, cache);
                this.key = null;
            }
            object2 = cache.getStyleMap(cacheContainer, node, arrset, bl4);
            return object2;
        }
    }

    public static ObservableList<CssParser.ParseError> errorsProperty() {
        if (errors == null) {
            errors = FXCollections.observableArrayList();
        }
        return errors;
    }

    public static ObservableList<CssParser.ParseError> getErrors() {
        return errors;
    }

    static {
        cacheContainerMap = new WeakHashMap<Parent, CacheContainer>();
        errors = null;
    }

    private static class InstanceHolder {
        static final StyleManager INSTANCE = new StyleManager();

        private InstanceHolder() {
        }
    }

    private static final class ImageCache {
        private Map<String, SoftReference<Image>> imageCache = new HashMap<String, SoftReference<Image>>();

        private ImageCache() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        Image getCachedImage(String string) {
            Object object = styleLock;
            synchronized (object) {
                Image image;
                block11: {
                    image = null;
                    if (this.imageCache.containsKey(string)) {
                        image = this.imageCache.get(string).get();
                    }
                    if (image == null) {
                        try {
                            image = new Image(string);
                            if (image.isError()) {
                                PlatformLogger platformLogger = StyleManager.getLogger();
                                if (platformLogger != null && platformLogger.isLoggable(PlatformLogger.Level.WARNING)) {
                                    DataURI dataURI = DataURI.tryParse(string);
                                    if (dataURI != null) {
                                        platformLogger.warning("Error loading image: " + dataURI);
                                    } else {
                                        platformLogger.warning("Error loading image: " + string);
                                    }
                                }
                                image = null;
                            }
                            this.imageCache.put(string, new SoftReference<Image>(image));
                        }
                        catch (IllegalArgumentException | NullPointerException runtimeException) {
                            PlatformLogger platformLogger = StyleManager.getLogger();
                            if (platformLogger == null || !platformLogger.isLoggable(PlatformLogger.Level.WARNING)) break block11;
                            platformLogger.warning(runtimeException.getLocalizedMessage());
                        }
                    }
                }
                return image;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        void cleanUpImageCache(String string) {
            Object object = styleLock;
            synchronized (object) {
                if (string == null || this.imageCache.isEmpty()) {
                    return;
                }
                String string2 = string.trim();
                if (string2.isEmpty()) {
                    return;
                }
                int n = string2.lastIndexOf(47);
                String string3 = n > 0 ? string2.substring(0, n) : string2;
                int n2 = string3.length();
                String[] arrstring = new String[this.imageCache.size()];
                int n3 = 0;
                Set<Map.Entry<String, SoftReference<Image>>> set = this.imageCache.entrySet();
                for (Map.Entry<String, SoftReference<Image>> entry : set) {
                    String string4 = entry.getKey();
                    if (entry.getValue().get() == null) {
                        arrstring[n3++] = string4;
                        continue;
                    }
                    n = string4.lastIndexOf(47);
                    String string5 = n > 0 ? string4.substring(0, n) : string4;
                    int n4 = string5.length();
                    boolean bl = n4 > n2 ? string5.startsWith(string3) : string3.startsWith(string5);
                    if (!bl) continue;
                    arrstring[n3++] = string4;
                }
                for (int i = 0; i < n3; ++i) {
                    this.imageCache.remove(arrstring[i]);
                }
            }
        }
    }

    private static class Key {
        String className;
        String id;
        final StyleClassSet styleClasses = new StyleClassSet();

        private Key() {
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof Key) {
                Key key = (Key)object;
                if (this.className == null ? key.className != null : !this.className.equals(key.className)) {
                    return false;
                }
                if (this.id == null ? key.id != null : !this.id.equals(key.id)) {
                    return false;
                }
                return this.styleClasses.equals(key.styleClasses);
            }
            return true;
        }

        public int hashCode() {
            int n = 7;
            n = 29 * n + (this.className != null ? this.className.hashCode() : 0);
            n = 29 * n + (this.id != null ? this.id.hashCode() : 0);
            n = 29 * n + this.styleClasses.hashCode();
            return n;
        }
    }

    static class CacheContainer {
        private Map<StyleCache.Key, StyleCache> styleCache;
        private Map<List<String>, Map<Key, Cache>> cacheMap;
        private List<StyleMap> styleMapList;
        private Map<String, Selector> inlineStylesCache;
        private int styleMapId = 0;
        private int baseStyleMapId = 0;

        CacheContainer() {
        }

        private Map<StyleCache.Key, StyleCache> getStyleCache() {
            if (this.styleCache == null) {
                this.styleCache = new HashMap<StyleCache.Key, StyleCache>();
            }
            return this.styleCache;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private Map<Key, Cache> getCacheMap(List<StylesheetContainer> list, String string) {
            if (this.cacheMap == null) {
                this.cacheMap = new HashMap<List<String>, Map<Key, Cache>>();
            }
            Object object = styleLock;
            synchronized (object) {
                Map<Key, Cache> map;
                if ((list == null || list.isEmpty()) && (string == null || string.isEmpty())) {
                    Map<Key, Cache> map2 = this.cacheMap.get(null);
                    if (map2 == null) {
                        map2 = new HashMap<Key, Cache>();
                        this.cacheMap.put(null, map2);
                    }
                    return map2;
                }
                int n = list.size();
                if (cacheMapKey == null) {
                    cacheMapKey = new ArrayList<String>(n);
                }
                for (int i = 0; i < n; ++i) {
                    StylesheetContainer stylesheetContainer = list.get(i);
                    if (stylesheetContainer == null || stylesheetContainer.fname == null || stylesheetContainer.fname.isEmpty()) continue;
                    cacheMapKey.add(stylesheetContainer.fname);
                }
                if (string != null) {
                    cacheMapKey.add(string);
                }
                if ((map = this.cacheMap.get(cacheMapKey)) == null) {
                    map = new HashMap<Key, Cache>();
                    this.cacheMap.put(cacheMapKey, map);
                    cacheMapKey = null;
                } else {
                    cacheMapKey.clear();
                }
                return map;
            }
        }

        private List<StyleMap> getStyleMapList() {
            if (this.styleMapList == null) {
                this.styleMapList = new ArrayList<StyleMap>();
            }
            return this.styleMapList;
        }

        private int nextSmapId() {
            this.styleMapId = this.baseStyleMapId + this.getStyleMapList().size();
            return this.styleMapId;
        }

        private void addStyleMap(StyleMap styleMap) {
            this.getStyleMapList().add(styleMap);
        }

        public StyleMap getStyleMap(int n) {
            int n2 = n - this.baseStyleMapId;
            if (0 <= n2 && n2 < this.getStyleMapList().size()) {
                return this.getStyleMapList().get(n2);
            }
            return StyleMap.EMPTY_MAP;
        }

        private void clearCache() {
            if (this.cacheMap != null) {
                this.cacheMap.clear();
            }
            if (this.styleCache != null) {
                this.styleCache.clear();
            }
            if (this.styleMapList != null) {
                this.styleMapList.clear();
            }
            this.baseStyleMapId = this.styleMapId;
            if (this.baseStyleMapId > 0x6FFFFFF9) {
                this.styleMapId = 0;
                this.baseStyleMapId = 0;
            }
        }

        private Selector getInlineStyleSelector(String string) {
            Stylesheet stylesheet;
            if (string == null || string.trim().isEmpty()) {
                return null;
            }
            if (this.inlineStylesCache != null && this.inlineStylesCache.containsKey(string)) {
                return this.inlineStylesCache.get(string);
            }
            if (this.inlineStylesCache == null) {
                this.inlineStylesCache = new HashMap<String, Selector>();
            }
            if ((stylesheet = new CssParser().parse("*{" + string + "}")) != null) {
                Selector selector;
                stylesheet.setOrigin(StyleOrigin.INLINE);
                List list = stylesheet.getRules();
                Rule rule = list != null && !list.isEmpty() ? (Rule)list.get(0) : null;
                ObservableList observableList = rule != null ? rule.getSelectors() : null;
                Selector selector2 = selector = observableList != null && !observableList.isEmpty() ? (Selector)observableList.get(0) : null;
                if (selector != null) {
                    selector.setOrdinal(-1);
                    this.inlineStylesCache.put(string, selector);
                    return selector;
                }
            }
            this.inlineStylesCache.put(string, null);
            return null;
        }
    }

    static class StylesheetContainer {
        final String fname;
        final Stylesheet stylesheet;
        final SelectorPartitioning selectorPartitioning;
        final RefList<Parent> parentUsers;
        final int hash;
        final byte[] checksum;
        boolean checksumInvalid = false;

        StylesheetContainer(String string, Stylesheet stylesheet) {
            this(string, stylesheet, stylesheet != null ? StyleManager.calculateCheckSum(stylesheet.getUrl()) : new byte[0]);
        }

        StylesheetContainer(String string, Stylesheet stylesheet, byte[] arrby) {
            this.fname = string;
            this.hash = string != null ? string.hashCode() : 127;
            this.stylesheet = stylesheet;
            if (stylesheet != null) {
                this.selectorPartitioning = new SelectorPartitioning();
                List list = stylesheet.getRules();
                int n = list == null || list.isEmpty() ? 0 : list.size();
                for (int i = 0; i < n; ++i) {
                    Rule rule = (Rule)list.get(i);
                    ObservableList observableList = rule.getSelectors();
                    int n2 = observableList == null || observableList.isEmpty() ? 0 : observableList.size();
                    for (int j = 0; j < n2; ++j) {
                        Selector selector = (Selector)observableList.get(j);
                        this.selectorPartitioning.partition(selector);
                    }
                }
            } else {
                this.selectorPartitioning = null;
            }
            this.parentUsers = new RefList();
            this.checksum = arrby;
        }

        void invalidateChecksum() {
            this.checksumInvalid = this.checksum.length > 0;
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (this.getClass() != object.getClass()) {
                return false;
            }
            StylesheetContainer stylesheetContainer = (StylesheetContainer)object;
            return !(this.fname == null ? stylesheetContainer.fname != null : !this.fname.equals(stylesheetContainer.fname));
        }

        public String toString() {
            return this.fname;
        }
    }

    static class RefList<K> {
        final List<Reference<K>> list = new ArrayList<Reference<K>>();

        RefList() {
        }

        void add(K k) {
            for (int i = this.list.size() - 1; 0 <= i; --i) {
                Reference<K> reference = this.list.get(i);
                K k2 = reference.get();
                if (k2 == null) {
                    this.list.remove(i);
                    continue;
                }
                if (k2 != k) continue;
                return;
            }
            this.list.add(new WeakReference<K>(k));
        }

        void remove(K k) {
            for (int i = this.list.size() - 1; 0 <= i; --i) {
                Reference<K> reference = this.list.get(i);
                K k2 = reference.get();
                if (k2 == null) {
                    this.list.remove(i);
                    continue;
                }
                if (k2 != k) continue;
                this.list.remove(i);
                return;
            }
        }

        boolean contains(K k) {
            for (int i = this.list.size() - 1; 0 <= i; --i) {
                Reference<K> reference = this.list.get(i);
                K k2 = reference.get();
                if (k2 != k) continue;
                return true;
            }
            return false;
        }
    }

    private static class Cache {
        private final List<Selector> selectors;
        private final Map<Key, Integer> cache;

        Cache(List<Selector> list) {
            this.selectors = list;
            this.cache = new HashMap<Key, Integer>();
        }

        private StyleMap getStyleMap(CacheContainer cacheContainer, Node node, Set<PseudoClass>[] arrset, boolean bl) {
            int n;
            Selector selector;
            Object object;
            if ((this.selectors == null || this.selectors.isEmpty()) && !bl) {
                return StyleMap.EMPTY_MAP;
            }
            int n2 = this.selectors.size();
            long[] arrl = new long[n2 / 64 + 1];
            boolean bl2 = true;
            for (int i = 0; i < n2; ++i) {
                long l;
                object = this.selectors.get(i);
                if (!object.applies((Styleable)node, (Set[])arrset, 0)) continue;
                int n3 = i / 64;
                arrl[n3] = l = arrl[n3] | 1L << i;
                bl2 = false;
            }
            if (bl2 && !bl) {
                return StyleMap.EMPTY_MAP;
            }
            String string = node.getStyle();
            object = new Key(arrl, string);
            if (this.cache.containsKey(object)) {
                Integer n4 = this.cache.get(object);
                StyleMap styleMap = n4 != null ? cacheContainer.getStyleMap(n4) : StyleMap.EMPTY_MAP;
                return styleMap;
            }
            ArrayList<Selector> arrayList = new ArrayList<Selector>();
            if (bl && (selector = cacheContainer.getInlineStyleSelector(string)) != null) {
                arrayList.add(selector);
            }
            for (n = 0; n < arrl.length; ++n) {
                if (arrl[n] == 0L) continue;
                int n5 = n * 64;
                for (int i = 0; i < 64; ++i) {
                    long l = 1L << i;
                    if ((l & arrl[n]) != l) continue;
                    Selector selector2 = this.selectors.get(n5 + i);
                    arrayList.add(selector2);
                }
            }
            n = cacheContainer.nextSmapId();
            this.cache.put((Key)object, n);
            StyleMap styleMap = new StyleMap(n, arrayList);
            cacheContainer.addStyleMap(styleMap);
            return styleMap;
        }

        private static class Key {
            final long[] key;
            final String inlineStyle;

            Key(long[] arrl, String string) {
                this.key = arrl;
                this.inlineStyle = string != null && string.trim().isEmpty() ? null : string;
            }

            public String toString() {
                return Arrays.toString(this.key) + (String)(this.inlineStyle != null ? "* {" + this.inlineStyle + "}" : "");
            }

            public int hashCode() {
                int n = 3;
                n = 17 * n + Arrays.hashCode(this.key);
                if (this.inlineStyle != null) {
                    n = 17 * n + this.inlineStyle.hashCode();
                }
                return n;
            }

            public boolean equals(Object object) {
                if (object == null) {
                    return false;
                }
                if (this.getClass() != object.getClass()) {
                    return false;
                }
                Key key = (Key)object;
                if (this.inlineStyle == null ? key.inlineStyle != null : !this.inlineStyle.equals(key.inlineStyle)) {
                    return false;
                }
                return Arrays.equals(this.key, key.key);
            }
        }
    }
}

