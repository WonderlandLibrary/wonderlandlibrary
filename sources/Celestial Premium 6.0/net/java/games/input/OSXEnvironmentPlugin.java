/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import net.java.games.input.AbstractController;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.GenericDesktopUsage;
import net.java.games.input.Keyboard;
import net.java.games.input.Mouse;
import net.java.games.input.OSXAbstractController;
import net.java.games.input.OSXComponent;
import net.java.games.input.OSXHIDDevice;
import net.java.games.input.OSXHIDDeviceIterator;
import net.java.games.input.OSXHIDElement;
import net.java.games.input.OSXHIDQueue;
import net.java.games.input.OSXKeyboard;
import net.java.games.input.OSXMouse;
import net.java.games.input.Rumbler;
import net.java.games.input.UsagePage;
import net.java.games.input.UsagePair;
import net.java.games.util.plugins.Plugin;

public final class OSXEnvironmentPlugin
extends ControllerEnvironment
implements Plugin {
    private static boolean supported = false;
    private final Controller[] controllers = this.isSupported() ? OSXEnvironmentPlugin.enumerateControllers() : new Controller[0];

    static void loadLibrary(final String lib_name) {
        AccessController.doPrivileged(new PrivilegedAction(){

            public final Object run() {
                try {
                    String lib_path = System.getProperty("net.java.games.input.librarypath");
                    if (lib_path != null) {
                        System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
                    } else {
                        System.loadLibrary(lib_name);
                    }
                }
                catch (UnsatisfiedLinkError e) {
                    e.printStackTrace();
                    supported = false;
                }
                return null;
            }
        });
    }

    static String getPrivilegedProperty(final String property) {
        return (String)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                return System.getProperty(property);
            }
        });
    }

    static String getPrivilegedProperty(final String property, final String default_value) {
        return (String)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                return System.getProperty(property, default_value);
            }
        });
    }

    private static final boolean isMacOSXEqualsOrBetterThan(int major_required, int minor_required) {
        int minor;
        int major;
        String os_version = System.getProperty("os.version");
        StringTokenizer version_tokenizer = new StringTokenizer(os_version, ".");
        try {
            String major_str = version_tokenizer.nextToken();
            String minor_str = version_tokenizer.nextToken();
            major = Integer.parseInt(major_str);
            minor = Integer.parseInt(minor_str);
        }
        catch (Exception e) {
            OSXEnvironmentPlugin.logln("Exception occurred while trying to determine OS version: " + e);
            return false;
        }
        return major > major_required || major == major_required && minor >= minor_required;
    }

    public final Controller[] getControllers() {
        return this.controllers;
    }

    public boolean isSupported() {
        return supported;
    }

    private static final void addElements(OSXHIDQueue queue, List elements, List components, boolean map_mouse_buttons) throws IOException {
        Iterator it = elements.iterator();
        while (it.hasNext()) {
            OSXHIDElement element = (OSXHIDElement)it.next();
            Component.Identifier id = element.getIdentifier();
            if (id == null) continue;
            if (map_mouse_buttons) {
                if (id == Component.Identifier.Button._0) {
                    id = Component.Identifier.Button.LEFT;
                } else if (id == Component.Identifier.Button._1) {
                    id = Component.Identifier.Button.RIGHT;
                } else if (id == Component.Identifier.Button._2) {
                    id = Component.Identifier.Button.MIDDLE;
                }
            }
            OSXComponent component = new OSXComponent(id, element);
            components.add(component);
            queue.addElement(element, component);
        }
    }

    private static final Keyboard createKeyboardFromDevice(OSXHIDDevice device, List elements) throws IOException {
        ArrayList components = new ArrayList();
        OSXHIDQueue queue = device.createQueue(32);
        try {
            OSXEnvironmentPlugin.addElements(queue, elements, components, false);
        }
        catch (IOException e) {
            queue.release();
            throw e;
        }
        Component[] components_array = new Component[components.size()];
        components.toArray(components_array);
        OSXKeyboard keyboard = new OSXKeyboard(device, queue, components_array, new Controller[0], new Rumbler[0]);
        return keyboard;
    }

    private static final Mouse createMouseFromDevice(OSXHIDDevice device, List elements) throws IOException {
        ArrayList components = new ArrayList();
        OSXHIDQueue queue = device.createQueue(32);
        try {
            OSXEnvironmentPlugin.addElements(queue, elements, components, true);
        }
        catch (IOException e) {
            queue.release();
            throw e;
        }
        Component[] components_array = new Component[components.size()];
        components.toArray(components_array);
        OSXMouse mouse = new OSXMouse(device, queue, components_array, new Controller[0], new Rumbler[0]);
        if (mouse.getPrimaryButton() != null && mouse.getX() != null && mouse.getY() != null) {
            return mouse;
        }
        queue.release();
        return null;
    }

    private static final AbstractController createControllerFromDevice(OSXHIDDevice device, List elements, Controller.Type type) throws IOException {
        ArrayList components = new ArrayList();
        OSXHIDQueue queue = device.createQueue(32);
        try {
            OSXEnvironmentPlugin.addElements(queue, elements, components, false);
        }
        catch (IOException e) {
            queue.release();
            throw e;
        }
        Component[] components_array = new Component[components.size()];
        components.toArray(components_array);
        OSXAbstractController controller = new OSXAbstractController(device, queue, components_array, new Controller[0], new Rumbler[0], type);
        return controller;
    }

    private static final void createControllersFromDevice(OSXHIDDevice device, List controllers) throws IOException {
        AbstractController game_pad;
        UsagePair usage_pair = device.getUsagePair();
        if (usage_pair == null) {
            return;
        }
        List elements = device.getElements();
        if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && (usage_pair.getUsage() == GenericDesktopUsage.MOUSE || usage_pair.getUsage() == GenericDesktopUsage.POINTER)) {
            Mouse mouse = OSXEnvironmentPlugin.createMouseFromDevice(device, elements);
            if (mouse != null) {
                controllers.add(mouse);
            }
        } else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && (usage_pair.getUsage() == GenericDesktopUsage.KEYBOARD || usage_pair.getUsage() == GenericDesktopUsage.KEYPAD)) {
            Keyboard keyboard = OSXEnvironmentPlugin.createKeyboardFromDevice(device, elements);
            if (keyboard != null) {
                controllers.add(keyboard);
            }
        } else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.JOYSTICK) {
            AbstractController joystick = OSXEnvironmentPlugin.createControllerFromDevice(device, elements, Controller.Type.STICK);
            if (joystick != null) {
                controllers.add(joystick);
            }
        } else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.MULTI_AXIS_CONTROLLER) {
            AbstractController multiaxis = OSXEnvironmentPlugin.createControllerFromDevice(device, elements, Controller.Type.STICK);
            if (multiaxis != null) {
                controllers.add(multiaxis);
            }
        } else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.GAME_PAD && (game_pad = OSXEnvironmentPlugin.createControllerFromDevice(device, elements, Controller.Type.GAMEPAD)) != null) {
            controllers.add(game_pad);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static final Controller[] enumerateControllers() {
        ArrayList controllers;
        block12: {
            controllers = new ArrayList();
            try {
                OSXHIDDeviceIterator it = new OSXHIDDeviceIterator();
                block9: while (true) {
                    while (true) {
                        try {
                            OSXHIDDevice device = it.next();
                            if (device == null) {
                                break block12;
                            }
                            boolean device_used = false;
                            try {
                                int old_size = controllers.size();
                                OSXEnvironmentPlugin.createControllersFromDevice(device, controllers);
                                device_used = old_size != controllers.size();
                            }
                            catch (IOException e) {
                                OSXEnvironmentPlugin.logln("Failed to create controllers from device: " + device.getProductName());
                            }
                            if (device_used) continue block9;
                            device.release();
                            continue block9;
                        }
                        catch (IOException e) {
                            OSXEnvironmentPlugin.logln("Failed to enumerate device: " + e.getMessage());
                            continue;
                        }
                        break;
                    }
                }
                finally {
                    it.close();
                }
            }
            catch (IOException e) {
                OSXEnvironmentPlugin.log("Failed to enumerate devices: " + e.getMessage());
                return new Controller[0];
            }
        }
        Controller[] controllers_array = new Controller[controllers.size()];
        controllers.toArray(controllers_array);
        return controllers_array;
    }

    static {
        String osName = OSXEnvironmentPlugin.getPrivilegedProperty("os.name", "").trim();
        if (osName.equals("Mac OS X")) {
            supported = true;
            OSXEnvironmentPlugin.loadLibrary("jinput-osx");
        }
    }
}

