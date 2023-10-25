package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class IDirectInputDevice {
  public static final int GUID_XAxis = 1;
  
  public static final int GUID_YAxis = 2;
  
  public static final int GUID_ZAxis = 3;
  
  public static final int GUID_RxAxis = 4;
  
  public static final int GUID_RyAxis = 5;
  
  public static final int GUID_RzAxis = 6;
  
  public static final int GUID_Slider = 7;
  
  public static final int GUID_Button = 8;
  
  public static final int GUID_Key = 9;
  
  public static final int GUID_POV = 10;
  
  public static final int GUID_Unknown = 11;
  
  public static final int GUID_ConstantForce = 12;
  
  public static final int GUID_RampForce = 13;
  
  public static final int GUID_Square = 14;
  
  public static final int GUID_Sine = 15;
  
  public static final int GUID_Triangle = 16;
  
  public static final int GUID_SawtoothUp = 17;
  
  public static final int GUID_SawtoothDown = 18;
  
  public static final int GUID_Spring = 19;
  
  public static final int GUID_Damper = 20;
  
  public static final int GUID_Inertia = 21;
  
  public static final int GUID_Friction = 22;
  
  public static final int GUID_CustomForce = 23;
  
  public static final int DI8DEVTYPE_DEVICE = 17;
  
  public static final int DI8DEVTYPE_MOUSE = 18;
  
  public static final int DI8DEVTYPE_KEYBOARD = 19;
  
  public static final int DI8DEVTYPE_JOYSTICK = 20;
  
  public static final int DI8DEVTYPE_GAMEPAD = 21;
  
  public static final int DI8DEVTYPE_DRIVING = 22;
  
  public static final int DI8DEVTYPE_FLIGHT = 23;
  
  public static final int DI8DEVTYPE_1STPERSON = 24;
  
  public static final int DI8DEVTYPE_DEVICECTRL = 25;
  
  public static final int DI8DEVTYPE_SCREENPOINTER = 26;
  
  public static final int DI8DEVTYPE_REMOTE = 27;
  
  public static final int DI8DEVTYPE_SUPPLEMENTAL = 28;
  
  public static final int DISCL_EXCLUSIVE = 1;
  
  public static final int DISCL_NONEXCLUSIVE = 2;
  
  public static final int DISCL_FOREGROUND = 4;
  
  public static final int DISCL_BACKGROUND = 8;
  
  public static final int DISCL_NOWINKEY = 16;
  
  public static final int DIDFT_ALL = 0;
  
  public static final int DIDFT_RELAXIS = 1;
  
  public static final int DIDFT_ABSAXIS = 2;
  
  public static final int DIDFT_AXIS = 3;
  
  public static final int DIDFT_PSHBUTTON = 4;
  
  public static final int DIDFT_TGLBUTTON = 8;
  
  public static final int DIDFT_BUTTON = 12;
  
  public static final int DIDFT_POV = 16;
  
  public static final int DIDFT_COLLECTION = 64;
  
  public static final int DIDFT_NODATA = 128;
  
  public static final int DIDFT_FFACTUATOR = 16777216;
  
  public static final int DIDFT_FFEFFECTTRIGGER = 33554432;
  
  public static final int DIDFT_OUTPUT = 268435456;
  
  public static final int DIDFT_VENDORDEFINED = 67108864;
  
  public static final int DIDFT_ALIAS = 134217728;
  
  public static final int DIDFT_OPTIONAL = -2147483648;
  
  public static final int DIDFT_NOCOLLECTION = 16776960;
  
  public static final int DIDF_ABSAXIS = 1;
  
  public static final int DIDF_RELAXIS = 2;
  
  public static final int DI_OK = 0;
  
  public static final int DI_NOEFFECT = 1;
  
  public static final int DI_PROPNOEFFECT = 1;
  
  public static final int DI_POLLEDDEVICE = 2;
  
  public static final int DI_DOWNLOADSKIPPED = 3;
  
  public static final int DI_EFFECTRESTARTED = 4;
  
  public static final int DI_TRUNCATED = 8;
  
  public static final int DI_SETTINGSNOTSAVED = 11;
  
  public static final int DI_TRUNCATEDANDRESTARTED = 12;
  
  public static final int DI_BUFFEROVERFLOW = 1;
  
  public static final int DIERR_INPUTLOST = -2147024866;
  
  public static final int DIERR_NOTACQUIRED = -2147024868;
  
  public static final int DIERR_OTHERAPPHASPRIO = -2147024891;
  
  public static final int DIDOI_FFACTUATOR = 1;
  
  public static final int DIDOI_FFEFFECTTRIGGER = 2;
  
  public static final int DIDOI_POLLED = 32768;
  
  public static final int DIDOI_ASPECTPOSITION = 256;
  
  public static final int DIDOI_ASPECTVELOCITY = 512;
  
  public static final int DIDOI_ASPECTACCEL = 768;
  
  public static final int DIDOI_ASPECTFORCE = 1024;
  
  public static final int DIDOI_ASPECTMASK = 3840;
  
  public static final int DIDOI_GUIDISUSAGE = 65536;
  
  public static final int DIEFT_ALL = 0;
  
  public static final int DIEFT_CONSTANTFORCE = 1;
  
  public static final int DIEFT_RAMPFORCE = 2;
  
  public static final int DIEFT_PERIODIC = 3;
  
  public static final int DIEFT_CONDITION = 4;
  
  public static final int DIEFT_CUSTOMFORCE = 5;
  
  public static final int DIEFT_HARDWARE = 255;
  
  public static final int DIEFT_FFATTACK = 512;
  
  public static final int DIEFT_FFFADE = 1024;
  
  public static final int DIEFT_SATURATION = 2048;
  
  public static final int DIEFT_POSNEGCOEFFICIENTS = 4096;
  
  public static final int DIEFT_POSNEGSATURATION = 8192;
  
  public static final int DIEFT_DEADBAND = 16384;
  
  public static final int DIEFT_STARTDELAY = 32768;
  
  public static final int DIEFF_OBJECTIDS = 1;
  
  public static final int DIEFF_OBJECTOFFSETS = 2;
  
  public static final int DIEFF_CARTESIAN = 16;
  
  public static final int DIEFF_POLAR = 32;
  
  public static final int DIEFF_SPHERICAL = 64;
  
  public static final int DIEP_DURATION = 1;
  
  public static final int DIEP_SAMPLEPERIOD = 2;
  
  public static final int DIEP_GAIN = 4;
  
  public static final int DIEP_TRIGGERBUTTON = 8;
  
  public static final int DIEP_TRIGGERREPEATINTERVAL = 16;
  
  public static final int DIEP_AXES = 32;
  
  public static final int DIEP_DIRECTION = 64;
  
  public static final int DIEP_ENVELOPE = 128;
  
  public static final int DIEP_TYPESPECIFICPARAMS = 256;
  
  public static final int DIEP_STARTDELAY = 512;
  
  public static final int DIEP_ALLPARAMS_DX5 = 511;
  
  public static final int DIEP_ALLPARAMS = 1023;
  
  public static final int DIEP_START = 536870912;
  
  public static final int DIEP_NORESTART = 1073741824;
  
  public static final int DIEP_NODOWNLOAD = -2147483648;
  
  public static final int DIEB_NOTRIGGER = -1;
  
  public static final int INFINITE = -1;
  
  public static final int DI_DEGREES = 100;
  
  public static final int DI_FFNOMINALMAX = 10000;
  
  public static final int DI_SECONDS = 1000000;
  
  public static final int DIPROPRANGE_NOMIN = -2147483648;
  
  public static final int DIPROPRANGE_NOMAX = 2147483647;
  
  private final DummyWindow window;
  
  private final long address;
  
  private final int dev_type;
  
  private final int dev_subtype;
  
  private final String instance_name;
  
  private final String product_name;
  
  private final List objects = new ArrayList();
  
  private final List effects = new ArrayList();
  
  private final List rumblers = new ArrayList();
  
  private final int[] device_state;
  
  private final Map object_to_component = new HashMap();
  
  private final boolean axes_in_relative_mode;
  
  private boolean released;
  
  private DataQueue queue;
  
  private int button_counter;
  
  private int current_format_offset;
  
  public IDirectInputDevice(DummyWindow window, long address, byte[] instance_guid, byte[] product_guid, int dev_type, int dev_subtype, String instance_name, String product_name) throws IOException {
    this.window = window;
    this.address = address;
    this.product_name = product_name;
    this.instance_name = instance_name;
    this.dev_type = dev_type;
    this.dev_subtype = dev_subtype;
    enumObjects();
    try {
      enumEffects();
      createRumblers();
    } catch (IOException e) {
      DirectInputEnvironmentPlugin.logln("Failed to create rumblers: " + e.getMessage());
    } 
    boolean all_relative = true;
    boolean has_axis = false;
    for (int i = 0; i < this.objects.size(); i++) {
      DIDeviceObject obj = this.objects.get(i);
      if (obj.isAxis()) {
        has_axis = true;
        if (!obj.isRelative()) {
          all_relative = false;
          break;
        } 
      } 
    } 
    this.axes_in_relative_mode = (all_relative && has_axis);
    int axis_mode = all_relative ? 2 : 1;
    setDataFormat(axis_mode);
    if (this.rumblers.size() > 0) {
      try {
        setCooperativeLevel(9);
      } catch (IOException e) {
        setCooperativeLevel(10);
      } 
    } else {
      setCooperativeLevel(10);
    } 
    setBufferSize(32);
    acquire();
    this.device_state = new int[this.objects.size()];
  }
  
  public final boolean areAxesRelative() {
    return this.axes_in_relative_mode;
  }
  
  public final Rumbler[] getRumblers() {
    return (Rumbler[])this.rumblers.toArray((Object[])new Rumbler[0]);
  }
  
  private final List createRumblers() throws IOException {
    DIDeviceObject x_axis = lookupObjectByGUID(1);
    if (x_axis == null)
      return this.rumblers; 
    DIDeviceObject[] axes = { x_axis };
    long[] directions = { 0L };
    for (int i = 0; i < this.effects.size(); i++) {
      DIEffectInfo info = this.effects.get(i);
      if ((info.getEffectType() & 0xFF) == 3 && (info.getDynamicParams() & 0x4) != 0)
        this.rumblers.add(createPeriodicRumbler(axes, directions, info)); 
    } 
    return this.rumblers;
  }
  
  private final Rumbler createPeriodicRumbler(DIDeviceObject[] axes, long[] directions, DIEffectInfo info) throws IOException {
    int[] axis_ids = new int[axes.length];
    for (int i = 0; i < axis_ids.length; i++)
      axis_ids[i] = axes[i].getDIIdentifier(); 
    long effect_address = nCreatePeriodicEffect(this.address, info.getGUID(), 17, -1, 0, 10000, -1, 0, axis_ids, directions, 0, 0, 0, 0, 10000, 0, 0, 50000, 0);
    return new IDirectInputEffect(effect_address, info);
  }
  
  private final DIDeviceObject lookupObjectByGUID(int guid_id) {
    for (int i = 0; i < this.objects.size(); i++) {
      DIDeviceObject object = this.objects.get(i);
      if (guid_id == object.getGUIDType())
        return object; 
    } 
    return null;
  }
  
  public final int getPollData(DIDeviceObject object) {
    return this.device_state[object.getFormatOffset()];
  }
  
  public final DIDeviceObject mapEvent(DIDeviceObjectData event) {
    int format_offset = event.getFormatOffset() / 4;
    return this.objects.get(format_offset);
  }
  
  public final DIComponent mapObject(DIDeviceObject object) {
    return (DIComponent)this.object_to_component.get(object);
  }
  
  public final void registerComponent(DIDeviceObject object, DIComponent component) {
    this.object_to_component.put(object, component);
  }
  
  public final synchronized void pollAll() throws IOException {
    checkReleased();
    poll();
    getDeviceState(this.device_state);
    this.queue.compact();
    getDeviceData(this.queue);
    this.queue.flip();
  }
  
  public final synchronized boolean getNextEvent(DIDeviceObjectData data) {
    DIDeviceObjectData next_event = (DIDeviceObjectData)this.queue.get();
    if (next_event == null)
      return false; 
    data.set(next_event);
    return true;
  }
  
  private final void poll() throws IOException {
    int res = nPoll(this.address);
    if (res != 0 && res != 1) {
      if (res == -2147024868) {
        acquire();
        return;
      } 
      throw new IOException("Failed to poll device (" + Integer.toHexString(res) + ")");
    } 
  }
  
  private final void acquire() throws IOException {
    int res = nAcquire(this.address);
    if (res != 0 && res != -2147024891 && res != 1)
      throw new IOException("Failed to acquire device (" + Integer.toHexString(res) + ")"); 
  }
  
  private final void unacquire() throws IOException {
    int res = nUnacquire(this.address);
    if (res != 0 && res != 1)
      throw new IOException("Failed to unAcquire device (" + Integer.toHexString(res) + ")"); 
  }
  
  private final boolean getDeviceData(DataQueue queue) throws IOException {
    int res = nGetDeviceData(this.address, 0, queue, queue.getElements(), queue.position(), queue.remaining());
    if (res != 0 && res != 1) {
      if (res == -2147024868) {
        acquire();
        return false;
      } 
      throw new IOException("Failed to get device data (" + Integer.toHexString(res) + ")");
    } 
    return true;
  }
  
  private final void getDeviceState(int[] device_state) throws IOException {
    int res = nGetDeviceState(this.address, device_state);
    if (res != 0) {
      if (res == -2147024868) {
        Arrays.fill(device_state, 0);
        acquire();
        return;
      } 
      throw new IOException("Failed to get device state (" + Integer.toHexString(res) + ")");
    } 
  }
  
  private final void setDataFormat(int flags) throws IOException {
    DIDeviceObject[] device_objects = new DIDeviceObject[this.objects.size()];
    this.objects.toArray((Object[])device_objects);
    int res = nSetDataFormat(this.address, flags, device_objects);
    if (res != 0)
      throw new IOException("Failed to set data format (" + Integer.toHexString(res) + ")"); 
  }
  
  public final String getProductName() {
    return this.product_name;
  }
  
  public final int getType() {
    return this.dev_type;
  }
  
  public final List getObjects() {
    return this.objects;
  }
  
  private final void enumEffects() throws IOException {
    int res = nEnumEffects(this.address, 0);
    if (res != 0)
      throw new IOException("Failed to enumerate effects (" + Integer.toHexString(res) + ")"); 
  }
  
  private final void addEffect(byte[] guid, int guid_id, int effect_type, int static_params, int dynamic_params, String name) {
    this.effects.add(new DIEffectInfo(this, guid, guid_id, effect_type, static_params, dynamic_params, name));
  }
  
  private final void enumObjects() throws IOException {
    int res = nEnumObjects(this.address, 31);
    if (res != 0)
      throw new IOException("Failed to enumerate objects (" + Integer.toHexString(res) + ")"); 
  }
  
  public final synchronized long[] getRangeProperty(int object_identifier) throws IOException {
    checkReleased();
    long[] range = new long[2];
    int res = nGetRangeProperty(this.address, object_identifier, range);
    if (res != 0)
      throw new IOException("Failed to get object range (" + res + ")"); 
    return range;
  }
  
  public final synchronized int getDeadzoneProperty(int object_identifier) throws IOException {
    checkReleased();
    return nGetDeadzoneProperty(this.address, object_identifier);
  }
  
  private final void addObject(byte[] guid, int guid_type, int identifier, int type, int instance, int flags, String name) throws IOException {
    Component.Identifier id = getIdentifier(guid_type, type, instance);
    int format_offset = this.current_format_offset++;
    DIDeviceObject obj = new DIDeviceObject(this, id, guid, guid_type, identifier, type, instance, flags, name, format_offset);
    this.objects.add(obj);
  }
  
  private static final Component.Identifier.Key getKeyIdentifier(int key_instance) {
    return DIIdentifierMap.getKeyIdentifier(key_instance);
  }
  
  private final Component.Identifier.Button getNextButtonIdentifier() {
    int button_id = this.button_counter++;
    return DIIdentifierMap.getButtonIdentifier(button_id);
  }
  
  private final Component.Identifier getIdentifier(int guid_type, int type, int instance) {
    switch (guid_type) {
      case 1:
        return Component.Identifier.Axis.X;
      case 2:
        return Component.Identifier.Axis.Y;
      case 3:
        return Component.Identifier.Axis.Z;
      case 4:
        return Component.Identifier.Axis.RX;
      case 5:
        return Component.Identifier.Axis.RY;
      case 6:
        return Component.Identifier.Axis.RZ;
      case 7:
        return Component.Identifier.Axis.SLIDER;
      case 10:
        return Component.Identifier.Axis.POV;
      case 9:
        return getKeyIdentifier(instance);
      case 8:
        return getNextButtonIdentifier();
    } 
    return Component.Identifier.Axis.UNKNOWN;
  }
  
  public final synchronized void setBufferSize(int size) throws IOException {
    checkReleased();
    unacquire();
    int res = nSetBufferSize(this.address, size);
    if (res != 0 && res != 1 && res != 2)
      throw new IOException("Failed to set buffer size (" + Integer.toHexString(res) + ")"); 
    this.queue = new DataQueue(size, DIDeviceObjectData.class);
    this.queue.position(this.queue.limit());
    acquire();
  }
  
  public final synchronized void setCooperativeLevel(int flags) throws IOException {
    checkReleased();
    int res = nSetCooperativeLevel(this.address, this.window.getHwnd(), flags);
    if (res != 0)
      throw new IOException("Failed to set cooperative level (" + Integer.toHexString(res) + ")"); 
  }
  
  public final synchronized void release() {
    if (!this.released) {
      this.released = true;
      for (int i = 0; i < this.rumblers.size(); i++) {
        IDirectInputEffect effect = this.rumblers.get(i);
        effect.release();
      } 
      nRelease(this.address);
    } 
  }
  
  private final void checkReleased() throws IOException {
    if (this.released)
      throw new IOException("Device is released"); 
  }
  
  protected void finalize() {
    release();
  }
  
  private static final native long nCreatePeriodicEffect(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int[] paramArrayOfint, long[] paramArrayOflong, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15) throws IOException;
  
  private static final native int nPoll(long paramLong) throws IOException;
  
  private static final native int nAcquire(long paramLong);
  
  private static final native int nUnacquire(long paramLong);
  
  private static final native int nGetDeviceData(long paramLong, int paramInt1, DataQueue paramDataQueue, Object[] paramArrayOfObject, int paramInt2, int paramInt3);
  
  private static final native int nGetDeviceState(long paramLong, int[] paramArrayOfint);
  
  private static final native int nSetDataFormat(long paramLong, int paramInt, DIDeviceObject[] paramArrayOfDIDeviceObject);
  
  private final native int nEnumEffects(long paramLong, int paramInt);
  
  private final native int nEnumObjects(long paramLong, int paramInt);
  
  private static final native int nGetRangeProperty(long paramLong, int paramInt, long[] paramArrayOflong);
  
  private static final native int nGetDeadzoneProperty(long paramLong, int paramInt) throws IOException;
  
  private static final native int nSetBufferSize(long paramLong, int paramInt);
  
  private static final native int nSetCooperativeLevel(long paramLong1, long paramLong2, int paramInt);
  
  private static final native void nRelease(long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\IDirectInputDevice.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */