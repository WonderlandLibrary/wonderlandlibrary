/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.AbstractController;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.DIAbstractController;
import net.java.games.input.DIComponent;
import net.java.games.input.DIDeviceObject;
import net.java.games.input.DIIdentifierMap;
import net.java.games.input.DIKeyboard;
import net.java.games.input.DIMouse;
import net.java.games.input.DummyWindow;
import net.java.games.input.IDirectInput;
import net.java.games.input.IDirectInputDevice;
import net.java.games.input.Keyboard;
import net.java.games.input.Mouse;
import net.java.games.util.plugins.Plugin;

public final class DirectInputEnvironmentPlugin
extends ControllerEnvironment
implements Plugin {
    private static boolean supported = false;
    private final Controller[] controllers;
    private final List<IDirectInputDevice> active_devices = new ArrayList<IDirectInputDevice>();
    private final DummyWindow window;

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

    public DirectInputEnvironmentPlugin() {
        DummyWindow window = null;
        Controller[] controllers = new Controller[]{};
        if (this.isSupported()) {
            try {
                window = new DummyWindow();
                try {
                    controllers = this.enumControllers(window);
                }
                catch (IOException e2) {
                    window.destroy();
                    throw e2;
                }
            }
            catch (IOException e3) {
                DirectInputEnvironmentPlugin.log("Failed to enumerate devices: " + e3.getMessage());
            }
            this.window = window;
            this.controllers = controllers;
            AccessController.doPrivileged(() -> {
                Runtime.getRuntime().addShutdownHook(new ShutdownHook());
                return null;
            });
        } else {
            this.window = null;
            this.controllers = controllers;
        }
    }

    @Override
    public final Controller[] getControllers() {
        return this.controllers;
    }

    private final Component[] createComponents(IDirectInputDevice device, boolean map_mouse_buttons) {
        List<DIDeviceObject> device_objects = device.getObjects();
        ArrayList<DIComponent> controller_components = new ArrayList<DIComponent>();
        for (int i2 = 0; i2 < device_objects.size(); ++i2) {
            DIDeviceObject device_object = device_objects.get(i2);
            Component.Identifier identifier = device_object.getIdentifier();
            if (identifier == null) continue;
            if (map_mouse_buttons && identifier instanceof Component.Identifier.Button) {
                identifier = DIIdentifierMap.mapMouseButtonIdentifier((Component.Identifier.Button)identifier);
            }
            DIComponent component = new DIComponent(identifier, device_object);
            controller_components.add(component);
            device.registerComponent(device_object, component);
        }
        Component[] components = new Component[controller_components.size()];
        controller_components.toArray(components);
        return components;
    }

    private final Mouse createMouseFromDevice(IDirectInputDevice device) {
        Component[] components = this.createComponents(device, true);
        DIMouse mouse = new DIMouse(device, components, new Controller[0], device.getRumblers());
        if (mouse.getX() != null && mouse.getY() != null && mouse.getPrimaryButton() != null) {
            return mouse;
        }
        return null;
    }

    private final AbstractController createControllerFromDevice(IDirectInputDevice device, Controller.Type type) {
        Component[] components = this.createComponents(device, false);
        DIAbstractController controller = new DIAbstractController(device, components, new Controller[0], device.getRumblers(), type);
        return controller;
    }

    private final Keyboard createKeyboardFromDevice(IDirectInputDevice device) {
        Component[] components = this.createComponents(device, false);
        return new DIKeyboard(device, components, new Controller[0], device.getRumblers());
    }

    private final Controller createControllerFromDevice(IDirectInputDevice device) {
        switch (device.getType()) {
            case 18: {
                return this.createMouseFromDevice(device);
            }
            case 19: {
                return this.createKeyboardFromDevice(device);
            }
            case 21: {
                return this.createControllerFromDevice(device, Controller.Type.GAMEPAD);
            }
            case 22: {
                return this.createControllerFromDevice(device, Controller.Type.WHEEL);
            }
            case 20: 
            case 23: 
            case 24: {
                return this.createControllerFromDevice(device, Controller.Type.STICK);
            }
        }
        return this.createControllerFromDevice(device, Controller.Type.UNKNOWN);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final Controller[] enumControllers(DummyWindow window) throws IOException {
        ArrayList<Controller> controllers = new ArrayList<Controller>();
        IDirectInput dinput = new IDirectInput(window);
        try {
            List<IDirectInputDevice> devices = dinput.getDevices();
            for (int i2 = 0; i2 < devices.size(); ++i2) {
                IDirectInputDevice device = devices.get(i2);
                Controller controller = this.createControllerFromDevice(device);
                if (controller != null) {
                    controllers.add(controller);
                    this.active_devices.add(device);
                    continue;
                }
                device.release();
            }
        }
        finally {
            dinput.release();
        }
        Controller[] controllers_array = new Controller[controllers.size()];
        controllers.toArray(controllers_array);
        return controllers_array;
    }

    @Override
    public boolean isSupported() {
        return supported;
    }

    static {
        String osName = DirectInputEnvironmentPlugin.getPrivilegedProperty("os.name", "").trim();
        if (osName.startsWith("Windows")) {
            supported = true;
            if ("x86".equals(DirectInputEnvironmentPlugin.getPrivilegedProperty("os.arch"))) {
                DirectInputEnvironmentPlugin.loadLibrary("jinput-dx8");
            } else {
                DirectInputEnvironmentPlugin.loadLibrary("jinput-dx8_64");
            }
        }
    }

    private final class ShutdownHook
    extends Thread {
        private ShutdownHook() {
        }

        @Override
        public final void run() {
            for (int i2 = 0; i2 < DirectInputEnvironmentPlugin.this.active_devices.size(); ++i2) {
                IDirectInputDevice device = (IDirectInputDevice)DirectInputEnvironmentPlugin.this.active_devices.get(i2);
                device.release();
            }
        }
    }
}

