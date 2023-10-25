/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;

public class PluginManager {
    private static final long NANOS_PER_SECOND = 1000000000L;
    private static ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> pluginTypeMap = new ConcurrentHashMap();
    private static final CopyOnWriteArrayList<String> PACKAGES = new CopyOnWriteArrayList();
    private static final String PATH = "org/apache/logging/log4j/core/config/plugins/";
    private static final String FILENAME = "Log4j2Plugins.dat";
    private static final String LOG4J_PACKAGES = "org.apache.logging.log4j.core";
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static String rootDir;
    private Map<String, PluginType<?>> plugins = new HashMap();
    private final String type;
    private final Class<?> clazz;

    public PluginManager(String type) {
        this.type = type;
        this.clazz = null;
    }

    public PluginManager(String type, Class<?> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 1) {
            System.err.println("A target directory must be specified");
            System.exit(-1);
        }
        rootDir = args[0].endsWith("/") || args[0].endsWith("\\") ? args[0] : args[0] + "/";
        PluginManager manager = new PluginManager("Core");
        String packages = args.length == 2 ? args[1] : null;
        manager.collectPlugins(false, packages);
        PluginManager.encode(pluginTypeMap);
    }

    public static void addPackage(String p) {
        if (PACKAGES.addIfAbsent(p)) {
            pluginTypeMap.clear();
        }
    }

    public PluginType<?> getPluginType(String name) {
        return this.plugins.get(name.toLowerCase());
    }

    public Map<String, PluginType<?>> getPlugins() {
        return this.plugins;
    }

    public void collectPlugins() {
        this.collectPlugins(true, null);
    }

    public void collectPlugins(boolean preLoad, String pkgs) {
        if (pluginTypeMap.containsKey(this.type)) {
            this.plugins = (Map)pluginTypeMap.get(this.type);
            preLoad = false;
        }
        long start = System.nanoTime();
        ResolverUtil resolver = new ResolverUtil();
        ClassLoader classLoader = Loader.getClassLoader();
        if (classLoader != null) {
            resolver.setClassLoader(classLoader);
        }
        if (preLoad) {
            ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> map = PluginManager.decode(classLoader);
            if (map != null) {
                pluginTypeMap = map;
                this.plugins = (Map)map.get(this.type);
            } else {
                LOGGER.warn("Plugin preloads not available from class loader {}", classLoader);
            }
        }
        if (this.plugins == null || this.plugins.size() == 0) {
            if (pkgs == null) {
                if (!PACKAGES.contains(LOG4J_PACKAGES)) {
                    PACKAGES.add(LOG4J_PACKAGES);
                }
            } else {
                String[] names;
                for (String name : names = pkgs.split(",")) {
                    PACKAGES.add(name);
                }
            }
        }
        PluginTest test = new PluginTest(this.clazz);
        for (String string : PACKAGES) {
            resolver.findInPackage(test, string);
        }
        for (Class class_ : resolver.getClasses()) {
            Plugin plugin = class_.getAnnotation(Plugin.class);
            String pluginCategory = plugin.category();
            if (!pluginTypeMap.containsKey(pluginCategory)) {
                pluginTypeMap.putIfAbsent(pluginCategory, new ConcurrentHashMap());
            }
            Map map = (Map)pluginTypeMap.get(pluginCategory);
            String type = plugin.elementType().equals("") ? plugin.name() : plugin.elementType();
            PluginType pluginType = new PluginType(class_, type, plugin.printObject(), plugin.deferChildren());
            map.put(plugin.name().toLowerCase(), pluginType);
            PluginAliases pluginAliases = class_.getAnnotation(PluginAliases.class);
            if (pluginAliases == null) continue;
            for (String alias : pluginAliases.value()) {
                type = plugin.elementType().equals("") ? alias : plugin.elementType();
                pluginType = new PluginType(class_, type, plugin.printObject(), plugin.deferChildren());
                map.put(alias.trim().toLowerCase(), pluginType);
            }
        }
        long elapsed = System.nanoTime() - start;
        this.plugins = (Map)pluginTypeMap.get(this.type);
        StringBuilder sb = new StringBuilder("Generated plugins");
        sb.append(" in ");
        DecimalFormat numFormat = new DecimalFormat("#0");
        long seconds = elapsed / 1000000000L;
        sb.append(numFormat.format(seconds)).append('.');
        numFormat = new DecimalFormat("000000000");
        sb.append(numFormat.format(elapsed %= 1000000000L)).append(" seconds");
        LOGGER.debug(sb.toString());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> decode(ClassLoader classLoader) {
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources("org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
        }
        catch (IOException ioe) {
            LOGGER.warn("Unable to preload plugins", (Throwable)ioe);
            return null;
        }
        ConcurrentHashMap map = new ConcurrentHashMap();
        while (resources.hasMoreElements()) {
            DataInputStream dis = null;
            try {
                URL url = resources.nextElement();
                LOGGER.debug("Found Plugin Map at {}", url.toExternalForm());
                InputStream is = url.openStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                dis = new DataInputStream(bis);
                int count = dis.readInt();
                for (int j = 0; j < count; ++j) {
                    String type = dis.readUTF();
                    int entries = dis.readInt();
                    ConcurrentHashMap types = (ConcurrentHashMap)map.get(type);
                    if (types == null) {
                        types = new ConcurrentHashMap(count);
                    }
                    for (int i = 0; i < entries; ++i) {
                        String key = dis.readUTF();
                        String className = dis.readUTF();
                        String name = dis.readUTF();
                        boolean printable = dis.readBoolean();
                        boolean defer = dis.readBoolean();
                        Class<?> clazz = Class.forName(className);
                        types.put(key, new PluginType(clazz, name, printable, defer));
                    }
                    map.putIfAbsent(type, types);
                }
                Closer.closeSilent(dis);
            }
            catch (Exception ex) {
                LOGGER.warn("Unable to preload plugins", (Throwable)ex);
                ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> concurrentMap = null;
                return concurrentMap;
            }
            finally {
                Closer.closeSilent(dis);
            }
        }
        return map.size() == 0 ? null : map;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void encode(ConcurrentMap<String, ConcurrentMap<String, PluginType<?>>> map) {
        String fileName = rootDir + PATH + FILENAME;
        DataOutputStream dos = null;
        try {
            File file = new File(rootDir + PATH);
            file.mkdirs();
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            dos.writeInt(map.size());
            for (Map.Entry outer : map.entrySet()) {
                dos.writeUTF((String)outer.getKey());
                dos.writeInt(((ConcurrentMap)outer.getValue()).size());
                for (Map.Entry entry : ((ConcurrentMap)outer.getValue()).entrySet()) {
                    dos.writeUTF((String)entry.getKey());
                    PluginType pt = (PluginType)entry.getValue();
                    dos.writeUTF(pt.getPluginClass().getName());
                    dos.writeUTF(pt.getElementName());
                    dos.writeBoolean(pt.isObjectPrintable());
                    dos.writeBoolean(pt.isDeferChildren());
                }
            }
            Closer.closeSilent(dos);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            Closer.closeSilent(dos);
        }
    }

    public static class PluginTest
    extends ResolverUtil.ClassTest {
        private final Class<?> isA;

        public PluginTest(Class<?> isA) {
            this.isA = isA;
        }

        @Override
        public boolean matches(Class<?> type) {
            return type != null && type.isAnnotationPresent(Plugin.class) && (this.isA == null || this.isA.isAssignableFrom(type));
        }

        public String toString() {
            StringBuilder msg = new StringBuilder("annotated with @" + Plugin.class.getSimpleName());
            if (this.isA != null) {
                msg.append(" is assignable to " + this.isA.getSimpleName());
            }
            return msg.toString();
        }
    }
}

