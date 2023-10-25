package net.java.games.input;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import net.java.games.util.plugins.Plugin;

public final class RawInputEnvironmentPlugin extends ControllerEnvironment implements Plugin {
  private static boolean supported = false;
  
  private final Controller[] controllers;
  
  static void loadLibrary(final String lib_name) {
    AccessController.doPrivileged(new PrivilegedAction() {
          private final String val$lib_name;
          
          public final Object run() {
            try {
              String lib_path = System.getProperty("net.java.games.input.librarypath");
              if (lib_path != null) {
                System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
              } else {
                System.loadLibrary(lib_name);
              } 
            } catch (UnsatisfiedLinkError e) {
              e.printStackTrace();
              RawInputEnvironmentPlugin.supported = false;
            } 
            return null;
          }
        });
  }
  
  static String getPrivilegedProperty(final String property) {
    return AccessController.<String>doPrivileged(new PrivilegedAction() {
          private final String val$property;
          
          public Object run() {
            return System.getProperty(property);
          }
        });
  }
  
  static String getPrivilegedProperty(final String property, final String default_value) {
    return AccessController.<String>doPrivileged(new PrivilegedAction() {
          private final String val$property;
          
          private final String val$default_value;
          
          public Object run() {
            return System.getProperty(property, default_value);
          }
        });
  }
  
  static {
    String osName = getPrivilegedProperty("os.name", "").trim();
    if (osName.startsWith("Windows")) {
      supported = true;
      if ("x86".equals(getPrivilegedProperty("os.arch"))) {
        loadLibrary("jinput-raw");
      } else {
        loadLibrary("jinput-raw_64");
      } 
    } 
  }
  
  public RawInputEnvironmentPlugin() {
    Controller[] controllers = new Controller[0];
    if (isSupported())
      try {
        RawInputEventQueue queue = new RawInputEventQueue();
        controllers = enumControllers(queue);
      } catch (IOException e) {
        logln("Failed to enumerate devices: " + e.getMessage());
      }  
    this.controllers = controllers;
  }
  
  public final Controller[] getControllers() {
    return this.controllers;
  }
  
  private static final SetupAPIDevice lookupSetupAPIDevice(String device_name, List setupapi_devices) {
    device_name = device_name.replaceAll("#", "\\\\").toUpperCase();
    for (int i = 0; i < setupapi_devices.size(); i++) {
      SetupAPIDevice device = setupapi_devices.get(i);
      if (device_name.indexOf(device.getInstanceId().toUpperCase()) != -1)
        return device; 
    } 
    return null;
  }
  
  private static final void createControllersFromDevices(RawInputEventQueue queue, List controllers, List devices, List setupapi_devices) throws IOException {
    List active_devices = new ArrayList();
    for (int i = 0; i < devices.size(); i++) {
      RawDevice device = devices.get(i);
      SetupAPIDevice setupapi_device = lookupSetupAPIDevice(device.getName(), setupapi_devices);
      if (setupapi_device != null) {
        RawDeviceInfo info = device.getInfo();
        Controller controller = info.createControllerFromDevice(device, setupapi_device);
        if (controller != null) {
          controllers.add(controller);
          active_devices.add(device);
        } 
      } 
    } 
    queue.start(active_devices);
  }
  
  private final Controller[] enumControllers(RawInputEventQueue queue) throws IOException {
    List controllers = new ArrayList();
    List devices = new ArrayList();
    enumerateDevices(queue, devices);
    List setupapi_devices = enumSetupAPIDevices();
    createControllersFromDevices(queue, controllers, devices, setupapi_devices);
    Controller[] controllers_array = new Controller[controllers.size()];
    controllers.toArray((Object[])controllers_array);
    return controllers_array;
  }
  
  public boolean isSupported() {
    return supported;
  }
  
  private static final List enumSetupAPIDevices() throws IOException {
    List devices = new ArrayList();
    nEnumSetupAPIDevices(getKeyboardClassGUID(), devices);
    nEnumSetupAPIDevices(getMouseClassGUID(), devices);
    return devices;
  }
  
  private static final native void enumerateDevices(RawInputEventQueue paramRawInputEventQueue, List paramList) throws IOException;
  
  private static final native void nEnumSetupAPIDevices(byte[] paramArrayOfbyte, List paramList) throws IOException;
  
  private static final native byte[] getKeyboardClassGUID();
  
  private static final native byte[] getMouseClassGUID();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\RawInputEnvironmentPlugin.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */