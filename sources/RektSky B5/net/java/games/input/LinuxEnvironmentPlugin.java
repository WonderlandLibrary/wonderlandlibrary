/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;
import net.java.games.input.LinuxAbstractController;
import net.java.games.input.LinuxCombinedController;
import net.java.games.input.LinuxComponent;
import net.java.games.input.LinuxDevice;
import net.java.games.input.LinuxDeviceTask;
import net.java.games.input.LinuxDeviceThread;
import net.java.games.input.LinuxEventComponent;
import net.java.games.input.LinuxEventDevice;
import net.java.games.input.LinuxJoystickAbstractController;
import net.java.games.input.LinuxJoystickAxis;
import net.java.games.input.LinuxJoystickButton;
import net.java.games.input.LinuxJoystickDevice;
import net.java.games.input.LinuxJoystickPOV;
import net.java.games.input.LinuxKeyboard;
import net.java.games.input.LinuxMouse;
import net.java.games.input.LinuxNativeTypesMap;
import net.java.games.input.LinuxPOV;
import net.java.games.input.Mouse;
import net.java.games.input.Rumbler;
import net.java.games.util.plugins.Plugin;

public final class LinuxEnvironmentPlugin
extends ControllerEnvironment
implements Plugin {
    private static final String LIBNAME = "jinput-linux";
    private static final String POSTFIX64BIT = "64";
    private static boolean supported = false;
    private final Controller[] controllers;
    private final List<LinuxDevice> devices = new ArrayList<LinuxDevice>();
    private static final LinuxDeviceThread device_thread = new LinuxDeviceThread();

    static void loadLibrary(String lib_name) {
        AccessController.doPrivileged(() -> {
            String lib_path = System.getProperty("net.java.games.input.librarypath");
            try {
                if (lib_path != null) {
                    System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
                } else {
                    System.loadLibrary(lib_name);
                }
            }
            catch (UnsatisfiedLinkError e2) {
                LinuxEnvironmentPlugin.log("Failed to load library: " + e2.getMessage());
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

    public static final Object execute(LinuxDeviceTask task) throws IOException {
        return device_thread.execute(task);
    }

    public LinuxEnvironmentPlugin() {
        if (this.isSupported()) {
            this.controllers = this.enumerateControllers();
            LinuxEnvironmentPlugin.log("Linux plugin claims to have found " + this.controllers.length + " controllers");
            AccessController.doPrivileged(() -> {
                Runtime.getRuntime().addShutdownHook(new ShutdownHook());
                return null;
            });
        } else {
            this.controllers = new Controller[0];
        }
    }

    @Override
    public final Controller[] getControllers() {
        return this.controllers;
    }

    private static final Component[] createComponents(List<LinuxEventComponent> event_components, LinuxEventDevice device) {
        int i2;
        LinuxEventComponent[][] povs = new LinuxEventComponent[4][2];
        ArrayList<LinuxComponent> components = new ArrayList<LinuxComponent>();
        for (i2 = 0; i2 < event_components.size(); ++i2) {
            LinuxEventComponent event_component = event_components.get(i2);
            Component.Identifier identifier = event_component.getIdentifier();
            if (identifier == Component.Identifier.Axis.POV) {
                int native_code = event_component.getDescriptor().getCode();
                switch (native_code) {
                    case 16: {
                        povs[0][0] = event_component;
                        break;
                    }
                    case 17: {
                        povs[0][1] = event_component;
                        break;
                    }
                    case 18: {
                        povs[1][0] = event_component;
                        break;
                    }
                    case 19: {
                        povs[1][1] = event_component;
                        break;
                    }
                    case 20: {
                        povs[2][0] = event_component;
                        break;
                    }
                    case 21: {
                        povs[2][1] = event_component;
                        break;
                    }
                    case 22: {
                        povs[3][0] = event_component;
                        break;
                    }
                    case 23: {
                        povs[3][1] = event_component;
                        break;
                    }
                    default: {
                        LinuxEnvironmentPlugin.log("Unknown POV instance: " + native_code);
                        break;
                    }
                }
                continue;
            }
            if (identifier == null) continue;
            LinuxComponent component = new LinuxComponent(event_component);
            components.add(component);
            device.registerComponent(event_component.getDescriptor(), component);
        }
        for (i2 = 0; i2 < povs.length; ++i2) {
            LinuxEventComponent x2 = povs[i2][0];
            LinuxEventComponent y2 = povs[i2][1];
            if (x2 == null || y2 == null) continue;
            LinuxPOV controller_component = new LinuxPOV(x2, y2);
            components.add(controller_component);
            device.registerComponent(x2.getDescriptor(), controller_component);
            device.registerComponent(y2.getDescriptor(), controller_component);
        }
        Component[] components_array = new Component[components.size()];
        components.toArray(components_array);
        return components_array;
    }

    private static final Mouse createMouseFromDevice(LinuxEventDevice device, Component[] components) throws IOException {
        LinuxMouse mouse = new LinuxMouse(device, components, new Controller[0], device.getRumblers());
        if (mouse.getX() != null && mouse.getY() != null && mouse.getPrimaryButton() != null) {
            return mouse;
        }
        return null;
    }

    private static final Keyboard createKeyboardFromDevice(LinuxEventDevice device, Component[] components) throws IOException {
        LinuxKeyboard keyboard = new LinuxKeyboard(device, components, new Controller[0], device.getRumblers());
        return keyboard;
    }

    private static final Controller createJoystickFromDevice(LinuxEventDevice device, Component[] components, Controller.Type type) throws IOException {
        LinuxAbstractController joystick = new LinuxAbstractController(device, components, new Controller[0], device.getRumblers(), type);
        return joystick;
    }

    private static final Controller createControllerFromDevice(LinuxEventDevice device) throws IOException {
        List<LinuxEventComponent> event_components = device.getComponents();
        Component[] components = LinuxEnvironmentPlugin.createComponents(event_components, device);
        Controller.Type type = device.getType();
        if (type == Controller.Type.MOUSE) {
            return LinuxEnvironmentPlugin.createMouseFromDevice(device, components);
        }
        if (type == Controller.Type.KEYBOARD) {
            return LinuxEnvironmentPlugin.createKeyboardFromDevice(device, components);
        }
        if (type == Controller.Type.STICK || type == Controller.Type.GAMEPAD) {
            return LinuxEnvironmentPlugin.createJoystickFromDevice(device, components, type);
        }
        return null;
    }

    private final Controller[] enumerateControllers() {
        ArrayList<Controller> controllers = new ArrayList<Controller>();
        ArrayList<Controller> eventControllers = new ArrayList<Controller>();
        ArrayList<Controller> jsControllers = new ArrayList<Controller>();
        this.enumerateEventControllers(eventControllers);
        this.enumerateJoystickControllers(jsControllers);
        block0: for (int i2 = 0; i2 < eventControllers.size(); ++i2) {
            for (int j2 = 0; j2 < jsControllers.size(); ++j2) {
                Component[] jsComponents;
                Component[] evComponents;
                Controller evController = (Controller)eventControllers.get(i2);
                Controller jsController = (Controller)jsControllers.get(j2);
                if (!evController.getName().equals(jsController.getName()) || (evComponents = evController.getComponents()).length != (jsComponents = jsController.getComponents()).length) continue;
                boolean foundADifference = false;
                for (int k2 = 0; k2 < evComponents.length; ++k2) {
                    if (evComponents[k2].getIdentifier() == jsComponents[k2].getIdentifier()) continue;
                    foundADifference = true;
                }
                if (foundADifference) continue;
                controllers.add(new LinuxCombinedController((LinuxAbstractController)eventControllers.remove(i2), (LinuxJoystickAbstractController)jsControllers.remove(j2)));
                --i2;
                --j2;
                continue block0;
            }
        }
        controllers.addAll(eventControllers);
        controllers.addAll(jsControllers);
        Controller[] controllers_array = new Controller[controllers.size()];
        controllers.toArray(controllers_array);
        return controllers_array;
    }

    private static final Component.Identifier.Button getButtonIdentifier(int index) {
        switch (index) {
            case 0: {
                return Component.Identifier.Button._0;
            }
            case 1: {
                return Component.Identifier.Button._1;
            }
            case 2: {
                return Component.Identifier.Button._2;
            }
            case 3: {
                return Component.Identifier.Button._3;
            }
            case 4: {
                return Component.Identifier.Button._4;
            }
            case 5: {
                return Component.Identifier.Button._5;
            }
            case 6: {
                return Component.Identifier.Button._6;
            }
            case 7: {
                return Component.Identifier.Button._7;
            }
            case 8: {
                return Component.Identifier.Button._8;
            }
            case 9: {
                return Component.Identifier.Button._9;
            }
            case 10: {
                return Component.Identifier.Button._10;
            }
            case 11: {
                return Component.Identifier.Button._11;
            }
            case 12: {
                return Component.Identifier.Button._12;
            }
            case 13: {
                return Component.Identifier.Button._13;
            }
            case 14: {
                return Component.Identifier.Button._14;
            }
            case 15: {
                return Component.Identifier.Button._15;
            }
            case 16: {
                return Component.Identifier.Button._16;
            }
            case 17: {
                return Component.Identifier.Button._17;
            }
            case 18: {
                return Component.Identifier.Button._18;
            }
            case 19: {
                return Component.Identifier.Button._19;
            }
            case 20: {
                return Component.Identifier.Button._20;
            }
            case 21: {
                return Component.Identifier.Button._21;
            }
            case 22: {
                return Component.Identifier.Button._22;
            }
            case 23: {
                return Component.Identifier.Button._23;
            }
            case 24: {
                return Component.Identifier.Button._24;
            }
            case 25: {
                return Component.Identifier.Button._25;
            }
            case 26: {
                return Component.Identifier.Button._26;
            }
            case 27: {
                return Component.Identifier.Button._27;
            }
            case 28: {
                return Component.Identifier.Button._28;
            }
            case 29: {
                return Component.Identifier.Button._29;
            }
            case 30: {
                return Component.Identifier.Button._30;
            }
            case 31: {
                return Component.Identifier.Button._31;
            }
        }
        return null;
    }

    private static final Controller createJoystickFromJoystickDevice(LinuxJoystickDevice device) {
        int i2;
        ArrayList<AbstractComponent> components = new ArrayList<AbstractComponent>();
        byte[] axisMap = device.getAxisMap();
        char[] buttonMap = device.getButtonMap();
        LinuxJoystickAxis[] hatBits = new LinuxJoystickAxis[6];
        for (i2 = 0; i2 < device.getNumButtons(); ++i2) {
            Component.Identifier button_id = LinuxNativeTypesMap.getButtonID(buttonMap[i2]);
            if (button_id == null) continue;
            LinuxJoystickButton button = new LinuxJoystickButton(button_id);
            device.registerButton(i2, button);
            components.add(button);
        }
        for (i2 = 0; i2 < device.getNumAxes(); ++i2) {
            Component.Identifier.Axis axis_id = (Component.Identifier.Axis)LinuxNativeTypesMap.getAbsAxisID(axisMap[i2]);
            LinuxJoystickAxis axis = new LinuxJoystickAxis(axis_id);
            device.registerAxis(i2, axis);
            if (axisMap[i2] == 16) {
                hatBits[0] = axis;
                continue;
            }
            if (axisMap[i2] == 17) {
                hatBits[1] = axis;
                axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[0], hatBits[1]);
                device.registerPOV((LinuxJoystickPOV)axis);
                components.add(axis);
                continue;
            }
            if (axisMap[i2] == 18) {
                hatBits[2] = axis;
                continue;
            }
            if (axisMap[i2] == 19) {
                hatBits[3] = axis;
                axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[2], hatBits[3]);
                device.registerPOV((LinuxJoystickPOV)axis);
                components.add(axis);
                continue;
            }
            if (axisMap[i2] == 20) {
                hatBits[4] = axis;
                continue;
            }
            if (axisMap[i2] == 21) {
                hatBits[5] = axis;
                axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[4], hatBits[5]);
                device.registerPOV((LinuxJoystickPOV)axis);
                components.add(axis);
                continue;
            }
            components.add(axis);
        }
        return new LinuxJoystickAbstractController(device, components.toArray(new Component[0]), new Controller[0], new Rumbler[0]);
    }

    private final void enumerateJoystickControllers(List<Controller> controllers) {
        File[] joystick_device_files = LinuxEnvironmentPlugin.enumerateJoystickDeviceFiles("/dev/input");
        if ((joystick_device_files == null || joystick_device_files.length == 0) && (joystick_device_files = LinuxEnvironmentPlugin.enumerateJoystickDeviceFiles("/dev")) == null) {
            return;
        }
        for (int i2 = 0; i2 < joystick_device_files.length; ++i2) {
            File event_file = joystick_device_files[i2];
            try {
                String path = LinuxEnvironmentPlugin.getAbsolutePathPrivileged(event_file);
                LinuxJoystickDevice device = new LinuxJoystickDevice(path);
                Controller controller = LinuxEnvironmentPlugin.createJoystickFromJoystickDevice(device);
                if (controller != null) {
                    controllers.add(controller);
                    this.devices.add(device);
                    continue;
                }
                device.close();
                continue;
            }
            catch (IOException e2) {
                LinuxEnvironmentPlugin.log("Failed to open device (" + event_file + "): " + e2.getMessage());
            }
        }
    }

    private static final File[] enumerateJoystickDeviceFiles(String dev_path) {
        File dev = new File(dev_path);
        return LinuxEnvironmentPlugin.listFilesPrivileged(dev, new FilenameFilter(){

            @Override
            public final boolean accept(File dir, String name) {
                return name.startsWith("js");
            }
        });
    }

    private static String getAbsolutePathPrivileged(File file) {
        return AccessController.doPrivileged(() -> file.getAbsolutePath());
    }

    private static File[] listFilesPrivileged(File dir, FilenameFilter filter) {
        return AccessController.doPrivileged(() -> {
            File[] files = dir.listFiles(filter);
            if (files == null) {
                LinuxEnvironmentPlugin.log("dir " + dir.getName() + " exists: " + dir.exists() + ", is writable: " + dir.isDirectory());
                files = new File[]{};
            } else {
                Arrays.sort(files, Comparator.comparing(File::getName));
            }
            return files;
        });
    }

    private final void enumerateEventControllers(List<Controller> controllers) {
        File dev = new File("/dev/input");
        File[] event_device_files = LinuxEnvironmentPlugin.listFilesPrivileged(dev, (dir, name) -> name.startsWith("event"));
        if (event_device_files == null) {
            return;
        }
        for (int i2 = 0; i2 < event_device_files.length; ++i2) {
            File event_file = event_device_files[i2];
            try {
                String path = LinuxEnvironmentPlugin.getAbsolutePathPrivileged(event_file);
                LinuxEventDevice device = new LinuxEventDevice(path);
                try {
                    Controller controller = LinuxEnvironmentPlugin.createControllerFromDevice(device);
                    if (controller != null) {
                        controllers.add(controller);
                        this.devices.add(device);
                        continue;
                    }
                    device.close();
                }
                catch (IOException e2) {
                    LinuxEnvironmentPlugin.log("Failed to create Controller: " + e2.getMessage());
                    device.close();
                }
                continue;
            }
            catch (IOException e3) {
                LinuxEnvironmentPlugin.log("Failed to open device (" + event_file + "): " + e3.getMessage());
            }
        }
    }

    @Override
    public boolean isSupported() {
        return supported;
    }

    static {
        String osName = LinuxEnvironmentPlugin.getPrivilegedProperty("os.name", "").trim();
        if (osName.equals("Linux")) {
            supported = true;
            if ("i386".equals(LinuxEnvironmentPlugin.getPrivilegedProperty("os.arch"))) {
                LinuxEnvironmentPlugin.loadLibrary(LIBNAME);
            } else {
                LinuxEnvironmentPlugin.loadLibrary("jinput-linux64");
            }
        }
    }

    private final class ShutdownHook
    extends Thread {
        private ShutdownHook() {
        }

        @Override
        public final void run() {
            for (int i2 = 0; i2 < LinuxEnvironmentPlugin.this.devices.size(); ++i2) {
                try {
                    LinuxDevice device = (LinuxDevice)LinuxEnvironmentPlugin.this.devices.get(i2);
                    device.close();
                    continue;
                }
                catch (IOException e2) {
                    ControllerEnvironment.log("Failed to close device: " + e2.getMessage());
                }
            }
        }
    }
}

