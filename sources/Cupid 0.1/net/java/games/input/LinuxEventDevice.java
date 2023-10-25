package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class LinuxEventDevice implements LinuxDevice {
  private final Map component_map;
  
  private final Rumbler[] rumblers;
  
  private final long fd;
  
  private final String name;
  
  private final LinuxInputID input_id;
  
  private final List components;
  
  private final Controller.Type type;
  
  private boolean closed;
  
  private final byte[] key_states;
  
  public LinuxEventDevice(String filename) throws IOException {
    long l;
    this.component_map = new HashMap();
    this.key_states = new byte[64];
    boolean detect_rumblers = true;
    try {
      l = nOpen(filename, true);
    } catch (IOException e) {
      l = nOpen(filename, false);
      detect_rumblers = false;
    } 
    this.fd = l;
    try {
      this.name = getDeviceName();
      this.input_id = getDeviceInputID();
      this.components = getDeviceComponents();
      if (detect_rumblers) {
        this.rumblers = enumerateRumblers();
      } else {
        this.rumblers = new Rumbler[0];
      } 
      this.type = guessType();
    } catch (IOException e) {
      close();
      throw e;
    } 
  }
  
  public final Controller.Type getType() {
    return this.type;
  }
  
  private static final int countComponents(List components, Class id_type, boolean relative) {
    int count = 0;
    for (int i = 0; i < components.size(); i++) {
      LinuxEventComponent component = components.get(i);
      if (id_type.isInstance(component.getIdentifier()) && relative == component.isRelative())
        count++; 
    } 
    return count;
  }
  
  private final Controller.Type guessType() throws IOException {
    Controller.Type type_from_usages = guessTypeFromUsages();
    if (type_from_usages == Controller.Type.UNKNOWN)
      return guessTypeFromComponents(); 
    return type_from_usages;
  }
  
  private final Controller.Type guessTypeFromUsages() throws IOException {
    byte[] usage_bits = getDeviceUsageBits();
    if (isBitSet(usage_bits, 0))
      return Controller.Type.MOUSE; 
    if (isBitSet(usage_bits, 3))
      return Controller.Type.KEYBOARD; 
    if (isBitSet(usage_bits, 2))
      return Controller.Type.GAMEPAD; 
    if (isBitSet(usage_bits, 1))
      return Controller.Type.STICK; 
    return Controller.Type.UNKNOWN;
  }
  
  private final Controller.Type guessTypeFromComponents() throws IOException {
    List components = getComponents();
    if (components.size() == 0)
      return Controller.Type.UNKNOWN; 
    int num_rel_axes = countComponents(components, Component.Identifier.Axis.class, true);
    int num_abs_axes = countComponents(components, Component.Identifier.Axis.class, false);
    int num_keys = countComponents(components, Component.Identifier.Key.class, false);
    int mouse_traits = 0;
    int keyboard_traits = 0;
    int joystick_traits = 0;
    int gamepad_traits = 0;
    if (this.name.toLowerCase().indexOf("mouse") != -1)
      mouse_traits++; 
    if (this.name.toLowerCase().indexOf("keyboard") != -1)
      keyboard_traits++; 
    if (this.name.toLowerCase().indexOf("joystick") != -1)
      joystick_traits++; 
    if (this.name.toLowerCase().indexOf("gamepad") != -1)
      gamepad_traits++; 
    int num_keyboard_button_traits = 0;
    int num_mouse_button_traits = 0;
    int num_joystick_button_traits = 0;
    int num_gamepad_button_traits = 0;
    for (int i = 0; i < components.size(); i++) {
      LinuxEventComponent component = components.get(i);
      if (component.getButtonTrait() == Controller.Type.MOUSE) {
        num_mouse_button_traits++;
      } else if (component.getButtonTrait() == Controller.Type.KEYBOARD) {
        num_keyboard_button_traits++;
      } else if (component.getButtonTrait() == Controller.Type.GAMEPAD) {
        num_gamepad_button_traits++;
      } else if (component.getButtonTrait() == Controller.Type.STICK) {
        num_joystick_button_traits++;
      } 
    } 
    if (num_mouse_button_traits >= num_keyboard_button_traits && num_mouse_button_traits >= num_joystick_button_traits && num_mouse_button_traits >= num_gamepad_button_traits) {
      mouse_traits++;
    } else if (num_keyboard_button_traits >= num_mouse_button_traits && num_keyboard_button_traits >= num_joystick_button_traits && num_keyboard_button_traits >= num_gamepad_button_traits) {
      keyboard_traits++;
    } else if (num_joystick_button_traits >= num_keyboard_button_traits && num_joystick_button_traits >= num_mouse_button_traits && num_joystick_button_traits >= num_gamepad_button_traits) {
      joystick_traits++;
    } else if (num_gamepad_button_traits >= num_keyboard_button_traits && num_gamepad_button_traits >= num_mouse_button_traits && num_gamepad_button_traits >= num_joystick_button_traits) {
      gamepad_traits++;
    } 
    if (num_rel_axes >= 2)
      mouse_traits++; 
    if (num_abs_axes >= 2) {
      joystick_traits++;
      gamepad_traits++;
    } 
    if (mouse_traits >= keyboard_traits && mouse_traits >= joystick_traits && mouse_traits >= gamepad_traits)
      return Controller.Type.MOUSE; 
    if (keyboard_traits >= mouse_traits && keyboard_traits >= joystick_traits && keyboard_traits >= gamepad_traits)
      return Controller.Type.KEYBOARD; 
    if (joystick_traits >= mouse_traits && joystick_traits >= keyboard_traits && joystick_traits >= gamepad_traits)
      return Controller.Type.STICK; 
    if (gamepad_traits >= mouse_traits && gamepad_traits >= keyboard_traits && gamepad_traits >= joystick_traits)
      return Controller.Type.GAMEPAD; 
    return null;
  }
  
  private final Rumbler[] enumerateRumblers() {
    List rumblers = new ArrayList();
    try {
      int num_effects = getNumEffects();
      if (num_effects <= 0)
        return (Rumbler[])rumblers.toArray((Object[])new Rumbler[0]); 
      byte[] ff_bits = getForceFeedbackBits();
      if (isBitSet(ff_bits, 80) && num_effects > rumblers.size())
        rumblers.add(new LinuxRumbleFF(this)); 
    } catch (IOException e) {
      LinuxEnvironmentPlugin.logln("Failed to enumerate rumblers: " + e.getMessage());
    } 
    return rumblers.<Rumbler>toArray(new Rumbler[0]);
  }
  
  public final Rumbler[] getRumblers() {
    return this.rumblers;
  }
  
  public final synchronized int uploadRumbleEffect(int id, int trigger_button, int direction, int trigger_interval, int replay_length, int replay_delay, int strong_magnitude, int weak_magnitude) throws IOException {
    checkClosed();
    return nUploadRumbleEffect(this.fd, id, direction, trigger_button, trigger_interval, replay_length, replay_delay, strong_magnitude, weak_magnitude);
  }
  
  public final synchronized int uploadConstantEffect(int id, int trigger_button, int direction, int trigger_interval, int replay_length, int replay_delay, int constant_level, int constant_env_attack_length, int constant_env_attack_level, int constant_env_fade_length, int constant_env_fade_level) throws IOException {
    checkClosed();
    return nUploadConstantEffect(this.fd, id, direction, trigger_button, trigger_interval, replay_length, replay_delay, constant_level, constant_env_attack_length, constant_env_attack_level, constant_env_fade_length, constant_env_fade_level);
  }
  
  final void eraseEffect(int id) throws IOException {
    nEraseEffect(this.fd, id);
  }
  
  public final synchronized void writeEvent(int type, int code, int value) throws IOException {
    checkClosed();
    nWriteEvent(this.fd, type, code, value);
  }
  
  public final void registerComponent(LinuxAxisDescriptor desc, LinuxComponent component) {
    this.component_map.put(desc, component);
  }
  
  public final LinuxComponent mapDescriptor(LinuxAxisDescriptor desc) {
    return (LinuxComponent)this.component_map.get(desc);
  }
  
  public final Controller.PortType getPortType() throws IOException {
    return this.input_id.getPortType();
  }
  
  public final LinuxInputID getInputID() {
    return this.input_id;
  }
  
  private final LinuxInputID getDeviceInputID() throws IOException {
    return nGetInputID(this.fd);
  }
  
  public final int getNumEffects() throws IOException {
    return nGetNumEffects(this.fd);
  }
  
  private final int getVersion() throws IOException {
    return nGetVersion(this.fd);
  }
  
  public final synchronized boolean getNextEvent(LinuxEvent linux_event) throws IOException {
    checkClosed();
    return nGetNextEvent(this.fd, linux_event);
  }
  
  public final synchronized void getAbsInfo(int abs_axis, LinuxAbsInfo abs_info) throws IOException {
    checkClosed();
    nGetAbsInfo(this.fd, abs_axis, abs_info);
  }
  
  private final void addKeys(List components) throws IOException {
    byte[] bits = getKeysBits();
    for (int i = 0; i < bits.length * 8; i++) {
      if (isBitSet(bits, i)) {
        Component.Identifier id = LinuxNativeTypesMap.getButtonID(i);
        components.add(new LinuxEventComponent(this, id, false, 1, i));
      } 
    } 
  }
  
  private final void addAbsoluteAxes(List components) throws IOException {
    byte[] bits = getAbsoluteAxesBits();
    for (int i = 0; i < bits.length * 8; i++) {
      if (isBitSet(bits, i)) {
        Component.Identifier id = LinuxNativeTypesMap.getAbsAxisID(i);
        components.add(new LinuxEventComponent(this, id, false, 3, i));
      } 
    } 
  }
  
  private final void addRelativeAxes(List components) throws IOException {
    byte[] bits = getRelativeAxesBits();
    for (int i = 0; i < bits.length * 8; i++) {
      if (isBitSet(bits, i)) {
        Component.Identifier id = LinuxNativeTypesMap.getRelAxisID(i);
        components.add(new LinuxEventComponent(this, id, true, 2, i));
      } 
    } 
  }
  
  public final List getComponents() {
    return this.components;
  }
  
  private final List getDeviceComponents() throws IOException {
    List components = new ArrayList();
    byte[] evtype_bits = getEventTypeBits();
    if (isBitSet(evtype_bits, 1))
      addKeys(components); 
    if (isBitSet(evtype_bits, 3))
      addAbsoluteAxes(components); 
    if (isBitSet(evtype_bits, 2))
      addRelativeAxes(components); 
    return components;
  }
  
  private final byte[] getForceFeedbackBits() throws IOException {
    byte[] bits = new byte[16];
    nGetBits(this.fd, 21, bits);
    return bits;
  }
  
  private final byte[] getKeysBits() throws IOException {
    byte[] bits = new byte[64];
    nGetBits(this.fd, 1, bits);
    return bits;
  }
  
  private final byte[] getAbsoluteAxesBits() throws IOException {
    byte[] bits = new byte[8];
    nGetBits(this.fd, 3, bits);
    return bits;
  }
  
  private final byte[] getRelativeAxesBits() throws IOException {
    byte[] bits = new byte[2];
    nGetBits(this.fd, 2, bits);
    return bits;
  }
  
  private final byte[] getEventTypeBits() throws IOException {
    byte[] bits = new byte[4];
    nGetBits(this.fd, 0, bits);
    return bits;
  }
  
  private final byte[] getDeviceUsageBits() throws IOException {
    byte[] bits = new byte[2];
    if (getVersion() >= 65537)
      nGetDeviceUsageBits(this.fd, bits); 
    return bits;
  }
  
  public final synchronized void pollKeyStates() throws IOException {
    nGetKeyStates(this.fd, this.key_states);
  }
  
  public final boolean isKeySet(int bit) {
    return isBitSet(this.key_states, bit);
  }
  
  public static final boolean isBitSet(byte[] bits, int bit) {
    return ((bits[bit / 8] & 1 << bit % 8) != 0);
  }
  
  public final String getName() {
    return this.name;
  }
  
  private final String getDeviceName() throws IOException {
    return nGetName(this.fd);
  }
  
  public final synchronized void close() throws IOException {
    if (this.closed)
      return; 
    this.closed = true;
    LinuxEnvironmentPlugin.execute(new LinuxDeviceTask() {
          private final LinuxEventDevice this$0;
          
          protected final Object execute() throws IOException {
            LinuxEventDevice.nClose(LinuxEventDevice.this.fd);
            return null;
          }
        });
  }
  
  private final void checkClosed() throws IOException {
    if (this.closed)
      throw new IOException("Device is closed"); 
  }
  
  protected void finalize() throws IOException {
    close();
  }
  
  private static final native long nOpen(String paramString, boolean paramBoolean) throws IOException;
  
  private static final native int nUploadRumbleEffect(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) throws IOException;
  
  private static final native int nUploadConstantEffect(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11) throws IOException;
  
  private static final native void nEraseEffect(long paramLong, int paramInt) throws IOException;
  
  private static final native void nWriteEvent(long paramLong, int paramInt1, int paramInt2, int paramInt3) throws IOException;
  
  private static final native LinuxInputID nGetInputID(long paramLong) throws IOException;
  
  private static final native int nGetNumEffects(long paramLong) throws IOException;
  
  private static final native int nGetVersion(long paramLong) throws IOException;
  
  private static final native boolean nGetNextEvent(long paramLong, LinuxEvent paramLinuxEvent) throws IOException;
  
  private static final native void nGetAbsInfo(long paramLong, int paramInt, LinuxAbsInfo paramLinuxAbsInfo) throws IOException;
  
  private static final native void nGetBits(long paramLong, int paramInt, byte[] paramArrayOfbyte) throws IOException;
  
  private static final native void nGetDeviceUsageBits(long paramLong, byte[] paramArrayOfbyte) throws IOException;
  
  private static final native void nGetKeyStates(long paramLong, byte[] paramArrayOfbyte) throws IOException;
  
  private static final native String nGetName(long paramLong) throws IOException;
  
  private static final native void nClose(long paramLong) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxEventDevice.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */