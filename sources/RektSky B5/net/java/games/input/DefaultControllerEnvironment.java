/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.File;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.util.plugins.Plugins;

class DefaultControllerEnvironment
extends ControllerEnvironment {
    static String libPath;
    private static Logger log;
    private ArrayList<Controller> controllers;
    private Collection<String> loadedPluginNames = new ArrayList<String>();

    static void loadLibrary(String lib_name) {
        AccessController.doPrivileged(() -> {
            String lib_path = System.getProperty("net.java.games.input.librarypath");
            if (lib_path != null) {
                System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
            } else {
                System.loadLibrary(lib_name);
            }
            return null;
        });
    }

    static String getPrivilegedProperty(String property) {
        return AccessController.doPrivileged(() -> System.getProperty(property));
    }

    static String getPrivilegedProperty(String property, String default_value) {
        return AccessController.doPrivileged(() -> System.getProperty(property, default_value));
    }

    @Override
    public Controller[] getControllers() {
        if (this.controllers == null) {
            this.controllers = new ArrayList();
            AccessController.doPrivileged(() -> this.scanControllers());
            String pluginClasses = DefaultControllerEnvironment.getPrivilegedProperty("jinput.plugins", "") + " " + DefaultControllerEnvironment.getPrivilegedProperty("net.java.games.input.plugins", "");
            if (!DefaultControllerEnvironment.getPrivilegedProperty("jinput.useDefaultPlugin", "true").toLowerCase().trim().equals("false") && !DefaultControllerEnvironment.getPrivilegedProperty("net.java.games.input.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
                String osName = DefaultControllerEnvironment.getPrivilegedProperty("os.name", "").trim();
                if (osName.equals("Linux")) {
                    pluginClasses = pluginClasses + " net.java.games.input.LinuxEnvironmentPlugin";
                } else if (osName.equals("Mac OS X")) {
                    pluginClasses = pluginClasses + " net.java.games.input.OSXEnvironmentPlugin";
                } else if (osName.equals("Windows XP") || osName.equals("Windows Vista") || osName.equals("Windows 7") || osName.equals("Windows 8") || osName.equals("Windows 8.1") || osName.equals("Windows 10")) {
                    pluginClasses = pluginClasses + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
                } else if (osName.equals("Windows 98") || osName.equals("Windows 2000")) {
                    pluginClasses = pluginClasses + " net.java.games.input.DirectInputEnvironmentPlugin";
                } else if (osName.startsWith("Windows")) {
                    log.warning("Found unknown Windows version: " + osName);
                    log.warning("Attempting to use default windows plug-in.");
                    pluginClasses = pluginClasses + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
                } else {
                    log.warning("Trying to use default plugin, OS name " + osName + " not recognised");
                }
            }
            StringTokenizer pluginClassTok = new StringTokenizer(pluginClasses, " \t\n\r\f,;:");
            while (pluginClassTok.hasMoreTokens()) {
                String className = pluginClassTok.nextToken();
                try {
                    if (this.loadedPluginNames.contains(className)) continue;
                    log.fine("Loading: " + className);
                    Class<?> ceClass = Class.forName(className);
                    ControllerEnvironment ce = (ControllerEnvironment)ceClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    if (ce.isSupported()) {
                        this.addControllers(ce.getControllers());
                        this.loadedPluginNames.add(ce.getClass().getName());
                        continue;
                    }
                    DefaultControllerEnvironment.log(ceClass.getName() + " is not supported");
                }
                catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
        }
        Controller[] ret = new Controller[this.controllers.size()];
        Iterator<Controller> it = this.controllers.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            ret[i2] = it.next();
            ++i2;
        }
        return ret;
    }

    private Void scanControllers() {
        String pluginPathName = DefaultControllerEnvironment.getPrivilegedProperty("jinput.controllerPluginPath");
        if (pluginPathName == null) {
            pluginPathName = "controller";
        }
        this.scanControllersAt(DefaultControllerEnvironment.getPrivilegedProperty("java.home") + File.separator + "lib" + File.separator + pluginPathName);
        this.scanControllersAt(DefaultControllerEnvironment.getPrivilegedProperty("user.dir") + File.separator + pluginPathName);
        return null;
    }

    private void scanControllersAt(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        try {
            Plugins plugins = new Plugins(file);
            Class[] envClasses = plugins.getExtends(ControllerEnvironment.class);
            for (int i2 = 0; i2 < envClasses.length; ++i2) {
                try {
                    ControllerEnvironment.log("ControllerEnvironment " + envClasses[i2].getName() + " loaded by " + envClasses[i2].getClassLoader());
                    ControllerEnvironment ce = (ControllerEnvironment)envClasses[i2].getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    if (ce.isSupported()) {
                        this.addControllers(ce.getControllers());
                        this.loadedPluginNames.add(ce.getClass().getName());
                        continue;
                    }
                    DefaultControllerEnvironment.log(envClasses[i2].getName() + " is not supported");
                    continue;
                }
                catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    private void addControllers(Controller[] c2) {
        for (int i2 = 0; i2 < c2.length; ++i2) {
            this.controllers.add(c2[i2]);
        }
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    static {
        log = Logger.getLogger(DefaultControllerEnvironment.class.getName());
    }
}

