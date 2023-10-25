package net.java.games.input;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import net.java.games.util.plugins.Plugin;

public final class OSXEnvironmentPlugin extends ControllerEnvironment implements Plugin {
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
              OSXEnvironmentPlugin.supported = false;
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
    if (osName.equals("Mac OS X")) {
      supported = true;
      loadLibrary("jinput-osx");
    } 
  }
  
  private static final boolean isMacOSXEqualsOrBetterThan(int major_required, int minor_required) {
    int major, minor;
    String os_version = System.getProperty("os.version");
    StringTokenizer version_tokenizer = new StringTokenizer(os_version, ".");
    try {
      String major_str = version_tokenizer.nextToken();
      String minor_str = version_tokenizer.nextToken();
      major = Integer.parseInt(major_str);
      minor = Integer.parseInt(minor_str);
    } catch (Exception e) {
      logln("Exception occurred while trying to determine OS version: " + e);
      return false;
    } 
    return (major > major_required || (major == major_required && minor >= minor_required));
  }
  
  public OSXEnvironmentPlugin() {
    if (isSupported()) {
      this.controllers = enumerateControllers();
    } else {
      this.controllers = new Controller[0];
    } 
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
      OSXHIDElement element = it.next();
      Component.Identifier id = element.getIdentifier();
      if (id == null)
        continue; 
      if (map_mouse_buttons)
        if (id == Component.Identifier.Button._0) {
          id = Component.Identifier.Button.LEFT;
        } else if (id == Component.Identifier.Button._1) {
          id = Component.Identifier.Button.RIGHT;
        } else if (id == Component.Identifier.Button._2) {
          id = Component.Identifier.Button.MIDDLE;
        }  
      OSXComponent component = new OSXComponent(id, element);
      components.add(component);
      queue.addElement(element, component);
    } 
  }
  
  private static final Keyboard createKeyboardFromDevice(OSXHIDDevice device, List elements) throws IOException {
    List components = new ArrayList();
    OSXHIDQueue queue = device.createQueue(32);
    try {
      addElements(queue, elements, components, false);
    } catch (IOException e) {
      queue.release();
      throw e;
    } 
    Component[] components_array = new Component[components.size()];
    components.toArray((Object[])components_array);
    Keyboard keyboard = new OSXKeyboard(device, queue, components_array, new Controller[0], new Rumbler[0]);
    return keyboard;
  }
  
  private static final Mouse createMouseFromDevice(OSXHIDDevice device, List elements) throws IOException {
    List components = new ArrayList();
    OSXHIDQueue queue = device.createQueue(32);
    try {
      addElements(queue, elements, components, true);
    } catch (IOException e) {
      queue.release();
      throw e;
    } 
    Component[] components_array = new Component[components.size()];
    components.toArray((Object[])components_array);
    Mouse mouse = new OSXMouse(device, queue, components_array, new Controller[0], new Rumbler[0]);
    if (mouse.getPrimaryButton() != null && mouse.getX() != null && mouse.getY() != null)
      return mouse; 
    queue.release();
    return null;
  }
  
  private static final AbstractController createControllerFromDevice(OSXHIDDevice device, List elements, Controller.Type type) throws IOException {
    List components = new ArrayList();
    OSXHIDQueue queue = device.createQueue(32);
    try {
      addElements(queue, elements, components, false);
    } catch (IOException e) {
      queue.release();
      throw e;
    } 
    Component[] components_array = new Component[components.size()];
    components.toArray((Object[])components_array);
    AbstractController controller = new OSXAbstractController(device, queue, components_array, new Controller[0], new Rumbler[0], type);
    return controller;
  }
  
  private static final void createControllersFromDevice(OSXHIDDevice device, List controllers) throws IOException {
    UsagePair usage_pair = device.getUsagePair();
    if (usage_pair == null)
      return; 
    List elements = device.getElements();
    if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && (usage_pair.getUsage() == GenericDesktopUsage.MOUSE || usage_pair.getUsage() == GenericDesktopUsage.POINTER)) {
      Controller mouse = createMouseFromDevice(device, elements);
      if (mouse != null)
        controllers.add(mouse); 
    } else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && (usage_pair.getUsage() == GenericDesktopUsage.KEYBOARD || usage_pair.getUsage() == GenericDesktopUsage.KEYPAD)) {
      Controller keyboard = createKeyboardFromDevice(device, elements);
      if (keyboard != null)
        controllers.add(keyboard); 
    } else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.JOYSTICK) {
      Controller joystick = createControllerFromDevice(device, elements, Controller.Type.STICK);
      if (joystick != null)
        controllers.add(joystick); 
    } else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.MULTI_AXIS_CONTROLLER) {
      Controller multiaxis = createControllerFromDevice(device, elements, Controller.Type.STICK);
      if (multiaxis != null)
        controllers.add(multiaxis); 
    } else if (usage_pair.getUsagePage() == UsagePage.GENERIC_DESKTOP && usage_pair.getUsage() == GenericDesktopUsage.GAME_PAD) {
      Controller game_pad = createControllerFromDevice(device, elements, Controller.Type.GAMEPAD);
      if (game_pad != null)
        controllers.add(game_pad); 
    } 
  }
  
  private static final Controller[] enumerateControllers() {
    List controllers = new ArrayList();
    try {
      OSXHIDDeviceIterator it = new OSXHIDDeviceIterator();
      try {
        while (true) {
          try {
            OSXHIDDevice device = it.next();
            if (device == null)
              break; 
            boolean device_used = false;
            try {
              int old_size = controllers.size();
              createControllersFromDevice(device, controllers);
              device_used = (old_size != controllers.size());
            } catch (IOException e) {
              logln("Failed to create controllers from device: " + device.getProductName());
            } 
            if (!device_used)
              device.release(); 
          } catch (IOException e) {
            logln("Failed to enumerate device: " + e.getMessage());
          } 
        } 
      } finally {
        it.close();
      } 
    } catch (IOException e) {
      log("Failed to enumerate devices: " + e.getMessage());
      return new Controller[0];
    } 
    Controller[] controllers_array = new Controller[controllers.size()];
    controllers.toArray((Object[])controllers_array);
    return controllers_array;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\OSXEnvironmentPlugin.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */