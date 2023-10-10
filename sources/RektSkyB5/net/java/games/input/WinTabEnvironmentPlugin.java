/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.File;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.DummyWindow;
import net.java.games.input.WinTabContext;
import net.java.games.input.WinTabDevice;
import net.java.games.util.plugins.Plugin;

public class WinTabEnvironmentPlugin
extends ControllerEnvironment
implements Plugin {
    private static boolean supported = false;
    private final Controller[] controllers;
    private final List<WinTabDevice> active_devices = new ArrayList<WinTabDevice>();
    private final WinTabContext winTabContext;

    static void loadLibrary(String lib_name) {
        AccessController.doPrivileged(() -> {
            try {
                String lib_path = System.getProperty("net.java.games.input.librarypath");
                if (lib_path != null) {
                    System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
                } else {
                    System.loadLibrary(lib_name);
                }
            }
            catch (UnsatisfiedLinkError e2) {
                e2.printStackTrace();
                supported = false;
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

    public WinTabEnvironmentPlugin() {
        if (this.isSupported()) {
            WinTabContext winTabContext = null;
            Controller[] controllers = new Controller[]{};
            try {
                DummyWindow window = new DummyWindow();
                winTabContext = new WinTabContext(window);
                try {
                    winTabContext.open();
                    controllers = winTabContext.getControllers();
                }
                catch (Exception e2) {
                    window.destroy();
                    throw e2;
                }
            }
            catch (Exception e3) {
                WinTabEnvironmentPlugin.log("Failed to enumerate devices: " + e3.getMessage());
                e3.printStackTrace();
            }
            this.controllers = controllers;
            this.winTabContext = winTabContext;
            AccessController.doPrivileged(() -> {
                Runtime.getRuntime().addShutdownHook(new ShutdownHook());
                return null;
            });
        } else {
            this.winTabContext = null;
            this.controllers = new Controller[0];
        }
    }

    @Override
    public boolean isSupported() {
        return supported;
    }

    @Override
    public Controller[] getControllers() {
        return this.controllers;
    }

    static {
        String osName = WinTabEnvironmentPlugin.getPrivilegedProperty("os.name", "").trim();
        if (osName.startsWith("Windows")) {
            supported = true;
            WinTabEnvironmentPlugin.loadLibrary("jinput-wintab");
        }
    }

    private final class ShutdownHook
    extends Thread {
        private ShutdownHook() {
        }

        @Override
        public final void run() {
            for (int i2 = 0; i2 < WinTabEnvironmentPlugin.this.active_devices.size(); ++i2) {
            }
            WinTabEnvironmentPlugin.this.winTabContext.close();
        }
    }
}

