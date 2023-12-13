/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.RawDevice;
import net.java.games.input.RawDeviceInfo;
import net.java.games.input.RawInputEventQueue;
import net.java.games.input.SetupAPIDevice;
import net.java.games.util.plugins.Plugin;

public final class RawInputEnvironmentPlugin
extends ControllerEnvironment
implements Plugin {
    private static boolean supported = false;
    private final Controller[] controllers;

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

    public RawInputEnvironmentPlugin() {
        Controller[] controllers = new Controller[]{};
        if (this.isSupported()) {
            try {
                RawInputEventQueue queue = new RawInputEventQueue();
                controllers = this.enumControllers(queue);
            }
            catch (IOException e2) {
                RawInputEnvironmentPlugin.log("Failed to enumerate devices: " + e2.getMessage());
            }
        }
        this.controllers = controllers;
    }

    @Override
    public final Controller[] getControllers() {
        return this.controllers;
    }

    private static final SetupAPIDevice lookupSetupAPIDevice(String device_name, List<SetupAPIDevice> setupapi_devices) {
        device_name = device_name.replaceAll("#", "\\\\").toUpperCase();
        for (int i2 = 0; i2 < setupapi_devices.size(); ++i2) {
            SetupAPIDevice device = setupapi_devices.get(i2);
            if (!device_name.contains(device.getInstanceId().toUpperCase())) continue;
            return device;
        }
        return null;
    }

    private static final void createControllersFromDevices(RawInputEventQueue queue, List<Controller> controllers, List<RawDevice> devices, List<SetupAPIDevice> setupapi_devices) throws IOException {
        ArrayList<RawDevice> active_devices = new ArrayList<RawDevice>();
        for (int i2 = 0; i2 < devices.size(); ++i2) {
            RawDeviceInfo info;
            Controller controller;
            RawDevice device = devices.get(i2);
            SetupAPIDevice setupapi_device = RawInputEnvironmentPlugin.lookupSetupAPIDevice(device.getName(), setupapi_devices);
            if (setupapi_device == null || (controller = (info = device.getInfo()).createControllerFromDevice(device, setupapi_device)) == null) continue;
            controllers.add(controller);
            active_devices.add(device);
        }
        queue.start(active_devices);
    }

    private static final native void enumerateDevices(RawInputEventQueue var0, List<RawDevice> var1) throws IOException;

    private final Controller[] enumControllers(RawInputEventQueue queue) throws IOException {
        ArrayList<Controller> controllers = new ArrayList<Controller>();
        ArrayList<RawDevice> devices = new ArrayList<RawDevice>();
        RawInputEnvironmentPlugin.enumerateDevices(queue, devices);
        List<SetupAPIDevice> setupapi_devices = RawInputEnvironmentPlugin.enumSetupAPIDevices();
        RawInputEnvironmentPlugin.createControllersFromDevices(queue, controllers, devices, setupapi_devices);
        Controller[] controllers_array = new Controller[controllers.size()];
        controllers.toArray(controllers_array);
        return controllers_array;
    }

    @Override
    public boolean isSupported() {
        return supported;
    }

    private static final List<SetupAPIDevice> enumSetupAPIDevices() throws IOException {
        ArrayList<SetupAPIDevice> devices = new ArrayList<SetupAPIDevice>();
        RawInputEnvironmentPlugin.nEnumSetupAPIDevices(RawInputEnvironmentPlugin.getKeyboardClassGUID(), devices);
        RawInputEnvironmentPlugin.nEnumSetupAPIDevices(RawInputEnvironmentPlugin.getMouseClassGUID(), devices);
        return devices;
    }

    private static final native void nEnumSetupAPIDevices(byte[] var0, List<SetupAPIDevice> var1) throws IOException;

    private static final native byte[] getKeyboardClassGUID();

    private static final native byte[] getMouseClassGUID();

    static {
        String osName = RawInputEnvironmentPlugin.getPrivilegedProperty("os.name", "").trim();
        if (osName.startsWith("Windows")) {
            supported = true;
            if ("x86".equals(RawInputEnvironmentPlugin.getPrivilegedProperty("os.arch"))) {
                RawInputEnvironmentPlugin.loadLibrary("jinput-raw");
            } else {
                RawInputEnvironmentPlugin.loadLibrary("jinput-raw_64");
            }
        }
    }
}

